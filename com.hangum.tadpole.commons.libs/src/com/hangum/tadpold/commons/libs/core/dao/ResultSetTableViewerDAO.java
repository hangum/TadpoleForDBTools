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
package com.hangum.tadpold.commons.libs.core.dao;

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
public class ResultSetTableViewerDAO {

	/**
	 * column 이름. <columnIndex, name>
	 */
	private Map<Integer, String> mapColumns = new HashMap<Integer, String>();
	/**
	 * column 정렬을 위한 index <columnIndex, java.sql.Type>
	 */
	private Map<Integer, Integer> mapColumnType = new HashMap<Integer, Integer>();
	/**
	 * data <columnIndex, data>
	 */
	private List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer, Object>>();

	/**
	 * 
	 * @param mapColumns
	 * @param mapColumnType
	 * @param sourceDataList
	 */
	public ResultSetTableViewerDAO(Map<Integer, String> mapColumns, Map<Integer, Integer> mapColumnType, List<Map<Integer, Object>> sourceDataList) {
		this.mapColumns = mapColumns;
		this.mapColumnType = mapColumnType;
		this.sourceDataList = sourceDataList;
	}

	/**
	 * @return the mapColumns
	 */
	public Map<Integer, String> getMapColumns() {
		return mapColumns;
	}

	/**
	 * @param mapColumns
	 *            the mapColumns to set
	 */
	public void setMapColumns(Map<Integer, String> mapColumns) {
		this.mapColumns = mapColumns;
	}

	/**
	 * @return the mapColumnType
	 */
	public Map<Integer, Integer> getMapColumnType() {
		return mapColumnType;
	}

	/**
	 * @param mapColumnType
	 *            the mapColumnType to set
	 */
	public void setMapColumnType(Map<Integer, Integer> mapColumnType) {
		this.mapColumnType = mapColumnType;
	}

	/**
	 * @return the sourceDataList
	 */
	public List<Map<Integer, Object>> getSourceDataList() {
		return sourceDataList;
	}

	/**
	 * @param sourceDataList
	 *            the sourceDataList to set
	 */
	public void setSourceDataList(List<Map<Integer, Object>> sourceDataList) {
		this.sourceDataList = sourceDataList;
	}

}
