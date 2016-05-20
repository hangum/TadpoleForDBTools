package com.hangum.tadpole.monitoring.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.monitoring.core.messages"; //$NON-NLS-1$
	public String Title;
	public String AddScheduleDialog_12;
	public String AddScheduleDialog_15;
	public String AddScheduleDialog_17;
	public String AddScheduleDialog_19;
	public String Confirm;
	public String AddScheduleDialog_21;
	public String AddScheduleDialog_23;
	public String AddScheduleDialog_3;
	public String AddScheduleDialog_4;
	public String AddScheduleDialog_5;
	public String AddScheduleDialog_7;
	public String AddScheduleDialog_8;
	public String AddScheduleDialog_9;
	public String AddSQLDialog_0;
	public String Description;
	public String SQL;
	public String Error;
	public String AddSQLDialog_4;
	public String AddSQLDialog_6;
	public String OK;
	public String Cancel;
	public String ScheduleEditor_1;
	public String ScheduleEditor_11;
	public String ScheduleEditor_12;
	public String ScheduleEditor_13;
	public String ScheduleEditor_2;
	public String ScheduleEditor_4;
	public String ScheduleEditor_6;
	public String ScheduleEditor_8;
	public String ScheduleEditor_9;
	public String ScheduleEditor_tltmModify_text;
	public String Warning;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
