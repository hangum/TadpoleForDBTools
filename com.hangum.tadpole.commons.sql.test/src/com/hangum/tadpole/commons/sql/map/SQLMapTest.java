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
package com.hangum.tadpole.commons.sql.map;

import java.sql.Statement;

import junit.framework.TestCase;

import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.internal.map.SQLMap;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * {@link com.hangum.tadpole.engine.manager.internal.map.SQLMap SQLMap을 테스트합니다.}
 * 
 * @author hangum
 *
 */
public class SQLMapTest extends TestCase {

	/**
	 * {@link com.hangum.tadpole.engine.manager.internal.map.SQLMap#getInstance(com.hangum.db.dao.system.UserDBDAO) SQLMap을 테스트합니다.}
	 */
	public void testGetInstance() {
		java.sql.Connection javaConn = null;
		
		try {
			final UserDBDAO userDB = TadpoleSystemInitializer.getUserDB();
			System.out.println(userDB);
			SqlMapClient client = SQLMap.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			Statement stmt = javaConn.createStatement();
			
			boolean result =  stmt.execute( "CREATE TABLE   sample_getinstance   (  id INTEGER NOT NULL,   name char(60) default NULL,  PRIMARY KEY (id)  );" );
			System.out.println("result is " + result);
			
			result =  stmt.execute( "DROP TABLE   sample_getinstance;" );	
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("fail get instance");
		} finally {
			try { javaConn.close(); } catch(Exception e){}
		}
		
	}

}
