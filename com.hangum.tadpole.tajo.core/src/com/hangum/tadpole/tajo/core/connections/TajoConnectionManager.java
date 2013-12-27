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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.ResultSetUtils;
import com.hangum.tadpole.tajo.core.connections.internal.ConnectionPoolManager;

/**
 * apache tajo connection manager
 * 현재는 jdbc이지만, 나중에는 native driver를 쓸수 있도록 별로 분리..
 * 
 * @author hangum
 *
 */
public class TajoConnectionManager {
	private static final Logger logger = Logger.getLogger(TajoConnectionManager.class);
	
	/**
	 * not select 
	 * 
	 * @param userDB
	 * @param sqlQuery
	 * @return
	 */
	public static void executeUpdate(UserDBDAO userDB, String sqlQuery) throws Exception {
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
	 * select
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @throws Exception
	 */
	public static Map<String, Object> select(UserDBDAO userDB, String requestQuery, int queryResultCount, boolean isResultComma) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		if(logger.isDebugEnabled()) logger.debug("\t * Query is [ " + requestQuery );
		
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			javaConn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			pstmt = javaConn.prepareStatement(requestQuery);
			rs = pstmt.executeQuery();
			
//			ResultSetMetaData  rsm = rs.getMetaData();
//			int columnCount = rsm.getColumnCount();
//			for(int i=0; i<columnCount; i++) {
//				System.out.println("[column info]" + i + "==> " +  rsm.getColumnLabel(i+1));
//			}
			
			// column의 data type을 얻습니다.
			retMap.put("mapColumnType", ResultSetUtils.getColumnType(rs.getMetaData()));
			
			// column name을 얻습니다. 
			// sqlite에서는 metadata를 얻은 후에 resultset을 얻어야 에러(SQLite JDBC: inconsistent internal state)가 나지 않습니다.
			retMap.put("mapColumns", ResultSetUtils.getColumnName(rs));
			
			// 결과를 프리퍼런스에서 처리한 맥스 결과 만큼만 거져옵니다.
			retMap.put("sourceDataList", ResultSetUtils.getResultToList(rs, queryResultCount, isResultComma));
		} catch(Exception e) {
			logger.error("Tajo select", e);
			throw e;
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
		
		return retMap;
	}
	
	/**
	 * 연결 테스트 합니다.
	 * 
	 * @param userDB
	 */
	public static void connectionCheck(UserDBDAO userDB) throws Exception {
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
	public static List<TableDAO> tableList(UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = new ArrayList<TableDAO>();
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionPoolManager.getDataSource(userDB).getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
	    	rs = dbmd.getTables(userDB.getDb(), null, null, null);

	    	while(rs.next()) {
	    		String strTBName = rs.getString("TABLE_NAME");
//	    		logger.debug( rs.getString("TABLE_CAT") );
//	    		logger.debug( rs.getString("TABLE_SCHEM") );
//	    		logger.debug( rs.getString("TABLE_NAME") );
//	    		logger.debug( rs.getString("TABLE_TYPE") );
//	    		logger.debug( rs.getString("REMARKS") );
	    		
	    		TableDAO tdao = new TableDAO(strTBName, "");
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
	public static List<TableColumnDAO> tableColumnList(UserDBDAO userDB, Map<String, String> mapParam) throws Exception {
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

}
