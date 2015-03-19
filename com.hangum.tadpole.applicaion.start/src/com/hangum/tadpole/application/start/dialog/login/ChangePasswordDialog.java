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
package com.hangum.tadpole.application.start.dialog.login;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
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

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.help.core.views.HelpViewPart;
import com.swtdesigner.ResourceManager;

/**
 * 
 * @author billygoo
 *
 */
public class ChangePasswordDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ChangePasswordDialog.class);

	private UserDAO user;
	private Text textPassword;
	private Text textRePassword;

	protected ChangePasswordDialog(Shell parentShell, UserDAO user) {
		super(parentShell);
		if (null == user) {
			throw new IllegalArgumentException("UserDao should not be set to null."); //$NON-NLS-1$
		}
		this.user = user;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.ChangePasswordDialog_1);

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setImage(ResourceManager.getPluginImage(BrowserActivator.APPLICTION_ID, "resources/icons/forgot-password.png")); //$NON-NLS-1$ //$NON-NLS-2$
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 128;
		gd_lblNewLabel.heightHint = 128;
		lblNewLabel.setLayoutData(gd_lblNewLabel);

		Label lblDescription = new Label(container, SWT.WRAP);
		GridData gd_lblDescription = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		lblDescription.setLayoutData(gd_lblDescription);
		lblDescription.setText(Messages.ChangePasswordDialog_2);

		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText(Messages.ChangePasswordDialog_3);

		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePassword.setText(Messages.ChangePasswordDialog_4);

		textRePassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	@Override
	protected void okPressed() {
		String pass = textPassword.getText().trim();
		String rePass = textRePassword.getText().trim();

		if (!pass.equals(rePass)) {
			MessageDialog.openError(getShell(), Messages.ChangePasswordDialog_5, Messages.ChangePasswordDialog_6);
			return;
		}
		if ("".equals(pass)) { //$NON-NLS-1$
			MessageDialog.openError(getShell(), Messages.ChangePasswordDialog_8, Messages.ChangePasswordDialog_9);
			return;
		}

		user.setPasswd(pass);

		try {
			TadpoleSystem_UserQuery.updateUserPassword(user);
		} catch (Exception e) {
			logger.error("password change", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), "Confirm", e.getMessage()); //$NON-NLS-1$
			return;
		}
		super.okPressed();
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(350, 300);
	}

}
