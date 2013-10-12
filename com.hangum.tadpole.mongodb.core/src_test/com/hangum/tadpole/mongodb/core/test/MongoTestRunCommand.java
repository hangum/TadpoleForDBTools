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

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoTestRunCommand {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, 10001);//ConAndAuthentication.port);
		DB db = mongo.getDB("admin");
		
		CommandResult cr = db.command(new BasicDBObject("replSetInitiate", "{'_id' : 'firstset', 'members' : [{'_id' : 1, 'host' : 'localhost:10001'}, {'_id' : 2, 'host' : 'localhost:10002'}, {'_id' : 3, 'host' : 'localhost:10003', arbiterOnly: true }]}"));
		System.out.println( cr.toString() );		
		
		mongo.close();
	}
}
