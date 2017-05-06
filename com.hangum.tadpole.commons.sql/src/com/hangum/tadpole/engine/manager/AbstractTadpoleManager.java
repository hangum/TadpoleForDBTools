/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.manager;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

public class AbstractTadpoleManager {
	private static final Logger logger = Logger.getLogger(AbstractTadpoleManager.class);
	
	/**
	 * change schema
	 * 
	 * @param conn
	 * @param strSchema
	 */
	public static void changeSchema(UserDBDAO userDB, Connection conn) {
		setConnectionInitialize(userDB, conn);
	}
	
	/**
	 * 
	 * @param userDB
	 * @param conn
	 */
	public static void setConnectionInitialize(final UserDBDAO userDB, final Connection conn) {
		if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) {
			// show variables like 'character_set_database' 에서  값을 가져와서 
			// set names 의 값을 설정해준다.
			String strCharacterSetDatabase = InitializeDB.dbCharacterSetDatabase(userDB);
			
			Statement statement = null;
			try {
				statement = conn.createStatement();
				statement.execute(String.format("set names `%s`", strCharacterSetDatabase));
			} catch (Exception e) {
				logger.error("mysql connection initialize: " + e.getMessage());
			} finally {
				try { if(statement != null) statement.close(); } catch(Exception e) {}
			}
			
		}

	}
}
