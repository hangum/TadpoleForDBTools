/**
 * Copyright (c) 2017 Tadpole Hub.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * @author sun.han
 */
package com.hangum.tadpole.commons.libs.core.errors;

import org.eclipse.rap.rwt.RWT;

public class TadpoleErrorMessages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.commons.libs.core.errors.TadpoleErrorMessages"; 
	
	public String NoError;
	
	public String EnterNumbersOnly;
	public String ItemIsEmpty;
	
	public String InvalidRange_GEAndLEWithItem;
	public String InvalidRange_GE;
	public String InvalidRange_G;
	public String InvalidRange_LE;
	public String InvalidRange_L;
	
	/* password */
//	public String PasswordRule;
	public String InvalidPassword;
	public String PasswordsDoNotMatch;
	
	/* License */
	public String InvalidLicense;
	public String LicenseExpired;
	public String LicenseWillExpire;
	public String NoValidLicenseFound;
	
	/* Account */
	public String UserNotFound;
	
	/* Database */
	public String UnsupportedDatabase;
	public String UnableToConnectToDatabase; 
	public String NoPermissionToAddDatabase;
	public String NoPermissionToDeleteDatabase;
	public String NoPermissionToUpdateDatabase;
	public String ExceededMaxDatabases;
	
	public String NoJDBCDriverFound;
	public String FailedToLoadJDBCDriver;
	public String InvalidJDBCDriver;
	
	/* File operations */
	public String FileNotFound;
	public String NoPermissionToUpdateFile;
	
	
	public String UnexpectedError;  /* TDP-999 */
	
	public static TadpoleErrorMessages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, TadpoleErrorMessages.class);
	}

	private TadpoleErrorMessages() {
	}
}
