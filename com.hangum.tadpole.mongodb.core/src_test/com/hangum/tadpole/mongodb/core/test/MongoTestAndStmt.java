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
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * and example
 * 
 * select * from rental where rental_id >= 1 and inventory_id = 367;
 * 
 * @author hangum
 *
 */
public class MongoTestAndStmt {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection myColl = db.getCollection("city1");
		
		// show column
		BasicDBObject myColumn = new BasicDBObject();
		myColumn.put("loc.y", true);
		
		// where
		BasicDBObject myAndQuery = new BasicDBObject();
//		myAndQuery.append("rental_id", new BasicDBObject("$gte", 1));
//		myAndQuery.append("inventory_id", 367);//new BasicDBObject("$eq", 367));
		
		DBCursor myCursor = myColl.find(myAndQuery, myColumn);		
		while (myCursor.hasNext()) {
			System.out.println(myCursor.next());
		}

		mongo.close();
	}

}
