package com.hangum.tadpole.commons;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.messages"; //$NON-NLS-1$
	public static String FileUploadDialog_0;
	public static String FileUploadDialog_1;
	public static String FileUploadDialog_3;
	public static String FileUploadDialog_4;
	public static String FileUploadDialog_6;
	public static String FileUploadDialog_8;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
