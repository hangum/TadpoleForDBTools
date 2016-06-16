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
package com.hangum.tadpole.preference;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.preference.messages"; //$NON-NLS-1$

	public String isSupportMyBatisDollos;

	public String EditorPreferencePage_3;
	
	public String AmazonPreferencePage_0;

	public String AmazonPreferencePage_1;

	public String ChangeUsePersonalToGrouprDialog_0;

	public String ChangeUsePersonalToGrouprDialog_1;

	public String Error;

	public String ChangeUsePersonalToGrouprDialog_11;

	public String ChangeUsePersonalToGrouprDialog_13;

	public String ChangeUsePersonalToGrouprDialog_15;

	public String ChangeUsePersonalToGrouprDialog_17;

	public String ChangeUsePersonalToGrouprDialog_19;

	public String ChangeUsePersonalToGrouprDialog_2;

	public String ChangeUsePersonalToGrouprDialog_21;

	public String ChangeUsePersonalToGrouprDialog_23;

	public String ChangeUsePersonalToGrouprDialog_26;

	public String Save;

	public String Cancel;

	public String ChangeUsePersonalToGrouprDialog_3;

	public String ChangeUsePersonalToGrouprDialog_4;

	public String ChangeUsePersonalToGrouprDialog_5;

//	public String ChangeUsePersonalToGrouprDialog_6;

	public String ChangeUsePersonalToGrouprDialog_7;

	public String ChangeUsePersonalToGrouprDialog_9;

	public String DefaultPreferencePage_0;
	public String DefaultPreferencePage_2;

//	public String UserInfoPerference_0;

	public String UserInfoPerference_10;

	public String UserInfoPerference_11;

	public String UserInfoPerference_14;

	public String UserInfoPerference_15;

	public String UserInfoPerference_16;

	public String UserInfoPerference_17;

	public String UserInfoPerference_2;

	public String UserInfoPerference_3;

	public String UserInfoPerference_4;

	public String UserInfoPerference_5;
	public String UserInfoPerference_6;

	public String UserInfoPerference_7;

	public String UserInfoPerference_9;
	
	public String DefaultPreferencePage_other_labelText;
	public String DefaultPreferencePage_other_labelText_1;

	public String GeneralPreferencePage_0;

	public String GeneralPreferencePage_2;
	public String RDBPreferencePage_0;

	public String RDBPreferencePage_3;

	public String RDBPreferencePage_5;
	public String RDBPreferencePage_resultType;

	public String RDBPreferencePage_btnCreatePlanTable_text;
	public String GeneralPreferencePage_lblExportDilimit_text;
	public String GeneralPreferencePage_text_text;
	public String GeneralPreferencePage_lblHomePage_text;
	public String GeneralPreferencePage_btnCheckButton_text;

	public String MongoDBPreferencePage_0;

	public String MongoDBPreferencePage_1;

	public String MongoDBPreferencePage_10;

	public String MongoDBPreferencePage_11;

	public String MongoDBPreferencePage_2;

	public String MongoDBPreferencePage_3;

	public String MongoDBPreferencePage_4;

	public String MongoDBPreferencePage_5;

	public String MongoDBPreferencePage_6;

	public String SecurityCredentialPreference_0;

	public String SecurityCredentialPreference_1;

	public String SecurityCredentialPreference_2;

	public String SecurityCredentialPreference_3;

	public String SecurityCredentialPreference_4;

	public String SecurityCredentialPreference_5;

	public String SQLFormatterPreferencePage_0;

	public String SQLFormatterPreferencePage_3;

	public String SQLFormatterPreferencePage_4;

//	public String SQLFormatterPreferencePage_7;

	public String SQLFormatterPreferencePage_8;
	public String RDBPreferencePage_lblNumberColumnAdd_text;
	public String SQLFormatterPreferencePage_btnCheckButton_text;
	public String SQLFormatterPreferencePage_btnNewLineBefore_text;
	public String SQLFormatterPreferencePage_btnRemoveEmptyLine_text;
	public String SQLFormatterPreferencePage_btnWordBreak_text;
	public String RDBPreferencePage_lblResultViewFont_text;
	public String RDBPreferencePage_btnNewButton_text;
	public String RDBPreferencePage_lblQueryTimeout_text;
	public String GeneralPreferencePage_grpEmailAccount_text;
	public String GeneralPreferencePage_lblSmtpServer_text;
	public String GeneralPreferencePage_lblPort_text;
	public String GeneralPreferencePage_lblAccount_text;
	public String GeneralPreferencePage_lblPassword_text;

//	public String GetAdminPreference_5;
	public String PerspectivePreferencePage_0;

	public String PerspectivePreferencePage_3;

	public String PerspectivePreferencePage_4;

	public String PerspectivePreferencePage_6;

	public String UserInfoPerference_button_text;
	public String UserInfoPerference_grpGoogleAuth_text;
	public String UserInfoPerference_btnGoogleOtp_text_1;
	public String UserInfoPerference_lblSecretKey_text_1;
	public String UserInfoPerference_lblQrcodeUrl_text;
	public String RDBPreferencePage_lblCommitCount_text;
	public String RDBPreferencePage_text_text;
	public String RDBPreferencePage_lblCharacterShownIn_text;
	public String RDBPreferencePage_text_text_1;
	public String EditorPreferencePage_0;

	public String EditorPreferencePage_1;

	public String EditorPreferencePage_2;

	public String EditorPreferencePage_lblTheme_text;

	public String LoginDialog_lblLanguage_text;

	public String Confirm;

	public String Close;

	public String Warning;

	public String RESTAPI_Help;

	public String Document;
	
	public String Language;
	
	public String TimeZone;
	
	public String UserAnswer_Yes;
	public String UserAnswer_No;
	
	public String ShowNullCharacters;
	
	public String OTP;
	
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
