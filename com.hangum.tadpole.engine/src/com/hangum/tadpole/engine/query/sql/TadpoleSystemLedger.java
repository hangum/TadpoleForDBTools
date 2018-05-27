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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.ledger.DelegerHistoryDAO;
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
	 * 오라클 변경이력데이터 
	 * @param userDBDAO 
	 * 
	 * @param ledgerDAO
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<DelegerHistoryDAO> getOracleDetailListLedger(UserDBDAO userDBDAO, LedgerDAO ledgerDAO) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDBDAO);
		return sqlClient.queryForList("getOracleDetailListLedger", ledgerDAO);
	}
	
	/**
	 * 
	 * @param strResult
	 * @param strUser
	 * @param crNumber
	 * @param endTime 
	 * @param startTime 
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<LedgerDAO> getMySQLListLedger(String strResult, String strUser, String crNumber, long startTime, long endTime) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("strResult", strResult); 
		mapParam.put("strUser", "%" + strUser + "%");
		mapParam.put("crNumber", "%" + crNumber + "%");
		
		Date dateSt = new Date(startTime);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		mapParam.put("startTime",  formatter.format(dateSt));
		
		Date dateEd = new Date(endTime);
		mapParam.put("endTime", formatter.format(dateEd));			
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getMySQLListLedger", mapParam);
	}
	
	/**
	 * ledger 정보를 저장한다.
	 *  
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static LedgerDAO insertLedger(LedgerDAO ledger) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		java.lang.Integer intSeq = (java.lang.Integer)sqlClient.insert("insert_ledger", ledger);
		ledger.setSeq(intSeq);
		
		return ledger;
	}

	/**
	 * 원장 데이터 저장 상태를 기록한다.
	 * 
	 * @param ledgerDAO
	 */
	public static void insertLedgerResult(LedgerDAO ledgerDAO) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("update_ledger_result", ledgerDAO);
	}
	
	/**
	 * 참조 번호 중복 오류 검사
	 * 
	 * @param cp_seq
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static boolean isDuplicationRefenceNumber(String cp_seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List listKey = sqlClient.queryForList("isDuplicationRefenceNumber", cp_seq);
		
		if(listKey.size() == 0) return false;
		else return true;
	}
	
}
