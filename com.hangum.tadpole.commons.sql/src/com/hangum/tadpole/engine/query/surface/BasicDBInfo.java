/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.surface;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_STATEMENT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * JDBC 메타데이터를 이용하여 스키마, 테이블, 컬럼 정보를 요청합니다, 
 * 
 * @author hangum
 *
 */
public abstract class BasicDBInfo implements ConnectionInterfact {
	private static final Logger logger = Logger.getLogger(BasicDBInfo.class);
	
	public abstract Connection getConnection(final UserDBDAO userDB) throws Exception;
	
	/**
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public String getKeyworkd(final UserDBDAO userDB) throws Exception {
		String strKeyWord = "";
		
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			strKeyWord = javaConn.getMetaData().getSQLKeywords();
		} finally {
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
		
		return strKeyWord;
	}
	
	/**
	 * not select 
	 * 
	 * @param userDB
	 * @param sqlQuery
	 * @return
	 */
	public int executeUpdate(UserDBDAO userDB, String sqlQuery) throws Exception {
		Statement statement = null;
		
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			statement = javaConn.createStatement();
			return statement.executeUpdate(sqlQuery);
		} finally {
			try { if(statement != null) statement.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
	}
	
	/**
	 * execute query plan
	 * 
	 * @param objects 
	 * @param sql_STATEMENT_TYPE
	 * @param statementParameter
	 * 
	 * @return
	 * @throws Exception
	 */
	public QueryExecuteResultDTO executeQueryPlan(UserDBDAO userDB, String strQuery, SQL_STATEMENT_TYPE sql_STATEMENT_TYPE, Object[] statementParameter) throws Exception {
		return select(userDB, PartQueryUtil.makeExplainQuery(userDB, strQuery), statementParameter, 1000);
	}
	
	/**
	 * execute update
	 * 
	 * @param javaConn
	 * @param userDB
	 * @param string
	 * @param name
	 * @throws Exception
	 */
	public int executeUpdate(UserDBDAO userDB, String string, String name) throws Exception {
		Statement statement = null;
		
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			String quoteString = javaConn.getMetaData().getIdentifierQuoteString();
			
			statement = javaConn.createStatement();
			return statement.executeUpdate(String.format(string, quoteString + name + quoteString));
		} finally {
			try { if(statement != null) statement.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
	}
	
	/**
	 * 연결 테스트 합니다.
	 * 
	 * @param userDB
	 */
	public void connectionCheck(UserDBDAO userDB) throws Exception {
		
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			DatabaseMetaData dbmd = javaConn.getMetaData();
			// just test
	    	dbmd.getCatalogSeparator();
	    	
		} catch(Exception e) {
			logger.error("connection check", e);
			throw e;
		} finally {
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
	}
	
	/**
	 * table 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public List<TableDAO> tableList(UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = new ArrayList<TableDAO>();
		
		ResultSet rs = null;
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			DatabaseMetaData dbmd = javaConn.getMetaData();
	    	rs = dbmd.getTables(null, null, "%", null);

	    	while(rs.next()) {
	    		String strTBName = rs.getString("TABLE_NAME");
	    		String strComment = rs.getString("REMARKS");
//	    		logger.debug( rs.getString("TABLE_CAT") );
//	    		logger.debug( rs.getString("TABLE_SCHEM") );
//	    		logger.debug( rs.getString("TABLE_NAME") );
//	    		logger.debug( rs.getString("TABLE_TYPE") );
//	    		logger.debug( rs.getString("REMARKS") );
	    		
	    		TableDAO tdao = new TableDAO(strTBName, strComment);
	    		showTables.add(tdao);
	    	}
	    	
	    	return showTables;
		} catch(Exception e) {
			logger.error("table list", e);
			throw e;
		} finally {
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
		
	}

	/**
	 * table의 컬컴 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @param tbName
	 * @throws Exception
	 */
	public List<TableColumnDAO> tableColumnList(UserDBDAO userDB, Map<String, String> mapParam) throws Exception {
		List<TableColumnDAO> showTableColumns = new ArrayList<TableColumnDAO>();
		ResultSet rs = null;
		
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			DatabaseMetaData dbmd = javaConn.getMetaData();
	    	rs = dbmd.getColumns(null, null, mapParam.get("table"), null);

	    	while(rs.next()) {
	    		TableColumnDAO tcDAO = new TableColumnDAO();
	    		tcDAO.setName(rs.getString("COLUMN_NAME"));
	    		tcDAO.setType(rs.getString("TYPE_NAME"));
	    		tcDAO.setNull(rs.getString("IS_NULLABLE"));
	    		tcDAO.setComment(rs.getString("REMARKS"));
	    		
	    		showTableColumns.add(tcDAO);
	    	}
	    	
	    	return showTableColumns;
		} catch(Exception e) {
			logger.error(mapParam.get("table") + " table column", e);
			throw e;
		} finally {
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
		
	}
	
	/**
	 * select
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @param statementParameter 
	 * @param limitCount
	 * 
	 * @throws Exception
	 */
	public QueryExecuteResultDTO select( UserDBDAO userDB, String requestQuery, Object[] statementParameter, int limitCount) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("\t * Query is [ " + requestQuery );
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Connection javaConn = null;
		try {
			javaConn = getConnection(userDB);
			pstmt = javaConn.prepareStatement(requestQuery);
			if(statementParameter != null) {
				for (int i=1; i<=statementParameter.length; i++) {
					pstmt.setObject(i, statementParameter[i-1]);					
				}
			}
			rs = pstmt.executeQuery();
			
			return new QueryExecuteResultDTO(userDB, requestQuery, true, rs, limitCount);
		} catch(Exception e) {
			logger.error("Tajo select", e);
			throw e;
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
	}
}
