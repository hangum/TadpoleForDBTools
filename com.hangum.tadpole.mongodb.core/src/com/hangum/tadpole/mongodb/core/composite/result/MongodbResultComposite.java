package com.hangum.tadpole.mongodb.core.composite.result;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.Define;
import com.hangum.db.dialogs.message.TadpoleMessageDialog;
import com.hangum.db.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.db.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.util.JSONUtil;
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
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.define.MongoDBDefine;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewDocumentDialog;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewIndexDialog;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBTableEditor;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.mongodb.DBObject;

/**
 * 몽고 디비 결과셋을 출력하는 콤포짖
 * 
 * @author hangum
 *
 */
public class MongodbResultComposite extends Composite {
	private static Logger logger = Logger.getLogger(MongodbResultComposite.class);
	
	/** composiate이 처음 호출 되었는지 */
	private boolean isFirstCall = false;
	
	/** data userdb*/
	private UserDBDAO userDB;
	/** collection name */
	private String collectionName;
	/** call editor */
	private MongoDBTableEditor editor;
	
	/** result tab folder */
	private TabFolder tabFolderMongoDB;
	
	/** tree viewer */
	private TreeViewer treeViewerMongo;
	private Label lblTreeViewCount;
	
	private Text textFilter;
	/** collection 결과 */
	private TableViewer resultTableViewer;
	private Label lblSearchCnt;
	private SashForm sashFormCollectionResult;
	
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	private Table tableTadpoleMsg;
	
	private TableViewer tableViewerMessage;
	private List<TadpoleMessageDAO> listMessage = new ArrayList<TadpoleMessageDAO>();
	
	/** browser */
	Composite compositeExternal;
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	/** query 의 결과 데이터  -- table의 데이터를 표시하는 용도 <column index, Data> */
	private List<HashMap<Integer, Object>> sourceDataList = new ArrayList<HashMap<Integer, Object>>();
	private Text textTextView;
	private Label lblTextViewCnt;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MongodbResultComposite(Composite parent, int style, final UserDBDAO userDB, final String collectionName, final MongoDBTableEditor editor) {
		super(parent, style);
		this.userDB = userDB;
		this.collectionName = collectionName;
		this.editor = editor;
		
		tabFolderMongoDB = new TabFolder(parent, SWT.NONE);
		tabFolderMongoDB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmTreeView = new TabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTreeView.setText("Tree View"); //$NON-NLS-1$
		
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
		
		Composite compositeTreeViewTail = new Composite(compositeTreeView, SWT.NONE);
		compositeTreeViewTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTreeViewTail.setLayout(new GridLayout(6, false));
		
		Button btnTreeInsertDocument = new Button(compositeTreeViewTail, SWT.NONE);
		btnTreeInsertDocument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newDocument();
			}
		});
		btnTreeInsertDocument.setText("Insert Document"); //$NON-NLS-1$
		
