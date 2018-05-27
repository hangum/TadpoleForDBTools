/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.security;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.session.manager.SessionManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Tadpole Security manager
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 24.
 *
 */
public class TadpoleSecurityManager {

	private static TadpoleSecurityManager instance = new TadpoleSecurityManager(); 
	
	/**
	 * security manager
	 */
	private TadpoleSecurityManager() {}
	
	public static TadpoleSecurityManager getInstance() {
		return instance;
	}
	
	/**
	 * Is db lock status?
	 * @param userDB
	 * @return
	 */
	public boolean isLockStatus(final UserDBDAO userDB) {
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_lock())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * DB is lock?
	 * 
	 * @param userDB
	 * @return
	 */
	public boolean isLock(final UserDBDAO userDB) {
		if(userDB == null) return false;
		
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_lock())		// 디비가 잠겨 있거나  
				|| PublicTadpoleDefine.YES_NO.NO.name().equals(GetAdminPreference.getSaveDBPassword()) 		// 패스워드를 저장하지 않거나
				|| !PublicTadpoleDefine.YES_NO.NO.name().equals(GetAdminPreference.getConnectionAskType()) 	// 어드민이 디비 연결시 마다 묻도록 했거나
		) {
			if(!SessionManager.isUnlockDB(userDB)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * If DB lock than open dialog
	 * 
	 * @param userDB
	 * @return
	 */
	public boolean ifLockOpenDialog(final UserDBDAO userDB) {
		if(!isLock(userDB)) {
			return openAskDialog(userDB);
		}
		
		return true;
	}
	
	/**
	 * 패스워드, otp, 패드워드 + otp 다이얼로그를 열지 결정한다.
	 * 
	 * @param userDB
	 * @return
	 */
	private boolean openAskDialog(final UserDBDAO userDB) {
		// SQLite은 패스워드가 없으므로..
		if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
			SessionManager.setUnlokDB(userDB);
			return true;
		}
		
		
		// PublicTadpoleDefine#DB_CONNECTION_ASK  NO, PASSWORD, OTP, PASSWORD_OTP
		final String strConnectionASK = GetAdminPreference.getConnectionAskType();
		if(!"NO".equals(strConnectionASK)) {
			// password
			if(strConnectionASK.equals(PublicTadpoleDefine.DB_CONNECTION_ASK.PASSWORD.toString())) {
				return checkPasswd(userDB);	
			} else if(strConnectionASK.equals(PublicTadpoleDefine.DB_CONNECTION_ASK.OTP.toString())) {
				return checkOTP(userDB);
			} else if(strConnectionASK.equals(PublicTadpoleDefine.DB_CONNECTION_ASK.PASSWORD_OTP.toString())) {
				return checkPasswordOTP(userDB);
			}
			
			return true;
		} else {
			return checkPasswd(userDB);
		}
	}
	
	/**
	 * check password dialog
	 * 
	 * @param userDB
	 * @return
	 */
	private boolean checkPasswordOTP(final UserDBDAO userDB) {
		DBPasswordAndOTPDialog dialog = new DBPasswordAndOTPDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
		if(Dialog.OK == dialog.open()) {
			SessionManager.setUnlokDB(userDB);
			return true;
		} else {
			userDB.setPasswd(Utils.getUniqueDigit(7));
			return false;
		}
	}
	
	/**
	 * check password dialog
	 * 
	 * @param userDB
	 * @return
	 */
	private boolean checkOTP(final UserDBDAO userDB) {
		if(LoadConfigFile.isUseOPT()) {
			if(PublicTadpoleDefine.OTP_METHOD.APP_VERIFICATION.name().equalsIgnoreCase(LoadConfigFile.otpMethod())) {
				OTPValidation validation = new OTPValidation();
				if(false == validation.validation(SessionManager.getEMAIL())) {
					userDB.setPasswd(Utils.getUniqueDigit(7));
					return false;
				} else {
					SessionManager.setUnlokDB(userDB);
					return true;
				}
			} else {
				OTPInputDialog dialog = new OTPInputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SessionManager.getEMAIL(), SessionManager.getOTPSecretKey());
				if(Dialog.OK == dialog.open()) {
					SessionManager.setUnlokDB(userDB);
					return true;
				} else {
					userDB.setPasswd(Utils.getUniqueDigit(7));
					return false;
				}
			}
		}
		
		SessionManager.setUnlokDB(userDB);
		return true;
	}
	
	/**
	 * check password dialog
	 * 
	 * @param userDB
	 * @return
	 */
	private boolean checkPasswd(final UserDBDAO userDB) {
		DBPasswordDialog dialog = new DBPasswordDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
		if(Dialog.OK == dialog.open()) {
			SessionManager.setUnlokDB(userDB);
			return true;
		} else {
			userDB.setPasswd(Utils.getUniqueDigit(7));
			return false;
		}
	}

}
