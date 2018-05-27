package com.hangum.tadpole.engine.security;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.otp.core.GetOTPCode;

public class OTPValidation {
	private static final Logger logger = Logger.getLogger(OTPInputDialog.class);

	public boolean validation(String userID) {
		try {
			GetOTPCode.isValidate(userID, "", "");
		} catch(Exception e) {
			logger.error("OTP check", e);
			MessageDialog.openError(null, CommonMessages.get().Error, e.getMessage());
			return false;
		}
		
		return true;
	}

}
