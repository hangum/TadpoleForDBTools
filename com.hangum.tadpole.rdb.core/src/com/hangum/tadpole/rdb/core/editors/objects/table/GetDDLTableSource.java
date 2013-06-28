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

import java.util.HashMap;
import java.util.Map;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * DDL Source
 * 
 * @author hangum
 *
 */
public class GetDDLTableSource {

	/**
	 * DDL Source view
	 * 
	 * 	Table, View 소스.
	 * 
	 * @param userDB
	 * @param actionType
	 * @param objectName
	 */
	public static String getSource(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType, String objectName) throws Exception {
		
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
			if(PublicTadpoleDefine.DB_ACTION.TABLES == actionType) {
				return ""+client.queryForObject("getTableScript", objectName);
			} else if(PublicTadpoleDefine.DB_ACTION.VIEWS == actionType) {
				return ""+client.queryForObject("getViewScript", objectName);
			}
			
			throw new Exception("Not support Database");
		} else {
			throw new Exception("Not Support Database");
		}
	}

	/**
	 * procedure source
	 * 
	 * @param userDB
	 * @param actionType
	 * @param index_name
	 * @param table_name
	 * @return
	 */
	public static String getIndexSource(UserDBDAO userDB, DB_ACTION actionType, String index_name, String table_name) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("index_name", index_name);
		paramMap.put("table_name", table_name);
		
		return ""+client.queryForObject("getIndexScript", paramMap);
	}

}
