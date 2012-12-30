/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.collections.TadpoleCollectionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.function.TadpoleFunctionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.index.TadpoleIndexesComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.procedure.TadpoleProcedureComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.table.TadpoleTableComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.trigger.TadpoleTriggerComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.view.TadpoleViewerComposite;
import com.hangum.tadpole.util.TadpoleWidgetUtils;
import com.hangum.tadpole.viewsupport.SelectionProviderMediator;

/**
 * object viewer
 * 
 * @author hangumNote
 * 
 */
public class ExplorerViewer extends ViewPart {
	private static Logger logger = Logger.getLogger(ExplorerViewer.class);
	public static String ID = "com.hangum.tadpole.rdb.core.view.object.explorer"; //$NON-NLS-1$
	private StructuredViewer[] arrayStructureViewer = null;
	
	/** tabfolder가 초기화 될때는 tab select 이벤트 먹지 않도록 조절하지 않도록 */
	private boolean boolInitObjectHead = true;

	/**
	 * 현재 오픈된페이지를 리프레쉬한다.
	 */
	public static enum CHANGE_TYPE { DEL, INS };

	private UserDBDAO userDB;
	private CTabFolder tabFolderObject;
	private Text textSearch;
	
	private Composite compositeBody;
	private TadpoleTriggerComposite 	triggerComposite 	= null;
	private TadpoleFunctionComposite 	functionCompostite 	= null;
	private TadpoleProcedureComposite	procedureComposite 	= null;
	private TadpoleIndexesComposite 	indexComposite 		= null;
	private TadpoleViewerComposite 		viewComposite 		= null;
	private TadpoleTableComposite 		tableCompost 		= null;
	
