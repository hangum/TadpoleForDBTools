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

import java.security.MessageDigest;

import org.apache.log4j.Logger;

/**
 * sha256 utils
 * 
 * @author hangum
 *
 */
public class SHA256Utils {
	private static final Logger logger = Logger.getLogger(SHA256Utils.class);

	/**
	 * sha 256 util
	 * 
	 * @param strData
	 * @return
	 */
	public static String getSHA256(String strData) throws Exception {
		StringBuffer hexString = new StringBuffer();

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(strData.getBytes("UTF-8"));

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

		} catch (Exception e) {
			logger.error("sha256 convert error", e);
			throw new Exception(e.getMessage());
		}

		return hexString.toString();
	}
}
