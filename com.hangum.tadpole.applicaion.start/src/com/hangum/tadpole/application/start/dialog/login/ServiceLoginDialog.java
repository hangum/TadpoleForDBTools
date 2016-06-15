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

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.admin.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.libs.core.googleauth.GoogleAuthManager;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.commons.util.CookieUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.IPFilterUtil;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * Tadpole DB Hub User login dialog.
 * support the localization : (http://wiki.eclipse.org/RAP/FAQ#How_to_switch_locale.2Flanguage_on_user_action.3F) 
 * 
 * @author hangum
 *
 */
public class ServiceLoginDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ServiceLoginDialog.class);
	
	private int ID_NEW_USER		 	= IDialogConstants.CLIENT_ID 	+ 1;
	private int ID_FINDPASSWORD 	= IDialogConstants.CLIENT_ID 	+ 2;
	
	private Label lblLoginForm;
	private Label lblLabelLblhangum;
	private Composite compositeLogin;
	private Label lblEmail;
	
	private Button btnCheckButton;
	private Text textEMail;
	private Label lblPassword;
	private Text textPasswd;
	private Label lblLanguage;
	private Combo comboLanguage;
	
	private Button btnLogin;
	private Button btnNewUser;
	private Button btnFindPasswd;
	
//	String strPaypal = "<form action='https://www.paypal.com/cgi-bin/webscr' method='post' target='_top'> " + //$NON-NLS-1$
//						"	<input type='hidden' name='cmd' value='_s-xclick'> " + //$NON-NLS-1$
//						"	<input type='hidden' name='encrypted' value='-----BEGIN PKCS7-----MIIHNwYJKoZIhvcNAQcEoIIHKDCCByQCAQExggEwMIIBLAIBADCBlDCBjjELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRQwEgYDVQQKEwtQYXlQYWwgSW5jLjETMBEGA1UECxQKbGl2ZV9jZXJ0czERMA8GA1UEAxQIbGl2ZV9hcGkxHDAaBgkqhkiG9w0BCQEWDXJlQHBheXBhbC5jb20CAQAwDQYJKoZIhvcNAQEBBQAEgYB3IDn/KYN412pCfvQWTnLBKX3PcmcRdBPjt6+XZqUrb0yVbZ+hzQETdyQMzULIj1PbATVrZpDzhgjCPNduIwN22ga9+MfiHwLPm6BUHJ67EV4SvY9zLKisBuaU2HfydW3q0lp1dPscQscFVmx/LoitJwt4G5t9C5kwhj37NESeIDELMAkGBSsOAwIaBQAwgbQGCSqGSIb3DQEHATAUBggqhkiG9w0DBwQIZ5TXQJMFnNWAgZDBYBl8qJb6fQdWnMDoM5S59A6tu+F7rnIrD0e7sg6FE1m+zo1B8SYRSfGuzWpi/s2Uuqa5tiwiosxcqL3dmcfK5ZKlsbJipa+098M9q5Ilugg/GN+kz8gUQqqJrwYA3DGuM+sg/BXoIjRj4NBXh6KG+eV4FLFRUD7EMoGA3u+KHMQ+0zqBq8NOgdCqI3ag99CgggOHMIIDgzCCAuygAwIBAgIBADANBgkqhkiG9w0BAQUFADCBjjELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRQwEgYDVQQKEwtQYXlQYWwgSW5jLjETMBEGA1UECxQKbGl2ZV9jZXJ0czERMA8GA1UEAxQIbGl2ZV9hcGkxHDAaBgkqhkiG9w0BCQEWDXJlQHBheXBhbC5jb20wHhcNMDQwMjEzMTAxMzE1WhcNMzUwMjEzMTAxMzE1WjCBjjELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRQwEgYDVQQKEwtQYXlQYWwgSW5jLjETMBEGA1UECxQKbGl2ZV9jZXJ0czERMA8GA1UEAxQIbGl2ZV9hcGkxHDAaBgkqhkiG9w0BCQEWDXJlQHBheXBhbC5jb20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMFHTt38RMxLXJyO2SmS+Ndl72T7oKJ4u4uw+6awntALWh03PewmIJuzbALScsTS4sZoS1fKciBGoh11gIfHzylvkdNe/hJl66/RGqrj5rFb08sAABNTzDTiqqNpJeBsYs/c2aiGozptX2RlnBktH+SUNpAajW724Nv2Wvhif6sFAgMBAAGjge4wgeswHQYDVR0OBBYEFJaffLvGbxe9WT9S1wob7BDWZJRrMIG7BgNVHSMEgbMwgbCAFJaffLvGbxe9WT9S1wob7BDWZJRroYGUpIGRMIGOMQswCQYDVQQGEwJVUzELMAkGA1UECBMCQ0ExFjAUBgNVBAcTDU1vdW50YWluIFZpZXcxFDASBgNVBAoTC1BheVBhbCBJbmMuMRMwEQYDVQQLFApsaXZlX2NlcnRzMREwDwYDVQQDFAhsaXZlX2FwaTEcMBoGCSqGSIb3DQEJARYNcmVAcGF5cGFsLmNvbYIBADAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4GBAIFfOlaagFrl71+jq6OKidbWFSE+Q4FqROvdgIONth+8kSK//Y/4ihuE4Ymvzn5ceE3S/iBSQQMjyvb+s2TWbQYDwcp129OPIbD9epdr4tJOUNiSojw7BHwYRiPh58S1xGlFgHFXwrEBb3dgNbMUa+u4qectsMAXpVHnD9wIyfmHMYIBmjCCAZYCAQEwgZQwgY4xCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJDQTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEUMBIGA1UEChMLUGF5UGFsIEluYy4xEzARBgNVBAsUCmxpdmVfY2VydHMxETAPBgNVBAMUCGxpdmVfYXBpMRwwGgYJKoZIhvcNAQkBFg1yZUBwYXlwYWwuY29tAgEAMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNTEwMzEwMzAyMjNaMCMGCSqGSIb3DQEJBDEWBBRJRxkqnn6TtfjQRDDRGzbcSP44qzANBgkqhkiG9w0BAQEFAASBgEJRwHPk6dra3xxTSHMU//jg3kYrk2qEYp/Zoq8s7mdcs3ezpdiaKXS+PPox2oDsYxYaKILBd4bh/6uelcVx5n3atULojdYVUdh/aq435GXwvPkTSO/XQIyIwOsKM1epzrMjgEEBMypuMnjqsQb9/KRdH6SfpJibe/5NHvjJ3E8F-----END PKCS7-----'> " + //$NON-NLS-1$
//						"	<input type='image' src='https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif' border='0' name='submit' alt='PayPal - The safer, easier way to pay online!'> " + //$NON-NLS-1$
//						"	</form>"; //$NON-NLS-1$
	private Composite compositeHead;
