/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableConstraintsDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB constraint composite
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

	// index
	private TableViewer constraintTableViewer;
	private ObjectComparator constraintComparator;
	private List<InformationSchemaDAO> listConstraints;
	private ConstraintViewFilter constraintFilter;

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
		
		createMenu();
		// index detail column

		sashForm.setWeights(new int[] { 1 });
	}

	/**
	 * create menu
	 * 
	 */
	private void createMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Constraint = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS, "Create Constraints");
		dropAction_Constraint = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS, "Drop Constraints");
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

	/** 
	 * setting tabledao
	 * 
	 * @param userDB
	 * @param tableDao
	 */
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
		
		if(creatAction_Constraint != null) creatAction_Constraint.dispose();
		if(dropAction_Constraint != null) dropAction_Constraint.dispose();
		if(refreshAction_Constraint != null) refreshAction_Constraint.dispose();
//		if(viewDDLAction != null) viewDDLAction.dispose();
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
