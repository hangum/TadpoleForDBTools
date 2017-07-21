/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.login.core.dialog;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.admin.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;
import com.hangum.tadpole.commons.libs.core.dao.LicenseDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.LicenseValidator;
import com.hangum.tadpole.commons.util.DateUtil;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.IPUtil;
import com.hangum.tadpole.commons.util.LDAPUtil;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.security.OTPInputDialog;
import com.hangum.tadpole.login.core.message.LoginDialogMessages;
import com.hangum.tadpole.preference.define.AdminPreferenceDefine;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.preference.dialogs.user.ChangePasswordDialog;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Abstract login dialog
 * 
 * @author hangum
 *
 */
public abstract class AbstractLoginDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AbstractLoginDialog.class);
	
	protected int ID_NEW_USER	 	= IDialogConstants.CLIENT_ID 	+ 1;
	protected int ID_FINDPASSWORD 	= IDialogConstants.CLIENT_ID 	+ 2;
	
	protected Text textEMail;
	protected Text textPasswd;
	protected Combo comboLanguage;

	protected AbstractLoginDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(String.format("%s", SystemDefine.NAME)); //$NON-NLS-1$
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}
	
	/**
	 * validation
	 * 
	 * @param strEmail
	 * @param strPass
	 */
	protected boolean validation(String strEmail, String strPass) {
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
	 * LDAP Login
	 * 
	 * @param strEmail
	 * @param strPass
	 */
	protected void ldapLogin(String strEmail, String strPass) throws TadpoleAuthorityException {
		LDAPUtil.getInstance().ldapLogin(strEmail, strPass);
	}
	
	/**
	 * 허용가능한 ip 검사 
	 * 
	 * @param userDao
	 * @param strAllowIP
	 * @param strUserIP
	 * @return
	 */
	protected boolean isAllowIP(UserDAO userDao, String strAllowIP, String strUserIP) {
		boolean isAllow = IPUtil.ifFilterString(strAllowIP, strUserIP);
		if(logger.isDebugEnabled())logger.debug(LoginDialogMessages.get().LoginDialog_21 + userDao.getEmail() + LoginDialogMessages.get().LoginDialog_22 + strAllowIP + LoginDialogMessages.get().LoginDialog_23+ RequestInfoUtils.getRequestIP());
		if(!isAllow) {
			logger.error(LoginDialogMessages.get().LoginDialog_21 + userDao.getEmail() + LoginDialogMessages.get().LoginDialog_22 + strAllowIP + LoginDialogMessages.get().LoginDialog_26+ RequestInfoUtils.getRequestIP());
			saveLoginHistory(userDao.getSeq(), strUserIP, PublicTadpoleDefine.YES_NO.NO.name(), String.format("IP : Access ip %s, User IP %s", strAllowIP, strUserIP));
			
			MessageDialog.openWarning(getParentShell(), CommonMessages.get().Warning, LoginDialogMessages.get().LoginDialog_28);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 사용자 otp
	 * 
	 * @param userDao
	 * @param strUserIP
	 * @return
	 */
	protected boolean isQuestOTP(UserDAO userDao, String strUserIP) {
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDao.getUse_otp())) {
			if(LoadConfigFile.isUseOPT()) {
				OTPInputDialog otpDialog = new OTPInputDialog(getShell(), userDao.getEmail(), userDao.getOtp_secret());
				if(Dialog.CANCEL == otpDialog.open()) {
					saveLoginHistory(userDao.getSeq(), strUserIP, PublicTadpoleDefine.YES_NO.NO.name(), String.format("OTP Fail"));
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 신규사용자. 
	 */
	protected void newUser() {
		NewUserDialog newUser = new NewUserDialog(getParentShell());
		if(Dialog.OK == newUser.open()) {
			String strEmail = newUser.getUserDao().getEmail();
			textEMail.setText(strEmail);
			textPasswd.setFocus();
		}
	}
	
	/**
	 * 사용자 패스워드 찾기
	 */
	protected void findPassword() {
		FindPasswordDialog dlg = new FindPasswordDialog(getShell(), textEMail.getText());
		dlg.open();
	}
	
	/**
	 * 로그인 사유
	 * 
	 * @param userSeq
	 * @param strIP
	 * @param strYesNO
	 * @param strReason
	 */
	protected void saveLoginHistory(int userSeq, String strIP, String strYesNO, String strReason) {
		if(LicenseValidator.getLicense().isValidate()) {
			TadpoleSystem_UserQuery.saveLoginHistory(userSeq, strIP, strYesNO, strReason);
		}
	}
	
	/**
	 * 로그인시 패스워드가 틀림.
	 * @param strEmail
	 * @param ip_servletRequest
	 * @param strYesNO
	 * @param strReason
	 */
	protected void saveLoginHistory(String strEmail, String ip_servletRequest, String strYesNO, String strReason) {
		try {
			List<UserDAO> listUser = TadpoleSystem_UserQuery.findExistUser(strEmail);
			if(!listUser.isEmpty()) {
				saveLoginHistory(listUser.get(0).getSeq(), ip_servletRequest, strYesNO, strReason);
			}
		} catch (Exception e) {
			logger.error("get userlist", e);
		}
		
	}

	
	/**
	 * system message
	 */
	protected void preLogin(UserDAO userDao) {
		LicenseDAO licenseDAO = LicenseValidator.getLicense();
		if(licenseDAO.isEnterprise() && !licenseDAO.isValidate()) {
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, licenseDAO.getMsg());
		}

		// 일반적인 상황이면 패스워드 교체 주기인지 검사한다.
		if(StringUtils.equals(GetAdminPreference.getLoginMethod(), AdminPreferenceDefine.SYSTEM_LOGIN_METHOD_VALUE)) {
			// 패스워드 수정 교체주기가 넘어서 있는지 점검한다.
			int intMaxDay = Integer.parseInt(GetAdminPreference.getPasswdDateLimit());
			long longChangedTime = DateUtil.afterMonthToMillis(userDao.getChanged_passwd_time().getTime(), intMaxDay);
			if(System.currentTimeMillis() > longChangedTime) {
				if(logger.isDebugEnabled()) logger.debug("Must be changed password. " + new Date(longChangedTime));
				ChangePasswordDialog dialog = new ChangePasswordDialog(getShell());
				dialog.open();
			} else {
				if(logger.isDebugEnabled()) logger.debug("Doesnot chaged password. password chaged date is " + new Date(longChangedTime));
			}
		}
	}
	
	@Override
	public boolean close() {
		//  로그인이 안되었을 경우 로그인 창이 남아 있도록...(https://github.com/hangum/TadpoleForDBTools/issues/31)
		if(!SessionManager.isLogin()) return false;
		
		return super.close();
	}
}
