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

import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;

/**
 * MongoDB MapReduce example
 * 
 * 
 // map
function() {
     emit(this.name, {count: 1, sum: this.number});
};

 // reduce
 function(key,values) {
    var n = { count: 0, sum: 0};
    for ( var i = 0; i < values.length; i ++ ) {
        n.sum += values[i].sum;
        n.count += values[i].count;
    };

    return n;
};

 * 
 * 
 * @author hangum
 * 
 */
public class MongoTestMapReduce {

	static String map = "function(){emit(this.name, {count: 1, sum: this.number});};";

	static String reduce = 
			"function( key , values ){" +
					"var n = { count: 0, sum: 0}; " +
					"for ( var i = 0; i < values.length; i ++ ) {" +
						"n.sum += values[i].sum;" +
						"n.count += values[i].count;" +
					"};" +
						
					"return n;" +
			"};";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl,
				ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		DBCollection coll = db.getCollection("person");

		MapReduceOutput out = coll.mapReduce(map, reduce, null, MapReduceCommand.OutputType.INLINE, null);
		for ( DBObject obj : out.results() ) {
			System.out.println("======================================================");
			System.out.println("\t[_id]\t" + obj.get("_id"));
			Map objResult = (Map)obj.get("value");
			System.out.println("\t[count]\t" + objResult.get("count"));
			System.out.println("\t[sum]\t" + objResult.get("sum"));
			
			System.out.println( obj );
		}
		
		mongo.close();

		try {
			Thread.sleep(1);
		} catch (Exception e) {
		}
	}
}
