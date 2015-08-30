/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.googleauth;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

/**
 * Google Authenticator utils
 * 
 * @see https://github.com/hangum/TadpoleForDBTools/issues/325#issuecomment-48547991
 * @see https://github.com/wstrange/GoogleAuth
 * 
 * @author hangum
 *
 */
public class GoogleAuthManager {

	private static GoogleAuthManager instance = null;
	
	private GoogleAuthManager() {}
	
	public static GoogleAuthManager getInstance() {
		if(instance == null) {
			instance = new GoogleAuthManager();
		}
		return instance;
	}
	
	/**
	 * make secret key
	 * 
	 * @return
	 */
	public String getSecretKey() {
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
		
		return key.getKey();
	}
	
	/**
	 * get code url
	 * 
	 * @param user
	 * @param host
	 * @param secret
	 * @return
	 */
	public String getURL(String user, String host, String secret) {
		String url = GoogleAuthenticatorKey.getQRBarcodeURL(user, host, secret);
        return url;
	}
	
	/**
	 * validate
	 * 
	 * @param secret
	 * @param code
	 * @return
	 */
	public boolean isValidate(String secret, int code) {
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		googleAuthenticator.setWindowSize(10);  //should give 5 * 30 seconds of grace...
		
        boolean isCodeValid = googleAuthenticator.authorize(secret, code);
        return isCodeValid;
	}
	
	
}
