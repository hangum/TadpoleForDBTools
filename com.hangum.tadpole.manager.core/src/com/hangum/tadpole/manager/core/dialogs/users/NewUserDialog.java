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
package com.hangum.tadpole.manager.core.dialogs.users;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.SecurityHint;
import com.hangum.tadpold.commons.libs.core.googleauth.GoogleAuthManager;
import com.hangum.tadpold.commons.libs.core.mails.SendEmails;
import com.hangum.tadpold.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpold.commons.libs.core.mails.template.NewUserMailBodyTemplate;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserGroupDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserGroupQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserRole;

/**
 * Add new user Dialog
 *  
 * @author hangum
 *
 */
public class NewUserDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(NewUserDialog.class);
	
	private Button btnManager;
	private Button btnUser;
	private Button btnDBA;
	
	private Composite compositeUserGroup;
	/** user group combo로 기 존재하는 그룹 정보  */
	private Combo comboUserGroup;
	
	/** user group text로 신규 그룹명을 입력 받는다 */
	private Text textUserGroup;
	
	private Text textEMail;
	private Text textPasswd;
	private Text textRePasswd;
	private Text textName;
	
	private Combo comboLanguage;
	
	/** 자동 허용유무 */
	private PublicTadpoleDefine.YES_NO approvalYn;
	
	private Combo comboQuestion;
	private Text textAnswer;

	/** OTP code */
	private String secretKey = ""; //$NON-NLS-1$
	private Button btnGetOptCode;
	private Label lblSecretKey;
	private Text textSecretKey;
	private Label lblQrcodeUrl;
	private Text textQRCodeURL;
	private Label lblOtpCdoe;
	private Text textOTPCode;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewUserDialog(Shell parentShell, PublicTadpoleDefine.YES_NO approvalYn) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.approvalYn = approvalYn;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.NewUserDialog_0);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.numColumns = 2;
		
		Label lblUserType = new Label(container, SWT.NONE);
		lblUserType.setText(Messages.NewUserDialog_lblUserType_text);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		if(!SessionManager.isLogin()) {
			btnManager = new Button(composite, SWT.RADIO);
			btnManager.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					initUserGroup();
				}
			});
			btnManager.setText(Messages.NewUserDialog_btnManager_text_1);
			btnManager.setSelection(true);
		}
		
		btnDBA = new Button(composite, SWT.RADIO);
		btnDBA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUserGroup();
			}
		});
		btnDBA.setText("DBA"); //$NON-NLS-1$
		btnDBA.setEnabled(false);
		
		btnUser = new Button(composite, SWT.RADIO);
		btnUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUserGroup();
			}
		});
		btnUser.setText(Messages.NewUserDialog_btnUser_text);
		btnUser.setSelection(false);
		btnUser.setEnabled(false);
		new Label(composite, SWT.NONE);
		
		Label lblGroupName = new Label(container, SWT.NONE);
		lblGroupName.setText(Messages.NewUserDialog_lblNewLabel_text);
		
		compositeUserGroup = new Composite(container, SWT.NONE);
		GridLayout gl_compositeUserGroup = new GridLayout(1, false);
		gl_compositeUserGroup.verticalSpacing = 0;
		gl_compositeUserGroup.horizontalSpacing = 0;
		gl_compositeUserGroup.marginHeight = 1;
		gl_compositeUserGroup.marginWidth = 0;
		compositeUserGroup.setLayout(gl_compositeUserGroup);
		compositeUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblIdemail = new Label(container, SWT.NONE);
		lblIdemail.setText(Messages.NewUserDialog_1);
		
		textEMail = new Text(container, SWT.BORDER);
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.NewUserDialog_2);
		
		textPasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.NewUserDialog_3);
		
		textRePasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(Messages.NewUserDialog_4);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLanguage = new Label(container, SWT.NONE);
		lblLanguage.setText(Messages.NewUserDialog_lblLanguage_text);
		
		comboLanguage = new Combo(container, SWT.READ_ONLY);
		comboLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboLanguage.add("ko"); //$NON-NLS-1$
		comboLanguage.add("en_us"); //$NON-NLS-1$
		comboLanguage.select(0);
		
		Label lblPasswordDescription = new Label(container, SWT.NONE);
		lblPasswordDescription.setText(Messages.NewUserDialog_18);
		lblPasswordDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblQuestion = new Label(container, SWT.NONE);
		lblQuestion.setText(Messages.NewUserDialog_22);

		comboQuestion = new Combo(container, SWT.READ_ONLY);
		comboQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (SecurityHint q : PublicTadpoleDefine.SecurityHint.values()) {
			comboQuestion.add(q.toString(), q.getOrderIndex());
			comboQuestion.setData(q.getOrderIndex()+q.toString(), q.getKey());
		}
		comboQuestion.select(0);
		
		Label lblAnswer = new Label(container, SWT.NONE);
		lblAnswer.setText(Messages.NewUserDialog_27);

		textAnswer = new Text(container, SWT.BORDER);
		textAnswer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpGoogleOtp = new Group(container, SWT.NONE);
		grpGoogleOtp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		grpGoogleOtp.setText(Messages.NewUserDialog_grpGoogleOtp_text);
		grpGoogleOtp.setLayout(new GridLayout(2, false));
		
		btnGetOptCode = new Button(grpGoogleOtp, SWT.CHECK);
		btnGetOptCode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateGoogleOTP();
			}
		});
		btnGetOptCode.setText(Messages.NewUserDialog_btnCheckButton_text);
		new Label(grpGoogleOtp, SWT.NONE);
		
		lblSecretKey = new Label(grpGoogleOtp, SWT.NONE);
		lblSecretKey.setText(Messages.NewUserDialog_lblAccessKey_1_text);
		
		textSecretKey = new Text(grpGoogleOtp, SWT.BORDER);
		textSecretKey.setEditable(false);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblQrcodeUrl = new Label(grpGoogleOtp, SWT.NONE);
		lblQrcodeUrl.setText("<a href='https://code.google.com/p/google-authenticator/' target='_blank'>" + Messages.NewUserDialog_lblQrcodeUrl_text + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblQrcodeUrl.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		textQRCodeURL = new Text(grpGoogleOtp, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData gd_textQRCodeURL = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textQRCodeURL.heightHint = 50;
		textQRCodeURL.setLayoutData(gd_textQRCodeURL);
		
		lblOtpCdoe = new Label(grpGoogleOtp, SWT.NONE);
		lblOtpCdoe.setText(Messages.NewUserDialog_lblOtpCdoe_text);
		
		textOTPCode = new Text(grpGoogleOtp, SWT.BORDER);
		textOTPCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initUserGroup();
		
		return container;
	}
	
	/**
	 * generate google otp 
	 */
	private void generateGoogleOTP() {
		if(!btnGetOptCode.getSelection()) {
			textSecretKey.setText(""); //$NON-NLS-1$
			textQRCodeURL.setText(""); //$NON-NLS-1$
			
			return;
		}
		secretKey = GoogleAuthManager.getInstance().getSecretKey();
		
		String strEmail = textEMail.getText();
		if("".equals(strEmail)) { //$NON-NLS-1$
			btnGetOptCode.setSelection(false);      
			textEMail.setFocus();
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_7);
			return;
		} else if(!isEmail(strEmail)) {
			btnGetOptCode.setSelection(false);      
			textEMail.setFocus();
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_15);
			return;
		}
		
		String[] strUserDomain = StringUtils.split(strEmail, "@"); //$NON-NLS-1$
		String strURL = GoogleAuthManager.getInstance().getURL(strUserDomain[0], strUserDomain[1], secretKey);
		if(logger.isDebugEnabled()) {
			logger.debug("user is " + strUserDomain[0] + ", domain is " + strUserDomain[1] + ", secretkey is " + secretKey); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			logger.debug("url is " + strURL); //$NON-NLS-1$
		}
		
		textSecretKey.setText(secretKey);
		textQRCodeURL.setText(strURL);
	}
	
	private void initUserGroup() {
		// 유저의 조건에 따른 화면을 초기화 한다.
		if(comboUserGroup != null) comboUserGroup.dispose();
		if(textUserGroup != null) textUserGroup.dispose();
		
		// user를 선택하면 그룹을 입력하도록 합니다.
		if(btnUser.getSelection() || btnDBA.getSelection()) {
			comboUserGroup = new Combo(compositeUserGroup, SWT.READ_ONLY);
			comboUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			// 사용자가 로그인 중일때.
			int groupSeq = -99;
			String groupName = ""; //$NON-NLS-1$
			if(SessionManager.isLogin()) groupSeq = SessionManager.getGroupSeq();
			
			try {
				List<UserGroupDAO> listUserGroup = TadpoleSystem_UserGroupQuery.getGroup();
				for (UserGroupDAO userGroupDAO : listUserGroup) {
					
					if(groupSeq == userGroupDAO.getSeq()) groupName = userGroupDAO.getName();
					comboUserGroup.add(userGroupDAO.getName());
					comboUserGroup.setData(userGroupDAO.getName(), userGroupDAO.getSeq());
				}
				
				if(SessionManager.isLogin()) {
					comboUserGroup.setText(groupName);
					comboUserGroup.setEnabled(false);
				} else {
					comboUserGroup.select(0);
				}
				
				comboUserGroup.setFocus();
			} catch (Exception e) {
				logger.error("initUserGroup", e); //$NON-NLS-1$
			}
		// 그룹을 선택하면 신규 그룹 이름을 입력합니다.
		} else {
			textUserGroup = new Text(compositeUserGroup, SWT.BORDER);
			textUserGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			textUserGroup.setFocus();
		}
		
		compositeUserGroup.layout();
	}
	
	@Override
	protected void okPressed() {
		String strGroupName = "";  //$NON-NLS-1$
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String passwd = StringUtils.trimToEmpty(textPasswd.getText());
		String rePasswd = StringUtils.trimToEmpty(textRePasswd.getText());
		String name = StringUtils.trimToEmpty(textName.getText());
		String questionKey = StringUtils.trimToEmpty((String)comboQuestion.getData(comboQuestion.getSelectionIndex() + comboQuestion.getText()));
		String answer = StringUtils.trimToEmpty(textAnswer.getText());
		
		if(!validation(strEmail, passwd, rePasswd, name, questionKey, answer)) return;
		if(btnGetOptCode.getSelection()) {
			if("".equals(textOTPCode.getText())) { //$NON-NLS-1$
				MessageDialog.openError(getShell(), "Error", Messages.NewUserDialog_40); //$NON-NLS-1$
				textOTPCode.setFocus();
				return;
			}
			if(!GoogleAuthManager.getInstance().isValidate(secretKey, NumberUtils.toInt(textOTPCode.getText()))) {
				MessageDialog.openError(getShell(), "Error", Messages.NewUserDialog_42); //$NON-NLS-1$
				textOTPCode.setFocus();
				return;
			}
		}
		
		// user 입력시 
		UserGroupDAO groupDAO = new UserGroupDAO();
		PublicTadpoleDefine.USER_TYPE userType = PublicTadpoleDefine.USER_TYPE.USER;
		if(btnUser.getSelection()) {
			strGroupName = comboUserGroup.getText();
			groupDAO.setSeq( (Integer)comboUserGroup.getData(strGroupName) );
			
		} else if(btnDBA.getSelection()) {
			strGroupName = comboUserGroup.getText();
			groupDAO.setSeq( (Integer)comboUserGroup.getData(strGroupName) );
			userType = PublicTadpoleDefine.USER_TYPE.DBA;
			
		} else {
			strGroupName = StringUtils.trimToEmpty(textUserGroup.getText());
			
			userType = PublicTadpoleDefine.USER_TYPE.MANAGER;
			// 그룹 등록
			try {
				groupDAO = TadpoleSystem_UserGroupQuery.newUserGroup(strGroupName);
			} catch(Exception e) {
				logger.error(Messages.NewUserDialog_8, e);
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_14, Messages.NewUserDialog_16 + e.getMessage());
				return;
			}
		}
		
		try {
			UserDAO newUserDAO = TadpoleSystem_UserQuery.newUser(strEmail, passwd, name, comboLanguage.getText(), approvalYn.toString(), questionKey, answer, 
					btnGetOptCode.getSelection()?"YES":"NO", textSecretKey.getText()); //$NON-NLS-1$ //$NON-NLS-2$
			
			// user_role 입력.
			TadpoleSystem_UserRole.newUserRole(groupDAO.getSeq(), newUserDAO.getSeq(), userType.toString(), PublicTadpoleDefine.YES_NO.YES.toString(), 
					PublicTadpoleDefine.USER_TYPE.ADMIN.toString());
			
//			if(!ApplicationArgumentUtils.isTestMode()) {
//				MessageDialog.openInformation(getParentShell(), Messages.NewUserDialog_14, Messages.NewUserDialog_21);
//			}
			
			sendEmail(userType, groupDAO.getSeq(), strGroupName, name, strEmail);
			
			MessageDialog.openInformation(null, "Confirm", Messages.NewUserDialog_31); //$NON-NLS-1$
			
		} catch (Exception e) {
			logger.error(Messages.NewUserDialog_8, e);
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_14, e.getMessage());
			return;
		}
		
		super.okPressed();
	}
	
	/**
	 * 
	 * @param userType
	 * @param groupSeq
	 * @param groupName
	 * @param name
	 * @param email
	 */
	private void sendEmail(PublicTadpoleDefine.USER_TYPE userType, int groupSeq, String groupName, String name, String email) {
		try {
			UserDAO userDao = null;
			if(PublicTadpoleDefine.USER_TYPE.MANAGER == userType) {
				userDao = TadpoleSystem_UserQuery.getAdmin();
			} else {
				userDao = TadpoleSystem_UserQuery.getGroupManager(groupSeq);
			}
			
			// manager 에게 메일을 보낸다.
			EmailDTO emailDao = new EmailDTO();
			emailDao.setSubject("Add new Tadpole user."); //$NON-NLS-1$
			// 
			// 그룹, 사용자, 권한.
			// 
			NewUserMailBodyTemplate mailContent = new NewUserMailBodyTemplate();
			String strContent = mailContent.getContent(groupName, name, email);
			emailDao.setContent(strContent);
			emailDao.setTo(userDao.getEmail());
			
			SendEmails sendEmail = new SendEmails(GetPreferenceGeneral.getSessionSMTPINFO());
			sendEmail.sendMail(emailDao);
		} catch(Exception e) {
			logger.error("Error send email", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * validation
	 * 
	 * @param strGroupName
	 * @param strEmail
	 * @param strPass
	 * @param rePasswd
	 * @param name
	 */
	private boolean validation(String strEmail, String strPass, String rePasswd, String name, String questionKey, String answer) {

		if(btnManager != null && btnManager.getSelection()) {
			if("".equals(StringUtils.trimToEmpty(textUserGroup.getText()))) { //$NON-NLS-1$
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_23);
				textUserGroup.setFocus();
				return false;
			}
		}
		
		if("".equals(strEmail)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_7);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_10);
			textPasswd.setFocus();
			return false;
		} else if("".equals(name)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_13);
			textName.setFocus();
			return false;
		} else if(!isEmail(strEmail)) {
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_15);
			textEMail.setFocus();
			return false;
		} else if("".equals(answer)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_26);
			textAnswer.setFocus();
			return false;
		}
		
		if(!strPass.equals(rePasswd)) {
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_17);
			textPasswd.setFocus();
			return false;
		}
		
		//  신규 그룹 입력시 오류 검증
		if(btnManager != null && btnManager.getSelection()) {
			String strGroupName = StringUtils.trimToEmpty(textUserGroup.getText());
			// 동일한 그룹명이 있는 지 검증한다.
			if(TadpoleSystem_UserGroupQuery.isUserGroup(strGroupName)) {
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_25);
				textUserGroup.setFocus();
				return false;
			}
		}
		
		try {
			// 기존 중복 이메일인지 검사합니다.
			if(!TadpoleSystem_UserQuery.isDuplication(strEmail)) {
				MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_9);
				textEMail.setFocus();
				return false;
			}
		} catch(Exception e) {
			logger.error(Messages.NewUserDialog_11, e);
			MessageDialog.openError(getParentShell(), Messages.NewUserDialog_6, Messages.NewUserDialog_12 + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * email검사
	 * 
	 * @param email
	 * @return
	 */
	private static boolean isEmail(String email) {
		Pattern p = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$"); //$NON-NLS-1$
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.NewUserDialog_19,	true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.NewUserDialog_20, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 560);
	}

}
