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

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
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

//	/**
//	 * db 연결 및 권한
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		ConAndAuthentication testMongoCls = new ConAndAuthentication();
//
//		Mongo mongo = testMongoCls.connection(serverurl, port);
//		if (mongo != null) {
//			DB testDB = mongo.getDB("test");
//			// Set<String> collectionNames =
//			// testMongoCls.getCollectionNames(testDB);
//			// for (String collection : collectionNames) {
//			testMongoCls.getCollectionInfo(testDB, "store");
//			// }
//		}
//		mongo.close();
//	}

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

//		} catch (UnknownHostException e) {
//			e.printStackTrace();
		} catch (Exception e) {
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
		List<CollectionFieldDAO> columnInfo = MongoDBTableColumn.tableColumnInfo(listIndex, db.getCollection(collection).findOne());
		for (CollectionFieldDAO collectionFieldDAO : columnInfo) {
			System.out.println("[field]" + collectionFieldDAO.getField() );
			
			if(!collectionFieldDAO.getChildren().isEmpty()) {
				List<CollectionFieldDAO> childColl = collectionFieldDAO.getChildren();
				for (CollectionFieldDAO collectionFieldDAO2 : childColl) {
					System.out.println("\t [child field]" + collectionFieldDAO2.getField());
					
					if(!collectionFieldDAO2.getChildren().isEmpty()) {
						List<CollectionFieldDAO> childColl2 = collectionFieldDAO2.getChildren();
						for (CollectionFieldDAO collectionFieldDAO3 : childColl2) {
							System.out.println("\t\t [child field]" + collectionFieldDAO3.getField());	
						}
						
					}	
				}
				
			}
		}

//		//
//		StringBuffer sbJson = new StringBuffer();
//
//		DBCursor cursor = coll.find();
//		while (cursor.hasNext()) {
//			DBObject dbObj = cursor.next();
//			String jsonData = dbObj.toString();
//			System.out.println("[data] \t" + jsonData);
//
//			sbJson.append(jsonData);
//		}
//
//		System.out
//				.println("#####[fully text]#########################################################");

		// System.out.println("\t\t ******[detail data][start]*******************************************");
		// ObjectMapper om = new ObjectMapper();
		// try {
		// Map<String, Object> mapObj = om.readValue(sbJson.toString(),
		// new TypeReference<Map<String, Object>>() {});
		// System.out.println("[json to object]" + mapObj);
		//
		// Set<String> keys = mapObj.keySet();
		// for (String key : keys) System.out.print(key + "\t:\t");
		// System.out.println();
		//
		//
		// } catch (JsonParseException e) {
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		System.out
				.println("\t\t ******[detail data][end]*******************************************");

		return "";
	}
}
