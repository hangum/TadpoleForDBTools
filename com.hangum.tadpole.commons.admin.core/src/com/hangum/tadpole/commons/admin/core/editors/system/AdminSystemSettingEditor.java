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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
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
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.commons.utils.zip.util.ZipUtils;
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
	private Combo comboIsPreferenceModify;
	private Combo comboResourceDownload;
	
	// smtp server
	private Text textSMTPHost;
	private Text textPort;
	private Text textEmail;
	private Text textPasswd;
	private Text textSendGridAPI;
	
	// download service
	private DownloadServiceHandler downloadServiceHandler;
	private Text textHomePage;
	private Button btnHomePageOpen;
	
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
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmPreferenceInitialize = new ToolItem(toolBar, SWT.NONE);
		tltmPreferenceInitialize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(getSite().getShell(), CommonMessages.get().Confirm, Messages.get().MsgRDBInitializeSetting)) {
					ChangeUserPreferenceValue changePreferenceValue = new ChangeUserPreferenceValue();
					changePreferenceValue.changePreferenceValue();
				}
			}
		});
		tltmPreferenceInitialize.setText(Messages.get().RDBInitializeSetting);
		
//		new ToolItem(toolBar, SWT.SEPARATOR);
//		ToolItem tltmRdb = new ToolItem(toolBar, SWT.NONE);
//		tltmRdb.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//			}
//		});
//		tltmRdb.setText(Messages.get().UnuseDBMaanagement);
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpSystemSetting = new Group(compositeBody, SWT.NONE);
		GridLayout gl_grpSystemSetting = new GridLayout(2, false);
		grpSystemSetting.setLayout(gl_grpSystemSetting);
		grpSystemSetting.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpSystemSetting.setText(Messages.get().SystemSetting);
		
		Label lblDBTimezone = new Label(grpSystemSetting, SWT.NONE);
		lblDBTimezone.setText(Messages.get().DatabaseTimeZone);
		
		Composite compositeTimezone = new Composite(grpSystemSetting, SWT.NONE);
		compositeTimezone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeTimezone = new GridLayout(2, false);
		gl_compositeTimezone.verticalSpacing = 0;
		gl_compositeTimezone.marginHeight = 0;
		gl_compositeTimezone.marginWidth = 5;
		compositeTimezone.setLayout(gl_compositeTimezone);
		
		comboTimezone = new Combo(compositeTimezone, SWT.NONE);
		comboTimezone.add("");
		for (String timzon : TimeZoneUtil.getTimezoneList()) {
			comboTimezone.add(timzon);
		}
		
		Label lblApplicationServer = new Label(compositeTimezone, SWT.NONE);
		lblApplicationServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblApplicationServer.setText(Messages.get().AppServerDbServerTimeZone);
		
		Label lblLoginMethod = new Label(grpSystemSetting, SWT.NONE);
		lblLoginMethod.setText(Messages.get().LoginMethod);
		
		Composite compositeLoginMethodDetail = new Composite(grpSystemSetting, SWT.NONE);
		compositeLoginMethodDetail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeLoginMethodDetail = new GridLayout(2, false);
		gl_compositeLoginMethodDetail.verticalSpacing = 0;
		gl_compositeLoginMethodDetail.marginHeight = 0;
		gl_compositeLoginMethodDetail.marginWidth = 5;
		compositeLoginMethodDetail.setLayout(gl_compositeLoginMethodDetail);
		
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
		
		Label lblDefaultHome = new Label(grpSystemSetting, SWT.NONE);
		lblDefaultHome.setText(Messages.get().DefaultHome);
		
		Composite compositeHome = new Composite(grpSystemSetting, SWT.NONE);
		compositeHome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHome = new GridLayout(2, false);
		gl_compositeHome.horizontalSpacing = 0;
		gl_compositeHome.verticalSpacing = 0;
		gl_compositeHome.marginHeight = 0;
		compositeHome.setLayout(gl_compositeHome);
		
		textHomePage = new Text(compositeHome, SWT.BORDER);
		textHomePage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnHomePageOpen = new Button(compositeHome, SWT.CHECK);
		btnHomePageOpen.setText("Open");
		
		Label lblLogDir = new Label(grpSystemSetting, SWT.NONE);
		lblLogDir.setText(Messages.get().LogDirectory);
		
		Composite compositeLogs = new Composite(grpSystemSetting, SWT.NONE);
		compositeLogs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeLogs = new GridLayout(2, false);
		gl_compositeLogs.verticalSpacing = 0;
		gl_compositeLogs.marginHeight = 0;
		compositeLogs.setLayout(gl_compositeLogs);
		
		textLog = new Text(compositeLogs, SWT.BORDER);
		textLog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textLog.setEditable(false);
		textLog.setText(new File(PublicTadpoleDefine.DEFAULT_LOG_FILE).getAbsolutePath());
		
		Button btnDownload = new Button(compositeLogs, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, Messages.get().DownloadMsg)) return;
					
					String strLogDir = StringUtils.removeEnd(textLog.getText(), "TadpoleHub_log.txt");
					if(logger.isDebugEnabled()) logger.debug(strLogDir);
					downloadFile("TadpoleHub_Log", strLogDir, "euc_kr");
				} catch(Exception ee) {
					logger.error("download log file", ee);
				}
			}
		});
		btnDownload.setText(CommonMessages.get().Download);
		
		Label lblResourceHome = new Label(grpSystemSetting, SWT.NONE);
		lblResourceHome.setText(Messages.get().ResourceHome);
		
		textResourceHome = new Text(grpSystemSetting, SWT.BORDER);
		textResourceHome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textResourceHome.setEnabled(false);
		textResourceHome.setEditable(false);
		textResourceHome.setText(ApplicationArgumentUtils.getResourcesDir());
		
		Label lblNewLabel = new Label(grpSystemSetting, SWT.NONE);
		lblNewLabel.setText(Messages.get().AdminSystemSettingEditor_2);
		
		Composite compositeShare = new Composite(grpSystemSetting, SWT.NONE);
		GridLayout gl_compositeShare = new GridLayout(3, false);
		gl_compositeShare.verticalSpacing = 0;
		gl_compositeShare.marginHeight = 0;
		compositeShare.setLayout(gl_compositeShare);
		compositeShare.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		comboNewUserPermit = new Combo(compositeShare, SWT.READ_ONLY);
		comboNewUserPermit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboNewUserPermit.add(yesNo.name());
		}
		
		Label lblSupportMonitoring = new Label(compositeShare, SWT.NONE);
		lblSupportMonitoring.setText(Messages.get().AdminSystemSettingEditor_SupportMonitoring);
		
		comboSupportMonitoring = new Combo(compositeShare, SWT.READ_ONLY);
		comboSupportMonitoring.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboSupportMonitoring.add(yesNo.name());
		}
		
		Label lblApiServerUrl = new Label(grpSystemSetting, SWT.NONE);
		lblApiServerUrl.setText(Messages.get().APIServerURL);
		
		textAPIServerURL = new Text(grpSystemSetting, SWT.BORDER);
		textAPIServerURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelHorizontal = new Label(compositeBody, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelHorizontal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSendgrid = new Label(compositeBody, SWT.NONE);
		lblSendgrid.setText(Messages.get().UseSendGridFirst);
		
		Group grpSendGrid = new Group(compositeBody, SWT.NONE);
		grpSendGrid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSendGrid.setText(Messages.get().SendGridSettings);
		grpSendGrid.setLayout(new GridLayout(2, false));
		
		Label lblSendgridApiKey = new Label(grpSendGrid, SWT.NONE);
		lblSendgridApiKey.setText(Messages.get().SendGridAPIKey);
		
		textSendGridAPI = new Text(grpSendGrid, SWT.BORDER);
		textSendGridAPI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpSMTPServer = new Group(compositeBody, SWT.NONE);
		grpSMTPServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSMTPServer.setText(Messages.get().SMTPSettings);
		grpSMTPServer.setLayout(new GridLayout(4, false));
		
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
		
		Label label = new Label(grpSettingDefaultUser, SWT.NONE);
		label.setText(Messages.get().SaveDBPassword);
		
		comboSaveDBPassword = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboSaveDBPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSaveDBPassword.setText(PublicTadpoleDefine.YES_NO.YES.name());
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboSaveDBPassword.add(yesNo.name());
		}
		
		Label label_1 = new Label(grpSettingDefaultUser, SWT.NONE);
		label_1.setText(Messages.get().IsPerferenceModify);
		
		comboIsPreferenceModify = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboIsPreferenceModify.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboIsPreferenceModify.add(yesNo.name());
		}
		
		Label lblResourceDownload = new Label(grpSettingDefaultUser, SWT.NONE);
		lblResourceDownload.setText(Messages.get().ResourceDownload);
		
		comboResourceDownload = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboResourceDownload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboResourceDownload.add(yesNo.name());
		}
		
		new Label(grpSettingDefaultUser, SWT.NONE);
		new Label(grpSettingDefaultUser, SWT.NONE);
		
