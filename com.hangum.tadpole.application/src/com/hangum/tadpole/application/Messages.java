package com.hangum.tadpole.application;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.application.messages"; //$NON-NLS-1$

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
