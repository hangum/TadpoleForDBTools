package com.hangum.tadpole.commons.otp.core;

import com.hangum.tadpole.commons.otp.OTPAuthManager;

/**
 * Get otp code
 * 
 * @author hangum
 *
 */
public class GetOTPCode {

	public static void isValidate(String userID, String secretKEY, String otpCode) throws Exception{
		OTPAuthManager.getInstance().isValidate(userID, secretKEY, otpCode);
	}
	
	public static String getSecretKey() {
		return "";
	}
	
	public static String getURL(String user, String host, String secret) {
		return "";
	}
	
}
