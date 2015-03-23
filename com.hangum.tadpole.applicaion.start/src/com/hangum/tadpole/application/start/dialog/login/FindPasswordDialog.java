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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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
import com.swtdesigner.ResourceManager;

/**
 * 
 * 
 * @author billygoo
 *
 */
public class FindPasswordDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(FindPasswordDialog.class);
	
	private Text textEmail;

	public FindPasswordDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Fogot password");
		newShell.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/Tadpole15-15.png")); //$NON-NLS-1$
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.FindPasswordDialog_0);
		
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setText(Messages.FindPasswordDialog_3);

		textEmail = new Text(container, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}

	private boolean checkValidation() {
		return !"".equals(textEmail.getText().trim()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEmail.getText());
		logger.info("Fogot password dialog" + strEmail);

		if (!checkValidation()) {
			MessageDialog.openWarning(getShell(), Messages.FindPasswordDialog_1, Messages.FindPasswordDialog_6);
			textEmail.setFocus();
			return;
		}
//		UserDAO validUser;
//		try {
//			validUser = TadpoleSystem_UserQuery.checkSecurityHint(strEmail, strQuestion, strAnswer);
//		} catch (Exception e) {
//			logger.error("Find password exception", e); //$NON-NLS-1$
//			MessageDialog.openError(getShell(), Messages.LoginDialog_7, e.getMessage());
//			return;
//		}
		super.okPressed();
	}
}
