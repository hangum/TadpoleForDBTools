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
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.PRODUCT_TYPE;

/**
 * Application license initialize
 * 
 * @author hangum
 *
 */
public class ApplicationLicenseInitialize {
	private static final Logger logger = Logger.getLogger(ApplicationLicenseInitialize.class);
	
	/**
	 * get license file location
	 * 
	 * @return
	 */
	public static String getLicenseFileLocation() {
		String strActiveProductType = "";
		if(PublicTadpoleDefine.ACTIVE_PRODUCT_TYPE == PRODUCT_TYPE.TadpoleDBHub) {
			strActiveProductType = "TadpoleHub";
		} else {
			strActiveProductType = PublicTadpoleDefine.ACTIVE_PRODUCT_TYPE.name();
		}
		
		return String.format("%s%s.lic", ApplicationArgumentUtils.getResourcesDir(), strActiveProductType);
	}
	
	/**
	 * license load
	 */
	public static void load() {
		LicenseExtensionHandler linceseHandler = new LicenseExtensionHandler();
		try {
			String strLicenseInfo = ApplicationArgumentUtils.initDBServer();
			if(!StringUtils.isEmpty(strLicenseInfo)) {
				logger.info("******** [0] Start enterprise version ");
				linceseHandler.license(strLicenseInfo);
			} else {
				File fileExist = new File(getLicenseFileLocation());
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
	
	/**
	 * live chage 
	 * 
	 * @param strLicense
	 * @throws Exception
	 */
	public static void liveChange(String strLicense) throws Exception {
		LicenseExtensionHandler linceseHandler = new LicenseExtensionHandler();
		linceseHandler.liveChange(strLicense);
	}

}
