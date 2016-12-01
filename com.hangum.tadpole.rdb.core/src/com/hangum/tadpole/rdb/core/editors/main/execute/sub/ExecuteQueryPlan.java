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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_STATEMENT_TYPE;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtils;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.utils.plan.CubridExecutePlanUtils;
import com.hangum.tadpole.rdb.core.editors.main.utils.plan.OracleExecutePlanUtils;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;

/**
 * Query plan을 관리합니다.
 * 
 * @author hangum
 *
 */
public class ExecuteQueryPlan {
	private static final Logger logger = Logger.getLogger(ExecuteQueryPlan.class);
//	private static final String PLAN_TABLE_NOT_FOUND = Messages.get().PLAN_TABLE_NOT_FOUND;
	/**
	 * execute plan 을 실행합니다.
	 * 
	 * @param reqQuery
	 * @param userDB
	 * @param planTableName
	 * 
	 * @throws Exception
	 */
	public static QueryExecuteResultDTO runSQLExplainPlan(final UserDBDAO userDB,
									final RequestQuery reqQuery, 
									final String planTableName
						) throws Exception {
	
		QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();

		java.sql.Connection javaConn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		java.sql.Statement stmt = null;
		
		try {
			if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup()) {
				TajoConnectionManager manager = new TajoConnectionManager();
				rsDAO = manager.executeQueryPlan(userDB, reqQuery.getSql(), reqQuery.getSqlStatementType(), reqQuery.getStatementParameter());
			} else {
				
				javaConn = TadpoleSQLManager.getInstance(userDB).getDataSource().getConnection();
								
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(DBGroupDefine.CUBRID_GROUP == userDB.getDBGroup()) {
					
					rsDAO.setColumnName(CubridExecutePlanUtils.getMapColumns());
					rsDAO.setDataList(CubridExecutePlanUtils.getMakeData(CubridExecutePlanUtils.plan(userDB, reqQuery)));
					
					return rsDAO;
					
				} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
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
					} catch(Exception e) {
						logger.error("Plan table query", e);
//						throw new Exception(String.format(PLAN_TABLE_NOT_FOUND, planTableName));
					} finally {
						if(pstmt != null) pstmt.close();
					}
					
					// 플랜결과를 디비에 저장합니다.
					OracleExecutePlanUtils.plan(userDB, reqQuery, planTableName, javaConn, statement_id);
					// 저장된 결과를 가져와서 보여줍니다.
					StringBuffer sbQuery = new StringBuffer();
					sbQuery.append("SELECT ")
				 	        //.append("         LPAD ('　', (LEVEL - 1) * 2 , '　')||row_number() over(partition by statement_id  order by level desc, position )||'.'||operation   ")
							.append("       operation   ")
							.append("		||(case when options is null then '' else ' '||options end) ")
							.append("		||(case when optimizer is null then '' else ' ('||initcap(optimizer)||')' end) as \"Operation\"  ")
							.append("		, object_owner||'.'||object_name as \"Object\" ")
							.append("		, cost as \"Cost\" ")
							.append("		, cardinality as \"Rows\" ")
							.append("		, bytes as \"Bytes\" ")
							.append("		, level - 1 as \"Pos\" ")
							.append("		, access_predicates as \"Access\" ")
							.append("		, filter_predicates as \"Filter\" ")
							.append("		, object_type as \"ObjectType\" ")
							.append(String.format(" FROM %s", planTableName))
							.append(" CONNECT BY prior id = parent_id ")
							.append(" AND prior statement_id = statement_id ")
							.append(" START WITH id = 0 ")
							.append(String.format(" AND statement_id = '%s'", statement_id))
							.append(" ORDER BY id");
					if(logger.isDebugEnabled()) logger.debug(sbQuery);
					pstmt = javaConn.prepareStatement(sbQuery.toString());
					rs = pstmt.executeQuery(); 
					
				 } else if(DBGroupDefine.MSSQL_GROUP == userDB.getDBGroup()) {
					 stmt = javaConn.createStatement();
					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "ON")); //$NON-NLS-1$
				
					 pstmt = javaConn.prepareStatement(reqQuery.getSql());
					 if(reqQuery.getSqlStatementType() == SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
						 final Object[] statementParameter = reqQuery.getStatementParameter();
						 for (int i=1; i<=statementParameter.length; i++) {
							 pstmt.setObject(i, statementParameter[i-1]);					
						 }	
					 }
					 rs = pstmt.executeQuery();

					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "OFF")); //$NON-NLS-1$
				} else {
					pstmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDB, reqQuery.getSql()));
					if(reqQuery.getSqlStatementType() == SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
						 final Object[] statementParameter = reqQuery.getStatementParameter();
						 for (int i=1; i<=statementParameter.length; i++) {
							 pstmt.setObject(i, statementParameter[i-1]);					
						 }	
					 }
					rs = pstmt.executeQuery();
				}
				
				rsDAO = new QueryExecuteResultDTO(userDB, reqQuery.getSql(), true, rs, 1000);
				
				try{
					// pstmt를 닫기전에 데이터가 추가로 있으면 기존 자료에 덧붙인다.
					TadpoleResultSet dataList = rsDAO.getDataList();
					while (pstmt.getMoreResults()) {
						dataList.appendList(ResultSetUtils.getResultToList(true, pstmt.getResultSet() , 1000, 0));
					}
					rsDAO.setDataList(dataList);
				}catch(Exception e){
					// getMoreResults 가 지원되지 않거나 오류 발생시 로그만 남기고 다음 처리 절차를 진행하도록한다.
					logger.error("Execute Plan" + e);
				}
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
