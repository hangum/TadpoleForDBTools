/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.browser.rap.core.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hangum.db.browser.rap.core.editors.main.PartQueryUtil;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.system.UserDBDAO;
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
