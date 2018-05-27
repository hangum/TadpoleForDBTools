package com.hangum.tadpole.monitoring.core.editors.monitoring.manage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.Activator;
import com.hangum.tadpole.monitoring.core.Messages;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.AddMonitoringDialog;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.TemplateMonitoringManageDialog;
import com.hangum.tadpole.monitoring.core.dialogs.monitoring.UpdateMonitoringDialog;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine;

/**
 * Monitoring manage editor
 * 
 * @author hangum
 *
 */
public class MonitoringManagerEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringManagerEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.manage";
	
	private List<ManagerListDTO> treeList = new ArrayList<ManagerListDTO>();
	private TreeViewer treeVUserDB;
	
	// index
	private UserDBDAO userDB;
	private TableViewer tableVMonitoringList;
	private ToolItem tltmRefresh;
	private ToolItem tltmAdd;
	private ToolItem tltmModify;
	private ToolItem tltmRemove;
	
//	private ToolItem tltmAddTemplate;
	
	// result search
	private Combo comboResult;
	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;
	private TableViewer tvResult;
	
//	private Combo comboStatics;
//	private Text text;
//	private Text text_1;

	public MonitoringManagerEditor() {
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

		MonitoringManagerInput mmInput = (MonitoringManagerInput) input;
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
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		Composite compositeTop = new Composite(parent, SWT.NONE);
		compositeTop.setLayout(new GridLayout(1, false));
		
		ToolBar toolBarTop = new ToolBar(compositeTop, SWT.FLAT | SWT.RIGHT);
		toolBarTop.setBounds(0, 0, 88, 20);
		
		ToolItem tltmManageTemplate = new ToolItem(toolBarTop, SWT.NONE);
		tltmManageTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TemplateMonitoringManageDialog dialog = new TemplateMonitoringManageDialog(parent.getShell());
				dialog.open();
				
				reLoadDBList();
			}
		});
		tltmManageTemplate.setText("Manage Template");
		
		SashForm sashFormMain = new SashForm(parent, SWT.BORDER | SWT.VERTICAL);
		sashFormMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm sashFormTerm = new SashForm(sashFormMain, SWT.BORDER);
		
		Composite compositeLeft = new Composite(sashFormTerm, SWT.NONE);
		compositeLeft.setLayout(new GridLayout(1, false));
		
		ToolBar toolBarDB = new ToolBar(compositeLeft, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmDBRefresh = new ToolItem(toolBarDB, SWT.NONE);
		tltmDBRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reLoadDBList();
			}
		});
		tltmDBRefresh.setText("Refresh");
		
		treeVUserDB = new TreeViewer(compositeLeft, SWT.BORDER);
		Tree treeDatabase = treeVUserDB.getTree();
		treeDatabase.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeDatabase.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		treeVUserDB.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				if (is.getFirstElement() instanceof UserDBDAO) {
					userDB = (UserDBDAO) is.getFirstElement();
					
					if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
						refreshMonitoringIndex();
						initIndexBtn(true);
						initSearchResult();
					}
				}
			}
			
		});

		Composite compositeRight = new Composite(sashFormTerm, SWT.NONE);
		compositeRight.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeRight, SWT.FLAT | SWT.RIGHT);
		
		tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshMonitoringIndex();
			}
		});
		tltmRefresh.setText("Refresh");
		
		tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddMonitoringDialog dialog = new AddMonitoringDialog(null, userDB);
				dialog.open();
				refreshMonitoringIndex();	
			}
		});
		tltmAdd.setText("Add");
		
		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableVMonitoringList.getSelection();
				if(!iss.isEmpty()) {
					
					MonitoringIndexDAO monitoringIndexDao = (MonitoringIndexDAO)iss.getFirstElement();
					UpdateMonitoringDialog dialog = new UpdateMonitoringDialog(parent.getShell(), monitoringIndexDao);
					if(Dialog.OK == dialog.open()) {
						refreshMonitoringIndex();	
					}
				}
			}
		});
		tltmModify.setText("Modify");
		tltmModify.setEnabled(false);
		
		tltmRemove = new ToolItem(toolBar, SWT.NONE);
		tltmRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableVMonitoringList.getSelection();
				if(!iss.isEmpty()) {
					if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, "Do you want delete?")) return;
					
					MonitoringIndexDAO monitoringIndexDao = (MonitoringIndexDAO)iss.getFirstElement();
					try {
						TadpoleSystem_monitoring.deleteMonitoringIndex(monitoringIndexDao);
						refreshMonitoringIndex();
					} catch (Exception e1) {
						logger.error("delete monitoring index", e1);
					}
				}
			}
		});
		tltmRemove.setText("Remove");
		tltmRemove.setEnabled(false);
		
