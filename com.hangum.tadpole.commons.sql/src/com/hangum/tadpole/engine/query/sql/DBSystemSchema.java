/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Get db system schema
 * 
 * @author hangum
 *
 */
public class DBSystemSchema {
	
	/**
	 * get view List
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<String> getViewList(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ) return new ArrayList<String>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("viewList", userDB.getDb()); //$NON-NLS-1$
	}

	/**
	 * get function list
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<ProcedureFunctionDAO> getFunctionList(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ||
				userDB.getDBDefine() == DBDefine.SQLite_DEFAULT 
		) return new ArrayList<ProcedureFunctionDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("functionList", userDB.getDb()); //$NON-NLS-1$
	}
}
