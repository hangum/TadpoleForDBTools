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
package com.hangum.tadpole.engine.security;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * DB Lock Dialog
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 24.
 *
 */
public class DBLockDialog extends Dialog {
	private UserDBDAO userDB;
	private Text textPassword;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DBLockDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().DBLockDialog_0);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		
		Label lblDbPassword = new Label(container, SWT.NONE);
		lblDbPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDbPassword.setText(Messages.get().DBLockDialog_1);
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					okPressed();
				}
			}
		});
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		textPassword.setFocus();

		return container;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		String strPassword = textPassword.getText();
		
		if(!strPassword.equals(userDB.getPasswd())) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().DBLockDialog_3);
			textPassword.setFocus();
			
			return;
		}
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancle, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(402, 118);
	}

}
