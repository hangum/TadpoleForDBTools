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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.dao.system.UserDBDAO;
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
				Mongo mongo = dbManager.get( searchKey );
				
				if(mongo == null) {
					final MongoOptions options = new MongoOptions();
					options.connectionsPerHost = 20;
					options.threadsAllowedToBlockForConnectionMultiplier = 5;
					options.maxWaitTime = 120000;
					options.autoConnectRetry = false;
					options.safe = true;
					
					String strReplcaSet = userDB.getExt1();
					if("".equals(strReplcaSet)) {
						mongo = new Mongo(new DBAddress(userDB.getUrl()), options);	
					} else {
						List<ServerAddress> listServerList = new ArrayList<ServerAddress>();
						listServerList.add(new ServerAddress(userDB.getHost(), Integer.parseInt(userDB.getPort())));
						
						String[] urls = StringUtils.split(strReplcaSet, ",");
						for (String ipPort : urls) {
							String[] strIpPort = StringUtils.split(ipPort, ":");
							
							listServerList.add(new ServerAddress(strIpPort[0], Integer.parseInt(strIpPort[1])));
						}
						
//						options.setReadPreference(ReadPreference.primary());
						
						mongo = new Mongo(listServerList, options);	
						
					}
					List<String> listDB = mongo.getDatabaseNames();
					
					boolean isDB = false;
					for (String dbName : listDB) if(userDB.getDb().equals(dbName)) isDB = true;						
					if(!isDB) {
						throw new MongoDBNotFoundException(userDB.getDb() + Messages.MongoDBConnection_0);
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
}