//	private Button btnNewButton;
//	private Button btnNewButton_1;
//	private Button btnNewButton_2;
	private Composite compositeOtherBtn;
	
	
	public ServiceLoginDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(String.format("%s", SystemDefine.NAME)); //$NON-NLS-1$
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		lblLoginForm = new Label(compositeHead, SWT.NONE);
		lblLoginForm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblLoginForm.setFont(SWTResourceManager.getFont(".SF NS Text", 15, SWT.NONE));
		lblLoginForm.setText(Messages.get().LoginDialog_WelcomeMsg);
		
		lblLabelLblhangum = new Label(compositeHead, SWT.NONE);
		lblLabelLblhangum.setText(String.format(Messages.get().LoginDialog_ProjectRelease, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.RELEASE_DATE));
		
		compositeLogin = new Composite(container, SWT.NONE);
		compositeLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeLogin.setLayout(new GridLayout(3, false));
		
		lblEmail = new Label(compositeLogin, SWT.NONE);
		lblEmail.setText(Messages.get().LoginDialog_1);
		
		textEMail = new Text(compositeLogin, SWT.BORDER);
		textEMail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					if(!"".equals(textPasswd.getText())) okPressed(); //$NON-NLS-1$
					else textPasswd.setFocus();
				}
			}
		});
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnCheckButton = new Button(compositeLogin, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!btnCheckButton.getSelection()) {
					CookieUtils.deleteLoginCookie();
				}
			}
		});
		btnCheckButton.setText(Messages.get().LoginDialog_9); //$NON-NLS-1$
		
		lblPassword = new Label(compositeLogin, SWT.NONE);
		lblPassword.setText(Messages.get().LoginDialog_4);
		
		textPasswd = new Text(compositeLogin, SWT.BORDER | SWT.PASSWORD);
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					okPressed();
				}
			}
		});
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnLogin = new Button(compositeLogin, SWT.NONE);
		btnLogin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				okPressed();
			}
		});
		btnLogin.setText(Messages.get().LoginDialog_15);
		
		lblLanguage = new Label(compositeLogin, SWT.NONE);
		lblLanguage.setText(Messages.get().LoginDialog_lblLanguage_text);
		
		comboLanguage = new Combo(compositeLogin, SWT.READ_ONLY);
		comboLanguage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeUILocale(comboLanguage.getText());
			}
		});
		comboLanguage.add(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
		comboLanguage.add(Locale.KOREAN.getDisplayLanguage(Locale.KOREAN));

		comboLanguage.setData(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), Locale.ENGLISH);
		comboLanguage.setData(Locale.KOREAN.getDisplayLanguage(Locale.KOREAN), Locale.KOREAN);
		comboLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		compositeOtherBtn = new Composite(compositeLogin, SWT.NONE);
		compositeOtherBtn.setLayout(new GridLayout(2, false));
		compositeOtherBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		
		btnNewUser = new Button(compositeOtherBtn, SWT.NONE);
		btnNewUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonPressed(ID_NEW_USER);
			}
		});
		btnNewUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewUser.setText(Messages.get().LoginDialog_button_text_1);
		
		try {
			SMTPDTO smtpDto = GetAdminPreference.getSessionSMTPINFO();
			if(smtpDto.isValid()) { //$NON-NLS-1$
				btnFindPasswd = new Button(compositeOtherBtn, SWT.NONE); //ID_FINDPASSWORD,
				btnFindPasswd.setText(Messages.get().FindPassword);
				btnFindPasswd.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						buttonPressed(ID_FINDPASSWORD);
					}
				});
			}
		} catch (Exception e) {
		//	ignore exception
		}
		
		// company info
		Composite compositeTailRight = new Composite(container, SWT.NONE);
		compositeTailRight.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		compositeTailRight.setLayout(new GridLayout(2, false));
		
		Label labelCompanyInfo = new Label(compositeTailRight, SWT.NONE);
		labelCompanyInfo.setText("사업자등록번호 : 460-86-00429 | 통신판매번호 : 제 2016-서울은평-0401호");
		
		Label tail_lblLoginForm = new Label(compositeTailRight, SWT.NONE);
		tail_lblLoginForm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 3));
		tail_lblLoginForm.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/TDB_64.png"));

		
		Label lblHangumtadpolehubcom = new Label(compositeTailRight, SWT.NONE);
		lblHangumtadpolehubcom.setText("대표, 개인정보관리책임자 : 조현종 | email : hangum@tadpolehub.com");
	
		Label label = new Label(compositeTailRight, SWT.NONE);
		label.setText("서울시 은평구 불광로5가길 3-2 102호 (Tel. 02-2226-8291)");
		
		Label lblCopyrightcTadpolehub = new Label(compositeTailRight, SWT.NONE);
		lblCopyrightcTadpolehub.setText("Copyright(c) 2016 TadpoleHub Co.,LTD ");
	
		Label tail_companyName = new Label(compositeTailRight, SWT.NONE);
		tail_companyName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 3));
		tail_companyName.setText("(주) 테드폴허브");
		
		AnalyticCaller.track("ServiceLoginDialog"); //$NON-NLS-1$
		
		initUI();
		
		return compositeLogin;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == ID_NEW_USER) {
			newUser();
			textEMail.setFocus();
		} else if(buttonId == ID_FINDPASSWORD) {
			findPassword();
			textEMail.setFocus();
		} else {
			okPressed();
		}
	}
	
	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String strPass = StringUtils.trimToEmpty(textPasswd.getText());

		if(!validation(strEmail, strPass)) return;
		
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.login(strEmail, strPass);
			
			// firsttime email confirm
			if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDao.getIs_email_certification())) {
				InputDialog inputDialog=new InputDialog(getShell(), Messages.get().LoginDialog_10, String.format(Messages.get().LoginDialog_17, strEmail), "", null); //$NON-NLS-3$ //$NON-NLS-1$
				if(inputDialog.open() == Window.OK) {
					if(!userDao.getEmail_key().equals(inputDialog.getValue())) {
						throw new Exception(Messages.get().LoginDialog_19);
					} else {
						TadpoleSystem_UserQuery.updateEmailConfirm(strEmail);
					}
				} else {
					throw new Exception(Messages.get().LoginDialog_20);
				}
			}
			
			if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDao.getApproval_yn())) {
				MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().LoginDialog_27);
				
				return;
			}
			
			// Check the allow ip
			String strAllowIP = userDao.getAllow_ip();
			boolean isAllow = IPFilterUtil.ifFilterString(strAllowIP, RequestInfoUtils.getRequestIP());
			if(logger.isDebugEnabled())logger.debug(Messages.get().LoginDialog_21 + userDao.getEmail() + Messages.get().LoginDialog_22 + strAllowIP + Messages.get().LoginDialog_23+ RequestInfoUtils.getRequestIP());
			if(!isAllow) {
				logger.error(Messages.get().LoginDialog_21 + userDao.getEmail() + Messages.get().LoginDialog_22 + strAllowIP + Messages.get().LoginDialog_26+ RequestInfoUtils.getRequestIP());
				MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().LoginDialog_28);
				return;
			}
			
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDao.getUse_otp())) {
				OTPLoginDialog otpDialog = new OTPLoginDialog(getShell());
				otpDialog.open(); 

				if(!GoogleAuthManager.getInstance().isValidate(userDao.getOtp_secret(), otpDialog.getIntOTPCode())) {
					throw new Exception(Messages.get().LoginDialog_2);
				}
			}
			
			// 로그인 유지.
			registLoginID(userDao.getEmail(), strPass);
			
			SessionManager.addSession(userDao);
			
			// save login_history
			TadpoleSystem_UserQuery.saveLoginHistory(userDao.getSeq());
		} catch (TadpoleAuthorityException e) {
			logger.error(String.format("Login exception. request email is %s, reason %s", strEmail, e.getMessage())); //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, e.getMessage());
			
			textPasswd.setText("");
			textPasswd.setFocus();
			return;
		} catch (Exception e) {
			logger.error(String.format("Login exception. request email is %s, reason %s", strEmail, e.getMessage())); //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, e.getMessage());
			
			textPasswd.setFocus();
			return;
		}	
		
		super.okPressed();
	}
	
	/**
	 * register login id
	 * 
	 * @param userId
	 */
	private void registLoginID(String userId, String userPwd) {
		if(!btnCheckButton.getSelection()) {
			CookieUtils.deleteLoginCookie();
			return;
		}
		
		CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_SAVE_CKECK, Boolean.toString(btnCheckButton.getSelection()));
		CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_ID, userId);
