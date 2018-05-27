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

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 쿼리 실행결과 dto
 * 
 * @author hangum
 *
 */
public class QueryExecuteResultDTO extends ResultSetUtilDTO {
	private String queryMsg = "";
	
	public QueryExecuteResultDTO() {
	}

	/**
	 * 쿼리 결과가 ResultSet일 경우
	 * 
	 * @param userDB
	 * @param reqQuery
	 * @param isShowRownum
	 * @param resultSet
	 * @param intSelectLimitCnt
	 * @param intLastIndex
	 * @throws SQLException
	 */
	public QueryExecuteResultDTO(
			UserDBDAO userDB,
			String reqQuery,
			boolean isShowRownum, 
			ResultSet resultSet, 
			int intSelectLimitCnt, 
			int intLastIndex
	) throws SQLException {
		super(userDB, reqQuery, isShowRownum, resultSet, intSelectLimitCnt, intLastIndex);
	}
	
	/**
	 * 쿼리 결과가 json 스트링일경우
	 * 
	 * @param userDB
	 * @param reqQuery
	 * @param isShowRownum
	 * @param strJson
	 * @param intSelectLimitCnt
	 * @param intLastIndex
	 * @throws SQLException
	 */
	public QueryExecuteResultDTO(
			UserDBDAO userDB,
			String reqQuery,
			boolean isShowRownum, 
			String strJson, 
			int intSelectLimitCnt, 
			int intLastIndex
	) throws SQLException {
		super(userDB, reqQuery, isShowRownum, strJson, intSelectLimitCnt, intLastIndex);
	}
	

	public QueryExecuteResultDTO(
			UserDBDAO userDB, String reqQuery, boolean isShowRownum, ResultSet rs, int queryResultCount) throws SQLException {
		super(userDB, reqQuery, isShowRownum, rs, queryResultCount, 0);
	}

	/**
	 * @return the queryMsg
	 */
	public String getQueryMsg() {
		return queryMsg;
	}

	/**
	 * @param queryMsg the queryMsg to set
	 */
	public void setQueryMsg(String queryMsg) {
		this.queryMsg = queryMsg;
	}

}
