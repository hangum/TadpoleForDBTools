package com.hangum.tadpole.commons.libs.core.message;

import org.eclipse.rap.rwt.RWT;

/**
 * This class contains messages that users must know and take appropriate action.
 * These types of messages can be seen in this class.
 *  - Enter numbers only.
 *  - Password must contain at least 8 characters.
 *  - The passwords you entered do not match.
 *  - The name cannot contain any special character.
 * 
 * @author sun.han
 *
 */
public class WarningMessages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.libs.core.message.WarningMessages"; //$NON-NLS-1$

	public String EnterNumbersOnly;
	public String EnterNumbersOnlyWithItem;
	
	/* [%s] must be greater than or equal to %d and less than or equal to %d. */
	public String InvalidRange_GEAndLEWithItem; 

	public static WarningMessages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, WarningMessages.class);
	}

	private WarningMessages() {
	}
}

