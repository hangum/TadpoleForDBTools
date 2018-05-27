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
 * or example
 * 
 * select * from rental where rental_id = 1 or rental_id = 2;
 * 
 * @author hangum
 * 
 */
public class MongoTestInStmt {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

//		for(int i=0; i<1000000; i++) {
			ConAndAuthentication testMongoCls = new ConAndAuthentication();
			Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
			DB db = mongo.getDB("test");
			
			DBCollection myColl = db.getCollection("rental");
			
			Integer[] inCondition = {1, 2};		
			BasicDBObject inQuery = new BasicDBObject();
			inQuery.put("rental_id", new BasicDBObject("$in", inCondition));
			
			DBCursor myCursor = myColl.find(inQuery);		
			while (myCursor.hasNext()) {
				System.out.println(myCursor.next());
			}
	
			mongo.close();
			
			try {
				Thread.sleep(1);
			} catch(Exception e) {}
//		}
	}

}
