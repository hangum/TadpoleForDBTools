/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.UnicodeUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.editor.core.rdb.texteditor.EditorExtension;
import com.hangum.tadpole.editor.core.rdb.texteditor.function.EditorBrowserFunctionService;
import com.hangum.tadpole.editor.core.utils.TadpoleEditorUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.editors.main.function.MainEditorBrowserFunctionService;
import com.hangum.tadpole.rdb.core.editors.main.sub.MainEditorHelper;
import com.hangum.tadpole.rdb.core.util.CubridExecutePlanUtils;
import com.hangum.tadpole.rdb.core.util.OracleExecutePlanUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.sql.dialog.save.ResourceSaveDialog;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.template.DBOperationType;
import com.hangum.tadpole.sql.util.PartQueryUtil;
import com.hangum.tadpole.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.sql.util.ResultSetUtils;
import com.hangum.tadpole.sql.util.SQLUtil;
import com.hangum.tadpole.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.sql.util.tables.SQLHistoryCreateColumn;
import com.hangum.tadpole.sql.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLHistorySorter;
import com.hangum.tadpole.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * 쿼리 수행 및 검색 창.
 * 
 * 에디터는 org.eclipse.orion.client-20120810-1752 로 작업.
 * 
 * @author hangum
 *
 */
public class MainEditor extends EditorExtension {
	/** Editor ID. */
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.main"; //$NON-NLS-1$
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(MainEditor.class);
	
	// toolbar auto commit
	private ToolItem tltmAutoCommit = null;
	private ToolItem tltmAutoCommitCommit = null;
	private ToolItem tltmAutoCommitRollback = null;
	// 
	
	/** resource 정보. */
	private UserDBResourceDAO dBResource;
	/** first save UserDBResource object */
	private UserDBResourceDAO userSetDBResource; //$NON-NLS-1$
	
	/** save mode */
	private boolean isFirstLoad = false;
	private boolean isDirty = false;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	/** 현 쿼리의 실행모드. */
	private PublicTadpoleDefine.QUERY_MODE queryMode = PublicTadpoleDefine.QUERY_MODE.DEFAULT;
	
	/** edior가 초기화 될때 처음 로드 되어야 하는 String. */
	private String initDefaultEditorStr;
	
	/** query 결과의 컬럼 정보 HashMap -- table의 헤더를 생성하는 용도 <column index, Data> */
	private Map<Integer, String> mapColumns = null;
	/** query 결과 column, type 정보를 가지고 있습니다 */
	private Map<Integer, Integer> mapColumnType = new HashMap<Integer, Integer>();
	
	/** query 의 결과 데이터  -- table의 데이터를 표시하는 용도 <column index, Data> */
	private List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer, Object>>();
		
	/** 이전 버튼 */
	private Button btnPrev;
	/** 이후 버튼 */
	private Button btnNext;
		
	/** 페이지 로케이션 */
	private int pageNumber = 1;
	
	/** 결과 filter */
	private Text textFilter;
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	private Label sqlResultStatusLabel;
	private TableViewer sqlResultTableViewer;
	private Table tableResult;
	
	/** query 결과 창 */
	private CTabFolder tabFolderResult;
	/** tab index name */
	private enum RESULT_TAB_NAME {RESULT_SET, SQL_RECALL, TADPOLE_MESSAGE};
	
	/** 쿼리결과 export */
	private Button btnSQLResultExport; 
	
	/** query history */
	private TableViewer tableViewerSQLHistory;
	private Table tableSQLHistory;
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	private Text textHistoryFilter;
	
	/** tadpole message */
	private TableViewer tableViewerMessage;
	private Table tableMessage;
	private List<TadpoleMessageDAO> listMessage = new ArrayList<TadpoleMessageDAO>();
	    
    /** content download를 위한 더미 composite */
    private Composite compositeDumy;
    
	public MainEditor() {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		MainEditorInput qei = (MainEditorInput)input;
		userDB = qei.getUserDB();
		strRoleType = SessionManager.getRoleType(userDB.getGroup_seq());
		
		dBResource = qei.getResourceDAO();
		if(dBResource == null) {
			setPartName(qei.getName());
			
			// fix : https://github.com/hangum/TadpoleForDBTools/issues/237
			initDefaultEditorStr = qei.getDefaultStr();
			if(!"".equals(initDefaultEditorStr)) {
				isFirstLoad = true;	
			}
		} else {
			setPartName(dBResource.getName());
			isFirstLoad = true;
			initDefaultEditorStr = qei.getDefaultStr();
		}
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setSashWidth(1);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		final Composite compositeEditor = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeEditor = new GridLayout(1, false);
		gl_compositeEditor.verticalSpacing = 0;
		gl_compositeEditor.horizontalSpacing = 0;
		gl_compositeEditor.marginHeight = 0;
		gl_compositeEditor.marginWidth = 0;
		compositeEditor.setLayout(gl_compositeEditor);
		
		// Shortcut prefix
		String prefixOSShortcut = ShortcutPrefixUtils.getCtrlShortcut();
		
		ToolBar toolBar = new ToolBar(compositeEditor, SWT.NONE | SWT.FLAT | SWT.RIGHT);
		toolBar.setToolTipText(String.format(Messages.MainEditor_toolBar_toolTipText, prefixOSShortcut));
		
		ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png")); //$NON-NLS-1$
		tltmConnectURL.setToolTipText("Connection Information"); //$NON-NLS-1$
		
		if(PermissionChecker.isShow(SessionManager.getRepresentRole())) {
			if(DBDefine.getDBDefine(userDB) == DBDefine.SQLite_DEFAULT ) {
				String fileName = new File(userDB.getDb()).getName();			
				tltmConnectURL.setText("Connect [ " + fileName + " ]"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				tltmConnectURL.setText("Connect [ " +  userDB.getHost() + ":" + userDB.getUsers() + " ]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}	
		} else {
			tltmConnectURL.setText("Connect Information"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(String.format(Messages.MainEditor_tltmExecute_toolTipText_1, prefixOSShortcut));
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query.png")); //$NON-NLS-1$
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_QUERY_FUNCTION);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecuteAll = new ToolItem(toolBar, SWT.NONE);
		tltmExecuteAll.setToolTipText(Messages.MainEditor_tltmExecuteAll_text);
		tltmExecuteAll.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query-all.png")); //$NON-NLS-1$
		tltmExecuteAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_QUERY_ALL_FUNCTION);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
	
		ToolItem tltmExplainPlanctrl = new ToolItem(toolBar, SWT.NONE);
		tltmExplainPlanctrl.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/execute_plan.png")); //$NON-NLS-1$
		tltmExplainPlanctrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_PLAN_FUNCTION);
			}
		});
		tltmExplainPlanctrl.setToolTipText(String.format(Messages.MainEditor_3, prefixOSShortcut));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSort = new ToolItem(toolBar, SWT.NONE);
		tltmSort.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/query_format.png")); //$NON-NLS-1$
		tltmSort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_FORMAT_FUNCTION);
			}
		});
		tltmSort.setToolTipText(String.format(Messages.MainEditor_4, prefixOSShortcut));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSQLToApplication = new ToolItem(toolBar, SWT.NONE);
		tltmSQLToApplication.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql_to_applications.png")); //$NON-NLS-1$
		tltmSQLToApplication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SQL_TO_APPLICATION);
			}
		});
	    tltmSQLToApplication.setToolTipText("SQL statement to Application code"); //$NON-NLS-1$
	    new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png")); //$NON-NLS-1$
		tltmDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browserEvaluate(EditorBrowserFunctionService.JAVA_DOWNLOAD_SQL);
			}
		});
		tltmDownload.setToolTipText("Download SQL"); //$NON-NLS-1$
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmAutoCommit = new ToolItem(toolBar, SWT.CHECK);
		tltmAutoCommit.setSelection(false);
		tltmAutoCommit.setText("Auto Commit"); //$NON-NLS-1$
		tltmAutoCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initAutoCommitAction(true);
			}
		});
		
		tltmAutoCommitCommit = new ToolItem(toolBar, SWT.NONE);
		tltmAutoCommitCommit.setSelection(false);
		tltmAutoCommitCommit.setText("Commit"); //$NON-NLS-1$
		tltmAutoCommitCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set commit][user id]" + strUserEMail + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.commit(strUserEMail, userDB);
			}
		});
		
		tltmAutoCommitRollback = new ToolItem(toolBar, SWT.NONE);
		tltmAutoCommitRollback.setSelection(false);
		tltmAutoCommitRollback.setText("Rollback"); //$NON-NLS-1$
		tltmAutoCommitRollback.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set rollback][user id]" + strUserEMail + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.rollback(strUserEMail, userDB);
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmHelp = new ToolItem(toolBar, SWT.NONE);
		tltmHelp.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/about.png")); //$NON-NLS-1$
		tltmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
				dialog.open();
			}
		});
		tltmHelp.setToolTipText(String.format(Messages.MainEditor_27, prefixOSShortcut));
		
	    ////// tool bar end ///////////////////////////////////////////////////////////////////////////////////
	    
	    ////// orion editor start /////////////////////////////////////////////////////////////////////////////
	    browserQueryEditor = new Browser(compositeEditor, SWT.BORDER);
	    browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));	    
	    
	    addBrowserService();	    
	    
