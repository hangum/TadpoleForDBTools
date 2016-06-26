/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.execute.sub;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.hangum.tadpole.db.vendor.cubrid.CubridExecutePlanUtils;
import com.hangum.tadpole.db.vendor.oracle.OracleExecutePlanUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;

/**
 * Query plan을 관리합니다.
 * 
 * @author hangum
 *
 */
public class ExecuteQueryPlan {
	private static final Logger logger = Logger.getLogger(ExecuteQueryPlan.class);

	/**
	 * execute plan 을 실행합니다.
	 * 
	 * @param reqQuery
	 * @param userDB
	 * @param planTableName
	 * @param strNullValue 
	 * 
	 * @throws Exception
	 */
	public static QueryExecuteResultDTO runSQLExplainPlan(final RequestQuery reqQuery, 
									final UserDBDAO userDB, 
									final String planTableName, String strNullValue
						) throws Exception {
	
		QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();

		java.sql.Connection javaConn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		java.sql.Statement stmt = null;
		
		try {
			if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
				TajoConnectionManager manager = new TajoConnectionManager();
				rsDAO = manager.executeQueryPlan(userDB, reqQuery.getSql(), strNullValue);
			} else {
				
				javaConn = TadpoleSQLManager.getInstance(userDB).getDataSource().getConnection();
								
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT) {
					
					rsDAO.setColumnName(CubridExecutePlanUtils.getMapColumns());
					rsDAO.setDataList(CubridExecutePlanUtils.getMakeData(CubridExecutePlanUtils.plan(userDB, reqQuery.getSql())));
					
					return rsDAO;
					
				} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT) {
					String statement_id = "tadpole"; //$NON-NLS-1$
					
					try {
						// generation to statement id for query plan. 
						pstmt = javaConn.prepareStatement("select USERENV('SESSIONID') from dual "); //$NON-NLS-1$
						rs = pstmt.executeQuery(); 
						if (rs.next()) statement_id = rs.getString(1);
					} finally {
						if(pstmt != null) pstmt.close();
						if(rs != null) rs.close();
					}
					
					try {
						pstmt = javaConn.prepareStatement("delete from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						pstmt.execute();
					} finally {
						if(pstmt != null) pstmt.close();
					}
					
					// 플랜결과를 디비에 저장합니다.
					OracleExecutePlanUtils.plan(userDB, reqQuery.getSql(), planTableName, javaConn, statement_id);
					// 저장된 결과를 가져와서 보여줍니다.
					StringBuffer sbQuery = new StringBuffer();
					sbQuery.append("SELECT TRIM(LEVEL), LPAD (' ', LEVEL - 1)||operation||' '||options||' on '||object_name \"Query\", ")
							.append("		cost \"Cost\", cardinality \"Rows\", bytes \"Bytes\", decode(level,1,0,position) \"Pos\" ")
							.append(String.format(" FROM %s", planTableName))
							.append(" CONNECT BY prior id = parent_id ")
							.append(" AND prior statement_id = statement_id ")
							.append(" START WITH id = 0 ")
							.append(String.format(" AND statement_id = '%s'", statement_id))
							.append(" ORDER BY id");
					if(logger.isDebugEnabled()) logger.debug(sbQuery);
					pstmt = javaConn.prepareStatement(sbQuery.toString());
					rs = pstmt.executeQuery(); 
					
				 } else if(DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine() || DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
					 stmt = javaConn.createStatement();
					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "ON")); //$NON-NLS-1$
				
					 pstmt = javaConn.prepareStatement(reqQuery.getSql());
					 rs = pstmt.executeQuery();

					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "OFF")); //$NON-NLS-1$
				} else {
					pstmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDB, reqQuery.getSql()));
					rs = pstmt.executeQuery();
				}
				
				rsDAO = new QueryExecuteResultDTO(
						userDB, reqQuery.getSql(), true, rs, 1000, strNullValue/*, true*/);
			}
			
			return rsDAO;
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
	}

}
