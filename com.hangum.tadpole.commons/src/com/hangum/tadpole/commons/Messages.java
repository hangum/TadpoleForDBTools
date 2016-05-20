package com.hangum.tadpole.commons;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.messages"; //$NON-NLS-1$
	public String FileUploadDialog_fileSelect;
	public String ExceptionDetailsErrorDialog_3;
	public String ExceptionDetailsErrorDialog_5;
	public String SingleFileuploadDialog_1;
	public String Error;
	public String SingleFileuploadDialog_5;
	public String SingleFileuploadDialog_7;
	public String Confirm;
	public String Cancle;
	public String Close;
	public String ImageViewer;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
