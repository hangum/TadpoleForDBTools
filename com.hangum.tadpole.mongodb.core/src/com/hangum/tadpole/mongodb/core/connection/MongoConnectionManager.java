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
package com.hangum.tadpole.mongodb.core.connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

/**
 * mongo db connection
 * 
 * @author hangum
 *
 */
public class MongoConnectionManager {
	private static final Logger logger = Logger.getLogger(MongoConnectionManager.class);
	
	private static Map<String, Mongo >  dbManager = null;
	private static MongoConnectionManager mongodbConnectionManager = null;
	
	static {
		if(mongodbConnectionManager == null) {
			mongodbConnectionManager = new MongoConnectionManager();
			dbManager = new HashMap<String, Mongo>();
		}
	}
	
	private MongoConnectionManager() {}
	
	/**
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static DB getInstance(UserDBDAO userDB) throws Exception {
		DB db = null;
		
		synchronized (dbManager) {
			
			try {
				String searchKey = getKey(userDB);
				Mongo mongo = dbManager.get( searchKey );
				
				if(mongo == null) {
					MongoOptions options = new MongoOptions();
					options.connectionsPerHost = 20;
					
					mongo = new Mongo(new DBAddress(userDB.getUrl()), options);
					List<String> listDB = mongo.getDatabaseNames();
					
					boolean isDB = false;
					for (String dbName : listDB) if(userDB.getDb().equals(dbName)) isDB = true;						
					if(!isDB) {
						throw new Exception(userDB.getDb() + Messages.MongoDBConnection_0);
					}
					
					// password 검색
					db = mongo.getDB(userDB.getDb());
					if(!"".equals(userDB.getUsers())) { //$NON-NLS-1$
						boolean auth = db.authenticate(userDB.getUsers(), userDB.getPasswd().toCharArray() );
						if(!auth) {
							throw new Exception(Messages.MongoDBConnection_3);
						}
					}	
					
					//
					dbManager.put(searchKey, mongo);
					
				} else {
					db = mongo.getDB(userDB.getDb());
				}
				
			} catch(Exception e) {
				logger.error("mongodb connection error", e);				
				throw e;
			}
		}
		
		return db;
		
	}
	
	/**
	 * map의 카를 가져옵니다.
	 * @param userDB
	 * @return
	 */
	private static String getKey(UserDBDAO userDB) {
		return userDB.getTypes()+userDB.getUrl()+userDB.getUsers()+userDB.getPasswd();
	}
//	/**
//	 * mongo db connection
//	 * 
//	 * @param userDB
//	 * @return
//	 * @throws Exception
//	 */
//	public static DB connection(UserDBDAO userDB) throws Exception {
//		Mongo mongo = null;
//		try {
//			MongoOptions options = new MongoOptions();
//			options.connectionsPerHost = 20;
//			
//			mongo = new Mongo(new DBAddress(userDB.getUrl()), options);
//			List<String> listDB = mongo.getDatabaseNames();
//			
//			boolean isDB = false;
//			for (String dbName : listDB) if(userDB.getDatabase().equals(dbName)) isDB = true;						
//			if(!isDB) {
//				throw new Exception(userDB.getDatabase() + Messages.MongoDBConnection_0);
//			}
//			
//			// password 검색
//			DB db = mongo.getDB(userDB.getDatabase());
//			if(!"".equals(userDB.getUser())) { //$NON-NLS-1$
//				boolean auth = db.authenticate(userDB.getUser(), userDB.getPasswd().toCharArray() );
//				if(!auth) {
//					throw new Exception(Messages.MongoDBConnection_3);
//				}
//			}
//			
//			return db;
//			
//		} catch (UnknownHostException e) {
//			logger.error("mongo db connection", e); //$NON-NLS-1$
//			
//			throw new Exception(Messages.MongoDBConnection_2);
//		} catch (MongoException e) {
//			logger.error("monodb exception", e); //$NON-NLS-1$
//			throw new Exception(Messages.MongoDBConnection_4 + e.getMessage());
////		} finally {
////			mongo.close();
//		}
//		
//	}
}
