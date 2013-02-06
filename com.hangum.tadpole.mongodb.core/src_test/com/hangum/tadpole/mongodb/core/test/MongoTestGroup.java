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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

/**
 * MongoDB Group example
 * 
 * http://greenfishblog.tistory.com/105 의 group 편
 * 
{"dep_id":1, "salary":1}
{"dep_id":1, "salary":2}
{"dep_id":1, "salary":3}
{"dep_id":2, "salary":10}
{"dep_id":2, "salary":12}
{"dep_id":2, "salary":16}
{"dep_id":3, "salary":4}
{"dep_id":3, "salary":1}
 * 
 * @author hangum
 * 
 */
public class MongoTestGroup {
	
	static String key = "";//{ dep_id : true}";
	static String initial = "{sum_salary:0,avg:0,cnt:0}";
	
	static String condition = "";//"{dep_id : { $gt : 2 }}";
	
	static String reduce = "function(obj,prev){prev.sum_salary += obj.salary;prev.cnt++;}";
	
	static String finalizer = "function(out){out.avg=out.sum_salary / out.cnt;}";
	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl,
				ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		DBCollection coll = db.getCollection("group");
		
		DBObject dbObjectKey = (DBObject)JSON.parse(key);
		DBObject dbObjectInitial = (DBObject) JSON.parse(initial);
		DBObject dbObjectCondition = (DBObject)JSON.parse(condition);

		DBObject resultDBObject = coll.group(dbObjectKey, dbObjectCondition, dbObjectInitial, reduce, finalizer);
		System.out.println(resultDBObject);
		
		
		mongo.close();

		try {
			Thread.sleep(1);
		} catch (Exception e) {
		}
	}
}
