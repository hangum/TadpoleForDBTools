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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.transaction.DBCPConnectionManager;
import com.hangum.tadpole.engine.manager.transaction.TransactionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

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
	 * @param connectId
	 * @param userId
	 * @param userDB
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getInstance(final String connectId, final String userId, final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(!userDB.is_isUseEnable()) {
			throw new TadpoleSQLManagerException("You do not have DB database permissions.");
		}
		
		final String searchKey = getKey(connectId, userId, userDB);

		Connection _conn = null;;
		TransactionDAO transactionDAO = dbManager.get(searchKey);
		if (transactionDAO == null) {
			try {
				DataSource ds = null;
				// gate way 서버에 연결하려는 디비 정보가 있는지
				if(isGatewayConnection && userDB.getDBDefine() != DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT) {
					final UserDBDAO gatawayUserDB = (UserDBDAO)userDB.clone();
					TDBGatewayManager.makeGatewayServer(userId, gatawayUserDB, isGateWayIDCheck);
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
				_transactionDAO.setConnectId(connectId);
				_transactionDAO.setUserId(userId);
				_transactionDAO.setUserDB(userDB);
				_transactionDAO.setStartTransaction(new Timestamp(System.currentTimeMillis()));
				_transactionDAO.setKey(searchKey);
				
				dbManager.put(searchKey, _transactionDAO);
			} catch(CloneNotSupportedException cnse) {
				logger.error("clone not support exception");
				removeInstance(searchKey);
				
				new TadpoleSQLManagerException(cnse.getMessage());
			} catch (SQLException e) {
				logger.error("transaction connection", e);
				removeInstance(searchKey);
				
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
		if(userDB.getDBDefine() == DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.TADPOLE_SYSTEM_DEFAULT) return javaConn;
		
		String strSQL = "";
		if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) {
			strSQL = String.format("use `%s`", userDB.getSchema());
			
		} else if(userDB.getDBGroup() == DBGroupDefine.ORACLE_GROUP) {
			strSQL = String.format("ALTER SESSION SET CURRENT_SCHEMA = %s", userDB.getSchema());

		} else if(userDB.getDBGroup() == DBGroupDefine.POSTGRE_GROUP) {
			strSQL = String.format("set schema '%s'", userDB.getSchema());
			
		} else {
			strSQL = userDB.getDBDefine().getValidateQuery(false);
		}

		Statement statement = null;
		try {
			statement = javaConn.createStatement();
			statement.executeUpdate(strSQL);
		} catch(Exception e) {
			logger.error("Transaction Connection disconnected. and now connect of newone. user id is " + userId, e);
			
			removeInstance(searchKey);
			
//			Display display = PlatformUI.getWorkbench().getDisplay();
//			if(MessageDialog.openConfirm(display.getActiveShell(), "error", "디비연결시 오류가 발생했습니다.  기존 연결을 지우고 새롭게 연결하시겠습니까?")) {
//				removeInstance(userId, searchKey);
//				try {
//					javaConn = getInstance(userId, userDB);
//				} catch (Exception e1) {
//					logger.error("user connection disconnect" + e1);
//				}
//			} else {
//				throw new SQLException(e);
//			}
		} finally {
			try { if(statement != null) statement.close(); } catch(Exception e) {}
		}
		
		return javaConn;
	}
	
	/**
	 * transaction commit
	 * 
	 * @param connectId
	 * @param userId
	 * @param userDB
	 */
	public static void commit(final String connectId, final String userId, final UserDBDAO userDB) {

		final String searchKey = getKey(connectId, userId, userDB);
		if (logger.isDebugEnabled()) {
			logger.debug("=============================================================================");
			logger.debug("\t commit [key]\t[" + searchKey + "]");
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
				
				removeInstance(searchKey);
			}
		}
	}
	
	/**
	 * all connection rollback
	 * 
	 * @param userId
	 * @param userDB
	 */
	public static void rollbackAll(String userId, UserDBDAO userDB) {
		final List<String> listKeys = new ArrayList<String>(dbManager.keySet());
		for (String strKey : listKeys) {
			String strArryKey[] = StringUtils.splitByWholeSeparator(strKey, PublicTadpoleDefine.DELIMITER);
			
			if(StringUtils.equals(userId + PublicTadpoleDefine.DELIMITER + userDB.getSeq(), 
							 strArryKey[0] + PublicTadpoleDefine.DELIMITER + strArryKey[1])
			) {
				_rollback(strKey);
			}	// end if
		}
		
	}

	/**
	 * connection rollback
	 * 
	 * @param connectId
	 * @param userId
	 * @param userDB
	 */
	public static void rollback(final String connectId, final String userId, final UserDBDAO userDB) {
		final String searchKey = getKey(connectId, userId, userDB);
		_rollback(searchKey);
	}
	
	/**
	 * real rollback connection
	 * 
	 * @param searchKey
	 */
	private static void _rollback(String searchKey) {
		TransactionDAO transactionDAO = dbManager.get(searchKey);

		if (logger.isDebugEnabled()) {
			logger.debug("=============================================================================");
			logger.debug("\t rollback [searchKey]" + searchKey);
			logger.debug("=============================================================================");
		}

		if (transactionDAO != null) {
			Connection conn = transactionDAO.getConn();
			try {
				if (logger.isDebugEnabled()) logger.debug("\tIs auto commit " + conn.getAutoCommit());
				conn.rollback();
			} catch (Exception e) {
				logger.error("rollback exception", e);
			} finally {
				try { if(conn != null) conn.close(); } catch (Exception e) {}
				removeInstance(searchKey);
			}
		} else {
			logger.error("Not found DB keys " + searchKey);
		}
	}

	/**
	 * 사용자가 로그 아웃등으로 나갈때 transaction rollback합니다.
	 * 
	 * @param userId
	 */
	public static void executeAllRollback(final String userId) {
		
		final List<String> listKeys = new ArrayList<String>(dbManager.keySet());
		for (String searchKey : listKeys) {
			try {
				if (StringUtils.startsWith(searchKey, userId + PublicTadpoleDefine.DELIMITER)) {
					if (logger.isDebugEnabled()) logger.debug(String.format("== logout executeRollback start== [%s]", searchKey));
	
					TransactionDAO transactionDAO = (TransactionDAO)dbManager.get(searchKey);
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
					dbManager.remove(searchKey);
					transactionDAO =null;
					DBCPConnectionManager.getInstance().releaseConnectionPool(searchKey);
				}
			} catch(Exception e) {
				logger.error("********************** Release connection pool exception", e);
			}	// end try
		}	// end for
	}

	/**
	 * remove map instance
	 * 
	 * @param userDB
	 */
	private static void removeInstance(final String searchKey) {
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

	public static boolean isInstance(final String connectId, final String userId, final UserDBDAO userDB) {
		return getDbManager().containsKey(getKey(connectId, userId, userDB));
	}

	/**
	 * map의 카를 가져옵니다.
	 * 
	 * @param connectId
	 * @param userId
	 * @param userDB
	 * @return
	 */
	private static String getKey(final String connectId, final String userId, final UserDBDAO userDB) {
		return 	userId 						+ PublicTadpoleDefine.DELIMITER + 
				userDB.getSeq() 			+ PublicTadpoleDefine.DELIMITER +
				connectId 					+ PublicTadpoleDefine.DELIMITER; 
	}

}
