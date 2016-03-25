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

import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_TYPE;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.execute.TransactionManger;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * batch 처리해야하는 sql
 * 
 * @author hangum
 *
 */
public class ExecuteBatchSQL {
	private static final Logger logger = Logger.getLogger(ExecuteBatchSQL.class);

	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * @param errMsg 
	 * 
	 * @param listQuery
	 * @param reqQuery
	 * @param userDB
	 * @param userType
	 * @param intCommitCount
	 * @param userEmail
	 * @throws Exception
	 */     
	public static void runSQLExecuteBatch(String errMsg, List<String> listQuery, 
			final RequestQuery reqQuery,
			final UserDBDAO userDB,
			final String userType,
			final int intCommitCount,
			final String userEmail
	) throws Exception {
		if(!PermissionChecker.isExecute(userType, userDB, listQuery)) {
			throw new Exception(errMsg);
		}
		// Check the db access control 
		for (String strQuery : listQuery) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDdl_lock())) {
				throw new Exception(errMsg);
			}
			
			PublicTadpoleDefine.QUERY_DML_TYPE queryType = SQLUtil.sqlQueryType(strQuery);
			if(reqQuery.getSqlType() == SQL_TYPE.DDL) {
				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDdl_lock())) {
					throw new Exception(errMsg);
				}
			}
			if(queryType == QUERY_DML_TYPE.INSERT) {
				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getInsert_lock())) {
					throw new Exception(errMsg);
				}
			}
			if(queryType == QUERY_DML_TYPE.UPDATE) {
				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getUpdate_lock())) {
					throw new Exception(errMsg);
				}
			}
			if(queryType == QUERY_DML_TYPE.DELETE) {
				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDelete_locl())) {
					throw new Exception(errMsg);
				}
			}
		}
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(userEmail, userDB);
			}
			statement = javaConn.createStatement();
			
			int count = 0;
			for (String strQuery : listQuery) {
				// 쿼리 중간에 commit이나 rollback이 있으면 어떻게 해야 하나???
				if(!TransactionManger.calledCommitOrRollback(reqQuery.getSql(), userEmail, userDB)) {
					
					if(StringUtils.startsWithIgnoreCase(strQuery.trim(), "CREATE TABLE")) { //$NON-NLS-1$
						strQuery = StringUtils.replaceOnce(strQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
				}
				statement.addBatch(strQuery);
				
				if (++count % intCommitCount == 0) {
					statement.executeBatch();
					count = 0;
				}
			}
			statement.executeBatch();
		} catch(Exception e) {
			logger.error("Execute batch update", e); //$NON-NLS-1$
			throw e;
		} finally {
			try { statement.close();} catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}

}
