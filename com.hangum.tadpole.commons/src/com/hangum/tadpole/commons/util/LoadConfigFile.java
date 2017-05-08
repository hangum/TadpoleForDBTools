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
	private static Properties _properties = null;
	
	/**
	 * get configuration files
	 * 
	 * @throws Exception
	 */
	public static void initializeConfigFile() {
		if(_properties == null) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(
						ApplicationArgumentUtils.getResourcesDir() + PublicTadpoleDefine.TDB_CONFIG_FILE));
			} catch (Exception e) {
				logger.error(String.format("Not found config files. %s",
						ApplicationArgumentUtils.getResourcesDir() + PublicTadpoleDefine.TDB_CONFIG_FILE));
			}
			_properties = properties;
		}
	}
	
	/** 
	 * 환경 설정 파일을 로드한다.
	 * 
	 * @return
	 */
	public static Properties getConfigFile() {
		if(_properties == null) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(
						ApplicationArgumentUtils.getResourcesDir() + PublicTadpoleDefine.TDB_CONFIG_FILE));
			} catch (Exception e) {
				logger.error(String.format("Not found config files. %s",
						ApplicationArgumentUtils.getResourcesDir() + PublicTadpoleDefine.TDB_CONFIG_FILE));
			}
			
			_properties = properties;
		}
		
		return _properties;
	}

	/**
	 * is use otp
	 * @return
	 */
	public static boolean isUseOPT() {
		String otpUse = getConfigFile().getProperty("otp.use");

		if (otpUse == null) return true;
		else if ("YES".equalsIgnoreCase(otpUse)) return true;
		else return false;
	}
	
	/**
	 * is use engine gateway
	 * 
	 * @return
	 */
	public static boolean isEngineGateway() {
		return "YES".equalsIgnoreCase(getConfigFile().getProperty("ENGINE.GATEWAY.USE"))?true:false;
	}

	/**
	 * is gateway user id check
	 * 
	 * @return
	 */
	public static boolean isGateWayIDCheck() {
		return "YES".equalsIgnoreCase(getConfigFile().getProperty("ENGINE.GATEWAY.ID_CHECK"))?true:false;
	}
}
