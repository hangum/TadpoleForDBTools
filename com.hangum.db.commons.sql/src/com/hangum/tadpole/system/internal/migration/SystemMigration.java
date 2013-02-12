/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.system.internal.migration;

import java.sql.Statement;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.system.TadpoleSystemInitializer;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * migration에서 사용하는 util 클래스
 * 
 * @author hangum
 *
 */
public class SystemMigration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemMigration.class);

	/**
	 * strQuery 쿼리를 수행합니다.
	 * 
	 * @param strQuery
	 * @throws Exception
	 */
	public static boolean runSQLExecuteBatch(String strQuery) throws Exception {
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			javaConn = client.getDataSource().getConnection();
			statement = javaConn.createStatement();
			
			return statement.execute( strQuery );
		} catch(Exception e) {
			logger.error("Execute batch update", e);
			throw e;
		} finally {
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e) {}
		}
	}
}
