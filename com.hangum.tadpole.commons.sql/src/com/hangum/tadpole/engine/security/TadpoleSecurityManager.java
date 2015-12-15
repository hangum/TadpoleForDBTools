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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;

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
		
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_lock())) {
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
			return openPasswdDialog(userDB);
		}
		
		return true;
	}
	
	/**
	 * Open password dialog
	 * @param userDB
	 * @return
	 */
	private boolean openPasswdDialog(final UserDBDAO userDB) {
		DBLockDialog dialog = new DBLockDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
		if(Dialog.OK == dialog.open()) {
			SessionManager.setUnlokDB(userDB);
			return true;
		} else {
			return false;
		}
	}

}
