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
package com.hangum.tadpole.commons.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * load default configfile(resourcehome/tadpole_interface.properties)
 * 
 * @author hangum
 *
 */
public class LoadConfigFile {
	private static final Logger logger = Logger.getLogger(LoadConfigFile.class);

	/**
	 * get configuration files
	 * 
	 * @throws Exception
	 */
	public static void initializeConfigFile() {

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(
					ApplicationArgumentUtils.getResourcesDir() + PublicTadpoleDefine.TDB_CONFIG_FILE));
		} catch (Exception e) {
			logger.error(String.format("Not found config files. %s",
					ApplicationArgumentUtils.getResourcesDir() + PublicTadpoleDefine.TDB_CONFIG_FILE));
		}

		ApplicationContext context = RWT.getApplicationContext();
		context.setAttribute("TDB_CONFIG_FILE", properties);
	}

	/**
	 * Get TadpoleDBHub config
	 * 
	 * @return
	 */
	public static Properties getConfig() {
		ApplicationContext context = RWT.getApplicationContext();
		return (Properties) context.getAttribute("TDB_CONFIG_FILE");
	}

	/**
	 * is use otp
	 * @return
	 */
	public static boolean isUseOPT() {
		String otpUse = getConfig().getProperty("otp.use");

		if (otpUse == null) return true;
		else if ("YES".equalsIgnoreCase(otpUse)) return true;
		else return false;
	}
}
