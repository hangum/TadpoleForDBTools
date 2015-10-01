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
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * tadpole system에서 공통으로 사용하는 모듈
 * 
 * @author hangum
 *
 */
public class TadpoleSystemCommons {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystemCommons.class);

	/**
	 * 쿼리중에 quote sql을 반영해서 작업합니다.
	 * 
	 * @param userDB
	 * @param executeType
	 * @param strDML
	 * @param args
	 * @throws Exception
	 */
	public static RequestResultDAO executSQL(UserDBDAO userDB, String executeType, String strDML, String ... args) throws Exception {
		if(logger.isDebugEnabled()) logger.debug(String.format(strDML, args));
		String strQuery = String.format(strDML, args);
		
		RequestResultDAO reqResultDAO = new RequestResultDAO();
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(RWT.getRequest().getRemoteAddr());
		reqResultDAO.setStrSQLText(strQuery);
	
		try {
			boolean bool = executSQL(userDB, executeType, strQuery);
		} catch(Exception e) {
			reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
			reqResultDAO.setMesssage(e.getMessage());
		} finally {
			reqResultDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
		}
		
		return reqResultDAO;
	}
	
	/**
	 * 쿼리중에 quote sql을 반영해서 작업합니다.
	 * 
	 * @param userDB
	 * @param executeType
	 * @param strSQL
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	private static boolean executSQL(UserDBDAO userDB, String executeType, String strSQL) throws TadpoleSQLManagerException, SQLException {
		java.sql.Connection javaConn = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			Statement stmt = javaConn.createStatement();

			return stmt.execute(strSQL);
			
		} finally {
			// save schema history
			TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), userDB, 
				"EDITOR",
				executeType,
				"",
				strSQL);
			
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
			
		}
	}
}
