package com.hangum.tadpole.commons;

import org.eclipse.osgi.util.NLS;
import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.messages"; //$NON-NLS-1$
	public String FileUploadDialog_0;
	public String FileUploadDialog_1;
	public String FileUploadDialog_3;
	public String FileUploadDialog_4;
	public String FileUploadDialog_6;
	public String FileUploadDialog_8;
	public String ExceptionDetailsErrorDialog_0;
	public String ExceptionDetailsErrorDialog_1;
	public String ExceptionDetailsErrorDialog_2;
	public String ExceptionDetailsErrorDialog_3;
	public String ExceptionDetailsErrorDialog_5;
	public String SingleFileuploadDialog_1;
	public String SingleFileuploadDialog_2;
	public String SingleFileuploadDialog_3;
	public String SingleFileuploadDialog_5;
	public String SingleFileuploadDialog_7;
	public String SingleFileuploadDialog_8;
	public String SingleFileuploadDialog_9;

	// static {
	// NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	// }
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
