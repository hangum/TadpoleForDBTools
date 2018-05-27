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

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.manager.TransactionManger;
import com.hangum.tadpole.engine.permission.AcessControlUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.restful.TadpoleException;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

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
	 * @param listReqQuery
	 * @param userType
	 * @param intCommitCount
	 * @param connectId
	 * @param userEmail
	 * @throws Exception
	 */     
	public static void runSQLExecuteBatch(String errMsg,  
			final List<RequestQuery> listReqQuery,
			final int intCommitCount,
			final String userEmail
	) throws TadpoleException, SQLException {
		if(listReqQuery.isEmpty()) return;
		
		final RequestQuery _reqQuery = listReqQuery.get(0);
		final UserDBDAO userDB = _reqQuery.getUserDB();
		
		AcessControlUtils.SQLPermissionCheck(errMsg, listReqQuery);
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(_reqQuery.isAutoCommit()) {
				javaConn = TadpoleSQLManager.getConnection(userDB);
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(_reqQuery.getConnectId(), userEmail, userDB);
			}
			statement = javaConn.createStatement();
			
			int count = 0;
			for (RequestQuery reqQuery : listReqQuery) {
				String strSQL = StringUtils.trimToEmpty(reqQuery.getSql());
				
				// 쿼리 중간에 commit이나 rollback이 있으면 어떻게 해야 하나???
				if(!TransactionManger.calledCommitOrRollback(strSQL, reqQuery.getConnectId(), userEmail, userDB)) {
					
					if(StringUtils.startsWithIgnoreCase(StringUtils.trimToEmpty(reqQuery.getSql()), "CREATE TABLE")) { //$NON-NLS-1$
						strSQL = StringUtils.replaceOnce(strSQL, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				statement.addBatch(SQLConvertCharUtil.toServer(userDB, strSQL));
				
				if (++count % intCommitCount == 0) {
					statement.executeBatch();
					count = 0;
				}
			}
			statement.executeBatch();
			
		} catch(Exception e) {
			String strErrMsg = PublicTadpoleDefine.LINE_SEPARATOR;
			
			if(e instanceof BatchUpdateException) {
				BatchUpdateException batchException = (BatchUpdateException)e;

				RequestQuery[] strQuery = listReqQuery.toArray(new RequestQuery[listReqQuery.size()]);
				// 업데이트에서 실패한 쿼리를 찾는다.
				int[] intUpdateCnts = batchException.getUpdateCounts();
				for(int i=0; i<intUpdateCnts.length; i++) {
					if(intUpdateCnts[i] < 0) {
						strErrMsg += strQuery[i].getSql() + PublicTadpoleDefine.LINE_SEPARATOR;
					}
				}
				
				if(!"".equals(strErrMsg)) {
					strErrMsg += " was not carried out. (Data not displayed was performed.) " + PublicTadpoleDefine.DOUBLE_LINE_SEPARATOR;
				}
				
				strErrMsg += "[Error]" + PublicTadpoleDefine.LINE_SEPARATOR + batchException.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR;
			} else {
				strErrMsg = e.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR;
			}
				 
			logger.error("Execute batch update", e); //$NON-NLS-1$
			
			throw new SQLException(strErrMsg, e);
		} finally {
			try { if(statement != null) statement.close();} catch(Exception e) {}

			if(_reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
	}

}
