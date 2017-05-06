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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.engine.define.DBDefine;
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
	
	private static boolean isGatewayConnection = false;
	private static boolean isGateWayIDCheck = false;

	private TadpoleSQLTransactionManager() {
	}

	static {
		if (transactionManager == null) {
			transactionManager = new TadpoleSQLTransactionManager();
			dbManager = new HashMap<String, TransactionDAO>();
			
			isGatewayConnection = LoadConfigFile.isEngineGateway();
			isGateWayIDCheck = LoadConfigFile.isGateWayIDCheck();
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
		
		if (logger.isDebugEnabled()) logger.debug("[userId]" + searchKey);

		Connection _conn = null;;
		TransactionDAO transactionDAO = dbManager.get(searchKey);
		if (transactionDAO == null) {
				
			try {
				DataSource ds = null;
				// gate way 서버에 연결하려는 디비 정보가 있는지
				if(isGatewayConnection && userDB.getDBDefine() != DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT) {
					final UserDBDAO gatawayUserDB = (UserDBDAO)userDB.clone();
					TDBGatewayManager.makeGatewayServer(gatawayUserDB, isGateWayIDCheck);
					ds = DBCPConnectionManager.getInstance().makeDataSource(searchKey, gatawayUserDB);
				} else {
					ds = DBCPConnectionManager.getInstance().makeDataSource(searchKey, userDB);
				}
				
				_conn = ds.getConnection();
				_conn.setAutoCommit(false);
				
				// 캐릭터 셋을 맞추어준다.
				setConnectionInitialize(userDB, _conn);

				final TransactionDAO _transactionDAO = new TransactionDAO();
				_transactionDAO.setConn(_conn);
				_transactionDAO.setUserId(userId);
				_transactionDAO.setUserDB(userDB);
				_transactionDAO.setStartTransaction(new Timestamp(System.currentTimeMillis()));
				_transactionDAO.setKey(searchKey);
				
				dbManager.put(searchKey, _transactionDAO);
				
			} catch (Exception e) {
				logger.error("transaction connection", e);
				removeInstance(userId, searchKey);
				
				throw e;
			}
		} else {
			_conn = transactionDAO.getConn();
		}
		
		// 변경시 마다 커넥션을 수정한다.
		return changeSchema(userId, searchKey, userDB, _conn);
	}
	
	/**
	 * 사용자 커넥션을 얻는다.
	 * 
	 * @param searchKey
	 * @param userDB
	 * @param javaConn
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	private static Connection changeSchema(final String userId, final String searchKey, final UserDBDAO userDB, Connection javaConn) throws TadpoleSQLManagerException, SQLException {
		
		String strSQL = "";
		if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) {
			strSQL = String.format("use `%s`", userDB.getSchema());
		} else {
			strSQL = userDB.getDBDefine().getValidateQuery(false);
		}

		Statement statement = null;
		try {
			statement = javaConn.createStatement();
			statement.executeUpdate(strSQL);
		} catch(Exception e) {
			logger.error("Transaction Connection disconnected. and now connect of newone. user id is " + userId);
			
//			Display display = PlatformUI.getWorkbench().getDisplay();
//			if(MessageDialog.openConfirm(display.getActiveShell(), "error", "디비연결시 오류가 발생했습니다.  기존 연결을 지우고 새롭게 연결하시겠습니까?")) {
				removeInstance(userId, searchKey);
				try {
					javaConn = getInstance(userId, userDB);
				} catch (Exception e1) {
					logger.error("user connection disconnect" + e1);
				}
//			} else {
//				throw new SQLException(e);
//			}
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
	public static void executeAllRollback(final String userId) {
		Iterator iteratorEntrySet = dbManager.entrySet().iterator();
		while(iteratorEntrySet.hasNext()) {
			Map.Entry mapObject = (Map.Entry)iteratorEntrySet.next();
			String searchKey = (String)mapObject.getKey();
			
			try {
				if (StringUtils.startsWith(searchKey, userId + PublicTadpoleDefine.DELIMITER)) {
					if (logger.isDebugEnabled()) logger.debug(String.format("== logout executeRollback start== [%s]", searchKey));
	
					TransactionDAO transactionDAO = (TransactionDAO)mapObject.getValue();
					if (transactionDAO != null) {
						Connection conn = transactionDAO.getConn();
						
						try {
							conn.rollback();
						} catch (Exception e) {
							logger.error("logout transaction commit", e);
						} finally {
							try { if (conn != null) conn.close(); } catch (Exception e) {}
						}
					} // end trsansaction dao

					// 기존 object와 커넥션 풀을 삭제한다.
					iteratorEntrySet.remove();
					transactionDAO =null;
					DBCPConnectionManager.getInstance().releaseConnectionPool(searchKey);
				}
			} catch(Exception e) {
				logger.error("********************** Release connection pool exception", e);
			}
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
			TransactionDAO transactionDAO = dbManager.remove(searchKey);
			transactionDAO =null;

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
