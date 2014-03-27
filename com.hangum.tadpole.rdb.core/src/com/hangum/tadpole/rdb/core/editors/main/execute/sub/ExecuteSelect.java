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
package com.hangum.tadpole.rdb.core.editors.main.execute.sub;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.execute.TransactionManger;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.sql.util.resultset.ResultSetUtils;
import com.hangum.tadpole.tajo.core.connections.manager.ConnectionPoolManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * execute sql 
 * 
 * @author hangum
 *
 */
public class ExecuteSelect {
	private static final Logger logger = Logger.getLogger(ExecuteSelect.class);
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 */
	public QueryExecuteResultDTO runSQLSelect(
			final RequestQuery reqQuery, 
			final UserDBDAO userDB,
			final String userType,
			final String userEmail,
			final int queryPageCount,
			final boolean isResultComma
	) throws Exception {
		QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();
		if(!PermissionChecker.isExecute(userType, userDB, reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
					javaConn = ConnectionPoolManager.getDataSource(userDB).getConnection();
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(userEmail, userDB);
				}
			}
			
			pstmt = javaConn.prepareStatement(reqQuery.getSql());
			rs = pstmt.executeQuery();
			
			rsDAO = new QueryExecuteResultDTO(true, rs, queryPageCount, isResultComma);
			
			if(userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT || userDB.getDBDefine() == DBDefine.HIVE_DEFAULT) {
			} else {
				// 데이터셋에 추가 결과 셋이 있을경우 모두 fetch 하여 결과 그리드에 표시한다.
				while(pstmt.getMoreResults()){  
					rsDAO.getDataList().getData().addAll(ResultSetUtils.getResultToList(pstmt.getResultSet(), queryPageCount, isResultComma).getData());
				}
			}
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
		
		return rsDAO;
	}
	
}
