package com.hangum.tadpole.monitoring.core.editors.monitoring.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringDashboardDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.Activator;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.MonitoringDetailStatusDialog;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.ResultSetViewDialog;
import com.hangum.tadpole.monitoring.core.editors.monitoring.manage.MonitoringManagerEditor;
import com.hangum.tadpole.monitoring.core.editors.monitoring.manage.MonitoringManagerInput;
import com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite.DBStatusComposite;
import com.hangum.tadpole.monitoring.core.manager.cache.MonitoringCacheRepository;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.SWTResourceManager;

/**
 * Monitoring main Editor
 * 
 * @author hangum
 *
 */
public class MonitoringMainEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringMainEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.main";
	
	private List<UserDBDAO> listUserMonitoringDB;
	String searchKey = "";
	
	private Map<Integer, DBStatusComposite> mapDBComposite = new HashMap<>();
	
	private boolean isThread = true;
	private final ServerPushSession pushSession = new ServerPushSession();
	private TableViewer tvError;
	
	/* head title group */
	private Group grpDatabaseList;
	private boolean boolTltmErrorIsPopup = false;

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
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeStatus = new Composite(parent, SWT.NONE);
		compositeStatus.setLayout(new GridLayout(6, false));
		compositeStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnManageMonitoring = new Button(compositeStatus, SWT.NONE);
		btnManageMonitoring.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
				try {
					MonitoringManagerInput input = new MonitoringManagerInput();
					page.openEditor(input, MonitoringManagerEditor.ID, false);				
					
				} catch (PartInitException ee) {
					logger.error("Does not open monitoring manager", ee);
					
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, ee.getMessage(), ee); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "Open monitoring manager", errStatus); //$NON-NLS-1$
				}
			}
		});
		btnManageMonitoring.setText("Manage");
		
		final Button btnAutoPopupDialog = new Button(compositeStatus, SWT.CHECK);
		btnAutoPopupDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnAutoPopupDialog.getSelection()) boolTltmErrorIsPopup = true;
				else boolTltmErrorIsPopup = false;
			}
		});
		btnAutoPopupDialog.setText("Auto popup Dialog");
		
		Label lblNewLabel = new Label(compositeStatus, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnClean = new Button(compositeStatus, SWT.NONE);
		btnClean.setBounds(0, 0, 95, 28);
		btnClean.setText("Clean");
		btnClean.setBackground(SWTResourceManager.getColor(MonitoringDefine.MONITORING_STATUS.CLEAN.getColor()));
		
		Button btnWarring = new Button(compositeStatus, SWT.NONE);
		btnWarring.setBounds(0, 0, 95, 28);
		btnWarring.setText("Warring");
		btnWarring.setBackground(SWTResourceManager.getColor(MonitoringDefine.MONITORING_STATUS.WARRING.getColor()));
		
		Button btnCritical = new Button(compositeStatus, SWT.NONE);
		btnCritical.setBounds(0, 0, 95, 28);
		btnCritical.setText("Critical");
		btnCritical.setBackground(SWTResourceManager.getColor(MonitoringDefine.MONITORING_STATUS.CRITICAL.getColor()));
		
		SashForm sashFormBody = new SashForm(parent, SWT.VERTICAL);
		sashFormBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		grpDatabaseList = new Group(sashFormBody, SWT.NONE);
		GridData gd_grpIndexDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		grpDatabaseList.setLayoutData(gd_grpIndexDescription);
		grpDatabaseList.setText("Database List");
		
		try {
			listUserMonitoringDB = TadpoleSystem_monitoring.getUserMonitoringDBList();
			RowLayout rl_grpDatabaseList = new RowLayout(SWT.HORIZONTAL);
			grpDatabaseList.setLayout(rl_grpDatabaseList);
			for(int i=0; i<listUserMonitoringDB.size(); i++) {
				UserDBDAO userDBDAO = listUserMonitoringDB.get(i);
				
				DBStatusComposite dbComposite = new DBStatusComposite(grpDatabaseList, SWT.NONE, userDBDAO); 
				dbComposite.setLayoutData(new RowData(80, 125));
				
				mapDBComposite.put(userDBDAO.getSeq(), dbComposite);
				
				if(i != (listUserMonitoringDB.size()-1)) searchKey += userDBDAO.getSeq() + ",";
				else searchKey += userDBDAO.getSeq();
			}
			
		} catch(Exception e) {
			logger.error("get monitoring db list", e);
		}
		
		// live error list
		Group compositeError = new Group(sashFormBody, SWT.NONE);
		compositeError.setLayout(new GridLayout(1, false));
		compositeError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeError.setText("Error List");

		tvError = new TableViewer(compositeError, SWT.BORDER | SWT.FULL_SELECTION);
		tvError.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if(!sel.isEmpty()) {
					MonitoringDashboardDAO dao = (MonitoringDashboardDAO)sel.getFirstElement();
					MonitoringDetailStatusDialog dialog = new MonitoringDetailStatusDialog(parent.getShell(), dao);
					dialog.open();	
				}
			}
		});
		Table tableError = tvError.getTable();
		tableError.setHeaderVisible(true);
		tableError.setLinesVisible(true);
		tableError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTableColumn(tvError);

		tvError.setContentProvider(ArrayContentProvider.getInstance());
		tvError.setLabelProvider(new MonitoringErrorLabelprovider());

		sashFormBody.setWeights(new int[] { 7, 3 });

		callbackui();
	}
	
	/**
	 * crate result column
	 */
	public void createTableColumn(TableViewer tvError) {
		String[] arryTable 	= {"Start Date", "DB Name", 	"KPI", "Title", "Description", "Warring", "Critical"};
		int[] arryWidth 	= {120, 			150,  		85, 	120, 		200,			60, 			60};
	
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
		
		final Display display = PlatformUI.getWorkbench().getDisplay();// tvError.getTable().getDisplay();
		final MonitoringCacheRepository instance = MonitoringCacheRepository.getInstance();
		
		final List<MonitoringDashboardDAO> listMonitoringResult = new ArrayList<MonitoringDashboardDAO>();
		
		final String email = SessionManager.getEMAIL();
		final List<MonitoringResultDAO> liveMonitoringResult = new ArrayList<MonitoringResultDAO>();
		
		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {
				
				while(isThread) {
					
					try {
						listMonitoringResult.clear();
						listMonitoringResult.addAll(TadpoleSystem_monitoring.getMonitoringErrorStatus(searchKey));
					} catch (Exception e1) {
						logger.error("Get monitoring dashboard data", e1);
					}
							
					liveMonitoringResult.clear();
					List<MonitoringResultDAO> listLive = instance.get(email);
					if(listLive != null) liveMonitoringResult.addAll(listLive);
						
					/** collect live error data */
					final List<MonitoringResultDAO> listErrorMonitoringResult = new ArrayList<MonitoringResultDAO>();
					for (MonitoringResultDAO resultDAO : liveMonitoringResult) {
						if(MonitoringDefine.MONITORING_STATUS.WARRING.toString().equals(resultDAO.getResult())) {
							listErrorMonitoringResult.add(resultDAO);
						} else if(MonitoringDefine.MONITORING_STATUS.CRITICAL.toString().equals(resultDAO.getResult())) {
							listErrorMonitoringResult.add(resultDAO);
						}
					}
					
					// collect old error data
					final Map<Integer, Integer> mapWarning = new HashMap<Integer, Integer>();
					final Map<Integer, Integer> mapCritical = new HashMap<Integer, Integer>();
					for (MonitoringDashboardDAO mrDAO : listMonitoringResult) {
						final int dbSeq 	= mrDAO.getDb_seq();
						final int warnCnt = mrDAO.getWarring_cnt();
						final int criCnt = mrDAO.getCritical_cnt();
						
						if(mapWarning.containsKey(dbSeq)) mapWarning.put(dbSeq, mapWarning.get(dbSeq) + warnCnt);
						else mapWarning.put(dbSeq, warnCnt);
						
						if(mapCritical.containsKey(dbSeq)) mapCritical.put(dbSeq, mapCritical.get(dbSeq) + criCnt);
						else mapCritical.put(dbSeq, criCnt);
					}
					
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							if (!tvError.getTable().isDisposed()) {

								// live message
								if(boolTltmErrorIsPopup) {
									// 에러 수만큼 팝업을 보여줍니다.
									for (MonitoringResultDAO monitoringResultDAO : listErrorMonitoringResult) {
										ResultSetViewDialog dialog = new ResultSetViewDialog(null, monitoringResultDAO);
										dialog.open();
									}
								}
								
								// All monitoring db state to clean
								for (Integer key : mapDBComposite.keySet()) {
									DBStatusComposite dbComposite = mapDBComposite.get(key);
									dbComposite.changeStatus(MonitoringDefine.MONITORING_STATUS.CLEAN.getColor(), "");	
								}
								
								// total message
								for (Integer key : mapWarning.keySet()) {
									DBStatusComposite dbComposite = mapDBComposite.get(key);
									dbComposite.changeStatus(MonitoringDefine.MONITORING_STATUS.WARRING.getColor(), "");//+mapWarning.get(key));
								}
								
								for (Integer key : mapCritical.keySet()) {
									DBStatusComposite dbComposite = mapDBComposite.get(key);
									
									if(mapCritical.get(key) != 0) dbComposite.changeStatus(MonitoringDefine.MONITORING_STATUS.CRITICAL.getColor(), "");//+mapCritical.get(key));
								}

								tvError.setInput(listMonitoringResult);
								tvError.refresh();
							}	// end tvError
						}
					});
					
					// 10 seconds
					try{ Thread.sleep(1000 * 10); } catch(Exception e) {}

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