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
package com.hangum.tadpole.mongodb.core.connection;

import junit.framework.TestCase;

import com.hangum.tadpole.mongodb.core.test.MakeUserDBDAO;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/** * 
 * mongodb connection test
 * 
 * @author hangum
 *
 */
public class MongoDBConnectionTest extends TestCase {

	public void testConnection() {
		
		try {
			DB db = MongoConnectionManager.getInstance(MakeUserDBDAO.getUserDB());
			if(db == null) {
				fail("connection exception");
			}
		} catch(Exception e) {
			fail("connection excepiton");
		}
	}
	
	
	public void testQuery() {
		try {
			for(int i=0; i<1000900; i++) {
				System.out.println("[" + i + "]");
				DB db = MongoConnectionManager.getInstance(MakeUserDBDAO.getUserDB());
				
				DBCollection myColl = db.getCollection("language");

				DBObject dbObject = (DBObject) JSON.parse("{'rental_id':1,  'inventory_id':367}");
				myColl.insert(dbObject);
	//			
				DBCursor cursorDoc = myColl.find();
				while (cursorDoc.hasNext()) {
					System.out.println(cursorDoc.next());
				}
				cursorDoc.close();
			}
		} catch(Exception e) {
			System.exit(0);
		}
	}
}
