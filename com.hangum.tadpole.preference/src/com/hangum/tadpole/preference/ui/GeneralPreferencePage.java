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
package com.hangum.tadpole.preference.ui;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserInfoData;

/**
 * general preference
 * 
 * @author hangum
 *
 */
public class GeneralPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = Logger.getLogger(GeneralPreferencePage.class);
	
	private Text textSessionTime;
	private Text textExportDelimit;
	private Text textHomePage;
	private Button btnCheckButtonHomepage;
	private Group grpEmailAccount;
	private Text textSMTP;
	private Text textPort;
	private Text textEmail;
	private Text textPasswd;

	public GeneralPreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.DefaultPreferencePage_2);
		
		textSessionTime = new Text(container, SWT.BORDER);
		textSessionTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblExportDilimit = new Label(container, SWT.NONE);
		lblExportDilimit.setText(Messages.GeneralPreferencePage_lblExportDilimit_text);
		
		textExportDelimit = new Text(container, SWT.BORDER);
		textExportDelimit.setText(Messages.GeneralPreferencePage_text_text);
		textExportDelimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblHomePage = new Label(container, SWT.NONE);
		lblHomePage.setText(Messages.GeneralPreferencePage_lblHomePage_text);
		
		textHomePage = new Text(container, SWT.BORDER);
		textHomePage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		btnCheckButtonHomepage = new Button(container, SWT.CHECK);
		btnCheckButtonHomepage.setText(Messages.GeneralPreferencePage_btnCheckButton_text);
		btnCheckButtonHomepage.setSelection(true);
		
		grpEmailAccount = new Group(container, SWT.NONE);
		grpEmailAccount.setVisible(false);
		grpEmailAccount.setLayout(new GridLayout(2, false));
		grpEmailAccount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpEmailAccount.setText(Messages.GeneralPreferencePage_grpEmailAccount_text);
		
		Label lblSmtpServer = new Label(grpEmailAccount, SWT.NONE);
		lblSmtpServer.setText(Messages.GeneralPreferencePage_lblSmtpServer_text);
		
		textSMTP = new Text(grpEmailAccount, SWT.BORDER);
		textSMTP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(grpEmailAccount, SWT.NONE);
		lblPort.setText(Messages.GeneralPreferencePage_lblPort_text);
		
		textPort = new Text(grpEmailAccount, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAccount = new Label(grpEmailAccount, SWT.NONE);
		lblAccount.setText(Messages.GeneralPreferencePage_lblAccount_text);
		
		textEmail = new Text(grpEmailAccount, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(grpEmailAccount, SWT.NONE);
		lblPassword.setText(Messages.GeneralPreferencePage_lblPassword_text);
		
		textPasswd = new Text(grpEmailAccount, SWT.BORDER | SWT.PASSWORD);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		if(SessionManager.isAdmin()) {
			grpEmailAccount.setVisible(true);
		}
		
		initDefaultValue();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	@Override
	public boolean performOk() {
		String txtSessionTime 	= textSessionTime.getText();
		String txtExportDelimit = textExportDelimit.getText();
		String txtHomePage 		= textHomePage.getText();
		String txtHomePageUse 	= ""+btnCheckButtonHomepage.getSelection();
		
		String txtSmtp 			= textSMTP.getText();
		String txtPort			= textPort.getText();
		String txtEmail			= textEmail.getText();
		String txtPasswd		= textPasswd.getText();
		
		if(!NumberUtils.isNumber(txtSessionTime)) {
			textSessionTime.setFocus();
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_2 + Messages.GeneralPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		if(!NumberUtils.isNumber(txtPort)) {
			textPort.setFocus();
			MessageDialog.openError(getShell(), "Confirm", "Port is " + Messages.GeneralPreferencePage_0);			 //$NON-NLS-1$
			return false;
		}
		
		// 테이블에 저장 
		try {			
			updateInfo(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, txtSessionTime);
			updateInfo(PreferenceDefine.EXPORT_DILIMITER, 			txtExportDelimit);
			updateInfo(PreferenceDefine.DEFAULT_HOME_PAGE, 			txtHomePage);
			updateInfo(PreferenceDefine.DEFAULT_HOME_PAGE_USE, 		txtHomePageUse);
			
			updateInfo(PreferenceDefine.SMTP_HOST_NAME, txtSmtp);
			updateInfo(PreferenceDefine.SMTP_PORT, 		txtPort);
			updateInfo(PreferenceDefine.SMTP_EMAIL, 	txtEmail);
			updateInfo(PreferenceDefine.SMTP_PASSWD, 	txtPasswd);
			
		} catch(Exception e) {
			logger.error("GeneralPreference saveing", e);
			
			MessageDialog.openError(getShell(), "Confirm", Messages.GeneralPreferencePage_2 + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		
		return super.performOk();
	}
	
	private void updateInfo(String key, String value) throws Exception {
		TadpoleSystem_UserInfoData.updateValue(key, value);
		SessionManager.setUserInfo(key, value);
	}
	
	@Override
	public boolean performCancel() {
		initDefaultValue();
		
		return super.performCancel();
	}
	
	@Override
	protected void performApply() {

		super.performApply();
	}
	
	@Override
	protected void performDefaults() {
		initDefaultValue();

		super.performDefaults();
	}
	
	/**
	 * 페이지 초기값 로딩 
	 */
	private void initDefaultValue() {
		textSessionTime.setText(GetPreferenceGeneral.getValue(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE));//"" + GetPreferenceGeneral.getSessionTimeout() ); //$NON-NLS-1$
		textExportDelimit.setText(GetPreferenceGeneral.getValue(PreferenceDefine.EXPORT_DILIMITER, PreferenceDefine.EXPORT_DILIMITER_VALUE));// "" + GetPreferenceGeneral.getExportDelimit() ); //$NON-NLS-1$
		textHomePage.setText(GetPreferenceGeneral.getValue(PreferenceDefine.DEFAULT_HOME_PAGE, PreferenceDefine.DEFAULT_HOME_PAGE_VALUE)); //$NON-NLS-1$
		
		String use = GetPreferenceGeneral.getValue(PreferenceDefine.DEFAULT_HOME_PAGE_USE, PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE);//GetPreferenceGeneral.getDefaultHomePageUse();
		if("true".equals(use)) {
			btnCheckButtonHomepage.setSelection(true);
		} else {
			btnCheckButtonHomepage.setSelection(false);
		}
		
		textSMTP.setText(GetPreferenceGeneral.getValue(PreferenceDefine.SMTP_HOST_NAME, PreferenceDefine.SMTP_HOST_NAME_VALUE));
		textPort.setText(GetPreferenceGeneral.getValue(PreferenceDefine.SMTP_PORT, PreferenceDefine.SMTP_PORT_VALUE));
		textEmail.setText(GetPreferenceGeneral.getValue(PreferenceDefine.SMTP_EMAIL, PreferenceDefine.SMTP_EMAIL_VALUE));
		textPasswd.setText(GetPreferenceGeneral.getValue(PreferenceDefine.SMTP_PASSWD, PreferenceDefine.SMTP_PASSWD_VALUE));
	}
}
