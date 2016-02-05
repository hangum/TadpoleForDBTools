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

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.util.bander.cubrid.CubridExecutePlanUtils;
import com.hangum.tadpole.rdb.core.util.bander.oracle.OracleExecutePlanUtils;

/**
 * Query plan을 관리합니다.
 * 
 * @author hangum
 *
 */
public class ExecuteQueryPlan {

	/**
	 * execute plan 을 실행합니다.
	 * 
	 * @param reqQuery
	 * @param userDBDAO
	 * @param planTableName
	 * 
	 * @throws Exception
	 */
	public static QueryExecuteResultDTO runSQLExplainPlan(final RequestQuery reqQuery, 
									final UserDBDAO userDBDAO, 
									final String planTableName
						) throws Exception {
	
		QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();

		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		java.sql.Statement stmt = null;
		
		try {
			javaConn = TadpoleSQLManager.getInstance(userDBDAO).getDataSource().getConnection();
							
			// 큐브리드 디비이면 다음과 같아야 합니다.
			if(userDBDAO.getDBDefine() == DBDefine.CUBRID_DEFAULT) {
				
				rsDAO.setColumnName(CubridExecutePlanUtils.getMapColumns());
				rsDAO.setDataList(CubridExecutePlanUtils.getMakeData(CubridExecutePlanUtils.plan(userDBDAO, reqQuery.getSql())));
				
				return rsDAO;
				
			} else if(userDBDAO.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDBDAO.getDBDefine() == DBDefine.TIBERO_DEFAULT) {					
				// generation to statement id for query plan. 
				pstmt = javaConn.prepareStatement("select USERENV('SESSIONID') from dual "); //$NON-NLS-1$
				rs = pstmt.executeQuery(); 
				String statement_id = "tadpole"; //$NON-NLS-1$
				if (rs.next()) statement_id = rs.getString(1);
				
				pstmt = javaConn.prepareStatement("delete from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				pstmt.execute(); 
				
				// 플랜결과를 디비에 저장합니다.
				OracleExecutePlanUtils.plan(userDBDAO, reqQuery.getSql(), planTableName, javaConn, pstmt, statement_id);
				// 저장된 결과를 가져와서 보여줍니다.
				pstmt = javaConn.prepareStatement("select * from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				rs = pstmt.executeQuery(); 
			 } else if(DBDefine.MSSQL_8_LE_DEFAULT == DBDefine.getDBDefine(userDBDAO) || DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDBDAO)) {
				 stmt = javaConn.createStatement();
				 stmt.execute(PartQueryUtil.makeExplainQuery(userDBDAO, "ON")); //$NON-NLS-1$

				 pstmt = javaConn.prepareStatement(reqQuery.getSql());
				 rs = pstmt.executeQuery();

				 stmt.execute(PartQueryUtil.makeExplainQuery(userDBDAO, "OFF")); //$NON-NLS-1$
			} else {
				pstmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDBDAO, reqQuery.getSql()));
				rs = pstmt.executeQuery();
			}

			rsDAO = new QueryExecuteResultDTO(
					userDBDAO, true, rs, 1000/*, true*/);
			return rsDAO;
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}
		}
	}

}