//		Composite compositeTail = new Composite(parent, SWT.NONE);
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeTail.setLayout(new GridLayout(1, false));
		
		registerServiceHandler();
		
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
		
		textHomePage.setText(GetAdminPreference.getHomePage());
		btnHomePageOpen.setSelection(Boolean.getBoolean(GetAdminPreference.getHomePageOpen()));
		
		comboIsAddDB.setText(GetAdminPreference.getIsAddDB());
		comboIsSharedDB.setText(GetAdminPreference.getIsSharedDB());
		comboNewUserPermit.setText(GetAdminPreference.getNewUserPermit());
		textAPIServerURL.setText(GetAdminPreference.getApiServerURL());
		comboSaveDBPassword.setText(GetAdminPreference.getSaveDBPassword());
		
		textIntLimtCnt.setText(GetAdminPreference.getDefaultAddDBCnt());
		textDefaultUseDay.setText(GetAdminPreference.getServiceDurationDay());
		comboSupportMonitoring.setText(GetAdminPreference.getSupportMonitoring());
		comboIsPreferenceModify.setText(GetAdminPreference.getIsPreferenceModify());
		
		comboResourceDownload.setText(GetAdminPreference.getIsDefaultDonwload());
		
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
		
		String strHomePage		= textHomePage.getText();
		String strHomePageOpen	= ""+btnHomePageOpen.getSelection();
		
		String strResourceDownload = comboResourceDownload.getText();
		
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
			
			// 프리퍼런스 수정여부
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.DEFAULT_HOME, strHomePage);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.DEFAULT_HOME, userInfoDao);
			
			// 홈페이지
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.DEFAULT_HOME_OPEN, strHomePageOpen);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.DEFAULT_HOME_OPEN, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.IS_PREFERENCE_MODIFY, comboIsPreferenceModify.getText());
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.IS_PREFERENCE_MODIFY, userInfoDao);
			
			// 리소스 다운로드
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.IS_RESOURCE_DOWNLOAD, strResourceDownload);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.IS_RESOURCE_DOWNLOAD, userInfoDao);
			
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
	
	@Override
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	}
	
	/**
	 * download file
	 * @param strFileLocation
	 * @throws Exception
	 */
	protected void downloadFile(String fileName, String strFileLocation, String encoding) throws Exception {
		String strZipFile = ZipUtils.pack(strFileLocation);
		byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
		
		_downloadExtFile(fileName +".zip", bytesZip); //$NON-NLS-1$
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/** download service handler call */
	private  void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	private  void _downloadExtFile(String fileName, byte[] newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents);
		
		DownloadUtils.provideDownload(getSite().getShell(), downloadServiceHandler.getId());
	}
}
