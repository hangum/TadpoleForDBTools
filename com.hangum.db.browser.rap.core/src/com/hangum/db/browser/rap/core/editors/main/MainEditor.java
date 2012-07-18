package com.hangum.db.browser.rap.core.editors.main;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
import org.eclipse.ui.part.EditorPart;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.editors.main.browserfunction.EditorBrowserFunctionService;
import com.hangum.db.browser.rap.core.util.CubridExecutePlanUtils;
import com.hangum.db.browser.rap.core.util.OracleExecutePlanUtils;
import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.commons.sql.util.SQLUtil;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.dao.system.UserDBResourceDAO;
import com.hangum.db.define.Define;
import com.hangum.db.define.PreferenceDefine;
import com.hangum.db.dialogs.message.TadpoleMessageDialog;
import com.hangum.db.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.db.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.db.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.system.TadpoleSystem_UserDBResource;
import com.hangum.db.util.UnicodeUtils;
import com.hangum.db.util.download.DownloadServiceHandler;
import com.hangum.db.util.download.DownloadUtils;
import com.hangum.db.util.tables.DefaultViewerSorter;
import com.hangum.db.util.tables.SQLHistoryLabelProvider;
import com.hangum.db.util.tables.SQLHistorySorter;
import com.hangum.db.util.tables.SQLResultContentProvider;
import com.hangum.db.util.tables.SQLResultFilter;
import com.hangum.db.util.tables.SQLResultLabelProvider;
import com.hangum.db.util.tables.SQLResultSorter;
import com.hangum.db.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.SWTResourceManager;

/**
 * 쿼리 수행 및 검색 창.
 * @author hangumNote
 *
 */
public class MainEditor extends EditorPart {
	/** Editor ID. */
	public static final String ID = "com.hangum.db.browser.rap.core.editor.main"; //$NON-NLS-1$
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(MainEditor.class);
	
	/** resource 정보. */
	private UserDBResourceDAO dBResource;
	
	/** save mode. */
	private boolean isDirty = false;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	/** 현 쿼리의 실행모드. */
	private Define.QUERY_MODE queryMode = Define.QUERY_MODE.DEFAULT;
	
	/** edior가 초기화 될때 처음 로드 되어야 하는 String. */
	private String initDefaultEditorStr;
	/** 현재 에디터에서 처리해야하는 디비 정보. */
	private UserDBDAO userDB;
	
	/** 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. */
	private int queryResultTerm = Activator.getDefault().getPreferenceStore().getInt(PreferenceDefine.SELECT_DEFAULT_PREFERENCE);
	private String planTableName = Activator.getDefault().getPreferenceStore().getString(PreferenceDefine.ORACLE_PLAN_TABLE);
	
	/** 실행한 마지막 SELECT 문. */
	private String lastSelectQuery = ""; //$NON-NLS-1$
	private int startResultPos = 0;
	private int endResultPos = queryResultTerm;
	
	/** query의 히스토리를 보여줍니다. */
	private List<String> listQueryHistory = new ArrayList<String>();

	/** 마지막으로 실행한 query */
	private String executeLastSQL = ""; //$NON-NLS-1$
	/** query 결과의 컬럼 정보 HashMap -- table의 헤더를 생성하는 용도 <column index, Data> */
	private HashMap<Integer, String> mapColumns = null;
	/** query 의 결과 데이터  -- table의 데이터를 표시하는 용도 <column index, Data> */
	private List<HashMap<Integer, Object>> sourceDataList = new ArrayList<HashMap<Integer, Object>>();
	
//	/** sql to application string */
//	private ToolItem tltmSQLToApplication;
		
	/** 이전 버튼 */
	private Button btnPrev;
	/** 이후 버튼 */
	private Button btnNext;
	
	/** 결과 filter */
	private Text textFilter;
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	private Label sqlResultStatusLabel;
	private TableViewer sqlResultTableViewer;
	private Table tableResult;
	
	/** query 결과 창 */
	private TabFolder tabFolderResult;
	/** tab index name */
	private enum RESULT_TAB_NAME {RESULT_SET, SQL_RECALL, TADPOLE_MESSAGE};
	
	/** 쿼리결과 export */
	private Button btnSQLResultExport; 
	
	/** query history */
	private TableViewer tableViewerSQLHistory;
	private Table tableSQLHistory;
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	
	/** tadpole message */
	private TableViewer tableViewerMessage;
	private Table tableMessage;
	private List<TadpoleMessageDAO> listMessage = new ArrayList<TadpoleMessageDAO>();

	///[browser editor]/////////////////////////////////////////////////////////////////////////////////////////////////////
	private static final String URL = "orion/tadpole/editor/embeddededitor.html"; //$NON-NLS-1$
	private Browser browserQueryEditor;
	/** browser.browserFunction의 서비스 헨들러 */
	private EditorBrowserFunctionService editorService;
	
    /** 에디터의 텍스트 */
    private String queryText = ""; //$NON-NLS-1$
    /** 에디터의 커서 포인트 */
    private int queryTextPoistion = 0;
    /** query append 텍스트 */
    private String appendQueryText = ""; //$NON-NLS-1$
	///[browser editor]/////////////////////////////////////////////////////////////////////////////////////////////////
    
	// 상태 정보
	private IStatusLineManager statusLineManager;
    private StatusLineContributionItem position;
    private StatusLineContributionItem keyMode;
    private StatusLineContributionItem writeMode;
    
