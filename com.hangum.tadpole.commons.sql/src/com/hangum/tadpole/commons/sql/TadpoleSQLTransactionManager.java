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
package com.hangum.tadpole.commons.sql;

import java.sql.Connection;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.transaction.DBCPConnectionManager;
import com.hangum.tadpole.commons.sql.transaction.TransactionDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

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
	private static HashMap<String, TransactionDAO> dbManager = null;
	private static TadpoleSQLTransactionManager transactionManager = null;
	private TadpoleSQLTransactionManager() {}
	
	static {
		if(transactionManager == null) {
			transactionManager = new TadpoleSQLTransactionManager();
			dbManager = new HashMap<String, TransactionDAO>();
		} 
	}
	
	/**
	 * java.sql.connection을 생성하고 관리합니다.
	 * 
	 * @param userId
	 * @param userDB
	 * @param isAutoCommit
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getInstance(final String userId, final UserDBDAO userDB, final boolean isAutoCommit) throws Exception {
		
		logger.debug("[userId]" + userId + "[userDB]" + userId + "[isAutoCommit]: " + isAutoCommit);
		
		synchronized(dbManager) {
			final String searchKey = getKey(userId, userDB);
			TransactionDAO transactionDAO = dbManager.get(searchKey);
			if(transactionDAO == null) {
				try {
					DataSource ds = DBCPConnectionManager.getInstance().getDataSource(userDB);
					
					transactionDAO = new TransactionDAO();
					Connection conn = ds.getConnection();
					conn.setAutoCommit(false);
					
					transactionDAO.setConn(conn);
					dbManager.put(searchKey, transactionDAO);
					logger.debug("\t New connection SQLMapSession." );
				} catch(Exception e) {
					logger.error("transaction connection", e);
				}
			} else {
				logger.debug("\t Already register SQLMapSession." );
				logger.debug("\t Is auto commit " + transactionDAO.getConn().getAutoCommit());
			}

			logger.debug("[conn code]" + transactionDAO.toString());
			
			return transactionDAO.getConn();
		}
	}
	
	/**
	 * 
	 * 
	 * @param userId
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static Connection getInstance(final String userId, final UserDBDAO userDB) throws Exception {
		synchronized (dbManager) {
			return dbManager.get(getKey(userId, userDB)).getConn();
		}
	}

	/**
	 * transaction commit
	 * 
	 * @param userId
	 * @param userDB
	 */
	public static void commit(final String userId, final UserDBDAO userDB) {
		logger.debug("=============================================================================");
		logger.debug("\t commit [userId]" + userId );
		logger.debug("=============================================================================");
		
		synchronized (dbManager) {
			TransactionDAO transactionDAO = dbManager.get(getKey(userId, userDB));
			
			if(transactionDAO != null) {
				Connection conn = transactionDAO.getConn();
				try {
					logger.debug("\tIs auto commit " + conn.getAutoCommit());
					conn.commit();
				} catch(Exception e) {
					logger.error("commit exception", e);
				} finally {
					try { if(conn != null) conn.close(); } catch(Exception e) {}
				}
					
				removeInstance(userId, userDB);
			}
		} // end synchronized
	}

	/**
	 * connection rollback
	 * 
	 * @param userId
	 * @param userDB
	 */
	public static void rollback(final String userId, final UserDBDAO userDB) {
		logger.debug("=============================================================================");
		logger.debug("\t rollback [userId]" + userId );
		logger.debug("=============================================================================");
		
		synchronized (dbManager) {
			TransactionDAO transactionDAO = dbManager.get(getKey(userId, userDB));
			
			if(transactionDAO != null) {
				Connection conn = transactionDAO.getConn();
				try {
					logger.debug("\tIs auto commit " + conn.getAutoCommit());
					conn.rollback();
				} catch(Exception e) {
					logger.error("commit exception", e);
				} finally {
					try { if(conn != null) conn.close(); } catch(Exception e) {}
				}
					
				removeInstance(userId, userDB);
			}
		} // end synchronized
	}
	
	/**
	 * remove map instance
	 * 
	 * @param userId
	 * @param userDB
	 */
	private static void removeInstance(final String userId, final UserDBDAO userDB) {
		synchronized (dbManager) {
			Connection conn = dbManager.remove(getKey(userId, userDB)).getConn();
			try { if(conn != null) conn.close(); } catch(Exception e) {}
			conn = null;
		}
	}
	
	/**
	 * map의 카를 가져옵니다.
	 * @param userDB
	 * @return
	 */
	private static String getKey(final String userId, final UserDBDAO userDB) {
		return userId + userDB.getSeq() + userDB.getTypes()+userDB.getUrl()+userDB.getUsers();//+dbInfo.getPasswd();
	}
}
