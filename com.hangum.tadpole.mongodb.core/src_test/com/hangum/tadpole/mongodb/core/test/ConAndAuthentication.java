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

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import com.hangum.db.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class ConAndAuthentication {
	public static String serverurl = "localhost";
	public static int port = 27017;

	/**
	 * db 연결 및 권한
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();

		Mongo mongo = testMongoCls.connection(serverurl, port);
		if (mongo != null) {
			DB testDB = mongo.getDB("test");
//			Set<String> collectionNames = testMongoCls.getCollectionNames(testDB);
//			for (String collection : collectionNames) {
				testMongoCls.getCollectionInfo(testDB, "city1");
//			}
		}
		mongo.close();
	}
	
	/**
	 * mongo db를 호출합니다.
	 * 
	 * @return
	 */
	public Mongo connection(String uri, int port) {
		Mongo m = null;
		try {
			m = new Mongo(uri, port);
			List<String> listDB = m.getDatabaseNames();
			for (String dbName : listDB) {
				System.out.println(dbName);
			}

			// authentication(optional)
			// boolean auth = db.authenticate(myUserName, myPassword);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

		return m;
	}

	/**
	 * collection list
	 * 
	 * @param db
	 * @return
	 */
	public Set<String> getCollectionNames(DB db) {
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
			System.out.println(s);
		}

		return colls;
	}

	/**
	 * collection info
	 * 
	 * @param db
	 * @param collection
	 * @return
	 */
	public String getCollectionInfo(DB db, String collection) {
		DBCollection coll = db.getCollection(collection);

		System.out.println("#################################################");
		System.out.println("[collection name]" + coll.getName());
		System.out.println("[count]" + coll.getCount());

		// index list
		List<DBObject> listIndex = coll.getIndexInfo();
		List<TableColumnDAO> columnInfo = MongoDBTableColumn.tableColumnInfo(listIndex, db.getCollection(collection).findOne());

		//
		StringBuffer sbJson = new StringBuffer();

		DBCursor cursor = coll.find();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();			
			String jsonData = dbObj.toString();
			System.out.println("[data] \t" + jsonData);

			sbJson.append(jsonData);
		}

		System.out.println("#####[fully text]#########################################################");

//		System.out.println("\t\t ******[detail data][start]*******************************************");
//		ObjectMapper om = new ObjectMapper();
//		try {
//			Map<String, Object> mapObj = om.readValue(sbJson.toString(), 
//																	new TypeReference<Map<String, Object>>() {});
//			System.out.println("[json to object]" + mapObj);
//		
//			Set<String> keys = mapObj.keySet();
//			for (String key : keys) System.out.print(key + "\t:\t");
//			System.out.println();
//			
//		
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		System.out
				.println("\t\t ******[detail data][end]*******************************************");

		return "";
	}
}
