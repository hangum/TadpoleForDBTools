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
package com.hangum.tadpole.mongodb.core.composite.result;

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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.DefaultViewerSorter;
import com.hangum.tadpole.engine.sql.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLHistorySorter;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.sql.util.tables.TreeUtil;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.define.MongoDBDefine;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewDocumentDialog;
import com.hangum.tadpole.mongodb.core.dialogs.collection.index.NewIndexDialog;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleMessageDialog;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailDialog;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * 몽고 디비 결과셋을 출력하는 콤포짖
 * 
 * @author hangum
 *
 */
public class MongodbResultComposite extends Composite {
	private static Logger logger = Logger.getLogger(MongodbResultComposite.class);
	
	/** 결과 텝창의 정보를 상수로정의 */
	private static final int TAB_POSITION_TREE_VIEW 	= 0;
	private static final int TAB_POSITION_TABLE_VIEW 	= 1;
	private static final int TAB_POSITION_JSON_VIEW 	= 2;
	private static final int TAB_POSITION_MESSAGE_VIEW 	= 3;
	
	/** preference default max count */
	private int defaultMaxCount = GetPreferenceGeneral.getMongoDefaultMaxCount();
	
	/** preference default result page */
	private String defaultResultPage = GetPreferenceGeneral.getMongoDefaultResultPage();
	
	/** data userdb*/
	private UserDBDAO userDB;
	/** collection name */
	private String collectionName;
	/** 사용자가 action을 할수 있는지 */
	private boolean isUserAction;
	
	/** 기본 검색 조건 */
	String strBasicField 	= ""; //$NON-NLS-1$
	String strBasicFind 	= ""; //$NON-NLS-1$
	String strBasicSort 	= ""; //$NON-NLS-1$
	int cntSkip 			= 0;
	int cntLimit 			= 0;
	
	/** error console 내용 */
	private StringBuffer sbConsoleErrorMsg = new StringBuffer();
	
	/** execute plan console */
	private StringBuffer sbConsoleExecuteMsg = new StringBuffer();
	
	/** result tab folder */
	private CTabFolder tabFolderMongoDB;
	
	/** tree viewer */
	private TreeViewer treeViewerMongo;
	private Label lblTreeViewCount;
	
	private Text textFilter;
	/** collection 결과 */
	private TableViewer resultTableViewer;
	private Label lblTableViewCount;
	private SashForm sashFormCollectionResult;
	
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	private Table tableTadpoleMsg;
	
	private TableViewer tableViewerMessage;
	private List<TadpoleMessageDAO> listMessage = new ArrayList<TadpoleMessageDAO>();
	
	/** browser */
	private Composite compositeExternal;
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;

	/** query 의 결과 데이터  -- table의 데이터를 표시하는 용도 <column index, Data> */
	private List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer, Object>>();
	private Map<Integer, String> mapColumns = new HashMap<Integer, String>();
	private List<MongodbTreeViewDTO> listTrees;
	
	/** label count  string */
	private String txtCnt = ""; //$NON-NLS-1$
	
	/** tadpole editor widget */
	private TadpoleEditorWidget tadpoleEditor;
	
	/**
	 * 
	 * @param parent
	 * @param style
	 * @param userDB
	 * @param collectionName
	 */
	public MongodbResultComposite(Composite parent, int style, final UserDBDAO userDB, final String collectionName, final boolean isUserAction) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.userDB = userDB;
		this.collectionName = collectionName;
		this.isUserAction = isUserAction;
		
		Composite compositeResult = new Composite(this, SWT.NONE);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 1;
		gl_compositeResult.horizontalSpacing = 1;
		gl_compositeResult.marginHeight = 1;
		gl_compositeResult.marginWidth = 1;
		compositeResult.setLayout(gl_compositeResult);
		
