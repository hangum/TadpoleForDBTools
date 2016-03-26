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
package com.hangum.tadpole.rdb.core.viewers.object.editor;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * <pre>
 * Table Column Editor 
 * Object Explorer의 테이블, 테이블 컬럼의 comment의 직접 에디터.
 * 
 * </pre>
 * 
 * @author hangum
 *
 */
public class TableColumnEditor extends EditingSupport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableColumnEditor.class);
	private ExplorerViewer explorerViewer;
	private TableViewer tableViewer;
	
	/**
	 * 테이블 컬럼 에디터
	 */
	public TableColumnEditor(ExplorerViewer explorerViewer, TableViewer tableViewer) {
		super(tableViewer);

		this.explorerViewer = explorerViewer;
		this.tableViewer = tableViewer;
	}

	/**
	 * 테이블 컬럼 에디터.
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(tableViewer.getTable());
	}

	/**
	 * 수정할수 있는지.
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	/**
	 * table, column commnet
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		
		if(element instanceof TableColumnDAO) {
			TableColumnDAO table = (TableColumnDAO)element;
			return table.getComment();
			
		} else if(element instanceof TableDAO) {
			TableDAO table = (TableDAO)element;
			return table.getComment();
		}
		
		return null;
	}

	/**
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		
		if(element instanceof TableColumnDAO) {
			TableColumnDAO table = (TableColumnDAO)element;
			
		} else if(element instanceof TableDAO) {
			TableDAO table = (TableDAO)element;
		}
		
		tableViewer.update(element, null);
	}

}
