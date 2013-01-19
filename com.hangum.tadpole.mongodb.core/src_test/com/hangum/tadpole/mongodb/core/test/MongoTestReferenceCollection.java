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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

/**
 * create collection (ref1, ref2)
 * ref1 and ref2- reference collection
 * 
 * 
 * see http://docs.mongodb.org/manual/applications/database-references/
 * 
 * @author hangum
 * 
 */
public class MongoTestReferenceCollection {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBObject colInformation = (DBObject) JSON.parse("{capped:true, size:100000}");
		DBCollection ref1Coll  = db.getCollection("ref1");
		if(ref1Coll != null) ref1Coll.drop(); 
		ref1Coll = db.createCollection("ref1", colInformation);		
		
		DBObject dbObjRef1 = (DBObject) JSON.parse("{'names': {'First': 'Gonza', 'Last': 'Vieira'}}");
		WriteResult wr = ref1Coll.insert(dbObjRef1);
		
		DBObject retDBObj = ref1Coll.findOne();
		createRef1Collection(db, retDBObj.get("_id"));
		
		mongo.close();
	}
	
	
	public static void createRef1Collection(DB db, Object objId) {
		DBObject colInformation = (DBObject) JSON.parse("{capped:true, size:100000}");
		DBCollection ref2Coll  = db.getCollection("ref2");
		if(ref2Coll != null) ref2Coll.drop();
		ref2Coll = db.createCollection("ref2", colInformation);
		
		BasicDBObject insertObj = new BasicDBObject();
		insertObj.put("ref1_id", objId);
		
		DBObject addField = new BasicDBObject();
		addField.put("name", "Reference id");
		
		insertObj.putAll(addField);
		
//		DBObject dbObjRef2 = (DBObject) JSON.parse("{'ref1_id': 50f9437cf023f820730a3b42,  {'names': {'First': 'Gonza', 'Last': 'Vieira'}}}");
		ref2Coll.insert(insertObj);
	}
	

}
