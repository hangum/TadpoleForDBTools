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
	public static String RDBPreferencePage_btnCreatePlanTable_text;
	public static String RDBPreferencePage_label_text;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
