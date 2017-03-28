/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application;

import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.hangum.tadpole.application.initialize.wizard.SystemInitializeWizard;
import com.hangum.tadpole.application.start.ApplicationWorkbenchAdvisor;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.engine.initialize.ApplicationLicenseInitialize;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.define.AdminPreferenceDefine;
import com.hangum.tadpole.preference.define.GetAdminPreference;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements EntryPoint {
	private static final Logger logger = Logger.getLogger(Application.class);

	public int createUI() {
		Display display = PlatformUI.createDisplay();
		
		Locale locale = RWT.getLocale();
		Locale.setDefault(locale);
		RWT.getUISession().setLocale(locale);
		RWT.setLocale(locale);
		
		systemInitialize();
	
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();		
		return PlatformUI.createAndRunWorkbench( display, advisor );
	}
	
	/**
	 * System initialize
	 * 
	 * 0. License load
	 * 1. jdbc driver load
	 * 2. If the system table does not exist, create a table.
	 * 2.1 System initialize
	 */
	private void systemInitialize() {
		if(TadpoleApplicationContextManager.isSystemInitialize()) return;
		
		try {
			// load license 
			ApplicationLicenseInitialize.load();
			
			// load default config file
			LoadConfigFile.initializeConfigFile();
			
			// initialize system 
			if(!TadpoleSystemInitializer.initSystem()) {
				if(logger.isInfoEnabled()) logger.info("Initialize System default setting.");
				
				WizardDialog dialog = new WizardDialog(null, new SystemInitializeWizard());
				if(Dialog.OK != dialog.open()) {
					throw new Exception("System initialization failed. Please restart system.\n");
				}
			}
			
			/* define login type */
			Properties prop = LoadConfigFile.getConfig();
			String txtLoginMethod = prop.getProperty("LOGIN_METHOD", AdminPreferenceDefine.SYSTEM_LOGIN_METHOD_VALUE);
			UserInfoDataDAO userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, txtLoginMethod);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, userInfoDao);
			
			/** 뷰에 보여주어야할 필터 값을 가져온다 */
			String strProductFilter = prop.getProperty("tadpole.db.producttype.remove.filter", "");
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SYSTEM_VIEW_PRODUCT_TYPE_FILTER, strProductFilter);
			GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SYSTEM_VIEW_PRODUCT_TYPE_FILTER, userInfoDao);
			
			/** cert user info */
			PublicTadpoleDefine.CERT_USER_INFO = prop.getProperty("CERT_USER_INFO", "");
			
		} catch(Exception e) {
			logger.error("Initialization failed.", e); //$NON-NLS-1$
			MessageDialog.openError(null, CommonMessages.get().Error, com.hangum.tadpole.application.start.Messages.get().ApplicationWorkbenchWindowAdvisor_2);
			
			System.exit(0);
		}
	}
}
