/*******************************************************************************
 * Copyright (c) 2012 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importdb.core.dialog.importdb.composite;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.importdb.core.dialog.importdb.dao.ModTableDAO;

/**
 * 수정 컬럼의 에디터 유무
 * 
 * @author hangum
 *
 */
public class ModifyEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	
	public ModifyEditingSupport(TableViewer viewer) {
		super(viewer);
		
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ModTableDAO modDao = (ModTableDAO)element;
		return modDao.isModify();
	}

	@Override
	protected void setValue(Object element, Object value) {
		ModTableDAO modDao = (ModTableDAO)element;
		modDao.setModify((Boolean)value);
		
		viewer.update(element, null);
	}

}
