/*******************************************************************************
 * Copyright (c) 2012 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importdb.core.dialog.importdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.PartQueryUtil;
import com.hangum.tadpole.commons.sql.util.SQLUtil;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * query util
 * 
 * @author hangum
 */
public class QueryUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(QueryUtil.class);
	
	private int DATA_COUNT = 1000;
	
	private UserDBDAO userDB;
	private String requestQuery;
	
	/** query  HashMap -- table 컬럼의 정보 다음과 같습니다. <column index, Data> */
	private HashMap<Integer, String> mapColumns = null;
	/** query 의 결과  -- table의 데이터는 다음과 같습니다. <column index, Data> */
	private List<HashMap<Integer, Object>> tableDataList = new ArrayList<HashMap<Integer, Object>>();
	/** 데이터의 데이터 타입 */
	private HashMap<Integer, String> tableDataTypeList = new HashMap<Integer, String>();
	
	private boolean isFirst = true;
	private int startPoint = 0;
	private int nextPoint = 0;
	
	public QueryUtil(UserDBDAO userDB, String requestQuery) {
		this.userDB = userDB;
		this.requestQuery = requestQuery;
	}
	
	
	public void nextQuery() throws Exception {
		startPoint += nextPoint;
		nextPoint += DATA_COUNT;
		runSQLSelect();
	}
	
	/**
	 * 테이블에 쿼리를 실행합니다.
	 */
	private void runSQLSelect() throws Exception {
		tableDataTypeList.clear();
		tableDataList.clear();		

		requestQuery = PartQueryUtil.makeSelect(userDB, requestQuery, startPoint, nextPoint);		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			PreparedStatement stmt = null;
			stmt = javaConn.prepareStatement(requestQuery); 
			
			rs = stmt.executeQuery();//Query( selText );
			
			// table column의 정보
			ResultSetMetaData  rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();
			for(int i=0; i<rsm.getColumnCount(); i++) {
				tableDataTypeList.put(i, rsm.getColumnClassName(i+1));
			}
			
			// rs set의 데이터 정
			tableDataList = new ArrayList<HashMap<Integer, Object>>();
			HashMap<Integer, Object> tmpRs = null;
			
			mapColumns = SQLUtil.mataDataToMap(rs);
			
			while(rs.next()) {
				tmpRs = new HashMap<Integer, Object>();
				
				for(int i=0;i<columnCount+1; i++) {
					try {
						tmpRs.put(i, rs.getObject(i+1));
					} catch(Exception e) {
						tmpRs.put(i, "");
					}
				}				
				tableDataList.add(tmpRs);
			}

		} finally {
			try { rs.close(); } catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
	}
	
	public boolean hasNext() {
		if(isFirst) {
			isFirst = false;
		} else {
			if(tableDataList.size() < DATA_COUNT) return false;
		}
		 
		return true;		
	}
	
	public HashMap<Integer, String> getMapColumns() {
		return mapColumns;
	}
	public List<HashMap<Integer, Object>> getTableDataList() {
		return tableDataList;
	}
	public HashMap<Integer, String> getTableDataTypeList() {
		return tableDataTypeList;
	}
}
