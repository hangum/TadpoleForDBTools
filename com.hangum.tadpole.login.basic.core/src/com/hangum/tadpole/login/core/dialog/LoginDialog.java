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
package com.hangum.tadpole.login.core.dialog;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
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

import com.hangum.tadpole.commons.admin.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;
import com.hangum.tadpole.commons.exception.TadpoleRuntimeException;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.CookieUtils;
import com.hangum.tadpole.commons.util.IPUtil;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.security.OTPInputDialog;
import com.hangum.tadpole.login.core.message.LoginDialogMessages;
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
public class LoginDialog extends AbstractLoginDialog {
	private static final Logger logger = Logger.getLogger(LoginDialog.class);
	
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
	
	private Composite compositeHead;
	private Composite compositeTail;
	
	public LoginDialog(Shell shell) {
		super(shell);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
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
		lblLoginForm.setText(LoginDialogMessages.get().LoginDialog_WelcomeMsg);
		
		lblLabelLblhangum = new Label(compositeHead, SWT.NONE);
		lblLabelLblhangum.setText(String.format(LoginDialogMessages.get().LoginDialog_ProjectRelease, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.RELEASE_DATE));
		
		Composite compositeLeftBtn = new Composite(container, SWT.NONE);
		compositeLeftBtn.setLayout(new GridLayout(1, false));
		
		Button button = new Button(compositeLeftBtn, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		button.setImage(ResourceManager.getPluginImage("com.hangum.tadpole.login.core", "resources/TDB_64.png")); //$NON-NLS-1$
		
		compositeLogin = new Composite(container, SWT.NONE);
		compositeLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeLogin.setLayout(new GridLayout(3, false));
		
		lblEmail = new Label(compositeLogin, SWT.NONE);
		lblEmail.setText(LoginDialogMessages.get().LoginDialog_1);
		
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
		btnCheckButton.setText(LoginDialogMessages.get().LoginDialog_9); //$NON-NLS-1$
		
		lblPassword = new Label(compositeLogin, SWT.NONE);
		lblPassword.setText(LoginDialogMessages.get().LoginDialog_4);
		
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
		btnLogin.setText(LoginDialogMessages.get().LoginDialog_15);
		
		lblLanguage = new Label(compositeLogin, SWT.NONE);
		lblLanguage.setText(LoginDialogMessages.get().LoginDialog_lblLanguage_text);
		
		comboLanguage = new Combo(compositeLogin, SWT.READ_ONLY);
		comboLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
		
		compositeTail = new Composite(container, SWT.NONE);
		GridLayout gl_compositeTail = new GridLayout(4, false);
		gl_compositeTail.verticalSpacing = 0;
		gl_compositeTail.horizontalSpacing = 0;
		gl_compositeTail.marginHeight = 0;
		gl_compositeTail.marginWidth = 0;
		compositeTail.setLayout(gl_compositeTail);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblLicense = new Label(compositeTail, SWT.NONE);
		lblLicense.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblLicense.setText("License is GNU Lesser General Public License v.3");
		
		Label lblDocument = new Label(compositeTail, SWT.NONE);
		lblDocument.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDocument.setText("<a href='" + LoginDialogMessages.get().LoginDialog_lblNewLabel_text_1 + "' target='_blank'>Document</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblDocument.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblIssue = new Label(compositeTail, SWT.NONE);
		lblIssue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIssue.setText("<a href='https://github.com/hangum/TadpoleForDBTools/issues' target='_blank'>Feedback</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblIssue.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		AnalyticCaller.track("login"); //$NON-NLS-1$
		
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
				InputDialog inputDialog=new InputDialog(getShell(), LoginDialogMessages.get().LoginDialog_10, String.format(LoginDialogMessages.get().LoginDialog_17, strEmail), "", null); //$NON-NLS-3$ //$NON-NLS-1$
				if(inputDialog.open() == Window.OK) {
					if(!userDao.getEmail_key().equals(inputDialog.getValue())) {
						throw new Exception(LoginDialogMessages.get().LoginDialog_19);
					} else {
						TadpoleSystem_UserQuery.updateEmailConfirm(strEmail);
					}
				} else {
					throw new Exception(LoginDialogMessages.get().LoginDialog_20);
				}
			}
			
			if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDao.getApproval_yn())) {
				MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, LoginDialogMessages.get().LoginDialog_27);
				
				return;
			}
			
			// login
			
