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

	public String ChangeUsePersonalToGrouprDialog_13;

	public String ChangeUsePersonalToGrouprDialog_15;

	public String ChangeUsePersonalToGrouprDialog_17;

	public String ChangeUsePersonalToGrouprDialog_19;

	public String ChangeUsePersonalToGrouprDialog_2;

	public String ChangeUsePersonalToGrouprDialog_21;

	public String ChangeUsePersonalToGrouprDialog_23;

	public String ChangeUsePersonalToGrouprDialog_26;

	public String ChangeUsePersonalToGrouprDialog_3;

	public String ChangeUsePersonalToGrouprDialog_4;

	public String ChangeUsePersonalToGrouprDialog_5;

	public String ChangeUsePersonalToGrouprDialog_7;

	public String ChangeUsePersonalToGrouprDialog_9;

	public String MaxNumOfRowsBySelect;
	public String SessionTimeout_mins;

	public String UserInfoPerference_10;

	public String OnlyUsedForPasswordRecovery;

	public String UserInfoPerference_14;

	public String UserInfoPerference_15;

	public String UserInfoPerference_16;

	public String UserInfoPerference_17;

	public String UserInfoPerference_2;

	public String Password;

	public String ConfirmPassword;

	public String UserInfoPerference_5;
	public String UserInfoPerference_6;

	public String UserInfoPerference_7;

	public String infoCloseYourAccount;
	
	public String OraclePlanTable;
	public String RowsPerPage;

	public String GeneralPreferencePage_2;

	public String RDBPreferencePage_3;

	public String RDBPreferencePage_5;
	public String ResultFormat;
	public String Table;
	public String CreatePlanTable;
	public String GeneralPreferencePage_lblExportDilimit_text;
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

	public String SecurityCredentialPreference_1;

	public String SecurityCredentialPreference_2;

	public String InfoCreateANewAPIKey;
	
	public String CreateAPIKey;

	public String TabWidth;

	public String SQLFormat_AddNewLineBeforeDECODE;
	public String SQLFormat_AddNewLineBeforeDECODE_ToolTip;

	public String SQLFormat_AddNewLineBeforeIN;
	public String SQLFormat_AddNewLineBeforeIN_ToolTip;

	public String MaximumNumberOfCharacters;
	public String SQLFormat_AddNewLineBeforeANDOR;
	public String SQLFormat_AddNewLineBeforeANDOR_ToolTip;
	public String SQLFormat_AddCommaBeforeNewLine;
	public String SQLFormat_AddCommaBeforeNewLine_ToolTip;
	public String SQLFormat_RemoveEmptyLines;
	public String FontForResultPages;
	public String Font;
	public String QueryTimeout;
	public String GeneralPreferencePage_grpEmailAccount_text;
	public String GeneralPreferencePage_lblSmtpServer_text;
	public String GeneralPreferencePage_lblPort_text;
	public String GeneralPreferencePage_lblAccount_text;
	public String GeneralPreferencePage_lblPassword_text;

	public String PerspectivePreferencePage_0;

	public String PerspectivePreferencePage_3;

	public String PerspectivePreferencePage_4;

	public String PerspectivePreferencePage_6;

	public String CloseAccount;

	public String BatchSize;
	public String MaximumNumberOfCharactersPerColumn;
	public String RDBPreferencePage_text_text_1;
	public String EditorPreferencePage_0;

	public String MaximumNumberOfCharactersPerLine;

	public String EditorPreferencePage_2;

	public String EditorPreferencePage_lblTheme_text;

	public String LoginDialog_lblLanguage_text;

	public String RESTAPI_Help;

	public String Language;
	
	public String TimeZone;
	
	public String ShowNullCharacters;

	public String EnterYourPasswd;

	public String PasswordDoNotMatch;
	public String inValidComplextyPasswd;

	public String ChangedPassword;

	public String PleaseInputName;

	public String PleaseInputEamil;

	public String EditorPreferen_AddComma;

	public String WhenClickingOnColumnName;
	
	public String SortData;
	public String CopyColumnNameToEditor;
	
	public String RDBPreferencePage_lblNumberColumnAdd_text;
	
	public String General;
	
	public String PleaseCreateOraclePlanTable;
	
	public String DatabaseSetting;
	
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
