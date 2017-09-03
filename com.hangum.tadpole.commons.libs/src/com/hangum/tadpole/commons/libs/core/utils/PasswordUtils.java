/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.utils;

/**
 * 패스워드 유틸
 * 
 * @author hangum
 *
 */
public class PasswordUtils {

	/**
	 * 인자만큼 임의의 패스워드 생성
	 * 
	 * @param intLimitLength
	 * @return
	 */
	public static String getRandomPasswd(int intLimitLength) {
		char charPwList[] = new char[] {
				'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
				'!', '@', '#', '$', '%', '^', '&' 
		};
		
		StringBuffer strPasswd = new StringBuffer();
		for (int i = 0; i < intLimitLength; i++) {
			int intPostionPasswd = (int)(Math.random()*(charPwList.length));
			strPasswd.append(charPwList[intPostionPasswd]);
		}
		return strPasswd.toString();
	}
}
