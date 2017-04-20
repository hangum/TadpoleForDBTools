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
package com.hangum.tadpole.rdb.core.editors.main.utils.plan;

import java.sql.PreparedStatement;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_STATEMENT_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;

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
	 * @param reqQuery
	 * @param planTableName
	 * @throws Exception
	 */
	public static void plan(UserDBDAO userDB, RequestQuery reqQuery, String planTableName, java.sql.Connection javaConn, String statement_id  ) throws Exception {
		
		PreparedStatement pstmt = null;
		try {
			String query = PartQueryUtil.makeExplainQuery(userDB, reqQuery.getSql());
			query = StringUtils.replaceOnce(query, PublicTadpoleDefine.STATEMENT_ID, statement_id);
			query = StringUtils.replaceOnce(query, PublicTadpoleDefine.DELIMITER, planTableName);
			
			pstmt = javaConn.prepareStatement( query );
			if(reqQuery.getSqlStatementType() == SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
				final Object[] statementParameter = reqQuery.getStatementParameter();
				for (int i=1; i<=statementParameter.length; i++) {
					pstmt.setObject(i, statementParameter[i-1]);					
				}	
			}
			pstmt.execute();
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
		}
		
	}
}
