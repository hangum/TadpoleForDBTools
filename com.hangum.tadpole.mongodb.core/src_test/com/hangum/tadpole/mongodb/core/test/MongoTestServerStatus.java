package com.hangum.tadpole.mongodb.core.test;

import com.hangum.db.util.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * server status example
 * 
 * @author hangum
 * 
 */
public class MongoTestServerStatus {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBObject queryObj = new BasicDBObject("listCommands", 1);
		CommandResult cr = db.command(queryObj);
			
		System.out.println("[ok]" + cr.ok() );
		if(!cr.ok()) System.out.println("[Exception ]" + cr.getException().toString());
		System.out.println("[toString]" + JSONUtil.getPretty(cr.toString()));
		System.out.println("[size]" + cr.size() );
		

		mongo.close();
	}

}
