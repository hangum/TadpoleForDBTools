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
package com.hangum.tadpole.rdb.core.viewers.object.sub;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;

/**
 * Object explorer composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractObjectComposite extends Composite {
	protected IWorkbenchPartSite site;
	protected CTabFolder tabFolderObject;
	
	/** TAB DATA KEY */
	public static String TAB_DATA_KEY = "DB_ACTION";
	
	protected UserDBDAO userDB;
	protected int DND_OPERATIONS = DND.DROP_COPY | DND.DROP_MOVE;
	
	/**
	 * 디비 중에 올챙이가 테이블,컬럼의 도움말을 제공하는 디비를 정의합니다.
	 */
	protected static DBDefine[] editType = {DBDefine.ORACLE_DEFAULT, DBDefine.POSTGRE_DEFAULT, DBDefine.MYSQL_DEFAULT, DBDefine.MARIADB_DEFAULT};

	/**
	 * is insert lock?
	 * @return
	 */
	public boolean isInsertLock() {
		return PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getInsert_lock());
	}
	
	/**
	 * is update lock?
	 * @return
	 */
	public boolean isUpdateLock() {
		return PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getUpdate_lock());
	}
	
	/**
	 * is delete lock?
	 * @return
	 */
	public boolean isDeleteLock() {
		return PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDelete_locl());
	}
	
	/**
	 * is ddl lock?
	 * 
	 * @return
	 */
	public boolean isDDLLock() {
		return PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDdl_lock());
	}
	
	/**
	 * 
	 * @param site
	 * @param parent
	 * @param userDB
	 */
	public AbstractObjectComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(tabFolderObject, SWT.NONE);
		
		this.site = site;
		this.tabFolderObject = tabFolderObject;
		this.userDB = userDB;
	}
	
	/**
	 * select userDB
	 * @return
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	protected String getUserRoleType() {
		return userDB.getRole_id();//SessionManager.getRoleType(getUserDB());
	}
	
	/**
	 * select site
	 * @return
	 */
	public IWorkbenchPartSite getSite() {
		return site;
	}
	
	/**
	 * search text
	 * @param searchText
	 */
	public abstract void setSearchText(String searchText);
	
	/**
	 * init action
	 */
	public abstract void initAction();
	
	/**
	 * select data of table
	 */
	public abstract void selectDataOfTable(String strObjectName);

	/**
	 * 테이블, 테이블 컬럼의 컬럼을 에디트 할수 있는지.
	 * @param userDB
	 * @return
	 */
	protected boolean isCommentEdit(UserDBDAO userDB) {
		if(userDB == null) return false;
		
		for (DBDefine dbType : editType) {
			if(dbType.getDBToString().equals(userDB.getDb())) return true;
		}
		
		return false;
	}
	
	/**
	 * trigger table column
	 * @param tv
	 */
	protected void createTriggerColumn(TableViewer tv, ObjectComparator comparator) {
		String[] name = {Messages.get().Trigger, Messages.get().Event, Messages.get().Table, Messages.get().Statement, Messages.get().Timing,
			Messages.get().Created, Messages.get().AbstractObjectComposite_6, Messages.get().Definer, Messages.get().AbstractObjectComposite_8, Messages.get().AbstractObjectComposite_9, Messages.get().Database,
			Messages.get().Collation
		};
		int[] size = {120, 70, 70, 70, 70,
					   70, 70, 70, 70, 70, 
					   70, 70
		};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().setMoveable(true);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * Procedure table column
	 * @param tv
	 */
	protected void createProcedureFunctionColumn(TableViewer tv, ObjectComparator comparator) {
		String[] name = {Messages.get().Name, Messages.get().Definer, Messages.get().Modified, Messages.get().Created,
						Messages.get().AbstractObjectComposite_16, Messages.get().Comment, Messages.get().AbstractObjectComposite_18, Messages.get().AbstractObjectComposite_19, Messages.get().Database, 
						Messages.get().Collation
		};
		int[] size = {120, 70, 70, 70,
						70, 70, 70, 70, 70, 
						70
		};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().setMoveable(true);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * indexes table column
	 * @param tv
	 */
	protected void createIndexesColumn(final TableViewer tv, final ObjectComparator comparator) {
		String[] name = {Messages.get().TableName, Messages.get().IndexName,Messages.get().Type,Messages.get().Comment};
		int[] size = {120, 120, 70, 70};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().setMoveable(true);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * 
	 * @param tv
	 * @param comparator
	 */
	protected void crateSchedule(final TableViewer tv, final ObjectComparator comparator) {
		String[] name = {"WHAT", "JOB", "NEXT_DATE", "NEXT_SEC", "FAILURES", "BROKEN"};
		int[] size = {120, 120, 70, 70, 70, 70};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().setMoveable(true);
//			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * table sorter
	 * 
	 * @param comparator
	 * @param viewer
	 * @param column
	 * @param index
	 * @return
	 */
	protected SelectionAdapter getSelectionAdapter(final TableViewer viewer, final ObjectComparator comparator, final TableColumn column, final int index) {
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				viewer.getTable().setSortDirection(comparator.getDirection());
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		
		return adapter;
	}
	
	/**
	 * get tabFolder
	 * @return
	 */
	protected CTabFolder getTabFolderObject() {
		return tabFolderObject;
	}
	
	@Override
	protected void checkSubclass() {
	}
}
