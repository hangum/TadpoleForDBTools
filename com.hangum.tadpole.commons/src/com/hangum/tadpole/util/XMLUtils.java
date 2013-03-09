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

import org.apache.commons.lang.StringUtils;

/**
 * xml data utils
 * 
 * @author hangum
 *
 */
public class XMLUtils {
	public final static String[] xml = { "&", "\\", "<", ">" };
	public final static String[] javaString = { "&amp;", "&apos;", "&lt;", "&gt;" };

	/**
	 * xml string 특수문자가 있을 경우 일반 문자로 변환
	 * 
	 * @param retVal
	 * @return
	 */
	public static String xmlToString(String retVal) {
			for (int i=0; i<xml.length; i++) {
				retVal = StringUtils.replace(retVal, xml[i], javaString[i]);
			}
			
			return retVal;
		}

	/**
	 * String을 xml string으로 변환
	 * 
	 * @param retVal
	 * @return
	 */
	public static String stringToXml(String retVal) {
			for (int i=0; i<xml.length; i++) {
				retVal = StringUtils.replace(retVal, javaString[i], xml[i]);
			}
			
			return retVal;
		}
}
