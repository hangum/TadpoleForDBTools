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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

/**
 * MongoDB ServerSide JavaScirpt
 * 
 * @author hangum
 * 
 */
public class MongoTestServerSideJavascript {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		createServerSideJavaScript(db);
		findAllServerSideJavaScript(db);
		
//		Object[] arryArgs ={25, 34};
//		evalServerSideJavaScript(db, "addNumbers", arryArgs);
		
		String[] arrayString = {"store"};
		evalServerSideJavaScript(db, "findCollection", arrayString);
		
		mongo.close();
		
		try {
			Thread.sleep(1);
		} catch(Exception e) {}
	}
	
	/**
	 * java script 생성
	 * @param db
	 */
	private static void createServerSideJavaScript(DB db) {
//		DBObject dbObject = (DBObject) JSON.parse("{'_id':'addNumbers', 'value':'function(x, y){ return x + y; }'}");
//		db.getCollection("system.js").save(dbObject);
		
//		DBObject dbObject = (DBObject) JSON.parse("{'_id':'addNumbers2', 'value':'function(x, y){ return x + y; }'}");
//		db.getCollection("system.js").save(dbObject);	
	}
	
	/**
	 * 모든 스크립트 리턴
	 * @param db
	 */
	private static void findAllServerSideJavaScript(DB db) {
		DBCursor dbCursor = db.getCollection("system.js").find();
		List<DBObject> lsitCursor = dbCursor.toArray();
		for (DBObject dbObject : lsitCursor) {
			System.out.println(dbObject.toString());
		}
	}
	
	/**
	 * 자바스크립트 내용
	 *  
	 * @param db
	 * @param jsName
	 * @return
	 */
	private static String findServerSideJavaScript(DB db, String jsName) {
		DBObject findDbObject = new BasicDBObject();
		findDbObject.put("_id", jsName);
		
		DBCursor dbCursor = db.getCollection("system.js").find(findDbObject);
		DBObject dbObject = dbCursor.next();
		return dbObject.get("value").toString();
	}
	
	/**
	 * 자바스크립트 생성
	 * 
	 * @param db
	 * @param name
	 * @param arryArgs
	 */
	private static void evalServerSideJavaScript(DB db, String name, Object[] arryArgs) {
		Object dbObject = db.eval(findServerSideJavaScript(db, name), arryArgs);
		System.out.println("[result]\t" + dbObject);
	}

}