		tabFolderMongoDB = new CTabFolder(compositeResult, SWT.NONE);
		tabFolderMongoDB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(treeViewerMongo == null) return;
				selectData();
			}
		});
		tabFolderMongoDB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolderMongoDB.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		CTabItem tbtmTreeView = new CTabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTreeView.setText(Messages.get().MongodbResultComposite_3);
		
		Composite compositeTreeView = new Composite(tabFolderMongoDB, SWT.NONE);
		tbtmTreeView.setControl(compositeTreeView);
		GridLayout gl_compositeTreeView = new GridLayout(1, false);
		gl_compositeTreeView.verticalSpacing = 2;
		gl_compositeTreeView.horizontalSpacing = 2;
		gl_compositeTreeView.marginHeight = 2;
		gl_compositeTreeView.marginWidth = 2;
		compositeTreeView.setLayout(gl_compositeTreeView);
		
		treeViewerMongo = new TreeViewer(compositeTreeView, SWT.BORDER | SWT.VIRTUAL | SWT.FULL_SELECTION);
		treeViewerMongo.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection)treeViewerMongo.getSelection();
				if(!iss.isEmpty()) {
					MongodbTreeViewDTO rsResult = (MongodbTreeViewDTO)iss.getFirstElement();
					String strKey = rsResult.getKey();

					TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(Display.getCurrent().getActiveShell(), collectionName + " [ " + strKey + " ]", JSONUtil.getPretty(rsResult.getDbObject().toString()));					 //$NON-NLS-1$ //$NON-NLS-2$
					dlg.open();
				}
			}
		});
		Tree tree = treeViewerMongo.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createTreeColumn();
		
		treeViewerMongo.setContentProvider(new TreeMongoContentProvider() );
		treeViewerMongo.setLabelProvider(new TreeMongoLabelProvider());
		
		Composite compositeTreeViewTail = new Composite(compositeTreeView, SWT.NONE);
		compositeTreeViewTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTreeViewTail.setLayout(new GridLayout(6, false));
		
		if(isUserAction) {
			Button btnTreeInsertDocument = new Button(compositeTreeViewTail, SWT.NONE);
			btnTreeInsertDocument.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					newDocument();
				}
			});
			btnTreeInsertDocument.setText(Messages.get().MongodbResultComposite_4);
			
			Button btnTreeDeleteDocument = new Button(compositeTreeViewTail, SWT.NONE);
			btnTreeDeleteDocument.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					deleteDocumentTree();
				}
			});
			btnTreeDeleteDocument.setText(Messages.get().MongodbResultComposite_5);
			
			Button btnTreeCreateIndex = new Button(compositeTreeViewTail, SWT.NONE);
			btnTreeCreateIndex.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					createIndex();
				}
			});
			btnTreeCreateIndex.setText(Messages.get().MongodbResultComposite_6);
		
			Label labelTreeViewDumy = new Label(compositeTreeViewTail, SWT.NONE);
			labelTreeViewDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		
		lblTreeViewCount = new Label(compositeTreeViewTail, SWT.NONE);
		lblTreeViewCount.setText(Messages.get().MongodbResultComposite_7);
		
		CTabItem tbtmTableView = new CTabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTableView.setText(Messages.get().MongodbResultComposite_8);
		
		sashFormCollectionResult = new SashForm(tabFolderMongoDB, SWT.VERTICAL);
		tbtmTableView.setControl(sashFormCollectionResult);
		
		Composite compositeBodyTable = new Composite(sashFormCollectionResult, SWT.VERTICAL);
		GridLayout gl_compositeBodyTable = new GridLayout(2, false);
		gl_compositeBodyTable.marginWidth = 2;
		gl_compositeBodyTable.marginHeight = 2;
		gl_compositeBodyTable.verticalSpacing = 2;
		gl_compositeBodyTable.horizontalSpacing = 2;
		compositeBodyTable.setLayout(gl_compositeBodyTable);
		
		Label lblFilter = new Label(compositeBodyTable, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.get().Filter);
		
		textFilter = new Text(compositeBodyTable, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		resultTableViewer = new TableViewer(compositeBodyTable, SWT.BORDER | SWT.VIRTUAL | SWT.FULL_SELECTION);
		resultTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection)resultTableViewer.getSelection();
				if(!iss.isEmpty()) {
					HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)iss.getFirstElement();
