/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.system;

import org.apache.log4j.Logger;

import java.sql.Statement;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDBDAO;
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
