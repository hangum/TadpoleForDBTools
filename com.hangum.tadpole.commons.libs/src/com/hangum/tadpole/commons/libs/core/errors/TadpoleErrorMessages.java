package com.hangum.tadpole.commons.libs.core.errors;

import org.eclipse.rap.rwt.RWT;

public class TadpoleErrorMessages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.libs.core.TadpoleErrorMessages"; 
	
	public String EnterNumbersOnly;
	public String ItemIsEmpty;
	
	/* password */
	public String PasswordRule;
	
	/* License */
	public String InvalidLicense;
	
	/* Account */
	public String UserNotFound;
	
	public static TadpoleErrorMessages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, TadpoleErrorMessages.class);
	}

	private TadpoleErrorMessages() {
	}
}
