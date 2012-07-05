package com.hangum.tadpole.mongodb.core.test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * not equals example
 * 
 * select * from rental where rental_id != 1;
 * 
 * @author hangum
 *
 */
public class MongoTestNotEqualsStmt {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection myColl = db.getCollection("rental");
		
		BasicDBObject myAndQuery = new BasicDBObject();
		myAndQuery.append("rental_id", new BasicDBObject("$ne", 1));		
		
		DBCursor myCursor = myColl.find(myAndQuery);		
		while (myCursor.hasNext()) {
			System.out.println(myCursor.next());
		}

	}

}
