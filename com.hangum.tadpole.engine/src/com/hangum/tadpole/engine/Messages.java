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
package com.hangum.tadpole.engine;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.engine.messages"; //$NON-NLS-1$

	public String Gullim;
	public String overflowUseService;
	public String AlreadyUseService;
	public String DBLockDialog_0;
	public String OracleObjectCompileUtils_0;
	public String ProcedureExecuterManager_0;
	public String ProcedureExecuterManager_4;
	public String ProcedureExecuterManager_6;

	public String SharedType;
	public String ResourceSaveDialog_10;
	public String ShowURL;
	public String ResourceSaveDialog_16;
	public String ResourceSaveDialog_19;
	public String ResourceSaveDialog_21;
	public String ResourceSaveDialog_3;
	public String ResourceSaveDialog_8;
	public String TadpoleSystem_UserDBResource_6;
	public String TadpoleSystem_UserDBResource_8;

	public String TadpoleSystem_UserQuery_0;
	public String TadpoleSystem_UserQuery_3;
	public String TadpoleSystem_UserQuery_5;
	public String APIURL;
	public String IsUseAPI;
	public String ServiceBill;
	public String doesNotAutority;

	public String OTPEmpty;
	
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
