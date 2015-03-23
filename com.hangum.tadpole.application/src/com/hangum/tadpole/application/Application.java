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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.hangum.tadpole.application.start.ApplicationWorkbenchAdvisor;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.rdb.core.Activator;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements EntryPoint {
	private static final Logger logger = Logger.getLogger(Application.class);

	public int createUI() {
		Display display = PlatformUI.createDisplay();//new TadpoleDisplay();
		
		systemInitialize();
		
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();		
		return PlatformUI.createAndRunWorkbench( display, advisor );
	}
	
	/**
	 * System initialize
	 * If the system table does not exist, create a table.
	 */
	private void systemInitialize() {
		// 시스템 초기 데이터 베이스 생성.
		boolean isTadpoleInitialize = PlatformUI.getPreferenceStore().getBoolean(PreferenceDefine.IS_TADPOLE_INITIALIZE);
		if(!isTadpoleInitialize || ApplicationArgumentUtils.isForceSystemInitialize()) {
			try {
				if(!TadpoleSystemInitializer.initSystem()) {
					throw new Exception("System initialize fail");
				}
				PlatformUI.getPreferenceStore().setValue(PreferenceDefine.IS_TADPOLE_INITIALIZE, true);
			} catch(Exception e) {
				logger.error("System initialize error", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", com.hangum.tadpole.application.start.Messages.ApplicationWorkbenchWindowAdvisor_2, errStatus); //$NON-NLS-1$
				
				System.exit(0);
			}
		}
	}
}
