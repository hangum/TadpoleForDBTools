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
package com.hangum.tadpole.rdb.core.dialog.dml;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.CommentCellEditor;

/**
 * 데이터 임포트 할것인지.
 * 
 * @author hangum
 * 
 */
public class DMLColumnEditingSupport extends EditingSupport {
	private static final Logger logger = Logger.getLogger(DMLColumnEditingSupport.class);

	private final TableViewer viewer;
	private final int columnIndex;
	GenerateStatmentDMLDialog parent;

	public DMLColumnEditingSupport(TableViewer viewer, int colIndex, GenerateStatmentDMLDialog parent) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = colIndex;
		this.parent = parent;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if (columnIndex == 0) {
			return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
		} else {
			//return new TextCellEditor(viewer.getTable());
			return new CommentCellEditor(columnIndex, viewer);
		}

	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ExtendTableColumnDAO modDao = (ExtendTableColumnDAO) element;
		if (columnIndex == 0) {
			return modDao.isCheck();
		} else {
			return modDao.getColumnAlias();
		}
	}

	@Override
	protected void setValue(Object element, Object value) {
		ExtendTableColumnDAO selDao = (ExtendTableColumnDAO) element;
		if (columnIndex == 0) { // Selection check button.
			selDao.setCheck((Boolean) value);
			viewer.update(element, null);
			if (selDao.isCheck()) {
				if ("*".equals(selDao.getField())) {
					for (ExtendTableColumnDAO allDao : (List<ExtendTableColumnDAO>) viewer.getInput()) {
						if (!"*".equals(allDao.getField())) {
							allDao.setCheck(false);
							viewer.update(allDao, null);
						}
					}
				} else {
					ExtendTableColumnDAO firstDao = (ExtendTableColumnDAO) viewer.getElementAt(0);
					firstDao.setCheck(false);
					viewer.update(firstDao, null);
				}
			}
		} else { // Column alias.
			selDao.setColumnAlias(value.toString());
			viewer.update(selDao, null);
		}
		
		parent.queryGenetation();
	}
}
