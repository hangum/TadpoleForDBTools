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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;

/**
 * Explorer view의 abstract class
 * 
 * @author hangum
 *
 */
public abstract class AbstraceExplorerViewer extends ViewPart {

	/**
	 * 디비 중에 올챙이가 테이블,컬럼의 도움말을 제공하는 디비를 정의합니다.
	 */
	protected static DBDefine[] editType = {DBDefine.ORACLE_DEFAULT, DBDefine.POSTGRE_DEFAULT, DBDefine.MYSQL_DEFAULT};
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
		String[] name = {"Trigger", "Event", "Table", "Statement", "Timing",
			"Created", "sql_mode", "Definer", "character_set_client", "collation_connection", "Database",
			"Collation"
		};
		int[] size = {120, 70, 70, 70, 70,
					   70, 70, 70, 70, 70, 
					   70, 70
		};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * Procedure table column
	 * @param tv
	 */
	protected void createProcedureFunctionColumn(TableViewer tv, ObjectComparator comparator) {
		String[] name = {"Name", "Type", "Definer", "Modified", "Created",
						"Security_type", "Comment", "character_set_client", "collation_connection", "Database", 
						"Collation"
		};
		int[] size = {120, 70, 70, 70, 70,
						70, 70, 70, 70, 70, 
						70
		};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * indexes table column
	 * @param tv
	 */
	protected void createIndexesColumn(final TableViewer tv, final ObjectComparator comparator) {
		String[] name = {"TABLE NAME", "INDEX NAME", "NON UNIQUE", "INDEX SCHEMA", "SEQ IN INDEX", 
						"COLUMN NAME", "COLLATION", "CARDINALITY", "SUB PART", "PACKED", 
						"NULLABLE", 	"INDEX TYPE","COMMENT"
		};
		int[] size = {120, 70, 70, 70, 70, 
						70,	70, 70, 70, 70, 
						70, 70,	70
		};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * view column
	 */
	protected void createViewColumne(TableViewer tv) {
		String[] name = {"Field", "Type", "Key", "Comment", "Null", "Default", "Extra"};
		int[] size = {120, 70, 50, 100, 50, 50, 50};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
		}
	}
	
	/**
	 * table table column
	 */
	protected void createTableColumne(TableViewer tv) {
		String[] name = {"Field", "Type", "Key", "Comment", "Null", "Default", "Extra"};
		int[] size = {120, 70, 50, 100, 50, 50, 50};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
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
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		
		return adapter;
	}
}
