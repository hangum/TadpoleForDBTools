package com.hangum.tadpole.mongodb.core.test;

import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoTestCreateDB {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("newdb2");
		Set<String> listColNames = db.getCollectionNames();
		for (String string : args) {
			System.out.println("[collection name]" + string);
		}
		
//		DBObject dbObject = (DBObject) JSON.parse("{capped:true, size:100000}");
//		db.createCollection("test", dbObject);
		
		mongo.close();
	}
}
