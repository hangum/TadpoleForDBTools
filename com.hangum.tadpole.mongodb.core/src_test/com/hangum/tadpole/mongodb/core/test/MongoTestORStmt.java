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
 * select * from rental where rental_id <=10 or rental_id =2;
 * 
 * 
 * @author hangum
 * 
 */
public class MongoTestORStmt {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("select * from rental where rental_id <=5 or rental_id =2;");
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection myColl = db.getCollection("rental");
		ArrayList<BasicDBObject> myList = new ArrayList<BasicDBObject>();
		myList.add(new BasicDBObject("rental_id", new BasicDBObject("$lte", 5)));
		myList.add(new BasicDBObject("rental_id", 2));
		
		BasicDBObject myOrQuery = new BasicDBObject("$or", myList);
		
		DBCursor myCursor = myColl.find(myOrQuery);		
		while (myCursor.hasNext()) {
			System.out.println(myCursor.next());
		}
		
		mongo.close();
	}

}
