/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.manager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDMLCommand;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * Initialize UserDB
 * 
 * @author hangum
 *
 */
public class InitializeDB {
	private static final Logger logger = Logger.getLogger(InitializeDB.class);
	
	/**
	 * initialize DB information
	 * 
	 * @param userDB
	 * @return
	 */
	public static UserDBDAO dbInfo(UserDBDAO userDB) {
		if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) {
			
			try {
				//
				// db readonly 여부
				//
				QueryExecuteResultDTO endStatus = ExecuteDMLCommand.executeQuery(userDB, "SHOW global variables like 'read_only'", 0, 20);
				List<Map<Integer, Object>> tdbResultSet = endStatus.getDataList().getData();
				String strReadonly = ""+tdbResultSet.get(0).get(1);
				if("OFF".equals(strReadonly)) userDB.setReadonly("NO"); 
				else userDB.setReadonly("YES");
			} catch (Exception e) {
				logger.error("mysql connection initialize: " + e.getMessage());
			}
		}
		
		return userDB;
	}
	
	/**
	 * initialize DB information
	 * 
	 * @param userDB
	 * @return
	 */
	public static String dbCharacterSetDatabase(UserDBDAO userDB) {
		String strCharacterSetDatabase = "";
		
		if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) {
			
			try {
				QueryExecuteResultDTO endStatus = ExecuteDMLCommand.executeQuery(userDB, "show variables like 'character_set_database'", 0, 20);
				List<Map<Integer, Object>> tdbResultSet = endStatus.getDataList().getData();
				strCharacterSetDatabase = ""+tdbResultSet.get(0).get(1);
				
				if(logger.isDebugEnabled()) logger.debug(String.format("**** get database character set %s ", strCharacterSetDatabase));
			} catch (Exception e) {
				logger.error("mysql Character " + e.getMessage());
			}
		}
		
		return strCharacterSetDatabase;
	}
}
