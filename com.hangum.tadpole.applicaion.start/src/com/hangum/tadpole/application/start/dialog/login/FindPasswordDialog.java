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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.libs.core.mails.template.TemporaryPasswordMailBodyTemplate;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.define.GetAdminPreference;

/**
 * find password
 * 
 * @author hangum
 *
 */
public class FindPasswordDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(FindPasswordDialog.class);
	private String strEmail;
	private Text textEmail;

	public FindPasswordDialog(Shell parentShell, String strEmail) {
		super(parentShell);
		this.strEmail = strEmail;
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().FindPassword);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.get().FindPasswordDialog_0);
		
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setText(Messages.get().LoginDialog_1);
		
		textEmail = new Text(container, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEmail.setText(strEmail);
		
		textEmail.setFocus();
		
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
		if(logger.isInfoEnabled()) logger.info("Find password dialog" + strEmail);

		if (!checkValidation()) {
			MessageDialog.openWarning(getShell(), Messages.get().Confirm, Messages.get().FindPasswordDialog_6);
			textEmail.setFocus();
			return;
		}
		
		UserDAO userDao = new UserDAO();
		userDao.setEmail(strEmail);
		String strTmpPassword = Utils.getUniqueDigit(12);
		userDao.setPasswd(strTmpPassword);
		
		try {
			TadpoleSystem_UserQuery.updateUserPasswordWithID(userDao);
			sendEmailAccessKey(strEmail, strTmpPassword);
			MessageDialog.openInformation(getShell(), Messages.get().Confirm, Messages.get().SendMsg);
		} catch (Exception e) {
			logger.error("password initialize and send email ", e);
			
			MessageDialog.openError(getShell(), Messages.get().Error, 
					String.format(Messages.get().SendMsgErr, e.getMessage()));
		}
		
		
		super.okPressed();
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().SEND, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}
	
	/**
	 * send password
	 * 
	 * @param email
	 * @param strConfirmKey
	 */
	private void sendEmailAccessKey(String email, String strConfirmKey) throws Exception {

		// manager 에게 메일을 보낸다.
		EmailDTO emailDao = new EmailDTO();
		emailDao.setSubject(Messages.get().TemporayPassword); //$NON-NLS-1$
		// 
		// 그룹, 사용자, 권한.
		// 
		TemporaryPasswordMailBodyTemplate mailContent = new TemporaryPasswordMailBodyTemplate();
		String strContent = mailContent.getContent(email, strConfirmKey);
		emailDao.setContent(strContent);
		emailDao.setTo(email);
		
		SendEmails sendEmail = new SendEmails(GetAdminPreference.getSessionSMTPINFO());
		sendEmail.sendMail(emailDao);

	}
}
