package com.hangum.db.system;

import junit.framework.TestCase;

/**
 * <code>com.hangum.db.system.TadpoleSystemCommons</code>
 * 
 * @author hangum
 *
 */
public class TadpoleSystemCommonsTest extends TestCase {

	/**
	 * executeSQL
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
