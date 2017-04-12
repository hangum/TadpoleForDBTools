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

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TadpoleSystemDAO;
import com.hangum.tadpole.engine.query.dao.system.ledger.LedgerDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tadpole System ledger query
 * 
 * @author hangum
 *
 */
public class TadpoleSystemLedger {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystemLedger.class);
	
	/**
	 * ledger 정보를 저장한다.
	 *  
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static LedgerDAO insertLedger(LedgerDAO ledger) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		java.lang.Integer intSeq = (java.lang.Integer)sqlClient.insert("insert_ledger", ledger);
		ledger.setSeq(intSeq);
		
		return ledger;
	}
	
	/**
	 * update system information
	 * 
	 * @param useType
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateSystemInformation(String useType) throws TadpoleSQLManagerException, SQLException {
		TadpoleSystemDAO dao = new TadpoleSystemDAO(SystemDefine.NAME, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.INFORMATION, useType);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("update_system", dao);
	}
	
}
