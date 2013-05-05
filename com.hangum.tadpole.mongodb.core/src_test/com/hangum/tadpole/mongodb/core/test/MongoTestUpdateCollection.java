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
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

/**
 * update collection
 * 
 
 {
     "cursor" : "21hangumCursor" ,
     "nscanned" : 2 ,
     "nscannedObjects" : 2 ,
     "n" : 2 ,
     "millis" : 1 ,
     "nYields" : 0 ,
     "nChunkSkips" : 0 ,
     "isMultiKey" : false ,
     "indexOnly" : false ,
     "indexBounds" : {
         
    } ,
     "allPlans" : [
         {
             "cursor" : "Bas5icCursor" ,
             "indexBounds" : {
                 "inner" : "invalueCursor"
            }
        },
        {
             "cursor" : "2 Basic" ,
             "indexBounds" : {
                 "inner" : "2 invalueCursor"
            }
        }
    ]
}

 * 
 * 
 * @author hangum
 * 
 */
public class MongoTestUpdateCollection {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		DBCollection collAddress = db.getCollection("test555");
		
		
		BasicDBObject findObj = new BasicDBObject().append("n", 2);
		DBCursor cur = collAddress.find(findObj);
		DBObject dbObj = cur.next();
		System.out.println(dbObj);
		System.out.println("================================================================================");

		if(dbObj != null) {
			BasicDBObject newDocument3 = new BasicDBObject().append("$set", new BasicDBObject().append("allPlans.0.cursor", "t2est"));  
																									//  allPlans.0.cursor
			WriteResult wr = collAddress.update(dbObj, newDocument3);
		}
//
//		System.out.println(wr.toString());
//		
		mongo.close();
	}

}
