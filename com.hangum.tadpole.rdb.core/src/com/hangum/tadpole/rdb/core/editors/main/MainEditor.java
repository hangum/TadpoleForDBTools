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

import java.io.BufferedReader;
import java.io.File;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
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
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.EditorExtension;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.dialogs.message.TadpoleImageViewDialog;
import com.hangum.tadpole.commons.dialogs.message.TadpoleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.export.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.editors.main.function.MainEditorBrowserFunctionService;
import com.hangum.tadpole.rdb.core.editors.main.sub.MainEditorHelper;
import com.hangum.tadpole.rdb.core.util.CubridExecutePlanUtils;
import com.hangum.tadpole.rdb.core.util.OracleExecutePlanUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.sql.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.sql.dialog.save.ResourceSaveDialog;
import com.hangum.tadpole.sql.format.SQLFormater;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
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
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
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
	private boolean isDirty = false;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	/** edior가 초기화 될때 처음 로드 되어야 하는 String. */
	private String initDefaultEditorStr = ""; //$NON-NLS-1$
	
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
		initDefaultEditorStr = qei.getDefaultStr();

		strRoleType = SessionManager.getRoleType(userDB.getGroup_seq());
		dBResource = qei.getResourceDAO();
		if(dBResource == null) setPartName(qei.getName());
		else  setPartName(dBResource.getName());
	}
	
	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		if(dBResource == null) return false;
		else return true;
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
		sashForm.setSashWidth(4);
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
				setFocus();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(String.format(Messages.MainEditor_tltmExecute_toolTipText_1, prefixOSShortcut));
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query.png")); //$NON-NLS-1$
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, QUERY_DELIMITER); //$NON-NLS-1$
				
				RequestQuery reqQuery = new RequestQuery(strQuery, EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.NONE, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecuteAll = new ToolItem(toolBar, SWT.NONE);
		tltmExecuteAll.setToolTipText(Messages.MainEditor_tltmExecuteAll_text);
		tltmExecuteAll.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query-all.png")); //$NON-NLS-1$
		tltmExecuteAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				RequestQuery reqQuery = new RequestQuery(strQuery, EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.ALL, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
	
		ToolItem tltmExplainPlanctrl = new ToolItem(toolBar, SWT.NONE);
		tltmExplainPlanctrl.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/execute_plan.png")); //$NON-NLS-1$
		tltmExplainPlanctrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, QUERY_DELIMITER); //$NON-NLS-1$
				
				RequestQuery reqQuery = new RequestQuery(strQuery, EditorDefine.QUERY_MODE.EXPLAIN_PLAN, EditorDefine.EXECUTE_TYPE.NONE, isAutoCommit());
				executeCommand(reqQuery);
				
			}
		});
		tltmExplainPlanctrl.setToolTipText(String.format(Messages.MainEditor_3, prefixOSShortcut));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSort = new ToolItem(toolBar, SWT.NONE);
		tltmSort.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/query_format.png")); //$NON-NLS-1$
		tltmSort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				try {
					browserEvaluate(EditorFunctionService.RE_NEW_TEXT, SQLFormater.format(strQuery));
				} catch(Exception ee) {
					logger.error("sql format", ee); //$NON-NLS-1$
				}
			}
		});
		tltmSort.setToolTipText(String.format(Messages.MainEditor_4, prefixOSShortcut));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSQLToApplication = new ToolItem(toolBar, SWT.NONE);
		tltmSQLToApplication.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql_to_applications.png")); //$NON-NLS-1$
		tltmSQLToApplication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				SQLToStringDialog dialog = new SQLToStringDialog(null, EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer.toString(), strQuery);
				dialog.open();
				setFocus();
			}
		});
	    tltmSQLToApplication.setToolTipText("SQL statement to Application code"); //$NON-NLS-1$
	    new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png")); //$NON-NLS-1$
		tltmDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!MessageDialog.openConfirm(null, Messages.MainEditor_38, Messages.MainEditor_39)) return;
		
				try {
					String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
					downloadExtFile(getUserDB().getDisplay_name()+".sql", strQuery); //$NON-NLS-1$
				} catch(Exception ee) {
					logger.error("Download SQL", ee); //$NON-NLS-1$
				}
			}
		});
		tltmDownload.setToolTipText(Messages.MainEditor_42);
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmAutoCommit = new ToolItem(toolBar, SWT.CHECK);
		tltmAutoCommit.setSelection(false);
		tltmAutoCommit.setText("Auto Commit"); //$NON-NLS-1$
		tltmAutoCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initAutoCommitAction(false, true);
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
				
				setFocus();
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
		tabFolderResult.setData(EditorDefine.RESULT_TAB_NAME.RESULT_SET.toString(), 0);
		tabFolderResult.setData(EditorDefine.RESULT_TAB_NAME.SQL_RECALL.toString(), 1);
		tabFolderResult.setData(EditorDefine.RESULT_TAB_NAME.TADPOLE_MESSAGE.toString(), 2);
		
		CTabItem tbtmResult = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmResult.setText(Messages.MainEditor_7);
		
		Composite compositeQueryResult = new Composite(tabFolderResult, SWT.NONE);
		tbtmResult.setControl(compositeQueryResult);
		
		GridLayout gl_compositeQueryResultBtn = new GridLayout(2, false);
		gl_compositeQueryResultBtn.marginWidth = 1;
		gl_compositeQueryResultBtn.marginHeight = 0;
		compositeQueryResult.setLayout(gl_compositeQueryResultBtn);
		
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
						if(strText == null || "".equals(strText)) return; //$NON-NLS-1$
						strText = RDBTypeToJavaTypeUtils.isNumberType(mapColumnType.get(i))? (" " + strText + ""): (" '" + strText + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						//appendTextAtPosition(strText);
						
						//결과 그리드의 선택된 행에서 마우스 클릭된 셀에 연결된 컬럼 오브젝트를 조회한다.
						Map<Integer, Object> mapColumns = sourceDataList.get(tableResult.getSelectionIndex());
						Object columnObject = mapColumns.get(i);
						
						// 해당컬럼 값이 널이 아니고 clob데이터 인지 확인한다.
						//if (columnObject != null && columnObject instanceof net.sourceforge.jtds.jdbc.ClobImpl ){
						if (columnObject != null && columnObject instanceof java.sql.Clob ){
							Clob cl = (Clob) columnObject;
	
							StringBuffer clobContent = new StringBuffer();
							String readBuffer = new String();
	
							// 버퍼를 이용하여 clob컬럼 자료를 읽어서 팝업 화면에 표시한다.
							BufferedReader bufferedReader;
							try {
								bufferedReader = new java.io.BufferedReader(cl.getCharacterStream());
								
								while ((readBuffer = bufferedReader.readLine())!= null)
									clobContent.append(readBuffer);

								TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(getSite().getShell(), tableResult.getColumn(i).getText(), clobContent.toString());
					            dlg.open();									
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else if (columnObject != null && columnObject instanceof java.sql.Blob ){
							try {
								Blob blob = (Blob) columnObject;
								
								TadpoleImageViewDialog dlg = new TadpoleImageViewDialog(getSite().getShell(), tableResult.getColumn(i).getText(), blob.getBinaryStream());
								dlg.open();								
							} catch (Exception e) {
								logger.error("Blob column echeck", e); //$NON-NLS-1$
							}
		
						}else if (columnObject != null && columnObject instanceof byte[] ){// (columnObject.getClass().getCanonicalName().startsWith("byte[]")) ){
							byte[] b = (byte[])columnObject;
							StringBuffer str = new StringBuffer();
							try {
								for (byte buf : b){
									str.append(buf);
								}
								str.append("\n\nHex : " + new BigInteger(str.toString(), 2).toString(16)); //$NON-NLS-1$
								TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(getSite().getShell(), tableResult.getColumn(i).getText(), str.toString() );
				                dlg.open();
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else{
							appendTextAtPosition(strText);
							logger.debug("\nColumn object type is" + columnObject.getClass().toString()); //$NON-NLS-1$
						}
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
		gl_compositeBtn.marginWidth = 1;
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
						String strContent = mapColumns.get(j)==null?"":mapColumns.get(j).toString(); //$NON-NLS-1$
						if(strContent.length() == 0 ) strContent = " "; //$NON-NLS-1$
						sbExportData.append(strContent).append(exportDelimit); //$NON-NLS-1$
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
		gl_compositeSQLHistory.marginWidth = 1;
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
		SQLHistoryCreateColumn.createTableHistoryColumn(tableViewerSQLHistory, sorterHistory, layoutColumnLayout, false);
		
		tableViewerSQLHistory.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerSQLHistory.setContentProvider(new ArrayContentProvider());
		tableViewerSQLHistory.setInput(listSQLHistory);
		tableViewerSQLHistory.setSorter(sorterHistory);
		
		Composite compositeRecallBtn = new Composite(compositeSQLHistory, SWT.NONE);
		compositeRecallBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeRecallBtn = new GridLayout(8, false);
		gl_compositeRecallBtn.marginWidth = 1;
		gl_compositeRecallBtn.marginHeight = 0;
		compositeRecallBtn.setLayout(gl_compositeRecallBtn);
		
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
		gl_compositeMessage.marginWidth = 1;
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
		
		GridLayout gl_compositeMessageSubBtn = new GridLayout(3, false);
		gl_compositeMessageSubBtn.marginWidth = 1;
		gl_compositeMessageSubBtn.marginHeight = 0;
		compositeMessageSub.setLayout(gl_compositeMessageSubBtn);
		
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
			browserEvaluate(EditorFunctionService.INSERT_TEXT, strText);
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
			browserEvaluate(EditorFunctionService.APPEND_TEXT, strText);
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
//		if(DBOperationType.valueOf(userDB.getOperation_type()) == DBOperationType.PRODUCTION) {
	    	browserQueryEditor.setUrl(REAL_DB_URL);
//	    } else {
//	    	browserQueryEditor.setUrl(DEV_DB_URL);
//	    }
		registerBrowserFunctions();
		
		browserQueryEditor.addProgressListener(new ProgressListener() {
			@Override
			public void completed( ProgressEvent event ) {
				try {
//					content assist기능에 테이블 정보 넣는 것은 잠시 보류합니다.
					browserEvaluate(IEditorFunction.INITIALIZE, EditorDefine.EXT_SQL, "", getInitDefaultEditorStr()); //$NON-NLS-1$
				} catch(Exception ee) {
					logger.error("rdb editor initialize ", ee); //$NON-NLS-1$
				}
			}
			public void changed( ProgressEvent event ) {}			
		});
	}
	
//	/**
//	 * 에디터에서 assist창에 보여줄 목록을 가져옵니다.
//	 * 
//	 * @return
//	 */
//	private String getAssistList() {
//		String strTablelist = ""; //$NON-NLS-1$
//		
//		try {
//			List<TableDAO> showTables = null;
//			if(userDB.getDBDefine() != DBDefine.TAJO_DEFAULT) {
//				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//				showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
//			} else {
//				showTables = new TajoConnectionManager().tableList(userDB);
//			}
//
//			for (TableDAO tableDao : showTables) {
//				strTablelist += tableDao.getName() + "|"; //$NON-NLS-1$
//			}
//			strTablelist = StringUtils.removeEnd(strTablelist, "|"); //$NON-NLS-1$
//			
//		} catch(Exception e) {
//			logger.error("MainEditor get the table list", e); //$NON-NLS-1$
//		}
//		
//		return strTablelist;
//	}
	
	/** Define.QUERY_MODE 명령을 내립니다 */
	public void executeCommand(RequestQuery reqQuery) {
		execute(reqQuery);
		resultFolderSel(EditorDefine.RESULT_TAB_NAME.RESULT_SET);
	}

	/**
	 * initialize editor
	 */
	private void initEditor() {
		if (userDB.getDBDefine() == DBDefine.HIVE_DEFAULT || userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
			tltmAutoCommit.setEnabled(false);
		}

		if("YES".equals(userDB.getIs_autocommit())) { //$NON-NLS-1$
			tltmAutoCommit.setSelection(false);
		} else {
			tltmAutoCommit.setSelection(true);
		}
		
		// 기존 에디터에서 auto commit button 이 어떻게 설정 되어 있는지 가져옵니다.
		initAutoCommitAction(true, false);
		
		tabFolderResult.setSelection(0);
		
		registerServiceHandler();
		setInitDefaultEditorStr(initDefaultEditorStr);
	}
	
	/**
	 * init auto commit button
	 * 
	 * @param isFirst
	 * @param isRiseEvent
	 */
	private void initAutoCommitAction(boolean isFirst, boolean isRiseEvent) {
		if(isAutoCommit()) {
			tltmAutoCommit.setToolTipText("Auto Commit true"); //$NON-NLS-1$
			tltmAutoCommitCommit.setEnabled(false);
			tltmAutoCommitRollback.setEnabled(false);
			
			if(!isFirst) {
				if(MessageDialog.openConfirm(null, Messages.MainEditor_30, Messages.MainEditor_47)) {
					TadpoleSQLTransactionManager.commit(strUserEMail, userDB);
				} else {
					TadpoleSQLTransactionManager.rollback(strUserEMail, userDB);
				}
			}
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
	private void execute(final RequestQuery reqQuery) {
		if("".equals(reqQuery.getSql().trim())) return;		 //$NON-NLS-1$
		
		// 실행해도 되는지 묻는다.
		boolean isDMLQuestion = false;
		if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {						
			for (String strSQL : reqQuery.getOriginalSql().split(PublicTadpoleDefine.SQL_DILIMITER)) {							
				if(!SQLUtil.isStatement(strSQL)) {
					isDMLQuestion = true;
					break;
				}
			}
		} else {
			if(!SQLUtil.isStatement(reqQuery.getSql())) isDMLQuestion = true;
		}
		// 실행해도 되는지 묻는다.
		if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDB.getQuestion_dml())) {
			if(isDMLQuestion) if(!MessageDialog.openConfirm(null, "Confirm", Messages.MainEditor_56)) return; //$NON-NLS-1$
		}
				
		// 쿼리를 실행 합니다. 
		final SQLHistoryDAO executingSQLDAO = new SQLHistoryDAO();
		final Job jobQueryManager = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(reqQuery.getSql(), IProgressMonitor.UNKNOWN);
				
				executingSQLDAO.setStartDateExecute(new Date());
				executingSQLDAO.setIpAddress(reqQuery.getUserIp());
				try {
					// 페이지를 초기화 합니다.
					pageNumber = 1;						
					if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {
						
						executingSQLDAO.setStrSQLText(reqQuery.getOriginalSql());
						List<String> listStrExecuteQuery = new ArrayList<String>();
						for (String strSQL : reqQuery.getSql().split(PublicTadpoleDefine.SQL_DILIMITER)) {
							String strExeSQL = SQLUtil.sqlExecutable(strSQL);
							
							// execute batch update는 ddl문이 있으면 안되어서 실행할 수 있는 쿼리만 걸러 줍니다.
							if(!SQLUtil.isStatement(strExeSQL)) {
								listStrExecuteQuery.add(strExeSQL);
							} else {
								reqQuery.setSql(strExeSQL);					
							}
						}
						
						// select 이외의 쿼리 실행
						if(!listStrExecuteQuery.isEmpty()) {
							runSQLExecuteBatch(listStrExecuteQuery, reqQuery);
						}
						
						// select 문장 실행
						if(SQLUtil.isStatement(reqQuery.getSql())) { //$NON-NLS-1$
							executingSQLDAO.setStartDateExecute(new Date());
							executingSQLDAO.setStrSQLText(reqQuery.getSql());
							executingSQLDAO.setIpAddress(reqQuery.getUserIp());

							runSQLSelect(reqQuery);
							executingSQLDAO.setRows(sourceDataList.size());
						}
					} else {
						executingSQLDAO.setStrSQLText(reqQuery.getSql());
						
						if(SQLUtil.isStatement(reqQuery.getSql())) {
							runSQLSelect(reqQuery);
							executingSQLDAO.setRows(sourceDataList.size());
						} else {
							runSQLOther(reqQuery);
						}
					}
				} catch(Exception e) {
					logger.error(Messages.MainEditor_50 + reqQuery.getSql(), e);
					
					executingSQLDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.toString()); //$NON-NLS-1$
					executingSQLDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
					
					executingSQLDAO.setEndDateExecute(new Date());
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		jobQueryManager.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							// table에 데이터 표시
							executeFinish(executingSQLDAO);
						} else {
							resultTableInit();
							executeErrorProgress(jobEvent.getResult().getMessage());
						}
						
						// 쿼리 후 화면 정리 작업을 합니다.
						afterQueryInit(executingSQLDAO, reqQuery);
						
						// 주의) 일반적으로는 포커스가 잘 가지만, 
						// progress bar가 열렸을 경우 포커스가 잃어 버리게 되어 포커스를 주어야 합니다.
						setOrionTextFocus();
						
						browserEvaluate(EditorFunctionService.EXECUTE_DONE);
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		jobQueryManager.setPriority(Job.INTERACTIVE);
		jobQueryManager.setName(userDB.getDisplay_name());
		jobQueryManager.setUser(true);
		jobQueryManager.schedule();
		
//		// job이 끝날때까지 대기합니다.
//		logger.info("===[job Manager state start]==" + new Date().getTime());
//		getSite().getShell().getDisplay().syncExec(new Runnable() {
//			public void run() {
//				while(true) {
//					IStatus jobStatus = jobQueryManager.getResult();
//		//			logger.info("\t\t Job status is " + jobStatus);
//					if(jobStatus != null) break;
//					
//					try {
//						Thread.sleep(100);
//					} catch(Exception e) {}
//				}
//			}
//		});
//		logger.info("===[Job manager state end]==" +  + new Date().getTime());
	}
	/**
	 * 쿼리 후 실행결과를 히스토리화면과 프로파일에 저장합니다.
	 */
	private void afterQueryInit(SQLHistoryDAO sqltHistoryDao, final RequestQuery reqQuery) {
		try {
			TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(user_seq, userDB, PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDITOR, sqltHistoryDao);
		} catch(Exception e) {
			logger.error("save the user query", e); //$NON-NLS-1$
		}
		
		listSQLHistory.add(sqltHistoryDao);
		tableViewerSQLHistory.refresh();
	}
	
	/**
	 * auto commit 
	 * @return
	 */
	public boolean isAutoCommit() {
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
	 */
	private void runSQLSelect(final RequestQuery reqQuery) throws Exception {		
		if(!PermissionChecker.isExecute(getUserType(), userDB, reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			Map<String, Object> resultMap = new TajoConnectionManager().select(userDB, reqQuery.getSql(), pageNumber, reqQuery.isAutoCommit());
			
			mapColumnType = (Map<Integer, Integer>)resultMap.get("mapColumnType"); //$NON-NLS-1$
			
			// column name을 얻습니다. 
			// sqlite에서는 metadata를 얻은 후에 resultset을 얻어야 에러(SQLite JDBC: inconsistent internal state)가 나지 않습니다.
			mapColumns = (Map<Integer, String>)resultMap.get("mapColumns"); //$NON-NLS-1$
			
			// 결과를 프리퍼런스에서 처리한 맥스 결과 만큼만 거져옵니다.
			sourceDataList = (List<Map<Integer, Object>>)resultMap.get("sourceDataList"); //$NON-NLS-1$
			
			return;
		}  
		
		// others db
		// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
		if(transactionQuery(reqQuery.getSql())) return;
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		java.sql.Statement stmt = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB);
			}
			
			if(reqQuery.getMode() == EditorDefine.QUERY_MODE.QUERY) {
				
				String tmpExeQuery = reqQuery.getSql();
				if(reqQuery.getSql().toUpperCase().startsWith("SELECT")) { //$NON-NLS-1$
					tmpExeQuery = PartQueryUtil.makeSelect(userDB, reqQuery.getSql(), 0, queryResultCount);
					if(logger.isDebugEnabled()) logger.debug("[SELECT] " + reqQuery.getSql()); //$NON-NLS-1$
				}
				
				pstmt = javaConn.prepareStatement(tmpExeQuery);
				//  환경설정에서 원하는 조건을 입력하였을 경우.
				rs = pstmt.executeQuery();
				
			// explain
			}  else if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
				
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(DBDefine.getDBDefine(userDB) == DBDefine.CUBRID_DEFAULT) {
					
					String cubridQueryPlan = CubridExecutePlanUtils.plan(userDB, reqQuery.getSql());
					mapColumns = CubridExecutePlanUtils.getMapColumns();
					sourceDataList = CubridExecutePlanUtils.getMakeData(cubridQueryPlan);
					
					return;
					
				} else if(DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT) {					
					// generation to statement id for query plan. 
					pstmt = javaConn.prepareStatement("select USERENV('SESSIONID') from dual "); //$NON-NLS-1$
					rs = pstmt.executeQuery(); 
					String statement_id = "tadpole"; //$NON-NLS-1$
					if (rs.next()) statement_id = rs.getString(1);
					
					pstmt = javaConn.prepareStatement("delete from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					pstmt.execute(); 
					
					// 플랜결과를 디비에 저장합니다.
					OracleExecutePlanUtils.plan(userDB, reqQuery.getSql(), planTableName, javaConn, pstmt, statement_id);
					// 저장된 결과를 가져와서 보여줍니다.
					pstmt = javaConn.prepareStatement("select * from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					rs = pstmt.executeQuery(); 
				 } else if(DBDefine.MSSQL_8_LE_DEFAULT == DBDefine.getDBDefine(userDB) || DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB)) {
					 stmt = javaConn.createStatement();
					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "ON")); //$NON-NLS-1$

					 pstmt = javaConn.prepareStatement(reqQuery.getSql());
					 rs = pstmt.executeQuery();

					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "OFF")); //$NON-NLS-1$
				} else {
				
					pstmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDB, reqQuery.getSql()));
					rs = pstmt.executeQuery();
					
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
			
			if(userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT || userDB.getDBDefine() == DBDefine.HIVE_DEFAULT) {
			} else {
				// 데이터셋에 추가 결과 셋이 있을경우 모두 fetch 하여 결과 그리드에 표시한다.
				while(pstmt.getMoreResults()){  
					if(logger.isDebugEnabled()) logger.debug("\n**********has more resultset1...***********"); //$NON-NLS-1$
					sourceDataList.addAll(ResultSetUtils.getResultToList(pstmt.getResultSet(), queryResultCount, isResultComma));
				}
			}
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
	}
	
	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * 
	 * @param listQuery
	 * @throws Exception
	 */
	private void runSQLExecuteBatch(List<String> listQuery, final RequestQuery reqQuery) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), userDB, listQuery)) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB);
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

			if(reqQuery.isAutoCommit()) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}

	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param reqQuery
	 * @exception
	 */
	private void runSQLOther(RequestQuery reqQuery) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), userDB, reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			new TajoConnectionManager().executeUpdate(userDB,reqQuery.getSql());
		} else { 
		
			// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
			if(transactionQuery(reqQuery.getSql())) return;
			
			java.sql.Connection javaConn = null;
			Statement statement = null;
			try {
				if(reqQuery.isAutoCommit()) {
					SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
					javaConn = client.getDataSource().getConnection();
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB);
				}
				
				statement = javaConn.createStatement();
				
				// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다.
				if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWith(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
						reqQuery.setSql(StringUtils.replaceOnce(reqQuery.getSql(), "(", " (")); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWithIgnoreCase(checkSQL, "CREATE OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE TRIGGER") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER TRIGGER") //$NON-NLS-1$
					) { //$NON-NLS-1$
						reqQuery.setSql(reqQuery.getSql() + QUERY_DELIMITER); //$NON-NLS-1$
					}
				}
				
				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
				if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT) statement.execute(reqQuery.getSql());
				else statement.executeUpdate(reqQuery.getSql());
				
			} finally {
				try { statement.close();} catch(Exception e) {}
	
				if(reqQuery.isAutoCommit()) {
					try { javaConn.close(); } catch(Exception e){}
				}
			}
		}  	// end which db
		
		// create table, drop table이면 작동하도록			
		try {
			if(!SQLUtil.isStatement(reqQuery.getSql())) refreshExplorerView();
		} catch(Exception e) {
			logger.error("CREATE, DROP, ALTER Query refersh error" + reqQuery.getSql()); //$NON-NLS-1$
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
	 * @param executingSQLDAO 실행된 마지막 쿼리
	 */
	private void executeFinish(SQLHistoryDAO executingSQLDAO) {
		setFilter();
		
		if(SQLUtil.isStatement(executingSQLDAO.getStrSQLText())) {			
			btnPrev.setEnabled(false);
			if( sourceDataList.size() < queryPageCount ) btnNext.setEnabled(false);
			else btnNext.setEnabled(true);
		} else {
			btnPrev.setEnabled(false);
			btnNext.setEnabled(false);
		}
		
		// 쿼리의 결과를 화면에 출력합니다.
		setResultTable(executingSQLDAO);
	}
	
	/**
	 * error message 추가한다.
	 * @param msg
	 */
	private void executeErrorProgress(final String msg) {
		TadpoleMessageDAO message = new TadpoleMessageDAO(new Date(), msg);
		listMessage.add(message);
		
		tableViewerMessage.refresh();
		resultFolderSel(EditorDefine.RESULT_TAB_NAME.TADPOLE_MESSAGE);
		
		setOrionTextFocus();
	}
	
	/**
	 * tab을 선택합니다.
	 * 
	 * @param selectTab
	 */
	private void resultFolderSel(final EditorDefine.RESULT_TAB_NAME selectTab) {
		int index = (Integer)tabFolderResult.getData(selectTab.toString());
		if(tabFolderResult.getSelectionIndex() != index) {
			tabFolderResult.setSelection(index);
		}
	}
	
	/**
	 * 쿼리의 결과를 화면에 출력하거나 정리 합니다.
	 */
	private void setResultTable(SQLHistoryDAO executingSQLDAO) {
		if(SQLUtil.isStatement(executingSQLDAO.getStrSQLText())) {			
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
			long longExecuteTime = executingSQLDAO.getEndDateExecute().getTime() - executingSQLDAO.getStartDateExecute().getTime();
			String strResultMsg = sourceDataList.size() + " " + Messages.MainEditor_33 + "[" + longExecuteTime + " ms]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tableResult.setToolTipText(strResultMsg);
			sqlResultStatusLabel.setText(sourceDataList.size()  + " " +  Messages.MainEditor_33 + "[" + longExecuteTime + " ms]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			sqlFilter.setTable(tableResult);
			
			// Pack the columns
			TableUtil.packTable(tableResult);
			resultFolderSel(EditorDefine.RESULT_TAB_NAME.RESULT_SET);
		} else {
			listMessage.add(new TadpoleMessageDAO(new Date(), "success. \n\n" + executingSQLDAO.getStrSQLText())); //$NON-NLS-1$
			tableViewerMessage.refresh(listMessage);
			resultFolderSel(EditorDefine.RESULT_TAB_NAME.TADPOLE_MESSAGE);
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
		setOrionTextFocus();
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
			String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
			if(performSave(strQuery)) {
				browserEvaluate(IEditorFunction.SAVE_DATA);
			}
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", strUserEMail), e); //$NON-NLS-1$
			monitor.setCanceled(true);
		}
	}
	
	@Override
	public void doSaveAs() {
		// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
		userSetDBResource = getFileName();
		if(userSetDBResource == null) return;
		
		// 저장을 호출합니다.
		try {
			String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
			if(performSave(strQuery)) {
				browserEvaluate(IEditorFunction.SAVE_DATA);
			}
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", strUserEMail), e); //$NON-NLS-1$
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
		boolean boolReturnVal = false;
		
		// new save
		if(dBResource == null) {
			// editor가 저장 가능 상태인지 검사합니다.
			if(!isDirty()) return false; 
			
			if(userSetDBResource == null) {
				userSetDBResource = getFileName();
				if(userSetDBResource == null) return false;
			}
			
			boolReturnVal = saveData(newContents);
			
		// save as
		} if(userSetDBResource != null) {
			boolReturnVal = saveData(newContents);
			if(boolReturnVal) userSetDBResource = null;

		// update
		} else {
			try {
				TadpoleSystem_UserDBResource.updateResource(dBResource, newContents);
				boolReturnVal = true;
				setDirty(false);
			} catch (Exception e) {
				logger.error("update file", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
				
				boolReturnVal = false;
			}
		}
		
		return boolReturnVal;
	}
	
	/**
	 * save data
	 * 
	 * @param newContents
	 * @return
	 */
	private boolean saveData(String newContents) {
		try {
			// db 저장
			dBResource = TadpoleSystem_UserDBResource.saveResource(userDB, userSetDBResource, newContents);
			dBResource.setParent(userDB);
			
			// title 수정
			setPartName(userSetDBResource.getName());
			
			// tree 갱신
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SAVE_FILE, ""+dBResource.getDb_seq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			setDirty(false);
			
			userSetDBResource = null;
		} catch (Exception e) {
			logger.error("save data", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
			
			return false;
		}
		
		return true;
	}
	
	/** save property dirty */
	public void setDirty(Boolean newValue) {
		if(isDirty != newValue) {
			isDirty = newValue;
			firePropertyChange(PROP_DIRTY);
		}
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
		resultFolderSel(EditorDefine.RESULT_TAB_NAME.SQL_RECALL);
		
		// table 데이터가 있으면 첫번째 데이터를 선택합니다.
		if(listSQLHistory.size() >= 1) {
			Table tb = tableViewerSQLHistory.getTable();
			tb.select(0);
			tb.setFocus();
		}
	}

	@Override
	protected void registerBrowserFunctions() {
		editorService = new MainEditorBrowserFunctionService(browserQueryEditor, EditorFunctionService.EDITOR_SERVICE_HANDLER, this);
	}
	
	/**
	 * 에디터 로드할때 사용할 초기 쿼리 입니다.
	 * @return
	 */
	public String getInitDefaultEditorStr() {
		return initDefaultEditorStr;
	}
	/** 에디터 로드할때 초기 텍스트를 설정합니다 */
	public void setInitDefaultEditorStr(String initDefaultEditorStr) {
		this.initDefaultEditorStr = initDefaultEditorStr;
	}
}