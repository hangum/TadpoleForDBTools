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
package com.hangum.tadpole.engine.initialize.wizard;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.mails.SendEmails;
import com.hangum.tadpold.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpold.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.commons.util.Utils;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

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

	/**
	 * Create the wizard.
	 */
	public SystemAdminWizardPage() {
		super("SystemInitializeWizard");
		setTitle("Administrator Information");
		setDescription("Administrator Information");
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
		grpAdministratorUserInformation.setText("Administrator User Information");
		
		Label lblEmail = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblEmail.setText("email");
		
		textEmail = new Text(grpAdministratorUserInformation, SWT.BORDER);
		textEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) validatePage();
			}
		});
		
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswd = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblPasswd.setText("passwd");
		
		textPasswd = new Text(grpAdministratorUserInformation, SWT.BORDER | SWT.PASSWORD);
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) validatePage();
			}
		});
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePasswd = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblRePasswd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRePasswd.setText("re passwd");
		
		textRePasswd = new Text(grpAdministratorUserInformation, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) validatePage();
			}
		});
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpSystemSmtp = new Group(container, SWT.NONE);
		grpSystemSmtp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSystemSmtp.setText("System SMTP(Only Google Account)");
		grpSystemSmtp.setLayout(new GridLayout(2, false));
		
		Label lblSmtpServer = new Label(grpSystemSmtp, SWT.NONE);
		lblSmtpServer.setText("SMTP Server");
		
		textSMTPServer = new Text(grpSystemSmtp, SWT.BORDER);
		textSMTPServer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) validatePage();
			}
		});
		textSMTPServer.setText("smtp.googlemail.com");
		textSMTPServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(grpSystemSmtp, SWT.NONE);
		lblPort.setText("PORT");
		
		textPort = new Text(grpSystemSmtp, SWT.BORDER);
		textPort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) validatePage();
			}
		});
		textPort.setText("465");
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblEmail_1 = new Label(grpSystemSmtp, SWT.NONE);
		lblEmail_1.setText("email");
		
		textSMTPEmail = new Text(grpSystemSmtp, SWT.BORDER);
		textSMTPEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) validatePage();
			}
		});
		textSMTPEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswd_1 = new Label(grpSystemSmtp, SWT.NONE);
		lblPasswd_1.setText("passwd");
		
		textSMTPPasswd = new Text(grpSystemSmtp, SWT.BORDER | SWT.PASSWORD);
		textSMTPPasswd.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				validatePage();
			}
		});
		textSMTPPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpSystemSmtp, SWT.NONE);
		
		Button btnTestSendEmail = new Button(grpSystemSmtp, SWT.NONE);
		btnTestSendEmail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if("".equals(textSMTPServer.getText())) {
					textSMTPServer.setFocus();
					MessageDialog.openError(null, "Error", "SMTP Server is empty.");
					return;
				} else if("".equals(textPort.getText())) {
					textPort.setFocus();
					MessageDialog.openError(null, "Error", "Port is empty.");
					return;
				} else if("".equals(textSMTPEmail.getText())) {
					textSMTPEmail.setFocus();
					MessageDialog.openError(null, "Error", "SMTP Email is empty.");
					return;
				} else if("".equals(textSMTPPasswd.getText())) {
					textSMTPPasswd.setFocus();
					MessageDialog.openError(null, "Error", "SMTP password is empty.");
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
				emailDto.setSubject("Tadpole DB Hub email test.");
				emailDto.setContent("Tadpole DB Hub administrator email test.");
				
				try {
					sendemail.sendMail(emailDto);
					MessageDialog.openInformation(textSMTPPasswd.getShell(), "Cofirm", "Success email send. Check your email box.");
				} catch (Exception e1) {
					logger.error("Send email test", e1);
					
					MessageDialog.openError(textSMTPPasswd.getShell(), "Error", "Send email test fail.\n" + e1.getMessage());
				}
			}
		});
		btnTestSendEmail.setText("Test send email");

		setControl(container);
		setPageComplete(false);
		
		textEmail.setFocus();
	}
	
	/**
	 * validation ui
	 * 
	 */
	private void validatePage() {
		if("".equals(textEmail.getText())) {
			textEmail.setFocus();
			setErrorMessage("User email is empty.");
			return;
		} else if("".equals(textPasswd.getText())) {
			textPasswd.setFocus();
			setErrorMessage("User password is empty.");
			return;
		} else if("".equals(textRePasswd.getText())) {
			textRePasswd.setFocus();
			setErrorMessage("User re password is empty.");
			return;
		} else if("".equals(textSMTPServer.getText())) {
			textSMTPServer.setFocus();
			setErrorMessage("SMTP Server is empty.");
			return;
		} else if("".equals(textPort.getText())) {
			textPort.setFocus();
			setErrorMessage("Port is empty.");
			return;
		} else if("".equals(textSMTPEmail.getText())) {
			textSMTPEmail.setFocus();
			setErrorMessage("SMTP Email is empty.");
			return;
		} else if("".equals(textSMTPPasswd.getText())) {
			textSMTPPasswd.setFocus();
			setErrorMessage("SMTP password is empty.");
			return;
		} else if(!Utils.isEmail(textEmail.getText())) {
			textEmail.setFocus();
			setErrorMessage("User email format check.");
			return;
		} else if(!textPasswd.getText().equals(textRePasswd.getText())) {
			textPasswd.setFocus();
			setErrorMessage("User password not is corrent.");
			return;
		}
		
		setErrorMessage(null);
		setPageComplete(true);
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
