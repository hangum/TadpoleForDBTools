/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.gateway.ExtensionDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;

/**
 * 외부 확장시스템 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_ExtensionDB {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystem_ExtensionDB.class);
		
	/**
	 * get extension info
	 * 
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public static List<ExtensionDBDAO> getExtensionInfo(String searchKey) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("findExtensionDB", searchKey); //$NON-NLS-1$
	}
	
	/**
	 * 접근이 허락된 디비 리스트를 가져온다.
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public static List<ExtensionDBDAO> getUserDBs(String userId) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("findUserExtensionDB", userId); //$NON-NLS-1$
	}
	
	/**
	 * 접근이 허락된 디비 리스트를 가져온다.
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public static List<ExtensionDBDAO> getUserDBTerm(String userId) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("findUserExtensionDBTerm", userId); //$NON-NLS-1$
	}
	
	/**
	 * delete extension
	 * 
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void deleteExtensionDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.delete("deleteExtensionDB"); //$NON-NLS-1$	
	}
	
	/**
	 * insertExtension data 
	 * 
	 * @param listExtensionDB
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void insertExtensionDB(List<ExtensionDBDAO> listExtensionDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		Connection connection = sqlClient.getDataSource().getConnection();
		connection.setAutoCommit(false);
		SqlMapSession session = sqlClient.openSession(connection);
		
		if(logger.isDebugEnabled()) logger.debug(" deleted before gateway data");
		try {
			session.delete("deleteExtensionDB"); //$NON-NLS-1$
			for (ExtensionDBDAO extensionDBDAO : listExtensionDB) {
				session.insert("saveExtensionDB", extensionDBDAO); //$NON-NLS-1$	
			}
			
			connection.commit();
		} catch(Exception sqle) {
			logger.error("extension db list", sqle);
			connection.rollback();
			throw new SQLException(sqle.getMessage());
		} finally {
			try { if(session != null) session.close(); } catch(Exception e) {}
			try { if(connection != null) connection.close(); } catch(Exception e) {}
		}
	
	}

}
