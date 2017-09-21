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
package com.hangum.tadpole.commons.libs.core.define;

import org.eclipse.core.runtime.Platform;

/**
 * 시스템 정보를 정의합니다.
 * 
 * @author hangum
 *
 */
public class SystemDefine {
	
	public static final String NAME = "Tadpole DB Hub";
	public static final String MAJOR_VERSION = "1.7.5";
	public static final String SUB_VERSION = "Build (a4)";
	public static final String RELEASE_DATE = "2017.09.19";
	public static final String INFORMATION = "http://hangum.github.io/TadpoleForDBTools/";
	
	public static final String ADMIN_EMAIL = "hangum@tadpolehub.com";
	public static final String SOURCE_PAGE = INFORMATION;
	
	/**
	 * get system information
	 * @return
	 */
	public static String getSystemInfo() {
		return String.format("%s %s-%s", MAJOR_VERSION, SUB_VERSION, RELEASE_DATE);
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
