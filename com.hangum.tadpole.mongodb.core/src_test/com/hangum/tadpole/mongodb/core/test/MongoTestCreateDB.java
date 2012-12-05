package com.hangum.tadpole.mongodb.core.test;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoTestCreateDB {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("newdb");
		
		mongo.close();
	}
}
