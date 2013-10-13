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
package com.hangum.tadpole.sql.util.sqlscripts.scripts;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * MSSQL Ver 8.0 이상의 DDL Script를 관리하는 아이.
 * 
 * @author hangum
 *
 */
public class MSSQLDDLScript extends MSSQL_8_LE_DDLScript {

	/**
	 * @param userDB
	 * @param actionType
	 */
	public MSSQLDDLScript(UserDBDAO userDB, DB_ACTION actionType) {
		super(userDB, actionType);
	}

}
