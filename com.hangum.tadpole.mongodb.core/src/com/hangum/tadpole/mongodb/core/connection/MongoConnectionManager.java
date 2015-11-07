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
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * mongo db connection
 * 
 * @author hangum
 *
 */
public class MongoConnectionManager {
	private static final Logger logger = Logger.getLogger(MongoConnectionManager.class);
	
	private static Map<String, MongoClient >  dbManager = null;
	private static MongoConnectionManager mongodbConnectionManager = null;
	
	static {
		if(mongodbConnectionManager == null) {
			mongodbConnectionManager = new MongoConnectionManager();
			dbManager = new HashMap<String, MongoClient>();
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
		
		/*
		 * https://mongodb.github.io/casbah/guide/connecting.html
		 */
		synchronized (dbManager) {
			String searchKey = getKey(userDB);
			try {
				MongoClient mongoDB = dbManager.get( searchKey );
				
				if(mongoDB == null) {
					MongoClientOptions clientOptions = MongoClientOptions.builder().connectionsPerHost(200).connectTimeout(5000).socketTimeout(5000).build();
					
					List<MongoCredential> listCredential = new ArrayList<>();
					if(!"".equals(userDB.getUsers())) { //$NON-NLS-1$
						// pass change
						String passwdDecrypt = "";
						try {
							passwdDecrypt = CipherManager.getInstance().decryption(userDB.getPasswd());
						} catch(Exception e) {
							passwdDecrypt = userDB.getPasswd();
						}
						
						MongoCredential mongocredintial = MongoCredential.createCredential(userDB.getUsers(), userDB.getDb(), passwdDecrypt.toCharArray());
						listCredential.add(mongocredintial);
					}
					
					String strReplcaSet = userDB.getExt1();
					if(strReplcaSet == null | "".equals(strReplcaSet)) {
						if(!listCredential.isEmpty()) {
							ServerAddress sa = new ServerAddress(userDB.getHost(), Integer.parseInt(userDB.getPort()));
							mongoDB = new MongoClient(sa, listCredential, clientOptions);
						} else {
							mongoDB = new MongoClient(new MongoClientURI(userDB.getUrl()));
						}
						
					} else {
						List<ServerAddress> listServerList = new ArrayList<ServerAddress>();
						listServerList.add(new ServerAddress(userDB.getHost(), Integer.parseInt(userDB.getPort())));
						
						String[] urls = StringUtils.split(strReplcaSet, ",");
						for (String ipPort : urls) {
							String[] strIpPort = StringUtils.split(ipPort, ":");
							
							listServerList.add(new ServerAddress(strIpPort[0], Integer.parseInt(strIpPort[1])));
						}
						
						if(!listCredential.isEmpty()) {
							mongoDB = new MongoClient(listServerList, listCredential, clientOptions);
						} else {
							mongoDB = new MongoClient(listServerList, clientOptions);
						}
					}
					
					// db를 map에 넣습니다.
					dbManager.put(searchKey, mongoDB);
				}
				db = mongoDB.getDB(userDB.getDb());

				
			} catch(Exception e) {
				logger.error("mongodb connection error", e);	
				dbManager.remove(searchKey);
				
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
		return userDB.getDbms_type()+userDB.getUrl()+userDB.getUsers()+userDB.getPasswd()+userDB.getDisplay_name();
	}
}
