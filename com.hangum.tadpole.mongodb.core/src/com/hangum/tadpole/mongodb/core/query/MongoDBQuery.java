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
package com.hangum.tadpole.mongodb.core.query;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.dao.mongodb.MongoDBIndexFieldDAO;
import com.hangum.tadpole.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.mongodb.core.define.MongoDBDefine;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
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
	 * create database
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static void createDB(UserDBDAO userDB) throws Exception {
		MongoOptions options = new MongoOptions();
		options.connectionsPerHost = 20;		
		Mongo mongo = new Mongo(userDB.getHost(), Integer.parseInt(userDB.getPort()));
		DB db = mongo.getDB(userDB.getDb());
		Set<String> listColNames = db.getCollectionNames();
		for (String string : listColNames) {
			System.out.println("[collection name]" + string);
		}
	}
	
	/**
	 * collection list
	 * 
	 * System collections(http://docs.mongodb.org/manual/reference/system-collections/)을 제외한 collection list를 리턴합니다.
	 * 
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<String> listCollection(UserDBDAO userDB) throws Exception {
		List<String> listReturn = new ArrayList<String>();
		
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
		for (String col : mongoDB.getCollectionNames()) {
			if(!isSystemCollection(col)) listReturn.add(col);
		}
		
		return listReturn;
	}
	
	/**
	 * collection column list
	 * 
	 * @param userDB
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public static List<CollectionFieldDAO> collectionColumn(UserDBDAO userDB, String colName) throws Exception {
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
	
//	/**
//	 * explain
//	 * 
//	 * @param userDB
//	 * @param colName
//	 * @param jsonStr
//	 * @return
//	 * @throws Exception
//	 */
//	public static String explain(UserDBDAO userDB, String colName, String jsonStr) throws Exception {
//		DBCollection collection = findCollection(userDB, colName);
//		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
//		
//		DBObject explainDBObject = collection.find(dbObject).explain();
//		return JSONUtil.getPretty(explainDBObject.toString());		
//	}
//	
//	/**
//	 * find query
//	 * 
//	 * @param userDB
//	 * @param colName
//	 * @param jsonStr
//	 * @return
//	 * @throws Exception
//	 */
//	public static DBCursor findQuery(UserDBDAO userDB, String colName, String jsonStr) throws Exception {
//		DBCollection collection = findCollection(userDB, colName);
//		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
//		
//		return collection.find(dbObject);	
//	}
	
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
	 * insert document
	 * 
	 * @param userDB
	 * @param colName
	 * @param dbObject
	 * @throws Exception
	 */
	public static void insertDocument(UserDBDAO userDB, String colName, DBObject[] dbObject) throws Exception {
		if(dbObject.length == 0) return;
		
		DBCollection collection = findCollection(userDB, colName);		
		WriteResult wr = collection.insert(dbObject);
		if(logger.isDebugEnabled()) {
			try {
				logger.debug( "[writer document]" + wr!=null?wr.toString():"" );
				logger.debug( "[wr error]" + wr!=null?wr.getError():"" );		
				logger.debug("[n]" + wr!=null?wr.getN():"" );
			} catch(Exception e) {
				logger.error("저장중에", e);
			}
		}
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
		WriteResult wr = collection.remove(dbObject);
		
		if(logger.isDebugEnabled()) {
			logger.debug( "[writer document]" + wr.toString() );
			logger.debug( wr.getError() );		
			logger.debug("[n]" + wr.getN() );
		}
		
		// 외부 참조키가 있어 삭제 되지 않는 경우
		if(wr.getN() == 0 && !"".equals(wr.getError())) {
			throw new Exception(wr.getError());
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
	 * @param strIndexName
	 * @param jsonStr
	 * @param unique	 
	 */
	public static void crateIndex(UserDBDAO userDB, String colName, String strIndexName, String jsonStr,  boolean unique) throws Exception {
		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
		
		DBCollection collection = findCollection(userDB, colName);
		collection.ensureIndex(dbObject, strIndexName, unique);
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
	 * list index
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static List<MongoDBIndexDAO> listAllIndex(UserDBDAO userDB) throws Exception {
		List<MongoDBIndexDAO> listReturnIndex = new ArrayList<MongoDBIndexDAO>();
		
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
		for (String col : mongoDB.getCollectionNames()) {
			
			if(!isSystemCollection(col)) {
				List<DBObject> listIndexObj = mongoDB.getCollection(col).getIndexInfo();
				for (DBObject dbObject : listIndexObj) {
					MongoDBIndexDAO indexDao = new MongoDBIndexDAO();
					
					indexDao.setV(dbObject.get("v").toString());
					Map<String, Object> objMap = (Map)dbObject.get("key");
					for (String strKey : objMap.keySet()) {
						indexDao.getListIndexField().add(new MongoDBIndexFieldDAO(strKey, objMap.get(strKey).toString()));
					}
					
					if(dbObject.containsField("unique")) {
						indexDao.setUnique( Boolean.valueOf(dbObject.get("unique").toString()) );
					}
					String strNs = dbObject.get("ns").toString();
					strNs = StringUtils.substringAfter(strNs, userDB.getDb() + ".");
					
					indexDao.setNs(strNs);
					indexDao.setName(dbObject.get("name").toString());				

					listReturnIndex.add(indexDao);
				}
			}
		}
		
		return listReturnIndex;
	}
	
	/**
	 * all Server Side Java Script
	 * @param userDB
	 * @throws Exception
	 */
	public static List<MongoDBServerSideJavaScriptDAO> listAllJavaScript(UserDBDAO userDB) throws Exception {
		List<MongoDBServerSideJavaScriptDAO> listReturn = new ArrayList<MongoDBServerSideJavaScriptDAO>();
		
		DBCursor dbCursor = findDB(userDB).getCollection("system.js").find();
		List<DBObject> lsitCursor = dbCursor.toArray();
		for (DBObject dbObject : lsitCursor) {
			listReturn.add(new MongoDBServerSideJavaScriptDAO(dbObject.get("_id").toString(), dbObject.get("value").toString()));
		}
		
		return listReturn;
	}

	/**
	 * save java script
	 * 
	 * @param userDB
	 * @param javaScriptDAO
	 * @throws Exception
	 */
	public static void insertJavaScript(UserDBDAO userDB, MongoDBServerSideJavaScriptDAO javaScriptDAO) throws Exception {
		DBObject dbObject = (DBObject) JSON.parse("{'_id':'" + javaScriptDAO.getName() + "', 'value':'" +  javaScriptDAO.getContent() + "'}");
		findDB(userDB).getCollection("system.js").save(dbObject);
	}
	
	/**
	 * update java script
	 * 
	 * @param userDB
	 * @param _id
	 * @param content
	 * @throws Exception
	 */
	public static void updateJavaScript(UserDBDAO userDB, String _id, String content) throws Exception {
		DBObject dbFindObject = (DBObject) JSON.parse("{'_id':'" + _id + "'}");
		DBObject dbUpdateObject = (DBObject) JSON.parse("{'_id':'" + _id + "', 'value':'" + content +"'}");
		
		findDB(userDB).getCollection("system.js").findAndModify(dbFindObject, dbUpdateObject);
	}
	
	/**
	 * remove javascript
	 * 
	 * @param userDB
	 * @param _id
	 * @throws Exception
	 */
	public static void deleteJavaScirpt(UserDBDAO userDB, String _id) throws Exception {
		DBObject dbFindObject = (DBObject) JSON.parse("{'_id':'" + _id + "'}");
		findDB(userDB).getCollection("system.js").remove(dbFindObject);
	}
	
	/**
	 * execute eval
	 * 
	 * @param userDB
	 * @param content
	 * @param inputArray
	 * @return
	 * @throws Exception
	 */
	public static Object executeEval(UserDBDAO userDB, String content, Object[] inputArray) throws Exception {
		Object dbObject = findDB(userDB).eval(content, inputArray);
		
		return dbObject;
	}
	
	/**
	 * is system collection
	 * @param colName
	 * @return
	 */
	public static boolean isSystemCollection(String colName) {
		for(String sysColl : MongoDBDefine.SYSTEM_COLLECTION) {
			if(colName.equals(sysColl)) return true;
		}
		
		return false;
	}
	
	/**
	 * Server status, return to String
	 * 
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
	 * Server status return to CommandResult
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static CommandResult serverStatusCommandResult(UserDBDAO userDB) throws Exception {
		DB mongoDB =  findDB(userDB);
		
		DBObject queryObj = new BasicDBObject("serverStatus", 1);
		return mongoDB.command(queryObj);		
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
		
		UserDBDAO adminDB = new UserDBDAO();
		adminDB.setHost(userDB.getHost());
		adminDB.setPort(userDB.getPort());
		adminDB.setUsers(userDB.getUsers());
		adminDB.setPasswd(userDB.getPasswd());
		adminDB.setUrl(userDB.getHost() + ":" + userDB.getPort() + "/admin");
		adminDB.setDb("admin");
		
		DB mongoDB =  findDB(adminDB);
		
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
	 * bucket list 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<String> getGridFSBucketList(UserDBDAO userDB) throws Exception {
		List<String> listStr = new ArrayList<String>();
		
		DB mongoDb = findDB(userDB);
		Set<String> colNames = mongoDb.getCollectionNames();
		for (String name : colNames) {
			if(StringUtils.contains(name, ".chunks")) listStr.add(StringUtils.removeEnd(name, ".chunks"));			
		}
		
		return listStr;
	}
	
	/**
	 * GridFS chunck detail information
	 * 
	 * @param userDB
	 * @param objectId
	 * @return
	 * @throws Exception
	 */
	public static DBCursor getGridFSChunckDetail(UserDBDAO userDB, String searchChunkName, String objectId) throws Exception {
		DBCollection col = findCollection(userDB, searchChunkName);
		
		// 
		DBObject queryObj = new BasicDBObject();
		queryObj.put("files_id", new ObjectId(objectId));
		
		// field
		DBObject fieldObj = new BasicDBObject();
		fieldObj.put("files_id", 1);
		fieldObj.put("n", 1);
		
		return col.find(queryObj, fieldObj);
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
	
		GridFSDBFile gridFSFile = gridFs.findOne(new ObjectId(_id));
		InputStream is = gridFSFile.getInputStream();
		return IOUtils.toByteArray(is);
	}
	
	/**
	 * delte gridfs
	 * 
	 * @param userDB
	 * @param _id
	 * @throws Exception
	 */
	public static void dleteGridFs(UserDBDAO userDB, String strBucket, String _id) throws Exception {
		DB mongoDb = findDB(userDB);
		GridFS gridFs = null;
		
		if("".equals(strBucket)) gridFs = new GridFS(mongoDb);
		else gridFs = new GridFS(mongoDb, strBucket);
		
		gridFs.remove(gridFs.findOne(new ObjectId(_id)));
	
	}
	
	/**
	 * insert gridfs
	 * 
	 * @param userDB
	 * @param strBucket
	 * @param fileList
	 * @throws Exception
	 */
	public static void insertGridFS(UserDBDAO userDB, String strBucket, List<String> fileList) throws Exception {
		
		DB mongoDb = findDB(userDB);
		GridFS gridFs = null;
		
		if("".equals(strBucket)) gridFs = new GridFS(mongoDb);
		else gridFs = new GridFS(mongoDb, strBucket);
		
		for (String strFile : fileList) {
			String saveFileName = strFile.substring(strFile.lastIndexOf(File.separator)+1);
			
			GridFSInputFile gfsFile = gridFs.createFile(new File(strFile));
			gfsFile.setFilename(saveFileName);
			gfsFile.save();
		}
		
	}

	/**
	 * index를 삭제합니다.
	 * 
	 * @param userDB
	 * @param name
	 */
	public static void dropIndex(UserDBDAO userDB, String colName, String name) throws Exception {
		findCollection(userDB, colName).dropIndex(name);		
	}
	
	
}
