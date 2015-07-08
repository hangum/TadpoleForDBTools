/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.test;

import com.mongodb.DB;
import com.mongodb.Mongo;

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
		
//		boolean bool = db.authenticate("admin", "admin".toCharArray());
//		System.out.println(bool);
		
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
