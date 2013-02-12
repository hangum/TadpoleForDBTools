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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * db index list
 * 
 * @author hangum
 * 
 */
public class MongoTestIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

//		for(int i=0; i<1000000; i++) {
			ConAndAuthentication testMongoCls = new ConAndAuthentication();
			Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
			DB db = mongo.getDB("test");
			
			Set<String> setCollection = db.getCollectionNames();
			for (String colName : setCollection) {
				DBCollection col = db.getCollection(colName);
				List<DBObject> listIndex = col.getIndexInfo();
				System.out.println("[colection]" + colName);
				for (DBObject dbObject : listIndex) {
					System.out.println("\t" + dbObject);
//					Map<String, Integer> objMap = (Map)dbObject.get("key");
//					Set<String> objMapKey = objMap.keySet();
//					for (String strKey : objMapKey) {
//						System.out.println("[key]" + strKey + "\t [value]" + objMap.get(strKey).toString());
//					}
				}
			}

			mongo.close();
			
			try {
				Thread.sleep(1);
			} catch(Exception e) {}
//		}
	}

}
