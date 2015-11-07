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
package com.hangum.tadpole.importexport.core.editors.mongodb.composite.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.importexport.core.editors.mongodb.composite.ModTableDAO;

/**
 * 데이터 임포트 할것인지.
 * 
 * @author hangum
 *
 */
public class RenameColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	
	public RenameColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ModTableDAO modDao = (ModTableDAO)element;
		return modDao.getReName();
	}

	@Override
	protected void setValue(Object element, Object value) {
		ModTableDAO modDao = (ModTableDAO)element;
		modDao.setReName(value.toString());
		
		viewer.update(element, null);
	}

}
