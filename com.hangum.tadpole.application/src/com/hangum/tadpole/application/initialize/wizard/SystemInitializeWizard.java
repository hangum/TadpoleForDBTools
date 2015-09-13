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

import org.eclipse.jface.wizard.Wizard;

import com.hangum.tadpole.application.Messages;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * System Administrator wizard 
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class SystemInitializeWizard extends Wizard {
	protected SystemAdminWizardUseType systemUseType;
	protected SystemAdminWizardPage adminPage;

	public SystemInitializeWizard() {
		setWindowTitle(Messages.SystemAdminWizardPage_3);
	}

	@Override
	public void addPages() {
		
		systemUseType = new SystemAdminWizardUseType();
		addPage(systemUseType);
		
		adminPage = new SystemAdminWizardPage();
		addPage(adminPage);
	}

	@Override
	public boolean performFinish() {
		SystemAdminWizardPageDAO adminDao = adminPage.getUserData();
		
		try {
			UserDAO newUserDAO = TadpoleSystem_UserQuery.newUser(PublicTadpoleDefine.INPUT_TYPE.NORMAL.toString(),
					adminDao.getEmail(), Utils.getUniqueDigit(7), PublicTadpoleDefine.YES_NO.YES.name(),
					adminDao.getPasswd(), 	
					PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN.toString(),
					"Tadpole System Admin", "en", PublicTadpoleDefine.YES_NO.YES.name(), PublicTadpoleDefine.YES_NO.NO.name(), ""); //$NON-NLS-1$ //$NON-NLS-2$
			
			insertUserPreferenceValue(newUserDAO, PreferenceDefine.SMTP_HOST_NAME, 	adminDao.getsMTPServer());
			insertUserPreferenceValue(newUserDAO, PreferenceDefine.SMTP_PORT, 		adminDao.getPort());
			insertUserPreferenceValue(newUserDAO, PreferenceDefine.SMTP_EMAIL, 		adminDao.getsMTPEmail());
			insertUserPreferenceValue(newUserDAO, PreferenceDefine.SMTP_PASSWD, 	adminDao.getsMTPPasswd());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * insert key, value
	 * 
	 * @param key
	 * @param use
	 * @throws Exception
	 */
	private static void insertUserPreferenceValue(UserDAO userDao, String key, String use) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(userDao.getSeq());
		
		userInfoData.setName(key);
		userInfoData.setValue0(use);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
	}

}
