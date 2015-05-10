/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.resultset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 올챙이에서 정하는 ResultSet 입니다.
 * 
 * @author hangum
 *
 */
public class TadpoleResultSet {
	/** 사용자가 설정한 데이터의 값만큼만 들어간다 */
	private List<Map<Integer, Object>> resultSet = new ArrayList<Map<Integer, Object>>();
	
	/** 
	 * Result set을 모두 읽었는지 
	 * 다 읽었다면 true
	 */
	private boolean isEndOfRead = true;
	
	public TadpoleResultSet() {
	}
	
	public TadpoleResultSet(List<Map<Integer, Object>> dataList) {
		this.resultSet = dataList;
	}
	
	/**
	 * @return the resultSet
	 */
	public final List<Map<Integer, Object>> getData() {
		return resultSet;
	}

	/**
	 * @param resultSet the resultSet to set
	 */
	public final void setData(List<Map<Integer, Object>> resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * @return the isEndOfRead
	 */
	public final boolean isEndOfRead() {
		return isEndOfRead;
	}

	/**
	 * @param isEndOfRead the isEndOfRead to set
	 */
	public final void setEndOfRead(boolean isEndOfRead) {
		this.isEndOfRead = isEndOfRead;
	}

}
