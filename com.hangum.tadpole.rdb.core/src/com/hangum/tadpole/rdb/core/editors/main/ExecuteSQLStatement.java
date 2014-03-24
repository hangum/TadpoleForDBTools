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
package com.hangum.tadpole.rdb.core.editors.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.QUERY_EXECUTE_STATUS;
import com.hangum.tadpole.rdb.core.editors.main.utils.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * Statme manager
 * 
 * @author hangum
 * 
 */
public class ExecuteSQLStatement implements Runnable {
	private Statement stmt = null;
	private RequestQuery reqQuery;
	private QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();

	/**
	 * 
	 * @param stmt
	 * @param userDB
	 * @param reqQuery
	 */
	public ExecuteSQLStatement(Statement stmt, RequestQuery reqQuery) {
		this.stmt = stmt;
		this.reqQuery = reqQuery;
	}

	@Override
	public void run() {
		ResultSet rset = null;

		try {
			rset = stmt.executeQuery(reqQuery.getSql());
			rsDAO = new QueryExecuteResultDTO(true, rset, 1000, true);

			// 사용자가 중지..
		} catch (InterruptedException ie) {
			rsDAO.setExecute_status(QUERY_EXECUTE_STATUS.USER_INTERRUPT);

			// 쿼리 익셉션.
		} catch (SQLException sqe) {
			rsDAO.setExecute_status(QUERY_EXECUTE_STATUS.SQL_EXCEPTION);
			rsDAO.setStrExceptionMsg(sqe.getMessage());

		} catch (Exception e) {
			rsDAO.setExecute_status(QUERY_EXECUTE_STATUS.UNKNOW_EXCEPTION);
			rsDAO.setStrExceptionMsg(e.getMessage());

		} finally {
			try {
				if (rset != null) rset.close();
			} catch (Exception e) {}
			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {}
		}
	}

	public QueryExecuteResultDTO getRsDAO() {
		return rsDAO;
	}

}
