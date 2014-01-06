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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.viewsupport.SelectionProviderMediator;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.collections.TadpoleMongoDBCollectionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.index.TadpoleMongoDBIndexesComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.serversidescript.TadpoleMongoDBJavaScriptComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.function.TadpoleFunctionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.index.TadpoleIndexesComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.orapackage.TadpolePackageComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.TadpoleProcedureComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.sysnonym.TadpoleSynonymComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TadpoleTableComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.trigger.TadpoleTriggerComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.view.TadpoleViewerComposite;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.UserDBResourceDAO;

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

	/**
	 * 현재 오픈된페이지를 리프레쉬한다.
	 */
	public static enum CHANGE_TYPE { DEL, INS };

	private UserDBDAO userDB;
	private CTabFolder tabFolderObject;
	private Text textSearch;
	
	private String strSelectItem;
	private Composite compositeBody;
	private TadpoleTriggerComposite 	triggerComposite 	= null;
	private TadpoleFunctionComposite 	functionCompostite 	= null;
	private TadpoleProcedureComposite	procedureComposite 	= null;
	private TadpolePackageComposite	    packageComposite 	= null;
	private TadpoleIndexesComposite 	indexComposite 		= null;
	private TadpoleViewerComposite 		viewComposite 		= null;
	private TadpoleTableComposite 		tableComposite 		= null;
	private TadpoleSynonymComposite 	synonymComposite 	= null;
	
	// mongodb
	private TadpoleMongoDBCollectionComposite mongoCollectionComposite 	= null;
	private TadpoleMongoDBIndexesComposite mongoIndexComposite 			= null;
	private TadpoleMongoDBJavaScriptComposite mongoJavaScriptComposite 	= null;

	public ExplorerViewer() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 1;
		gl_parent.verticalSpacing = 0;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		Composite compositeSearch = new Composite(parent, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(2, false);
		gl_compositeSearch.horizontalSpacing = 2;
		gl_compositeSearch.verticalSpacing = 2;
		gl_compositeSearch.marginHeight = 2;
		gl_compositeSearch.marginWidth = 2;
		compositeSearch.setLayout(gl_compositeSearch);

		Label lblNewLabel = new Label(compositeSearch, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.ExplorerViewer_0);

		// filter를 설정합니다.
		textSearch = new Text(compositeSearch, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String strSelectTab = tabFolderObject.getItem(tabFolderObject.getSelectionIndex()).getText();
				String strSearchText = textSearch.getText();
				
				if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.COLLECTIONS.toString())) {
					mongoCollectionComposite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.TABLES.toString())) {
					tableComposite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.SYNONYM.toString())) {
					synonymComposite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.VIEWS.toString())) {
					viewComposite.filter(strSearchText);					
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.INDEXES.toString())) {
					if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB)) {
						mongoIndexComposite.filter(strSearchText);
					} else {
						indexComposite.filter(strSearchText);
					}					
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.PROCEDURES.toString())) {
					procedureComposite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.PACKAGES.toString())) {
					packageComposite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.FUNCTIONS.toString())) {
					functionCompostite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.TRIGGERS.toString())) {
					triggerComposite.filter(strSearchText);
				
				} else if (strSelectTab.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.JAVASCRIPT.toString())) {
					mongoJavaScriptComposite.filter(strSearchText);
				}
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
					strSelectItem = ((CTabItem)evt.item).getText();
					refershSelectObject(strSelectItem);
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
		if(null != indexComposite) indexComposite.dispose(); 
		if(null != procedureComposite) procedureComposite.dispose(); 
		if(null != packageComposite) packageComposite.dispose(); 
		if(null != functionCompostite) functionCompostite.dispose(); 
		if(null != triggerComposite) triggerComposite.dispose();
		
		if(null != mongoCollectionComposite) { 
			mongoCollectionComposite.dispose();
			mongoIndexComposite.dispose();
			mongoJavaScriptComposite.dispose();
		}

		// Initialize resources
		if (selectElement instanceof UserDBDAO || selectElement instanceof UserDBResourceDAO) {
			UserDBDAO selectUserDb = null;
			if (selectElement instanceof UserDBDAO) selectUserDb = (UserDBDAO)selectElement;
			else 									selectUserDb = ((UserDBResourceDAO)selectElement).getParent();

			// 기존 디비가 중복 선택되었으면 리프레쉬 하지 않는다.
			if (userDB != null) if (userDB.getSeq() == selectUserDb.getSeq()) return;

			// 디비 선택
			userDB = selectUserDb;
			
			// 존재하는 tadfolder를 삭제한다.
			for (CTabItem tabItem : tabFolderObject.getItems()) tabItem.dispose();
			initObjectDetail(DBDefine.getDBDefine(userDB));
		} else {
			userDB = null;

			// 존재하는 tadfolder를 삭제한다.
			for (CTabItem tabItem : tabFolderObject.getItems()) tabItem.dispose();
			createTable();
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
			createIndexes();
			createTrigger();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(), 
				tableComposite.getTableColumnViewer(),
				viewComposite.getViewListViewer(), 
				indexComposite.getTableViewer(), 
				triggerComposite.getTableViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			strSelectItem = PublicTadpoleDefine.DB_ACTION.TABLES.toString();
			
		} else if (dbDefine == DBDefine.TAJO_DEFAULT) {
			createTable();
			
			arrayStructuredViewer = new StructuredViewer[] { 
					tableComposite.getTableListViewer() 
				};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			strSelectItem = PublicTadpoleDefine.DB_ACTION.TABLES.toString();
				
		// hive
		} else if (dbDefine == DBDefine.HIVE_DEFAULT || dbDefine == DBDefine.HIVE2_DEFAULT) {
			createTable();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(),
				tableComposite.getTableColumnViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			strSelectItem = PublicTadpoleDefine.DB_ACTION.TABLES.toString();
			
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
			
			strSelectItem = PublicTadpoleDefine.DB_ACTION.COLLECTIONS.toString();
			
		} else if (dbDefine == DBDefine.ORACLE_DEFAULT) {
			createTable();
			createView();
			createSynonym();
			createIndexes();
			createProcedure();
			createPackage();
			createFunction();
			createTrigger();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(),
				tableComposite.getTableColumnViewer(),
				viewComposite.getViewListViewer(), 
				synonymComposite.getSynonymListViewer(),
				synonymComposite.getSynonymColumnViewer(),
				indexComposite.getTableViewer(), 
				procedureComposite.getTableViewer(), 
				packageComposite.getTableViewer(), 
				packageComposite.getSubTableViewer(),
				functionCompostite.getTableviewer(), 
				triggerComposite.getTableViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			
			strSelectItem = PublicTadpoleDefine.DB_ACTION.TABLES.toString();
		// cubrid, mysql, postgre, mssql
		} else {
			createTable();
			createView();
			createIndexes();
			createProcedure();
			createFunction();
			createTrigger();
			
			arrayStructuredViewer = new StructuredViewer[] { 
				tableComposite.getTableListViewer(), 
				tableComposite.getTableColumnViewer(),
				viewComposite.getViewListViewer(), 
				indexComposite.getTableViewer(), 
				procedureComposite.getTableViewer(), 
				functionCompostite.getTableviewer(), 
				triggerComposite.getTableViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructuredViewer, tableComposite.getTableListViewer()));
			
			strSelectItem = PublicTadpoleDefine.DB_ACTION.TABLES.toString();
		}
		
		refreshTable(false);
		
	}
	
	/**
	 * 현재 선택된 tab을 리프레쉬합니다.
	 * 
	 * @param strSelectItemText TabItem text
	 */
	private void refershSelectObject(String strSelectItemText) {
//		테이블 초기화 될때 무조건 리프레쉬 되므로 다시리프레쉬 되는것을 막습니다.
//		if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.TABLES.toString())) {
//			refreshTable(false);
//		} else
		if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.VIEWS.toString())) {
			refreshView(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.SYNONYM.toString())) {
			refreshSynonym(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.INDEXES.toString())) {
			refreshIndexes(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.PROCEDURES.toString())) {
			refreshProcedure(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.PACKAGES.toString())) {
			refreshPackage(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.FUNCTIONS.toString())) {
			refreshFunction(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.TRIGGERS.toString())) {
			refreshTrigger(false);
		} else if (strSelectItemText.equalsIgnoreCase(PublicTadpoleDefine.DB_ACTION.JAVASCRIPT.toString())) {
			refreshJS(false);
		}
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
	 * Trigger 정의
	 */
	private void createTrigger() {
		triggerComposite = new TadpoleTriggerComposite(getSite(), tabFolderObject, userDB);
		triggerComposite.initAction();
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
	 * indexes 정의
	 */
	private void createIndexes() {
		indexComposite = new TadpoleIndexesComposite(getSite(), tabFolderObject, userDB);
		indexComposite.initAction();
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
	public void refreshSynonym(boolean boolRefresh) {
		synonymComposite.refreshSynonym(getUserDB(), boolRefresh);
	}

	/**
	 * view 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshView(boolean boolRefresh) {
		viewComposite.refreshView(getUserDB(), boolRefresh);
	}

	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes(boolean boolRefresh) {
		if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB)) {
			mongoIndexComposite.refreshIndexes(userDB, boolRefresh);
		} else {
			indexComposite.refreshIndexes(getUserDB(), boolRefresh);
		}
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 */
	public void refreshProcedure(boolean boolRefresh) {
		procedureComposite.refreshProcedure(userDB, boolRefresh);
	}

	/**
	 * package 정보를 최신으로 갱신 합니다.
	 */
	public void refreshPackage(boolean boolRefresh) {
		packageComposite.refreshPackage(userDB, boolRefresh);
	}

	/**
	 * trigger 정보를 최신으로 갱신 합니다.
	 */
	public void refreshTrigger(boolean boolRefresh) {
		triggerComposite.refreshTrigger(userDB, boolRefresh);
	}

	/**
	 * function 정보를 최신으로 갱신 합니다.
	 */
	public void refreshFunction(boolean boolRefresh) {
		functionCompostite.refreshFunction(userDB, boolRefresh);
	}

	/**
	 * table 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshTable(boolean boolRefresh) {
		if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB)) {		
			mongoCollectionComposite.refreshTable(userDB, boolRefresh);	
		} else {
			tableComposite.refreshTable(userDB, boolRefresh);
		}		
	}
	
	/**
	 * mongodb server side javascript define
	 */
	public void refreshJS(boolean boolRefresh) {
		mongoJavaScriptComposite.refreshJavaScript(userDB, boolRefresh);
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	/**
	 *  refresh table
	 * 
	 * @param chgUserDB
	 * @param changeType
	 * @param changeTbName
	 */
	public void refreshCurrentTab(UserDBDAO chgUserDB) {
		if (this.userDB.getSeq() != chgUserDB.getSeq())	return;
		if (tabFolderObject.getSelectionIndex() != 0)	return;

		refershSelectObject(strSelectItem);
	}
	
	@Override
	public void setFocus() {
	}

}
