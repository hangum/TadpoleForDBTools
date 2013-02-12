/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.util;

import org.eclipse.rwt.RWT;

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
		StringBuffer errMsg = new StringBuffer(prefixMsg + "\r\n");
		
		ServletUserAgnet sua = new ServletUserAgnet();
		sua.detect(RWT.getRequest());
		String strOs = sua.getOsType().toString();
		String strBrowser = sua.getBrowserType().toString();
		String strFullVersion = sua.getFullVersion() + sua.getMajorVersion();
		
		errMsg.append("[email]" + email + "[ip]" + RWT.getRequest().getLocalAddr() + "\r\n");
		errMsg.append("[os]" + strOs + "[browser]" + strBrowser + "[full version]" + strFullVersion);
		
		return errMsg.toString();
	}
}
