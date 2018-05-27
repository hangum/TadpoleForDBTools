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

public class UpdateEx {
	public static String MangoDB_IP = "127.0.0.1";
	public static String DB_NAME = "test";
	public static Mongo m = null;

	public static void dropCollection(String name) throws Exception {
		if (m == null) m = new Mongo(MangoDB_IP);
		DB db = m.getDB(DB_NAME);
		DBCollection coll = db.getCollection(name);
		coll.drop();
	}

	public static DBCollection retvCollection(String name) throws Exception {
		if (m == null)
			m = new Mongo(MangoDB_IP);
		DB db = m.getDB(DB_NAME);

		DBCollection coll = db.getCollection(name);
		return coll;
	}

	public static void insert(DBCollection coll, String hosting, String type,
			int clients) {
		BasicDBObject doc = new BasicDBObject();
		doc.put("hosting", hosting);
		doc.put("type", type);
		doc.put("clients", clients);
		coll.insert(doc);
	}

	public static void showCollection(DBCollection coll) {
		DBCursor cursorDocJSON = coll.find();
		while (cursorDocJSON.hasNext()) {
			System.out.println(cursorDocJSON.next());
		}
	}

	// A normal way to update an existing document. Find hosting = hostB, and
	// update it with a new document.
	public static void exam01(DBCollection collection) throws Exception {
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("hosting", "hostB");
		newDocument.put("type", "shared host");
		newDocument.put("clients", 111);

		collection.update(new BasicDBObject().append("hosting", "hostB"),
				newDocument);
	}

	// Find hosting = hostB, update the "clients" value by increasing its value
	public static void exam02(DBCollection collection) throws Exception {
		BasicDBObject newDocument = new BasicDBObject().append("$inc",
				new BasicDBObject().append("clients", 99));

		collection.update(new BasicDBObject().append("hosting", "hostB"),
				newDocument);
	}

	// Find hosting = hostA, update the “type” from “vps” to “dedicated server”.
	public static void exam03(DBCollection collection) throws Exception {
		BasicDBObject newDocument3 = new BasicDBObject().append("$set",
				new BasicDBObject().append("type", "dedicated server"));

		collection.update(new BasicDBObject().append("hosting", "hostA"),
				newDocument3);
	}

	public static void exam04(DBCollection collection) throws Exception {
		// find type = vps , update all matched documents , "clients" value to
		// 888
		BasicDBObject updateQuery = new BasicDBObject().append("$set",
				new BasicDBObject().append("clients", "888"));

		// both methods are doing the same thing.
		// collection.updateMulti(new BasicDBObject().append("type", "vps"),
		// updateQuery);
		collection.update(new BasicDBObject().append("type", "vps"),
				updateQuery, false, true);
	}
	
	public static void exam05(DBCollection collection) throws Exception {
		// find type = vps , update all matched documents , "clients" value to
		// 888
		BasicDBObject updateQuery = new BasicDBObject().append("$set",
				new BasicDBObject().append("clients", "11111"));
		
		BasicDBObject findObj = new BasicDBObject().append("hosting", "hostA");
		DBObject dbObj = collection.find(findObj).next();
		System.out.println(dbObj);

		// both methods are doing the same thing.
		// collection.updateMulti(new BasicDBObject().append("type", "vps"),
		// updateQuery);
		collection.update(dbObj, updateQuery);
	}

	public static void main(String args[]) throws Exception {
//		dropCollection("tutorial_update"); // Reset collection "tutorial_update"
		DBCollection coll = retvCollection("tutorial_update");
		insert(coll, "hostA", "vps", 1000);
//		insert(coll, "hostB", "dedicated server", 100);
//		insert(coll, "hostB", "vps", 900);

		exam05(coll);
		showCollection(coll);
	}
}
