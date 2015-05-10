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
package com.hangum.tadpole.mongodb.core.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

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
	public static DB getInstance(UserDBDAO userDB) throws MongoDBNotFoundException, Exception {
		DB db = null;
		
		synchronized (dbManager) {
			
			try {
				String searchKey = getKey(userDB);
				Mongo mongoDB = dbManager.get( searchKey );
				
				if(mongoDB == null) {
					final MongoOptions options = new MongoOptions();
					options.connectionsPerHost = 20;
					options.threadsAllowedToBlockForConnectionMultiplier = 5;
					options.maxWaitTime = 120000;
					options.autoConnectRetry = false;
					options.safe = true;
					
					String strReplcaSet = userDB.getExt1();
					if(strReplcaSet == null | "".equals(strReplcaSet)) {
						mongoDB = new Mongo(new DBAddress(userDB.getUrl()), options);
						
					} else {
						List<ServerAddress> listServerList = new ArrayList<ServerAddress>();
						listServerList.add(new ServerAddress(userDB.getHost(), Integer.parseInt(userDB.getPort())));
						
						String[] urls = StringUtils.split(strReplcaSet, ",");
						for (String ipPort : urls) {
							String[] strIpPort = StringUtils.split(ipPort, ":");
							
							listServerList.add(new ServerAddress(strIpPort[0], Integer.parseInt(strIpPort[1])));
						}
//						options.setReadPreference(ReadPreference.primary());
						
						mongoDB = new Mongo(listServerList, options);	
					}
					
					// password 적용.
					db = mongoDB.getDB(userDB.getDb());
					if(!"".equals(userDB.getUsers())) { //$NON-NLS-1$
						// pass change
						String passwdDecrypt = "";
						try {
							passwdDecrypt = CipherManager.getInstance().decryption(userDB.getPasswd());
						} catch(Exception e) {
							passwdDecrypt = userDB.getPasswd();
						}
						
						boolean auth = db.authenticate(userDB.getUsers(), passwdDecrypt.toCharArray());
						if(!auth) {
							throw new Exception(Messages.MongoDBConnection_3);
						}
					}	

//					
//					어드민 권한이 있어야 가능한 부분이므로 주석 처리합니다.
//					
//					// db 종류를 가져옵니다.
//					List<String> listDB = mongoDB.getDatabaseNames();
//					boolean isDB = false;
//					for (String dbName : listDB) if(userDB.getDb().equals(dbName)) isDB = true;						
//					if(!isDB) {
//						throw new MongoDBNotFoundException(userDB.getDb() + Messages.MongoDBConnection_0);
//					}
					try {
						//디비가 정상 생성 되어 있는지 권한이 올바른지 검사하기 위해 날려봅니다.
						db.getCollectionNames();
					} catch(Exception e) {
						logger.error("error", e);
						throw new MongoDBNotFoundException(userDB.getDb() + " " + e.getMessage());//Messages.MongoDBConnection_0);
					}
					
					// db를 map에 넣습니다.
					dbManager.put(searchKey, mongoDB);
					
				} else {
					db = mongoDB.getDB(userDB.getDb());
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
		return userDB.getDbms_type()+userDB.getUrl()+userDB.getUsers()+userDB.getPasswd();
	}
}
