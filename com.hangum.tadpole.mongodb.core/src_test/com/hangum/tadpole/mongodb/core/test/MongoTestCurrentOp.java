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

import com.hangum.tadpole.util.JSONUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * db.currentOp, db.killOp example
 * 
 * @author hangum
 * 
 */
public class MongoTestCurrentOp {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBObject dbObj = (DBObject)db.eval("db.currentOp()");
		System.out.println(JSONUtil.getPretty(dbObj.toString()));
		
		BasicDBList dbInprogs = (BasicDBList)dbObj.get("inprog");
		for (Object object : dbInprogs) {
			BasicDBObject obj = (BasicDBObject)object;			
			System.out.println("[opid]" + obj.get("opid"));
			
//			System.out.println("##[start] killOp##############");
//			db.eval("db.killOp(" + obj.get("opid") + ")");
//			System.out.println("##[stop] killOp##############");
		}
			
		mongo.close();
	}

}
