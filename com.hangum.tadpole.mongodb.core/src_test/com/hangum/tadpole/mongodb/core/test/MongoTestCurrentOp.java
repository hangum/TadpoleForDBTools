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

import com.hangum.tadpole.commons.util.JSONUtil;
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
