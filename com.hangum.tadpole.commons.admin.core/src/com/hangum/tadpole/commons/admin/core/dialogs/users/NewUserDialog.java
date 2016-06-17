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

import java.util.Locale;

import org.apache.commons.io.IOUtils;
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
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.initialize.AddDefaultSampleDBToUser;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.utils.TimeZoneUtil;
import com.hangum.tadpole.preference.define.GetAdminPreference;

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
	private Combo comboTimezone;
	
	/** OTP code */
	private String secretKey = ""; //$NON-NLS-1$
	private Button btnGetOptCode;
	private Text textSecretKey;
	private Label labelQRCodeURL;
	private Label lblOtpCdoe;
	private Text textOTPCode;
	
	private UserDAO userDao = new UserDAO();
	private Composite composite;
	private Label label;
	private Button btnServiceContract;
	private Button btnPersonContract;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
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
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);		
		this.isAdmin = isAdmin;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().NewUserDialog_0);
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
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.numColumns = 2;
		
		Label lblIdemail = new Label(container, SWT.NONE);
		lblIdemail.setText(Messages.get().email);
		
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
		lblName.setText(Messages.get().Name);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLanguage = new Label(container, SWT.NONE);
		lblLanguage.setText(Messages.get().NewUserDialog_lblLanguage_text);
		
		comboLanguage = new Combo(container, SWT.READ_ONLY);
		comboLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboLanguage.add(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
		comboLanguage.add(Locale.KOREAN.getDisplayLanguage(Locale.KOREAN));
		comboLanguage.setData(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), Locale.ENGLISH);
		comboLanguage.setData(Locale.KOREAN.getDisplayLanguage(Locale.KOREAN), Locale.KOREAN);
		comboLanguage.select(0);
		
		Label lblTimezone = new Label(container, SWT.NONE);
		lblTimezone.setText(Messages.get().Timezone);
		
		comboTimezone = new Combo(container, SWT.READ_ONLY);
		comboTimezone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (String timzon : TimeZoneUtil.getTimezoneList()) {
			comboTimezone.add(timzon);
		}
		comboTimezone.setText(PublicTadpoleDefine.DEFAULT_TIME_ZONE);
		
		label = new Label(container, SWT.NONE);
		label.setText(Messages.get().Agreement);
		
		composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnServiceContract = new Button(composite, SWT.CHECK);
		btnServiceContract.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!btnServiceContract.getSelection()) return;
				
				try {
					String strContract = IOUtils.toString(NewUserDialog.class.getResourceAsStream("serviceContract.txt"));
					ServiceContractDialog dialog = new ServiceContractDialog(getShell(), Messages.get().TermsOfService, strContract);
					dialog.open();
				} catch(Exception e3) {
					logger.error("Doesn't read service contract", e3);
				}
			}
		});
		btnServiceContract.setText(Messages.get().TermsOfService);
		
		btnPersonContract = new Button(composite, SWT.CHECK);
		btnPersonContract.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!btnPersonContract.getSelection()) return;
				
				try {
					String strContract = IOUtils.toString(NewUserDialog.class.getResourceAsStream("personServiceContract.txt"));
					ServiceContractDialog dialog = new ServiceContractDialog(getShell(), Messages.get().PrivacyTermsandConditions, strContract);
					dialog.open();
				} catch(Exception e3) {
					logger.error("Doesn't read service contract", e3);
				}
			}
		});
		btnPersonContract.setText(Messages.get().PrivacyTermsandConditions);
		
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
			getShell().setSize(370, 294);
			textSecretKey.setText(""); //$NON-NLS-1$
			labelQRCodeURL.setText(""); //$NON-NLS-1$
			
			return;
		}
		
		String strEmail = textEMail.getText();
		if("".equals(strEmail)) { //$NON-NLS-1$
			getShell().setSize(370, 294);
			btnGetOptCode.setSelection(false);      
			textEMail.setFocus();
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_7);
			return;
		} else if(!ValidChecker.isValidEmailAddress(strEmail)) {
			getShell().setSize(370, 294);
			btnGetOptCode.setSelection(false);      
			textEMail.setFocus();
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_15);
			return;
		}
		getShell().setSize(380, 412);
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
		if(!btnServiceContract.getSelection()) {
			MessageDialog.openError(getShell(), Messages.get().Warning, Messages.get().PlzConfirmTermsService);
			return;
		} else if(!btnPersonContract.getSelection()) {
			MessageDialog.openError(getShell(), Messages.get().Warning, Messages.get().PlzConfirmTermsService);
			return;
		}
		
		if(btnGetOptCode.getSelection()) {
			if("".equals(textOTPCode.getText())) { //$NON-NLS-1$
				MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().NewUserDialog_40);
				textOTPCode.setFocus();
				return;
			}
			if(!GoogleAuthManager.getInstance().isValidate(secretKey, NumberUtils.toInt(textOTPCode.getText()))) {
				MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().NewUserDialog_42); //$NON-NLS-1$
				textOTPCode.setFocus();
				return;
			}
		}
		
		try {
			/**
			 * 어드민의 허락이 필요하면 디비에 등록할때는 NO를 입력, 필요치 않으면 YES를 입력.
			 */
			String approvalYn = GetAdminPreference.getNewUserPermit();
			String isEmamilConrim = PublicTadpoleDefine.YES_NO.NO.name();
			
			SMTPDTO smtpDto = new SMTPDTO();
			try {
				smtpDto = GetAdminPreference.getSessionSMTPINFO();
			} catch(Exception e) {
				// igonre exception
			}
			if(isAdmin || !smtpDto.isValid()) { //$NON-NLS-1$
				isEmamilConrim 	= PublicTadpoleDefine.YES_NO.YES.name();
			}
			
			String strEmailConformKey = Utils.getUniqueDigit(7);
			Locale locale = (Locale)comboLanguage.getData(comboLanguage.getText());
			userDao = TadpoleSystem_UserQuery.newUser(
					PublicTadpoleDefine.INPUT_TYPE.NORMAL.toString(),
					strEmail, strEmailConformKey, isEmamilConrim, 
					passwd, 
					PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString(),
					name, 
					locale.toLanguageTag(), 
					comboTimezone.getText(),
					approvalYn,  
					btnGetOptCode.getSelection()?"YES":"NO",  //$NON-NLS-1$ //$NON-NLS-2$
					textSecretKey.getText(),
					"*"); //$NON-NLS-1$ //$NON-NLS-2$
		
			boolean isSentMail = false;
			if(smtpDto.isValid()) {
				sendEmailAccessKey(name, strEmail, strEmailConformKey);
				isSentMail = true;
			}
			
			try {
				AddDefaultSampleDBToUser.addUserDefaultDB(userDao.getSeq(), userDao.getEmail());
			} catch (Exception e) {
				logger.error("Sample db copy error", e); //$NON-NLS-1$
			}
			
			if(isSentMail) MessageDialog.openInformation(null, Messages.get().Confirm, String.format(Messages.get().NewUserDialog_31, strEmail));
			else MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().NewUserDialog_29); //$NON-NLS-1$
			
		} catch (Exception e) {
			logger.error(Messages.get().NewUserDialog_8, e);
			MessageDialog.openError(getParentShell(), Messages.get().Confirm, e.getMessage());
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
			
//			if(logger.isDebugEnabled()) logger.debug(strContent);
			
			emailDao.setContent(strContent);
			emailDao.setTo(email);
			
			SendEmails sendEmail = new SendEmails(smtpDto);
			sendEmail.sendMail(emailDao);
		} catch(Exception e) {
			logger.error(String.format("New user key sening error name %s, email %s, confirm key %s", name, email, strConfirmKey), e); //$NON-NLS-1$
			
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().NewUserDialog_34);
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
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_7);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_10);
			textPasswd.setFocus();
			return false;
		} else if("".equals(name)) { //$NON-NLS-1$
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_13);
			textName.setFocus();
			return false;
		} else if(!ValidChecker.isValidEmailAddress(strEmail)) {
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_15);
			textEMail.setFocus();
			return false;
		} else if(!ValidChecker.isSimplePasswordChecker(strPass)) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().NewUserDialog_25);
			textPasswd.setFocus();
			return false;
		} else if("".equals(rePasswd)) {
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_10);
			textRePasswd.setFocus();
			return false;
		}
		
		if(!strPass.equals(rePasswd)) {
			MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_17);
			textPasswd.setFocus();
			return false;
		}
				
		try {
			// 기존 중복 이메일인지 검사합니다.
			if(!TadpoleSystem_UserQuery.isDuplication(strEmail)) {
				MessageDialog.openWarning(getParentShell(), Messages.get().Warning, Messages.get().NewUserDialog_9);
				textEMail.setFocus();
				return false;
			}
		} catch(Exception e) {
			logger.error("new user", e);
			MessageDialog.openError(getParentShell(), Messages.get().Error, Messages.get().NewUserDialog_12 + e.getMessage());
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
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Save,	true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancle, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(370, 294);
	}
	
	/**
	 * Return user dao
	 * @return
	 */
	public UserDAO getUserDao() {
		return userDao;
	}

}
