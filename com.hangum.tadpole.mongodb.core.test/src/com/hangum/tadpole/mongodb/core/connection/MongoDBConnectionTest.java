package com.hangum.tadpole.mongodb.core.connection;

import junit.framework.TestCase;

import com.hangum.tadpole.mongodb.core.test.ConAndAuthentication;
import com.hangum.tadpole.mongodb.core.test.MakeUserDBDAO;
import com.mongodb.DB;
import com.mongodb.Mongo;

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
	
	
	public void testQuery() {
		for(int i=0; i<10000; i++) {
			System.out.println("[" + i + "]");
			ConAndAuthentication testMongoCls = new ConAndAuthentication();
			Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
			DB db = mongo.getDB("test");
			
//			DBCollection myColl = db.getCollection("rental");
//	
//			DBObject dbObject = (DBObject) JSON.parse("{'rental_id':1,  'inventory_id':367}");
//			myColl.insert(dbObject);
//			
//			DBCursor cursorDoc = myColl.find();
//			while (cursorDoc.hasNext()) {
//				System.out.println(cursorDoc.next());
//			}
			mongo.close();
		}
	}
}
