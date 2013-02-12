/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
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
