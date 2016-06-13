/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.start;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.application.start.messages"; //$NON-NLS-1$

	public String NewVersionCheckAction_0;
	public String NewVersionCheckAction_2;
	
	public String AboutAction_0;
	public String AboutAction_3;
	public String AboutAction_5;
	public String ApplicationActionBarAdvisor_0;
	public String ApplicationActionBarAdvisor_1;
	public String ApplicationActionBarAdvisor_2;
	public String ApplicationActionBarAdvisor_3;
	public String ApplicationActionBarAdvisor_4;
	public String ApplicationActionBarAdvisor_5;
	public String ApplicationWorkbenchWindowAdvisor_2;
	public String ApplicationWorkbenchWindowAdvisor_4;
	
	public String LoginDialog_1;
	public String LoginDialog_10;
	public String LoginDialog_11;
	
	public String LoginDialog_14;
	public String LoginDialog_15;
	
	public String LoginDialog_17;
	public String LoginDialog_19;
	public String LoginDialog_2;
	public String LoginDialog_20;
	public String LoginDialog_21;
	public String LoginDialog_22;
	public String LoginDialog_23;
	public String LoginDialog_26;
	public String LoginDialog_27;
	public String LoginDialog_28;
	public String LoginDialog_30;
	public String LoginDialog_4;
	
	public String LoginDialog_9;
	public String BugIssueAction_0;
	
	public String AboutDialog_0;
	public String AboutDialog_5;
	public String AboutDialog_6;
	public String AboutDialog_7;
	public String AboutDialog_8;
	public String AboutDialog_lblReleaseDate_text;
	public String FindPasswordDialog_0;
	public String FindPasswordDialog_6;
	public String LoginDialog_lblNewLabel_text;
	public String OTPLoginDialog_0;
	public String OTPLoginDialog_3;
	public String UserInformationDialog_5;
	
	public String LoginDialog_lblNewLabel_text_1;

	public String LoginDialog_button_text_1;
	public String SelectPerspectiveDialog_0;
	public String SelectPerspectiveDialog_3;
	public String SelectPerspectiveDialog_5;
	public String SelectPerspectiveDialog_6;
	public String SelectPerspectiveDialog_7;
	public String SelectPerspectiveDialog_8;
	public String LoginDialog_lblLanguage_text;
	public String LoginDialog_WelcomeMsg;
	public String LoginDialog_ProjectRelease;
	
	public String NewVersionViewDialog_0;
	public String NewVersionViewDialog_1;
	public String NewVersionViewDialog_2;
	public String NewVersionViewDialog_5;
	public String NewVersionViewDialog_6;
	public String NewVersionViewDialog_7;
	public String NewVersionViewDialog_8;
	public String NewVersionViewDialog_NewVersion;
	public String NewVersionViewDialog_DoesnotCheck;

	public String OK;
	public String Confirm;
	public String Error;
	public String Close;

	public String FindPassword;

	public String SEND;

	public String SendMsg;

	public String SendMsgErr;

	public String AboutAction_TDB;

	public String Warning;

	public String OpenUserManuel;

	public String TemporayPassword;

	public String ServiceBill;
	
	public String OTP;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
