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

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * in example example
 * 
 * 
 * select * from rental
 * where (rental_id != 1 and inventory_id = 100) or customer_id = 3  
 * 
 * 
 * @author hangum
 * 
 */
public class MongoTestAndORComplexStmt {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection myColl = db.getCollection("rental");
		
		BasicDBObject mainQuery = new BasicDBObject();
		
		// tmp and
		BasicDBObject tmpAndQuery = new BasicDBObject();		
		tmpAndQuery.append("inventory_id", 100);
		tmpAndQuery.append("rental_id", new BasicDBObject("$ne", 1));
		
		mainQuery.put("$and", tmpAndQuery);
		
		// tmp or
		ArrayList<BasicDBObject> myList = new ArrayList<BasicDBObject>();
		myList.add(new BasicDBObject("customer_id", 3));
		
		mainQuery.put("$or", myList);
		
		System.out.println( mainQuery.toString() );
		
		DBCursor myCursor = myColl.find(mainQuery);
		System.out.println("[result cursor size is " + myCursor.count());
		while (myCursor.hasNext()) {
			System.out.println(myCursor.next());
		}
		
		mongo.close();
	}
	
}
