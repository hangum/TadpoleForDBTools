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

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.utils.LicenseDAO;
import com.hangum.tadpole.engine.utils.LicenseValidator;
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
	 * system message
	 */
	protected void preLogin() {
		LicenseDAO licenseDAO = LicenseValidator.getLicense();
		if(licenseDAO.isEnterprise() && !licenseDAO.isValidate()) {
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, licenseDAO.getMsg());
		}
	}
	
	@Override
	public boolean close() {
		//  로그인이 안되었을 경우 로그인 창이 남아 있도록...(https://github.com/hangum/TadpoleForDBTools/issues/31)
		if(!SessionManager.isLogin()) return false;
		
		return super.close();
	}
}
