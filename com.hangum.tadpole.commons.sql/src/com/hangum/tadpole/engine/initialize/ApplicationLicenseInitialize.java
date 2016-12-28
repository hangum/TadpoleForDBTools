/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.initialize;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.license.LicenseExtensionHandler;

/**
 * Application license initialize
 * 
 * @author hangum
 *
 */
public class ApplicationLicenseInitialize {
	private static final Logger logger = Logger.getLogger(ApplicationLicenseInitialize.class);
	public static String TDB_License_FILE = ApplicationArgumentUtils.getResourcesDir() + "TadpoleHub.lic";
	
	public static void load() {
		LicenseExtensionHandler linceseHandler = new LicenseExtensionHandler();
		try {
			String strLicenseInfo = ApplicationArgumentUtils.initDBServer();
			if(!StringUtils.isEmpty(strLicenseInfo)) {
				logger.info("******** [0] Start enterprise version ");
				linceseHandler.license(strLicenseInfo);
			} else {
				File fileExist = new File(TDB_License_FILE);
				if(fileExist.isFile()) {
					logger.info("******** [1] Start enterprise version ");
					linceseHandler.license(fileExist);
				}
			}
		} catch(Exception e) {
			logger.error("System initialize exception", e);
			MessageDialog.openError(null, "Error", "License validation exception\n" + e.getMessage());
			
			System.exit(0);
		}
		
	}

}
