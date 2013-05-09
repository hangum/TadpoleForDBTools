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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * GET DDL Table Source
 * 
 * @author hangum
 *
 */
public class GetDDLTableSource {

	/**
	 * ddl table source
	 * 
	 * @param userDB
	 * @param tableName
	 */
	public static String getSource(UserDBDAO userDB, String tableName) throws Exception {
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			return ""+client.queryForObject("getTableScript", tableName);
		} else {
			throw new Exception("Not Support DB");
		}
	}

}
