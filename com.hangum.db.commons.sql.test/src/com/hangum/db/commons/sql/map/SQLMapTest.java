package com.hangum.db.commons.sql.map;

import java.sql.Statement;

import junit.framework.TestCase;

import com.hangum.db.system.TadpoleSystemConnector;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * {@link com.hangum.db.commons.sql.map.SQLMap SQLMap을 테스트합니다.}
 * 
 * @author hangum
 *
 */
public class SQLMapTest extends TestCase {

	/**
	 * {@link com.hangum.db.commons.sql.map.SQLMap#getInstance(com.hangum.db.dao.system.UserDBDAO) SQLMap을 테스트합니다.}
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
