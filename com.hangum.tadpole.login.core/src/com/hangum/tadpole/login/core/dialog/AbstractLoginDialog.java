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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;
import com.hangum.tadpole.commons.libs.core.dao.LicenseDAO;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.LicenseValidator;
import com.hangum.tadpole.commons.util.DateUtil;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.LDAPUtil;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
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
public class AbstractLoginDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AbstractLoginDialog.class);
	
	protected int ID_NEW_USER	 	= IDialogConstants.CLIENT_ID 	+ 1;
	protected int ID_FINDPASSWORD 	= IDialogConstants.CLIENT_ID 	+ 2;

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
	 * LDAP Login
	 * 
	 * @param strEmail
	 * @param strPass
	 */
	protected void ldapLogin(String strEmail, String strPass) throws TadpoleAuthorityException {
		LDAPUtil.ldapLogin(strEmail, strPass, GetAdminPreference.getLDAPURL(), GetAdminPreference.getLDAPAuthentication(), GetAdminPreference.getLDAPUser());
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