//		createStatusLine();
	    ////// orion editor end /////////////////////////////////////////////////////////////////////////////
		
		Composite compositeResult = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
		compositeResult.setLayout(gl_compositeResult);
		
		tabFolderResult = new CTabFolder(compositeResult, SWT.NONE);
		tabFolderResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolderResult.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());		
		
		// tab 의 index를 설정한다.
		tabFolderResult.setData(RESULT_TAB_NAME.RESULT_SET.toString(), 0);
		tabFolderResult.setData(RESULT_TAB_NAME.SQL_RECALL.toString(), 1);
		tabFolderResult.setData(RESULT_TAB_NAME.TADPOLE_MESSAGE.toString(), 2);
		
		CTabItem tbtmResult = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmResult.setText(Messages.MainEditor_7);
		
		Composite compositeQueryResult = new Composite(tabFolderResult, SWT.NONE);
		tbtmResult.setControl(compositeQueryResult);
		compositeQueryResult.setLayout(new GridLayout(2, false));
		
		Label lblFilter = new Label(compositeQueryResult, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.MainEditor_lblFilter_text);
		
		textFilter = new Text(compositeQueryResult,SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		sqlResultTableViewer = new TableViewer(compositeQueryResult, SWT.BORDER | SWT.SINGLE);
		sqlResultTableViewer.setUseHashlookup(true);
		tableResult = sqlResultTableViewer.getTable();
		tableResult.addListener(SWT.MouseDoubleClick, new Listener() {
		    public void handleEvent(Event event) {
		    	TableItem[] selection = tableResult.getSelection();
				if (selection.length != 1) return;

				TableItem item = tableResult.getSelection()[0];
				for (int i=0; i<tableResult.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						String strText = item.getText(i);
						if(strText == null || "".equals(strText)) return;
						strText = RDBTypeToJavaTypeUtils.isNumberType(mapColumnType.get(i))? (" " + strText + ""): (" '" + strText + "'");
						
						appendTextAtPosition(strText);

//		                TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(getSite().getShell(), tableResult.getColumn(i).getText(), msg);
//		                dlg.open();
					}
				}
		    }
		});
		
		tableResult.setLinesVisible(true);
		tableResult.setHeaderVisible(true);
		tableResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		sqlFilter.setTable(tableResult);
		
		Composite compositeBtn = new Composite(compositeQueryResult, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(5, false);
		gl_compositeBtn.marginWidth = 3;
		gl_compositeBtn.marginHeight = 0;
		compositeBtn.setLayout(gl_compositeBtn);
		
		btnPrev = new Button(compositeBtn, SWT.NONE);
		btnPrev.setEnabled(false);
		btnPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnPrev();
			}
		});
		btnPrev.setText(Messages.MainEditor_8);
		
		btnNext = new Button(compositeBtn, SWT.NONE);
		btnNext.setEnabled(false);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnNext();
			}
		});
		btnNext.setText(Messages.MainEditor_9);
		
		compositeDumy = new Composite(compositeBtn, SWT.NONE);
		compositeDumy.setLayout(new GridLayout(1, false));
		compositeDumy.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnSQLResultExport = new Button(compositeBtn, SWT.NONE);
		btnSQLResultExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 분리자 정보를 가져옵니다.
				StringBuffer sbExportData = new StringBuffer();
					
				// column 헤더추가.
				TableColumn[] tcs = sqlResultTableViewer.getTable().getColumns();
				for (TableColumn tableColumn : tcs) {
					sbExportData.append( tableColumn.getText()).append(exportDelimit);//","); //$NON-NLS-1$
				}
				sbExportData.append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				
				// column 데이터 추가 
				for(int i=0; i<sourceDataList.size(); i++) {
					Map<Integer, Object> mapColumns = sourceDataList.get(i);
					for(int j=0; j<mapColumns.size(); j++) {
						sbExportData.append(mapColumns.get(j)).append(exportDelimit); //$NON-NLS-1$
					}
					sbExportData.append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadExtFile(userDB.getDisplay_name() + "_SQLResultExport.csv", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnSQLResultExport.setText(Messages.MainEditor_btnExport_text);
		
		sqlResultStatusLabel = new Label(compositeBtn, SWT.NONE);
		sqlResultStatusLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));

		///////////////////// tab item //////////////////////////
		CTabItem tbtmNewItem = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmNewItem.setText(Messages.MainEditor_10);
		
		Composite compositeSQLHistory = new Composite(tabFolderResult, SWT.NONE);
		tbtmNewItem.setControl(compositeSQLHistory);
		GridLayout gl_compositeSQLHistory = new GridLayout(1, false);
		gl_compositeSQLHistory.marginHeight = 0;
		gl_compositeSQLHistory.marginWidth = 0;
		gl_compositeSQLHistory.marginBottom = 0;
		compositeSQLHistory.setLayout(gl_compositeSQLHistory);
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableViewerSQLHistory = new TableViewer(compositeSQLHistory, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewerSQLHistory.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				if(!is.isEmpty()) {
					appendText(getHistoryTabelSelectData() + PublicTadpoleDefine.SQL_DILIMITER);
				}
			}
		});
		
		tableSQLHistory = tableViewerSQLHistory.getTable();
		tableSQLHistory.setLinesVisible(true);
		tableSQLHistory.setHeaderVisible(true);
		tableSQLHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableSQLHistory.setSortDirection(SWT.DOWN);
		
		// auto column layout
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tableViewerSQLHistory.getTable());
		tableViewerSQLHistory.getTable().setLayout(layoutColumnLayout);
		
		SQLHistorySorter sorterHistory = new SQLHistorySorter();
		SQLHistoryCreateColumn.createTableHistoryColumn(tableViewerSQLHistory, sorterHistory, layoutColumnLayout);
		
		tableViewerSQLHistory.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerSQLHistory.setContentProvider(new ArrayContentProvider());
		tableViewerSQLHistory.setInput(listSQLHistory);
		tableViewerSQLHistory.setSorter(sorterHistory);
		
		Composite compositeRecallBtn = new Composite(compositeSQLHistory, SWT.NONE);
		compositeRecallBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeRecallBtn.setLayout(new GridLayout(8, false));
		
		final Button btnExportHistory = new Button(compositeRecallBtn, SWT.NONE);
		btnExportHistory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection is = (IStructuredSelection)tableViewerSQLHistory.getSelection();
				if(!is.isEmpty()) {
					appendText(getHistoryTabelSelectData() + PublicTadpoleDefine.SQL_DILIMITER);
				} else {
					MessageDialog.openInformation(null, Messages.MainEditor_2, Messages.MainEditor_29);
				}
			}
		});
		btnExportHistory.setText(Messages.MainEditor_12);
		
		Button btnDetailView = new Button(compositeRecallBtn, SWT.NONE);
		btnDetailView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection is = (IStructuredSelection)tableViewerSQLHistory.getSelection();
				Object selElement = is.getFirstElement();
				if(selElement instanceof SQLHistoryDAO) {
					SQLHistoryDAO tmd = (SQLHistoryDAO)selElement;
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(getSite().getShell(), Messages.MainEditor_11, SQLHistoryLabelProvider.dateToStr(tmd.getStartDateExecute()), tmd.getStrSQLText() );
					dlg.open();
				} else {
					MessageDialog.openInformation(null, Messages.MainEditor_2, Messages.MainEditor_29);
				}
			}
		});
		btnDetailView.setText(Messages.MainEditor_btnDetailView_text);
		
		Button btnSetEditor = new Button(compositeRecallBtn, SWT.NONE);
		btnSetEditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSetEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(SQLHistoryDAO dao : listSQLHistory) {
					sbExportData.append( dao.getStrSQLText() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadExtFile(userDB.getDisplay_name() + "_RecallSQLExport.txt", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnSetEditor.setText(Messages.MainEditor_17);
		
		// table clear
		Button btnHistoyClear = new Button(compositeRecallBtn, SWT.NONE);
		btnHistoyClear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnHistoyClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listSQLHistory.clear();
				tableViewerSQLHistory.refresh();
			}
		});
		btnHistoyClear.setText(Messages.MainEditor_btnClear_text);
		
		Label labelDumyRecal = new Label(compositeRecallBtn, SWT.NONE);
		labelDumyRecal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textHistoryFilter = new Text(compositeRecallBtn, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textHistoryFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) refreshSqlHistory();
			}
		});
		textHistoryFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		// refresh
		Button btnRefresh = new Button(compositeRecallBtn, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshSqlHistory();
			}
		});
		btnRefresh.setText(Messages.MainEditor_24);
		
		///////////////////// tab Message //////////////////////////
		CTabItem tbtmMessage = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmMessage.setText(Messages.MainEditor_0);
		
		Composite compositeMessage = new Composite(tabFolderResult, SWT.NONE);
		tbtmMessage.setControl(compositeMessage);
		GridLayout gl_compositeMessage = new GridLayout(1, false);
		gl_compositeMessage.marginHeight = 0;
		compositeMessage.setLayout(gl_compositeMessage);
		
		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableViewerMessage = new TableViewer(compositeMessage, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewerMessage.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object selElement = is.getFirstElement();
				if(selElement instanceof TadpoleMessageDAO) {
					TadpoleMessageDAO tmd = (TadpoleMessageDAO)selElement;
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(getSite().getShell(), Messages.MainEditor_20, SQLHistoryLabelProvider.dateToStr(tmd.getDateExecute()), tmd.getStrMessage());
					dlg.open();
				}
			}
		});
		tableMessage = tableViewerMessage.getTable();
		tableMessage.setLinesVisible(true);
		tableMessage.setHeaderVisible(true);
		tableMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableMessage.setSortDirection(SWT.DOWN);
		
		// auto column layout
		AutoResizeTableLayout layoutColumnLayoutMsg = new AutoResizeTableLayout(tableViewerMessage.getTable());
		tableViewerMessage.getTable().setLayout(layoutColumnLayoutMsg);
		
		SQLHistorySorter sorterMessage = new SQLHistorySorter();
		MainEditorHelper.createTableMessageColumn(tableViewerMessage, sorterMessage, layoutColumnLayoutMsg);
		
		tableViewerMessage.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerMessage.setContentProvider(new ArrayContentProvider());
		tableViewerMessage.setInput(listMessage);
		tableViewerMessage.setComparator(sorterMessage);
		
		Composite compositeMessageSub = new Composite(compositeMessage, SWT.NONE);
		compositeMessageSub.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeMessageSub.setLayout(new GridLayout(3, false));
		
		Button btnExportMessage = new Button(compositeMessageSub, SWT.NONE);
		btnExportMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportMessage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(TadpoleMessageDAO dao : listMessage) {
					sbExportData.append( dao.getStrMessage() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadExtFile(userDB.getDisplay_name() + "_Message.txt", sbExportData.toString()); //$NON-NLS-1$
			}
		});
		btnExportMessage.setText(Messages.MainEditor_43);
		
		Label labelMsgDumy = new Label(compositeMessageSub, SWT.NONE);
		labelMsgDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnMessageViewClear = new Button(compositeMessageSub, SWT.NONE);
		btnMessageViewClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listMessage.clear();
				tableViewerMessage.refresh();
			}
		});
		btnMessageViewClear.setText(Messages.MainEditor_btnClear_text);
		/////////////////////// end tap item /////////////////////////////////////////////
		
		sashForm.setWeights(new int[] {65, 35});
		
		initEditor();
		
		// autocommit true 혹은 false값이 바뀌었을때..
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == PublicTadpoleDefine.AUTOCOMMIT_USE) {
					String strAutoCommit_seq = event.getNewValue().toString();
					// UserDB.seq || auto commit ture or false 
					String[] arryVal = StringUtils.split(strAutoCommit_seq, "||"); //$NON-NLS-1$
					int seq = Integer.parseInt(arryVal[0]);
					boolean boolUseAutocommit = Boolean.parseBoolean(arryVal[1]);

					if(!tltmAutoCommit.isDisposed()) {
						if(seq == userDB.getSeq()) {
							tltmAutoCommit.setSelection(boolUseAutocommit);
							if(!boolUseAutocommit) {
								tltmAutoCommit.setToolTipText("Auto Commit true"); //$NON-NLS-1$
								tltmAutoCommitCommit.setEnabled(false);
								tltmAutoCommitRollback.setEnabled(false);
							} else {
								tltmAutoCommit.setToolTipText("Auto Commit false"); //$NON-NLS-1$
								tltmAutoCommitCommit.setEnabled(true);
								tltmAutoCommitRollback.setEnabled(true);
							}
						}	// end tltmAutoCommit
					}	// end seq
				} // end if(event.getProperty()
			} //
		}); // end property change
	}
	
	/**
	 * append text at position
	 * 
	 * @param strText
	 */
	public void appendTextAtPosition(String strText) {
		try {
			setAppendQueryText(strText); //$NON-NLS-1$
			browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_APPEND_QUERY_TEXT_AT_POSITION);
		} catch(Exception ee){
			logger.error("query text at position" , ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * append text at position
	 * 
	 * @param strText
	 */
	public void appendText(String strText) {
		try {
			setAppendQueryText(strText); //$NON-NLS-1$
			browserEvaluate(EditorBrowserFunctionService.JAVA_SCRIPT_APPEND_QUERY_TEXT);
		} catch(Exception ee){
			logger.error("query text" , ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * refresh sql history table 
	 */
	private void refreshSqlHistory() {
		try {
			listSQLHistory.clear();
			listSQLHistory.addAll( TadpoleSystem_ExecutedSQL.getExecuteQueryHistory(user_seq, getUserDB().getSeq(), textHistoryFilter.getText().trim()) );
			tableViewerSQLHistory.refresh();
		} catch(Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * browser handler
	 */
	protected void addBrowserService() {
		if(DBOperationType.valueOf(userDB.getOperation_type()) == DBOperationType.PRODUCTION) {
	    	browserQueryEditor.setUrl(REAL_DB_URL);
	    } else {
	    	browserQueryEditor.setUrl(DEV_DB_URL);
	    }
		
		registerBrowserFunctions();
		
		browserQueryEditor.addProgressListener(new ProgressListener() {
			@Override
			public void completed( ProgressEvent event ) {
				
				try {
					browserQueryEditor.evaluate("getEditor();"); //$NON-NLS-1$
				} catch(Exception e) {
					logger.error(RequestInfoUtils.requestInfo("MainEditor browser init", strUserEMail), e); //$NON-NLS-1$
				}

				String callCommand = TadpoleEditorUtils.makeCommand(getInitExt(), getInitDefaultEditorStr(), getAssistList());
				browserEvaluate(callCommand);
			}
			public void changed( ProgressEvent event ) {}			
		});
	}
	
	/**
	 * 에디터에서 assist창에 보여줄 목록을 가져옵니다.
	 * 
	 * @return
	 */
	private String getAssistList() {
		String strTablelist = "select,select * from,"; //$NON-NLS-1$
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableDAO> showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$

			for (TableDAO tableDao : showTables) {
				strTablelist += tableDao.getName() + ","; //$NON-NLS-1$
			}
			strTablelist = StringUtils.removeEnd(strTablelist, ","); //$NON-NLS-1$
			
		} catch(Exception e) {
			logger.error("MainEditor get the table list", e); //$NON-NLS-1$
		}
		
		return strTablelist;
	}
	
	/** Define.QUERY_MODE 명령을 내립니다 */
	public void executeCommand(PublicTadpoleDefine.QUERY_MODE mode) {
		queryMode = mode;
		execute();
		
		resultFolderSel(RESULT_TAB_NAME.RESULT_SET);
	}

	/**
	 * initialize editor
	 */
	private void initEditor() {
		if (DBDefine.getDBDefine(userDB) == DBDefine.HIVE_DEFAULT) {
			tltmAutoCommit.setEnabled(false);
		}

		if("YES".equals(userDB.getIs_autocommit())) { //$NON-NLS-1$
			tltmAutoCommit.setSelection(false);
		} else {
			tltmAutoCommit.setSelection(true);
		}
		
		// 기존 에디터에서 auto commit button 이 어떻게 설정 되어 있는지 가져옵니다.
		initAutoCommitAction(false);
		
		tabFolderResult.setSelection(0);
		
		registerServiceHandler();
		setOrionText( initDefaultEditorStr );
	}
	
	/**
	 * init auto commit button
	 * 
	 * @param isRiseEvent
	 */
	private void initAutoCommitAction(boolean isRiseEvent) {
		if(isAutoCommit()) {
			tltmAutoCommit.setToolTipText("Auto Commit true"); //$NON-NLS-1$
			tltmAutoCommitCommit.setEnabled(false);
			tltmAutoCommitRollback.setEnabled(false);
		} else {
			tltmAutoCommit.setToolTipText("Auto Commit false"); //$NON-NLS-1$
			tltmAutoCommitCommit.setEnabled(true);
			tltmAutoCommitRollback.setEnabled(true);
		}
		
		if(isRiseEvent) {
			// auto commit의 실행버튼을 동일한 db를 열고 있는 에디터에서 공유합니다.
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.AUTOCOMMIT_USE, userDB.getSeq() + "||" + tltmAutoCommit.getSelection() + "||" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}

	/**
	 * sql history 데이터를 만듭니다.
	 */
	private void sqlHistory(List<SQLHistoryDAO> listExecutingSqltHistoryDao) {
		listSQLHistory.addAll(listExecutingSqltHistoryDao);
		tableViewerSQLHistory.refresh();
	}
	
	/**
	 * sql history를 텍스트로 만듭니다.
	 * @return
	 * @throws Exception
	 */
	private String getHistoryTabelSelectData() {
		StringBuffer sbData = new StringBuffer();
		
		for(TableItem ti : tableSQLHistory.getSelection()) {
			sbData.append(ti.getText(1));
		}
		
		return sbData.toString();
	}
	
	/**
	 * 쿼리를 수행합니다.
	 * 
	 * text중에 블럭으로 되어 있는 부분만 쿼리를 수행합니다.
	 * ;를 기준으로 여러개로 나누어 쿼리를 수행합니다.
	 * 
	 */
	private void execute() {
		// 히스토리와 모니터링 데이터를 보관하기위해 변수.
		final List<SQLHistoryDAO> listExecutingSqltHistoryDao = new ArrayList<SQLHistoryDAO>();
		
		String tmpStrSelText= StringUtils.trimToEmpty(getOrionText());
		if("".equals(tmpStrSelText)) return;		 //$NON-NLS-1$
		
		// query의 히스토리를 보여 주기위한 변수 정의
		final List<String> listQueryHistory = new ArrayList<String>();
		
		// execute batch 처리를 위한 변수 정의
		final List<String> listStrExecuteQuery = new ArrayList<String>();
		// TODO array 쿼리인 경우 history 정보를 저장하기 위해 변수를 사용합니다.
		String tmpArayExecuteQuery = ""; //$NON-NLS-1$
		
		// select 문 처리를 위한 변수 정의
		String executeLastSQL  = ""; //$NON-NLS-1$
		
		// 현재 실행 할 쿼리의 형태(전체 실행, 부분 쿼리 실행)인지
		final int intExecuteQueryType = getOrionEditorCursorPosition();

		// 전체 실행이면..
		if(intExecuteQueryType == ALL_QUERY_EXECUTE) {						
			tmpStrSelText = UnicodeUtils.getUnicode(tmpStrSelText);
			
			// 분리자 만큼 돌려면 실행 할 쿼리를 모읍니다.
			for (String strSQL : tmpStrSelText.split(PublicTadpoleDefine.SQL_DILIMITER)) {							

				// 히스토리 데이터에 실행된 쿼리 저장
				listQueryHistory.add(strSQL);						
				
				// 구분자 ;를 제거 합니다.(특정 디비에서는 ;가 있으면 오류)
				strSQL = StringUtils.removeEnd(strSQL, PublicTadpoleDefine.SQL_DILIMITER);
				
				// 쿼리 텍스트에 쿼리 이외의 특수 문자를 제거
				executeLastSQL = SQLUtil.executeQuery(strSQL);
				
				// execute batch update는 ddl문이 있으면 안되어서 실행할 수 있는 쿼리만 걸러 줍니다.
				if(!isStatement(executeLastSQL)) {
					listStrExecuteQuery.add(executeLastSQL);
					tmpArayExecuteQuery += executeLastSQL + PublicTadpoleDefine.LINE_SEPARATOR;
					executeLastSQL = ""; //$NON-NLS-1$
				}
			}
		// 블럭 쿼리를 실행하였으면 쿼리를 분리자로 나누지 않고 전체를 수행합니다.
		} else if(intExecuteQueryType == BLOCK_QUERY_EXECUTE) {	
			String strSQL = SQLTextUtil.executeQuery(tmpStrSelText);

			// 히스토리 데이터에 실행된 쿼리 저장
			listQueryHistory.add(strSQL);						
			
			// 구분자 ;를 제거 합니다.(특정 디비에서는 ;가 있으면 오류)
			strSQL = StringUtils.removeEnd(strSQL, PublicTadpoleDefine.SQL_DILIMITER);
			
			// 쿼리를 수행할수 있도록 가공합니다.
			executeLastSQL = SQLUtil.executeQuery(strSQL);
		// 일반 적인 쿼리 실행.
		} else {
			String strSQL = SQLTextUtil.executeQuery(tmpStrSelText, intExecuteQueryType);

			// 히스토리 데이터에 실행된 쿼리 저장
			listQueryHistory.add(strSQL);						
			
			// 구분자 ;를 제거 합니다.(특정 디비에서는 ;가 있으면 오류)
			strSQL = StringUtils.removeEnd(strSQL, PublicTadpoleDefine.SQL_DILIMITER);
			
			// 쿼리를 수행할수 있도록 가공합니다.
			executeLastSQL = SQLUtil.executeQuery(strSQL);
		}
		
		// 실행해도 되는지 묻는다.
		if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDB.getQuestion_dml())) {
			boolean isDML = false;
			// 여러개 쿼리를 실행했을 경우.
			for (String strQuery : listStrExecuteQuery) {
				if(!SQLUtil.isStatement(strQuery)) isDML = true;
			}
			// 단일 쿼리를 실행했을 경우.
			if(!SQLUtil.isStatement(executeLastSQL)) isDML = true;
			
			if(isDML) {
				if(!MessageDialog.openConfirm(null, "Confirm", Messages.MainEditor_56)) return; //$NON-NLS-1$
			}
		}

		// 쿼리를 실행할 수 있도록 준비합니다.
		final String finalExecuteSQL = executeLastSQL;
		final boolean isStatement = isStatement(executeLastSQL);
		
		/* 선택은 autocommit false이므로 아래와 같습니다. */
		final boolean isAutoCommit = isAutoCommit();
		
		final String strArayExecuteQuery = tmpArayExecuteQuery;

		// job
		Job job = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.MainEditor_46, IProgressMonitor.UNKNOWN);
				
				SQLHistoryDAO executingSQLHistoryDAO = new SQLHistoryDAO();
				executingSQLHistoryDAO.setStartDateExecute(new Date());
				
				try {
					// 페이지를 초기화 합니다.
					pageNumber = 1;	
					
					if(intExecuteQueryType == ALL_QUERY_EXECUTE) {
						
						executingSQLHistoryDAO.setStrSQLText(strArayExecuteQuery);
						// select 이외의 쿼리 실행
						if(!listStrExecuteQuery.isEmpty()) {
							runSQLExecuteBatch(listStrExecuteQuery, isAutoCommit);
						}
						//finally bolock duplicate
						//executingSQLHistoryDAO.setEndDateExecute(new Date());
						//executingSQLHistoryDAO.setResult("Success"); //$NON-NLS-1$
						//listExecutingSqltHistoryDao.add(executingSQLHistoryDAO);
						
						// select 문장 실행
						if(isStatement) { //$NON-NLS-1$
							executingSQLHistoryDAO = new SQLHistoryDAO();
							executingSQLHistoryDAO.setStartDateExecute(new Date());
							
							runSQLSelect(finalExecuteSQL, isAutoCommit);
							executingSQLHistoryDAO.setRows(sourceDataList.size());
						}
					} else {
						executingSQLHistoryDAO.setStrSQLText(finalExecuteSQL);
						
						if(isStatement) {
							runSQLSelect(finalExecuteSQL, isAutoCommit);
							executingSQLHistoryDAO.setRows(sourceDataList.size());
						} else {
							runSQLOther(finalExecuteSQL, isAutoCommit);
						}
					}
					
					executingSQLHistoryDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.S.toString()); //$NON-NLS-1$
				} catch(Exception e) {
					logger.error(Messages.MainEditor_50 + finalExecuteSQL, e);
					
					executingSQLHistoryDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.toString()); //$NON-NLS-1$
					executingSQLHistoryDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
					
					executingSQLHistoryDAO.setEndDateExecute(new Date());
					listExecutingSqltHistoryDao.add(executingSQLHistoryDAO);
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							// table에 데이터 표시
							executeFinish(finalExecuteSQL);
							
							// 쿼리 실행후에 결과 테이블에 포커스가 가도록
							setOrionTextFocus();
						} else {
							resultTableInit();
							executeErrorProgress(jobEvent.getResult().getMessage());
						}

						sqlHistory(listExecutingSqltHistoryDao);
						saveExecuteSQLData(listExecutingSqltHistoryDao);
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}
	
	/**
	 * execute SQL data
	 * 
	 * @param listExecutingSqltHistoryDao
	 */
	private void saveExecuteSQLData(List<SQLHistoryDAO> listExecutingSqltHistoryDao) {
		if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDB.getIs_profile())) {
			try {
				TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(user_seq, userDB, PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDITOR, listExecutingSqltHistoryDao);
			} catch(Exception e) {
				logger.error("save the user query", e); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * sql이 statement인지 검사합니다.
	 * @param sql
	 * @return
	 */
	private boolean isStatement(String sql) {
		boolean isTmpStatement = false;
		if(SQLUtil.isStatement(sql)) isTmpStatement = true;

		return isTmpStatement;
	}
	
	/**
	 * auto commit 
	 * @return
	 */
	private boolean isAutoCommit() {
		return !tltmAutoCommit.getSelection();
	}
	
	/**
	 * transaction 쿼리인지 검사합니다.
	 * 
	 * @param query
	 * @return
	 */
	private boolean transactionQuery(String query) {
		if(StringUtils.startsWith(query, "commit")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.commit(strUserEMail, userDB);
			return true;
		}
		// 
		if(StringUtils.startsWith(query, "rollback")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.rollback(strUserEMail, userDB);
			return true;
		}
		
		return false;
	}
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 * @param isAutoCommit
	 */
	private void runSQLSelect(String requestQuery, final boolean isAutoCommit) throws Exception {		
		if(!PermissionChecker.isExecute(getUserType(), userDB, requestQuery)) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
		if(transactionQuery(requestQuery)) return;
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		
		try {
			if(isAutoCommit) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB, isAutoCommit);
			}
			
			if(PublicTadpoleDefine.QUERY_MODE.DEFAULT == queryMode) {
				
				if(requestQuery.toUpperCase().startsWith("SELECT")) { //$NON-NLS-1$
					requestQuery = PartQueryUtil.makeSelect(userDB, requestQuery, 0, queryResultCount);
					if(logger.isDebugEnabled()) logger.debug("[SELECT] " + requestQuery); //$NON-NLS-1$
				}
				
				stmt = javaConn.prepareStatement(requestQuery);
				//  환경설정에서 원하는 조건을 입력하였을 경우.
				rs = stmt.executeQuery();
				
			// explain
			}  else if(PublicTadpoleDefine.QUERY_MODE.EXPLAIN_PLAN == queryMode) {
				
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(DBDefine.getDBDefine(userDB) == DBDefine.CUBRID_DEFAULT) {
					
					String cubridQueryPlan = CubridExecutePlanUtils.plan(userDB, requestQuery);
					mapColumns = CubridExecutePlanUtils.getMapColumns();
					sourceDataList = CubridExecutePlanUtils.getMakeData(cubridQueryPlan);
					
					return;
					
				} else if(DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT) {
					// 플랜결과를 디비에 저장합니다.
					OracleExecutePlanUtils.plan(userDB, requestQuery, planTableName);
					// 저장된 결과를 가져와서 보여줍니다.
					stmt = javaConn.prepareStatement("select * from " + planTableName); //$NON-NLS-1$
					rs = stmt.executeQuery();
					
				} else {
				
					stmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDB, requestQuery));
					rs = stmt.executeQuery();
					
				}
			}

			//////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////
			// column의 data type을 얻습니다.
			mapColumnType = ResultSetUtils.getColumnType(rs.getMetaData());
			
			// column name을 얻습니다. 
			// sqlite에서는 metadata를 얻은 후에 resultset을 얻어야 에러(SQLite JDBC: inconsistent internal state)가 나지 않습니다.
			mapColumns = ResultSetUtils.getColumnName(rs);
			
			// 결과를 프리퍼런스에서 처리한 맥스 결과 만큼만 거져옵니다.
			sourceDataList = ResultSetUtils.getResultToList(rs, queryResultCount, isResultComma);
			
		} finally {
			try { stmt.close(); } catch(Exception e) {}
			try { rs.close(); } catch(Exception e) {}
			
			if(isAutoCommit) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}
	
	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * 
	 * @param listQuery
	 * @param isAutoCommit
	 * @throws Exception
	 */
	private void runSQLExecuteBatch(List<String> listQuery, final boolean isAutoCommit) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), userDB, listQuery)) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(isAutoCommit) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB, isAutoCommit);
			}
			statement = javaConn.createStatement();
			
			for (String strQuery : listQuery) {
				// 쿼리 중간에 commit이나 rollback이 있으면 어떻게 해야 하나???
				if(!transactionQuery(strQuery)) { 
					
					if(StringUtils.startsWith(strQuery.trim().toUpperCase(), "CREATE TABLE")) { //$NON-NLS-1$
						strQuery = StringUtils.replaceOnce(strQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
				}
				statement.addBatch(strQuery);
			}
			statement.executeBatch();
		} catch(Exception e) {
			logger.error("Execute batch update", e); //$NON-NLS-1$
			throw e;
		} finally {
			try { statement.close();} catch(Exception e) {}

			if(isAutoCommit) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}

	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param sqlQuery
	 * @param isAutoCommit
	 * @exception
	 */
	private void runSQLOther(String sqlQuery, final boolean isAutoCommit) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), userDB, sqlQuery)) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
		if(transactionQuery(sqlQuery)) return;
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		try {
			if(isAutoCommit) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB, isAutoCommit);
			}
			
			statement = javaConn.createStatement();
			
			final String checkSQL = sqlQuery.trim().toUpperCase();
			// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다. 
			if(StringUtils.startsWith(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
				sqlQuery = StringUtils.replaceOnce(sqlQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
			
			// 오라클의 경우 procedure, function, package, trigger의 경우 마지막에 ; 가 있어야 정상 프로시저로 인정됩니다. 
			//
			} else if(StringUtils.startsWith(checkSQL, "CREATE OR") || //$NON-NLS-1$
					StringUtils.startsWith(checkSQL, "CREATE PROCEDURE") || //$NON-NLS-1$
					StringUtils.startsWith(checkSQL, "CREATE FUNCTION") || //$NON-NLS-1$
					StringUtils.startsWith(checkSQL, "CREATE PACKAGE") || //$NON-NLS-1$
					StringUtils.startsWith(checkSQL, "CREATE TRIGGER") //$NON-NLS-1$
			) { //$NON-NLS-1$
				sqlQuery += ";"; //$NON-NLS-1$
			}
			// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
			statement.execute(sqlQuery);//executeUpdate( sqlQuery );
			
			// create table, drop table이면 작동하도록			
			if(StringUtils.startsWith(sqlQuery.trim().toUpperCase(), "CREATE") ||  //$NON-NLS-1$
				StringUtils.startsWith(sqlQuery.trim().toUpperCase(), "DROP")  || //$NON-NLS-1$
				StringUtils.startsWith(sqlQuery.trim().toUpperCase(), "ALTER")) {  //$NON-NLS-1$
																					//$NON-NLS-1$ //$NON-NLS-2$
				
				try {
					refreshExplorerView();
				} catch(Exception e) {
					logger.error("CREATE, DROP, ALTER Query refersh error", e); //$NON-NLS-1$
				}
			}
		} finally {
			try { statement.close();} catch(Exception e) {}

			if(isAutoCommit) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}		
	}

	/**
	 * CREATE, DROP, ALTER 문이 실행되어 ExplorerViewer view를 리프레쉬합니다.
	 */
	private void refreshExplorerView() {

		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
					ev.refreshCurrentTab(userDB);
				} catch (PartInitException e) {
					logger.error("ExplorerView show", e); //$NON-NLS-1$
				}
			}
			
		});
	}
	
	/**
	 * 1) 마지막 쿼리를 받아서 selct 문일 경우 쿼리 네비게이션을 확성화 해준다.
	 * 2) filter를 설정한다.
	 * 
	 * @param lastQuery 실행된 마지막 쿼리
	 */
	private void executeFinish(String finalExecuteSQL) {
		setFilter();
		
		if(isStatement(finalExecuteSQL)) {			
			btnPrev.setEnabled(false);
			if( sourceDataList.size() < queryPageCount ) btnNext.setEnabled(false);
			else btnNext.setEnabled(true);
		} else {
			btnPrev.setEnabled(false);
			btnNext.setEnabled(false);
		}
		
		// 쿼리의 결과를 화면에 출력합니다.
		setResultTable(finalExecuteSQL);
	}
	
	/**
	 * error message 추가한다.
	 * @param msg
	 */
	private void executeErrorProgress(final String msg) {
		TadpoleMessageDAO message = new TadpoleMessageDAO(new Date(), msg);
		listMessage.add(message);
		
		tableViewerMessage.refresh();
		resultFolderSel(RESULT_TAB_NAME.TADPOLE_MESSAGE);
		
		setOrionTextFocus();
	}
	
	/**
	 * tab을 선택합니다.
	 * 
	 * @param selectTab
	 */
	private void resultFolderSel(final RESULT_TAB_NAME selectTab) {
		int index = (Integer)tabFolderResult.getData(selectTab.toString());
		if(tabFolderResult.getSelectionIndex() != index) {
			tabFolderResult.setSelection(index);
		}
	}
	
	/**
	 * 쿼리의 결과를 화면에 출력하거나 정리 합니다.
	 */
	private void setResultTable(String finalExecuteSQL) {
		if(isStatement(finalExecuteSQL)) {			
			// table data를 생성한다.
			sqlSorter = new SQLResultSorter(-999);
			
			SQLResultLabelProvider.createTableColumn(sqlResultTableViewer, mapColumns, mapColumnType, sqlSorter);
			sqlResultTableViewer.setLabelProvider(new SQLResultLabelProvider());
			sqlResultTableViewer.setContentProvider(new SQLResultContentProvider(sourceDataList));
			
			// 쿼리 결과를 사용자가 설정 한 만큼 보여준다. 
			List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
			int readCount = (sourceDataList.size()+1) - queryPageCount;
			if(readCount < -1) readCount = sourceDataList.size();
			else if(readCount > queryPageCount) readCount = queryPageCount;
			
			if(logger.isDebugEnabled()) {
				logger.debug("====[first][start]================================================================="); //$NON-NLS-1$
				logger.debug("[total count]" + sourceDataList.size() + "[first][readCount]" + readCount); //$NON-NLS-1$ //$NON-NLS-2$
				logger.debug("====[first][stop]================================================================="); //$NON-NLS-1$
			}
				
			for(int i=0; i<readCount; i++) {
				showList.add(sourceDataList.get(i));
			}
			// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
			
			sqlResultTableViewer.setInput(showList);
			sqlResultTableViewer.setSorter(sqlSorter);
			
			// 메시지를 출력합니다.
			tableResult.setToolTipText(sourceDataList.size() + Messages.MainEditor_33);
			sqlFilter.setTable(tableResult);
			sqlResultStatusLabel.setText(sourceDataList.size() + Messages.MainEditor_33);
			
			// Pack the columns
			TableUtil.packTable(tableResult);
			resultFolderSel(RESULT_TAB_NAME.RESULT_SET);
		} else {
			listMessage.add(new TadpoleMessageDAO(new Date(), "success")); //$NON-NLS-1$
			tableViewerMessage.refresh(listMessage);
			resultFolderSel(RESULT_TAB_NAME.TADPOLE_MESSAGE);
		}
	}
	
	/**
	 * 다음 버튼 처리
	 * 
	 * pageLocation
	 * 
	 */
	private void btnNext() {
		// table data를 생성한다.
		sqlSorter = new SQLResultSorter(-999);
		
		List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
		
		// 쿼리 결과를 사용자가 설정 한 만큼 보여준다.
		int startCount 	= queryPageCount * pageNumber;
		int endCount 	= queryPageCount * (pageNumber+1);
		if(logger.isDebugEnabled()) logger.debug("btnNext ======> [start point]" + startCount + "\t [endCount]" + endCount); //$NON-NLS-1$ //$NON-NLS-2$
		
		//  
		if(endCount >= (sourceDataList.size()+1)) {
			endCount = sourceDataList.size();
			
			// 다음 버튼을 비활성화 한다.
			btnNext.setEnabled(false);
		}
		
		// 데이터 출력.
		for(int i=startCount; i<endCount; i++) {
			showList.add(sourceDataList.get(i));
		}
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		
		sqlResultTableViewer.setInput(showList);
		sqlResultTableViewer.setSorter(sqlSorter);
		
		// Pack the columns
		TableUtil.packTable(tableResult);
		
		// page 번호를 하나 추가한다.
		pageNumber++;
		if(!btnPrev.getEnabled()) btnPrev.setEnabled(true);
	}
	
	/**
	 * 이전 버튼 처리
	 */
	private void btnPrev() {
		// table data를 생성한다.
		sqlSorter = new SQLResultSorter(-999);		
		List<Map<Integer, Object>>  showList = new ArrayList<Map<Integer,Object>>();
		
		// 쿼리 결과를 사용자가 설정 한 만큼 보여준다.
		int startCount 	= queryPageCount * (pageNumber-2);
		int endCount 	= queryPageCount * (pageNumber-1);
		if(logger.isDebugEnabled()) logger.debug("btnPrev ======> [start point]" + startCount + "\t [endCount]" + endCount); //$NON-NLS-1$ //$NON-NLS-2$
		
		if(startCount <= 0) {
			startCount = 0;
			endCount = queryPageCount;
			
			btnPrev.setEnabled(false);
		}
		
		// 데이터 출력.
		for(int i=startCount; i<endCount; i++) {
			showList.add(sourceDataList.get(i));
		}
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		
		sqlResultTableViewer.setInput(showList);
		sqlResultTableViewer.setSorter(sqlSorter);
		
		// Pack the columns
		TableUtil.packTable(tableResult);
		
		// page 번호를 하나 추가한다.
		pageNumber--;
		if(!btnNext.getEnabled()) btnNext.setEnabled(true);
	}
	
	/**
	 * 결과 테이블을 초기화 상태로 만듭니다.
	 */
	private void resultTableInit() {
		// rs set의 결과를 테이블에 출력하기 위해 입력한다.
		sourceDataList = new ArrayList<Map<Integer, Object>>();
		
		// 마지막 쿼리에 데이터를 정리 합니다.
		sqlResultTableViewer.setLabelProvider( new SQLResultLabelProvider() );
		sqlResultTableViewer.setContentProvider(new SQLResultContentProvider(sourceDataList) );
		sqlResultTableViewer.setInput(sourceDataList);			
		sqlResultStatusLabel.setText(Messages.MainEditor_28 );
	}
	
	/**
	 * 필터를 설정합니다.
	 */
	private void setFilter() {
		sqlFilter.setFilter(textFilter.getText());
		sqlResultTableViewer.addFilter( sqlFilter );
	}

	@Override
	public void setFocus() {
//		setOrionTextFocus();
	}
	
	/**
	 * orion text
	 * 
	 * @return
	 */
	public String getOrionText() {
		return queryText;
	}
	
	/**
	 * orion editor cursor position
	 * 
	 * @return
	 */
	public int getOrionEditorCursorPosition() {
		return this.queryEditorCursorPoistion;
	}
	
	/**
	 * set orion editor cursor position
	 * 
	 * @param queryTextPoistion
	 */
	public void setOrionEditorCursorPostion(int queryTextPoistion) {
		this.queryEditorCursorPoistion = queryTextPoistion;
	}
	
	/**
	 * set orion text
	 * 
	 * @param queryText
	 */
	public void setOrionText(String queryText) {
		this.queryText = queryText;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
		if(dBResource == null) {
			userSetDBResource = getFileName();
			if(userSetDBResource == null) return;
		}
		
		// 저장을 호출합니다.
		try {
			Object resultObj = browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SAVE_FUNCTION);
			if(!(resultObj instanceof Boolean && (Boolean) resultObj)) {
				monitor.setCanceled(true);
			}
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", strUserEMail), e); //$NON-NLS-1$
			monitor.setCanceled(true);
		}	

	}

	/**
	 * new file name
	 * @return
	 */
	private UserDBResourceDAO getFileName() {
		ResourceSaveDialog rsDialog = new ResourceSaveDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, PublicTadpoleDefine.RESOURCE_TYPE.SQL);
		if(rsDialog.open() == Window.OK) {
			return rsDialog.getRetResourceDao();
		} else {
			return null;
		}
	}
	
	/**
	 * save
	 * 
	 * @param newContents
	 * @return
	 */
	public boolean performSave(String newContents) {
		// null은 단축키를 바로 눌렀을 경우에 호출되어 집니다.
		if(dBResource == null) {
			// editor가 저장 가능 상태인지 검사합니다.
			if(!isDirty()) return false; 
			
			if(userSetDBResource == null) {
				userSetDBResource = getFileName();
				if(userSetDBResource == null) return false;
			}
			
			try {
				// db 저장
				dBResource = TadpoleSystem_UserDBResource.saveResource(userDB, userSetDBResource, newContents);
				dBResource.setParent(userDB);
				
				// title 수정
				setPartName(userSetDBResource.getName());
				
				// tree 갱신
				PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SAVE_FILE, ""+dBResource.getDb_seq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				setDirty(false);
			} catch (Exception e) {
				logger.error("save file", e); //$NON-NLS-1$

				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
		} else {
			try {
				TadpoleSystem_UserDBResource.updateResource(dBResource, newContents);
				setDirty(false);
			} catch (Exception e) {
				logger.error("update file", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
				
				return false;
			}
		}

		return true;
	}
	
	/** save property dirty */
	public void setDirty(Boolean newValue) {
//		logger.debug("[setdirty][isFirstLoad]" + isFirstLoad + "[newValue]" + newValue + "[isDirty]"+ isDirty);
		if(!isFirstLoad) {
			if(isDirty != newValue) {
				isDirty = newValue;
				firePropertyChange(PROP_DIRTY);
			}
		}
		
		isFirstLoad = false;
	}
	
	@Override
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	}

	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}

	@Override
	public void doSaveAs() {
	}
	
	public UserDBResourceDAO getdBResource() {
		return dBResource;
	}

	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	public void downloadExtFile(String fileName, String newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents.getBytes());
		
		DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
	}
	
	/**
	 * sql history 를 선택합니다.
	 */
	public void selectHistoryPage() {
		resultFolderSel(RESULT_TAB_NAME.SQL_RECALL);
		
		// table 데이터가 있으면 첫번째 데이터를 선택합니다.
		if(listSQLHistory.size() >= 1) {
			Table tb = tableViewerSQLHistory.getTable();
			tb.select(0);
			tb.setFocus();
		}
	}

	@Override
	protected void registerBrowserFunctions() {
		editorService = new MainEditorBrowserFunctionService(browserQueryEditor, EditorBrowserFunctionService.EDITOR_SERVICE_HANDLER, this);
	}
	
	/**
	 * 에디터 로드할때 사용할 초기 쿼리 입니다.
	 * @return
	 */
	public String getInitDefaultEditorStr() {
		return initDefaultEditorStr;
	}
	
	/**
	 * 에디터에서 사용할 확장자를 만듭니다.
	 * @return
	 */
	public String getInitExt() {
		String extension = "tadpole_edit"; //$NON-NLS-1$
		DBDefine userDBDefine = DBDefine.getDBDefine(getUserDB());
		
		if(userDBDefine == DBDefine.MYSQL_DEFAULT || userDBDefine == DBDefine.MARIADB_DEFAULT) {
			extension += ".mysql"; //$NON-NLS-1$
		} else if(userDBDefine == DBDefine.ORACLE_DEFAULT) {
			extension += ".oracle"; //$NON-NLS-1$
		} else if(userDBDefine == DBDefine.MSSQL_DEFAULT) {
			extension += ".mssql"; //$NON-NLS-1$
		} else if(userDBDefine == DBDefine.SQLite_DEFAULT) {
			extension += ".sqlite"; //$NON-NLS-1$
		} else if(userDBDefine == DBDefine.CUBRID_DEFAULT) {
			extension += ".mysql"; //$NON-NLS-1$
		} else if(userDBDefine == DBDefine.HIVE_DEFAULT) {
			extension += ".hql"; //$NON-NLS-1$
		} else {
			extension += ".postgresql"; //$NON-NLS-1$
		}
		
		return extension;
	}
}
