/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.dialogs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.mails.SendEmails;
import com.hangum.tadpold.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.get.GetAdminPreference;

/**
 * 모든 사용자에게 메시지를 보냅니다.
 * 
 * @author hangum
 *
 */
public class SendMessageDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SendMessageDialog.class);
	
	private Combo comboType;
	private Text textMessage;
	private Text textTitle;
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SendMessageDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Send Email");
	}

	@Override
	protected int getShellStyle() {
		int ret = super.getShellStyle(); 
		return ret | SWT.RESIZE;
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblType = new Label(compositeHead, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type");
		
		comboType = new Combo(compositeHead, SWT.NONE);
		comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboType.add("Send email all users");
		comboType.select(0);
		
		Composite compositeBody = new Composite(container, SWT.BORDER);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblTitle = new Label(compositeBody, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeBody, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMessage = new Label(compositeBody, SWT.NONE);
		lblMessage.setText("Message");
		
		textMessage = new Text(compositeBody, SWT.BORDER | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textMessage.setText("<pre>\n\n</pre>");
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	@Override
	protected void okPressed() {

		if(StringUtils.isEmpty(textTitle.getText())) {
			MessageDialog.openError(null, "Error", "Title is null.");
			textTitle.setFocus();
			return;
		}
		if(StringUtils.isEmpty(textMessage.getText())) {
			MessageDialog.openError(null, "Error", "Message is null.");
			textMessage.setFocus();
			return;
		}
		
		// 모든 사용자에게 이메일을 보냅니다.
		try {
			List<UserDAO> listUser = TadpoleSystem_UserQuery.getAllUser();
			SendEmails email = new SendEmails(GetAdminPreference.getSMTPINFO());
			for (UserDAO userDAO : listUser) {
				logger.info("admin user sender " + userDAO.getEmail());
				
				try {
					EmailDTO emailDto = new EmailDTO();
					emailDto.setSubject(textTitle.getText());
					emailDto.setContent(textMessage.getText());
					emailDto.setTo(userDAO.getEmail());
					
					email.sendMail(emailDto);
				} catch(Exception e) {
					logger.error("user sender", e);
				}
			}
			
			MessageDialog.openInformation(null, "Confirm", "email send has completed.");
		} catch (Exception e) {
			logger.error("get user data", e);
		}
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Send", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(441, 480);
	}
}
