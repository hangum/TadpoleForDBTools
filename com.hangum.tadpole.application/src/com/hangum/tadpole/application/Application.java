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

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.hangum.tadpole.application.initialize.EnginDBInitializer;
import com.hangum.tadpole.application.initialize.wizard.SystemInitializeWizard;
import com.hangum.tadpole.application.start.ApplicationWorkbenchAdvisor;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.initialize.TadpoleHubStartupInitializer;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements EntryPoint {
	private static final Logger logger = Logger.getLogger(Application.class);
	
	/**
	 * create ui
	 */
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		
		if(!TadpoleApplicationContextManager.isSystemInitialize()) {
			systemInitialize();
		}
	
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();		
		return PlatformUI.createAndRunWorkbench( display, advisor );
	}
	
	/**
	 * System initialize
	 * 
	 */
	private void systemInitialize() {
		try {
			TadpoleHubStartupInitializer.initializeLoadConfigAndJDBCDriver();
			
			// initialize system 
			if(!EnginDBInitializer.initSystem()) {
				if(logger.isInfoEnabled()) logger.info("Initialize System default setting.");
				
				WizardDialog dialog = new WizardDialog(null, new SystemInitializeWizard());
				if(Dialog.OK != dialog.open()) {
					throw new Exception("System initialization failed. Please restart system.\n");
				}
			}
			
			// initialize system value
			TadpoleHubStartupInitializer.initializeSystemValue();
			
		} catch(Exception e) {
			logger.error("Tadpole Hub initialization failed.", e); //$NON-NLS-1$
			MessageDialog.openError(null, CommonMessages.get().Error, com.hangum.tadpole.application.start.Messages.get().ApplicationWorkbenchWindowAdvisor_2 + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR + "[Error message]" + PublicTadpoleDefine.LINE_SEPARATOR + e.getMessage());
			
			System.exit(0);
		}
	}
	
}