//		CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_PWD, userPwd);

		Locale locale = (Locale)comboLanguage.getData(comboLanguage.getText());
		CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE, locale.toLanguageTag());
	}
	
	@Override
	public boolean close() {
		//  로그인이 안되었을 경우 로그인 창이 남아 있도록...(https://github.com/hangum/TadpoleForDBTools/issues/31)
		if(!SessionManager.isLogin()) return false;
		
		return super.close();
	}

	/**
	 * validation
	 * 
	 * @param strEmail
	 * @param strPass
	 */
	private boolean validation(String strEmail, String strPass) {
		// validation
		if("".equals(strEmail)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().LoginDialog_11);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().LoginDialog_14);
			textPasswd.setFocus();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.marginHeight = 0;
		parent.setLayout(layout);
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		String defaultLanguage = RWT.getUISession().getLocale().getDisplayLanguage(Locale.ENGLISH);
		boolean isExist = false;
		for(String strName : comboLanguage.getItems()) {
			if(strName.equals(defaultLanguage)) {
				isExist = true;
				comboLanguage.setText(strName);
				changeUILocale(comboLanguage.getText());
				break;
			}
		}
		
		// 쿠키에서 사용자 정보를 찾지 못했으면..
		if(!isExist) {
			// 사용자 브라우저 랭귀지를 가져와서, 올챙이가 지원하는 랭귀지인지 검사해서..
			String locale = RequestInfoUtils.getDisplayLocale();
			for(String strLocale : comboLanguage.getItems()) {
				if(strLocale.equals(locale)) {
					isExist = true;
					break;
				}
			}
			// 있으면... 
			if(isExist) comboLanguage.setText(locale);
			else comboLanguage.setText(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
			
			// 랭귀지를 바꾸어 준다.
			changeUILocale(comboLanguage.getText());
		}
		
		// find login id
		initCookieData();
		if("".equals(textEMail.getText())) {
			textEMail.setFocus();
		} else {
			textPasswd.setFocus();
		}
		
		// check support browser
		if(!RequestInfoUtils.isSupportBrowser()) {
			String errMsg = Messages.get().LoginDialog_30 + RequestInfoUtils.getUserBrowser() + ".\n" + Messages.get().UserInformationDialog_5 + "\n" + Messages.get().LoginDialog_lblNewLabel_text;  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, errMsg);
		}
	}
	
	/**
	 * initialize cookie data
	 */
	private void initCookieData() {
		HttpServletRequest request = RWT.getRequest();
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			int intCount = 0;
			for (Cookie cookie : cookies) {				
				if(PublicTadpoleDefine.TDB_COOKIE_USER_ID.equals(cookie.getName())) {
					textEMail.setText(cookie.getValue());
					intCount++;
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_PWD.equals(cookie.getName())) {
					textPasswd.setText(cookie.getValue());
					intCount++;
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_SAVE_CKECK.equals(cookie.getName())) {
					btnCheckButton.setSelection(Boolean.parseBoolean(cookie.getValue()));
					intCount++;
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE.equals(cookie.getName())) {
					Locale locale = Locale.forLanguageTag(cookie.getValue());
					comboLanguage.setText(locale.getDisplayLanguage(locale));
					changeUILocale(comboLanguage.getText());
					intCount++;
				}
				
				if(intCount == 4) break;
			}
		}
	}
	
	/**
	 * change ui locale
	 * 
	 * @param strComoboStr
	 */
	private void changeUILocale(String strComoboStr) {
		Locale localeSelect = (Locale)comboLanguage.getData(strComoboStr);
		RWT.getUISession().setLocale(localeSelect);
		
		lblLoginForm.setText(Messages.get().LoginDialog_WelcomeMsg);
		lblLabelLblhangum.setText(String.format(Messages.get().LoginDialog_ProjectRelease, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.RELEASE_DATE));
		btnLogin.setText(Messages.get().LoginDialog_15);
		
		btnCheckButton.setText(Messages.get().LoginDialog_9);
		lblEmail.setText(Messages.get().LoginDialog_1);
		lblPassword.setText(Messages.get().LoginDialog_4);
		lblLanguage.setText(Messages.get().LoginDialog_lblLanguage_text);
		
		if(btnNewUser != null) btnNewUser.setText(Messages.get().LoginDialog_button_text_1);
		if(btnFindPasswd != null) {
			btnFindPasswd.setText(Messages.get().FindPassword);
		}
		
		compositeLogin.layout();
	}

	private void newUser() {
		NewUserDialog newUser = new NewUserDialog(getParentShell());
		if(Dialog.OK == newUser.open()) {
			String strEmail = newUser.getUserDao().getEmail();
			textEMail.setText(strEmail);
			textPasswd.setFocus();
		}
	}
	
	private void findPassword() {
		FindPasswordDialog dlg = new FindPasswordDialog(getShell(), textEMail.getText());
		dlg.open();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(520, 340);
	}
}