/*******************************************************************************
 * Copyright (c) 2014 hangum.
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
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * external browser query
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_ExternalBrowser {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_ExternalBrowser.class);
	
	/**
	 * list external browser 
	 * 
	 * @param userDBDao
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ExternalBrowserInfoDAO> getExternalBrowser(UserDBDAO userDBDao) throws TadpoleSQLManagerException, SQLException {		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getExternalBrowser", userDBDao.getSeq());
	}
}