//		tltmAddTemplate = new ToolItem(toolBar, SWT.NONE);
//		tltmAddTemplate.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		tltmAddTemplate.setText("Add Template");
				
		tableVMonitoringList = new TableViewer(compositeRight, SWT.BORDER | SWT.FULL_SELECTION);
		tableVMonitoringList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(!event.getSelection().isEmpty()) {
					tltmModify.setEnabled(true);
					tltmRemove.setEnabled(true);
				}
			}
		});
		Table tableIndex = tableVMonitoringList.getTable();
		tableIndex.setHeaderVisible(true);
		tableIndex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createMonitoringColumn();
		
		tableVMonitoringList.setLabelProvider(new MonitoringIndexLabelProvider());
		tableVMonitoringList.setContentProvider(ArrayContentProvider.getInstance());
		
		sashFormTerm.setWeights(new int[] {3, 7});
		
		Composite compositeResult = new Composite(sashFormMain, SWT.NONE);
		compositeResult.setLayout(new GridLayout(1, false));
		
		Composite compositeResultSearch = new Composite(compositeResult, SWT.NONE);
		compositeResultSearch.setLayout(new GridLayout(9, false));
		compositeResultSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
//		Label lblStatics = new Label(compositeResultSearch, SWT.NONE);
//		lblStatics.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblStatics.setText("Statics");
//		
//		comboStatics = new Combo(compositeResultSearch, SWT.READ_ONLY);
//		comboStatics.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		comboStatics.setVisibleItemCount(5);
//		comboStatics.add("All");
//		comboStatics.add("5 Minute");
//		comboStatics.add("1 Hour");
//		comboStatics.add("1 Day");
//		comboStatics.add("1 Week");
//		comboStatics.add("1 Month");
//		comboStatics.select(0);
		
		Label lblNewLabel = new Label(compositeResultSearch, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Result");
		
		comboResult = new Combo(compositeResultSearch, SWT.READ_ONLY);
		for (MonitoringDefine.MONITORING_STATUS type : MonitoringDefine.MONITORING_STATUS.values()) {
			comboResult.add(type.getName());
		}
		comboResult.setVisibleItemCount(MonitoringDefine.MONITORING_STATUS.values().length);
		comboResult.select(1);
		
		Label lblStart = new Label(compositeResultSearch, SWT.NONE);
		lblStart.setText("start");
		
		dateTimeStart = new DateTime(compositeResultSearch, SWT.BORDER | SWT.DROP_DOWN);
		
		Label label = new Label(compositeResultSearch, SWT.NONE);
		label.setText("~");
		
		dateTimeEnd = new DateTime(compositeResultSearch, SWT.BORDER | SWT.DROP_DOWN);
		
		Button btnSearch = new Button(compositeResultSearch, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchResultData();
			}
		});
		btnSearch.setText("Search");
		
		CTabFolder tabFolderResult = new CTabFolder(compositeResult, SWT.NONE);
		tabFolderResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tbtmTable = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmTable.setText("Table");
		
		Composite compositeTable = new Composite(tabFolderResult, SWT.NONE);
		tbtmTable.setControl(compositeTable);
		compositeTable.setLayout(new GridLayout(1, false));
		
		tvResult = new TableViewer(compositeTable, SWT.BORDER | SWT.FULL_SELECTION);
		tvResult.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if(!sel.isEmpty()) {
//					MonitoringResultDAO dao = (MonitoringResultDAO)sel.getFirstElement();
//					ResultSetViewDialog dialog = new ResultSetViewDialog(null, dao);
//					dialog.open();	
				}
			}
		});
		Table tableResult = tvResult.getTable();
		tableResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableResult.setLinesVisible(true);
		tableResult.setHeaderVisible(true);
		
		createTableColumn(tvResult);

		tvResult.setContentProvider(ArrayContentProvider.getInstance());
		tvResult.setLabelProvider(new MonitoringResultLabelprovider());
		
		sashFormMain.setWeights(new int[] {3, 7});
		tabFolderResult.setSelection(0);
		
