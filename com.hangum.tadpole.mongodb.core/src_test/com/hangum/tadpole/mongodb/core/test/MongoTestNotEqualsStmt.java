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
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * not equals example
 * 
 * select * from rental where rental_id != 1;
 * 
 * @author hangum
 *
 */
public class MongoTestNotEqualsStmt {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBCollection myColl = db.getCollection("test_table");
		
//		BasicDBObject myAndQuery = new BasicDBObject();
//		myAndQuery.append("rental_id", new BasicDBObject("$ne", 1));
		
		BasicDBObject basicFields = new BasicDBObject();
		BasicDBObject basicWhere = new BasicDBObject();
		BasicDBObject basicSort = new BasicDBObject();
		
		DBCursor myCursor = myColl.find(basicFields, basicWhere).sort(basicSort).limit(999);		
		while (myCursor.hasNext()) {
			System.out.println(myCursor.next());
		}

		mongo.close();
	}

}
