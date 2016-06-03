/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpold.commons.libs.core.googleauth;

import java.util.List;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

/**
 *
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 11.
 *
 */
public class GoogleAuthManagerTest {

//	public static void main(String[] args) {
//		GoogleAuthManagerTest gamt = new GoogleAuthManagerTest();
//	}
	/**
	 * 
	 */
	public GoogleAuthManagerTest() {
		
		
		
		
//		boolean isValidate = GoogleAuthManager.getInstance().isValidate("6RIGPDKIT6TEK3MF", 220554);
//		System.out.println(isValidate);
		
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
		String strKey =  key.getKey();
		System.out.println(strKey);
		
		List<Integer> listScratchCode = key.getScratchCodes();
		for (Integer integer : listScratchCode) {
			System.out.println(integer);
		}
		
		boolean isVadiate = googleAuthenticator.authorize("ISFIOTOY2BMCXIU2", 39943463);
		System.out.println(isVadiate);
		
	}

}
