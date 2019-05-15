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
package com.tadpole.common.define.core.define;

import org.eclipse.core.runtime.Platform;

/**
 * 시스템 정보를 정의합니다.
 * 
 * @author hangum
 *
 */
public class SystemDefine {
	
	// DB HUB
	public static final String DBHUB_NAME = "Tadpole DB Hub";
	public static final String DBHUB_MAJOR_VERSION = "2.0.0";
	public static final String DBHUB_SUB_VERSION = "Build (A2)";
	public static final String DBHUB_RELEASE_DATE = "2019.05.15";
	
	// History Hub
	public static final String HISTORYHUB_NAME = "Tadpole History Hub";
	public static final String HISTORYHUB_MAJOR_VERSION = "1.1.2";
	public static final String HISTORYHUB_SUB_VERSION = "Build (R2)";
	public static final String HISTORYHUB_RELEASE_DATE = "2018.06.30";
	
	
	public static final String INFORMATION = "http://tadpolehub.com/";
	
	public static final String ADMIN_EMAIL = "hangum@tadpolehub.com";
	public static final String SOURCE_PAGE = INFORMATION;
	
	/**
	 * 제품이름
	 * @return
	 */
	public static String getProductName() {
		if(PublicTadpoleDefine.ACTIVE_PRODUCT_TYPE == PublicTadpoleDefine.PRODUCT_TYPE.TadpoleDBHub) {
			return DBHUB_NAME;
		} else {
			return HISTORYHUB_NAME;
		}
	}
	
	/**
	 * 정확한 제품 정보를 리턴한다.
	 * 
	 * @return
	 */
	public static String getSystemInfo() {
		if(PublicTadpoleDefine.ACTIVE_PRODUCT_TYPE == PublicTadpoleDefine.PRODUCT_TYPE.TadpoleDBHub) {
			return getDBHubSystemInfo();
		} else {
			return getHistoryHubSystemInfo();
		}
	}
	
	/**
	 * get system information
	 * @return
	 */
	public static String getDBHubSystemInfo() {
		return String.format("v%s %s-%s", DBHUB_MAJOR_VERSION, DBHUB_SUB_VERSION, DBHUB_RELEASE_DATE);
	}
	
	/**
	 * get system information
	 * @return
	 */
	public static String getHistoryHubSystemInfo() {
		return String.format("v%s %s-%s", HISTORYHUB_MAJOR_VERSION, HISTORYHUB_SUB_VERSION, HISTORYHUB_RELEASE_DATE);
	}
	
	/**
	 * get configure root
	 * @return
	 */
	public static String getConfigureRoot() {
		if(isOSGIRuntime()) {
			return Platform.getInstallLocation().getURL().getFile();
		} else {
			return System.getProperty("user.home") + PublicTadpoleDefine.DIR_SEPARATOR;
		}
	}
	
	/**
	 * 현재 동작 하는 런타임이 osgi framework인지?
	 * @return
	 */
	public static boolean isOSGIRuntime() {
		Object objectUseOSGI = System.getProperty("osgi.framework.useSystemProperties");
		if(objectUseOSGI == null) return false;
		else return true; 
	}
}
