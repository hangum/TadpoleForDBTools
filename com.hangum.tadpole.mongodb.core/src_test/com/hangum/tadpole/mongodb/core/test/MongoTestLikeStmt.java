package com.hangum.tadpole.mongodb.core.test;

import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * in example example
 * 
 * 
 * select * from rental where rental_id <=10 or rental_id =2;
 * 
 * 
 * @author hangum
 * 
 */
public class MongoTestLikeStmt {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("select * from language where name like '%en%'");
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection myColl = db.getCollection("language");
		BasicDBObject dbObject = new BasicDBObject();
		Pattern regex = Pattern.compile(".*en*");
		dbObject.put("name", regex);
		
		DBCursor myCursor = myColl.find(dbObject);		
		while (myCursor.hasNext()) {
			System.out.println(myCursor.next());
		}
		

	}

}
