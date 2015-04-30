/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.auth.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;

/**
 * table column filter dialog
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 29.
 *
 */
public class TableColumnFilterDialog extends Dialog {
	private DBAccessControlDAO dbAccessDetail;
	private Text textTableName;
	private Text textColumnNames;
	
	private AccessCtlObjectDAO returnObjectDao = new AccessCtlObjectDAO();

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TableColumnFilterDialog(Shell parentShell, DBAccessControlDAO dbAccessDetail) {
		super(parentShell);
		
		this.dbAccessDetail = dbAccessDetail;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText("Table, Column Filter dialog"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblTableName = new Label(container, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText("Table Name");
		
		textTableName = new Text(container, SWT.BORDER);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblColumnName = new Label(container, SWT.NONE);
		lblColumnName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblColumnName.setText("Column Name");
		
		textColumnNames = new Text(container, SWT.BORDER | SWT.MULTI);
		textColumnNames.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return container;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if("".equals(textTableName.getText())) {
			MessageDialog.openError(getShell(), "Error", "Check table name.");
			textTableName.setFocus();
			return;
		}
		
		if("".equals(textColumnNames.getText())) {
			MessageDialog.openError(getShell(), "Error", "Check column name.");
			textColumnNames.setFocus();
			return;
		}
		
		// later check real table name
		
		// 
		
		returnObjectDao.setType("SELECT");
		returnObjectDao.setAccess_seq(dbAccessDetail.getSeq());
		returnObjectDao.setObj_name(textTableName.getText());
		returnObjectDao.setDetail_obj(textColumnNames.getText());
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	/**
	 *  return object access control dao
	 */
	public AccessCtlObjectDAO getUpdateData() {
		return returnObjectDao;
	}

}