//		Composite compositeSummary = new Composite(compositeResult, SWT.NONE);
//		compositeSummary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeSummary.setLayout(new GridLayout(4, false));
//		
//		Label lblTotal = new Label(compositeSummary, SWT.NONE);
//		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblTotal.setText("Total");
//		
//		text_1 = new Text(compositeSummary, SWT.BORDER);
//		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Label lblAverage = new Label(compositeSummary, SWT.NONE);
//		lblAverage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblAverage.setText("Average");
//		
//		text = new Text(compositeSummary, SWT.BORDER);
//		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		initUI();
	}
	
	/**
	 * crate result column
	 */
	public void createTableColumn(TableViewer tvError) {
		String[] arryTable 	= {"Title", "Status", "Value", "User Descripton", "System Description", "Result", "Date"};
		int[] arryWidth 	= {120, 		65, 	80, 	200, 				200,				160, 		100};
	
		for(int i=0; i<arryTable.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvError, SWT.NONE);
			TableColumn tblclmnDbName = tableViewerColumn.getColumn();
			tblclmnDbName.setWidth(arryWidth[i]);
			tblclmnDbName.setText(arryTable[i]);
		}
	}
	
	/**
	 * 
	 */
	private void searchResultData() {
		IStructuredSelection iss = (IStructuredSelection)tableVMonitoringList.getSelection();
		if(!iss.isEmpty()) {
			MonitoringIndexDAO monitoringIndexDao = (MonitoringIndexDAO)iss.getFirstElement();
			
			Calendar cal = Calendar.getInstance();
			cal.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
			long startTime = cal.getTimeInMillis();
			cal.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
			long endTime = cal.getTimeInMillis();
			
			try {
				List<MonitoringResultDAO> listResult = TadpoleSystem_monitoring.getMonitoringResultHistory(monitoringIndexDao, comboResult.getText(), "", startTime, endTime);
				for (MonitoringResultDAO monitoringResultDAO : listResult) {
					monitoringResultDAO.setMonitoringIndexDAO(monitoringIndexDao);
				}
				tvResult.setInput(listResult);
				tvResult.refresh();
			} catch(Exception e) {
				logger.error("find monitoring result", e);
			}
		}
		
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		treeVUserDB.setContentProvider(new UserDBContentProvider());
		treeVUserDB.setLabelProvider(new UserDBLabelProvider());
		treeVUserDB.setInput(treeList);
		reLoadDBList();
		
		// end 
		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_YEAR, -1);
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		initIndexBtn(false);

		// google analytic
		AnalyticCaller.track(MonitoringManagerEditor.ID);
	}
	
	/**
	 * initialize index button
	 * 
	 * @param isEnable
	 */
	private void initIndexBtn(boolean isEnable) {
		tltmRefresh.setEnabled(isEnable);
		tltmAdd.setEnabled(isEnable);
		
//		tltmAddTemplate.setEnabled(isEnable);
	}
	
	/**
	 * initialize resultData
	 */
	private void initSearchResult() {
		List<MonitoringResultDAO> listResult = new ArrayList<>();
		tvResult.setInput(listResult);
	}
	
	/**
	 * refresh db list
	 */
	public void reLoadDBList() {
		try {
			treeList.clear();
			List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getSessionUserDB();
			// 그룹 이름을 생성합니다.
			List<String> groupNames = new ArrayList<String>();
			for (UserDBDAO userDBDAO : userDBS) {
				if(!groupNames.contains(userDBDAO.getGroup_name())) {
					groupNames.add(userDBDAO.getGroup_name());
				}
			}

			for (UserDBDAO userDBDAO : userDBS) {
				addUserDB(userDBDAO, false);
			}

		} catch (Exception e) {
			logger.error("initialize Managerview", e);

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(),CommonMessages.get().Error, "Can't load database.", errStatus); //$NON-NLS-1$
		}

		treeVUserDB.refresh();
		treeVUserDB.expandToLevel(2);
	}
	
	/**
	 * monitoring index 초기화
	 */
	private void refreshMonitoringIndex() {
		try {
			List<MonitoringIndexDAO> listMonitoringIndex = TadpoleSystem_monitoring.getUserMonitoringIndex(userDB);
			tableVMonitoringList.setInput(listMonitoringIndex);
			tableVMonitoringList.refresh();
			
			tltmModify.setEnabled(false);
			tltmRemove.setEnabled(false);
		} catch (Exception e) {
			logger.error("get UserMonitoring index", e);
		}
		
	}
	
	/**
	 * create monitoring column 
	 */
	private void createMonitoringColumn() {
		String[] arryTitle = {"Title", "KPI", "Method", "Type",  "Index Name", "Condition", "Condition Value", "After"};
		int[] arryWidth = 	 {120, 		70, 	50, 		130, 		130, 		70, 		70, 			150};
		
		for (int i=0; i<arryTitle.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableVMonitoringList, SWT.NONE);
			TableColumn tblclmnTitle = tableViewerColumn.getColumn();
			tblclmnTitle.setWidth(arryWidth[i]);
			tblclmnTitle.setText(arryTitle[i]);
		}
		
	}
	
	/**
	 * tree에 새로운 항목 추가
	 * 
	 * @param userDB
	 * @param defaultOpen
	 *            default editor open
	 */
	private void addUserDB(UserDBDAO userDB, boolean defaultOpen) {
		for (ManagerListDTO dto : treeList) {
			if (dto.getName().equals(userDB.getGroup_name())) {
				dto.addLogin(userDB);

				if (defaultOpen) {
					selectAndOpenView(userDB);
					treeVUserDB.expandToLevel(userDB, 2);
				}
				return;
			} // end if(dto.getname()....
		} // end for

		// 신규 그룹이면...
		ManagerListDTO managerDto = new ManagerListDTO(userDB.getGroup_name());
		managerDto.addLogin(userDB);
		treeList.add(managerDto);

		if (defaultOpen) {
			selectAndOpenView(userDB);
			treeVUserDB.expandToLevel(userDB, 2);
		}
	}
	
	/**
	 * 트리를 갱신하고 쿼리 창을 엽니다.
	 * 
	 * @param userDB
	 */
	private void selectAndOpenView(UserDBDAO userDB) {
		treeVUserDB.refresh();
		treeVUserDB.setSelection(new StructuredSelection(userDB), true);

		// mongodb 일경우 열지 않는다.
		if (userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
//			MainEditorInput mei = new MainEditorInput(dto);
//			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			try {
//				page.openEditor(mei, MainEditor.ID);
//			} catch (PartInitException e) {
//				logger.error("main editor open", e); //$NON-NLS-1$
//
//				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//				ExceptionDetailsErrorDialog.openError(getSite().getShell(),CommonMessages.get().Error, "Can't open resource. ", errStatus); //$NON-NLS-1$
//			}
		}
	}
	
	@Override
	public void setFocus() {
	}
}
