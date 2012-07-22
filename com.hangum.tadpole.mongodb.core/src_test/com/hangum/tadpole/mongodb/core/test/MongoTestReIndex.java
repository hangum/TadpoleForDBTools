package com.hangum.tadpole.mongodb.core.test;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * reIndex example
 * 
 * @author hangum
 * 
 */
public class MongoTestReIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBObject queryObj = new BasicDBObject("reIndex", "store");
		CommandResult cr = db.command(queryObj);
		
		System.out.println("[ok]" + cr.ok() );
		if(!cr.ok()) System.out.println("[Exception ]" + cr.getException().toString());
		System.out.println("[toString]" + cr.toString() );
		System.out.println("[size]" + cr.size() );
		

	}

}