			// Check the allow ip
			String strAllowIP = userDao.getAllow_ip();
			String ip_servletRequest = RequestInfoUtils.getRequestIP();
			boolean isAllow = IPUtil.ifFilterString(strAllowIP, ip_servletRequest);
			if(logger.isDebugEnabled())logger.debug(LoginDialogMessages.get().LoginDialog_21 + userDao.getEmail() + LoginDialogMessages.get().LoginDialog_22 + strAllowIP + LoginDialogMessages.get().LoginDialog_23+ RequestInfoUtils.getRequestIP());
			if(!isAllow) {
				logger.error(LoginDialogMessages.get().LoginDialog_21 + userDao.getEmail() + LoginDialogMessages.get().LoginDialog_22 + strAllowIP + LoginDialogMessages.get().LoginDialog_26+ RequestInfoUtils.getRequestIP());
				MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, LoginDialogMessages.get().LoginDialog_28);
				return;
			}
			
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDao.getUse_otp())) {
				OTPInputDialog otpDialog = new OTPInputDialog(getShell(), userDao.getEmail(), userDao.getOtp_secret());
				if(Dialog.CANCEL == otpDialog.open()) {
					return;
				}
			}
			
			// 로그인 유지.
			registLoginID();
			
			//
			SessionManager.addSession(userDao, SessionManager.LOGIN_IP_TYPE.SERVLET_REQUEST.name(), ip_servletRequest);
			
			// session 관리
			preLogin();
			
			// save login_history
			TadpoleSystem_UserQuery.saveLoginHistory(userDao.getSeq());
		} catch (TadpoleAuthorityException e) {
			logger.error(String.format("Login exception. request email is %s, reason %s", strEmail, e.getMessage())); //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, e.getMessage());
			
			textPasswd.setText("");
			textPasswd.setFocus();
			return;
		} catch(TadpoleRuntimeException e) {
			logger.error(String.format("Login exception. request email is %s, reason %s", strEmail, e.getMessage())); //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, e.getMessage());
			
			textPasswd.setFocus();
			return;
		} catch (Exception e) {
			logger.error(String.format("Login exception. request email is %s, reason %s", strEmail, e.getMessage()), e); //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, e.getMessage());
			
			textPasswd.setFocus();
			return;
		}	
		
		super.okPressed();
	}
	
	/**
	 * register login id
	 */
	private void registLoginID() {
		try {
			if(!btnCheckButton.getSelection()) {
				CookieUtils.deleteLoginCookie();
				return;
			}
			
			CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_SAVE_CKECK, Boolean.toString(btnCheckButton.getSelection()));
			CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_ID, textEMail.getText());
			Locale locale = (Locale)comboLanguage.getData(comboLanguage.getText());
			CookieUtils.saveCookie(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE, locale.toLanguageTag());
		} catch(Exception e) {
			logger.error("registe cookie", e);
		}
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
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, LoginDialogMessages.get().LoginDialog_11);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, LoginDialogMessages.get().LoginDialog_14);
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
		btnNewUser = createButton(parent, ID_NEW_USER, LoginDialogMessages.get().LoginDialog_button_new_user, false);
		try {
			SMTPDTO smtpDto = GetAdminPreference.getSessionSMTPINFO();
			if(smtpDto.isValid()) { //$NON-NLS-1$
				btnFindPasswd = createButton(parent, ID_FINDPASSWORD, LoginDialogMessages.get().ResetPassword, false);
			}
		} catch (Exception e) {
//			ignore exception
		}
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		initCookieData();
		if("".equals(textEMail.getText())) {
			textEMail.setFocus();
		} else {
			textPasswd.setFocus();
		}
		
		// check support browser
		if(!RequestInfoUtils.isSupportBrowser()) {
			String errMsg = LoginDialogMessages.get().LoginDialog_30 + RequestInfoUtils.getUserBrowser() + ".\n" + LoginDialogMessages.get().UserInformationDialog_5 + "\n" + LoginDialogMessages.get().LoginDialog_lblNewLabel_text;  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, errMsg);
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
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_SAVE_CKECK.equals(cookie.getName())) {
					btnCheckButton.setSelection(Boolean.parseBoolean(cookie.getValue()));
					intCount++;
				} else if(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE.equals(cookie.getName())) {
					Locale locale = Locale.forLanguageTag(cookie.getValue());
					comboLanguage.setText(locale.getDisplayLanguage(locale));
					changeUILocale(comboLanguage.getText());
					intCount++;
				}
				
				if(intCount == 3) return;
			}
		}
		
		// 세션에 발견되지 않았으면.
		comboLanguage.select(0);
		changeUILocale(comboLanguage.getText());
	}
	
	/**
	 * change ui locale
	 * 
	 * @param strComoboStr
	 */
	private void changeUILocale(String strComoboStr) {
		Locale localeSelect = (Locale)comboLanguage.getData(strComoboStr);
		RWT.getUISession().setLocale(localeSelect);
		
		lblLoginForm.setText(LoginDialogMessages.get().LoginDialog_WelcomeMsg);
		lblLabelLblhangum.setText(String.format(LoginDialogMessages.get().LoginDialog_ProjectRelease, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.RELEASE_DATE));
		btnLogin.setText(LoginDialogMessages.get().LoginDialog_15);
		
		btnCheckButton.setText(LoginDialogMessages.get().LoginDialog_9);
		lblEmail.setText(LoginDialogMessages.get().LoginDialog_1);
		lblPassword.setText(LoginDialogMessages.get().LoginDialog_4);
		lblLanguage.setText(LoginDialogMessages.get().LoginDialog_lblLanguage_text);
		
		if(btnNewUser != null) btnNewUser.setText(LoginDialogMessages.get().LoginDialog_button_new_user);
		if(btnFindPasswd != null) {
			btnFindPasswd.setText(LoginDialogMessages.get().ResetPassword);
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
		return new Point(480, 270);
	}
}