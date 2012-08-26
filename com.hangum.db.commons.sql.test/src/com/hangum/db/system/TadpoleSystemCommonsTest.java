package com.hangum.db.system;

import junit.framework.TestCase;

/**
 * {@link com.hangum.db.system.TadpoleSystemCommons 시스템쿼리}
 * 
 * @author hangum
 *
 */
public class TadpoleSystemCommonsTest extends TestCase {

	/**
	 * {@link com.hangum.db.system.TadpoleSystemCommons#executSQL(com.hangum.db.dao.system.UserDBDAO, String) 쿼리실행(select 제외)}executeSQL
	 */
	public void testExecutSQL() {
		try {
			
			TadpoleSystemCommons.executSQL(TadpoleSystemConnector.getUserDB(), "CREATE TABLE   sample_table_a   (  id INTEGER NOT NULL,   name char(60) default NULL,  PRIMARY KEY (id)  );");
			
			TadpoleSystemCommons.executSQL(TadpoleSystemConnector.getUserDB(), "INSERT INTO sample_table_a  (id, name)  VALUES  ( 1, '11' ); ");
			
			TadpoleSystemCommons.executSQL(TadpoleSystemConnector.getUserDB(), "drop table sample_table_a; ");
			
		} catch (Exception e) {
			fail("execute sql " + e.getMessage());
			
			e.printStackTrace();
		}
		
	}

}
