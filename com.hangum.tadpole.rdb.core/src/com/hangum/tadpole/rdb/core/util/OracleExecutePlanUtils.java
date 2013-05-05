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
package com.hangum.tadpole.rdb.core.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.PartQueryUtil;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle execute plan
 * 
 * @author hangum
 *
 */
public class OracleExecutePlanUtils {

	/**
	 * oracle query plan을 실행합니다. 
	 * 
	 * @param userDB
	 * @param sql
	 * @param planTableName
	 * @throws Exception
	 */
	public static void plan(UserDBDAO userDB, String sql, String planTableName) throws Exception {
		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
				
			stmt = javaConn.prepareStatement( String.format(PartQueryUtil.makeExplainQuery(userDB,  sql), planTableName));
			rs = stmt.executeQuery();
			
		} finally {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(javaConn != null) javaConn.close();
		}
		
	}
}
