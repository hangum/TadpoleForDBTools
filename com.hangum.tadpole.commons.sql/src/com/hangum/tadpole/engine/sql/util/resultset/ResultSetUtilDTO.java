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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
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
	
	/** EXECUTE STATEMENT TYPE */
	private PublicTadpoleDefine.SQL_STATEMENTS_TYPE statementType = PublicTadpoleDefine.SQL_STATEMENTS_TYPE.SELECT;
	
	/** 
	 * column 이름. <columnIndex, name>
	 */
	private Map<Integer, String> columnName = new HashMap<Integer, String>();
	
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
	private Map<Integer, Map> columnMetaData = new HashMap<Integer, Map>();
	
	/**
	 * data <columnIndex, data>
	 */
	private TadpoleResultSet dataList = new TadpoleResultSet();
	
	public ResultSetUtilDTO() {
	}
	
	/**
	 * 
	 * @param statementType
	 * @param userDB
	 * @param mapColumns
	 * @param mapColumnType
	 * @param sourceDataList
	 */
	public ResultSetUtilDTO(PublicTadpoleDefine.SQL_STATEMENTS_TYPE statementType, 
			UserDBDAO userDB, 
			Map<Integer, String> columnName,
			Map<Integer, String> columnTableName,
			Map<Integer, Integer> columnType, 
			TadpoleResultSet dataList
	) {
		this.statementType = statementType;
		this.userDB 	= userDB;
		this.columnName = columnName;
		this.columnTableName = columnTableName;
		this.columnType = columnType;
		this.dataList 	= dataList;
	}

	/**
	 * 메인에디터에서 보여주기위한 정보를 만듭니다.
	 *
	 * @param statementType
	 * @param userDB
	 * @param isShowRownum
	 * @param rs
	 * @param limitCount
	 * @param isResultComma
	 * @throws Exception
	 */
	public ResultSetUtilDTO(PublicTadpoleDefine.SQL_STATEMENTS_TYPE statementType, UserDBDAO userDB, boolean isShowRownum, ResultSet rs, int limitCount) throws Exception {
		this.statementType = statementType;
		this.userDB = userDB;
		
		if(rs != null) {
			columnName = ResultSetUtils.getColumnName(isShowRownum, rs);
			columnTableName = ResultSetUtils.getColumnTableName(isShowRownum, rs);
			columnType = ResultSetUtils.getColumnType(isShowRownum, rs.getMetaData());
			dataList = ResultSetUtils.getResultToList(isShowRownum, rs, limitCount);
			columnMetaData = ResultSetUtils.getColumnTableColumnName(userDB, rs.getMetaData());
		}
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

	public Map<Integer, Map> getColumnMetaData() {
		return columnMetaData;
	}

	public void setColumnMetaData(Map<Integer, Map> columnMetaData) {
		this.columnMetaData = columnMetaData;
	}

	/**
	 * @return the statementType
	 */
	public PublicTadpoleDefine.SQL_STATEMENTS_TYPE getStatementType() {
		return statementType;
	}

	/**
	 * @param statementType the statementType to set
	 */
	public void setStatementType(PublicTadpoleDefine.SQL_STATEMENTS_TYPE statementType) {
		this.statementType = statementType;
	}

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
	
}
