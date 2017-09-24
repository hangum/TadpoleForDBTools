package com.hangum.tadpole.commons;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.messages"; //$NON-NLS-1$
	public String FileUploadDialog_fileSelect;
	public String ExceptionDetailsErrorDialog_3;
	public String ExceptionDetailsErrorDialog_5;
	public String SingleFileuploadDialog_1;
	public String SingleFileuploadDialog_5;
	public String SingleFileuploadDialog_7;
	public String ImageViewer;
	
	public String PleaseCheckIDpassword;
	public String FILEOPEN_ADD_APPEND;
	public String FILEOPEN_NEW_WINDOW;
	public String FILEOPEN_REMOVE_AND_ADD;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
