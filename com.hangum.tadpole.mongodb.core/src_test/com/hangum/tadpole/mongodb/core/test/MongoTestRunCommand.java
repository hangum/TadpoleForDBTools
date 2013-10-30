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

import java.awt.color.CMMException;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoTestRunCommand {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, 27017);//ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection dbColl = db.getCollection("user");
		
		DBObject cmdObj = (DBObject) JSON.parse("{language  : 'en_us'}, {seq: true, email:true}, {seq, -1}");
		DBCursor dbCur = dbColl.find(cmdObj);
		for(DBObject obj : dbCur.toArray()) {
			System.out.println(obj);
		}
		
//		CommandResult cr = db.command(cmdObj);//new BasicDBObject("create", "hyunjong"));
//		System.out.println( cr.toString() );		
		
		mongo.close();
	}
}
