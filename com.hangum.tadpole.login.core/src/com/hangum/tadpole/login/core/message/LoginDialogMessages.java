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
package com.hangum.tadpole.login.core.message;

import org.eclipse.rap.rwt.RWT;

public class LoginDialogMessages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.login.core.message.messages"; //$NON-NLS-1$

	// 회사 관련 정보 표현 시작.
	public String company_RegistrationNumber;
	public String company_information;
	public String company_address_tel;
	public String company_name;
	// 회사 관련 정보 표현 종료.
	
	public String LoginDialog_10;
	public String LoginDialog_11;
	
	public String LoginDialog_14;
	public String LoginDialog_15;
	
	public String LoginDialog_17;
	public String LoginDialog_19;
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
	public String LoginDialog_lblNewLabel_text;
	public String UserInformationDialog_5;
	
	public String LoginDialog_lblNewLabel_text_1;

	public String LoginDialog_button_new_user;
	public String LoginDialog_lblLanguage_text;
	public String LoginDialog_WelcomeMsg;
	public String LoginDialog_ProjectRelease;

	public String ResetPassword;

	public String OpenUserManuel;

	public String TemporayPassword;

	public String doesnotFoundPrivateIP;

	public String LoginDialog_userIP;

	public String AlreadyLoginConfirm;

	public String PleaseCheckIDpassword;

	public static LoginDialogMessages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, LoginDialogMessages.class);
	}

	private LoginDialogMessages() {
	}
}
