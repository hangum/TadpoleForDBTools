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

import com.hangum.tadpole.commons.util.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

/**
 * profilling example
 * 
 * @author hangum
 * 
 */
public class MongoTestProfilling {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		// 프로파일링 시작		
		System.out.println("####[profilling 중지 시작]######################################################");
		CommandResult cr = db.command(new BasicDBObject("profile", 0));
		
		System.out.println("[ok]" + cr.ok() );
		if(!cr.ok()) System.out.println("[Exception ]" + cr.getException().toString());
		System.out.println("[toString]" + JSONUtil.getPretty(cr.toString()));
		System.out.println("[size]" + cr.size() );
		System.out.println("####[profilling 중지 종료]######################################################");

		// 기존 프로파일 정보 삭제 시작 		
		System.out.println("####[profilling collections 삭제 시작]######################################################");
		if(db.collectionExists("system.profile")) {
			DBCollection profileColl = db.getCollection("system.profile");
			profileColl.drop();
		}
		System.out.println("####[profilling collections 삭제 종료]######################################################");
		// 기존 프로파일 정보 삭제 종료  
		
		// system.profile collection 생성시작 
		System.out.println("####[profilling collections 생성 시작]######################################################");
		DBObject dbObject = (DBObject) JSON.parse("{capped:true, size:8000000}");
		DBCollection dbColl = db.createCollection("system.profile", dbObject);
		BasicDBObject newProfileColl = (BasicDBObject)dbColl.getStats().copy();
		System.out.println("####[profilling collections 생성 종료]######################################################");
		// system.profile collection 생성종료 
		
		System.out.println("####[profilling 시작]######################################################");
		cr = db.command(new BasicDBObject("profile", 2));
		
		System.out.println("[ok]" + cr.ok() );
		if(!cr.ok()) System.out.println("[Exception ]" + cr.getException().toString());
		System.out.println("[toString]" + JSONUtil.getPretty(cr.toString()));
		System.out.println("[size]" + cr.size() );
		System.out.println("####[profilling 종료]######################################################");
		
//		//#######################################################################################################
//		//#######################################################################################################
//		//#######################################################################################################		
		System.out.println("####[start profilling result]######################################################");
		DBCollection myColl = db.getCollection("system.profile");
		
		BasicDBObject query = new BasicDBObject();
		query.put("millis", new BasicDBObject("$gt", 4));
		
		DBCursor myCursor = myColl.find();		
		while (myCursor.hasNext()) {
			DBObject dbObj = myCursor.next();			
			System.out.println(dbObj.get("ts") + " - " + dbObj.get("ns") + " - " + dbObj.get("nscanned") + "/" + dbObj.get("nreturned") + " millis :" + dbObj.get("millis"));
		}
		System.out.println("####[end profilling result]######################################################");		
	
		mongo.close();
	}

}
