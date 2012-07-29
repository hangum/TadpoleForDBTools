
package com.hangum.tadpole.mongodb.core.test;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

/**
 * db.stats();
 * 
 * @author hangum
 * 
 */
public class MongoTestAddUsers  {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		boolean bool = db.authenticate("admin", "admin".toCharArray());
		System.out.println(bool);
		
//		WriteResult wr = db.addUser("admin", "admin".toCharArray());
//		System.out.println("[add user]" +  wr.getError() );

//		CommandResult cr = db.getStats();
//			
//		System.out.println("[ok]" + cr.ok() );
//		if(!cr.ok()) System.out.println("[Exception ]" + cr.getException().toString());
//		System.out.println("[toString]" + JSONUtil.getPretty(cr.toString()));
//		System.out.println("[size]" + cr.size() );
		
		mongo.close();
	}

}
