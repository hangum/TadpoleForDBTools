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
package com.hangum.tadpole.importexport.core.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtils;
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
	
	/** query  HashMap -- table 컬럼의 정보 다음과 같습니다. <column index, Data> */
	private Map<Integer, String> mapColumns = null;
	/** query 의 결과  -- table의 데이터는 다음과 같습니다. <column index, Data> */
	private List<HashMap<Integer, Object>> tableDataList = new ArrayList<HashMap<Integer, Object>>();
	/** 데이터의 데이터 타입 */
	private HashMap<Integer, String> tableDataTypeList = new HashMap<Integer, String>();
	
	/** 처음한번은 반듯이 동작해야 하므로 */
	private boolean isFirst = true;
	private int startPoint = 0;
	private int nextPoint = -1;
	
	public SQLQueryUtil(UserDBDAO userDB, String requestQuery) {
		this.userDB = userDB;
		this.requestQuery = requestQuery;
	}
	
	
	public void nextQuery() throws Exception {
		startPoint = nextPoint+1;
		nextPoint = nextPoint + DATA_COUNT;
		runSQLSelect();
	}
	
	/**
	 * 테이블에 쿼리를 실행합니다.
	 */
	private void runSQLSelect() throws Exception {
		tableDataTypeList.clear();
		tableDataList.clear();		

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
			ResultSetMetaData  rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();
			for(int i=0; i<rsm.getColumnCount(); i++) {
				tableDataTypeList.put(i, rsm.getColumnClassName(i+1));
			}
			
			// rs set의 데이터 정
			tableDataList = new ArrayList<HashMap<Integer, Object>>();
			HashMap<Integer, Object> tmpRs = null;
			
			mapColumns = ResultSetUtils.getColumnName(rs);
			
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
	
	public Map<Integer, String> getMapColumns() {
		return mapColumns;
	}
	public List<HashMap<Integer, Object>> getTableDataList() {
		return tableDataList;
	}
	public HashMap<Integer, String> getTableDataTypeList() {
		return tableDataTypeList;
	}
}
