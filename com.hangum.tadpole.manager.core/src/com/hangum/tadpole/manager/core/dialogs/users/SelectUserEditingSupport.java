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

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;

/**
 * SampleDAtaEditingSupport
 * 
 * @author hangum
 */
public class SelectUserEditingSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917152389392816571L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FindUserAndDBRoleDialog.class);

	public static final String[] userRoleNames = { "NONE", PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString(), PublicTadpoleDefine.USER_ROLE_TYPE.MANAGER.toString(), PublicTadpoleDefine.USER_ROLE_TYPE.USER.toString(), PublicTadpoleDefine.USER_ROLE_TYPE.GUEST.toString() };

	private final TableViewer viewer;
	private int columnIndex;

	public SelectUserEditingSupport(TableViewer viewer, int columnIndex) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {

		 UserDAO dao = (UserDAO) element;

		if (columnIndex == 0) {

			return new CheckboxCellEditor(null, SWT.CHECK);// | SWT.READ_ONLY);

		} else if (columnIndex == 3) {

			return new ComboBoxCellEditor(viewer.getTable(), userRoleNames);

		} else if (columnIndex == 4 || columnIndex == 5) {
			return new DialogCellEditor(viewer.getTable()) {
				@Override
		        protected Object openDialogBox(Control cellEditorWindow) {
		            Shell shell = Display.getDefault().getActiveShell();	
		            
		            Timestamp original = (Timestamp) SelectUserEditingSupport.this.getValue(element);
		            
		            TimeStampDialog dialog = new TimeStampDialog(shell, original, viewer.getTable().getColumn(columnIndex).getText());
		            if (IStatus.OK == dialog.open()) {
		                setValue(dialog.getValue());
		                return dialog.getValue();
		            }else{
		            	return original;
		            }
		        }
			};	

		} else {
			return null;
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		UserDAO dao = (UserDAO) element;

		if (columnIndex == 0 || columnIndex >= 3) {

			return true;

		} else {
			return false;
		}
	}

	@Override
	protected Object getValue(Object element) {
		UserDAO dao = (UserDAO) element;

		if (columnIndex == 0) {
			return dao.isSelect();
		}else if (columnIndex == 3) {
			for(int i=0; i <= userRoleNames.length; i++){
				if (userRoleNames[i].equals(dao.getRole_type())) return i;
			}
		}else if (columnIndex == 4) {
			return dao.getService_start();
		}else if (columnIndex == 5) {
			return dao.getService_end();
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		UserDAO dao = (UserDAO) element;
		if (columnIndex == 0) {
			dao.setSelect((Boolean) value);
		}else if (columnIndex == 3) {
			dao.setRole_type(userRoleNames[(int)value]);
		}else if (columnIndex == 4) {
			dao.setService_start((Timestamp) value);
		}else if (columnIndex == 5) {
			dao.setService_end((Timestamp) value);
		}

		viewer.update(element, null);
	}

}
