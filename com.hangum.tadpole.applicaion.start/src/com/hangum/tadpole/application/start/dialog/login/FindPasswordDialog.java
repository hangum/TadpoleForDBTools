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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.SecurityHint;
import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserQuery;
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
	private Text textAnswer;
	private Combo comboQuestion;

	public FindPasswordDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.FindPasswordDialog_0);

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/icons/forgot-password.png")); //$NON-NLS-1$ //$NON-NLS-2$
		lblNewLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 128;
		gd_lblNewLabel.heightHint = 128;
		lblNewLabel.setLayoutData(gd_lblNewLabel);

		Label lblDescription = new Label(container, SWT.WRAP);
		GridData gd_lblDescription = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblDescription.heightHint = 63;
		lblDescription.setLayoutData(gd_lblDescription);
		lblDescription.setText(Messages.FindPasswordDialog_2);

		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText(Messages.FindPasswordDialog_3);

		textEmail = new Text(container, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblQuestion = new Label(container, SWT.NONE);
		lblQuestion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQuestion.setText(Messages.FindPasswordDialog_4);

		comboQuestion = new Combo(container, SWT.READ_ONLY);
		comboQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (SecurityHint q : PublicTadpoleDefine.SecurityHint.values()) {
			comboQuestion.add(q.toString(), q.getOrderIndex());
			comboQuestion.setData(q.toString(), q.getKey());
		}
		comboQuestion.select(0);

		Label lblAnswer = new Label(container, SWT.NONE);
		lblAnswer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAnswer.setText(Messages.FindPasswordDialog_5);

		textAnswer = new Text(container, SWT.BORDER);
		textAnswer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	private boolean checkValidation() {
		return !"".equals(textEmail.getText().trim()) && !"".equals(textAnswer.getText().trim()) && comboQuestion.getSelectionIndex() != -1 && comboQuestion.getSelectionIndex() != 0; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEmail.getText());
		String strQuestion = StringUtils.trimToEmpty((String) comboQuestion.getData(comboQuestion.getText()));
		String strAnswer = StringUtils.trimToEmpty(textAnswer.getText());

		if (!checkValidation()) {
			MessageDialog.openWarning(getShell(), Messages.FindPasswordDialog_1, Messages.FindPasswordDialog_6);
			return;
		}
		UserDAO validUser;
		try {
			validUser = TadpoleSystem_UserQuery.checkSecurityHint(strEmail, strQuestion, strAnswer);
		} catch (Exception e) {
			logger.error("Find password exception", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), Messages.LoginDialog_7, e.getMessage());
			return;
		}
		super.okPressed();
		ChangePasswordDialog dialog = new ChangePasswordDialog(getParentShell(), validUser);
		dialog.open();
	}

}
