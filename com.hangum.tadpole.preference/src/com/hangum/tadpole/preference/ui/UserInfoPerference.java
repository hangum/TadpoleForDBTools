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
package com.hangum.tadpole.preference.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserQuery;

/**
 * 사용자 정보 수정
 * 
 * @author hangum
 *
 */
public class UserInfoPerference extends PreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(UserInfoPerference.class);
	
//	private Text textGroupName;
	private Text textEmail;
	private Text textPassword;
	private Text textRePassword;
	private Text textName;

	/**
	 * Create the preference page.
	 */
	public UserInfoPerference() {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
//		Label lblGroupName = new Label(container, SWT.NONE);
//		lblGroupName.setText(Messages.UserInfoPerference_1);
//		
//		textGroupName = new Text(container, SWT.BORDER);
//		textGroupName.setEnabled(false);
//		textGroupName.setEditable(false);
//		textGroupName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		textGroupName.setText(SessionManager.getGroupName());
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setText(Messages.UserInfoPerference_2);
		
		textEmail = new Text(container, SWT.BORDER);
		textEmail.setEnabled(false);
		textEmail.setEditable(false);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEmail.setText(SessionManager.getEMAIL());
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.UserInfoPerference_3);
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPassword.setText(SessionManager.getPassword());
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.UserInfoPerference_4);
		
		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textRePassword.setText(SessionManager.getPassword());
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(Messages.UserInfoPerference_5);
		
		textName = new Text(container, SWT.BORDER);
		textName.setEnabled(false);
		textName.setEditable(false);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textName.setText(SessionManager.getName());
		
		return container;
	}

	@Override
	public boolean performOk() {
		String pass = textPassword.getText().trim();
		String rePass = textRePassword.getText().trim();
		
		if(!pass.equals(rePass)) {
			MessageDialog.openError(getShell(), Messages.UserInfoPerference_0, Messages.UserInfoPerference_6);
			return false;
		}
		
		UserDAO user = new UserDAO();
		user.setSeq(SessionManager.getSeq());
		user.setPasswd(pass);		
		try {
			TadpoleSystem_UserQuery.updateUserPassword(user);
			
			//fix https://github.com/hangum/TadpoleForDBTools/issues/243
			SessionManager.setPassword(user.getPasswd());
		} catch (Exception e) {
			logger.error("password change", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Confirm", e.getMessage());			 //$NON-NLS-1$
			
			return false;
		}
		
		return super.performOk();
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
