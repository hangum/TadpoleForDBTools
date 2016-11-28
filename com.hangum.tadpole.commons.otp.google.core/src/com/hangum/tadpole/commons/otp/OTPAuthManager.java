package com.hangum.tadpole.commons.otp;

import com.hangum.tadpole.commons.otp.google.core.Messages;

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
public class OTPAuthManager {

	private static OTPAuthManager instance = null;
	
	private OTPAuthManager() {}
	
	public static OTPAuthManager getInstance() {
		if(instance == null) {
			instance = new OTPAuthManager();
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
	 * @param strOTPCode 
	 * @return
	 */
	public boolean isValidate(String email, String secret, String strOTPCode) throws Exception {
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		googleAuthenticator.setWindowSize(10);  //should give 5 * 30 seconds of grace...
		
		int intCode = 0;
		try {
			intCode = Integer.parseInt(strOTPCode);
		} catch(Exception e){}
		
        boolean isCodeValid = googleAuthenticator.authorize(secret, intCode);
        if(!isCodeValid)  throw new Exception(Messages.get().OTP_invalid);
        return isCodeValid;
	}
	
	
}
