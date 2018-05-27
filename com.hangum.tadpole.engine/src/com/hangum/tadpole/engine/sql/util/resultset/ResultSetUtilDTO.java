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
package com.hangum.tadpole.engine.sql.util.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * java.sql.ResultSet을 TableViewer에 보여주기 위한 DAO.
 * 
 * @author hangum
 *
 */
public class ResultSetUtilDTO {
		
	/**
	 * userDB dao
	 */
	private UserDBDAO userDB;
	
	/**
	 * 요청 쿼리
	 */
	private String reqQuery;
	
	/** 
	 * column 이름. <columnIndex, name>
	 */
	private Map<Integer, String> columnName = new HashMap<Integer, String>();
	
	/** 
	 * column label 이름. <columnIndex, name>
	 */
	private Map<Integer, String> columnLabelName = new HashMap<Integer, String>();
	
	/** 
	 * column of table name <columnIndex, name>
	 */
	private Map<Integer, String> columnTableName = new HashMap<Integer, String>();
	
	/**
	 * column type <columnIndex, java.sql.Type>
	 */
	private Map<Integer, Integer> columnType = new HashMap<Integer, Integer>();
	
	/**
	 * column metadata
	 * 
	 * result map is schema, table, column
	 */
//	private Map<Integer, Map> columnMetaData = new HashMap<Integer, Map>();
	
	/**
	 * data <columnIndex, data>
	 */
	private TadpoleResultSet dataList = null;//new TadpoleResultSet();
	
	/**
	 * 결과가 json 일 경우 
	 */
	private String jsonString = null;
	
	/**
	 *  extension 결과를 정의한다.
	 *  
	 *  ex) mysql의 Show status, show profile 정보를 입력한다.
	 */
	protected Map<String, Object> mapExtendResult = new HashMap<String, Object>();
	
	/**
	 * 
	 */
	public ResultSetUtilDTO() {
	}
	
	/**
	 * 메인 에디터에 결과를 ResultSet으로 보여줄 경우
	 * 
	 * @param statementType
	 * @param userDB
	 * @param mapColumns
	 * @param mapColumnType
	 * @param sourceDataList
	 */
	public ResultSetUtilDTO(
			UserDBDAO userDB, 
			String reqQuery,
			Map<Integer, String> columnName,
			Map<Integer, String> columnTableName,
			Map<Integer, Integer> columnType, 
			TadpoleResultSet dataList
	) {
		this.userDB 	= userDB;
		this.reqQuery 	= reqQuery;
		this.columnName = columnName;
		this.columnLabelName = columnName;
		this.columnTableName = columnTableName;
		this.columnType = columnType;
		this.dataList 	= dataList;
	}

	/**
	 * 메인에디터에서 보여주기위한 정보를 만듭니다.
	 *
	 * @param userDB
	 * @param isShowRownum
	 * @param rs
	 * @param limitCount
	 * @param isResultComma
	 * @param intLastIndex
	 * @throws Exception
	 */
	public ResultSetUtilDTO(
						final UserDBDAO userDB, 
						final String reqQuery,
						final boolean isShowRownum, final ResultSet rs, final int limitCount, int intLastIndex) throws SQLException {
		this.userDB = userDB;
		this.reqQuery = reqQuery;
		
		if(rs != null) {
			columnTableName = ResultSetUtils.getColumnTableName(userDB, isShowRownum, rs);
			columnName 		= ResultSetUtils.getColumnName(userDB, columnTableName, isShowRownum, rs);
			columnLabelName = ResultSetUtils.getColumnLabelName(userDB, columnTableName, isShowRownum, rs);
			columnType 		= ResultSetUtils.getColumnType(isShowRownum, rs.getMetaData());
			
			if(isShowRownum && (columnName.size() == 1)) {
				dataList = new TadpoleResultSet();
			} else {
				dataList = ResultSetUtils.getResultToList(userDB, isShowRownum, rs, limitCount, intLastIndex);
			}
			
//			columnMetaData = ResultSetUtils.getColumnTableColumnName(userDB, rs.getMetaData());
		}
	}
	
	/**
	 * 메인 에디터에 결과를 json 으로 보여줄 경우
	 * 
	 * @param userDB
	 * @param reqQuery
	 * @param isShowRownum
	 * @param jsonString
	 * @param limitCount
	 * @param intLastIndex
	 */
	public ResultSetUtilDTO(
			final UserDBDAO userDB, 
			final String reqQuery,
			final boolean isShowRownum, final String jsonString, final int limitCount, int intLastIndex) {
		this.userDB = userDB;
		this.reqQuery = reqQuery;
		
		this.jsonString = jsonString;
	}
	

	/**
	 * @return the jsonString
	 */
	public String getJsonString() {
		return jsonString;
	}

	/**
	 * @return the userDB
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}

	/**
	 * @param userDB the userDB to set
	 */
	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	/**
	 * @return the reqQuery
	 */
	public String getReqQuery() {
		return reqQuery;
	}

	/**
	 * @param reqQuery the reqQuery to set
	 */
	public void setReqQuery(String reqQuery) {
		this.reqQuery = reqQuery;
	}
	
	/**
	 * @return the columnName
	 */
	public Map<Integer, String> getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(Map<Integer, String> columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the columnType
	 */
	public Map<Integer, Integer> getColumnType() {
		return columnType;
	}

	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(Map<Integer, Integer> columnType) {
		this.columnType = columnType;
	}

	/**
	 * @return the dataList
	 */
	public final TadpoleResultSet getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public final void setDataList(TadpoleResultSet dataList) {
		this.dataList = dataList;
	}

	public void addDataAll(List<Map<Integer, Object>> resultToList) {
		this.dataList.getData().addAll(resultToList);
	}

//	public Map<Integer, Map> getColumnMetaData() {
//		return columnMetaData;
//	}
//
//	public void setColumnMetaData(Map<Integer, Map> columnMetaData) {
//		this.columnMetaData = columnMetaData;
//	}

	/**
	 * @return the columnTableName
	 */
	public Map<Integer, String> getColumnTableName() {
		return columnTableName;
	}

	/**
	 * @param columnTableName the columnTableName to set
	 */
	public void setColumnTableName(Map<Integer, String> columnTableName) {
		this.columnTableName = columnTableName;
	}

	/**
	 * @return the columnLabelName
	 */
	public Map<Integer, String> getColumnLabelName() {
		return columnLabelName;
	}

	/**
	 * @param columnLabelName the columnLabelName to set
	 */
	public void setColumnLabelName(Map<Integer, String> columnLabelName) {
		this.columnLabelName = columnLabelName;
	}
	
	/**
	 * 확장 결과를 가져온다.
	 * @return
	 */
	public Map<String, Object> getMapExtendResult() {
		return mapExtendResult;
	}
	
	/**
	 * 확장 결과를 설정한다.
	 * @param mapExtendResult
	 */
	public void setMapExtendResult(String key, Object obj) {
		this.mapExtendResult.put(key, obj);
	}

}
