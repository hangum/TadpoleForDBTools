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
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLExtManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.manager.TransactionManger;
import com.hangum.tadpole.engine.permission.AcessControlUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.restful.TadpoleException;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.SQL_STATEMENT_TYPE;

/**
 * Execute ddl, insert, update, delete etc..
 *
 * @author hangum
 * @version 1.6.1
 *
 */
public class ExecuteOtherSQL {
	private static final Logger logger = Logger.getLogger(ExecuteOtherSQL.class);
	
	/**
	 * other sql execution
	 * 
	 * @param errMsg 
	 * @param reqQuery
	 * @param userEmail
	 * @throws SQLException
	 * @throws Exception
	 */
	public static int runPermissionSQLExecution(
								String errMsg, 
								final RequestQuery reqQuery, 
								final String userEmail
			) throws TadpoleSQLManagerException, SQLException, TadpoleException {
		
		AcessControlUtils.SQLPermissionCheck(errMsg, reqQuery);
		return runSQLOther(reqQuery, userEmail);
	}
	
	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param reqQuery
	 * @exception
	 */
	public static int runSQLOther(
			final RequestQuery reqQuery, 
			final String userEmail) throws TadpoleSQLManagerException, SQLException, TadpoleException 
	{
		final UserDBDAO userDB = reqQuery.getUserDB();

		// 데이터 변경 수를 지정.
		int intEChangeCnt = -1;
		
		// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
		if(TransactionManger.calledCommitOrRollback(reqQuery.getConnectId(), reqQuery.getSql(), userEmail, userDB)) return intEChangeCnt;
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		try {
			if(DBGroupDefine.DYNAMODB_GROUP == userDB.getDBGroup()) {
				javaConn = TadpoleSQLExtManager.getInstance().getConnection(userDB);
			} else {
				if(reqQuery.isAutoCommit()) {
					javaConn = TadpoleSQLManager.getConnection(userDB);
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(reqQuery.getConnectId(), userEmail, userDB);
				}
			}
			
			// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다.
			if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
				final String checkSQL = reqQuery.getSql().trim().toUpperCase();
				if(StringUtils.startsWithIgnoreCase(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
					String strTmpCreateStmt = StringUtils.replaceOnce(reqQuery.getSql(), "(", " (");  //$NON-NLS-1$ //$NON-NLS-2$
					
					reqQuery.setSql(strTmpCreateStmt);
				}
			}
			
			final String strSQL = SQLConvertCharUtil.toServer(userDB, reqQuery.getSql());
			if(reqQuery.getSqlStatementType() == SQL_STATEMENT_TYPE.NONE) {
				statement = javaConn.createStatement();
				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
				if(DBGroupDefine.HIVE_GROUP == userDB.getDBGroup() || DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) { 
					statement.execute(strSQL);
				} else {
					intEChangeCnt = statement.executeUpdate(strSQL);
				}
			} else if(reqQuery.getSqlStatementType() == SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
				preparedStatement = javaConn.prepareStatement(strSQL);
				final Object[] statementParameter = reqQuery.getStatementParameter();
				for (int i=1; i<=statementParameter.length; i++) {
					if(statementParameter[i-1] instanceof String) {
						preparedStatement.setObject(i, SQLConvertCharUtil.toServer(userDB, ""+ statementParameter[i-1]));
					} else {
						preparedStatement.setObject(i, statementParameter[i-1]);
					}
				}
				
				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
				if(DBGroupDefine.HIVE_GROUP == userDB.getDBGroup() || DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) { 
					preparedStatement.execute();
				} else {
					intEChangeCnt = preparedStatement.executeUpdate();
				}
			}
			
			return intEChangeCnt;
			
		} finally {
			try { if(statement != null) statement.close();} catch(Exception e) {}
			try { if(preparedStatement != null) preparedStatement.close();} catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
	}
	
}
