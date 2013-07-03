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
package com.hangum.tadpole.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.SQLUtil;
import com.hangum.tadpole.dao.system.ExecutedSqlResourceDAO;
import com.hangum.tadpole.dao.system.ExecutedSqlResourceDataDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dialogs.message.dao.SQLHistoryDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * executed_sql_resource관련 테이블 쿼리.
 * 해당 테이블은 사용자 sql 실행쿼리에 관한 정보를 가지고 있습니다.
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_ExecutedSQL {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystem_ExecutedSQL.class);
	
	/**
	 * 마지막 실행했떤 쿼리 100개를 리턴합니다.
	 * 
	 * @param user_seq
	 * @param dbSeq
	 * @return
	 * @throws Exception
	 */
	public static List<SQLHistoryDAO> getExecuteQueryHistory(int user_seq, int dbSeq, String filter) throws Exception {
		List<SQLHistoryDAO> returnSQLHistory = new ArrayList<SQLHistoryDAO>();
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq",user_seq);
		queryMap.put("db_seq", 	dbSeq);
		queryMap.put("filter", "%" + filter + "%");
		queryMap.put("count", 	100);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<java.util.Map> listResourceData =  sqlClient.queryForList("getExecuteQueryHistory", queryMap);
		
		for (Map resultMap : listResourceData) {
			
			boolean bool  = resultMap.containsKey("EXECUTED_SQL_RESOURCE_SEQ");
			
			int seq = (Integer)resultMap.get("EXECUTED_SQL_RESOURCE_SEQ");
			
			Long startdateexecute = (Long)resultMap.get("STARTDATEEXECUTE");
			String strSQLText = (String)resultMap.get("DATAS");
			Long enddateexecute = (Long)resultMap.get("ENDDATEEXECUTE");
			
			int row = (Integer)resultMap.get("ROW");
			String result = (String)resultMap.get("RESULT");
			
			
			SQLHistoryDAO dao = new SQLHistoryDAO(new Date(startdateexecute), strSQLText, new Date(enddateexecute), row, result, "");
			dao.setSeq(seq);
			returnSQLHistory.add(dao);
		}
		
		return returnSQLHistory;
	}
	
	/**
	 * find execute sql
	 * 
	 * @return
	 */
	public static List<SQLHistoryDAO> findExecuteSQL(String operationType, String groupName, String displayName, Date executeTime, int duration) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		
		return null;
	}

	/**
	 * save resource 
	 * @param listExecutingSqltHistoryDao
	 */
	public static void saveExecuteSQUeryResource(int user_seq, UserDBDAO userDB, PublicTadpoleDefine.EXECUTE_SQL_TYPE sqlType, List<SQLHistoryDAO> listExecutingSqltHistoryDao) throws Exception {

		for (SQLHistoryDAO sqlHistoryDAO : listExecutingSqltHistoryDao) {
			ExecutedSqlResourceDAO executeSQLResourceDao = new ExecutedSqlResourceDAO();
			executeSQLResourceDao.setDb_seq(userDB.getSeq());
			executeSQLResourceDao.setUser_seq(user_seq);
			executeSQLResourceDao.setTypes(sqlType.toString());
			
			executeSQLResourceDao.setStartDateExecute(sqlHistoryDAO.getStartDateExecute());
			executeSQLResourceDao.setEndDateExecute(sqlHistoryDAO.getEndDateExecute());
			executeSQLResourceDao.setRow(sqlHistoryDAO.getRows());
			executeSQLResourceDao.setResult(sqlHistoryDAO.getResult());
			executeSQLResourceDao.setMessage(""+sqlHistoryDAO.getRows());
			
			// 기존에 등록 되어 있는지 검사한다
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			ExecutedSqlResourceDAO executeSQL =  (ExecutedSqlResourceDAO)sqlClient.insert("userExecuteSQLResourceInsert", executeSQLResourceDao); //$NON-NLS-1$
			logger.debug("[return seq] " + executeSQL.toString());
			
			insertResourceData(executeSQL, sqlHistoryDAO.getStrSQLText());
		}
				
	}
	
	/**
	 * resource data 
	 * 
	 * @param userDBResource
	 * @param contents
	 * @throws Exception
	 */
	private static void insertResourceData(ExecutedSqlResourceDAO userDBResource, String contents) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		// content data를 저장합니다.
		ExecutedSqlResourceDataDAO dataDao = new ExecutedSqlResourceDataDAO();
		dataDao.setExecuted_sql_resource_seq(userDBResource.getSeq());
		String[] arrayContent = SQLUtil.makeResourceDataArays(contents);
		for (String content : arrayContent) {
			dataDao.setDatas(content);		
			sqlClient.insert("userExecuteSQLResourceDataInsert", dataDao); //$NON-NLS-1$				
		}
	}
	
}
