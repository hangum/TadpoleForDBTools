/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.preference.messages"; //$NON-NLS-1$
	
	public static String DefaultPreferencePage_0;
	public static String DefaultPreferencePage_1;
	public static String DefaultPreferencePage_2;

	public static String UserInfoPerference_0;
	public static String UserInfoPerference_6;
	public static String UserInfoPerference_8;
	
	public static String DefaultPreferencePage_other_labelText;
	public static String DefaultPreferencePage_stringFieldEditor_stringValue;
	public static String DefaultPreferencePage_other_labelText_1;

	public static String GeneralPreferencePage_0;

	public static String GeneralPreferencePage_2;
	public static String RDBPreferencePage_0;

	public static String RDBPreferencePage_3;

	public static String RDBPreferencePage_5;

	public static String RDBPreferencePage_btnCreatePlanTable_text;
	public static String GeneralPreferencePage_lblExportDilimit_text;
	public static String GeneralPreferencePage_text_text;
	public static String GeneralPreferencePage_lblHomePage_text;
	public static String GeneralPreferencePage_lblHomePageUse_text;
	public static String GeneralPreferencePage_btnCheckButton_text;
	public static String GeneralPreferencePage_lblStandalonePort_text;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