	public MainEditor() {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		
		MainEditorInput qei = (MainEditorInput)input;
		userDB = qei.getUserDB();
		
		dBResource = qei.getResourceDAO();
		if(dBResource == null) setPartName(qei.getName());
		else setPartName(dBResource.getFilename());
		
		initDefaultEditorStr = qei.getDefaultStr();
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
		
		// property change event
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (PreferenceDefine.SELECT_DEFAULT_PREFERENCE.equalsIgnoreCase(event.getProperty())) {
					queryResultTerm = Integer.valueOf(event.getNewValue().toString());
				}
			}
		});
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		SashForm sashForm = new SashForm(composite, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setBounds(0, 0, 64, 64);
		
		final Composite compositeEditor = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeEditor = new GridLayout(1, false);
		gl_compositeEditor.verticalSpacing = 0;
		gl_compositeEditor.marginHeight = 0;
		compositeEditor.setLayout(gl_compositeEditor);
		
		ToolBar toolBar = new ToolBar(compositeEditor, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_QUERY_FUNCTION);
				} catch(Exception ee) {
					logger.error("execute query", ee); //$NON-NLS-1$
				}
			}
		});
		tltmExecute.setText(Messages.MainEditor_2);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecuteAll = new ToolItem(toolBar, SWT.NONE);
		tltmExecuteAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_QUERY_ALL_FUNCTION);
				} catch(Exception ee) {
					logger.error("execute query", ee); //$NON-NLS-1$
				}
			}
		});
		tltmExecuteAll.setText(Messages.MainEditor_tltmExecuteAll_text);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExplainPlanctrl = new ToolItem(toolBar, SWT.NONE);
		tltmExplainPlanctrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_PLAN_FUNCTION);
				} catch(Exception ee) {
					logger.error("execute plan", ee); //$NON-NLS-1$
				}
			}
		});
		tltmExplainPlanctrl.setText(Messages.MainEditor_3);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSort = new ToolItem(toolBar, SWT.NONE);
		tltmSort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_EXECUTE_FORMAT_FUNCTION);
				} catch(Exception ee) {
					logger.error("execute format", ee); //$NON-NLS-1$
				}
			}
		});
		tltmSort.setText(Messages.MainEditor_4);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSQLToApplication = new ToolItem(toolBar, SWT.NONE);
		tltmSQLToApplication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SQL_TO_APPLICATION);
				} catch(Exception ee) {
					logger.error("execute format", ee); //$NON-NLS-1$
				}
			}
		});
//		SQLToApplicationSelectionListener sqlToApplicationListener = new SQLToApplicationSelectionListener(this, tltmSQLToApplication);
//		for(Define.SQL_TO_APPLICATION app : Define.SQL_TO_APPLICATION.values()) {
//			sqlToApplicationListener.add(app.toString());
//		}
//	    tltmSQLToApplication.addSelectionListener(sqlToApplicationListener);
	    tltmSQLToApplication.setToolTipText("SQL statement to Application code"); //$NON-NLS-1$
	    tltmSQLToApplication.setText("SQL to Application");
	    
	    ////// tool bar end ///////////////////////////////////////////////////////////////////////////////////
	    
	    browserQueryEditor = new Browser(compositeEditor, SWT.BORDER);
	    browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    
