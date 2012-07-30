package com.hangum.tadpole.mongodb.core.query;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.hangum.db.dao.mysql.TableColumnDAO;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
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
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
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
	public static void dropCollection(UserDBDAO userDB, String colName) throws Exception {
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
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
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
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
		return MongoConnectionManager.getInstance(userDB);
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
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
		
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
	
	/**
	 * Server status
	 * @param userDB
	 * @throws Exception
	 */
	public static String serverStatus(UserDBDAO userDB) throws Exception {
		DB mongoDB =  findDB(userDB);
		
		DBObject queryObj = new BasicDBObject("serverStatus", 1);
		CommandResult cr = mongoDB.command(queryObj);
		if(cr.ok()) {
			return cr.toString();
		} else {
			throw cr.getException();
		}
	}
	
	/**
	 * top
	 * 
	 * @param userDB
	 * @param top
	 * @return
	 * @throws Exception
	 */
	public static String top(UserDBDAO userDB, int top) throws Exception {
		DB mongoDB =  findDB(userDB);
		
		DBObject queryObj = new BasicDBObject("top", top);
		CommandResult cr = mongoDB.command(queryObj);
		if(cr.ok()) {
			return cr.toString();
		} else {
			
			logger.error("top command" + userDB, cr.getException());
			throw new Exception(cr.getErrorMessage());//cr.getException();
		}
	}

	/**
	 * mongodb web console 정보 
	 * 
	 * @param userDB
	 * @return
	 */
	public static String webConsole(UserDBDAO userDB) throws Exception {
		return "http://" + userDB.getHost() + ":" + (Integer.parseInt(userDB.getPort()) + 1000);
	}
	
	/**
	 * db stats
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static String dbStats(UserDBDAO userDB) throws Exception {
		DB mongoDB =  findDB(userDB);
		CommandResult cr = mongoDB.getStats();
		
		if(cr.ok()) {
			return cr.toString();
		} else {
			throw cr.getException();
		}
	}
	
	/**
	 * collection exist
	 * 
	 * @param userDB
	 * @param collName
	 * @return
	 * @throws Exception
	 */
	public static boolean collectionExist(UserDBDAO userDB, String collName) throws Exception {
		DB mongoDB =  findDB(userDB);
		return mongoDB.collectionExists(collName);
	}

	/**
	 * profiling start
	 * @param userDB
	 * @param level 0 : stop, 1 or 2 : start
	 * @return
	 * @throws Exception
	 */
	public static boolean commandProfiling(UserDBDAO userDB, int level) throws Exception {
		DB mongoDB =  findDB(userDB);
		
		DBObject queryObj = new BasicDBObject("profile", level);
		CommandResult cr = mongoDB.command(queryObj);
		
		if(cr.ok()) {
			return true;
		} else {
			throw cr.getException();
		}
	}
	
	/**
	 * currentOp
	 * 
	 * @param userDB
	 * @return
	 * @throws Excepiton
	 */
	public static DBObject currentOp(UserDBDAO userDB) throws Exception {
		DB mongoDb = findDB(userDB);
		DBObject dbObj = (DBObject)mongoDb.eval("db.currentOp()");
		
		return dbObj;
	}
	
	/**
	 * killOp
	 * 
	 * @param userDB
	 * @param opId
	 * @throws Exception
	 */
	public static void killOp(UserDBDAO userDB, String opId) throws Exception {
		DB mongoDb = findDB(userDB);
		DBObject dbObj = (DBObject)mongoDb.eval("db.killOp(" + opId + ")");
		if(logger.isDebugEnabled()) logger.debug("[killOp] " + dbObj);
	}
	
	/**
	 * user 추가.
	 * 
	 * @param userDB
	 * @param id
	 * @param passwd
	 * @param isReadOnly
	 * @throws Exception
	 */
	public static void addUser(UserDBDAO userDB, String id, String passwd, boolean isReadOnly) throws Exception {
		DB mongoDb = findDB(userDB);		
		WriteResult wr = mongoDb.addUser(id, passwd.toCharArray(), isReadOnly);		
	}
	
	/**
	 * 사용자 리스트 
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static DBCursor getUser(UserDBDAO userDB) throws Exception {
		DB mongoDb = findDB(userDB);
		return mongoDb.getCollection("system.users").find();
	}
	
	/**
	 * 사용자 삭제.
	 * @param userDB
	 * @param id
	 * @throws Exception
	 */
	public static void deleteUser(UserDBDAO userDB, String id) throws Exception {
		DBCollection collection = findCollection(userDB, "system.users");
		
		DBObject query = new BasicDBObject("user", id);
		WriteResult wr = collection.remove(query);
	}
	
	/**
	 * gridFS
	 * 
	 * @param userDB
	 * @param strBucket
	 * @param strFileName
	 * @param intSkip
	 * @param intLimit
	 * @return
	 * @throws Exception
	 */
	public static DBCursor getGridFS(UserDBDAO userDB, String strBucket, String strFileName, int intSkip, int intLimit) throws Exception {
		DB mongoDb = findDB(userDB);
		GridFS gridFs = null;
		
		if("".equals(strBucket)) gridFs = new GridFS(mongoDb);
		else gridFs = new GridFS(mongoDb, strBucket);
		
		if("".equals(strFileName)) {
			return gridFs.getFileList().skip(intSkip).limit(intLimit);
		} else {
			DBObject queryObj = new BasicDBObject(); 
			Pattern regex = Pattern.compile(".*" + strFileName + "*");
			queryObj.put("filename", regex);
			
			return gridFs.getFileList(queryObj).skip(intSkip).limit(intLimit);
		}
	}
	
	/**
	 * get gridfs data
	 * 
	 * @param userDB
	 * @param _id
	 * @return 
	 * @throws Exception
	 */
	public static byte[] getGridFS(UserDBDAO userDB, String strBucket, String _id) throws Exception {
		DB mongoDb = findDB(userDB);
		GridFS gridFs = null;
		
		if("".equals(strBucket)) gridFs = new GridFS(mongoDb);
		else gridFs = new GridFS(mongoDb, strBucket);
	
		ObjectId objectId = new ObjectId(_id);
		GridFSDBFile gridFSFile = gridFs.findOne(objectId);
		InputStream is = gridFSFile.getInputStream();
		return IOUtils.toByteArray(is);
	}
	
}
