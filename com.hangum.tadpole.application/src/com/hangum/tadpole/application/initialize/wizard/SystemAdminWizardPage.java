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
package com.hangum.tadpole.application.initialize.wizard;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.Messages;

/**
 *
 * System Adminitstrator wizard
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class SystemAdminWizardPage extends WizardPage {
	private static final Logger logger = Logger.getLogger(SystemAdminWizardPage.class);
	
	private Text textEmail;
	private Text textPasswd;
	private Text textRePasswd;
	private Text textSMTPServer;
	private Text textPort;
	private Text textSMTPEmail;
	private Text textSMTPPasswd;
	
	private static final String PASSWORD_RULE_PATTERN = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{8,}$";
	
	/**
	 * Create the wizard.
	 */
	public SystemAdminWizardPage() {
		super("SystemInitializeWizard"); //$NON-NLS-1$
		setTitle(Messages.SystemAdminWizardPage_1);
		setDescription(Messages.SystemAdminWizardPage_2);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		container.setLayout(new GridLayout(1, false));
		
		Group grpAdministratorUserInformation = new Group(container, SWT.NONE);
		grpAdministratorUserInformation.setLayout(new GridLayout(2, false));
		grpAdministratorUserInformation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpAdministratorUserInformation.setText(Messages.SystemAdminWizardPage_3);
		
		Label lblEmail = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblEmail.setAlignment(SWT.RIGHT);
		GridData gd_lblEmail = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblEmail.widthHint = 120;
		lblEmail.setLayoutData(gd_lblEmail);
		lblEmail.setText(Messages.SystemAdminWizardPage_4);
		
		textEmail = new Text(grpAdministratorUserInformation, SWT.BORDER);
		textEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) validatePage(textEmail);
			}
		});
		
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswd = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblPasswd.setAlignment(SWT.RIGHT);
		lblPasswd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPasswd.setText(Messages.SystemAdminWizardPage_5);
		
		textPasswd = new Text(grpAdministratorUserInformation, SWT.BORDER | SWT.PASSWORD);
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) validatePage(textPasswd);
			}
		});
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePasswd = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblRePasswd.setAlignment(SWT.RIGHT);
		lblRePasswd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePasswd.setText(Messages.SystemAdminWizardPage_6);
		
		textRePasswd = new Text(grpAdministratorUserInformation, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) validatePage(textRePasswd);
			}
		});
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpSystemSmtp = new Group(container, SWT.NONE);
		grpSystemSmtp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSystemSmtp.setText(Messages.SystemAdminWizardPage_7);
		grpSystemSmtp.setLayout(new GridLayout(2, false));
		
		Label lblSmtpServer = new Label(grpSystemSmtp, SWT.NONE);
		lblSmtpServer.setAlignment(SWT.RIGHT);
		GridData gd_lblSmtpServer = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblSmtpServer.widthHint = 120;
		lblSmtpServer.setLayoutData(gd_lblSmtpServer);
		lblSmtpServer.setText(Messages.SystemAdminWizardPage_8);
		
		textSMTPServer = new Text(grpSystemSmtp, SWT.BORDER);
		textSMTPServer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) validatePage(textSMTPServer);
			}
		});
		textSMTPServer.setText(Messages.SystemAdminWizardPage_9);
		textSMTPServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSMTPServer.setEditable(false);
		
		Label lblPort = new Label(grpSystemSmtp, SWT.NONE);
		lblPort.setAlignment(SWT.RIGHT);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText(Messages.SystemAdminWizardPage_10);
		
		textPort = new Text(grpSystemSmtp, SWT.BORDER);
		textPort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) validatePage(textPort);
			}
		});
		textPort.setText(Messages.SystemAdminWizardPage_11);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPort.setEditable(false);
		
		Label lblEmail_1 = new Label(grpSystemSmtp, SWT.NONE);
		lblEmail_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail_1.setAlignment(SWT.RIGHT);
		lblEmail_1.setText(Messages.SystemAdminWizardPage_12);
		
		textSMTPEmail = new Text(grpSystemSmtp, SWT.BORDER);
		textSMTPEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) validatePage(textPort);
			}
		});
		textSMTPEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswd_1 = new Label(grpSystemSmtp, SWT.NONE);
		lblPasswd_1.setAlignment(SWT.RIGHT);
		lblPasswd_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPasswd_1.setText(Messages.SystemAdminWizardPage_13);
		
		textSMTPPasswd = new Text(grpSystemSmtp, SWT.BORDER | SWT.PASSWORD);
		textSMTPPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) 
				validatePage(textSMTPPasswd);
			}
		});
		textSMTPPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpSystemSmtp, SWT.NONE);
		
		Button btnTestSendEmail = new Button(grpSystemSmtp, SWT.NONE);
		GridData gd_btnTestSendEmail = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnTestSendEmail.widthHint = 200;
		btnTestSendEmail.setLayoutData(gd_btnTestSendEmail);
		btnTestSendEmail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if("".equals(textSMTPServer.getText())) { //$NON-NLS-1$
					textSMTPServer.setFocus();
					MessageDialog.openError(null, Messages.SystemAdminWizardPage_15, Messages.SystemAdminWizardPage_16);
					return;
				} else if("".equals(textPort.getText())) { //$NON-NLS-1$
					textPort.setFocus();
					MessageDialog.openError(null, Messages.SystemAdminWizardPage_15, Messages.SystemAdminWizardPage_19);
					return;
				} else if("".equals(textSMTPEmail.getText())) { //$NON-NLS-1$
					textSMTPEmail.setFocus();
					MessageDialog.openError(null, Messages.SystemAdminWizardPage_15, Messages.SystemAdminWizardPage_22);
					return;
				} else if("".equals(textSMTPPasswd.getText())) { //$NON-NLS-1$
					textSMTPPasswd.setFocus();
					MessageDialog.openError(null, Messages.SystemAdminWizardPage_15, Messages.SystemAdminWizardPage_25);
					return;
				}
			
				// Test send smtp mail
				SMTPDTO smtpDao = new SMTPDTO();
				smtpDao.setHost(textSMTPServer.getText());
				smtpDao.setPort(textPort.getText());
				smtpDao.setEmail(textSMTPEmail.getText());
				smtpDao.setPasswd(textSMTPPasswd.getText());
				
				SendEmails sendemail = new SendEmails(smtpDao);
				
				EmailDTO emailDto = new EmailDTO();
				emailDto.setTo(textSMTPEmail.getText());
				emailDto.setSubject(Messages.SystemAdminWizardPage_26);
				emailDto.setContent(Messages.SystemAdminWizardPage_27);
				
				try {
					sendemail.sendMail(emailDto);
					MessageDialog.openInformation(textSMTPPasswd.getShell(), Messages.SystemAdminWizardPage_28, Messages.SystemAdminWizardPage_29);
				} catch (Exception e1) {
					logger.error(Messages.SystemAdminWizardPage_30, e1);
					
					MessageDialog.openError(textSMTPPasswd.getShell(), Messages.SystemAdminWizardPage_15, Messages.SystemAdminWizardPage_32 + e1.getMessage());
				}
			}
		});
		btnTestSendEmail.setText(Messages.SystemAdminWizardPage_33);

		setControl(container);
		setPageComplete(false);
		
		AnalyticCaller.track("SystemAdminWizard"); //$NON-NLS-1$
		
		textEmail.setFocus();
	}
	
	/**
	 * validation ui
	 * 
	 */
	private void validatePage(Control controlWorker) {
		if("".equals(textEmail.getText())) { //$NON-NLS-1$
			errorSet(textEmail, Messages.SystemAdminWizardPage_35);
			return;
		} else if(!Utils.isEmail(textEmail.getText())) {
			errorSet(textEmail, Messages.SystemAdminWizardPage_48);
			return;
		} else if(!textPasswd.getText().matches(PASSWORD_RULE_PATTERN)) { //$NON-NLS-1$
			errorSet(textPasswd, Messages.SystemAdminWizardPage_37);
			return;
		} else if(!textPasswd.getText().equals(textRePasswd.getText())) { //$NON-NLS-1$
			errorSet(textRePasswd, Messages.SystemAdminWizardPage_39);
			return;
//		} else if(!textPasswd.getText().equals(textRePasswd.getText())) {
//			errorSet(textPasswd, Messages.SystemAdminWizardPage_49);
//			return;
//
//		} else if("".equals(textSMTPServer.getText())) { //$NON-NLS-1$
//			errorSet(textSMTPServer, Messages.SystemAdminWizardPage_41);
//			return;
//		} else if("".equals(textPort.getText())) { //$NON-NLS-1$
//			errorSet(textPort, Messages.SystemAdminWizardPage_43);
//			return;
//		} else if("".equals(textSMTPEmail.getText())) { //$NON-NLS-1$
//			errorSet(textSMTPEmail, Messages.SystemAdminWizardPage_45);
//			return;
//		} else if("".equals(textSMTPPasswd.getText())) {
//			errorSet(textSMTPPasswd, Messages.SystemAdminWizardPage_47);
//			return;
		}
		
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	private void errorSet(Control ctl, String msg) {
		ctl.setFocus();
		setErrorMessage(msg);
		setPageComplete(false);
	}
	
	/**
	 * get wizard dao
	 * 
	 * @return
	 */
	public SystemAdminWizardPageDAO getUserData() {
		SystemAdminWizardPageDAO dao = new SystemAdminWizardPageDAO();
		
		dao.setEmail(textEmail.getText());
		dao.setPasswd(textPasswd.getText());
		dao.setRePasswd(textRePasswd.getText());

		dao.setsMTPServer(textSMTPServer.getText());
		dao.setPort(textPort.getText());
		dao.setsMTPEmail(textSMTPEmail.getText());
		dao.setsMTPPasswd(textSMTPPasswd.getText());
		
		return dao;
	}
	
	
}