//	    browserQueryEditor.setText("about:blank"); //$NON-NLS-1$
	    browserQueryEditor.setUrl(URL);
		addBrowserHandler();
		createStatusLine();
		
		Composite compositeResult = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		compositeResult.setLayout(gl_compositeResult);
		
		tabFolderResult = new TabFolder(compositeResult, SWT.NONE);
		tabFolderResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolderResult.setBounds(0, 0, 124, 43);
		
		// tab 의 index를 설정한다.
		tabFolderResult.setData(RESULT_TAB_NAME.RESULT_SET.toString(), 0);
		tabFolderResult.setData(RESULT_TAB_NAME.SQL_RECALL.toString(), 1);
		tabFolderResult.setData(RESULT_TAB_NAME.TADPOLE_MESSAGE.toString(), 2);
		
		TabItem tbtmResult = new TabItem(tabFolderResult, SWT.NONE);
		tbtmResult.setText(Messages.MainEditor_7);
		
		Composite compositeQueryResult = new Composite(tabFolderResult, SWT.NONE);
		tbtmResult.setControl(compositeQueryResult);
		compositeQueryResult.setLayout(new GridLayout(2, false));
		
		Label lblFilter = new Label(compositeQueryResult, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.MainEditor_lblFilter_text);
		
		textFilter = new Text(compositeQueryResult, SWT.BORDER);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		sqlResultTableViewer = new TableViewer(compositeQueryResult, SWT.BORDER | SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.MULTI);
		sqlResultTableViewer.setUseHashlookup(true);
		tableResult = sqlResultTableViewer.getTable();
		tableResult.addListener(SWT.MouseDoubleClick, new Listener() {
		    public void handleEvent(Event event) {
		        Point pt = new Point(event.x, event.y);
		        TableItem ti = tableResult.getItem(pt);
		        if (ti == null) return;
		        
		        for (int i = 0; i < tableResult.getItemCount(); i++) {
		            Rectangle rect = ti.getBounds(i);
		            if (rect.contains(pt)) {
		            	String msg = ti.getText(i);
		            	if("".equals(msg.trim())) return; //$NON-NLS-1$
		            	
		                TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(getSite().getShell(), tableResult.getColumn(i).getText(), msg);
		                dlg.open();
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
				startResultPos -= queryResultTerm;
				endResultPos = queryResultTerm;
				
				if(startResultPos < 0) {
					startResultPos = 0;
					endResultPos = queryResultTerm;
					
					return;
				} else if(startResultPos == 0) {
					if(btnPrev.isEnabled()) btnPrev.setEnabled(false);
				}
				if(!btnNext.isEnabled()) btnNext.setEnabled(true);
				
				runSQLSelectProgress(lastSelectQuery, startResultPos, endResultPos); 
			}
		});
		btnPrev.setText(Messages.MainEditor_8);
		
		btnNext = new Button(compositeBtn, SWT.NONE);
		btnNext.setEnabled(false);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startResultPos += queryResultTerm;
				endResultPos = queryResultTerm;
 
				runSQLSelectProgress(lastSelectQuery, startResultPos, endResultPos); 
				
				// 결과 후 버튼 처리
				if(!btnPrev.isEnabled()) btnPrev.setEnabled(true);
				if( sourceDataList.size() < queryResultTerm ) {
					btnNext.setEnabled(false);
				}
			}
		});
		btnNext.setText(Messages.MainEditor_9);
		
		final Composite compositeDumy = new Composite(compositeBtn, SWT.NONE);
		compositeDumy.setLayout(new GridLayout(1, false));
		compositeDumy.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnSQLResultExport = new Button(compositeBtn, SWT.NONE);
		btnSQLResultExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
					
				// column 헤더추
				TableColumn[] tcs = sqlResultTableViewer.getTable().getColumns();
				for (TableColumn tableColumn : tcs) {
					sbExportData.append( tableColumn.getText()).append(","); //$NON-NLS-1$
				}
				sbExportData.append(Define.LINE_SEPARATOR); //$NON-NLS-1$
				
				// column 데이터 추가 
				for(int i=0; i<sourceDataList.size(); i++) {
					HashMap<Integer, Object> mapColumns = sourceDataList.get(i);
					for(int j=0; j<mapColumns.size(); j++) {
						sbExportData.append(mapColumns.get(j)).append(","); //$NON-NLS-1$
					}
					sbExportData.append(Define.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadServiceHandler.setName(userDB.getDisplay_name() + "_ResultSetExport.csv"); //$NON-NLS-1$
				downloadServiceHandler.setContent(sbExportData.toString());
				DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
			}
		});
		btnSQLResultExport.setText(Messages.MainEditor_btnExport_text);
		
		sqlResultStatusLabel = new Label(compositeBtn, SWT.NONE);
		sqlResultStatusLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));

		///////////////////// tab item //////////////////////////
		TabItem tbtmNewItem = new TabItem(tabFolderResult, SWT.NONE);
		tbtmNewItem.setText(Messages.MainEditor_10);
		
		Composite compositeSQLHistory = new Composite(tabFolderResult, SWT.NONE);
		tbtmNewItem.setControl(compositeSQLHistory);
		GridLayout gl_compositeSQLHistory = new GridLayout(1, false);
		gl_compositeSQLHistory.marginHeight = 0;
		compositeSQLHistory.setLayout(gl_compositeSQLHistory);
		
		tableViewerSQLHistory = new TableViewer(compositeSQLHistory, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewerSQLHistory.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object selElement = is.getFirstElement();
				if(selElement instanceof SQLHistoryDAO) {
					SQLHistoryDAO tmd = (SQLHistoryDAO)selElement;
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(getSite().getShell(), Messages.MainEditor_11, SQLHistoryLabelProvider.dateToStr(tmd.getDateExecute()), tmd.getStrSQLText() );
					dlg.open();
				}
			}
		});
		
		tableSQLHistory = tableViewerSQLHistory.getTable();
		tableSQLHistory.setLinesVisible(true);
		tableSQLHistory.setHeaderVisible(true);
		tableSQLHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableSQLHistory.setBounds(0, 0, 85, 85);
		tableSQLHistory.setSortDirection(SWT.DOWN);
		
		SQLHistorySorter sorterHistory = new SQLHistorySorter();
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerSQLHistory, SWT.NONE);
		TableColumn tblclmnDate = tableViewerColumn.getColumn();
		tblclmnDate.setWidth(140);
		tblclmnDate.setText(Messages.MainEditor_14);
		tblclmnDate.addSelectionListener(getSelectionAdapter(tableViewerSQLHistory, sorterHistory, tblclmnDate, 0));
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerSQLHistory, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_1.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText(Messages.MainEditor_15);
		tblclmnSql.addSelectionListener(getSelectionAdapter(tableViewerSQLHistory, sorterHistory, tblclmnSql, 1));
		
		tableViewerSQLHistory.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerSQLHistory.setContentProvider(new ArrayContentProvider());
		tableViewerSQLHistory.setInput(listSQLHistory);
		tableViewerSQLHistory.setComparator(sorterHistory);
		
		Composite compositeRecallBtn = new Composite(compositeSQLHistory, SWT.NONE);
		compositeRecallBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeRecallBtn.setLayout(new GridLayout(2, false));
		
		final Button btnExportHistory = new Button(compositeRecallBtn, SWT.NONE);
		btnExportHistory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection is = (IStructuredSelection)tableViewerSQLHistory.getSelection();
				if(!is.isEmpty()) {
					try {
						setAppendQueryText(getHistoryTabelSelectData() + Define.SQL_DILIMITER); //$NON-NLS-1$
						browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_APPEND_QUERY_TEXT);
					} catch(Exception ee){
						logger.error("history selection" , ee); //$NON-NLS-1$
					}
				}
			}
		});
		btnExportHistory.setText(Messages.MainEditor_12);
		
		
		Button btnSetEditor = new Button(compositeRecallBtn, SWT.NONE);
		btnSetEditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSetEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(SQLHistoryDAO dao : listSQLHistory) {
					sbExportData.append( dao.getStrSQLText() ).append(Define.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadServiceHandler.setName(userDB.getDisplay_name() + "_ReCallSQLExport.txt"); //$NON-NLS-1$
				downloadServiceHandler.setContent(sbExportData.toString());
				DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
			}
		});
		btnSetEditor.setText(Messages.MainEditor_17);
		
		///////////////////// tab Message //////////////////////////
		TabItem tbtmMessage = new TabItem(tabFolderResult, SWT.NONE);
		tbtmMessage.setText(Messages.MainEditor_0);
		
		Composite compositeMessage = new Composite(tabFolderResult, SWT.NONE);
		tbtmMessage.setControl(compositeMessage);
		GridLayout gl_compositeMessage = new GridLayout(1, false);
		gl_compositeMessage.marginHeight = 0;
		compositeMessage.setLayout(gl_compositeMessage);
		
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
		tableMessage.setBounds(0, 0, 85, 85);
		tableMessage.setSortDirection(SWT.DOWN);
		
		SQLHistorySorter sorterMessage = new SQLHistorySorter();
		tableViewerColumn = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		tblclmnDate = tableViewerColumn.getColumn();
		tblclmnDate.setWidth(140);
		tblclmnDate.setText(Messages.MainEditor_14);
		tblclmnDate.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnDate, 0));
				
		tableViewerColumn_1 = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		tblclmnSql = tableViewerColumn_1.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText("Message"); //$NON-NLS-1$
		tblclmnSql.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnSql, 1));
		
		tableViewerMessage.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerMessage.setContentProvider(new ArrayContentProvider());
		tableViewerMessage.setInput(listMessage);
		tableViewerMessage.setComparator(sorterMessage);
		
		Composite compositeMessageSub = new Composite(compositeMessage, SWT.NONE);
		compositeMessageSub.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeMessageSub.setLayout(new GridLayout(1, false));
		
		Button btnExportMessage = new Button(compositeMessageSub, SWT.NONE);
		btnExportMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportMessage.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			StringBuffer sbExportData = new StringBuffer();
			
			for(TadpoleMessageDAO dao : listMessage) {
				sbExportData.append( dao.getStrMessage() ).append(Define.LINE_SEPARATOR); //$NON-NLS-1$
			}
			
			downloadServiceHandler.setName(userDB.getDisplay_name() + "_Message.txt"); //$NON-NLS-1$
			downloadServiceHandler.setContent(sbExportData.toString());
			DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
		}
		});
		btnExportMessage.setText(Messages.MainEditor_43);
		/////////////////////// end tap item /////////////////////////////////////////////
		
		sashForm.setWeights(new int[] {65, 35});
		
		getEditorSite().getActionBars().getStatusLineManager().setMessage(Messages.MainEditor_18);
		initEditor();
	}
	
	/**
	 * tablecolumn adapter
	 * @param column
	 * @param index
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewer viewer, final DefaultViewerSorter comparator, final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				
				viewer.getTable().setSortDirection(comparator.getDirection());
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}
	
	/**
	 * brower handler
	 */
	private void addBrowserHandler() {
		 browserQueryEditor.addProgressListener( new ProgressListener() {
		      public void completed( ProgressEvent event ) {
		    	  browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_INIT_EMBEDDED_EDITOR); //$NON-NLS-1$
		      }
		      public void changed( ProgressEvent event ) {}
		 });
		 
		 registerBrowserFunctions();
	}
	
	private void registerBrowserFunctions() {
		editorService = new EditorBrowserFunctionService(browserQueryEditor, EditorBrowserFunctionService.EDITOR_SERVICE_HANDLER, this);
	}
	
	private void unregisterBrowserFunctions() {
		if(editorService != null && editorService instanceof BrowserFunction) {
			editorService.dispose();
		}
	}
	
	/** Define.QUERY_MODE 명령을 내립니다 */
	public void executeCommand(Define.QUERY_MODE mode) {
		queryMode = mode;
		execute();
		
		resultFolderSel(RESULT_TAB_NAME.RESULT_SET);
	}

	/**
	 * 데이터 초기화 합니다.
	 */
	private void initEditor() {
		registerServiceHandler();
		setOrionText( initDefaultEditorStr );
	}

	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}

	/**
	 * sql history 데이터를 만듭니다.
	 */
	private void sqlHistory(String executeSQL) {
		
		boolean isAlready = false;
		for(SQLHistoryDAO history : listSQLHistory) {
			if(history.getStrSQLText().equals(StringUtils.trimToEmpty(executeSQL))) isAlready = true;
		}
		
		if(!isAlready) {
			// 쿼리의 실행결과 히스토리를 생성합니다.
			SQLHistoryDAO sqlHistoryDAO = new SQLHistoryDAO(new Date(), StringUtils.trimToEmpty(executeSQL));
			listSQLHistory.add(sqlHistoryDAO);
			tableViewerSQLHistory.refresh();
		}
	}
	
	/**
	 * sql history를 텍스트로 만듭니다.
	 * @return
	 * @throws Exception
	 */
	private String getHistoryTabelSelectData() throws Exception {
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
	public void execute() {
		listQueryHistory = new ArrayList<String>();
		
		
		// job
		Job job = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.MainEditor_46, IProgressMonitor.UNKNOWN);
				
				try {
					int tmpStartPoint 	= getOrionTextPosition();	// cursor의 시작 포인트
					String tmpStrSelText= StringUtils.trimToEmpty(getOrionText());
					if(tmpStartPoint != -999) {//"".equals(tmpStrSelText.trim())) { //$NON-NLS-1$
						
						tmpStrSelText = SQLTextUtil.executeQuery(tmpStrSelText, tmpStartPoint);
					}
					if("".equals(tmpStrSelText)) return Status.OK_STATUS; //$NON-NLS-1$
					
					/////////////////////////////////////////////////////////////////////////////////////////
//					logger.debug("[original] =========================================\r\n]" + tmpStrSelText);
//					logger.debug("######################################################################");
					String[] strArrySQLS = UnicodeUtils.getUnicode(tmpStrSelText).split(Define.SQL_DILIMITER); 	//$NON-NLS-1$
//				
//					logger.debug("[processing] =========================================\r\n]" + strArrySQLS[0]);
//					logger.debug("######################################################################");
					
					for (String strSQL : strArrySQLS) {
						
						if(monitor.isCanceled()) {
							monitor.done();
							return Status.CANCEL_STATUS;
						}

						// 구분자 ;로 여러개의 쿼리를 실행했으면
						if(strSQL.endsWith( Define.SQL_DILIMITER )) { 				//$NON-NLS-1$
							strSQL = strSQL.substring(0, strSQL.length()-1);
						}
						
						listQueryHistory.add(strSQL);						
						
						// 모니터링 쿼리
						monitor.subTask(executeLastSQL);
						monitor.setTaskName(executeLastSQL);

						// 쿼리를 수행할수 있도록 가공합니다.
						executeLastSQL = SQLUtil.executeQuery(strSQL);
						
						if(executeLastSQL.toUpperCase().startsWith("SHOW") ||  //$NON-NLS-1$
								executeLastSQL.toUpperCase().startsWith("SELECT") ||  //$NON-NLS-1$
									executeLastSQL.toUpperCase().startsWith("DESCRIBE") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							
							startResultPos = 0;
							endResultPos = queryResultTerm;	
							
							runSQLSelect(executeLastSQL); //$NON-NLS-1$ //$NON-NLS-2$									
						}
						else runSQLOther(executeLastSQL);
					}	// end query for
				} catch(Exception e) {
					logger.error(Messages.MainEditor_50 + executeLastSQL, e);
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
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
							// query history
							for (String strQuery : listQueryHistory) sqlHistory(strQuery);
							
							// table에 데이터 표시
							executeFinish();
							
							// 쿼리 실행후에 결과 테이블에 포커스가 가도록
							setOrionTextFocus();
						} else {
							resultTableInit();
							executeErrorProgress(jobEvent.getResult().getMessage());
						}
						
					}
					
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}

	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param selText
	 */
	private void runSQLOther(String selText) throws Exception {
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			Statement stmt = javaConn.createStatement();
			boolean boolResult = stmt.execute( selText );
			
			// create table, drop table이면 작동하도록
			String upperQuery = selText.toUpperCase();
			if(upperQuery.startsWith("CREATE TABLE") || upperQuery.startsWith("DROP TABLE")) { //$NON-NLS-1$ //$NON-NLS-2$
				
				try {
					ExplorerViewer.CHANGE_TYPE changeType = ExplorerViewer.CHANGE_TYPE.DEL;
					String changeTbName = ""; //$NON-NLS-1$
					if(upperQuery.startsWith("CREATE TABLE")) { //$NON-NLS-1$
						changeType = ExplorerViewer.CHANGE_TYPE.INS;
						changeTbName = StringUtils.trimToEmpty(selText.substring(12));
					} else {
						changeTbName = StringUtils.trimToEmpty(selText.substring(10));
					}
					changeTbName = changeTbName.split(" ")[0]; //$NON-NLS-1$
					if(logger.isDebugEnabled()) logger.debug("[change name]" + changeTbName); //$NON-NLS-1$
				
					refreshExplorerView(changeType, changeTbName);
				} catch(Exception e) {
					logger.error("CREATE, DROP TABLE Query refersh error", e); //$NON-NLS-1$
				}
			}
		} finally {
			try { javaConn.close(); } catch(Exception e){}
		}		
	}

	/**
	 * ExplorerViewer view를 리프레쉬합니다.
	 * 
	 * @param changeType
	 * @param changeTbName
	 */
	private void refreshExplorerView(final ExplorerViewer.CHANGE_TYPE changeType, final String changeTbName) {

		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
					ev.refreshCurrentTab(userDB, changeType, changeTbName);
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
	private void executeFinish() {
		setFilter();
		
		if(executeLastSQL.toUpperCase().startsWith("SHOW") ||  //$NON-NLS-1$
				executeLastSQL.toUpperCase().startsWith("SELECT") ||  //$NON-NLS-1$
					executeLastSQL.toUpperCase().startsWith("DESCRIBE") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			lastSelectQuery = executeLastSQL;
			 
			btnPrev.setEnabled(false);
			if( sourceDataList.size() < queryResultTerm ) btnNext.setEnabled(false);
			else btnNext.setEnabled(true);
		} else {
			btnPrev.setEnabled(false);
			btnNext.setEnabled(false);
		}
		
		// 쿼리의 결과를 화면에 출력합니다.
		setResultTable();
	}
	
	/**
	 * 1) 프로그래스바의 쿼리 마지막을 처리 합니다.
	 * 2) filter를 설정한다.
	 * 
	 * @param lastQuery 실행된 마지막 쿼리
	 */
	private void executeFinishProgress() {
		setFilter();
		
		if(executeLastSQL.toUpperCase().startsWith("SHOW") ||  //$NON-NLS-1$
				executeLastSQL.toUpperCase().startsWith("SELECT") ||  //$NON-NLS-1$
					executeLastSQL.toUpperCase().startsWith("DESCRIBE") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			lastSelectQuery = executeLastSQL;
		
		
			// 쿼리의 결과를 화면에 출력합니다.
			setResultTable();
			resultFolderSel(RESULT_TAB_NAME.RESULT_SET);
		}
		
		setOrionTextFocus();
	}
	
	
	
	/**
	 * error message 추가한다.
	 * @param msg
	 */
	private void executeErrorProgress(final String msg) {
		TadpoleMessageDAO message = new TadpoleMessageDAO(new Date(), msg);
		listMessage.add(message);
		
		tableViewerMessage.refresh(listMessage);
		resultFolderSel(RESULT_TAB_NAME.TADPOLE_MESSAGE);
		
		setOrionTextFocus();
	}
	
	/**
	 * tab을 선택합니다.
	 * 
	 * @param TAB
	 */
	private void resultFolderSel(final RESULT_TAB_NAME TAB) {
		int index = (Integer)tabFolderResult.getData(TAB.toString());
		if(tabFolderResult.getSelectionIndex() != index) {
			tabFolderResult.setSelection(index);
		}
	}
	
	/**
	 * 쿼리의 결과를 화면에 출력하거나 정리 합니다.
	 */
	private void setResultTable() {
		if(executeLastSQL.toUpperCase().startsWith("SHOW") ||  //$NON-NLS-1$
				executeLastSQL.toUpperCase().startsWith("SELECT") ||  //$NON-NLS-1$
					executeLastSQL.toUpperCase().startsWith("DESCRIBE") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			// table data를 생성한다.
			sqlSorter = new SQLResultSorter(-999);
			
			SQLResultLabelProvider.createTableColumn(sqlResultTableViewer, mapColumns, sqlSorter);
			sqlResultTableViewer.setLabelProvider( new SQLResultLabelProvider() );
			sqlResultTableViewer.setContentProvider(new SQLResultContentProvider(sourceDataList) );
			sqlResultTableViewer.setInput(sourceDataList);
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
	 * 결과 테이블을 초기화 상태로 만듭니다.
	 */
	private void resultTableInit() {
		// rs set의 결과를 테이블에 출력하기 위해 입력한다.
		sourceDataList = new ArrayList<HashMap<Integer, Object>>();
		
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
	
	/**
	 * 쿼리를 요청합니다.
	 * 
	 * @param selText
	 * @throws Exception
	 */
	private void runSQLSelect(String selText) throws Exception {
		runSQLSelect(selText, startResultPos, endResultPos);
	}
	
	/**
	 * prev, next 버튼을 클릭했을 경우 처리 버튼
	 * 
	 * @param requestQuery
	 * @param startResultPos
	 * @param endResultPos
	 */
	private void runSQLSelectProgress(final String requestQuery, final int startResultPos, final int endResultPos) {
		// job
		Job job = new Job(Messages.MainEditor_58) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.MainEditor_59, IProgressMonitor.UNKNOWN);
				
				try{
					monitor.subTask(requestQuery);
					runSQLSelect(requestQuery, startResultPos, endResultPos);
				} catch(Exception e) {
					logger.error(Messages.MainEditor_60 + executeLastSQL, e);
					
					return new Status(Status.WARNING,Activator.PLUGIN_ID, Messages.MainEditor_26 + e.getMessage());
				} finally {
					monitor.done();
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
							executeFinishProgress();
						} else {
							resultTableInit();
							executeErrorProgress(jobEvent.getResult().getMessage());
						}
					}
				});	// end display.asyncExec				
			}	// end done
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}

	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 * @param startResultPos
	 * @param endResultPos
	 */
	private void runSQLSelect(String requestQuery, int startResultPos, int endResultPos) throws Exception {
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			if(Define.QUERY_MODE.DEFAULT == queryMode) {
				
				if( requestQuery.toUpperCase().startsWith("SELECT") ) { //$NON-NLS-1$
					requestQuery = PartQueryUtil.makeSelect(userDB, requestQuery, startResultPos, endResultPos);
					if(logger.isDebugEnabled()) logger.debug("[SELECT] " + requestQuery); //$NON-NLS-1$
				}
				
				stmt = javaConn.prepareStatement(requestQuery);
			//  환경설정에서 원하는 조건을 입력하였을 경우.
				rs = stmt.executeQuery();//Query( selText );
				
			// explain
			}  else if(Define.QUERY_MODE.EXPLAIN_PLAN == queryMode) {
				
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(DBDefine.getDBDefine(userDB.getType()) == DBDefine.CUBRID_DEFAULT) {
					
					String cubridQueryPlan = CubridExecutePlanUtils.plan(userDB, requestQuery);
					mapColumns = CubridExecutePlanUtils.getMapColumns();
					sourceDataList = CubridExecutePlanUtils.getMakeData(cubridQueryPlan);
					
					return;
					
				} else if(DBDefine.getDBDefine(userDB.getType()) == DBDefine.ORACLE_DEFAULT) {
					
					OracleExecutePlanUtils.plan(userDB, requestQuery, planTableName);
					stmt = javaConn.prepareStatement("select * from " + planTableName);
					rs = stmt.executeQuery();
					
				} else {
				
					stmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDB, requestQuery));
					rs = stmt.executeQuery();
					
				}
			}

			//////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////
