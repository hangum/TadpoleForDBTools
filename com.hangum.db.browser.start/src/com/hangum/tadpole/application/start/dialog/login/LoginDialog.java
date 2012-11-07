/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.start.dialog.login;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.manager.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystemConnector;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;
import com.swtdesigner.ResourceManager;

/**
 * login dialog
 * 
 * 국제화
 * http://wiki.eclipse.org/RAP/FAQ#How_to_switch_locale.2Flanguage_on_user_action.3F 
 * 
 * @author hangum
 *
 */
public class LoginDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(LoginDialog.class);
	
	private int ID_NEW_USER = 998;
	private  int ID_GUEST_USER = 999;
	private int ID_ADMIN_USER = 1000;
	private int ID_MANAGER_USER = 1001;
	
	private Text textEMail;
	private Text textPasswd;
//	private Combo comboLocalization;
	
	public LoginDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.LoginDialog_0);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 3;
		
		Label lblPleaseSignIn = new Label(container, SWT.NONE);
		lblPleaseSignIn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPleaseSignIn.setText(Messages.LoginDialog_lblPleaseSignIn_text);
		new Label(container, SWT.NONE);
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText(Messages.LoginDialog_1);
		
		textEMail = new Text(container, SWT.BORDER);
//		textEMail.setText("adi.tadpole@gmail.com");
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLoginImage = new Label(container, SWT.NONE);
		lblLoginImage.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
		lblLoginImage.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/icons/LoginManager.png")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText(Messages.LoginDialog_4);
		
		textPasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					okPressed();
				}
			}
		});
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		textEMail.setFocus();
		init();

		return container;
	}
	
	/**
	 * 초기 로그인시 유저가 한명이면 어드민 정보를 보여줍니다. 
	 */
	private void init() {
//		try {
//			UserDAO user = TadpoleSystem_UserQuery.loginUserCount();
//			if(user != null) {
//				textEMail.setText(user.getEmail());
//				textPasswd.setText(user.getPasswd());
//			}
//		} catch (Exception e) {
//			logger.error(Messages.LoginDialog_6, e);
//		}
		
//		System.out.println("[현재 Locale]===>" + RWT.getLocale().toString());
		
	}

	private void newUser() {
		NewUserDialog newUser = new NewUserDialog(getParentShell());
		newUser.open();
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		 if(buttonId == ID_NEW_USER) {
				newUser();
				
				return;
		 } else if(buttonId == IDialogConstants.OK_ID) {
			 okPressed();
			 
		 } else {
			String userId = "", password = "";
			
			if(buttonId == ID_GUEST_USER) {
				userId = TadpoleSystemConnector.GUEST_EMAIL;
				password = TadpoleSystemConnector.GUEST_PASSWD;
			
			} else if(buttonId == ID_ADMIN_USER) {
				userId = TadpoleSystemConnector.ADMIN_EMAIL;
				password = TadpoleSystemConnector.ADMIN_PASSWD;
				
			} else if(buttonId == ID_MANAGER_USER) {
				userId = TadpoleSystemConnector.MANAGER_EMAIL;
				password = TadpoleSystemConnector.MANAGER_PASSWD;
			}
			
			// 정상이면 session에 로그인 정보를 입력하고 
			try {
				UserDAO login = TadpoleSystem_UserQuery.login(userId, password);
				if(!Define.USER_TYPE.MANAGER.toString().equals(login.getUser_type())) {
					UserDAO groupManagerUser =  TadpoleSystem_UserQuery.getGroupManager(login.getGroup_seq());
					
					SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), groupManagerUser.getSeq());
				}  else {
					SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), -1);
				}
				
				init();
			} catch (Exception e) {
				logger.error("demo mode user login", e);
				MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, e.getMessage());
				return;
			}	
			
			super.okPressed();
			
		}
	}
	
	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String strPass = StringUtils.trimToEmpty(textPasswd.getText());

		if(!validation(strEmail, strPass)) return;
		
		// 신규 사용자 등록이면 
		try {
			UserDAO login = TadpoleSystem_UserQuery.login(strEmail, strPass);
			
			if(Define.USER_TYPE.USER.toString().equals(login.getUser_type())) {
				// 그룹의 manager 정보
				UserDAO groupManagerUser = TadpoleSystem_UserQuery.getGroupManager(login.getGroup_seq());
				
				// 정상이면 session에 로그인 정보를 입력하고 
				SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), groupManagerUser.getSeq());
			} else {
				SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), -1);
			}
		} catch (Exception e) {
			logger.error("login", e);
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, e.getMessage());
			return;
		}	
		
		super.okPressed();
	}
		
	@Override
	public boolean close() {
		// 로그인이 안되었을 경우 로그인 창이 남아 있도록...
		// https://github.com/hangum/TadpoleForDBTools/issues/31
		if( SessionManager.getSeq() == 0) return false;
		
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
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, Messages.LoginDialog_11);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, Messages.LoginDialog_14);
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
//		createButton(parent, ID_ADMIN_USER, "Admin", false);
		
		// -test 일 경우만 ..
		if(ApplicationArgumentUtils.isTestMode()) {
			createButton(parent, ID_MANAGER_USER, "Manager", false);
			createButton(parent, ID_GUEST_USER, "Guest", false);
		}
		
		createButton(parent, ID_NEW_USER, "New", false);
		createButton(parent, IDialogConstants.OK_ID, Messages.LoginDialog_15, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(526, 222);
	}
	
}
