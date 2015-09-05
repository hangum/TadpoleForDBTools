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

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
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
	 */
	public static boolean executSQL(UserDBDAO userDB, String executeType, String strDML, String ... args) throws TadpoleSQLManagerException, SQLException {
		String strQuery = String.format(strDML, args);
		
		java.sql.Connection javaConn = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			if(logger.isDebugEnabled()) logger.debug(String.format(strDML, args));
			
			Statement stmt = javaConn.createStatement();

			return stmt.execute(strDML);
			
		} finally {
			// save schema history
			
			TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), userDB, 
				"EDITOR",
				executeType,
				"",
				strQuery);
			
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
			
		}
	}
}
