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
package com.hangum.tadpole.util;

import org.eclipse.rap.rwt.RWT;

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
	 * 사용자 인포
	 * 
	 * @param prefixMsg
	 * @param email
	 * @return
	 */
	public static String requestInfo(String prefixMsg, String email) {
		StringBuffer retMsg = new StringBuffer(prefixMsg + "\r\n");
		
		ServletUserAgnet sua = new ServletUserAgnet();
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
	public static ServletUserAgnet.OS_SIMPLE_TYPE findOSSimpleType() {
		ServletUserAgnet sua = new ServletUserAgnet();
		sua.detect(RWT.getRequest());
		
		return sua.getOSSimpleType();
	}
	
}