//			// table column을 생성한다.
//			ResultSetMetaData  rsm = rs.getMetaData();
//			int columnCount = rs.getMetaData().getColumnCount();
//			
//			logger.debug("### [Table] [start ]### [column count]" + rsm.getColumnCount() + "#####################################################################################################");
//			for(int i=0;i<rs.getMetaData().getColumnCount(); i++) {
//				logger.debug("\t ==[column start]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
//				logger.debug("\tColumnLabel  		:  " 	+ rsm.getColumnLabel(i+1));
//				
//				logger.debug("\t AutoIncrement  	:  " 	+ rsm.isAutoIncrement(i+1));
//				logger.debug("\t Nullable		  	:  " 	+ rsm.isNullable(i+1));
//				logger.debug("\t CaseSensitive  	:  " 	+ rsm.isCaseSensitive(i+1));
//				logger.debug("\t Currency		  	:  " 	+ rsm.isCurrency(i+1));
//				
//				logger.debug("\t DefinitelyWritable :  " 	+ rsm.isDefinitelyWritable(i+1));
//				logger.debug("\t ReadOnly		  	:  " 	+ rsm.isReadOnly(i+1));
//				logger.debug("\t Searchable		  	:  " 	+ rsm.isSearchable(i+1));
//				logger.debug("\t Signed			  	:  " 	+ rsm.isSigned(i+1));
////				logger.debug("\t Currency		  	:  " 	+ rsm.isWrapperFor(i+1));
//				logger.debug("\t Writable		  	:  " 	+ rsm.isWritable(i+1));
//				
//				logger.debug("\t ColumnClassName  	:  " 	+ rsm.getColumnClassName(i+1));
//				logger.debug("\t CatalogName  		:  " 	+ rsm.getCatalogName(i+1));
//				logger.debug("\t ColumnDisplaySize  :  " 	+ rsm.getColumnDisplaySize(i+1));
//				logger.debug("\t ColumnType  		:  " 	+ rsm.getColumnType(i+1));
//				logger.debug("\t ColumnTypeName 	:  " 	+ rsm.getColumnTypeName(i+1));
//				
//				logger.debug("\t Precision 			:  " 	+ rsm.getPrecision(i+1));
//				logger.debug("\t Scale			 	:  " 	+ rsm.getScale(i+1));
//				logger.debug("\t SchemaName		 	:  " 	+ rsm.getSchemaName(i+1));
//				logger.debug("\t TableName		 	:  " 	+ rsm.getTableName(i+1));
//				logger.debug("\t ==[column end]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
//			}
//			
//			
//			logger.debug("#### [Table] [end ] ########################################################################################################");
			
			////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////
			
			// rs set의 결과를 테이블에 출력하기 위해 입력한다.
			sourceDataList = new ArrayList<HashMap<Integer, Object>>();
			HashMap<Integer, Object> tmpRs = null;
			
			// 
			// sqlite에서는 metadata를 얻은 후에 resultset을 얻어야 에러(SQLite JDBC: inconsistent internal state)가 나지 않습니다.
			// 
			// table metadata를 얻습니다.
			mapColumns = SQLUtil.mataDataToMap(rs);
			
			// 결과 처리를 합니다.
			while(rs.next()) {
				tmpRs = new HashMap<Integer, Object>();
				
				for(int i=0;i<rs.getMetaData().getColumnCount(); i++) {
					tmpRs.put(i, rs.getString(i+1));
				}
				
				sourceDataList.add(tmpRs);
			}
			
		} finally {
			try { stmt.close(); } catch(Exception e) {}
			try { rs.close(); } catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
	}
	
	@Override
	public void setFocus() {
		setOrionTextFocus();
	}
	
	/**
	 * orion text setfocus
	 */
	public void setOrionTextFocus() {
		try {
			browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SET_FOCUS_FUNCTION);
		} catch(Exception e) {
			// ignore exception
		}
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
	 * orion text pocus position
	 * 
	 * @return
	 */
	public int getOrionTextPosition() {
		return this.queryTextPoistion;
	}
	
	/**
	 * set orion text position
	 * 
	 * @param queryTextPoistion
	 */
	public void setOrionTestPostion(int queryTextPoistion) {
		this.queryTextPoistion = queryTextPoistion;
	}
	
	/**
	 * set orion text
	 * 
	 * @param queryText
	 */
	public void setOrionText(String queryText) {
		this.queryText = queryText;
	}
	
	/**
	 * set position text
	 * 
	 * @param text
	 */
	public void setPositionStatus(String text) {
		position.setText(text);
	}
	
	/////[query 추가]/////////////////////////////////////////////////////////////////////////////
	public String getAppendQueryText() {
		return appendQueryText;
	}
	public void setAppendQueryText(String appendQueryText) {
		this.appendQueryText = appendQueryText;
	}
	//////////////////////////////////////////////////////////////////////////////////	

	/**
	 * <pre>
	 * set create status line
	 * 현재 리소스를 10% 정도 잡아 먹는 것으로 파악되어서 블럭 처리 합니다.
	 * 더 좋은 방법이 나올때까지는 이 방법을 사용합니다.
	 * </pre>
	 */
	private void createStatusLine() {
		statusLineManager = getEditorSite().getActionBars().getStatusLineManager();
//		IContributionItem[] ics = statusLineManager.getItems();
//		for (IContributionItem iContributionItem : ics) {
//			if("position".equals( iContributionItem.getId() )) { //$NON-NLS-1$
//				
//				position = (StatusLineContributionItem)iContributionItem;
//				return;
//			}
//		}
		
		position = new StatusLineContributionItem("position", 15); //$NON-NLS-1$
		keyMode = new StatusLineContributionItem("keyMode", 15); //$NON-NLS-1$
		writeMode = new StatusLineContributionItem("writeMode", 15); //$NON-NLS-1$
		statusLineManager.add(writeMode);
		statusLineManager.add(new Separator());
		statusLineManager.add(keyMode);
		statusLineManager.add(new Separator());
		statusLineManager.add(position);
//		
//		// Set the initial cursor position
//		position.setText("0 : 0"); //$NON-NLS-1$

		writeMode.setText(Messages.MainEditor_1);
		keyMode.setText(Messages.MainEditor_67);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		
		if(dBResource == null) {
			FileNameValidator fv = new FileNameValidator(userDB);
			InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Save", Messages.MainEditor_68, userDB.getDisplay_name(), fv); //$NON-NLS-1$
			if (dlg.open() == Window.OK) {
				saveFileName = fv.getFileName();
			}
		}
		
		// 파일 명이 있으면... 
		if(!"".equals( saveFileName) ) { //$NON-NLS-1$
			try {
				Object resultObj = browserQueryEditor.evaluate(EditorBrowserFunctionService.JAVA_SCRIPT_SAVE_FUNCTION);
				if (!(resultObj instanceof Boolean && (Boolean) resultObj)) {
					monitor.setCanceled(true);
				}
			} catch(SWTException e) {
				logger.error("doSave exception", e); //$NON-NLS-1$
				monitor.setCanceled(true);
			}	
		}
	}
	
	public boolean performSaveS(String newContents) {
		if(dBResource != null) {
			return performSave(newContents);
		}
		
		return false;
	}

	/**
	 * save
	 * 
	 * @param newContents
	 * @return
	 */
	String saveFileName = ""; //$NON-NLS-1$
	public boolean performSave(String newContents) {
		if(dBResource == null) {
//			FileNameValidator fv = new FileNameValidator(userDB);
//			InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Save", Messages.MainEditor_68, userDB.getDisplay_name(), fv); //$NON-NLS-1$
//			if (dlg.open() == Window.OK) {
			
				String absultPath = Define.RESOURCE_TYPE.SQL.toString() + userDB.getUser_seq() + userDB.getSeq();//  + userDB.getDisplay_name();
				String saveFullFileName = Define.SQL_FILE_LOCATION + absultPath + saveFileName + ".sql"; //$NON-NLS-1$

				new File(Define.SQL_FILE_LOCATION).mkdirs();
				
				try {
					fileSave(saveFullFileName, newContents);
					
					// db 저장
					dBResource = TadpoleSystem_UserDBResource.saveResource(userDB, Define.RESOURCE_TYPE.SQL, absultPath, saveFileName);
					dBResource.setParent(userDB);
					
					// title 수정
					setPartName(saveFileName);
					
					// tree 갱신
					PlatformUI.getPreferenceStore().setValue(Define.SAVE_FILE, ""+dBResource.getDb_seq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				
				} catch (Exception e) {
					logger.error("save file", e); //$NON-NLS-1$
//					MessageDialog.openError(null, Messages.MainEditor_71, Messages.MainEditor_72 + e.getMessage());
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
					
					return false;
				}
//			}
			
		} else {
			try {
				fileSave(Define.SQL_FILE_LOCATION + dBResource.getFilepath() + dBResource.getFilename() + ".sql", newContents); //$NON-NLS-1$
				
			} catch (Exception e) {
				logger.error("update file", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
				
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 파일 저장
	 * 
	 * @param saveFileName
	 * @param newContents
	 * @throws Exception
	 */
	private void fileSave(String saveFileName, String newContents) throws Exception {
	
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(saveFileName), false);
			fw.write(newContents);
			fw.flush();
			
		} finally {
			try { fw.close(); } catch(Exception e) {}
		}
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
		unregisterBrowserFunctions();
		unregisterServiceHandler();
		super.dispose();
	}

	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	/** sqleditor browser function call */
	public void browserEvaluate(String command) {
		try {
			browserQueryEditor.evaluate(command);
		} catch(Exception e) {
			logger.error("browser evaluate [ " + command + " ]", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
//	/** sql to appliation String */
//	public String getSQLToApplicationString() {
//		return tltmSQLToApplication.getText();
//	}

	@Override
	public void doSaveAs() {
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public UserDBResourceDAO getdBResource() {
		return dBResource;
	}
}
