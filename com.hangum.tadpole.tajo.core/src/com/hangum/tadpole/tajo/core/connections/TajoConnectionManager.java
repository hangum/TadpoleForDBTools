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
package com.hangum.tadpole.tajo.core.connections;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.surface.ConnectionInterfact;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.tajo.core.connections.manager.ConnectionPoolManager;

/**
 * apache tajo connection manager
 * 현재는 jdbc이지만, 나중에는 native driver를 쓸수 있도록 별로 분리..
 * 
 * @author hangum
 *
 */
public class TajoConnectionManager implements ConnectionInterfact {
	private static final Logger logger = Logger.getLogger(TajoConnectionManager.class);
	
	/**
	 * java.sql.connection을 생성하고 관리합니다.
	 * 
	 * @param userDB
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getInstance(final UserDBDAO userDB) throws Exception {
		java.sql.Connection javaConn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			
		return javaConn;
	}
	
	/**
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static String getKeyworkd(final UserDBDAO userDB) throws Exception {
		String strKeyWord = "";
		java.sql.Connection javaConn = getInstance(userDB);
		try {
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
	public void executeUpdate(UserDBDAO userDB, String sqlQuery) throws Exception {
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			javaConn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			statement = javaConn.createStatement();
			statement.executeUpdate(sqlQuery);
		} finally {
			try { if(statement != null) statement.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
	}
	
	/**
	 * execute query plan
	 * 
	 * @return
	 * @throws Exception
	 */
	public QueryExecuteResultDTO executeQueryPlan(UserDBDAO userDB, String strQuery, String strNullValue) throws Exception {
		return select(userDB, PartQueryUtil.makeExplainQuery(userDB, strQuery), 1000, strNullValue);
	}
	
	/**
	 * execute update
	 * 
	 * @param userDB
	 * @param string
	 * @param name
	 * @throws Exception
	 */
	public void executeUpdate(UserDBDAO userDB, String string, String name) throws Exception {
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			javaConn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			String quoteString = javaConn.getMetaData().getIdentifierQuoteString();
			
			statement = javaConn.createStatement();
			statement.executeUpdate(String.format(string, quoteString + name + quoteString));
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
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
	    	rs = dbmd.getTables(null, null, null, null);
	    	
		} catch(Exception e) {
			logger.error("connection check", e);
			throw e;
		} finally {
			if(rs != null) rs.close();
			if(conn != null) conn.close();
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
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
	    	rs = dbmd.getTables(userDB.getDb(), null, null, null);

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
	    	
		} catch(Exception e) {
			logger.error("table list", e);
			throw e;
		} finally {
			if(rs != null) rs.close();
			if(conn != null) conn.close();
		}
		
		return showTables;
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
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(userDB.getUrl());
			DatabaseMetaData dbmd = conn.getMetaData();
	    	rs = dbmd.getColumns(userDB.getDb(), null, mapParam.get("table"), null);

	    	while(rs.next()) {
	    		TableColumnDAO tcDAO = new TableColumnDAO();
	    		tcDAO.setName(rs.getString("COLUMN_NAME"));
	    		tcDAO.setType(rs.getString("TYPE_NAME"));
	    		
	    		tcDAO.setComment(rs.getString("REMARKS"));
	    		
	    		showTableColumns.add(tcDAO);
	    	}
	    	
		} catch(Exception e) {
			logger.error(mapParam.get("table") + " table column", e);
			throw e;
		} finally {
			if(rs != null) rs.close();
			if(conn != null) conn.close();
		}
		
		return showTableColumns;
	}
	
	/**
	 * select
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @param limitCount
	 * 
	 * @throws Exception
	 */
	public QueryExecuteResultDTO select(UserDBDAO userDB, String requestQuery, int limitCount, String strNullValue) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("\t * Query is [ " + requestQuery );
		
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			javaConn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			pstmt = javaConn.prepareStatement(requestQuery);
			rs = pstmt.executeQuery();
			
			return new QueryExecuteResultDTO(userDB, requestQuery, true, rs, limitCount, strNullValue);
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
