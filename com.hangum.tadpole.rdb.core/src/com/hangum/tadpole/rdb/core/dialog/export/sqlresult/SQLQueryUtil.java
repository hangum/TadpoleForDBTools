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
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	
	private int DATA_COUNT = 1000;
	
	private UserDBDAO userDB;
	private String requestQuery;
	private String strNullValue;
	
	/** 처음한번은 반듯이 동작해야 하므로 */
	private boolean isFirst = true;
	private int startPoint = 0;
	private int nextPoint = -1;
	
	private QueryExecuteResultDTO queryResultDAO = new QueryExecuteResultDTO();
	
	public SQLQueryUtil(UserDBDAO userDB, String requestQuery, String strNullValue) {
		this.userDB = userDB;
		this.requestQuery = requestQuery;
		this.strNullValue = strNullValue;
	}
	
	public QueryExecuteResultDTO nextQuery() throws Exception {
		startPoint = nextPoint+1;
		nextPoint = nextPoint + DATA_COUNT;
		return runSQLSelect();
	}
	
	/**
	 * 테이블에 쿼리를 실행합니다.
	 */
	private QueryExecuteResultDTO runSQLSelect() throws Exception {
		queryResultDAO = new QueryExecuteResultDTO();
		String thisTimeQuery = PartQueryUtil.makeSelect(userDB, requestQuery, startPoint, nextPoint);		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			PreparedStatement stmt = null;
			stmt = javaConn.prepareStatement(thisTimeQuery); 
			
			rs = stmt.executeQuery();//Query( selText );
			
			// table column의 정보
			
			queryResultDAO = new QueryExecuteResultDTO(userDB, thisTimeQuery, true, rs, startPoint, nextPoint, strNullValue);
		} finally {
			try { rs.close(); } catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
		
		return queryResultDAO;
	}
	
	public boolean hasNext() {
		if(isFirst) {
			isFirst = false;
		} else {
			if(queryResultDAO.getDataList().getData().isEmpty()) return false;
		}
		 
		return true;		
	}
	
	
}
