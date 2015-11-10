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

import org.eclipse.osgi.util.NLS;
import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.engine.messages"; //$NON-NLS-1$
	public String DBLockDialog_0;
	public String DBLockDialog_1;
	public String DBLockDialog_2;
	public String DBLockDialog_3;
	public String DBLockDialog_4;
	public String DBLockDialog_5;
	public String OracleObjectCompileUtils_0;
	public String ProcedureExecuterManager_0;
	public String ProcedureExecuterManager_4;
	public String ProcedureExecuterManager_6;
	public String ProcedureExecuterManager_error;
	public String TadpoleSQLManager_0;
	public String SQLHistoryCreateColumn_0;
	public String SQLHistoryCreateColumn_1;
	public String SQLHistoryCreateColumn_2;
	public String SQLHistoryCreateColumn_3;
	public String SQLHistoryCreateColumn_4;
	public String SQLHistoryCreateColumn_5;
	public String SQLHistoryCreateColumn_6;
	public String SQLHistoryCreateColumn_7;
	public String SQLHistoryCreateColumn_8;

	public String ResourceSaveDialog_0;
	public String ResourceSaveDialog_1;
	public String ResourceSaveDialog_10;
	public String ResourceSaveDialog_11;
	public String ResourceSaveDialog_12;
	public String ResourceSaveDialog_13;
	public String ResourceSaveDialog_14;
	public String ResourceSaveDialog_16;
	public String ResourceSaveDialog_19;
	public String ResourceSaveDialog_2;
	public String ResourceSaveDialog_20;
	public String ResourceSaveDialog_21;
	public String ResourceSaveDialog_3;
	public String ResourceSaveDialog_5;
	public String ResourceSaveDialog_7;
	public String ResourceSaveDialog_8;
	public String TadpoleSystem_UserDBQuery_11;
	public String TadpoleSystem_UserDBQuery_4;
	public String TadpoleSystem_UserDBQuery_7;
	public String TadpoleSystem_UserDBResource_12;
	public String TadpoleSystem_UserDBResource_3;
	public String TadpoleSystem_UserDBResource_6;
	public String TadpoleSystem_UserDBResource_8;
	public String TadpoleSystem_UserDBResource_9;
	public String TadpoleSystem_UserQuery_1;
	public String TadpoleSystem_UserQuery_11;
	public String TadpoleSystem_UserQuery_15;
	public String TadpoleSystem_UserQuery_2;
	public String TadpoleSystem_UserQuery_4;
	public String TadpoleSystem_UserQuery_7;
	public String TadpoleSystem_UserQuery_9;
	public String TadpoleSystemConnector_10;
	public String TadpoleSystemConnector_11;
	public String TadpoleSystemConnector_13;
	public String TadpoleSystemConnector_14;
	public String TadpoleSystemConnector_15;
	public String TadpoleSystemConnector_16;
	// public String TadpoleSystemConnector_2;
	public String TadpoleSystemConnector_4;
	public String TadpoleSystemConnector_5;
	public String TadpoleSystemConnector_6;
	public String TadpoleSystemConnector_7;
	public String TadpoleSystemConnector_9;

	public String TadpoleSystem_UserQuery_0;
	public String TadpoleSystem_UserQuery_3;
	public String TadpoleSystem_UserQuery_5;
	public String TadpoleSystem_UserQuery_6;
	public String ResourceSaveDialog_lblApiName_text;
	public String ResourceSaveDialog_lblUseApi_text;

	// static {
	// NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	// }
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
