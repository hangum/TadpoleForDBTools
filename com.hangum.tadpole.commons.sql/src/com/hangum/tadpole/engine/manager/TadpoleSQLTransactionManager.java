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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.transaction.DBCPConnectionManager;
import com.hangum.tadpole.engine.manager.transaction.TransactionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Tadpole SQL Transaction manager
 * 
 * @author hangum
 *
 */
public class TadpoleSQLTransactionManager extends AbstractTadpoleManager {
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
			dbManager = new HashMap<String, TransactionDAO>();
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
		final String searchKey = getKey(userId, userDB);
		
		if (logger.isInfoEnabled()) {
			logger.info("[userId]" + searchKey);
		}

		Connection _conn = null;;
		TransactionDAO transactionDAO = dbManager.get(searchKey);
		if (transactionDAO == null) {
				
			try {
				DataSource ds = DBCPConnectionManager.getInstance().makeDataSource(searchKey, userDB);
				_conn = ds.getConnection();
				_conn.setAutoCommit(false);
				
				final TransactionDAO _transactionDAO = new TransactionDAO();
				_transactionDAO.setConn(_conn);
				_transactionDAO.setUserId(userId);
				_transactionDAO.setUserDB(userDB);
				_transactionDAO.setStartTransaction(new Timestamp(System.currentTimeMillis()));
				_transactionDAO.setKey(searchKey);
				
				if (logger.isInfoEnabled()) {
					logger.info("\t New connection strt......");

					PreparedStatement ps = null;
					ResultSet rs = null;
					try {
						ps = _conn.prepareStatement(userDB.getDBDefine().getValidateQuery());
						rs = ps.executeQuery();
						logger.info("\t result => " + rs.getRow());
					} catch(Exception e) {
						logger.error("test connection query", e);
					} finally {
						if(rs != null) rs.close();
						if(ps != null) ps.close();
					}
					logger.info("\t New connection end......");
				}
				
				dbManager.put(searchKey, _transactionDAO);
				
			} catch (Exception e) {
				logger.error("transaction connection", e);
				removeInstance(userId, searchKey);
				
				throw e;
			}
		} else {
			_conn = transactionDAO.getConn();
			
			if (logger.isInfoEnabled()) {
				logger.info("\t Already connection start......");
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = _conn.prepareStatement(userDB.getDBDefine().getValidateQuery());
					rs = ps.executeQuery();
					logger.info("\t result => " + rs.getRow());
				} catch(Exception e) {
					logger.error("test connection query", e);
				} finally {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				}
				logger.info("\t Already connection end......");
			}
		}
		
		// 변경시 마다 커넥션을 수정한다.
		return changeScheema(userDB, _conn);
	}
	
	/**
	 * 사용자 커넥션을 얻는다.
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	private static Connection changeScheema(final UserDBDAO userDB, final Connection javaConn) throws TadpoleSQLManagerException, SQLException {

		Statement statement = null;
		try {
			if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) {
				statement = javaConn.createStatement();
				statement.executeUpdate("use " + userDB.getSchema());
			}
		} catch(Exception e) {
			logger.error("change scheman ", e);
			throw new SQLException(e);
		} finally {
			if(statement != null) statement.close();
		}
		
		return javaConn;
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
