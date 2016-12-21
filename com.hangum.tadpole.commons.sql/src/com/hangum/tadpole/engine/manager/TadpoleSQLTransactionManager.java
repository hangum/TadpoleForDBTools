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
package com.hangum.tadpole.engine.manager;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.transaction.DBCPConnectionManager;
import com.hangum.tadpole.engine.manager.transaction.TransactionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Tadpole SQL Transaction manager
 * 
 * @author hangum
 *
 */
public class TadpoleSQLTransactionManager {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSQLTransactionManager.class);
	private static Map<String, TransactionDAO> dbManager = null;
	private static TadpoleSQLTransactionManager transactionManager = null;

	private TadpoleSQLTransactionManager() {
	}

	static {
		if (transactionManager == null) {
			transactionManager = new TadpoleSQLTransactionManager();
			dbManager = new ConcurrentHashMap<String, TransactionDAO>();
		}
	}

	/**
	 * java.sql.connection을 생성하고 관리합니다.
	 * 
	 * @param userId
	 * @param userDB
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getInstance(final String userId, final UserDBDAO userDB) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[userId]" + userId + "[userDB]" + userDB.getUrl() + "/" + userDB.getUsers());
		}

		final String searchKey = getKey(userId, userDB);
		TransactionDAO transactionDAO = dbManager.get(searchKey);
		if (transactionDAO == null) {
//			synchronized(dbManager) {
//				transactionDAO = dbManager.get(searchKey);
//				if(transactionDAO != null) {
//					if(logger.isInfoEnabled()) logger.info("Return Transaction connection. [connection is]" + transactionDAO.getConn());
//					return transactionDAO.getConn();
//				}
				
				try {
					DataSource ds = DBCPConnectionManager.getInstance().makeDataSource(searchKey, userDB);
	
					TransactionDAO _transactionDAO = new TransactionDAO();
					Connection conn = ds.getConnection();
					conn.setAutoCommit(false);
	
					_transactionDAO.setConn(conn);
					_transactionDAO.setUserId(userId);
					_transactionDAO.setUserDB(userDB);
					_transactionDAO.setStartTransaction(new Timestamp(System.currentTimeMillis()));
	
					_transactionDAO.setKey(searchKey);
	
					dbManager.put(searchKey, _transactionDAO);
					if (logger.isDebugEnabled()) logger.debug("\t New connection SQLMapSession......");
					
					return _transactionDAO.getConn();
				} catch (Exception e) {
					logger.error("transaction connection", e);
					removeInstance(userId, searchKey);
					
					throw e;
				}
//			}
//		} else {
//			if (logger.isDebugEnabled()) {
//				logger.debug("\t Already register SQLMapSession.\t Is auto commit connection information " + transactionDAO.getConn());
//			}
		}
//		if (logger.isDebugEnabled()) logger.debug("[conn code]" + transactionDAO.getConn());

		return transactionDAO.getConn();
	}
	
	/**
	 * transaction commit
	 * 
	 * @param userId
	 * @param userDB
	 */
	public static void commit(final String userId, final UserDBDAO userDB) {

		final String searchKey = getKey(userId, userDB);
		if (logger.isDebugEnabled()) {
			logger.debug("=============================================================================");
			logger.debug("\t rollback [userId]" + searchKey);
			logger.debug("=============================================================================");
		}

		TransactionDAO transactionDAO = dbManager.get(searchKey);
		if (transactionDAO != null) {
			Connection conn = transactionDAO.getConn();
			try {
				conn.commit();
			} catch (Exception e) {
				logger.error("commit exception", e);
			} finally {
				try { if(conn != null) conn.close();} catch (Exception e) {}
				
				removeInstance(userId, searchKey);
			}
		}
	}

	/**
	 * connection rollback
	 * 
	 * @param userId
	 * @param userDB
	 */
	public static void rollback(final String userId, final UserDBDAO userDB) {

		final String searchKey = getKey(userId, userDB);
		if (logger.isDebugEnabled()) {
			logger.debug("=============================================================================");
			logger.debug("\t rollback [userId]" + searchKey);
			logger.debug("=============================================================================");
		}
		TransactionDAO transactionDAO = dbManager.get(searchKey);

		if (transactionDAO != null) {
			Connection conn = transactionDAO.getConn();
			try {
				if (logger.isDebugEnabled()) logger.debug("\tIs auto commit " + conn.getAutoCommit());
				conn.rollback();
			} catch (Exception e) {
				logger.error("rollback exception", e);
			} finally {
				try { if(conn != null) conn.close(); } catch (Exception e) {}
				removeInstance(userId, searchKey);
			}
		}
	}

	/**
	 * 사용자가 로그 아웃등으로 나갈때 transaction rollback합니다.
	 * 
	 * @param userId
	 */
	public static void executeRollback(final String userId) {
		Set<String> keys = dbManager.keySet();
		for (String searchKey : keys) {
			if (StringUtils.startsWith(searchKey, userId + PublicTadpoleDefine.DELIMITER)) {
				if (logger.isDebugEnabled()) logger.debug(String.format("== logout executeRollback start== [%s]", searchKey));

				TransactionDAO transactionDAO = dbManager.get(searchKey);
				if (transactionDAO != null) {
					Connection conn = transactionDAO.getConn();
					try {
						conn.rollback();
					} catch (Exception e) {
						logger.error("logout transaction commit", e);
					} finally {
						try { if (conn != null) conn.close(); } catch (Exception e) {}
						removeInstance(userId, searchKey);
					}
					
				} // end trsansaction dao
			} //
		}
	}

	/**
	 * remove map instance
	 * 
	 * @param userId
	 * @param userDB
	 */
	private static void removeInstance(final String userId, final String searchKey) {
		if (logger.isDebugEnabled()) logger.debug("\t #### [TadpoleSQLTransactionManager] remove Instance: " + searchKey);

		try {
			dbManager.remove(searchKey);

			DBCPConnectionManager.getInstance().releaseConnectionPool(searchKey);
		} catch (Exception e) {
			logger.error("remove connection", e);
		}
	}

	public static Map<String, TransactionDAO> getDbManager() {
		return dbManager;
	}

	public static boolean isInstance(final String userId, final UserDBDAO userDB) {
		return getDbManager().containsKey(getKey(userId, userDB));
	}

	/**
	 * map의 카를 가져옵니다.
	 * 
	 * @param userDB
	 * @return
	 */
	private static String getKey(final String userId, final UserDBDAO userDB) {
		return userId 						+ PublicTadpoleDefine.DELIMITER + 
				userDB.getDisplay_name() 	+ PublicTadpoleDefine.DELIMITER + 
				userDB.getDbms_type() 		+ PublicTadpoleDefine.DELIMITER + 
				userDB.getSeq() 			+ PublicTadpoleDefine.DELIMITER + 
				userDB.getUsers() 			+ PublicTadpoleDefine.DELIMITER;
	}
}
