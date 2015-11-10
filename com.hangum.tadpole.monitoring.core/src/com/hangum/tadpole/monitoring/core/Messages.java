package com.hangum.tadpole.monitoring.core;

import org.eclipse.osgi.util.NLS;
import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.monitoring.core.messages"; //$NON-NLS-1$
	public String AddScheduleDialog_0;
	public String AddScheduleDialog_1;
	public String AddScheduleDialog_10;
	public String AddScheduleDialog_12;
	public String AddScheduleDialog_14;
	public String AddScheduleDialog_15;
	public String AddScheduleDialog_16;
	public String AddScheduleDialog_17;
	public String AddScheduleDialog_19;
	public String AddScheduleDialog_20;
	public String AddScheduleDialog_21;
	public String AddScheduleDialog_23;
	public String AddScheduleDialog_25;
	public String AddScheduleDialog_26;
	public String AddScheduleDialog_27;
	public String AddScheduleDialog_3;
	public String AddScheduleDialog_4;
	public String AddScheduleDialog_5;
	public String AddScheduleDialog_7;
	public String AddScheduleDialog_8;
	public String AddScheduleDialog_9;
	public String AddSQLDialog_0;
	public String AddSQLDialog_1;
	public String AddSQLDialog_2;
	public String AddSQLDialog_3;
	public String AddSQLDialog_4;
	public String AddSQLDialog_6;
	public String AddSQLDialog_7;
	public String AddSQLDialog_8;
	public String ScheduleEditor_1;
	public String ScheduleEditor_10;
	public String ScheduleEditor_11;
	public String ScheduleEditor_12;
	public String ScheduleEditor_13;
	public String ScheduleEditor_2;
	public String ScheduleEditor_3;
	public String ScheduleEditor_4;
	public String ScheduleEditor_6;
	public String ScheduleEditor_8;
	public String ScheduleEditor_9;
	public String ScheduleEditor_tltmModify_text;

	// static {
	// NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	// }
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
