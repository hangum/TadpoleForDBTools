/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.dialogs.user;

import org.apache.log4j.Logger;
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

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * change password dialog
 * 
 * @author hangum
 *
 */
public class ChangePasswordDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ChangePasswordDialog.class);

	private Text textOldPassword;
	
	private Text textPassword;
	private Text textRePassword;


	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ChangePasswordDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CommonMessages.get().ChangePassword);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 2;
		
		Label lblOldPassword = new Label(container, SWT.NONE);
		lblOldPassword.setText(Messages.get().OldPassword);
		
		textOldPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textOldPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label labelSeparator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.get().Password);

		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.get().ConfirmPassword);

		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	@Override
	protected void okPressed() {
		String strPasswdComplexity = GetAdminPreference.getPasswdComplexity();
		int intLengthLimit = Integer.parseInt(GetAdminPreference.getPasswdLengthLimit());

		try {
			TadpoleSystem_UserQuery.login(SessionManager.getEMAIL(), textOldPassword.getText());
		} catch(Exception e) {
			textOldPassword.setFocus();
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, String.format(CommonMessages.get().IsIncorrect, Messages.get().OldPassword));
			return;
		}
		
		String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().Password, intLengthLimit, "30");
		
		String strPasswd 	= textPassword.getText();
		String strRePasswd 	= textRePassword.getText();
		
		if("".equals(strPasswd)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, Messages.get().EnterYourPasswd);
			textPassword.setFocus();
			return;
		} else if (!ValidChecker.isPasswordLengthChecker(intLengthLimit, strPasswd)) {
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning,
					String.format(CommonMessages.get().ValueIsLessThanOrOverThan, Messages.get().Password, intLengthLimit, "30")); //$NON-NLS-1$
			textPassword.setFocus();
			return;
		} else if (!strPasswd.equals(strRePasswd)) {
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, Messages.get().PasswordDoNotMatch);
			textPassword.setFocus();
			return;
		} else if ("YES".equals(strPasswdComplexity)) {
			if (!ValidChecker.isPasswordChecker(strPasswd)) {
				MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, Messages.get().inValidComplextyPasswd);
				textPassword.setFocus();
				return;
			}
		}
		
		if(MessageDialog.openConfirm(getShell(), CommonMessages.get().Confirm, CommonMessages.get().doYouWantTosave)) {
			UserDAO userDAO = new UserDAO();
			userDAO.setSeq(SessionManager.getUserSeq());
			userDAO.setPasswd(textPassword.getText()); 
		
			try {
				TadpoleSystem_UserQuery.updateUserPassword(userDAO);
				MessageDialog.openInformation(null, CommonMessages.get().Confirm, Messages.get().ChangedPassword);
			} catch(Exception e) {
				logger.error("Changing password", e); //$NON-NLS-1$
				MessageDialog.openError(getShell(),CommonMessages.get().Error, e.getMessage());			 //$NON-NLS-1$
			}	
		}

		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Save, true);
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(350, 200);
	}

}
