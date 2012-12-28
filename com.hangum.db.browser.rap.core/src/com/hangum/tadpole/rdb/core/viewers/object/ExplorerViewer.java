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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.function.TadpoleFunctionComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.index.TadpoleIndexesComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.procedure.TadpoleProcedureComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.table.TadpoleTableComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.trigger.TadpoleTriggerComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.view.TadpoleViewerComposite;
import com.hangum.tadpole.viewsupport.SelectionProviderMediator;

/**
 * object viewer
 * 
 * @author hangumNote
 * 
 */
public class ExplorerViewer extends ViewPart {

	public static String ID = "com.hangum.tadpole.rdb.core.view.object.explorer"; //$NON-NLS-1$
	private static Logger logger = Logger.getLogger(ExplorerViewer.class);

	/**
	 * 현재 오픈된페이지를 리프레쉬한다.
	 */
	public static enum CHANGE_TYPE {
		DEL, INS
	};

	private UserDBDAO userDB;
	private TabFolder tabFolderObject;
	private Text textSearch;
	
	private TadpoleTriggerComposite 	triggerComposite 	= null;
	private TadpoleFunctionComposite 	functionCompostite 	= null;
	private TadpoleProcedureComposite	 procedureComposite = null;
	private TadpoleIndexesComposite 	indexComposite 		= null;
	private TadpoleViewerComposite 	viewComposite 			= null;
	private TadpoleTableComposite 	tabbleCompost 			= null;

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
				TabItem ti = tabFolderObject.getItem(tabFolderObject.getSelectionIndex());
				if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TABLES.toString())) {
					tabbleCompost.filter(textSearch.getText());
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

		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);

		tabFolderObject = new TabFolder(compositeBody, SWT.NONE);
		tabFolderObject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				if (userDB == null) return;

				TabItem ti = (TabItem) evt.item;
				refershSelectTable(ti);
			}
		});
		tabFolderObject.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTable();

		createView();

		createIndexes();

		createProcedure();

		createFunction();

		createTrigger();

