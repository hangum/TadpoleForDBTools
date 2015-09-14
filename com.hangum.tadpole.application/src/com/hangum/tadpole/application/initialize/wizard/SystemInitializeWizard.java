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
package com.hangum.tadpole.application.initialize.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.hangum.tadpole.application.Messages;
import com.hangum.tadpole.application.initialize.wizard.dao.SystemAdminWizardUserDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;

/**
 * System Administrator wizard 
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class SystemInitializeWizard extends Wizard {
	protected SystemAdminWizardUseTypePage systemUseType;
	protected SystemAdminWizardAddUserPage addUserPage;

	public SystemInitializeWizard() {
		setWindowTitle(Messages.SystemAdminWizardPage_3);
	}

	@Override
	public void addPages() {
		
		systemUseType = new SystemAdminWizardUseTypePage();
		addPage(systemUseType);
		
		addUserPage = new SystemAdminWizardAddUserPage();
		addPage(addUserPage);
	}
	
	@Override
	public boolean canFinish() {
		if(PublicTadpoleDefine.SYSTEM_USE_GROUP.PERSONAL.name().equals(systemUseType.getUseType())) {
			return true;
		}
		
		return super.canFinish();
	}

	@Override
	public boolean performFinish() {
		SystemAdminWizardUserDAO adminDao = addUserPage.getUserData();
		
		try {
//			UserDAO newUserDAO = 
					TadpoleSystem_UserQuery.newUser(PublicTadpoleDefine.INPUT_TYPE.NORMAL.toString(),
					adminDao.getEmail(), Utils.getUniqueDigit(7), PublicTadpoleDefine.YES_NO.YES.name(),
					adminDao.getPasswd(), 	
					PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN.toString(),
					"Tadpole System Admin", "en", PublicTadpoleDefine.YES_NO.YES.name(), PublicTadpoleDefine.YES_NO.NO.name(), ""); //$NON-NLS-1$ //$NON-NLS-2$
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
