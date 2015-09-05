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
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 올챙이 시스템의 사용자 데이터를 정의 합니다.
 * 프리퍼런스 데이터를 저장합니다.
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
	 * Return to user credential
	 * 
	 * @param strAccessKey
	 * @param strSecretKey
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserInfoDataDAO> getUserCredential(String strAccessKey, String strSecretKey) throws TadpoleSQLManagerException, SQLException {		
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("SECURITY_CREDENTIAL_ACCESS_KEY", strAccessKey);
		mapParam.put("SECURITY_CREDENTIAL_SECRET_KEY", strSecretKey);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getUserInfoDataCredential", mapParam); //$NON-NLS-1$
	}

	/**
	 * 사용자 정보 데이터. 
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserInfoDataDAO> getUserInfoData() throws TadpoleSQLManagerException, SQLException {
		return getUserInfoData(SessionManager.getUserSeq());
	}
	/**
	 * 사용자 정보 데이터. 
	 * 
	 * @param userSeq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserInfoDataDAO> getUserInfoData(int userSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getUserInfoData", userSeq); //$NON-NLS-1$
	}
	
	/**
	 * user info data insert
	 * 
	 * @param listUserData
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void insertUserInfoData(List<UserInfoDataDAO> listUserData) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userInfoDataInsert", listUserData); //$NON-NLS-1$
	}
	
	/**
	 * user info data insert
	 * 
	 * @param listUserData
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void insertUserInfoData(UserInfoDataDAO listUserData) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userInfoDataInsert", listUserData); //$NON-NLS-1$
	}

	/**
	 * update encript  value
	 * 
	 * @param key
	 * @param value
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateEncriptValue(String key, String value) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getUserSeq());
		
		userInfoData.setName(key);
		userInfoData.setValue0(CipherManager.getInstance().encryption(value));
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// session 에도 암호화 되게 저장합니다. 
		SessionManager.setUserInfo(key, CipherManager.getInstance().encryption(value));
	}


	/**
	 * update key, value
	 * 
	 * @param key
	 * @param value
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateValue(String key, String value) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getUserSeq());
		
		userInfoData.setName(key);
		userInfoData.setValue0(value);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		SessionManager.setUserInfo(key, value);
	}
	

	/**
	 * 신규 사용자의 기본 유저 데이터 정보를 저장합니다.
	 * 
	 * @param limitSelect
	 * @param resultSelect
	 * @param queryTimeout
	 * @param oraclePlan
	 * @param txtRDBNumberColumnIsComman RDB의 결과테이블이 숫자 컬럼인 경우 ,를 넣을 것인지?
	 * @param txtFontInfo font information
	 * @param txtCommitCount commit count
	 */
	public static void updateRDBUserInfoData(String limitSelect, String resultSelect, String queryTimeout, String oraclePlan, 
			String txtRDBNumberColumnIsComman, String txtFontInfo, String txtCommitCount, String txtShownInTheColumn) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getUserSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SELECT_LIMIT_COUNT);
		userInfoData.setValue0(limitSelect);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// 검색 결과 페이지 당 보여주는 갯수 
		userInfoData.setName(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		userInfoData.setValue0(resultSelect);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// query time out
		userInfoData.setName(PreferenceDefine.SELECT_QUERY_TIMEOUT);
		userInfoData.setValue0(queryTimeout);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// ORACLE PLAN TABLE 
		userInfoData.setName(PreferenceDefine.ORACLE_PLAN_TABLE);
		userInfoData.setValue0(oraclePlan);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// RDB Result set number column.
		userInfoData.setName(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA);
		userInfoData.setValue0(txtRDBNumberColumnIsComman);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.RDB_RESULT_FONT);
		userInfoData.setValue0(txtFontInfo);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.RDB_COMMIT_COUNT);
		userInfoData.setValue0(txtCommitCount);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN);
		userInfoData.setValue0(txtShownInTheColumn);
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
	public static void updateSQLFormatterInfoData(String tabSize, String sqlFormatDecode, String sqlFormatIn,
			String txtNewLineBefeoreAndOr, String txtNewLineBefeoreComma, String  txtRemoveEmptyLine,
			String txtWordbreak, String strTextWidth
			
			) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB()); 
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getUserSeq());
		
		userInfoData.setName(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE);
		userInfoData.setValue0(tabSize);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE);
		userInfoData.setValue0(sqlFormatDecode);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE);
		userInfoData.setValue0(sqlFormatIn);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE);
		userInfoData.setValue0(txtNewLineBefeoreAndOr);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE);
		userInfoData.setValue0(txtNewLineBefeoreComma);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE);
		userInfoData.setValue0(txtRemoveEmptyLine);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE);
		userInfoData.setValue0(txtWordbreak);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		userInfoData.setName(PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE);
		userInfoData.setValue0(strTextWidth);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * mongodb update
	 * 
	 * @param txtLimitCount
	 * @param txtMacCount
	 * @param txtFindPage
	 * @param txtResultPage
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateMongoDBUserInfoData(String txtLimitCount, String txtMacCount, String txtFindPage, String txtResultPage) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getUserSeq());
		
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
	 * Update User info data
	 * 
	 * @param key
	 * @param value0
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserInfoData(String key, String value0) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getUserSeq());
		
		userInfoData.setName(key);
		userInfoData.setValue0(value0);
		sqlClient.update("userInfoDataUpdate", userInfoData);
	}
	
	
	/**
	 * 사용자의 프로필.
	 * 
	 */
	public static void initializeUserPreferenceData(UserDAO userdb) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(userdb.getSeq());
		/*user_db 테이블의 seq가 들어가야함.*/
		/* 사용자별 dbms의 종류에 따른 환경설정 정보 - 초기등록시 작업이 아니라 커넥션 최초 등록시 작업하는게 맞음. */
		/* dbms종속적인 환경설정도 있을 수 있으므로.. */
		//userInfoData.setDb_seq(1);
		
		// Security Credential Preference 
				userInfoData.setName(PreferenceDefine.SECURITY_CREDENTIAL_USE);
				userInfoData.setValue0(PublicTadpoleDefine.YES_NO.YES.name());
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				// set SECURITY_CREDENTIAL_ACCESS_KEY
				userInfoData.setName(PreferenceDefine.SECURITY_CREDENTIAL_ACCESS_KEY);
				userInfoData.setValue0(Utils.getUniqueID());
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				// set SECURITY_CREDENTIAL_SECRET_KEY 
				userInfoData.setName(PreferenceDefine.SECURITY_CREDENTIAL_SECRET_KEY);
				userInfoData.setValue0(Utils.getUniqueID());
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// editor and connection sysn status
		userInfoData.setName(PreferenceDefine.SYNC_EIDOTR_STATS);
		userInfoData.setValue0(""+PreferenceDefine.SYNC_EIDOTR_STATS_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
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
		
		// query time out
		userInfoData.setName(PreferenceDefine.SELECT_QUERY_TIMEOUT);
		userInfoData.setValue0(""+PreferenceDefine.SELECT_QUERY_TIMEOUT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// ORACLE PLAN TABLE 
		userInfoData.setName(PreferenceDefine.ORACLE_PLAN_TABLE);
		userInfoData.setValue0(PreferenceDefine.ORACLE_PLAN_TABLE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// RDB 결과 테이블의 폰트 설정.
		userInfoData.setName(PreferenceDefine.RDB_RESULT_FONT);
		userInfoData.setValue0(PreferenceDefine.RDB_RESULT_FONT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// RDB 결과가 숫자 컬럼이면 ,를 찍도록 합니다.
		userInfoData.setName(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA);
		userInfoData.setValue0(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// 에디터에서 커밋 카운트.
		userInfoData.setName(PreferenceDefine.RDB_COMMIT_COUNT);
		userInfoData.setValue0(PreferenceDefine.RDB_COMMIT_COUNT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// 에디터에서 결과 화면에서 컬럼의 보여지는 캐릭터 숫자.
		userInfoData.setName(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN);
		userInfoData.setValue0(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE);
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
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
				userInfoData.setName(PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE);
				userInfoData.setValue0(PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE_VALUE);
				sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
				
	}
	
	/**
	 * 사용자 정보를 저장합니다.
	 * 
	 * @param userdb
	 * @param key
	 * @param value
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	private  void insertUserInfoData(UserDBDAO userdb, String key, String value) throws TadpoleSQLManagerException, SQLException {
		UserInfoDataDAO userInfoData = new UserInfoDataDAO(userdb.getSeq(), key, value);
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
	}
	
}