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
package com.hangum.tadpole.sql.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java.sql.ResultSet을 TableViewer에 보여주기 위한 DAO.
 * 
 * @author hangum
 *
 */
public class ResultSetUtilDAO {

	/** 
	 * column 이름. <columnIndex, name>
	 */
	private Map<Integer, String> columnName = new HashMap<Integer, String>();
	/**
	 * column type <columnIndex, java.sql.Type>
	 */
	private Map<Integer, Integer> columnType = new HashMap<Integer, Integer>();
	/**
	 * data <columnIndex, data>
	 */
	private List<Map<Integer, Object>> dataList = new ArrayList<Map<Integer,Object>>();
	
	public ResultSetUtilDAO() {
	}
	
	/**
	 * 
	 * @param mapColumns
	 * @param mapColumnType
	 * @param sourceDataList
	 */
	public ResultSetUtilDAO(Map<Integer, String> columnName, Map<Integer, Integer> columnType, List<Map<Integer, Object>> dataList) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.dataList = dataList;
	}

	public ResultSetUtilDAO(ResultSet rs, int queryResultCount, boolean isResultComma) throws Exception {
		if(rs != null) {
			columnName = ResultSetUtils.getColumnName(rs);
			columnType = ResultSetUtils.getColumnType(rs.getMetaData());
			dataList = ResultSetUtils.getResultToList(rs, queryResultCount, isResultComma);
		}
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
	public List<Map<Integer, Object>> getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<Map<Integer, Object>> dataList) {
		this.dataList = dataList;
	}

	public void addDataAll(List<Map<Integer, Object>> resultToList) {
		this.dataList.addAll(resultToList);
	}

}
