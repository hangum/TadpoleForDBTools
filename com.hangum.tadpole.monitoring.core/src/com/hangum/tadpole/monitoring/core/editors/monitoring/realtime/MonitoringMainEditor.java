package com.hangum.tadpole.monitoring.core.editors.monitoring.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.ResultSetViewDialog;
import com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite.DBStatusComposite;
import com.hangum.tadpole.monitoring.core.manager.cache.MonitoringCacheRepository;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_monitoring;
import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

/**
 * Monitoring main Editor
 * 
 * @author hangum
 *
 */
public class MonitoringMainEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringMainEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.main";
	
	private Map<Integer, DBStatusComposite> mapDBComposite = new HashMap<>();
	
	
	public static int STATUS_COLOR_CLEAN 	= SWT.COLOR_DARK_GREEN;
	public static int STATUS_COLOR_WARNING = SWT.COLOR_DARK_GRAY;
	public static int STATUS_COLOR_CRITICAL = SWT.COLOR_RED;
	
	
	private boolean isThread = true;
	private final ServerPushSession pushSession = new ServerPushSession();
	private TableViewer tvError;
	
//	/** db color list */
//	private Map<Integer, RGB> dbColorList = new HashMap<>();
	
	/* head title group */
	private Group grpDatabaseList;
	private List<MonitoringResultDAO> listMonitoringResult;
	
	private boolean boolTltmErrorIsPopup = true;

	public MonitoringMainEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		MonitoringMainInput mmInput = (MonitoringMainInput) input;
		setPartName(mmInput.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeStatus = new Composite(parent, SWT.NONE);
		compositeStatus.setLayout(new GridLayout(3, false));
		compositeStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		Button btnClean = new Button(compositeStatus, SWT.NONE);
		btnClean.setBounds(0, 0, 95, 28);
		btnClean.setText("Clean");
		btnClean.setBackground(SWTResourceManager.getColor(MonitoringMainEditor.STATUS_COLOR_CLEAN));
		
		Button btnWarring = new Button(compositeStatus, SWT.NONE);
		btnWarring.setBounds(0, 0, 95, 28);
		btnWarring.setText("Warring");
		btnWarring.setBackground(SWTResourceManager.getColor(MonitoringMainEditor.STATUS_COLOR_WARNING));
		
		Button btnCritical = new Button(compositeStatus, SWT.NONE);
		btnCritical.setBounds(0, 0, 95, 28);
		btnCritical.setText("Critical");
		btnCritical.setBackground(SWTResourceManager.getColor(MonitoringMainEditor.STATUS_COLOR_CRITICAL));
		
		SashForm sashFormBody = new SashForm(parent, SWT.VERTICAL);
		sashFormBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		grpDatabaseList = new Group(sashFormBody, SWT.NONE);
		GridData gd_grpIndexDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		grpDatabaseList.setLayoutData(gd_grpIndexDescription);
		grpDatabaseList.setText("Database List");
		
		try {
			List<UserDBDAO> userDBS = TadpoleSystem_monitoring.getUserMonitoringDBList();
			RowLayout rl_grpDatabaseList = new RowLayout(SWT.HORIZONTAL);
			grpDatabaseList.setLayout(rl_grpDatabaseList);
			for(int i=0; i<userDBS.size(); i++) {
				UserDBDAO userDBDAO = userDBS.get(i);
				
				DBStatusComposite dbComposite = new DBStatusComposite(grpDatabaseList, SWT.NONE, userDBDAO); 
				dbComposite.setLayoutData(new RowData(75, 125));
				
				mapDBComposite.put(userDBDAO.getSeq(), dbComposite);
			}
		} catch(Exception e) {
			logger.error("get monitoring db list", e);
		}
		
		// live error list
		Group compositeError = new Group(sashFormBody, SWT.NONE);
		compositeError.setLayout(new GridLayout(1, false));
		compositeError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeError.setText("Live Error List");
		
		ToolBar toolBarError = new ToolBar(compositeError, SWT.FLAT | SWT.RIGHT);
		final ToolItem tltmErrorIsPopup = new ToolItem(toolBarError, SWT.CHECK);
		tltmErrorIsPopup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tltmErrorIsPopup.getSelection()) boolTltmErrorIsPopup = true;
				else boolTltmErrorIsPopup = false;
			}
		});
		tltmErrorIsPopup.setText("Error is popup Dialog?");
		tltmErrorIsPopup.setSelection(true);

		tvError = new TableViewer(compositeError, SWT.BORDER | SWT.FULL_SELECTION);
		tvError.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if(!sel.isEmpty()) {
					MonitoringResultDAO dao = (MonitoringResultDAO)sel.getFirstElement();
					ResultSetViewDialog dialog = new ResultSetViewDialog(null, dao);
					dialog.open();	
				}
			}
		});
		Table tableError = tvError.getTable();
		tableError.setHeaderVisible(true);
		tableError.setLinesVisible(true);
		tableError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTableColumn(tvError);

		tvError.setContentProvider(new ArrayContentProvider());
		tvError.setLabelProvider(new MonitoringErrorLabelprovider());

		sashFormBody.setWeights(new int[] { 7, 3 });

		callbackui();
	}
	
	/**
	 * crate result column
	 */
	public void createTableColumn(TableViewer tvError) {
		String[] arryTable = {"Date", "DB Name", "Title", "Value", "Condition", "Result"};
		int[] arryWidth = {120, 150, 120, 60, 100, 500};
	
		crateTableColumn(tvError, arryTable, arryWidth);
	}
	
	/**
	 * create table viewer column
	 * 
	 * @param tv
	 * @param arryTable
	 * @param arryWidth
	 */
	private void crateTableColumn(TableViewer tv, String[] arryTable, int[] arryWidth) {
		for(int i=0; i<arryTable.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tv, SWT.NONE);
			TableColumn tblclmnDbName = tableViewerColumn.getColumn();
			tblclmnDbName.setWidth(arryWidth[i]);
			tblclmnDbName.setText(arryTable[i]);
		}
	}

	private void callbackui() {
		pushSession.start();
		Thread thread = new Thread(startUIThread());
		thread.setDaemon(true);
		thread.start();
	}

	private Runnable startUIThread() {
		final String email = SessionManager.getEMAIL();
		final Display display = PlatformUI.getWorkbench().getDisplay();// tvError.getTable().getDisplay();
		final MonitoringCacheRepository instance = MonitoringCacheRepository.getInstance();

		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {

				while(isThread) {
					listMonitoringResult = instance.get(email);
					if(null != listMonitoringResult) {
						
						/** collect error data */
						final List<MonitoringResultDAO> listErrorMonitoringResult = new ArrayList<MonitoringResultDAO>();
						final Map<Integer, Integer> mapWaringCnt = new HashMap<Integer, Integer>();
						final Map<Integer, Integer> mapCriticalCnt = new HashMap<Integer, Integer>();
						
						for (MonitoringResultDAO resultDAO : listMonitoringResult) {
							if(PublicTadpoleDefine.YES_NO.YES.toString().equals(resultDAO.getResult())) {
								listErrorMonitoringResult.add(resultDAO);
								
								if(mapWaringCnt.containsKey(resultDAO.getDb_seq())) mapWaringCnt.put(resultDAO.getDb_seq(), mapWaringCnt.get(resultDAO.getDb_seq()) + 1);
								else mapWaringCnt.put(resultDAO.getDb_seq(), 1);
							}
						}
						
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (!tvError.getTable().isDisposed()) {
									tvError.setInput(listErrorMonitoringResult);
									tvError.refresh();

									if(boolTltmErrorIsPopup) {
										// 에러 수만큼 팝업을 보여줍니다.
										for (MonitoringResultDAO monitoringResultDAO : listErrorMonitoringResult) {
											ResultSetViewDialog dialog = new ResultSetViewDialog(null, monitoringResultDAO);
											dialog.open();
										}
									}
									
									for (Integer key : mapWaringCnt.keySet()) {
										DBStatusComposite dbComposite = mapDBComposite.get(key);
										dbComposite.changeStatus(STATUS_COLOR_WARNING, ""+mapWaringCnt.get(key));
									}
									
								}
							}
						});
						
						// 20 seconds
						try{ Thread.sleep(999 * 10); } catch(Exception e) {}
					}
				}
			};
		};

		return bgRunnable;
	}
	
	@Override
	public void dispose() {
		super.dispose();

		isThread = false;
		pushSession.stop();
	}

	@Override
	public void setFocus() {
	}
}