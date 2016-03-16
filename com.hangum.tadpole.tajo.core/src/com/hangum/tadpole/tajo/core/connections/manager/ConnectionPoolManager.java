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
package com.hangum.tadpole.tajo.core.connections.manager;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * DBCP connection manager
 * 
 * @author hangum
 *
 */
public class ConnectionPoolManager {
	private static final Logger logger = Logger.getLogger(ConnectionPoolManager.class);
	
	static {
		try {
			Class.forName("org.apache.tajo.jdbc.TajoDriver");
		} catch(Exception e) {
			logger.error("Apache Tajo Class not found exception", e);
		}
	}
	
	public static ConnectionPoolManager instance = null;//new ConnectionPoolManager();
	private static Map<String, DataSource> mapDataSource = new HashMap<String, DataSource>();
	private ConnectionPoolManager() {}
	
	public static ConnectionPoolManager getInstance(final UserDBDAO userDB) {
		if(instance == null) {
			instance = new ConnectionPoolManager();
		}
		return instance;
	}
	
	private static DataSource makePool(UserDBDAO userDB) {
		GenericObjectPool connectionPool = new GenericObjectPool();
		connectionPool.setMaxActive(5);
//		connectionPool.setWhenExhaustedAction((byte)1);
//		connectionPool.setMaxWait(1000 * 60); 					// 1분대기.
//		connectionPool.setTimeBetweenEvictionRunsMillis(3 * 1000);
		connectionPool.setTestWhileIdle(true);
		
		String passwdDecrypt = "";
		try {
			passwdDecrypt = CipherManager.getInstance().decryption(userDB.getPasswd());
		} catch(Exception e) {
			passwdDecrypt = userDB.getPasswd();
		}
		ConnectionFactory cf = new DriverManagerConnectionFactory(userDB.getUrl(), userDB.getUsers(), passwdDecrypt);

		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
		DataSource ds = new PoolingDataSource(connectionPool);
		mapDataSource.put(getKey(userDB), ds);
		
		return ds;
	}
	
	public static DataSource getDataSource(final UserDBDAO userDB) {
		DataSource retDataSource = mapDataSource.get(getKey(userDB));
		if(retDataSource == null) { 
			return makePool(userDB);
		}
		
		return retDataSource;
	}
	
	/**
	 * map의 카를 가져옵니다.
	 * @param userDB
	 * @return
	 */
	private static String getKey(final UserDBDAO userDB) {
		return userDB.getSeq() + userDB.getDbms_type()+userDB.getUrl()+userDB.getUsers();//+dbInfo.getPasswd();
	}

}
