/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.ext.appm.APPMHandler;

/**
 * password dialog
 * 
 * @author hangum
 *
 */
public abstract class PasswordDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(PasswordDialog.class);
	
	protected UserDBDAO userDB;
	protected Text textPassword;
	
	/**
	 * password dialog
	 * 
	 * @param parentShell
	 * @param userDB
	 */
	protected PasswordDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		this.userDB = userDB;
	}

	/**
	 * initialize UI
	 */
	protected void initUI() {
		try {
			String strCacchPassword = TadpoleSQLManager.getPassword(userDB);
			if(strCacchPassword != null) {
				textPassword.setText(strCacchPassword);
			} else {
				Map<String, String> mapAppm = new HashMap<String, String>();
				mapAppm.put("ip", 		userDB.getHost());
				mapAppm.put("port", 	userDB.getPort());
				mapAppm.put("account",	userDB.getUsers());
				
				String strAMMPPassword = APPMHandler.getInstance().getPassword(mapAppm);
				textPassword.setText(strAMMPPassword);
			}
		} catch (Exception e) {
			logger.error("appm error", e);
			MessageDialog.openInformation(getShell(), CommonMessages.get().Error, "APPM interface error :" + e.getMessage());
			
			textPassword.setText("");
		} finally {
			userDB.setPasswd("");
		}
	}
}