	// mongodb
	private TadpoleCollectionComposite collectionComposite = null;

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
		textSearch = new Text(compositeSearch, SWT.BORDER);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				CTabItem ti = tabFolderObject.getItem(tabFolderObject.getSelectionIndex());
				if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TABLES.toString())) {
					tableCompost.filter(textSearch.getText());
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.VIEWS.toString())) {
					viewComposite.filter(textSearch.getText());
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.INDEXES.toString())) {
					indexComposite.filter(textSearch.getText());
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.PROCEDURES.toString())) {
					procedureComposite.filter(textSearch.getText());
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.FUNCTIONS.toString())) {
					functionCompostite.filter(textSearch.getText());
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TRIGGERS.toString())) {
					triggerComposite.filter(textSearch.getText());
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
					String strSelectItem = ((CTabItem)evt.item).getText();
					refershSelectObject(strSelectItem);
				}
			}
		});
		tabFolderObject.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// dumy table
		createTable();

		// 왼쪽 트리에서 데이터 받았는지.
		getSite().getPage().addSelectionListener(ManagerViewer.ID, managementViewerListener);

		// erd table에 선택되었을때..
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == Define.SELECT_ERD_TABLE) {
					String tableName = event.getNewValue().toString();
					if (tabFolderObject.getSelectionIndex() != 0) tabFolderObject.setSelection(0);

					tableCompost.selectTable(tableName);
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
	private void initObjectHead(Object selectElement) {
		if (selectElement instanceof UserDBDAO || selectElement instanceof UserDBResourceDAO) {
			UserDBDAO selectUserDb = null;
			if (selectElement instanceof UserDBDAO) selectUserDb = (UserDBDAO)selectElement;
			else 									selectUserDb = ((UserDBResourceDAO)selectElement).getParent();

			// 기존 디비가 중복 선택되었으면 리프레쉬 하지 않는다.
			if (userDB != null) if (userDB.getSeq() == selectUserDb.getSeq()) return;

			// 디비 선택
			userDB = selectUserDb;
			getViewSite().getActionBars().getStatusLineManager().setMessage(userDB.getDb());
			
			// 존재하는 tadfolder를 삭제한다.
			for (CTabItem tabItem : tabFolderObject.getItems()) tabItem.dispose();
			initObjectDetail(DBDefine.getDBDefine(userDB.getTypes()));

		} else {
			userDB = null;
			getViewSite().getActionBars().getStatusLineManager().setMessage("");

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
			
			// view의 set selection provider 설정
			arrayStructureViewer = new StructuredViewer[] { 
					tableCompost.getTableListViewer(), 
					viewComposite.getViewListViewer(), 
					indexComposite.getTableViewer(), 
					triggerComposite.getTableViewer()
				};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructureViewer, tableCompost.getTableListViewer()));
			
			refreshTable(false);
			
		// mongodb
		} else if (dbDefine == DBDefine.MONGODB_DEFAULT) {
			
			createMongoCollection();
			refreshTable(false);
			
			arrayStructureViewer = new StructuredViewer[] { 
				collectionComposite.getCollectionListViewer()
			};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructureViewer, collectionComposite.getCollectionListViewer()));

		// cubrid, mysql, oracle, postgre, mssql
		} else {
			createTable();
			createView();
			createIndexes();
			createProcedure();
			createFunction();
			createTrigger();
			
			arrayStructureViewer = new StructuredViewer[] { 
					tableCompost.getTableListViewer(), 
					viewComposite.getViewListViewer(), 
					indexComposite.getTableViewer(), 
					procedureComposite.getTableViewer(), 
					functionCompostite.getTableviewer(), 
					triggerComposite.getTableViewer()
				};
			getViewSite().setSelectionProvider(new SelectionProviderMediator(arrayStructureViewer, tableCompost.getTableListViewer()));
		
			refreshTable(false);
		}
	}
	
	/**
	 * 현재 선택된 tab을 리프레쉬합니다.
	 * 
	 * @param strSelectItemText TabItem text
	 */
	private void refershSelectObject(String strSelectItemText) {
//		if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TABLES.toString())) {
//			refreshTable();
//			System.out.println("\t =========== refresh table=-");
//		} else 
		if (strSelectItemText.equalsIgnoreCase(Define.DB_ACTION.VIEWS.toString())) {
			refreshView(false);
		} else if (strSelectItemText.equalsIgnoreCase(Define.DB_ACTION.INDEXES.toString())) {
			refreshIndexes(false);
		} else if (strSelectItemText.equalsIgnoreCase(Define.DB_ACTION.PROCEDURES.toString())) {
			refreshProcedure(false);
		} else if (strSelectItemText.equalsIgnoreCase(Define.DB_ACTION.FUNCTIONS.toString())) {
			refreshFunction(false);
		} else if (strSelectItemText.equalsIgnoreCase(Define.DB_ACTION.TRIGGERS.toString())) {
			refreshTrigger(false);
		}
	}
	
	/**
	 * mongodb collection
	 */
	private void createMongoCollection() {
		collectionComposite = new TadpoleCollectionComposite(getSite(), tabFolderObject, userDB);
		collectionComposite.initAction();
		tabFolderObject.setSelection(0);
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
		tableCompost = new TadpoleTableComposite(getSite(), tabFolderObject, userDB);
		tableCompost.initAction();
		tabFolderObject.setSelection(0);
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
		indexComposite.refreshIndexes(getUserDB(), boolRefresh);
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 */
	public void refreshProcedure(boolean boolRefresh) {
		procedureComposite.refreshProcedure(userDB, boolRefresh);
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
		if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {		
			collectionComposite.refreshTable(userDB, boolRefresh);	
		} else {
			tableCompost.refreshTable(userDB, boolRefresh);
		}		
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
	public void refreshCurrentTab(UserDBDAO chgUserDB, CHANGE_TYPE changeType, String changeTbName) {
		if (this.userDB.getSeq() != chgUserDB.getSeq())	return;
		if (tabFolderObject.getSelectionIndex() != 0)	return;

		tableCompost.refreshTable(changeType, changeTbName);
	}
	
	@Override
	public void setFocus() {
	}

}
