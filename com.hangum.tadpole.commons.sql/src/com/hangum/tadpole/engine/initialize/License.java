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
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;

/**
 * License loader
 * 
 * @author hangum
 *
 */
public class License {
	private static final Logger logger = Logger.getLogger(License.class);
	public static String tdbLicense = ApplicationArgumentUtils.getResourcesDir() + "TadpoleHub.lic";
	
	public static void load() {
		if(ApplicationArgumentUtils.isInitialize) return;
		
		try {
			// license file checke
			File fileExist = new File(tdbLicense);
			if(fileExist.isFile()) {
				String[] arrLicenseInfo = StringUtils.split(IOUtils.toString(new FileInputStream(fileExist)), "\n");
				if(arrLicenseInfo.length != 2) throw new Exception("License not validate");
				ApplicationArgumentUtils.passwd = arrLicenseInfo[0];
				ApplicationArgumentUtils.dbServer = arrLicenseInfo[1];
			}
			ApplicationArgumentUtils.isInitialize = true;
		} catch(Exception e) {
			logger.error("System initialize exception", e);
			MessageDialog.openError(null, "Error", "License validate exception\n" + e.getMessage());
			
			System.exit(0);
		}
		
	}

}
