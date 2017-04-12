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

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.gateway.ExtensionDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("findExtensionDB", searchKey); //$NON-NLS-1$
	}
	
	/**
	 * delete extension
	 * 
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void deleteExtensionDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("deleteExtensionDB"); //$NON-NLS-1$	
	}
	
	/**
	 * insertExtension data 
	 * 
	 * @param listExtensionDB
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void insertExtensionDB(List<ExtensionDBDAO> listExtensionDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());

//		sqlClient.startTransaction();
		sqlClient.startBatch();
		for (ExtensionDBDAO extensionDBDAO : listExtensionDB) {
			sqlClient.insert("saveExtensionDB", extensionDBDAO); //$NON-NLS-1$	
		}
		sqlClient.executeBatch();
//		sqlClient.commitTransaction();

	}

	public static List<ExtensionDBDAO> getUserDBs(String userId) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("findUserExtensionDB", userId); //$NON-NLS-1$
	}
}
