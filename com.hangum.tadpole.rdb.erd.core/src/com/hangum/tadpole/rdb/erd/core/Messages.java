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
package com.hangum.tadpole.rdb.erd.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.rdb.erd.core.messages"; //$NON-NLS-1$
	public String AutoLayoutAction_0;
	public String AutoLayoutAction_2;
	public String AutoLayoutAction_3;
	public String ERDRefreshAction_0;
	public String ERDRefreshAction_4;
	public String ERDViewStyleAction_0;
	public String ERDViewStyleDailog_0;
	public String ERDViewStyleDailog_1;
	public String ERDViewStyleDailog_10;
	public String ERDViewStyleDailog_11;
	public String ERDViewStyleDailog_12;
	public String ERDViewStyleDailog_13;
	public String ERDViewStyleDailog_2;
	public String ERDViewStyleDailog_3;
	public String ERDViewStyleDailog_32;
	public String ERDViewStyleDailog_4;
	public String ERDViewStyleDailog_6;
	public String ERDViewStyleDailog_8;
	public String ERDViewStyleDailog_9;
	public String TableTransferDropTargetListener_1;
	public String TadpoleEditor_0;
	public String TadpoleEditor_1;
	public String TadpoleEditor_12;
	public String TadpoleEditor_3;
	public String TadpoleEditor_9;
	public String TadpoleModelUtils_2;
	public String Confirm;
	public String Error;
	public String Warning;
	public String ShowColumn;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
