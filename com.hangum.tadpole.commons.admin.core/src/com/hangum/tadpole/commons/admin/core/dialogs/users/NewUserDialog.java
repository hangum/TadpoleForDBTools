/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.dialogs.users;

import org.apache.commons.lang.StringEscapeUtils;
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

import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.googleauth.GoogleAuthManager;
import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.commons.libs.core.mails.template.NewUserMailBodyTemplate;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.initialize.AddDefaultSampleDBToUser;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.get.GetAdminPreference;

/**
 * Add new user Dialog
 *  
 * @author hangum
 * @since 2014.12.01
 *
 */
public class NewUserDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(NewUserDialog.class);
	private boolean isAdmin = false;
	
	private Text textEMail;
	private Text textPasswd;
	private Text textRePasswd;
	private Text textName;
	
	private Combo comboLanguage;
	
	/** OTP code */
	private String secretKey = ""; //$NON-NLS-1$
	private Button btnGetOptCode;
	private Text textSecretKey;
	private Label labelQRCodeURL;
	private Label lblOtpCdoe;
	private Text textOTPCode;
	
	private UserDAO userDao = new UserDAO();
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewUserDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * @param shell
	 * @param b
	 */
	public NewUserDialog(Shell parentShell, boolean isAdmin) {
		super(parentShell);
		
		this.isAdmin = isAdmin;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().NewUserDialog_0);
		
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
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
		
		Label lblIdemail = new Label(container, SWT.NONE);
		lblIdemail.setText(Messages.get().NewUserDialog_1);
		
		textEMail = new Text(container, SWT.BORDER);
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText(Messages.get().NewUserDialog_2);
		
		textPasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRePassword = new Label(container, SWT.NONE);
		lblRePassword.setText(Messages.get().NewUserDialog_3);
		
		textRePasswd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		textRePasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(Messages.get().NewUserDialog_4);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLanguage = new Label(container, SWT.NONE);
		lblLanguage.setText(Messages.get().NewUserDialog_lblLanguage_text);
		
		comboLanguage = new Combo(container, SWT.READ_ONLY);
		comboLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboLanguage.add("ko"); //$NON-NLS-1$
		comboLanguage.add("en_us"); //$NON-NLS-1$
		comboLanguage.select(1);
		
		btnGetOptCode = new Button(container, SWT.CHECK);
		btnGetOptCode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateGoogleOTP();
			}
		});
		btnGetOptCode.setText(Messages.get().NewUserDialog_btnCheckButton_text);
		
		Label lblWhatIsQRCode = new Label(container, SWT.NONE);
		lblWhatIsQRCode.setText(Messages.get().NewUserDialog_5);
		lblWhatIsQRCode.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Group grpGoogleOtp = new Group(container, SWT.NONE);
		grpGoogleOtp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		grpGoogleOtp.setText(Messages.get().NewUserDialog_grpGoogleOtp_text);
		grpGoogleOtp.setLayout(new GridLayout(2, false));
		
		Label lblSecretKey = new Label(grpGoogleOtp, SWT.NONE);
		lblSecretKey.setText(Messages.get().NewUserDialog_lblAccessKey_1_text);
		
		textSecretKey = new Text(grpGoogleOtp, SWT.BORDER | SWT.READ_ONLY);
		textSecretKey.setEditable(false);
		textSecretKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblQrcodeUrl = new Label(grpGoogleOtp, SWT.NONE);
		lblQrcodeUrl.setText(Messages.get().NewUserDialog_lblQrcodeUrl_text);
		
		labelQRCodeURL = new Label(grpGoogleOtp, SWT.NONE);
		labelQRCodeURL.setText(""); //$NON-NLS-1$
		labelQRCodeURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelQRCodeURL.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		lblOtpCdoe = new Label(grpGoogleOtp, SWT.NONE);
		lblOtpCdoe.setText(Messages.get().NewUserDialog_lblOtpCdoe_text);
		
		textOTPCode = new Text(grpGoogleOtp, SWT.BORDER);
		textOTPCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEMail.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * generate google otp 
	 */
	private void generateGoogleOTP() {
		if(!btnGetOptCode.getSelection()) {
			getShell().setSize(370, 240);
			textSecretKey.setText(""); //$NON-NLS-1$
			labelQRCodeURL.setText(""); //$NON-NLS-1$
			
			return;
		}
		
		String strEmail = textEMail.getText();
		if("".equals(strEmail)) { //$NON-NLS-1$
			getShell().setSize(370, 240);
			btnGetOptCode.setSelection(false);      
			textEMail.setFocus();
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_7);
			return;
		} else if(!ValidChecker.isValidEmailAddress(strEmail)) {
			getShell().setSize(370, 240);
			btnGetOptCode.setSelection(false);      
			textEMail.setFocus();
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_15);
			return;
		}
		getShell().setSize(380, 370);
		secretKey = GoogleAuthManager.getInstance().getSecretKey();
		textSecretKey.setText(secretKey);
		
		String[] strUserDomain = StringUtils.split(strEmail, "@"); //$NON-NLS-1$
		String strURL = GoogleAuthManager.getInstance().getURL(strUserDomain[0], strUserDomain[1], secretKey);
		if(logger.isDebugEnabled()) {
			logger.debug("user is " + strUserDomain[0] + ", domain is " + strUserDomain[1] + ", secretkey is " + secretKey); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			logger.debug("url is " + strURL); //$NON-NLS-1$
		}
		
		strURL = StringEscapeUtils.escapeHtml(strURL);
		labelQRCodeURL.setText(String.format("<a href='%s' target='_blank'>Show QRCode(Only support Google Chrome)</a>", strURL)); //$NON-NLS-1$
	}
	
	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String passwd = StringUtils.trimToEmpty(textPasswd.getText());
		String rePasswd = StringUtils.trimToEmpty(textRePasswd.getText());
		String name = StringUtils.trimToEmpty(textName.getText());
		
		if(!validation(strEmail, passwd, rePasswd, name)) return;
		if(btnGetOptCode.getSelection()) {
			if("".equals(textOTPCode.getText())) { //$NON-NLS-1$
				MessageDialog.openError(getShell(), Messages.get().NewUserDialog_24, Messages.get().NewUserDialog_40);
				textOTPCode.setFocus();
				return;
			}
			if(!GoogleAuthManager.getInstance().isValidate(secretKey, NumberUtils.toInt(textOTPCode.getText()))) {
				MessageDialog.openError(getShell(), "Error", Messages.get().NewUserDialog_42); //$NON-NLS-1$
				textOTPCode.setFocus();
				return;
			}
		}
		
		try {
			/**
			 * 어드민의 허락이 필요하면 디비에 등록할때는 NO를 입력, 필요치 않으면 YES를 입력.
			 */
			String approvalYn = GetAdminPreference.getNewUserPermit();//ApplicationArgumentUtils.getNewUserPermit()?PublicTadpoleDefine.YES_NO.NO.name():PublicTadpoleDefine.YES_NO.YES.name();
			String isEmamilConrim = PublicTadpoleDefine.YES_NO.NO.name();
			
			SMTPDTO smtpDto = new SMTPDTO();
			try {
				smtpDto = GetAdminPreference.getSessionSMTPINFO();
			} catch(Exception e) {
				// igonre exception
			}
			if(isAdmin || "".equals(smtpDto.getEmail())) { //$NON-NLS-1$
				isEmamilConrim 	= PublicTadpoleDefine.YES_NO.YES.name();
			}
			
			String strEmailConformKey = Utils.getUniqueDigit(7);
			userDao = TadpoleSystem_UserQuery.newUser(
					PublicTadpoleDefine.INPUT_TYPE.NORMAL.toString(),
					strEmail, strEmailConformKey, isEmamilConrim, 
					passwd, 
					PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString(),
					name, comboLanguage.getText(), approvalYn,  
					btnGetOptCode.getSelection()?"YES":"NO",  //$NON-NLS-1$ //$NON-NLS-2$
					textSecretKey.getText(),
					"*"); //$NON-NLS-1$ //$NON-NLS-2$
		
			boolean isSentMail = false;
			if(!"".equals(smtpDto.getEmail())) { //$NON-NLS-1$
				sendEmailAccessKey(name, strEmail, strEmailConformKey);
				isSentMail = true;
			}
			
			try {
				AddDefaultSampleDBToUser.addUserDefaultDB(userDao.getSeq(), userDao.getEmail());
			} catch (Exception e) {
				logger.error("Sample db copy error", e); //$NON-NLS-1$
			}
			
			if(isSentMail) MessageDialog.openInformation(null, Messages.get().NewUserDialog_14, Messages.get().NewUserDialog_31);
			else MessageDialog.openInformation(null, Messages.get().NewUserDialog_14, Messages.get().NewUserDialog_29); //$NON-NLS-1$
			
		} catch (Exception e) {
			logger.error(Messages.get().NewUserDialog_8, e);
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_14, e.getMessage());
			return;
		}
		
		super.okPressed();
	}
	
	/**
	 * send email sccess key
	 * 
	 * @param name
	 * @param email
	 * @param strConfirmKey
	 */
	private void sendEmailAccessKey(String name, String email, String strConfirmKey) {
		try {
			SMTPDTO smtpDto = GetAdminPreference.getSessionSMTPINFO();
			
			// manager 에게 메일을 보낸다.
			EmailDTO emailDao = new EmailDTO();
			emailDao.setSubject(Messages.get().NewUserDialog_32);
			// 
			// 그룹, 사용자, 권한.
			// 
			NewUserMailBodyTemplate mailContent = new NewUserMailBodyTemplate();
			String strContent = mailContent.getContent(name, email, strConfirmKey);
			emailDao.setContent(strContent);
			emailDao.setTo(email);
			
			SendEmails sendEmail = new SendEmails(smtpDto);
			sendEmail.sendMail(emailDao);
		} catch(Exception e) {
			logger.error(String.format("New user key sening error name %s, email %s, confirm key %s", name, email, strConfirmKey), e); //$NON-NLS-1$
			
			MessageDialog.openError(getShell(), Messages.get().NewUserDialog_24, Messages.get().NewUserDialog_34);
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
	private boolean validation(String strEmail, String strPass, String rePasswd, String name) {

		if("".equals(strEmail)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_7);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_10);
			textPasswd.setFocus();
			return false;
		} else if("".equals(name)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_13);
			textName.setFocus();
			return false;
		} else if(!ValidChecker.isValidEmailAddress(strEmail)) {
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_15);
			textEMail.setFocus();
			return false;
		} else if(StringUtils.length(strPass) < 5) {
			MessageDialog.openError(getShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_25);
			textPasswd.setFocus();
			return false;
		}
		
		if(!strPass.equals(rePasswd)) {
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_17);
			textPasswd.setFocus();
			return false;
		}
				
		try {
			// 기존 중복 이메일인지 검사합니다.
			if(!TadpoleSystem_UserQuery.isDuplication(strEmail)) {
				MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_9);
				textEMail.setFocus();
				return false;
			}
		} catch(Exception e) {
			logger.error(Messages.get().NewUserDialog_11, e);
			MessageDialog.openError(getParentShell(), Messages.get().NewUserDialog_6, Messages.get().NewUserDialog_12 + e.getMessage());
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
		createButton(parent, IDialogConstants.OK_ID, Messages.get().NewUserDialog_19,	true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().NewUserDialog_20, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(370, 242);
	}
	
	/**
	 * Return user dao
	 * @return
	 */
	public UserDAO getUserDao() {
		return userDao;
	}

}
