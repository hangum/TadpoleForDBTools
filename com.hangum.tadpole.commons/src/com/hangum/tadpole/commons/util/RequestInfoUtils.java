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
package com.hangum.tadpole.commons.util;

import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * <pre>
 * 사용자 정보를 보기 좋게
 * 
 * 1. email.
 * 2. accept ip
 * 3. os
 * 4. brower
 * 5. full version
 * </pre>
 * 
 * @author hangum
 *
 */
public class RequestInfoUtils {
	
	/**
	 * 올챙이가 동작 할 수 없는 브라우저라면, 사용자 브라우저 를 리턴합니다.
	 * 
	 * @return
	 */
	public static String isTadpoleRunning() {
		ServletUserAgent sua = new ServletUserAgent();
		sua.detect(RWT.getRequest());

		String strBrowser = sua.getBrowserType().toString();
		for(PublicTadpoleDefine.TADPOLE_SUPPORT_BROWSER supportBrowser: PublicTadpoleDefine.TADPOLE_SUPPORT_BROWSER.values()) {
			if(strBrowser.equalsIgnoreCase(supportBrowser.toString())) return "";
		}
		
		return strBrowser;
	}

	/**
	 * Get user browser and browser version information.
	 * 
	 * @return
	 */
	public static String getUserBrowser() {
		ServletUserAgent sua = new ServletUserAgent();
		sua.detect(RWT.getRequest());

		String strBrowser = sua.getBrowserType().toString();
		String strFullVersion = sua.getFullVersion() + sua.getMajorVersion();
		
		return strBrowser + " Ver" + strFullVersion;
	}

	/**
	 * 사용자 인포
	 * 
	 * @param prefixMsg
	 * @param email
	 * @return
	 */
	public static String requestInfo(String prefixMsg, String email) {
		StringBuffer retMsg = new StringBuffer(prefixMsg + "\r\n");
		
		ServletUserAgent sua = new ServletUserAgent();
		sua.detect(RWT.getRequest());
		String strOs = sua.getOSSimpleType().toString();
		String strBrowser = sua.getBrowserType().toString();
		String strFullVersion = sua.getFullVersion() + sua.getMajorVersion();
		
		retMsg.append("[email]" + email + "[ip]" + RWT.getRequest().getLocalAddr() + "\r\n");
		retMsg.append("[os]" + strOs + "[browser]" + strBrowser + "[full version]" + strFullVersion);
		
		return retMsg.toString();
	}
	
	/**
	 * 사용자의 os를 얻습니다.
	 * 
	 * @return
	 */
	public static ServletUserAgent.OS_SIMPLE_TYPE findOSSimpleType() {
		ServletUserAgent sua = new ServletUserAgent();
		sua.detect(RWT.getRequest());
		
		return sua.getOSSimpleType();
	}
	
}
