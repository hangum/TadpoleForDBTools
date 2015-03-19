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
package com.hangum.tadpole.engine.manager.transaction;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * DBCP connection manager
 * 
 * @author hangum
 *
 */
public class DBCPConnectionManager {
	public static DBCPConnectionManager instance = new DBCPConnectionManager();
	private Map<String, DataSource> mapDataSource = new HashMap<String, DataSource>();
	private DBCPConnectionManager() {}
	
	public static DBCPConnectionManager getInstance() {
		return instance;
	}
	
	private DataSource makePool(final String userId, UserDBDAO userDB) {
		GenericObjectPool connectionPool = new GenericObjectPool();
		connectionPool.setMaxActive(2);
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
		mapDataSource.put(TadpoleSQLTransactionManager.getKey(userId, userDB), ds);
		
		return ds;
	}
	
	public DataSource getDataSource(final String userId, final UserDBDAO userDB) {
		DataSource retDataSource = mapDataSource.get(TadpoleSQLTransactionManager.getKey(userId, userDB));
		if(retDataSource == null) { 
			return makePool(userId, userDB);
		}
		
		return retDataSource;
	}
	
//	/**
//	 * map의 카를 가져옵니다.
//	 * @param userDB
//	 * @return
//	 */
//	private static String getKey(final String userId, final UserDBDAO userDB) {
//		return userId + userDB.getSeq() + userDB.getDbms_types()+userDB.getUrl()+userDB.getUsers();//+dbInfo.getPasswd();
//	}

}
