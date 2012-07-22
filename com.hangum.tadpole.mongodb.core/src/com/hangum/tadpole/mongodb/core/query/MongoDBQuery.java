package com.hangum.tadpole.mongodb.core.query;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.List;

import com.hangum.db.dao.mysql.TableColumnDAO;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.connection.MongoDBConnection;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

/**
 * mongodb 조작 query
 * 
 * @author hangum
 *
 */
public class MongoDBQuery {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBQuery.class);
	
	/**
	 * collection column list
	 * 
	 * @param userDB
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public static List<TableColumnDAO> collectionColumn(UserDBDAO userDB, String colName) throws Exception {
		DB mongoDB = MongoDBConnection.connection(userDB);
		DBCollection coll = mongoDB.getCollection(colName);
									
		return MongoDBTableColumn.tableColumnInfo(coll.getIndexInfo(), coll.findOne());
	}
	
	/**
	 * collection drop
	 * 
	 * @param userDB
	 * @param colName
	 * @throws Exception
	 */
	public static void deleteCollection(UserDBDAO userDB, String colName) throws Exception {
		DB mongoDB = MongoDBConnection.connection(userDB);
		mongoDB.getCollection(colName).drop();
	}
	
	/**
	 * collection 정보를 리턴한다.
	 * 
	 * @param userDB
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public static DBCollection findCollection(UserDBDAO userDB, String colName) throws Exception {
		DB mongoDB = MongoDBConnection.connection(userDB);
		return mongoDB.getCollection(colName);
	}
	
	/**
	 * db정보를 리턴.
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static DB findDB(UserDBDAO userDB) throws Exception {
		return MongoDBConnection.connection(userDB);
	}
	
	/**
	 * collection 생성.
	 * 
	 * @param userDB
	 * @param colName
	 * @param jsonStr
	 * @throws Exception
	 */
	public static void createCollection(UserDBDAO userDB, String colName, String jsonStr) throws Exception {
		DB mongoDB = MongoDBConnection.connection(userDB);
		
		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
		mongoDB.createCollection(colName, dbObject);
	}
	
	/**
	 * objectid find document
	 * 
	 * @param userDB
	 * @param colName
	 * @param objectId
	 * @throws Exception
	 */
	public static DBObject findDocument(UserDBDAO userDB, String colName, String objectId) throws Exception {
		DBCollection collection = findCollection(userDB, colName);
		DBObject queryObj = new BasicDBObject("_id", new ObjectId(objectId));
		
		return collection.findOne(queryObj);
	}
	
	/**
	 * insert document
	 * 
	 * @param userDB
	 * @param colName
	 * @param jsonStr
	 * @throws Exception
	 */
	public static void insertDocument(UserDBDAO userDB, String colName, String jsonStr) throws Exception {
		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
		DBCollection collection = findCollection(userDB, colName);
		
		WriteResult wr = collection.insert(dbObject);
//		결과셋에서 참조할 정보가 없어서 사용자에게 알려줄 정보가 없음(????????????????????????????????)
//		if(logger.isDebugEnabled()) {
//			logger.debug( "[writer document]" + wr.toString() );
//			logger.debug( wr.getError() );		
//			logger.debug("[n]" + wr.getN() );
//		}
	}
	
	/**
	 * delete document
	 * 
	 * @param userDB
	 * @param colName
	 * @param objectId
	 * @throws Exception
	 */
	public static void deleteDocument(UserDBDAO userDB, String colName, DBObject dbObject) throws Exception {
		DBCollection collection = findCollection(userDB, colName);
//		DBObject query = new BasicDBObject("_id", new ObjectId(objectId));
		WriteResult wr = collection.remove(dbObject);
		if(logger.isDebugEnabled()) {
			logger.debug( "[writer document]" + wr.toString() );
			logger.debug( wr.getError() );		
			logger.debug("[n]" + wr.getN() );
		}
	}
	
	/**
	 * update document
	 * 
	 * @param userDB
	 * @param colName
	 * @param dBObject
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void updateDocument(UserDBDAO userDB, String colName, DBObject dBObject, String key, String value) throws Exception {
		DBCollection collection = findCollection(userDB, colName);
		
		if(logger.isDebugEnabled()) {
			logger.debug("[dBObject] \t " + dBObject);
			logger.debug("===============================[key]\t " + key + "\t [value]\t" + value);
		}
		BasicDBObject updateQuery = new BasicDBObject().append("$set", new BasicDBObject().append(key, value));
//		BasicDBObject newDocument3 = new BasicDBObject().append("$set", new BasicDBObject().append("allPlans.0.cursor", "t2est"));
		collection.update(dBObject, updateQuery);
	}

	/**
	 * create index
	 * 
	 * @param userDB
	 * @param colName
	 * @param object
	 */
	public static void crateIndex(UserDBDAO userDB, String colName, String jsonStr) throws Exception {
		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
		
		DBCollection collection = findCollection(userDB, colName);
		collection.ensureIndex(dbObject);		
	}
	
	/**
	 * rename collection
	 * 
	 * @param userDB
	 * @param originalName
	 * @param newName
	 * @throws Exception
	 */
	public static void renameCollection(UserDBDAO userDB, String originalName, String newName) throws Exception {
		DBCollection collection = findCollection(userDB, originalName);
		collection.rename(newName, true);
	}

	/**
	 * reIndex collection
	 * 
	 * @param userDB
	 * @param colName
	 * @throws Exception
	 */
	public static void reIndexCollection(UserDBDAO userDB, String colName) throws Exception {
		DB mongoDB =  findDB(userDB);
		
		DBObject queryObj = new BasicDBObject("reIndex", colName );
		CommandResult cr = mongoDB.command(queryObj);
		
		if(!cr.ok()) throw cr.getException();
		if(logger.isDebugEnabled()) logger.debug("[reIndex] complements" + colName);
	}
}
