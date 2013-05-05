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
	public static String REF_1 = "TEST_REF1";
	public static String REF_2 = "TEST_REF2";
			

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		DBObject colInformation = (DBObject) JSON.parse("{capped:true, size:100000}");
		DBCollection ref1Coll  = db.getCollection(REF_1);
		if(ref1Coll != null) ref1Coll.drop(); 
		ref1Coll = db.createCollection(REF_1, colInformation);		
		
		DBObject dbObjRef1 = (DBObject) JSON.parse("{ 'name' : 'cho'}");//"{'names': {'First': 'Gonza', 'Last': 'Vieira'}}");
		WriteResult wr = ref1Coll.insert(dbObjRef1);
		
		DBObject retDBObj = ref1Coll.findOne();
		createRef1Collection(db, retDBObj.get("_id"));
		
		mongo.close();
	}
	
	
	public static void createRef1Collection(DB db, Object objId) {
		DBObject colInformation = (DBObject) JSON.parse("{capped:true, size:100000}");
		DBCollection ref2Coll  = db.getCollection(REF_2);
		if(ref2Coll != null) ref2Coll.drop();
		ref2Coll = db.createCollection(REF_2, colInformation);
		
		BasicDBObject insertObj = new BasicDBObject();
		insertObj.put(REF_1 + "_id", objId);
		
		DBObject addField = new BasicDBObject();
		addField.put("name", "Reference id");
		
		insertObj.putAll(addField);
		
//		DBObject dbObjRef2 = (DBObject) JSON.parse("{'ref1_id': 50f9437cf023f820730a3b42,  {'names': {'First': 'Gonza', 'Last': 'Vieira'}}}");
		ref2Coll.insert(insertObj);
	}
	

}
