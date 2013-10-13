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
package com.hangum.tadpole.sql.system;

import org.apache.log4j.Logger;

import java.sql.Statement;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
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
	 * smtm.execute문의 쿼리를 날립니다. 즉 select 이외의 문...
	 * 
	 * @param selText
	 */
	public static void executSQL(UserDBDAO userDB, String selText) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("[executeSQL]" + selText);
		
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			Statement stmt = javaConn.createStatement();
			boolean result =  stmt.execute( selText );
	
		} finally {
			try { javaConn.close(); } catch(Exception e){
				// igonr exception
			}
		}		
	}
}
