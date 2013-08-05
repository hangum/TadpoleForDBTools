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

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 올챙이 시스템의 사용자 데이터를 정의 합니다.
 * 프리퍼런스 데이터를 저장합니다.
 * 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserInfoData {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserInfoData.class);

	/**
	 * 모든 사용자 정보 데이터. 
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List allUserInfoData() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("allUserInfoData", SessionManager.getSeq()); //$NON-NLS-1$
	}
	
	/**
	 * 사용자 정보 데이터. 
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Object getUserInfoData(String key) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForObject("getUserInfoData", SessionManager.getSeq()); //$NON-NLS-1$
	}
	
	/**
	 * user info data insert
	 * 
	 * @param listUserData
	 * @throws Exception
	 */
	public static void insertUserInfoData(List<UserInfoDataDAO> listUserData) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userInfoDataInsert", listUserData); //$NON-NLS-1$
	}
	
	/**
	 * user info data insert
	 * 
	 * @param listUserData
	 * @throws Exception
	 */
	public static void insertUserInfoData(UserInfoDataDAO listUserData) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userInfoDataInsert", listUserData); //$NON-NLS-1$
	}

	/**
	 * general 정보의 session time을 저장합니다.
	 * 
	 * @param sessionTimeOut
	 * @throws Exception
	 */
	public static void updateGeneralUserInfoData(String sessionTimeOut) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		userInfoData.setValue0(sessionTimeOut);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * general 정보의 export delimit을 저장합니다.
	 * 
	 * @param sessionTimeOut
	 * @throws Exception
	 */
	public static void updateGeneralExportDelimitData(String delimit) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.EXPORT_DILIMITER);
		userInfoData.setValue0(delimit);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * default home page
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void updateDefaultHomePage(String url) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.DEFAULT_HOME_PAGE);
		userInfoData.setValue0(url);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * default home page
	 * 
	 * @param use
	 * @throws Exception
	 */
	public static void updateDefaultHomePageUse(String use) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.DEFAULT_HOME_PAGE_USE);
		userInfoData.setValue0(use);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * 신규 사용자의 기본 유저 데이터 정보를 저장합니다.
	 * 
	 * @param userdb
	 * @param limitSelect
	 * @param resultSelect
	 * @param oraclePlan
	 * @param txtRDBNumberColumnIsComman RDB의 결과테이블이 숫자 컬럼인 경우 ,를 넣을 것인지?
	 */
	public static void updateRDBUserInfoData(String limitSelect, String resultSelect, String oraclePlan, String txtRDBNumberColumnIsComman) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SELECT_LIMIT_COUNT);
		userInfoData.setValue0(limitSelect);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// 검색 결과 페이지 당 보여주는 갯수 
		userInfoData.setName(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		userInfoData.setValue0(resultSelect);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// ORACLE PLAN TABLE 
		userInfoData.setName(PreferenceDefine.ORACLE_PLAN_TABLE);
		userInfoData.setValue0(oraclePlan);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// RDB Result set number column.
		userInfoData.setName(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA);
		userInfoData.setValue0(txtRDBNumberColumnIsComman);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * update SQLFormatter
	 * 
	 * @param userdb
	 * @param tabSize
	 * @param resultSelect
	 * @param sqlFormatIn
	 */
	public static void updateSQLFormatterInfoData(String tabSize, String sqlFormatDecode, String sqlFormatIn) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB()); 
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		userInfoData.setName(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE);
		userInfoData.setValue0(tabSize);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE);
		userInfoData.setValue0(sqlFormatDecode);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE);
		userInfoData.setValue0(sqlFormatIn);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * mongodb update
	 * 
	 * @param txtLimitCount
	 * @param txtMacCount
	 * @param txtFindPage
	 * @param txtResultPage
	 * @throws Exception
	 */
	public static void updateMongoDBUserInfoData(String txtLimitCount, String txtMacCount, String txtFindPage, String txtResultPage) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	MONGO_DEFAULT_LIMIT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_LIMIT);
		userInfoData.setValue0(txtLimitCount);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_MAX_COUNT 
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
		userInfoData.setValue0(txtMacCount);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_FIND
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_FIND);
		userInfoData.setValue0(txtFindPage);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_RESULT 
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_RESULT);
		userInfoData.setValue0(txtResultPage);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
	}
	
	/**
	 * 신규 사용자의 기본 유저 데이터 정보를 저장합니다.
	 * 
	 */
	public static void insertUserInfoData(UserDAO userdb) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(userdb.getSeq());
		
		// export delimiter
		userInfoData.setName(PreferenceDefine.EXPORT_DILIMITER);
		userInfoData.setValue0(""+PreferenceDefine.EXPORT_DILIMITER_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		//SESSION TIME OUT 
		userInfoData.setName(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		userInfoData.setValue0(""+PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$

		//DEFAULT_HOME PAGE
		userInfoData.setName(PreferenceDefine.DEFAULT_HOME_PAGE);
		userInfoData.setValue0(""+PreferenceDefine.DEFAULT_HOME_PAGE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		//DEFAULT_HOME PAGE_USE
		userInfoData.setName(PreferenceDefine.DEFAULT_HOME_PAGE_USE);
		userInfoData.setValue0(""+PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SELECT_LIMIT_COUNT);
		userInfoData.setValue0(""+PreferenceDefine.SELECT_SELECT_LIMIT_COUNT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// 검색 결과 페이지 당 보여주는 갯수 
		userInfoData.setName(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		userInfoData.setValue0(""+PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// ORACLE PLAN TABLE 
		userInfoData.setName(PreferenceDefine.ORACLE_PLAN_TABLE);
		userInfoData.setValue0(PreferenceDefine.ORACLE_PLAN_TABLE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// RDB 결과가 숫자 컬럼이면 ,를 찍도록 합니다.
		userInfoData.setName(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA);
		userInfoData.setValue0(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_LIMIT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_LIMIT);
		userInfoData.setValue0(PreferenceDefine.MONGO_DEFAULT_LIMIT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_MAX_COUNT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
		userInfoData.setValue0(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$		
		
		// MONGO_DEFAULT_FIND
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_FIND);
		userInfoData.setValue0(PreferenceDefine.MONGO_DEFAULT_FIND_BASIC);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_RESULT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_RESULT);
		userInfoData.setValue0(PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		
		// SQLFormatter
				userInfoData.setName(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
	}
	
}
 