//		Button btnTreeModifyDocument = new Button(compositeTreeViewTail, SWT.NONE);
//		btnTreeModifyDocument.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		btnTreeModifyDocument.setText("Modify Document");
		
		Button btnTreeDeleteDocument = new Button(compositeTreeViewTail, SWT.NONE);
		btnTreeDeleteDocument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDocumentTree();
			}
		});
		btnTreeDeleteDocument.setText("Delete Document"); //$NON-NLS-1$
		
		Button btnTreeCreateIndex = new Button(compositeTreeViewTail, SWT.NONE);
		btnTreeCreateIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createIndex();
			}
		});
		btnTreeCreateIndex.setText("Add Index");
		
		Label labelTreeViewDumy = new Label(compositeTreeViewTail, SWT.NONE);
		labelTreeViewDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblTreeViewCount = new Label(compositeTreeViewTail, SWT.NONE);
		new Label(compositeTreeViewTail, SWT.NONE);
		
		createTreeColumn();
		
		treeViewerMongo.setContentProvider(new TreeMongoContentProvider() );
		treeViewerMongo.setLabelProvider(new TreeMongoLabelProvider());
		
		TabItem tbtmTableView = new TabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTableView.setText("Table View"); //$NON-NLS-1$
		
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
		lblFilter.setText("Filter"); //$NON-NLS-1$
		
		textFilter = new Text(compositeBodyTable, SWT.BORDER);
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
					String jsonString = rsResult.get(MongoDBDefine.PRIMARY_ID_KEY).toString();
					TadpoleSimpleMessageDialog dlg = new TadpoleSimpleMessageDialog(null, collectionName, JSONUtil.getPretty(jsonString));					 //$NON-NLS-1$ //$NON-NLS-2$
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
		
		Button btnInsertDocument = new Button(compositeTail, SWT.NONE);
		btnInsertDocument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newDocument();
			}
		});
		btnInsertDocument.setText("Insert Document"); //$NON-NLS-1$
		
		Button btnDeleteDocument = new Button(compositeTail, SWT.NONE);
		btnDeleteDocument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDocumentTable();
			}
		});
		btnDeleteDocument.setText("Delete Document"); //$NON-NLS-1$
		
		Button btnCreateIndex = new Button(compositeTail, SWT.NONE);
		btnCreateIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 createIndex();
			}
		});
		btnCreateIndex.setText("Add Index");
		
		Label labelTableDumy = new Label(compositeTail, SWT.NONE);
		GridData gd_labelTableDumy = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelTableDumy.widthHint = 5;
		labelTableDumy.setLayoutData(gd_labelTableDumy);
		
		Button btnExportCSV = new Button(compositeTail, SWT.NONE);
		btnExportCSV.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				// column 헤더추
				TableColumn[] tcs = resultTableViewer.getTable().getColumns();
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
				DownloadUtils.provideDownload(compositeExternal, downloadServiceHandler.getId());
			}
		});
		btnExportCSV.setText("Export CSV"); //$NON-NLS-1$
		
		compositeExternal = new Composite(compositeTail, SWT.NONE);
		compositeExternal.setLayout(new GridLayout(1, false));
		compositeExternal.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(compositeTail, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblSearchCnt = new Label(compositeTail, SWT.NONE);
		new Label(compositeTail, SWT.NONE);
		new Label(compositeTail, SWT.NONE);
		
		TabItem tbtmTextView = new TabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTextView.setText("Text View"); //$NON-NLS-1$
		
		Composite compositeTextView = new Composite(tabFolderMongoDB, SWT.NONE);
		tbtmTextView.setControl(compositeTextView);
		GridLayout gl_compositeTextView = new GridLayout(1, false);
		gl_compositeTextView.verticalSpacing = 2;
		gl_compositeTextView.horizontalSpacing = 2;
		gl_compositeTextView.marginHeight = 2;
		gl_compositeTextView.marginWidth = 2;
		compositeTextView.setLayout(gl_compositeTextView);
		
		textTextView = new Text(compositeTextView, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI | SWT.VIRTUAL);
		textTextView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeTextJson = new Composite(compositeTextView, SWT.NONE);
		compositeTextJson.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTextJson.setLayout(new GridLayout(6, false));
		
		Button btnInsertTextDocument = new Button(compositeTextJson, SWT.NONE);
		btnInsertTextDocument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newDocument();
			}
		});
		btnInsertTextDocument.setText("Insert Document");
		
		Button btnTextCreateIndex = new Button(compositeTextJson, SWT.NONE);
		btnTextCreateIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createIndex();
			}
		});
		btnTextCreateIndex.setText("Add Index");
		
		Label labelTextDumy = new Label(compositeTextJson, SWT.NONE);
		GridData gd_labelTextDumy = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelTextDumy.widthHint = 5;
		labelTextDumy.setLayoutData(gd_labelTextDumy);
		
		Button btnExportJson = new Button(compositeTextJson, SWT.NONE);
		btnExportJson.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				downloadServiceHandler.setName(userDB.getDisplay_name() + "_JSONExport.json"); //$NON-NLS-1$
				downloadServiceHandler.setContent(textTextView.getText());
				DownloadUtils.provideDownload(compositeExternal, downloadServiceHandler.getId());
			}
		});
		btnExportJson.setText("Export JSON"); //$NON-NLS-1$
		
		Label labelTextViewDumy = new Label(compositeTextJson, SWT.NONE);
		labelTextViewDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblTextViewCnt = new Label(compositeTextJson, SWT.NONE);
		
		// tabpole message
		TabItem tbtmTadpoleMessage = new TabItem(tabFolderMongoDB, SWT.NONE);
		tbtmTadpoleMessage.setText("Tadpole Message"); //$NON-NLS-1$
		
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
		tblclmnDate.setText("Date"); //$NON-NLS-1$
		tblclmnDate.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnDate, 0));
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerMessage, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_1.getColumn();
		tblclmnSql.setWidth(500);
		tblclmnSql.setText("Message"); //$NON-NLS-1$
		tblclmnSql.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnSql, 1));
		
		tableViewerMessage.setLabelProvider(new SQLHistoryLabelProvider());
		tableViewerMessage.setContentProvider(new ArrayContentProvider());
		tableViewerMessage.setInput(listMessage);
		tableViewerMessage.setComparator(sorterMessage);
		
		Composite composite = new Composite(compositeTadpoleMsg, SWT.NONE);		
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Button btnExportTadpoleMessage = new Button(composite, SWT.NONE);
		btnExportTadpoleMessage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sbExportData = new StringBuffer();
				
				for(TadpoleMessageDAO dao : listMessage) {
					sbExportData.append( dao.getStrMessage() ).append(Define.LINE_SEPARATOR); //$NON-NLS-1$
				}
				
				downloadServiceHandler.setName(userDB.getDisplay_name() + "_Message.txt"); //$NON-NLS-1$
				downloadServiceHandler.setContent(sbExportData.toString());
				DownloadUtils.provideDownload(compositeExternal, downloadServiceHandler.getId());
			}
		});
		btnExportTadpoleMessage.setText("Export Tadpole Message"); //$NON-NLS-1$
		
		registerServiceHandler();
		
		firstTabInit();
	}
	
	/**
	 * page를 초기화 합니다.
	 */
	private void firstTabInit() {
			
		if(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE.equals( editor.getDefaultResultPage() )) {
			tabFolderMongoDB.setSelection(0);
		} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals( editor.getDefaultResultPage() )) {
			tabFolderMongoDB.setSelection(1);
		} else {
			tabFolderMongoDB.setSelection(2);
		}

	}
	
	/**
	 * page를 초기화 합니다.
	 */
	private void tabInit() {

		if(tabFolderMongoDB.getSelectionIndex() == 3) {
			
			if(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE.equals( editor.getDefaultResultPage() )) {
				tabFolderMongoDB.setSelection(0);
			} else if(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE.equals( editor.getDefaultResultPage() )) {
				tabFolderMongoDB.setSelection(1);
			} else {
				tabFolderMongoDB.setSelection(2);
			}
		}
	}
	
	/**
	 * document insert
	 */
	private void newDocument() {
		NewDocumentDialog dialog = new NewDocumentDialog(Display.getCurrent().getActiveShell(), userDB, collectionName);
		if(Dialog.OK == dialog.open()) {
			editor.findExtension();
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
			MessageDialog.openError(null, Messages.MongodbResultComposite_9, Messages.MongodbResultComposite_10);
			return;
		} else if(MessageDialog.openConfirm(null, Messages.MongodbResultComposite_9, Messages.MongodbResultComposite_12)) {
			
			MongodbTreeViewDTO dto = (MongodbTreeViewDTO)iss.getFirstElement();
			if(logger.isDebugEnabled()) logger.info("[delete object id is]" + dto.getDbObject().toString()); //$NON-NLS-1$
			
			try {
				MongoDBQuery.deleteDocument(userDB, collectionName, dto.getDbObject());
				editor.findExtension();
			} catch (Exception e) {
				logger.error(collectionName + " collection document remove object id is" + dto.getDbObject(), e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "Document remove", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * document del - table
	 */
	private void deleteDocumentTable() {
		IStructuredSelection iss = (IStructuredSelection)resultTableViewer.getSelection();
		if(iss.isEmpty()) {
			MessageDialog.openError(null, Messages.MongodbResultComposite_9, Messages.MongodbResultComposite_10);
			return;
		} else if(MessageDialog.openConfirm(null, Messages.MongodbResultComposite_9, Messages.MongodbResultComposite_12)) {
			HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)iss.getFirstElement();
			Object dbObject = rsResult.get(MongoDBDefine.PRIMARY_ID_KEY);
			
			try {
				MongoDBQuery.deleteDocument(userDB, collectionName, (DBObject)dbObject);
				editor.findExtension();
			} catch (Exception e) {
				logger.error(collectionName + " collection document remove object id is" + dbObject, e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "Document remove", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * 테이블의 결과를 출력합니다.
	 *
	 * @param textCnt count
	 * @param mapColumns table column
	 * @param listTrees tree data
	 * @param sourceDataList table data
	 */
	public void setResult(String textCnt, 
							Map<Integer, String> mapColumns, 
							List<MongodbTreeViewDTO> listTrees,
							List<HashMap<Integer, Object>> sourceDataList
	) {
		this.sourceDataList = sourceDataList; 
		
		// tree view
		treeViewerMongo.setInput(listTrees);
		treeViewerMongo.expandToLevel(2);
		lblTreeViewCount.setText(textCnt);
		
		// table view
		lblSearchCnt.setText(textCnt);
		sqlSorter = new SQLResultSorter(-999);
		
		createTableColumn(mapColumns, sqlSorter);
		resultTableViewer.setLabelProvider( new SQLResultLabelProvider() );
		resultTableViewer.setContentProvider(new SQLResultContentProvider(sourceDataList) );
		resultTableViewer.setInput(sourceDataList);
		resultTableViewer.setSorter(sqlSorter);		
		sqlFilter.setTable(resultTableViewer.getTable());
		
		TableUtil.packTable(resultTableViewer.getTable());
		
		tabInit();
		
		// text view
		StringBuffer sbJsonStr = new StringBuffer();		
		for(int i=0; i<sourceDataList.size(); i++) {
			sbJsonStr.append("/* " + i + " */" + Define.LINE_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
			sbJsonStr.append(sourceDataList.get(i).get(MongoDBDefine.PRIMARY_ID_KEY) + Define.LINE_SEPARATOR);
		}
		textTextView.setText(JSONUtil.getPretty(sbJsonStr.toString()));
		lblTextViewCnt.setText(textCnt);		
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
				final TreeViewerColumn tableColumn = new TreeViewerColumn(treeViewerMongo, SWT.LEFT);
				tableColumn.getColumn().setText( columnName[i] );
				tableColumn.getColumn().setWidth( columnSize[i] );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
				
				if(i == 1) tableColumn.setEditingSupport(new TreeViewerEditingSupport(userDB, collectionName, treeViewerMongo));
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
	 * console에 text 추가합니다.
	 * 
	 * @param msg
	 * @deprecated
	 */
	public void appendConsole() {
		tabInit();
	}
	
	/**
	 * append tadpole message 
	 * 
	 * @param msg
	 */
	public void appendMessage(String msg) {
		tabFolderMongoDB.setSelection(3);
		listMessage.add(new TadpoleMessageDAO(new Date(), msg));
		tableViewerMessage.refresh();
	}

	@Override
	protected void checkSubclass() {
	}
}