/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.editors.system;

import java.io.File;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.admin.core.Activator;
import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.commons.admin.core.dialogs.LDAPConfigurationDialog;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.engine.utils.TimeZoneUtil;
import com.hangum.tadpole.preference.define.AdminPreferenceDefine;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.rdb.core.dialog.driver.JDBCDriverManageDialog;

/**
 * Admin System setting editor
 * 
 * @author hangum
 *
 */
public class AdminSystemSettingEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(AdminSystemSettingEditor.class);
	public static final String ID = "com.hangum.tadpole.admin.editor.admn.system.setting"; //$NON-NLS-1$
	
	private Combo comboTimezone;
	private Combo comboLoginMethod;
	private Button btnLdapConfiguration;
	private Combo comboNewUserPermit;
	private Text textResourceHome;
	
	private Combo comboIsAddDB;
	private Combo comboIsSharedDB;
	private Text textIntLimtCnt;
	private Text textDefaultUseDay;
	private Text textLog;
	private Combo comboSupportMonitoring;
	private Text textAPIServerURL;
	private Combo comboSaveDBPassword;
	
	// smtp server
	private Text textSMTPHost;
	private Text textPort;
	private Text textEmail;
	private Text textPasswd;
	private Text textSendGridAPI;
	
	public AdminSystemSettingEditor() {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		AdminSystemSettingEditorInput esqli = (AdminSystemSettingEditorInput) input;
		setPartName(esqli.getName());
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.BORDER);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveData();
			}
		});
		tltmSave.setToolTipText(CommonMessages.get().Save);
		tltmSave.setImage(GlobalImageUtils.getSave());
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmJdbcDriverManage = new ToolItem(toolBar, SWT.NONE);
		tltmJdbcDriverManage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JDBCDriverManageDialog dialog = new JDBCDriverManageDialog(getSite().getShell());
				if(Dialog.OK ==  dialog.open()) {
					if(dialog.isUploaded()) {
						MessageDialog.openInformation(getSite().getShell(), CommonMessages.get().Information, Messages.get().jdbcdriver);
					}
				}
			}
		});
		tltmJdbcDriverManage.setText(Messages.get().JDBCDriverManage);
		
		ToolItem tltmRdb = new ToolItem(toolBar, SWT.NONE);
		tltmRdb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(getSite().getShell(), CommonMessages.get().Confirm, Messages.get().MsgRDBInitializeSetting)) {
					ChangeUserPreferenceValue changePreferenceValue = new ChangeUserPreferenceValue();
					changePreferenceValue.changeRDBValue();
				}
			}
		});
		tltmRdb.setText(Messages.get().RDBInitializeSetting);
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(2, false);
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblDBTimezone = new Label(compositeBody, SWT.NONE);
		lblDBTimezone.setText(Messages.get().DatabaseTimeZone);
		
		comboTimezone = new Combo(compositeBody, SWT.NONE);
		comboTimezone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboTimezone.add("");
		for (String timzon : TimeZoneUtil.getTimezoneList()) {
			comboTimezone.add(timzon);
		}
		new Label(compositeBody, SWT.NONE);
		
		Label lblApplicationServer = new Label(compositeBody, SWT.NONE);
		lblApplicationServer.setText(Messages.get().AppServerDbServerTimeZone);
		
		Label lblLoginMethod = new Label(compositeBody, SWT.NONE);
		lblLoginMethod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLoginMethod.setText("Login method");
		
		Composite compositeLoginMethodDetail = new Composite(compositeBody, SWT.NONE);
		compositeLoginMethodDetail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeLoginMethodDetail.setLayout(new GridLayout(2, false));
		
		// login type
		comboLoginMethod = new Combo(compositeLoginMethodDetail, SWT.READ_ONLY);
		comboLoginMethod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initializeLoginMethodBtn();
			}
		});
		comboLoginMethod.add("original");
		comboLoginMethod.add("LDAP");
		comboLoginMethod.select(0);
		
		btnLdapConfiguration = new Button(compositeLoginMethodDetail, SWT.NONE);
		btnLdapConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LDAPConfigurationDialog dialog = new LDAPConfigurationDialog(getSite().getShell());
				dialog.open();
			}
		});
		btnLdapConfiguration.setText("LDAP Configuration");
		
		Label lblLogDir = new Label(compositeBody, SWT.NONE);
		lblLogDir.setText(Messages.get().LogDirectory);
		
		textLog = new Text(compositeBody, SWT.BORDER);
		textLog.setEditable(false);
		textLog.setEnabled(false);
		textLog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textLog.setText(new File(PublicTadpoleDefine.DEFAULT_LOG_FILE).getAbsolutePath());
		
		Label lblResourceHome = new Label(compositeBody, SWT.NONE);
		lblResourceHome.setText(Messages.get().ResourceHome);
		
		textResourceHome = new Text(compositeBody, SWT.BORDER);
		textResourceHome.setEnabled(false);
		textResourceHome.setEditable(false);
		textResourceHome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textResourceHome.setText(ApplicationArgumentUtils.getResourcesDir());
		
		Label lblNewLabel = new Label(compositeBody, SWT.NONE);
		lblNewLabel.setText(Messages.get().AdminSystemSettingEditor_2);
		
		comboNewUserPermit = new Combo(compositeBody, SWT.READ_ONLY);
		comboNewUserPermit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboNewUserPermit.add(yesNo.name());
		}
		
		Label lblSupportMonitoring = new Label(compositeBody, SWT.NONE);
		lblSupportMonitoring.setText(Messages.get().AdminSystemSettingEditor_SupportMonitoring);
		
		comboSupportMonitoring = new Combo(compositeBody, SWT.READ_ONLY);
		comboSupportMonitoring.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboSupportMonitoring.add(yesNo.name());
		}
		
		Label lblApiServerUrl = new Label(compositeBody, SWT.NONE);
		lblApiServerUrl.setText(Messages.get().APIServerURL);
		
		textAPIServerURL = new Text(compositeBody, SWT.BORDER);
		textAPIServerURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(compositeBody, SWT.NONE);
		label.setText(Messages.get().SaveDBPassword);
		
		comboSaveDBPassword = new Combo(compositeBody, SWT.READ_ONLY);
		comboSaveDBPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboSaveDBPassword.add(yesNo.name());
		}
		comboSaveDBPassword.setText(PublicTadpoleDefine.YES_NO.YES.name());
		new Label(compositeBody, SWT.NONE);
		
		Label labelHorizontal = new Label(compositeBody, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelHorizontal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		
		Label lblSendgrid = new Label(compositeBody, SWT.NONE);
		lblSendgrid.setText(Messages.get().UseSendGridFirst);
		new Label(compositeBody, SWT.NONE);
		
		Group grpSendGrid = new Group(compositeBody, SWT.NONE);
		grpSendGrid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSendGrid.setText(Messages.get().SendGridSettings);
		grpSendGrid.setLayout(new GridLayout(2, false));
		
		Label lblSendgridApiKey = new Label(grpSendGrid, SWT.NONE);
		lblSendgridApiKey.setText(Messages.get().SendGridAPIKey);
		
		textSendGridAPI = new Text(grpSendGrid, SWT.BORDER);
		textSendGridAPI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		
		Group grpSMTPServer = new Group(compositeBody, SWT.NONE);
		grpSMTPServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSMTPServer.setText(Messages.get().SMTPSettings);
		grpSMTPServer.setLayout(new GridLayout(2, false));
		
		Label lblSmtpServer = new Label(grpSMTPServer, SWT.NONE);
		lblSmtpServer.setText(Messages.get().SMTPServer);
		
		textSMTPHost = new Text(grpSMTPServer, SWT.BORDER);
		textSMTPHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(grpSMTPServer, SWT.NONE);
		lblPort.setText(Messages.get().Port);
		
		textPort = new Text(grpSMTPServer, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAccount = new Label(grpSMTPServer, SWT.NONE);
		lblAccount.setText(Messages.get().AdminEmail);
		
		textEmail = new Text(grpSMTPServer, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(grpSMTPServer, SWT.NONE);
		lblPassword.setText(Messages.get().Password);
		
		textPasswd = new Text(grpSMTPServer, SWT.BORDER | SWT.PASSWORD);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(compositeBody, SWT.NONE);
		
		Group grpSettingDefaultUser = new Group(compositeBody, SWT.NONE);
		grpSettingDefaultUser.setLayout(new GridLayout(4, false));
		grpSettingDefaultUser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSettingDefaultUser.setText(Messages.get().UserRoles);
		
		Label lblNewLabel_1 = new Label(grpSettingDefaultUser, SWT.NONE);
		lblNewLabel_1.setText(Messages.get().IsAddDB);
		
		comboIsAddDB = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboIsAddDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboIsAddDB.add(yesNo.name());
		}
		
		Label lblIsSharedDb = new Label(grpSettingDefaultUser, SWT.NONE);
		lblIsSharedDb.setText(Messages.get().IsSharedDB);
		
		comboIsSharedDB = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboIsSharedDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboIsSharedDB.add(yesNo.name());
		}
		Label lblDefaultAddDb = new Label(grpSettingDefaultUser, SWT.NONE);
		lblDefaultAddDb.setText(Messages.get().DefaultAddDBCount);
		
		textIntLimtCnt = new Text(grpSettingDefaultUser, SWT.BORDER);
		textIntLimtCnt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDefaultUseDay = new Label(grpSettingDefaultUser, SWT.NONE);
		lblDefaultUseDay.setText(Messages.get().DefaultUseDay);
		
		textDefaultUseDay = new Text(grpSettingDefaultUser, SWT.BORDER);
		textDefaultUseDay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
//		Composite compositeTail = new Composite(parent, SWT.NONE);
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeTail.setLayout(new GridLayout(1, false));
		
		initUI();
		
		AnalyticCaller.track(ID);
	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		comboTimezone.setText(GetAdminPreference.getDBTimezone());
		comboLoginMethod.setText(GetAdminPreference.getLoginMethod());
		initializeLoginMethodBtn();
		comboIsAddDB.setText(GetAdminPreference.getIsAddDB());
		comboIsSharedDB.setText(GetAdminPreference.getIsSharedDB());
		comboNewUserPermit.setText(GetAdminPreference.getNewUserPermit());
		textAPIServerURL.setText(GetAdminPreference.getApiServerURL());
		comboSaveDBPassword.setText(GetAdminPreference.getSaveDBPassword());
		
		textIntLimtCnt.setText(GetAdminPreference.getDefaultAddDBCnt());
		textDefaultUseDay.setText(GetAdminPreference.getServiceDurationDay());
		comboSupportMonitoring.setText(GetAdminPreference.getSupportMonitoring());
		
		// email
		try {
			SMTPDTO smtpDto = GetAdminPreference.getSessionSMTPINFO();
			textSendGridAPI.setText(smtpDto.getSendgrid_api());
			textSMTPHost.setText(smtpDto.getHost());
			textPort.setText(smtpDto.getPort());
			textEmail.setText(smtpDto.getEmail());
			textPasswd.setText(smtpDto.getPasswd());
		} catch (Exception e) {
			logger.error("SMTP Initialization Failed." + e.getMessage());
			textSMTPHost.setText(AdminPreferenceDefine.SMTP_HOST_NAME_VALUE);
			textPort.setText(AdminPreferenceDefine.SMTP_PORT_VALUE);
		}
	}
	
	/**
	 * initialize login method button
	 */
	private void initializeLoginMethodBtn() {
		if("LDAP".equals(comboLoginMethod.getText())) {
			btnLdapConfiguration.setEnabled(true);
		} else {
			btnLdapConfiguration.setEnabled(false);
		}
	}
	
	/**
	 * save data
	 * 
	 */
	private void saveData() {
		String txtLoginMethod 	= comboLoginMethod.getText();
		String txtSendGrid		= textSendGridAPI.getText();
		String txtSmtpHost 		= textSMTPHost.getText();
		String txtPort			= textPort.getText();
		String txtEmail			= textEmail.getText();
		String txtPasswd		= textPasswd.getText();
		
		if(!NumberUtils.isDigits(textIntLimtCnt.getText())) {
			MessageDialog.openError(null, CommonMessages.get().Confirm, Messages.get().mustBeNumber);
			textIntLimtCnt.setFocus();
			return;
		} else if(!NumberUtils.isNumber(textDefaultUseDay.getText())) {
			MessageDialog.openError(null, CommonMessages.get().Confirm, Messages.get().mustBeNumber);
			textDefaultUseDay.setFocus();
			return;
		} else if(!NumberUtils.isNumber(txtPort)) {
			textPort.setFocus();
			MessageDialog.openError(null,CommonMessages.get().Error, Messages.get().InputDigits);			 //$NON-NLS-1$
			return;
		}
		
		if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, Messages.get().AdminSystemSettingEditor_4)) return;
		
		try {
			UserInfoDataDAO userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.DB_TIME_ZONE, comboTimezone.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.DB_TIME_ZONE, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, txtLoginMethod);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.NEW_USER_PERMIT, comboNewUserPermit.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.NEW_USER_PERMIT, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.API_SERVER_URL, textAPIServerURL.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.API_SERVER_URL, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SAVE_DB_PASSWORD, comboSaveDBPassword.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SAVE_DB_PASSWORD, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.IS_ADD_DB, comboIsAddDB.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.IS_ADD_DB, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.IS_SHARED_DB, comboIsSharedDB.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.IS_SHARED_DB, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.DEFAULT_ADD_DB_CNT, textIntLimtCnt.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.DEFAULT_ADD_DB_CNT, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SERVICE_DURATION_DAY, textDefaultUseDay.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SERVICE_DURATION_DAY, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SUPPORT_MONITORING, comboSupportMonitoring.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SUPPORT_MONITORING, userInfoDao);
			
			// update admin value
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SENDGRID_API_NAME, txtSendGrid);
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SMTP_HOST_NAME, txtSmtpHost);
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SMTP_PORT, txtPort);
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SMTP_EMAIL, txtEmail);
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SMTP_PASSWD, txtPasswd);
			
			// session에 정보를 설정한다.
			SMTPDTO smtpDto = new SMTPDTO();
			smtpDto.setSendgrid_api(txtSendGrid);
			smtpDto.setHost(txtSmtpHost);
			smtpDto.setPort(txtPort);
			smtpDto.setEmail(txtEmail);
			smtpDto.setPasswd(txtPasswd);
			GetAdminPreference.setSessionSmtpInfo(smtpDto);
			
		} catch (Exception e) {
			logger.error("save exception", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}
}
