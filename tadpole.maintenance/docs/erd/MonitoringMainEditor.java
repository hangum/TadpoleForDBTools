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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.ResultSetViewDialog;
import com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite.ChartColorUtils;
import com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite.LineChartComposite;
import com.hangum.tadpole.monitoring.core.manager.cache.MonitoringCacheRepository;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_monitoring;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

/**
 * Monitoring main Editor
 * 
 * @author hangum
 *
 */
public class MonitoringMainEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringMainEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.main";
	
	private boolean isThread = true;
	private final ServerPushSession pushSession = new ServerPushSession();
	private TableViewer tvError;
	
	/** db color list */
	private Map<Integer, RGB> dbColorList = new HashMap<>();
	
	/* head title group */
	private Group grpIndexDescription;
	
	private List<MonitoringResultDAO> listMonitoringResult;
	private LineChartComposite compositeNetworkIn;
	private LineChartComposite compositeNetworkOut;
	private LineChartComposite compositeConnection;
	private TableViewer tvStatement;

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
		
		grpIndexDescription = new Group(parent, SWT.NONE);
		grpIndexDescription.setLayout(new RowLayout(SWT.HORIZONTAL));
		GridData gd_grpIndexDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_grpIndexDescription.heightHint = 50;
		gd_grpIndexDescription.minimumHeight = 50;
		grpIndexDescription.setLayoutData(gd_grpIndexDescription);
		grpIndexDescription.setText("Index Description");
		
		try {
			List<UserDBDAO> userDBS = TadpoleSystem_monitoring.getUserMonitoringDBList();
			for(int i=0; i<userDBS.size(); i++) {
				UserDBDAO userDBDAO = userDBS.get(i);
				
				Button btnAa = new Button(grpIndexDescription, SWT.NONE);
				btnAa.setText(userDBDAO.getDisplay_name());
//				btnAa.setSelection(true);
				
				RGB rgb = ChartColorUtils.getCat20Colors()[i];
				dbColorList.put(userDBDAO.getSeq(), rgb);
				btnAa.setBackground(SWTResourceManager.getColor(rgb));
			}
		} catch(Exception e) {
			logger.error("get userdb list", e);
		}
			
		SashForm sashFormBody = new SashForm(parent, SWT.VERTICAL);
		sashFormBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeChart = new Composite(sashFormBody, SWT.NONE);
		compositeChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeChart.setLayout(new GridLayout(2, false));
		
		Composite compositeChartLeft = new Composite(compositeChart, SWT.NONE);
		compositeChartLeft.setLayout(new GridLayout(1, false));
		GridData gd_compositeChartLeft = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_compositeChartLeft.minimumWidth = 300;
		gd_compositeChartLeft.widthHint = 300;
		compositeChartLeft.setLayoutData(gd_compositeChartLeft);
		
//		compositeNetworkIn = new LineChartComposite(compositeChartLeft, dbColorList, "Network In", "(Byea)");
//		compositeNetworkIn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		compositeNetworkIn.setLayout(new GridLayout(1, false));
//		
//		compositeNetworkOut = new LineChartComposite(compositeChartLeft, dbColorList, "Network Out", "(Byte)");
//		compositeNetworkOut.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		compositeNetworkOut.setLayout(new GridLayout(1, false));
//		
//		compositeConnection = new LineChartComposite(compositeChartLeft, dbColorList, "Client Connection", "(EA)");
//		compositeConnection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		compositeConnection.setLayout(new GridLayout(1, false));
		
		Composite compositeCenter = new Composite(compositeChart, SWT.NONE);
		compositeCenter.setLayout(new GridLayout(1, false));
		compositeCenter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		Group grpStatementCount = new Group(compositeCenter, SWT.NONE);
//		grpStatementCount.setLayout(new GridLayout(1, false));
//		grpStatementCount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		grpStatementCount.setText("Statement Count");
//		
//		tvStatement = new TableViewer(grpStatementCount, SWT.BORDER | SWT.FULL_SELECTION);
//		Table table = tvStatement.getTable();
//		table.setHeaderVisible(true);
//		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		createTVStatement();

		Group compositeError = new Group(sashFormBody, SWT.NONE);
		compositeError.setLayout(new GridLayout(1, false));
		compositeError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeError.setText("Live Error List");
		
//		ToolBar toolBarError = new ToolBar(compositeError, SWT.FLAT | SWT.RIGHT);

//		ToolItem tltmStop = new ToolItem(toolBarError, SWT.NONE);
//		tltmStop.setText("Stop");

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
	 * create table viewer statement
	 */
	private void createTVStatement() {
		String[] arryTable = {"DB Name", "SELECT", "INSERT", "UPDATE", "CREATE", "ALTER", "DROP"};
		int[] arryWidth = {120, 60, 60, 60, 60, 60, 60};
		
		crateTableColumn(tvStatement, arryTable, arryWidth);
	}
	
	/**
	 * crate result column
	 */
	public void createTableColumn(TableViewer tvError) {
		String[] arryTable = {"DB Name", "is Error", "Title", "Value", "Condition", "Result"};
		int[] arryWidth = {120, 50, 120, 60, 100, 500};
	
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
						for (MonitoringResultDAO monitoringResultDAO : listMonitoringResult) {
							if(PublicTadpoleDefine.YES_NO.YES.toString().equals(monitoringResultDAO.getResult())) listErrorMonitoringResult.add(monitoringResultDAO);
						}
						
						final List<MonitoringResultDAO> listNetworkIn = new ArrayList<MonitoringResultDAO>();
						final List<MonitoringResultDAO> listNetworkOut = new ArrayList<MonitoringResultDAO>();
						final List<MonitoringResultDAO> listConnection = new ArrayList<MonitoringResultDAO>();
						
						for (MonitoringResultDAO monitoringResultDAO : listMonitoringResult) {
							if("NETWORK_IN".equals(monitoringResultDAO.getMonitoring_type())) 	listNetworkIn.add(monitoringResultDAO);
							if("NETWORK_OUT".equals(monitoringResultDAO.getMonitoring_type())) 	listNetworkOut.add(monitoringResultDAO);
							if("CONNECTION".equals(monitoringResultDAO.getMonitoring_type())) 	listConnection.add(monitoringResultDAO);
						}
						
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (!tvError.getTable().isDisposed()) {
									tvError.setInput(listErrorMonitoringResult);
									tvError.refresh();
									
//									compositeNetworkIn.addRowData(listNetworkIn, true);
//									compositeNetworkOut.addRowData(listNetworkOut, true);
//									compositeConnection.addRowData(listConnection, false);
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