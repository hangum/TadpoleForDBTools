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
package com.hangum.tadpole.commons.sql.map;

import java.sql.Statement;

import junit.framework.TestCase;

import com.hangum.tadpole.commons.sql.map.SQLMap;
import com.hangum.tadpole.system.TadpoleSystemConnector;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * {@link com.hangum.tadpole.commons.sql.map.SQLMap SQLMap을 테스트합니다.}
 * 
 * @author hangum
 *
 */
public class SQLMapTest extends TestCase {

	/**
	 * {@link com.hangum.tadpole.commons.sql.map.SQLMap#getInstance(com.hangum.db.dao.system.UserDBDAO) SQLMap을 테스트합니다.}
	 */
	public void testGetInstance() {
		java.sql.Connection javaConn = null;
		
		try {
			
			SqlMapClient client = SQLMap.getInstance( TadpoleSystemConnector.getUserDB() );
			javaConn = client.getDataSource().getConnection();
			
			Statement stmt = javaConn.createStatement();
			
			boolean result =  stmt.execute( "CREATE TABLE   sample_getinstance   (  id INTEGER NOT NULL,   name char(60) default NULL,  PRIMARY KEY (id)  );" );
			
			result =  stmt.execute( "DROP TABLE   sample_getinstance;" );	
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("fail get instance");
		} finally {
			try { javaConn.close(); } catch(Exception e){}
		}
		
	}

}
