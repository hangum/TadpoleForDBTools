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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.constraints;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.TableConstraintsDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.IndexColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB indexes composite
 * 
 * @author hangum
 *
 */
public class TadpoleConstraintComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleConstraintComposite.class);
	
	private CTabItem tbtmConstraint;
	private TableDAO tableDao;
	private String selectConstraintName = ""; //$NON-NLS-1$

	// index
	private TableViewer constraintTableViewer;
	private ObjectComparator constraintComparator;
	private List<InformationSchemaDAO> listConstraints;
	private ConstraintViewFilter constraintFilter;

	// column info
	private TableViewer constraintColumnViewer;
	private ObjectComparator constraintColumnComparator;
	private List showConstraintColumns;

	private ObjectCreatAction creatAction_Constraint;
	private ObjectDropAction dropAction_Constraint;
	private ObjectRefreshAction refreshAction_Constraint;
//	private GenerateViewDDLAction viewDDLAction;

	/**
	 * indexes info
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleConstraintComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmConstraint = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmConstraint.setText(Messages.get().Constraints);
		tbtmConstraint.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS.name());

		Composite compositeConstraints = new Composite(tabFolderObject, SWT.NONE);
		tbtmConstraint.setControl(compositeConstraints);
		GridLayout gl_compositeConstraints = new GridLayout(1, false);
		gl_compositeConstraints.verticalSpacing = 2;
		gl_compositeConstraints.horizontalSpacing = 2;
		gl_compositeConstraints.marginHeight = 2;
		gl_compositeConstraints.marginWidth = 2;
		compositeConstraints.setLayout(gl_compositeConstraints);

		SashForm sashForm = new SashForm(compositeConstraints, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		constraintTableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
	
		constraintTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {

				// 인덱스 디테일한 정보를 확인할동안은 블럭으로 만들어 놓습니다.
				if (userDB.getDBDefine() == DBDefine.SQLite_DEFAULT 
						//userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT 
						//userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT
				)  return;
				
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object tableDAO = is.getFirstElement();

					if (tableDAO != null) {
						TableConstraintsDAO constraint = (TableConstraintsDAO) tableDAO;

						if (selectConstraintName.equals(constraint.getCONSTRAINT_NAME() )) return;
						selectConstraintName = constraint.getCONSTRAINT_NAME();

						SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
						HashMap<String, String>paramMap = new HashMap<String, String>();
						paramMap.put("table_schema", constraint.getTABLE_SCHEMA()); //$NON-NLS-1$
						paramMap.put("table_name", constraint.getTABLE_NAME()); //$NON-NLS-1$
						paramMap.put("constraint_type", constraint.getConstraint_type()); //$NON-NLS-1$
						paramMap.put("constraint_name", constraint.getCONSTRAINT_NAME()); //$NON-NLS-1$
						paramMap.put("index_name", constraint.getINDEX_NAME()); //$NON-NLS-1$
						
						//showConstraintColumns = sqlClient.queryForList("constraintDetailList", paramMap); //$NON-NLS-1$
						// TODO: DBMS별로 제약조건 세부내역을 어떻게 표시할지 고민해봐야함.
						// 오라클의 fk같은경우는 연결된 index나 pk정보를 표시하면 될듯하고...
						// check 제약조건인경우는 column2 >= 100 and column2 <= 1000  형식의 정의식을 표시하거나 column9 is not null 과 같은 정보를 표시해야함. LONG타입이므로 blob타입 처리에 대한 방법도 확인이 필요함.
						showConstraintColumns = null;
					} else
						showConstraintColumns = null;

					constraintColumnViewer.setInput(showConstraintColumns);
					constraintColumnViewer.refresh();
					TableUtil.packTable(constraintColumnViewer.getTable());

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});		
		
		
		Table tableTableList = constraintTableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		constraintComparator = new DefaultComparator();
		constraintTableViewer.setSorter(constraintComparator);

		createIndexesColumn(constraintTableViewer, constraintComparator);

		constraintTableViewer.setLabelProvider(new ConstraintLabelProvider());
		constraintTableViewer.setContentProvider(new ArrayContentProvider());

		constraintFilter = new ConstraintViewFilter();
		constraintTableViewer.addFilter(constraintFilter);
		
		// columns
		constraintColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = constraintColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		constraintColumnComparator = new IndexColumnComparator();
		constraintColumnViewer.setSorter(constraintColumnComparator);
		constraintColumnComparator.setColumn(0);

		createIndexColumne(constraintColumnViewer);

		constraintColumnViewer.setContentProvider(new ArrayContentProvider());
		constraintColumnViewer.setLabelProvider(new ConstraintColumnLabelprovider());
		

		createMenu();
		// index detail column

		sashForm.setWeights(new int[] { 1, 1 });

	}

	/**
	 * selection adapter
	 * 
	 * @param indexColumn
	 * @param i
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn indexColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				constraintColumnComparator.setColumn(index);
				
				constraintColumnViewer.getTable().setSortDirection(constraintColumnComparator.getDirection());
				constraintColumnViewer.getTable().setSortColumn(indexColumn.getColumn());
				
				constraintColumnViewer.refresh();
			}
		};
		
		return selectionAdapter;
	}

	/**
	 * index column list
	 */
	protected void createIndexColumne(final TableViewer tv) {
		String[] name = {Messages.get().SEQ, Messages.get().Column, Messages.get().Order};
		int[] size = {60, 300, 50};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn indexColumn = new TableViewerColumn(tv, SWT.LEFT);
			indexColumn.getColumn().setText(name[i]);
			indexColumn.getColumn().setWidth(size[i]);
			indexColumn.getColumn().addSelectionListener(getSelectionAdapter(indexColumn, i));
		}
	}
	/**
	 * create menu
	 * 
	 */
	private void createMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Constraint = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS, Messages.get().TadpoleIndexesComposite_1);
		dropAction_Constraint = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS, Messages.get().TadpoleIndexesComposite_2);
		refreshAction_Constraint = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS, Messages.get().Refresh);
		