//		// view의 set selection provider 설정
		StructuredViewer[] viewers = new StructuredViewer[] { 
				tabbleCompost.getTableListViewer(), 
				viewComposite.getViewListViewer(), 
				indexComposite.getTableViewer(), 
				procedureComposite.getTableViewer(), 
				functionCompostite.getTableviewer(), 
				triggerComposite.getTableViewer()
			};
		SelectionProviderMediator mediator = new SelectionProviderMediator(viewers, tabbleCompost.getTableListViewer());
		getViewSite().setSelectionProvider(mediator);

		// 왼쪽 트리에서 데이터 받았는지.
		getSite().getPage().addSelectionListener(ManagerViewer.ID, managementViewerListener);

		// erd table에 선택되었을때..
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == Define.SELECT_ERD_TABLE) {
					String tableName = event.getNewValue().toString();
					if (tabFolderObject.getSelectionIndex() != 0) tabFolderObject.setSelection(0);

					tabbleCompost.selectTable(tableName);
				} // end if(event.getProperty()
			} //
		}); // end property change
	}

	/**
	 * 현재 선택된 tab을 리프레쉬합니다.
	 * 
	 * @param ti
	 */
	private void refershSelectTable(TabItem ti) {
		if(userDB == null) return;
		
		if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.VIEWS.toString())) {
			refreshView();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.INDEXES.toString())) {
			refreshIndexes();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.PROCEDURES.toString())) {
			refreshProcedure();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.FUNCTIONS.toString())) {
			refreshFunction();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TRIGGERS.toString())) {
			refreshTrigger();
		}
	}

	/**
	 * Trigger 정의
	 */
	private void createTrigger() {
		triggerComposite = new TadpoleTriggerComposite(getSite(), tabFolderObject, userDB);
	}

	/**
	 * Procedure 정의
	 */
	private void createFunction() {
		functionCompostite = new TadpoleFunctionComposite(getSite(), tabFolderObject, userDB);
	}

	/**
	 * Procedure 정의
	 */
	private void createProcedure() {
		procedureComposite = new TadpoleProcedureComposite(getSite(), tabFolderObject, userDB);
	}

	/**
	 * indexes 정의
	 */
	private void createIndexes() {
		indexComposite = new TadpoleIndexesComposite(getSite(), tabFolderObject, userDB);
	}

	/**
	 * view 정의
	 */
	private void createView() {
		viewComposite = new TadpoleViewerComposite(getSite(), tabFolderObject, userDB);
	}
	
	/**
	 * Table 정의
	 */
	private void createTable() {
		tabbleCompost = new TadpoleTableComposite(getSite(), tabFolderObject, userDB);
	}

	/**
	 * management의 tree가 선택되었을때
	 */
	private ISelectionListener managementViewerListener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {

			if (selection instanceof IStructuredSelection) {
				IStructuredSelection is = (IStructuredSelection) selection;
				initObjectHead(is.getFirstElement());
			} // end selection
			
		} // end selectionchange
	};

	/**
	 * management의 head가 선택되었을때
	 * 
	 * @param element
	 */
	public void initObjectHead(Object element) {
		initSearch();

		if (element instanceof UserDBDAO || element instanceof UserDBResourceDAO) {
			UserDBDAO selectUserDb = null;
			if (element instanceof UserDBDAO) {
				selectUserDb = (UserDBDAO) element;
			} else if (element instanceof UserDBResourceDAO) {
				selectUserDb = ((UserDBResourceDAO) element).getParent();
			}

			// 기존 디비가 중복 선택되었으면 리프레쉬 하지 않는다.
			if (userDB != null) {
				if (userDB.getSeq() == selectUserDb.getSeq()) return;
			}
			userDB = selectUserDb;

			getViewSite().getActionBars().getStatusLineManager().setMessage(userDB.getDb());
			tabFolderObject.setSelection(0);

			tabbleCompost.refreshTable(userDB, "DB"); //$NON-NLS-1$

			initObjectDetail(DBDefine.getDBDefine(userDB.getTypes()));

		} else if (element instanceof ManagerListDTO) {
			ManagerListDTO managerList = (ManagerListDTO) element;

			userDB = null;
			
			// table을 닫는다.
			tabbleCompost.initTable();

			initObjectDetail(managerList.getName());//DbType());
			
		// Connection Manager의 모든 db가 삭제 되었을때 호출됨 
		} else {
			tabFolderObject.setSelection(0);
			userDB = null;

			// table을 닫는다.
			tabbleCompost.initTable();
		}
	}
	
	/**
	 * 다른 디비가 선택되어 지면 초기화 되어야 할 object 목록
	 * 
	 * @param Connection Manager에서 선택된 object
	 */
	private void initObjectDetail(Object selObject) {//DBDefine dbDefie) {
		DBDefine dbDefine = null;
		if(selObject instanceof DBDefine) dbDefine = (DBDefine)selObject;
				
		// dbtype에 따른 object 뷰를 조절합니다.
		if (dbDefine == DBDefine.SQLite_DEFAULT) {
			// procedure, function 항목을 닫는다.
			for (TabItem tabItem : tabFolderObject.getItems()) {
				if (!tabItem.isDisposed()) {
					if ("Procedures".equals(tabItem.getText())) tabItem.dispose(); //$NON-NLS-1$
					else if ("Functions".equals(tabItem.getText())) tabItem.dispose(); //$NON-NLS-1$
				}
			}
		} else if (dbDefine == DBDefine.MONGODB_DEFAULT) {
			
			// table 항목 이외의 모든 항목을 닫는다.
			for (TabItem tabItem : tabFolderObject.getItems()) {
				if (!tabItem.isDisposed()) {
					if (!"Tables".equals(tabItem.getText())) tabItem.dispose(); //$NON-NLS-1$
				}
			}

		} else {

			boolean isViews = false;
			boolean isIndexes = false;

			boolean isProcedure = false;
			boolean isFunction = false;

			boolean isTriggers = false;

			TabItem[] tItems = tabFolderObject.getItems();
			for (TabItem tabItem : tItems) {

				if ("Views".equals(tabItem.getText()))isViews = true; //$NON-NLS-1$
				if ("Indexes".equals(tabItem.getText()))isIndexes = true; //$NON-NLS-1$

				if ("Procedures".equals(tabItem.getText()))isProcedure = true; //$NON-NLS-1$
				if ("Functions".equals(tabItem.getText()))isFunction = true; //$NON-NLS-1$

				if ("Triggers".equals(tabItem.getText()))isTriggers = true; //$NON-NLS-1$
			}

			if (!isViews) 	createView();
			if (!isIndexes) createIndexes();

			if (!isProcedure) createProcedure();
			if (!isFunction)  createFunction();

			if (!isTriggers) createTrigger();
		}

		// table column viewer
		tabbleCompost.initAction();

		// viewer
		viewComposite.initAction();

		// index
		indexComposite.initAction();

		// procedure
		procedureComposite.initAction();

		// function
		functionCompostite.initAction();

		// trigger
		triggerComposite.initAction();
	}
	
	/**
	 * init search
	 */
	private void initSearch() {
		textSearch.setText("");
		
		tabbleCompost.setSearchText("");
		viewComposite.setSearchText("");
		indexComposite.setSearchText("");
		procedureComposite.setSearchText("");
		functionCompostite.setSearchText("");
		triggerComposite.setSearchText("");
	}
	/**
	 * view 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshView() {
		viewComposite.refreshView(getUserDB());
	}

	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes() {
		indexComposite.refreshIndexes(getUserDB());
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 */
	public void refreshProcedure() {
		procedureComposite.refreshProcedure(userDB);
	}

	/**
	 * trigger 정보를 최신으로 갱신 합니다.
	 */
	public void refreshTrigger() {
		triggerComposite.refreshTrigger(userDB);
	}

	/**
	 * function 정보를 최신으로 갱신 합니다.
	 */
	public void refreshFunction() {
		functionCompostite.refreshFunction(userDB);
	}

	/**
	 * table 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshTable(final String source) {		
		tabbleCompost.refreshTable(userDB, source);
	}

	@Override
	public void setFocus() {
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

		tabbleCompost.refreshTable(changeType, changeTbName);
	}
}
