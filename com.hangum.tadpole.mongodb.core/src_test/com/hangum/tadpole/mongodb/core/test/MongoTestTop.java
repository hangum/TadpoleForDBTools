package com.hangum.tadpole.mongodb.core.test;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * top example
 * 
 * @author hangum
 * 
 */
public class MongoTestTop {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("admin");
		
		DBObject queryObj = new BasicDBObject("top", 1);
		CommandResult cr = db.command(queryObj);
		if(cr.ok()) {
			System.out.println(cr.toString());
		} else {
			
			System.out.println( cr.getException());
		}
	}

}