//					String jsonString = rsResult.get(MongoDBDefine.PRIMARY_ID_KEY).toString();
					
					DBObject dbObject = (DBObject)rsResult.get(MongoDBDefine.PRIMARY_ID_KEY);
					
					FindOneDetailDialog dlg = new FindOneDetailDialog(null, userDB, collectionName, dbObject);
					dlg.open();
				}
			}
		});
		Table table = resultTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		sqlFilter.setTable(table);
		
		Composite compositeTail = new Composite(compositeBodyTable, SWT.NONE);
		compositeTail.setLayout(new GridLayout(10, false));
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		if(isUserAction) {
			Button btnInsertDocument = new Button(compositeTail, SWT.NONE);
			btnInsertDocument.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					newDocument();
				}
			});
			btnInsertDocument.setText(Messages.get().MongodbResultComposite_13);
			
			Button btnDeleteDocument = new Button(compositeTail, SWT.NONE);
			btnDeleteDocument.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					deleteDocumentTable();
				}
			});
			btnDeleteDocument.setText(Messages.get().MongodbResultComposite_14);
			
			Button btnCreateIndex = new Button(compositeTail, SWT.NONE);
			btnCreateIndex.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					 createIndex();
				}
			});
			btnCreateIndex.setText(Messages.get().MongodbResultComposite_15);
		
			Label labelTableDumy = new Label(compositeTail, SWT.NONE);
			GridData gd_labelTableDumy = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_labelTableDumy.widthHint = 5;
			labelTableDumy.setLayoutData(gd_labelTableDumy);
		}
		
		Button btnExportCSV = new Button(compositeTail, SWT.NONE);
		btnExportCSV.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportDataBody = new StringBuffer();
				
				// fixed : https://github.com/hangum/TadpoleForDBTools/issues/284
				// column 헤더추가.
				String tmpStrHead = "";
				TableColumn[] tcs = resultTableViewer.getTable().getColumns();
				for (TableColumn tableColumn : tcs) {
					tmpStrHead += tableColumn.getText() + ", "; //$NON-NLS-1$
				}
				tmpStrHead = StringUtils.removeEnd(tmpStrHead, ", ");
				tmpStrHead += PublicTadpoleDefine.LINE_SEPARATOR; //$NON-NLS-1$
				
				// column 데이터 추가. 
				for(int i=0; i<sourceDataList.size(); i++) {
					String tmpData = "";
					Map<Integer, Object> mapColumns = sourceDataList.get(i);
					for(int j=0; j<tcs.length; j++) {
						tmpData += mapColumns.get(j) + ", "; //$NON-NLS-1$
					}
					tmpData = StringUtils.removeEnd(tmpData, ", ");
					tmpData += PublicTadpoleDefine.LINE_SEPARATOR;
					
					sbExportDataBody.append(tmpData); //$NON-NLS-1$
				}
				
				downloadServiceHandler.setName(userDB.getDisplay_name() + "_ResultSetExport.csv"); //$NON-NLS-1$
				downloadServiceHandler.setByteContent((tmpStrHead + sbExportDataBody.toString()).getBytes());
				DownloadUtils.provideDownload(compositeExternal, downloadServiceHandler.getId());
			}
		});
		btnExportCSV.setText(Messages.get().MongodbResultComposite_16);
		
		compositeExternal = new Composite(compositeTail, SWT.NONE);
		compositeExternal.setLayout(new GridLayout(1, false));
		compositeExternal.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(compositeTail, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblTableViewCount = new Label(compositeTail, SWT.NONE);
		
		// JSON View
		CTabItem tbtmJSONView = new CTabItem(tabFolderMongoDB, SWT.NONE);
		tbtmJSONView.setText("JSON View");
		
		Composite compositeJSONView = new Composite(tabFolderMongoDB, SWT.NONE);
		tbtmJSONView.setControl(compositeJSONView);
		compositeJSONView.setLayout(new GridLayout(1, false));
		
		tadpoleEditor = new TadpoleEditorWidget(compositeJSONView, SWT.BORDER, EditorDefine.EXT_JSON, "", "");
		tadpoleEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		// tabpole message
		CTabItem tbtmTadpoleMessage = new CTabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTadpoleMessage.setText(Messages.get().MongodbResultComposite_17);
		
		Composite compositeTadpoleMsg = new Composite(tabFolderMongoDB, SWT.NONE);
		tbtmTadpoleMessage.setControl(compositeTadpoleMsg);
		compositeTadpoleMsg.setLayout(new GridLayout(1, false));
		
		tableViewerMessage = new TableViewer(compositeTadpoleMsg, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerMessage.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object selElement = is.getFirstElement();
				if(selElement instanceof TadpoleMessageDAO) {
					TadpoleMessageDAO tmd = (TadpoleMessageDAO)selElement;
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(null, "Message", SQLHistoryLabelProvider.dateToStr(tmd.getDateExecute()), tmd.getStrMessage() ); //$NON-NLS-1$
					dlg.open();
				}
			}
		});
		tableTadpoleMsg = tableViewerMessage.getTable();
		tableTadpoleMsg.setHeaderVisible(true);
		tableTadpoleMsg.setLinesVisible(true);
		tableTadpoleMsg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableTadpoleMsg.setSortDirection(SWT.DOWN);
		SQLHistorySorter sorterMessage = new SQLHistorySorter();
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		TableColumn tblclmnDate = tableViewerColumn.getColumn();
		tblclmnDate.setWidth(140);
		tblclmnDate.setText(Messages.get().Date);
		tblclmnDate.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnDate, 0));
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_1.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText(Messages.get().MongodbResultComposite_19);
		tblclmnSql.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnSql, 1));
		
		tableViewerMessage.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerMessage.setContentProvider(new ArrayContentProvider());
		tableViewerMessage.setInput(listMessage);
		tableViewerMessage.setComparator(sorterMessage);
		
		Composite composite = new Composite(compositeTadpoleMsg, SWT.NONE);		
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		Button btnExportTadpoleMessage = new Button(composite, SWT.NONE);
		btnExportTadpoleMessage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(TadpoleMessageDAO dao : listMessage) {
					sbExportData.append( dao.getStrMessage() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadServiceHandler.setName(userDB.getDisplay_name() + "_Message.txt"); //$NON-NLS-1$
				downloadServiceHandler.setByteContent(sbExportData.toString().getBytes());
				DownloadUtils.provideDownload(compositeExternal, downloadServiceHandler.getId());
			}
		});
		btnExportTadpoleMessage.setText(Messages.get().MongodbResultComposite_20);
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnClear = new Button(composite, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listMessage.clear();
				tableViewerMessage.refresh();
			}
		});
		btnClear.setText(Messages.get().MongodbResultComposite_21);
		
		registerServiceHandler();		
		firstTabInit();
	}
	
	public void find(String strBasicField) {
		this.strBasicField 	= strBasicField;
		
		find();
	}
	
	/**
	 * find query
	 * 
	 * @param strBasicField
	 * @param strBasicFind
	 * @param strBasicSort
	 * @param cntSkip
	 * @param cntLimit
	 */
	public void find(String strBasicField, String strBasicFind, String strBasicSort, int cntSkip, int cntLimit) {

		this.strBasicField 	= strBasicField;
		this.strBasicFind 	= strBasicFind;
		this.strBasicSort 	= strBasicSort;
		this.cntSkip 		= cntSkip;
		this.cntLimit 		= cntLimit;
		
		find();
	}	
	
	/**
	 * 
	 */
	private void find() {
		
		final Display display = getDisplay();		
		// job
		Job job = new Job("SQL execute job") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Starting JSON query...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
						
				try {			
					// field					
					BasicDBObject basicFields = (BasicDBObject)JSON.parse(strBasicField);
					if(null == basicFields) basicFields = new BasicDBObject();
					
					// search
					DBObject basicWhere = (DBObject)JSON.parse(strBasicFind);
					if(null == basicWhere) basicWhere = new BasicDBObject();
					
					// sort
					BasicDBObject basicSort = (BasicDBObject)JSON.parse(strBasicSort);
					if(null == basicSort) basicSort = new BasicDBObject();
					
					if(logger.isDebugEnabled()) {
						logger.debug("############[text condition]#####################"); //$NON-NLS-1$
						logger.debug("[Fields]" + strBasicField); //$NON-NLS-1$
						logger.debug("[Find]" + strBasicFind); //$NON-NLS-1$
						logger.debug("[Sort]" + strBasicSort); //$NON-NLS-1$
						logger.debug("############[text condition]#####################"); //$NON-NLS-1$
					}
					monitor.setTaskName(basicWhere.toString());
		
					// console 초기화
					sbConsoleErrorMsg.setLength(0);
					sbConsoleExecuteMsg.setLength(0);
					
					// 검색
					find(basicFields, basicWhere, basicSort, cntSkip, cntLimit);
				} catch (Exception e) {
					logger.error("find basic collection exception", e); //$NON-NLS-1$
					return new Status(Status.ERROR, Activator.PLUGIN_ID, "findBasic " + e.getMessage()); //$NON-NLS-1$
				} finally {
					monitor.done();
				}
				
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {

				final IJobChangeEvent jobEvent = event; 
				
				display.asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							setResult();
						} else {
							sbConsoleErrorMsg.append(jobEvent.getResult().getMessage());
							appendMessage(jobEvent.getResult().getException(), jobEvent.getResult().getMessage());
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
	 * 실제 몽고디비와 접속해서 검색을 수행합니다.
	 * 
	 * @param basicFields
	 * @param basicWhere
	 * @param basicSort
	 */
	private void find(BasicDBObject basicFields, DBObject basicWhere, BasicDBObject basicSort, int cntSkip, int cntLimit) throws Exception {
		if( (cntLimit - cntSkip) >= defaultMaxCount) {
//			"검색 수가 " + defaultMaxCount + "를 넘을수 없습니다. Prefernece에서 값을 조절하십시오."
//			Search can not exceed the number 5. Set in Perference.
			throw new Exception(String.format(Messages.get().MongoDBTableEditor_0, ""+defaultMaxCount));  //$NON-NLS-2$ //$NON-NLS-1$
		}
		
		DB mongoDB = MongoDBQuery.findDB(userDB);		
		DBCollection dbCollection = MongoDBQuery.findCollection(userDB, collectionName);
		
		// 데이터 검색
		DBCursor dbCursor = null;
		try {
			if(cntSkip > 0 && cntLimit > 0) {
					
				dbCursor = dbCollection.
									find(basicWhere, basicFields).
									sort(basicSort).
									skip(cntSkip).
									limit(cntLimit)
									;
				
			} else if(cntSkip == 0 && cntLimit > 0) {
				
				dbCursor = dbCollection.
						find(basicWhere, basicFields).
						sort(basicSort).
						limit(cntLimit);
			} else {
				dbCursor = dbCollection.
									find(basicWhere, basicFields).
									sort(basicSort);				
			}
			
			DBObject explainDBObject = dbCursor.explain();
			sbConsoleExecuteMsg.append(JSONUtil.getPretty(explainDBObject.toString())).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			sbConsoleErrorMsg.append(JSONUtil.getPretty(mongoDB.getWriteConcern()==null?"":mongoDB.getWriteConcern().toString())).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			
			if(logger.isDebugEnabled()) logger.debug(mongoDB.getWriteConcern().toString());
			
			// 결과 데이터를 출력합니다.
			refreshDBView(dbCursor, dbCursor.count());
			
		} finally {
			if(dbCursor != null) dbCursor.close();
		}
	}
	
	/**
	 * 결과셋을 출력합니다.
	 * 
	 * @param iteResult
	 * @param intTotalCnt
	 * @throws Exception
	 */
	public void refreshDBView(DBObject dbObject, int intTotalCnt) throws Exception {
		List<DBObject> listIte = new ArrayList<DBObject>();
		listIte.add(dbObject);
		
		refreshDBView(listIte, intTotalCnt);
	}
	
	/**
	 * 결과셋을 출력합니다.
	 * 
	 * @param iteResult
	 * @param intTotalCnt
	 * @throws Exception
	 */
	public void refreshDBView(Iterable<DBObject> iteResult, int intTotalCnt) throws Exception {
		int totCnt = 0;
		
		mapColumns = new HashMap<Integer, String>();
		sourceDataList = new ArrayList<Map<Integer, Object>>();
		listTrees = new ArrayList<MongodbTreeViewDTO>();

		// 헤더를 분석하여 만듭니다.
		for(DBObject dbObject : iteResult) {
			MongoDBTableColumn.getTabelColumnView(dbObject, mapColumns);
		}
		
		for(DBObject dbObject : iteResult) {
			// append tree data columnInfo.get(key)
			MongodbTreeViewDTO treeDto = new MongodbTreeViewDTO(dbObject, "(" + totCnt + ") {..}", "", "Document");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			parserTreeObject(dbObject, treeDto, dbObject);
			listTrees.add(treeDto);
							
			// append table data
			HashMap<Integer, Object> dataMap = new HashMap<Integer, Object>();				
			for(int i=0; i<mapColumns.size(); i++)	{
				
				Object keyVal = dbObject.get(mapColumns.get(i));
				if(keyVal == null) dataMap.put(i, "");  //$NON-NLS-1$
				else dataMap.put(i, keyVal.toString());
			}
			// 데이터 삭제 및 수정에서 사용하기 위한 id
			dataMap.put(MongoDBDefine.PRIMARY_ID_KEY, dbObject);
			sourceDataList.add(dataMap);
			
			// append row text
			totCnt++;
		}
		txtCnt = intTotalCnt + "/" + totCnt; //$NON-NLS-1$
	}
	
	/**
	 * parser tree obejct
	 * 
	 * @param dbObject
	 */
	private void parserTreeObject(final DBObject rootDbObject, final MongodbTreeViewDTO treeDto, final DBObject dbObject) throws Exception {
		List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
		
		Map<Integer, String> tmpMapColumns = MongoDBTableColumn.getTabelColumnView(dbObject);
		for(int i=0; i<tmpMapColumns.size(); i++)	{
			MongodbTreeViewDTO tmpTreeDto = new MongodbTreeViewDTO();
			tmpTreeDto.setDbObject(rootDbObject);
			
			String keyName = tmpMapColumns.get(i);			
			Object keyVal = dbObject.get(keyName);
			
			tmpTreeDto.setRealKey(keyName);
			// is sub document
			if( keyVal instanceof BasicDBObject ) {
				tmpTreeDto.setKey(tmpMapColumns.get(i) + " {..}"); //$NON-NLS-1$
				tmpTreeDto.setType("Document"); //$NON-NLS-1$
				
				parserTreeObject(rootDbObject, tmpTreeDto, (DBObject)keyVal);
			} else if(keyVal instanceof BasicDBList) {
				BasicDBList dbObjectList = (BasicDBList)keyVal;
				
				tmpTreeDto.setKey(tmpMapColumns.get(i) + " [" + dbObjectList.size() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				tmpTreeDto.setType("Array"); //$NON-NLS-1$
				parseObjectArray(rootDbObject, tmpTreeDto, dbObjectList);
			} else {
				tmpTreeDto.setKey(tmpMapColumns.get(i));
				tmpTreeDto.setType(keyVal != null?keyVal.getClass().getName():"Unknow"); //$NON-NLS-1$
				
				if(keyVal == null) tmpTreeDto.setValue(""); //$NON-NLS-1$
				else tmpTreeDto.setValue(keyVal.toString());
			}
			
			// 컬럼의 데이터를 넣는다.
			listTrees.add(tmpTreeDto);
		}
		
		treeDto.setChildren(listTrees);
	}
	
	/**
	 * object array
	 * 
	 * @param treeDto
	 * @param dbObject
	 * @throws Exception
	 */
	private void parseObjectArray(final DBObject rootDbObject, final MongodbTreeViewDTO treeDto, final BasicDBList dbObjectList) throws Exception {
		List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
		
		for(int i=0; i<dbObjectList.size(); i++) {
			MongodbTreeViewDTO mongodbDto = new MongodbTreeViewDTO();
			
			mongodbDto.setRealKey("" + i ); //$NON-NLS-1$
			mongodbDto.setKey("(" + i + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			mongodbDto.setDbObject(rootDbObject);

			Object keyVal = dbObjectList.get(i);
			if( keyVal instanceof BasicDBObject ) {
				mongodbDto.setType("Document"); //$NON-NLS-1$
				
				parserTreeObject(rootDbObject, mongodbDto, (DBObject)keyVal);
			} else if(keyVal instanceof BasicDBList) {
				BasicDBList tmpDbObjectList = (BasicDBList)keyVal;
				
				mongodbDto.setType("Array"); //$NON-NLS-1$
				parseObjectArray(rootDbObject, mongodbDto, tmpDbObjectList);
			} else {
				mongodbDto.setType(keyVal != null?keyVal.getClass().getName():"Unknow"); //$NON-NLS-1$
				
				if(keyVal == null) mongodbDto.setValue(""); //$NON-NLS-1$
				else mongodbDto.setValue(keyVal.toString());
			}
			
			listTrees.add(mongodbDto);
		}
		
		treeDto.setChildren(listTrees);
	}
	
	/**
	 * 테이블의 결과를 출력합니다.
	 *
	 */
	public void setResult() {
		
		// 화면을 초기화 합니다.
		List<MongodbTreeViewDTO> tmpTree = new ArrayList<MongodbTreeViewDTO>();
		treeViewerMongo.setInput(tmpTree);
		treeViewerMongo.refresh();
		
		List<HashMap<Integer, Object>> tmpTable = new ArrayList<HashMap<Integer,Object>>();
		resultTableViewer.setLabelProvider( new SQLResultLabelProvider(GetPreferenceGeneral.getISRDBNumberIsComma(), GetPreferenceGeneral.getResultNull()) );
		resultTableViewer.setContentProvider(new SQLResultContentProvider(sourceDataList) );
		resultTableViewer.setInput(tmpTable);
		resultTableViewer.refresh();
		
		lblTreeViewCount.setText(""); //$NON-NLS-1$
		lblTableViewCount.setText(""); //$NON-NLS-1$
		
		setJsonView("");
		
		// 
		if(tabFolderMongoDB.getSelectionIndex() == TAB_POSITION_MESSAGE_VIEW) {
			if(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE.equals( defaultResultPage )) {
				tabFolderMongoDB.setSelection(TAB_POSITION_TREE_VIEW);
			} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals( defaultResultPage )) {
				tabFolderMongoDB.setSelection(TAB_POSITION_TABLE_VIEW);
			} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals( defaultResultPage )) {
				tabFolderMongoDB.setSelection(TAB_POSITION_JSON_VIEW);
			} else {
				tabFolderMongoDB.setSelection(TAB_POSITION_MESSAGE_VIEW);
			}
		}
		selectData();		
	}
	
	/**
	 * select data
	 */
	private void selectData() {
		int selectionIndex = tabFolderMongoDB.getSelectionIndex();
		
		// tree view
		if(selectionIndex == TAB_POSITION_TREE_VIEW) {
			
			treeViewerMongo.setInput(listTrees);
			treeViewerMongo.expandToLevel(1);
			
			lblTreeViewCount.setText(txtCnt);
			lblTreeViewCount.getParent().layout();
			
		// table view
		} else if(selectionIndex == TAB_POSITION_TABLE_VIEW) {
			
			lblTableViewCount.setText(txtCnt);
			lblTableViewCount.getParent().layout();
			
			sqlSorter = new SQLResultSorter(-999);
			
			createTableColumn(mapColumns, sqlSorter);
			resultTableViewer.setLabelProvider( new SQLResultLabelProvider() );
			resultTableViewer.setContentProvider(new SQLResultContentProvider(sourceDataList) );
			resultTableViewer.setInput(sourceDataList);
			resultTableViewer.setSorter(sqlSorter);		
			sqlFilter.setTable(resultTableViewer.getTable());
		} else if(selectionIndex == TAB_POSITION_JSON_VIEW) {
			StringBuffer sbJsonStr = new StringBuffer();
			
			sbJsonStr.append("{");
			for(int i=0; i<sourceDataList.size(); i++) {
				sbJsonStr.append(i + ": ");
				String strJson = sourceDataList.get(i).get(MongoDBDefine.PRIMARY_ID_KEY).toString();
				if(i < sourceDataList.size()-1) sbJsonStr.append(strJson + ", ");
				else sbJsonStr.append(strJson);
			}
			sbJsonStr.append("}");

			setJsonView(sbJsonStr.toString());
		}
		
		TreeUtil.packTree(treeViewerMongo.getTree());
		TableUtil.packTable(resultTableViewer.getTable());
	}
	
	/**
	 * page를 초기화 합니다.
	 */
	private void firstTabInit() {
			
		if(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE.equals( defaultResultPage )) {
			tabFolderMongoDB.setSelection(TAB_POSITION_TREE_VIEW);
		} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals( defaultResultPage )) {
			tabFolderMongoDB.setSelection(TAB_POSITION_TABLE_VIEW);
		} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals( defaultResultPage )) {
			tabFolderMongoDB.setSelection(TAB_POSITION_JSON_VIEW);
		} else {
			tabFolderMongoDB.setSelection(TAB_POSITION_MESSAGE_VIEW);
		}

	}
	
	/**
	 * document insert
	 */
	private void newDocument() {
		NewDocumentDialog dialog = new NewDocumentDialog(Display.getCurrent().getActiveShell(), userDB, collectionName);
		if(Dialog.OK == dialog.open()) {
			find();
		}
	}
	
	/**
	 * 인덱스를 생성합니다. 
	 */
	private void createIndex() {
		NewIndexDialog dialog = new NewIndexDialog(Display.getCurrent().getActiveShell(), userDB, collectionName);
		dialog.open();
	}
	
	/**
	 * document del - tree
	 */
	private void deleteDocumentTree() {
		IStructuredSelection iss = (IStructuredSelection)treeViewerMongo.getSelection();
		if(iss.isEmpty()) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().MongodbResultComposite_10);
			return;
		} else if(MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().MongodbResultComposite_12)) {
			
			MongodbTreeViewDTO dto = (MongodbTreeViewDTO)iss.getFirstElement();
			if(logger.isDebugEnabled()) logger.info("[delete object id is]" + dto.getDbObject().toString()); //$NON-NLS-1$
			
			try {
				MongoDBQuery.deleteDocument(userDB, collectionName, dto.getDbObject());
				find();
			} catch (Exception e) {
				logger.error(collectionName + " collection document remove object id is" + dto.getDbObject(), e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Document remove", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * document del - table
	 */
	private void deleteDocumentTable() {
		IStructuredSelection iss = (IStructuredSelection)resultTableViewer.getSelection();
		if(iss.isEmpty()) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().MongodbResultComposite_10);
			return;
		} else if(MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().MongodbResultComposite_12)) {
			HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)iss.getFirstElement();
			Object dbObject = rsResult.get(MongoDBDefine.PRIMARY_ID_KEY);
			
			try {
				MongoDBQuery.deleteDocument(userDB, collectionName, (DBObject)dbObject);
				find();
			} catch (Exception e) {
				logger.error(collectionName + " collection document remove object id is" + dbObject, e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Document remove", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * treeview create
	 */
	private void createTreeColumn() {
		String[] columnName = {"Key", "Value", "Type"};  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int[] columnSize = {170, 300, 170};
		
		try {
			// reset column 
			for(int i=0; i<columnName.length; i++) {
				final TreeViewerColumn treeColumn = new TreeViewerColumn(treeViewerMongo, SWT.LEFT);
				treeColumn.getColumn().setText( columnName[i] );
				treeColumn.getColumn().setWidth( columnSize[i] );
				treeColumn.getColumn().setResizable(true);
				treeColumn.getColumn().setMoveable(false);
				if(isUserAction) {
					if(i == 1) treeColumn.setEditingSupport(new TreeViewerEditingSupport(userDB, collectionName, treeViewerMongo));
				}
			}	// end for
			
		} catch(Exception e) { 
			logger.error("MongoDB Tree view Editor", e); //$NON-NLS-1$
		}		
	}
	
	/**
	 * 필터를 설정합니다.
	 */
	private void setFilter() {
		sqlFilter.setFilter(textFilter.getText());
		resultTableViewer.addFilter( sqlFilter );
	}
	
	/**
	 * table column adapter
	 * 
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
	 * collection column
	 * 
	 * @param mapColumns2
	 */
	private void createTableColumn(Map<Integer, String> mapColumns, final SQLResultSorter tableSorter) {
		// 기존 column을 삭제한다.
		Table table = resultTableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		try {
			// reset column 
			for (Integer key : mapColumns.keySet()) {
				
				final int index = key;
				final TableViewerColumn tableColumn = new TableViewerColumn(resultTableViewer, SWT.LEFT);
				tableColumn.getColumn().setText( mapColumns.get(key) );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
				
				tableColumn.getColumn().addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						tableSorter.setColumn(index);
						int dir = resultTableViewer.getTable().getSortDirection();
						if (resultTableViewer.getTable().getSortColumn() == tableColumn.getColumn()) {
							dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
						} else {
							dir = SWT.DOWN;
						}
						
						resultTableViewer.getTable().setSortDirection(dir);
						resultTableViewer.getTable().setSortColumn(tableColumn.getColumn());
						resultTableViewer.refresh();
					}
				});
			}	// end for
			
		} catch(Exception e) { 
			logger.error("MongoDB Table Editor", e); //$NON-NLS-1$
		}		
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/**
	 * append tadpole message 
	 * 
	 * @param throwable
	 * @param msg
	 */
	public void appendMessage(Throwable throwable, String msg) {
		tabFolderMongoDB.setSelection(3);
		listMessage.add(new TadpoleMessageDAO(new Date(), msg, throwable));
		tableViewerMessage.refresh();
	}

	@Override
	protected void checkSubclass() {
	}
	
	public void structureView() {
		final Display display = getDisplay();		
		// job
		Job job = new Job("Structure collection analyized job") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Collection structure...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
			
				List<DBObject> mapOnlyOnDBObject = new ArrayList<>();
				
				try {
					MongoDBQueryUtil qu = new MongoDBQueryUtil(userDB, collectionName);
					while(qu.nextQuery()) {
						
						// row 단위
						List<DBObject> listDBObject = qu.getCollectionDataList();
						for (DBObject dbObject : listDBObject) {
							
							if(mapOnlyOnDBObject.isEmpty()) mapOnlyOnDBObject.add(dbObject);
							else {
								// 검사한 모든 행이 없는지 검사 결과를 넣습니다. 
								Map<Integer, Boolean> mapSearchResult = new HashMap<>();
								
								// 유일한 오브젝트에 모두 들어 있는지 비교한다.
								for(int i=0; i<mapOnlyOnDBObject.size(); i++) {
									DBObject alradyDbObject = mapOnlyOnDBObject.get(i);
	//								if(logger.isDebugEnabled()) logger.debug("\t=====> find object is " + alradyDbObject.toString());
									
									// 검사하려는 오브젝트에 비교한다.
									for (String strKey : dbObject.keySet()) {
	//									if(logger.isDebugEnabled()) logger.debug("\t\t search object is " + strKey);
										
										if(!alradyDbObject.containsField(strKey)) {
											mapSearchResult.put(i, true);
											break;
										}
									}
									
								} // end already for
								
								int intFoundObject = 0;
								for (int i=0; i<mapOnlyOnDBObject.size(); i++) {
									if(mapSearchResult.containsKey(i)) {
										intFoundObject++;
									}
								}
								
								if(intFoundObject == mapOnlyOnDBObject.size()) {
									mapOnlyOnDBObject.add(dbObject);
								}
							}	// end if
							
						}
						
						refreshDBView(mapOnlyOnDBObject, mapOnlyOnDBObject.size());
					}
				
				} catch (Exception e) {
					logger.error("struct collection exception", e); //$NON-NLS-1$
					return new Status(Status.ERROR, Activator.PLUGIN_ID, "collection structure " + e.getMessage()); //$NON-NLS-1$
				} finally {
					monitor.done();
				}
				
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
	
				final IJobChangeEvent jobEvent = event; 
				
				display.asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							setResult();
						} else {
							sbConsoleErrorMsg.append(jobEvent.getResult().getMessage());
							appendMessage(jobEvent.getResult().getException(), jobEvent.getResult().getMessage());
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
	 * setting json view 
	 * 
	 * @param strJSON
	 */
	private void setJsonView(String strJSON) {
		try {
			tadpoleEditor.setText(JSONUtil.getPretty(strJSON));
		} catch(Exception e) {
			logger.error("json view", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * error console
	 */
	public void consoleError() {		
		DBObject dbObject = (DBObject)JSON.parse(sbConsoleErrorMsg.toString() );
		FindOneDetailDialog dlg = new FindOneDetailDialog(null, userDB, collectionName + " " + Messages.get().MongodbResultComposite_25, dbObject);
		dlg.open();
	}

	/**
	 * execute plan console
	 */
	public void consoleExecutePlan() {
		DBObject dbObject = (DBObject)JSON.parse(sbConsoleExecuteMsg.toString());
		FindOneDetailDialog dlg = new FindOneDetailDialog(null, userDB, collectionName + " " + Messages.get().MongodbResultComposite_26, dbObject);
		dlg.open();
	}
}
