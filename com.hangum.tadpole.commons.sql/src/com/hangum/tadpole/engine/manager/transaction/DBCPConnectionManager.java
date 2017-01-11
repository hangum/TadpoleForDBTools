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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * DBCP connection manager
 * 
 * @author hangum
 *
 */
public class DBCPConnectionManager {
	private static final Logger logger = Logger.getLogger(DBCPConnectionManager.class);
	
	public static DBCPConnectionManager instance = new DBCPConnectionManager();
	private Map<String, DataSource> mapDataSource = new HashMap<String, DataSource>();
//	private Map<String, GenericObjectPool> mapGenericObject = new ConcurrentHashMap<String, GenericObjectPool>();
	
	private DBCPConnectionManager() {}
	
	public static DBCPConnectionManager getInstance() {
		return instance;
	}
	
	private DataSource makePool(final String searchKey, UserDBDAO userDB) {
		GenericObjectPool connectionPool = new GenericObjectPool();
		connectionPool.setMaxActive(2);
		connectionPool.setWhenExhaustedAction((byte)1);
		connectionPool.setMaxWait(1000 * 60); 								// 1분대기.
		connectionPool.setTimeBetweenEvictionRunsMillis(60L * 1000L * 1L);	// 60초에 한번씩 테스트
		connectionPool.setTestWhileIdle(true);
		
		String passwdDecrypt = "";
		try {
			passwdDecrypt = CipherManager.getInstance().decryption(userDB.getPasswd());
		} catch(Exception e) {
			passwdDecrypt = userDB.getPasswd();
		}
		
		ConnectionFactory cf = new DriverManagerConnectionFactory(userDB.getUrl(), userDB.getUsers(), passwdDecrypt);
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
		pcf.setValidationQuery(userDB.getDBDefine().getValidateQuery());
		
		if(!"".equals(PublicTadpoleDefine.CERT_USER_INFO)) {
			// initialize connection string
			List<String> listInitializeSql = new ArrayList<String>();
			String strFullHelloSQL = String.format(PublicTadpoleDefine.CERT_USER_INFO, userDB.getTdbLogingIP(), userDB.getTdbUserID()) + "\n " + userDB.getDBDefine().getValidateQuery();
			
			listInitializeSql.add(strFullHelloSQL);
			
			if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
				listInitializeSql.add(String.format("CALL DBMS_APPLICATION_INFO.SET_MODULE('Tadpole Hub-Transaction(%s)', '')", userDB.getTdbUserID()));
			}
			
			pcf.setConnectionInitSql(listInitializeSql);
		}
		
		// setting poolable connection factory
		DataSource ds = new PoolingDataSource(connectionPool);
		mapDataSource.put(searchKey, ds);
		
		return ds;
	}
	
	public DataSource makeDataSource(final String searchKey, final UserDBDAO userDB) {
		DataSource retDataSource = mapDataSource.get(searchKey);
		if(retDataSource == null) { 
			return makePool(searchKey, userDB);
		}
		
		return retDataSource;
	}
	
	public DataSource getDataSource(final String searchKey) {
		return mapDataSource.get(searchKey);
	}
	
	public void releaseConnectionPool(final String searchKey) {
//		GenericObjectPool connectionPool = mapGenericObject.remove(searchKey);
//		try {
//			if(connectionPool != null) {
//				connectionPool.clear();
//				connectionPool.close();
//			}
//		} catch(Exception e) {
//			logger.error(String.format("**** release connection key is %s", searchKey), e);
//		} finally {
//			connectionPool = null; 
//			mapDataSource.remove(searchKey);
//		}
		mapDataSource.remove(searchKey);
	}
}
