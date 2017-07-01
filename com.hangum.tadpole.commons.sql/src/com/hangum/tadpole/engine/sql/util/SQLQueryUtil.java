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
package com.hangum.tadpole.engine.sql.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * sql query util
 * 
 * @author hangum
 */
public class SQLQueryUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLQueryUtil.class);
	
	private int DATA_COUNT = 3000;
	
	/** DATA MAX COUNT */
	private int MAX_DATA_COUNT = -1;//Integer.parseInt(GetAdminPreference.getQueryResultDownloadLimit());
	
	private UserDBDAO userDB;
	private String requestQuery;
	
	/** 처음한번은 반듯이 동작해야 하므로 */
	private boolean isFirst = true;
	private int startPoint = 0;
	
	private boolean isOneTime = false;
	
	private QueryExecuteResultDTO queryResultDAO = new QueryExecuteResultDTO();
	
	public SQLQueryUtil(UserDBDAO userDB, String requestQuery) {
		this(userDB, requestQuery, false, -1);
	}
	
	/**
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @param isOneTime
	 */
	public SQLQueryUtil(UserDBDAO userDB, String requestQuery, boolean isOneTime, int intMaxCount) {
		this.userDB = userDB;
		this.requestQuery = requestQuery;
		
		this.isFirst = true;
		this.startPoint = 0;
		
		this.isOneTime = isOneTime;
		this.MAX_DATA_COUNT = intMaxCount;
	}
	
	public QueryExecuteResultDTO nextQuery() throws Exception {
		return runSQLSelect();
	}
	
	/**
	 * 테이블에 쿼리를 실행합니다.
	 */
	private QueryExecuteResultDTO runSQLSelect() throws Exception {
		queryResultDAO = new QueryExecuteResultDTO();
		// 한번에 데이터 가져오기를 실행하면 가져올수 있는데이터만큼 가져온다.
		String thisTimeQuery = "";
		if(!this.isOneTime) {
			thisTimeQuery = PartQueryUtil.makeSelect(userDB, requestQuery, startPoint, DATA_COUNT);
		} else {
			thisTimeQuery = requestQuery;
		}
		
		if(logger.isDebugEnabled()) logger.debug("[query]" + thisTimeQuery);
		ResultSet rs = null;
		PreparedStatement stmt = null;
		java.sql.Connection javaConn = null;
		
		try {
			javaConn = TadpoleSQLManager.getConnection(userDB);
			
			stmt = javaConn.prepareStatement(thisTimeQuery); 
			rs = stmt.executeQuery();//Query( selText );
			
			// table column의 정보
			if(!this.isOneTime) {
				queryResultDAO = new QueryExecuteResultDTO(userDB, thisTimeQuery, false, rs, startPoint, DATA_COUNT);
			} else {
				queryResultDAO = new QueryExecuteResultDTO(userDB, thisTimeQuery, false, rs, startPoint);
			}
		} finally {
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
		
		return queryResultDAO;
	}
	
	public boolean hasNext() {
		if(isFirst) {
			isFirst = false;
			return true;
		} else {
			if(this.isOneTime) return false;
			
			startPoint = startPoint + DATA_COUNT;
			if(this.MAX_DATA_COUNT < startPoint) return false;
			if(queryResultDAO.getDataList().getData().isEmpty()) return false;
		}
		 
		return true;
	}

	public String getRequestQuery() {
		return requestQuery;
	}

	public void setRequestQuery(String requestQuery) {
		this.requestQuery = requestQuery;
	}

	public int getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}
}
