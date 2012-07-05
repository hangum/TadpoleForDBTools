package com.hangum.tadpole.mongodb.core.connection;

import junit.framework.TestCase;

import com.hangum.tadpole.mongodb.core.test.MakeUserDBDAO;
import com.mongodb.DB;

/** * 
 * mongodb connection test
 * 
 * @author hangum
 *
 */
public class MongoDBConnectionTest extends TestCase {

	public void testConnection() {
		
		try {
			DB db = MongoDBConnection.connection(MakeUserDBDAO.getUserDB());
			if(db == null) {
				fail("connection exception");
			}
		} catch(Exception e) {
			fail("connection excepiton");
		}
	}
}
