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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.application.initialize.Messages;
import com.hangum.tadpole.application.initialize.wizard.dao.SystemAdminWizardUserDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;

/**
 *
 * System Adminitstrator wizard
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class SystemAdminWizardDefaultUserPage extends WizardPage {
	private static final Logger logger = Logger.getLogger(SystemAdminWizardDefaultUserPage.class);
	
	private boolean isComplete = false;
	private Text textEmail;
	private Text textPasswd;
	private Text textRePasswd;
	
	/**
	 * Create the wizard.
	 */
	public SystemAdminWizardDefaultUserPage() {
		super("SystemInitializeWizard"); //$NON-NLS-1$
		setTitle(Messages.get().SystemAdminWizardPage_1);
		setDescription(Messages.get().SystemAdminWizardPage_2);
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
		grpAdministratorUserInformation.setText(Messages.get().SystemAdminWizardPage_1);
		
		Label lblEmail = new Label(grpAdministratorUserInformation, SWT.NONE);
		GridData gd_lblEmail = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblEmail.widthHint = 120;
		lblEmail.setLayoutData(gd_lblEmail);
		lblEmail.setText(CommonMessages.get().Email);
		
		textEmail = new Text(grpAdministratorUserInformation, SWT.BORDER);
		textEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.TAB) {
					String strEmail =  textEmail.getText() + e.character;
					if("".equals(strEmail)){//!ValidChecker.isValidEmailAddress(strEmail)) {
						errorSet(textEmail, Messages.get().SystemAdminWizardPage_35);
					} else {
						setErrorMessage(null);
					}
				}
			}
		});
		
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPasswd = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblPasswd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblPasswd.setText(CommonMessages.get().Password);
		
		textPasswd = new Text(grpAdministratorUserInformation, SWT.BORDER | SWT.PASSWORD);
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection | e.keyCode == SWT.TAB) {
					String strEmail =  textEmail.getText();
					String strPass = textPasswd.getText() + e.character;
					String strRePass = textRePasswd.getText();
					
					validateValue(strEmail, strPass, strRePass);
				}
			}
		});
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePasswd = new Label(grpAdministratorUserInformation, SWT.NONE);
		lblRePasswd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblRePasswd.setText(Messages.get().SystemAdminWizardPage_6);
		
		textRePasswd = new Text(grpAdministratorUserInformation, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String strEmail =  textEmail.getText();
				String strPass = textPasswd.getText();
				String strRePass = textRePasswd.getText() + e.character;
				
				validateValue(strEmail, strPass, strRePass);
			}
		});
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		setControl(container);
		setPageComplete(false);
		
		AnalyticCaller.track("SystemAdminWizardAddUserPage"); //$NON-NLS-1$
		
		textEmail.setFocus();
	}
	
	/**
	 * validate 
	 * 
	 * @param strEmail
	 * @param strPass
	 * @param strRePass
	 */
	private void validateValue(String strEmail, String strPass, String strRePass) {
		strEmail = StringUtils.trimToEmpty(strEmail);
		strPass = StringUtils.trimToEmpty(strPass);
		strRePass = StringUtils.trimToEmpty(strRePass);
		
		strPass = StringUtils.removeEnd(strPass, "\t");
		strPass = StringUtils.removeEnd(strPass, "\n");
		
		strRePass = StringUtils.removeEnd(strRePass, "\t");
		strRePass = StringUtils.removeEnd(strRePass, "\n");
		
		isComplete = false;
		if("".equals(strEmail)) { //$NON-NLS-1$
			errorSet(textEmail, Messages.get().SystemAdminWizardPage_35);
			return;
//		} else if(!ValidChecker.isValidEmailAddress(strEmail)) {
//			errorSet(textEmail, Messages.get().SystemAdminWizardPage_48);
//			return;
		} else if(!ValidChecker.isPasswordLengthChecker(7, strPass)) { //$NON-NLS-1$
			errorSet(textPasswd, Messages.get().SystemAdminWizardPage_37);
			return;
		} else if(!strPass.equals(strRePass)) { //$NON-NLS-1$
			errorSet(textRePasswd, Messages.get().SystemAdminWizardPage_39);
			return;
		}
		
		isComplete = true;
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	private void errorSet(Control ctl, String msg) {
		ctl.setFocus();
		setErrorMessage(msg);
		setPageComplete(false);
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	/**
	 * get wizard dao
	 * 
	 * @return
	 */
	public SystemAdminWizardUserDAO getUserData() {
		SystemAdminWizardUserDAO dao = new SystemAdminWizardUserDAO();
		
		dao.setEmail(textEmail.getText());
		dao.setPasswd(textPasswd.getText());
		dao.setRePasswd(textRePasswd.getText());
		
		return dao;
	}
}
