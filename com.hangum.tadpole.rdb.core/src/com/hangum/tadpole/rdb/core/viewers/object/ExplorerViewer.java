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
package com.hangum.tadpole.rdb.core.viewers.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.viewsupport.SelectionProviderMediator;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.DBOtherDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.collections.TadpoleMongoDBCollectionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.index.TadpoleMongoDBIndexesComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.serversidescript.TadpoleMongoDBJavaScriptComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.function.TadpoleFunctionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.orapackage.TadpolePackageComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.TadpoleProcedureComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.sysnonym.TadpoleSynonymComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TadpoleTableComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.view.TadpoleViewerComposite;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * object explorer viewer
 * 
 * @author hangum
 * 
 */
public class ExplorerViewer extends ViewPart {
	private static Logger logger = Logger.getLogger(ExplorerViewer.class);
	public static String ID = "com.hangum.tadpole.rdb.core.view.object.explorer"; //$NON-NLS-1$
	
	/** tabfolder가 초기화 될때는 tab select 이벤트 먹지 않도록 조절하지 않도록 */
	private boolean boolInitObjectHead = true;

	/** multi structured viewer */
	private StructuredViewer[] arrayStructuredViewer = null;

	private UserDBDAO userDB;
	private CTabFolder tabFolderObject;
	private Text textSearch;
	
	// rdb
	private Composite compositeBody;
	
	private TadpoleTableComposite 		tableComposite 		= null;
	private TadpoleViewerComposite 		viewComposite 		= null;
	
	// oracle
	private TadpoleSynonymComposite 	synonymComposite 	= null;
	
	private TadpoleProcedureComposite	procedureComposite 	= null;
	private TadpolePackageComposite	    packageComposite 	= null;
	private TadpoleFunctionComposite 	functionCompostite 	= null;
	
	// mongodb
	private TadpoleMongoDBCollectionComposite mongoCollectionComposite 	= null;
	private TadpoleMongoDBIndexesComposite mongoIndexComposite 			= null;
	private TadpoleMongoDBJavaScriptComposite mongoJavaScriptComposite 	= null;

