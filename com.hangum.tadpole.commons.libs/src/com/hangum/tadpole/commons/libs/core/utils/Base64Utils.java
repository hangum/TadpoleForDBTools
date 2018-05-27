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
package com.hangum.tadpole.commons.libs.core.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * base64 utils
 * 
 * @author hangum
 *
 */
public class Base64Utils {
	/**
	 * encode base64 util
	 * 
	 * @param str
	 * @return
	 */
	public static String base64Encode(String str) {
		byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
		
		return new String(encodedBytes);
	}
	
	/**
	 * decode base64 util
	 * @param str
	 * @return
	 */
	public static String base64Decode(String str) {
		byte[] decodedBytes = Base64.decodeBase64(str.getBytes());
		
		return new String(decodedBytes);
	}
	
}
