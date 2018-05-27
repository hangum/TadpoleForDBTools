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

import java.sql.Timestamp;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;

import com.hangum.tadpole.application.initialize.Messages;
import com.hangum.tadpole.application.initialize.wizard.dao.SystemAdminWizardUserDAO;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystemQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * System Administrator wizard 
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class SystemInitializeWizard extends Wizard {
	private static final Logger logger = Logger.getLogger(SystemInitializeWizard.class);
	
	protected SystemAdminTermsPage termPage;
	protected SystemAdminWizardUseTypePage systemUseType;
	protected SystemAdminWizardDefaultUserPage addUserPage;

	public SystemInitializeWizard() {
		setWindowTitle(Messages.get().SystemAdminWizardPage_3);
	}

	@Override
	public void addPages() {
		termPage = new SystemAdminTermsPage();
		addPage(termPage);
		
		systemUseType = new SystemAdminWizardUseTypePage();
		addPage(systemUseType);
		
		addUserPage = new SystemAdminWizardDefaultUserPage();
		addPage(addUserPage);
	}
	
	@Override
	public boolean canFinish() {
		if(termPage.getAggree()) {
			if(PublicTadpoleDefine.SYSTEM_USE_GROUP.PERSONAL.name().equals(systemUseType.getUseType())) {
				return true;
			} else {
				if(addUserPage.isComplete()) return true;
			}
		}
		
		return super.canFinish();
	}

	@Override
	public boolean performFinish() {
		UserDAO systeUser = null; 
		
		if(PublicTadpoleDefine.SYSTEM_USE_GROUP.PERSONAL.name().equals(systemUseType.getUseType())) {
			
			try {
				// 사용 그룹을 개인으로 수정.
				TadpoleSystemQuery.updateSystemInformation(PublicTadpoleDefine.SYSTEM_USE_GROUP.PERSONAL.name());
				
				// 기본 유저 하면을 입력합니다.				
				systeUser = TadpoleSystem_UserQuery.newUser(
						PublicTadpoleDefine.LOGIN_INPUT_TYPE.NORMAL.name(),
						PublicTadpoleDefine.SYSTEM_DEFAULT_USER, 
						Utils.getUniqueDigit(7), 
						PublicTadpoleDefine.YES_NO.YES.name(),
						"", 	
						PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN.toString(),
						"Personal Admin", 
						Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), 
						PublicTadpoleDefine.DEFAULT_TIME_ZONE,
						PublicTadpoleDefine.YES_NO.YES.name(), 
						PublicTadpoleDefine.YES_NO.NO.name(), 
						"",
						"*",
						PublicTadpoleDefine.SYSTEM_DEFAULT_USER,
						new Timestamp(System.currentTimeMillis())); //$NON-NLS-1$ //$NON-NLS-2$
				
			} catch(Exception e) {
				logger.error("System initialize Exception", e);
			}
			
		} else {
			SystemAdminWizardUserDAO adminDao = addUserPage.getUserData();
			
			try {
				// 사용 그룹을 개인으로 수정.
				TadpoleSystemQuery.updateSystemInformation(PublicTadpoleDefine.SYSTEM_USE_GROUP.GROUP.name());

				// 사용자 등록
				systeUser = TadpoleSystem_UserQuery.newUser(PublicTadpoleDefine.LOGIN_INPUT_TYPE.NORMAL.name(),
				adminDao.getEmail(), Utils.getUniqueDigit(7), PublicTadpoleDefine.YES_NO.YES.name(),
				adminDao.getPasswd(), 	
				PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN.toString(),
				CommonMessages.get().TadpoleAdministrator,  
				Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), 
				PublicTadpoleDefine.DEFAULT_TIME_ZONE,
				PublicTadpoleDefine.YES_NO.YES.name(), 
				PublicTadpoleDefine.YES_NO.NO.name(), 
				"", 
				"*",
				adminDao.getEmail(),
				new Timestamp(System.currentTimeMillis())); //$NON-NLS-1$ //$NON-NLS-2$
				
			} catch(Exception e) {
				logger.error("System initialize Exception", e);
			}
		}
		
//		// save database 를 생성합니다.
//		try {
//			AddDefaultSampleDBToUser.addUserDefaultDB(systeUser.getSeq(), systeUser.getEmail());
//		} catch (Exception e) {
//			logger.error("Sample SQLite file copy error", e);
//		}
		
		return true;
	}
}