	public ExplorerViewer() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.get().ObjectExplorer);
		
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 1;
		gl_parent.verticalSpacing = 0;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		Composite compositeSearch = new Composite(parent, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(1, false);
		gl_compositeSearch.horizontalSpacing = 2;
		gl_compositeSearch.verticalSpacing = 2;
		gl_compositeSearch.marginHeight = 2;
		gl_compositeSearch.marginWidth = 2;
		compositeSearch.setLayout(gl_compositeSearch);

		// filter를 설정합니다.
		textSearch = new Text(compositeSearch, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				filterText();
			}
		});
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);

		tabFolderObject = new CTabFolder(compositeBody, SWT.NONE);
		tabFolderObject.setBorderVisible(false);
		tabFolderObject.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		tabFolderObject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				if (userDB == null) return;
				if(boolInitObjectHead) {
					CTabItem ct = (CTabItem)evt.item;
					refershSelectObject(""+ct.getData(AbstractObjectComposite.TAB_DATA_KEY));
				}
			}
		});
		tabFolderObject.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// dumy table
		createTable();

		// Connection View에서 선택이벤트 받기.
		getSite().getPage().addSelectionListener(ManagerViewer.ID, managementViewerListener);

		// erd table에 선택되었을때..
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == PublicTadpoleDefine.SELECT_ERD_TABLE) {
					String tableName = event.getNewValue().toString();
					if (tabFolderObject.getSelectionIndex() != 0) tabFolderObject.setSelection(0);

					tableComposite.selectTable(tableName);
				} // end if(event.getProperty()
			} //
		}); // end property change
	}
	
	/**
	 * filter text
	 */
	private void filterText() {
		String strSelectTab = ""+tabFolderObject.getItem(tabFolderObject.getSelectionIndex()).getData(AbstractObjectComposite.TAB_DATA_KEY);
		String strSearchText = textSearch.getText();
		
		if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.COLLECTIONS.name())) {
			mongoCollectionComposite.filter(strSearchText);
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.TABLES.name())) {
			tableComposite.filter(strSearchText);
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.SYNONYM.name())) {
			synonymComposite.filter(strSearchText);

		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.VIEWS.name())) {
			viewComposite.filter(strSearchText);					
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.INDEXES.name())) {
			if(userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {
				mongoIndexComposite.filter(strSearchText);
			}					
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.PROCEDURES.name())) {
			procedureComposite.filter(strSearchText);
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.PACKAGES.name())) {
			packageComposite.filter(strSearchText);
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.FUNCTIONS.name())) {
			functionCompostite.filter(strSearchText);
		
		} else if (strSelectTab.equalsIgnoreCase(OBJECT_TYPE.JAVASCRIPT.name())) {
			mongoJavaScriptComposite.filter(strSearchText);
		}
	}

	/**
	 * management의 tree가 선택되었을때
	 */
	private ISelectionListener managementViewerListener = new ISelectionListener() {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection is = (IStructuredSelection) selection;
				boolInitObjectHead = false;
				initObjectHead(is.getFirstElement());
				boolInitObjectHead = true;
			} // end selection			
		} // end selectionchange
	};

	/**
	 * management의 head가 선택되었을때
	 * 
	 * @param selectElement
	 */
	public void initObjectHead(Object selectElement) {
		// 기존 사용자원을 반납합니다. 
		if(null != tableComposite) tableComposite.dispose(); 
		if(null != viewComposite) viewComposite.dispose(); 
		if(null != synonymComposite) synonymComposite.dispose();
		if(null != procedureComposite) procedureComposite.dispose(); 
		if(null != packageComposite) packageComposite.dispose(); 
		if(null != functionCompostite) functionCompostite.dispose(); 
		
		if(null != mongoCollectionComposite) { 
			mongoCollectionComposite.dispose();
			mongoIndexComposite.dispose();
			mongoJavaScriptComposite.dispose();
		}

		// Initialize resources
		if (selectElement instanceof UserDBDAO || selectElement instanceof UserDBResourceDAO) {
			selectUserDB(selectElement);
		} else if(selectElement instanceof ResourcesDAO) {
			ResourcesDAO dao = (ResourcesDAO)selectElement;
			selectUserDB(dao.getUserDBDAO());
		} else if(selectElement instanceof DBOtherDAO) {
			DBOtherDAO dao = (DBOtherDAO)selectElement;
			try {
				UserDBDAO schemaUserDB = (UserDBDAO)dao.getParent().getUserDBDAO().clone();
				schemaUserDB.setSchema(dao.getName());
			
				selectUserDB(schemaUserDB);
			} catch(Exception e) {
				logger.error("cloable not support exception", e);
			}
		} else {
			userDB = null;

			// 존재하는 tadfolder를 삭제한다.
			for (CTabItem tabItem : tabFolderObject.getItems()) tabItem.dispose();
			createTable();
		}
	}
	
	/**
	 * select user databse
	 * @param selectElement
	 */
	private void selectUserDB(Object selectElement) {
		UserDBDAO selectUserDb = null;
		if (selectElement instanceof UserDBDAO) selectUserDb = (UserDBDAO)selectElement;
		else 									selectUserDb = ((UserDBResourceDAO)selectElement).getParent();
		
		// 기존 디비가 중복 선택되었으면 리프레쉬 하지 않는다.
		if (userDB != null) {
			if (userDB.getSeq() == selectUserDb.getSeq()) {
				textSearch.setText(selectUserDb.getSchema());
				filterText();
				userDB = selectUserDb;		
				return;
			}
		}
		// 디비 선택
		userDB = selectUserDb;

		// 존재하는 tadfolder를 삭제한다.
		for (CTabItem tabItem : tabFolderObject.getItems()) tabItem.dispose();
		
		// is dblock
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_lock()) &&
				!SessionManager.isUnlockDB(selectUserDb)) {
			userDB = null;
			createTable();
		} else {
			initObjectDetail(userDB.getDBDefine());
		}
	}
	
	/**
	 * 다른 디비가 선택되어 지면 초기화 되어야 할 object 목록
	 * 
	 * @param dbDefine Manager에서 선택된 object
	 */
	private void initObjectDetail(DBDefine dbDefine) {
		// sqlite
		if (dbDefine == DBDefine.SQLite_DEFAULT) {
			createTable();
			createView();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(), 
				tableComposite.getTableColumnViewer(),
				tableComposite.getIndexComposite().getTableViewer(),
				tableComposite.getTriggerComposite().getTableViewer(),
				viewComposite.getTableViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));

		// tajo, hive, hive2
		} else if (dbDefine == DBDefine.TAJO_DEFAULT | dbDefine == DBDefine.HIVE_DEFAULT | dbDefine == DBDefine.HIVE2_DEFAULT) {
			createTable();
			
			arrayStructuredViewer = new StructuredViewer[] { 
					tableComposite.getTableListViewer(),
					tableComposite.getTableColumnViewer()
				};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
				
		// mongodb
		} else if (dbDefine == DBDefine.MONGODB_DEFAULT) {
			createMongoCollection();
			createMongoIndex();
			createMongoJavaScript();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				mongoCollectionComposite.getCollectionListViewer(),
				mongoIndexComposite.getTableViewer(),
				mongoJavaScriptComposite.getTableViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, mongoCollectionComposite.getCollectionListViewer()));

		// oracle , tibero
		} else if (dbDefine == DBDefine.ORACLE_DEFAULT | dbDefine == DBDefine.TIBERO_DEFAULT) {
			createTable();
			createView();
			createSynonym();
			createProcedure();
			createPackage();
			createFunction();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(),
				tableComposite.getTableColumnViewer(),
				tableComposite.getIndexComposite().getTableViewer(),
				tableComposite.getConstraintsComposite().getTableViewer(),
				tableComposite.getTriggerComposite().getTableViewer(),
				viewComposite.getTableViewer(), 
				synonymComposite.getTableviewer(), 
				procedureComposite.getTableViewer(), 
				packageComposite.getPackageTableViewer(), 
				packageComposite.getProcFuncTableViewer(),
				functionCompostite.getTableviewer() 
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			
		// altibase, cubrid
		} else if (dbDefine == DBDefine.CUBRID_DEFAULT | 
				dbDefine == DBDefine.ALTIBASE_DEFAULT |
				dbDefine == DBDefine.POSTGRE_DEFAULT
		) { 
			createTable();
			createView();
			createProcedure();
			createFunction();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(), 
				tableComposite.getTableColumnViewer(),
				tableComposite.getIndexComposite().getTableViewer(),
				tableComposite.getTriggerComposite().getTableViewer(),
				viewComposite.getTableViewer(), 
				procedureComposite.getTableViewer(), 
				functionCompostite.getTableviewer() 
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			
		// mysql, postgre, mssql
		} else {
			createTable();
			createView();
			createProcedure();
			createFunction();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(), 
				tableComposite.getTableColumnViewer(),
				tableComposite.getIndexComposite().getTableViewer(),
				tableComposite.getConstraintsComposite().getTableViewer(),
				tableComposite.getTriggerComposite().getTableViewer(),
				viewComposite.getTableViewer(), 
				procedureComposite.getTableViewer(), 
				functionCompostite.getTableviewer() 
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
		}
		refershSelectObject(PublicTadpoleDefine.OBJECT_TYPE.TABLES.name());
	}
	
	/**
	 * Refresh the select tab.
	 * 
	 * @param strSelectItemText
	 */
	private void refershSelectObject(String strSelectItemText) {
		refershSelectObject(strSelectItemText, "");
	}
	
	/**
	 * 현재 선택된 tab을 리프레쉬합니다.
	 * 
	 * @param strSelectItemText TabItem text
	 * @param strObjectName
	 */
	private void refershSelectObject(String strSelectItemText, String strObjectName) {

		if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.TABLES.name())) {
			refreshTable(true, strObjectName);
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.INDEXES.name())) {
			refreshIndexes(true, strObjectName);
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.CONSTRAINTS.name())) {
			refreshConstraints(true, strObjectName);
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.TRIGGERS.name())) {
			refreshTrigger(true, strObjectName);	
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.VIEWS.name())) {
			refreshView(true, strObjectName);
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.SYNONYM.name())) {
			refreshSynonym(true, strObjectName);
		
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.PROCEDURES.name())) {
			refreshProcedure(true, strObjectName);
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.PACKAGES.name())) {
			refreshPackage(true, strObjectName);
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.FUNCTIONS.name())) {
			refreshFunction(true, strObjectName);
		
		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.JAVASCRIPT.name())) {
			refreshJS(true, strObjectName);
		}
		filterText();
		
		// google analytic
		AnalyticCaller.track(ExplorerViewer.ID, strSelectItemText);
	}
	
	/**
	 * selected tab refresh
	 */
	private void refreshSelectTab() {
//		if(logger.isDebugEnabled()) logger.debug("tabFolderObject.getSelection().getText()" + tabFolderObject.getSelection().getText());
		refershSelectObject(tabFolderObject.getSelection().getText(), "");
	}
	
	/**
	 * mongo server side javascript define
	 */
	private void createMongoJavaScript() {
		mongoJavaScriptComposite = new TadpoleMongoDBJavaScriptComposite(getSite(), tabFolderObject, userDB);
		mongoJavaScriptComposite.initAction();
	}
	
	/**
	 * mongodb collection define
	 */
	private void createMongoCollection() {
		mongoCollectionComposite = new TadpoleMongoDBCollectionComposite(getSite(), tabFolderObject, userDB);
		mongoCollectionComposite.initAction();
		tabFolderObject.setSelection(0);
	}
	
	/**
	 * mongodb index define
	 */
	private void createMongoIndex() {
		mongoIndexComposite = new TadpoleMongoDBIndexesComposite(getSite(), tabFolderObject, userDB);
		mongoIndexComposite.initAction();
	}

	/**
	 * Procedure 정의
	 */
	private void createFunction() {
		functionCompostite = new TadpoleFunctionComposite(getSite(), tabFolderObject, userDB);
		functionCompostite.initAction();
	}

	/**
	 * Procedure 정의
	 */
	private void createProcedure() {
		procedureComposite = new TadpoleProcedureComposite(getSite(), tabFolderObject, userDB);
		procedureComposite.initAction();
	}

	/**
	 * Package 정의
	 */
	private void createPackage() {
		packageComposite = new TadpolePackageComposite(getSite(), tabFolderObject, userDB);
		packageComposite.initAction();
	}

	/**
	 * view 정의
	 */
	private void createView() {
		viewComposite = new TadpoleViewerComposite(getSite(), tabFolderObject, userDB);
		viewComposite.initAction();
	}
	
	/**
	 * Table 정의
	 */
	private void createTable() {
		tableComposite = new TadpoleTableComposite(getSite(), tabFolderObject, userDB);
		tableComposite.initAction();
		tabFolderObject.setSelection(0);
	}

	/**
	 * Synonym 정의
	 */
	private void createSynonym() {
		synonymComposite = new TadpoleSynonymComposite(getSite(), tabFolderObject, userDB);
		synonymComposite.initAction();
	}

	/**
	 * Synonym 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshSynonym(boolean boolRefresh, String strObjectName) {
		synonymComposite.refreshSynonym(getUserDB(), boolRefresh, strObjectName);
	}

	/**
	 * view 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshView(boolean boolRefresh, String strObjectName) {
		viewComposite.refreshView(getUserDB(), boolRefresh, strObjectName);
	}

	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes(boolean boolRefresh, String strObjectName) {
		if(userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {
			mongoIndexComposite.refreshIndexes(userDB, boolRefresh);
		} else {
			tableComposite.getIndexComposite().refreshIndexes(getUserDB(), boolRefresh, strObjectName);
		}
	}
	
	/**
	 * constraints 정보를 최신으로 갱신 합니다.
	 */
	public void refreshConstraints(boolean boolRefresh, String strObjectName) {
		tableComposite.getConstraintsComposite().refreshConstraints(getUserDB(), boolRefresh, strObjectName);
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 */
	public void refreshProcedure(boolean boolRefresh, String strObjectName) {
		procedureComposite.refreshProcedure(userDB, boolRefresh, strObjectName);
	}

	/**
	 * package 정보를 최신으로 갱신 합니다.
	 */
	public void refreshPackage(boolean boolRefresh, String strObjectName) {
		packageComposite.refreshPackage(userDB, boolRefresh, strObjectName);
	}

	/**
	 * trigger 정보를 최신으로 갱신 합니다.
	 */
	public void refreshTrigger(boolean boolRefresh, String strObjectName) {
		tableComposite.getTriggerComposite().refreshTrigger(userDB, boolRefresh, strObjectName);
	}

	/**
	 * function 정보를 최신으로 갱신 합니다.
	 */
	public void refreshFunction(boolean boolRefresh, String strObjectName) {
		functionCompostite.refreshFunction(userDB, boolRefresh, strObjectName);
	}

	/**
	 * table 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshTable(boolean boolRefresh, String strObjectName) {
		if(userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {		
			mongoCollectionComposite.refreshTable(userDB, boolRefresh);	
		} else {
			tableComposite.refreshTable(userDB, boolRefresh, strObjectName);
		}
	}
	
	/**
	 * select table refresh
	 */
	public void refreshTableColumn() {
		tableComposite.refreshTableColumn();		
	}
	
	/**
	 * mongodb server side javascript define
	 */
	public void refreshJS(boolean boolRefresh, String strObjectName) {
		mongoJavaScriptComposite.refreshJavaScript(userDB, boolRefresh);
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	/**
	 *  refresh object
	 * 
	 * @param chgUserDB
	 * @param reqQuery
	 */
	public void refreshCurrentTab(UserDBDAO chgUserDB, final RequestQuery reqQuery) {
		if(reqQuery.getSqlDDLType() == null) return;
		
		QUERY_DDL_TYPE queryDDLType = reqQuery.getSqlDDLType();
		String strObjectName = reqQuery.getSqlObjectName();
		
		refreshCurrentTab(queryDDLType, strObjectName, chgUserDB);
		// refresh filter
		filterText();
	}
	
	/**
	 * 
	 * @param queryDDLType
	 * @param strObjectName
	 * @param chgUserDB
	 */
	public void refreshCurrentTab(QUERY_DDL_TYPE queryDDLType, String strObjectName, UserDBDAO chgUserDB) {
		if (this.userDB.getSeq() != chgUserDB.getSeq())	return;
		
		if(queryDDLType == QUERY_DDL_TYPE.TABLE) {
			refershSelectObject(OBJECT_TYPE.TABLES.name(), strObjectName);
		} else if(queryDDLType == QUERY_DDL_TYPE.INDEX) {
			refershSelectObject(OBJECT_TYPE.INDEXES.name(), strObjectName);
		} else if(queryDDLType == QUERY_DDL_TYPE.TRIGGER) {
			refershSelectObject(OBJECT_TYPE.TRIGGERS.name(), strObjectName);

		} else if(queryDDLType == QUERY_DDL_TYPE.VIEW) {
			refershSelectObject(OBJECT_TYPE.VIEWS.name(), strObjectName);
		} else if(queryDDLType == QUERY_DDL_TYPE.PROCEDURE) {
			refershSelectObject(OBJECT_TYPE.PROCEDURES.name(), strObjectName);
		} else if(queryDDLType == QUERY_DDL_TYPE.FUNCTION) {
			refershSelectObject(OBJECT_TYPE.FUNCTIONS.name(), strObjectName);
		} else if(queryDDLType == QUERY_DDL_TYPE.PACKAGE) {
			refershSelectObject(OBJECT_TYPE.PACKAGES.name(), strObjectName);
		} else if(queryDDLType == QUERY_DDL_TYPE.SYNONYM) {
			refershSelectObject(OBJECT_TYPE.SYNONYM.name(), strObjectName);
		} else {
			refreshSelectTab();
		}
	}
	
	@Override
	public void setFocus() {
	}

}
