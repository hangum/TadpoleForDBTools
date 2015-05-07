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

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.QUERY_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.execute.TransactionManger;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.utils.UserPreference;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 *
 * @author hangum
 * @version 1.6.1
 *
 */
public class ExecuteOtherSQL {
	private static final Logger logger = Logger.getLogger(ExecuteOtherSQL.class);
	
	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param reqQuery
	 * @exception
	 */
	public static void runSQLOther(
			final RequestQuery reqQuery, 
			final UserDBDAO userDB,
			final String userType,
			final String userEmail) throws SQLException, Exception 
	{
		if(!PermissionChecker.isExecute(userType, userDB, reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		PublicTadpoleDefine.QUERY_TYPE queryType = reqQuery.getQueryType();
		if(queryType == QUERY_TYPE.DDL) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDdl_lock())) {
				throw new Exception(Messages.MainEditor_21);
			}
		}
		if(queryType == QUERY_TYPE.INSERT) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getInsert_lock())) {
				throw new Exception(Messages.MainEditor_21);
			}
		}
		if(queryType == QUERY_TYPE.UPDATE) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getUpdate_lock())) {
				throw new Exception(Messages.MainEditor_21);
			}
		}
		if(queryType == QUERY_TYPE.DELETE) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDelete_locl())) {
				throw new Exception(Messages.MainEditor_21);
			}
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			new TajoConnectionManager().executeUpdate(userDB,reqQuery.getSql());
		} else { 
		
			// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
			if(TransactionManger.transactionQuery(reqQuery.getSql(), userEmail, userDB)) return;
			
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
				
				// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다.
				if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWith(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
						reqQuery.setSql(StringUtils.replaceOnce(reqQuery.getSql(), "(", " (")); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWithIgnoreCase(checkSQL, "CREATE OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE TRIGGER") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER TRIGGER") //$NON-NLS-1$
					) { //$NON-NLS-1$
						reqQuery.setSql(reqQuery.getSql() + UserPreference.QUERY_DELIMITER); //$NON-NLS-1$
					}
				}
				
				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
				if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT) statement.execute(reqQuery.getSql());
				else statement.executeUpdate(reqQuery.getSql());
				
			} finally {
				try { statement.close();} catch(Exception e) {}
	
				if(reqQuery.isAutoCommit()) {
					try { javaConn.close(); } catch(Exception e){}
				}
			}
		}  	// end which db
	}
	
}
