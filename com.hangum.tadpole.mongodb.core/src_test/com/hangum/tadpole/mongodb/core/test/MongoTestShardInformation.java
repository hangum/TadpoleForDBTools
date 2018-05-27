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
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * listshards example
 * 
 * @author hangum
 * 
 */
public class MongoTestShardInformation {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection("127.0.0.1", 27018);
		DB db = mongo.getDB("admin");
		
		DBObject queryObj = new BasicDBObject("listshards", 1);
		CommandResult cr = db.command(queryObj);
		if(cr.ok()) {
			System.out.println(cr.toString());
		} else {			
			System.out.println( cr.getException());
		}
		
		// shard key는 인덱스가 생성 되어 있어야 합니당.
		final BasicDBObject shardKey = new BasicDBObject("TrackId", 1);		
		final BasicDBObject cmd = new BasicDBObject("shardcollection", "test.Track");
        cmd.put("key", shardKey);
        CommandResult result4 = mongo.getDB("admin").command(cmd);

        System.out.println("====>" + result4);
	}
	
	
	private static void shardCollection() {
		
	}

}
