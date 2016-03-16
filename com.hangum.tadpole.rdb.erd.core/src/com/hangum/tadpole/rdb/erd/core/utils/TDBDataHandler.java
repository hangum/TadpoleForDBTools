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
package com.hangum.tadpole.rdb.erd.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 시스템 쿼리를 가져오기위해.
 * 
 * @author hangum
 *
 */
public class TDBDataHandler {
	/**
	 * table의 컬럼 정보를 가져옵니다.
	 * 
	 * @param strTBName
	 * @return
	 * @throws Exception
	 */
	public static List<TableColumnDAO> getColumns(final UserDBDAO userDB, final TableDAO table) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("db", userDB.getDb());
		param.put("schema", table.getSchema_name());
		if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
			param.put("table", table.getSysName());
		} else if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
			param.put("user", StringUtils.substringBefore(table.getName(), "."));
			param.put("table", StringUtils.substringAfter(table.getName(), "."));
		} else {
			param.put("table", table.getName());
		}

		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			return new TajoConnectionManager().tableColumnList(userDB, param);
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			return sqlClient.queryForList("tableColumnList", param);
		}
	}
}