//		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().TadpoleIndexesComposite_7);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			if(!isDDLLock()) {
				menuMgr.add(creatAction_Constraint);
				menuMgr.add(dropAction_Constraint);
				menuMgr.add(new Separator());
			}
			menuMgr.add(refreshAction_Constraint);
//			menuMgr.add(new Separator());
//			menuMgr.add(viewDDLAction);
		}

		constraintTableViewer.getTable().setMenu(menuMgr.createContextMenu(constraintTableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, constraintTableViewer);
	}

	/**
	 * init action
	 */
	public void initAction() {
		if (listConstraints != null) listConstraints.clear();
		constraintTableViewer.setInput(listConstraints);
		constraintTableViewer.refresh();

		if(getUserDB() == null) return;
		creatAction_Constraint.setUserDB(getUserDB());
		dropAction_Constraint.setUserDB(getUserDB());
		refreshAction_Constraint.setUserDB(getUserDB());
		
//		viewDDLAction.setUserDB(getUserDB());
	}
	
	public void setTable(UserDBDAO userDB, TableDAO tableDao) {
		this.userDB = userDB;
		this.tableDao = tableDao;
				
		refreshConstraints(userDB, true, "");
	}
	
	/**
	 * index 정보를 최신으로 갱신 합니다.
	 * @param strObjectName 
	 */
	public void refreshConstraints(final UserDBDAO userDB, boolean boolRefresh, String strObjectName) {
		if(!boolRefresh) if(listConstraints != null) return;
		
		this.userDB = userDB;
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("table_schema", userDB.getDb());
			map.put("table_name", tableDao.getName());
			
			listConstraints = sqlClient.queryForList("tableConstraintsList", map); //$NON-NLS-1$

			constraintTableViewer.setInput(listConstraints);
			constraintTableViewer.refresh();
			
			TableUtil.packTable(constraintTableViewer.getTable());

			// select tabitem
			getTabFolderObject().setSelection(tbtmConstraint);
			
			selectDataOfTable(strObjectName);
		} catch (Exception e) {
			logger.error("index refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_1, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		constraintFilter.setSearchText(textSearch);
		constraintTableViewer.refresh();		
	}
	
	/**
	 * table viewer
	 * @return
	 */
	public TableViewer getTableViewer() {
		return constraintTableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		constraintFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		creatAction_Constraint.dispose();
		dropAction_Constraint.dispose();
		refreshAction_Constraint.dispose();
//		viewDDLAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableViewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<listConstraints.size(); i++) {
			TableConstraintsDAO tableDao = (TableConstraintsDAO)getTableViewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getCONSTRAINT_NAME() )) {
				getTableViewer().setSelection(new StructuredSelection(getTableViewer().getElementAt(i)), true);
				break;
			}
		}		
	}
}
