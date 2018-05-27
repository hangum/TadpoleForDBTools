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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.otp.core.GetOTPCode;
import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * DB Lock Dialog
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 24.
 *
 */
public class DBPasswordAndOTPDialog extends PasswordDialog {
	private static final Logger logger = Logger.getLogger(DBPasswordAndOTPDialog.class);
	private Text textOTP;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DBPasswordAndOTPDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell, userDB);
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
		lblDbPassword.setText(CommonMessages.get().Password);
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					textOTP.setFocus();
				}
			}
		});
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblOtp = new Label(container, SWT.NONE);
		lblOtp.setText("OTP Code");
		
		textOTP = new Text(container, SWT.BORDER);
		textOTP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textOTP.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					okPressed();
				}
			}
		});
		
		initUI();
		textOTP.setFocus();

		return container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		String strOTPCode = textOTP.getText();
		
		if("".equals(strOTPCode)) {
			MessageDialog.openError(getShell(), CommonMessages.get().Error, Messages.get().OTPEmpty);
			textOTP.setFocus();
			return;
		}
		try {
			GetOTPCode.isValidate(SessionManager.getEMAIL(), SessionManager.getOTPSecretKey(), strOTPCode);
		} catch(Exception e) {
			logger.error("OTP check", e);
			MessageDialog.openError(getShell(), CommonMessages.get().Error, e.getMessage());
			textOTP.setFocus();
			
			return;
		}
		
		// 실제 접속 되는지 테스트해봅니다.
		try {
			userDB.setPasswd(StringUtils.trim(textPassword.getText()));
			TadpoleSQLManager.getInstance(userDB);
		} catch(Exception e) {
			logger.error("Test Passwd+opt Connection error ");
			
			String msg = e.getMessage();
			if(StringUtils.contains(msg, "No more data to read from socket")) {
				MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, msg + CommonMessages.get().Check_DBAccessSystem);
			} else {
				MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, msg);
			}
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
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Confirm, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  CommonMessages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(402, 148);
	}

}
