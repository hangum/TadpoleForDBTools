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
package com.hangum.tadpole.manager.core.dialogs.users;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * SampleDAtaEditingSupport
 * 
 * @author hangum
 */
public class TargetDBEditingSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917152389392816571L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FindUserAndDBRoleDialog.class);

	private final TableViewer viewer;
	private int columnIndex;

	public TargetDBEditingSupport(TableViewer viewer, int columnIndex) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		

		UserDBDAO dao = (UserDBDAO) element;

		if (columnIndex == 0) {

			return new CheckboxCellEditor(null, SWT.CHECK);// | SWT.READ_ONLY);

		} else{
			return null;
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		UserDBDAO dao = (UserDBDAO) element;

		if ( columnIndex == 0) {
			
				return true;
			
		} else {
			return false;
		}
	}

	@Override
	protected Object getValue(Object element) {
		UserDBDAO dao = (UserDBDAO) element;

		 if (columnIndex == 0) {
			return dao.isSelect();
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		UserDBDAO dao = (UserDBDAO) element;
		if (columnIndex ==  0) {
			dao.setSelect((Boolean) value);
		}

		viewer.update(element, null);
	}

}
