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
		sqlClient.update("userInfoDataUpdate", new UserInfoDataDAO(SessionManager.getUserSeq(), key, CipherManager.getInstance().encryption(value))); //$NON-NLS-1$
		
		// session 에도 암호화 되게 저장합니다. 
		SessionManager.setUserInfo(key, CipherManager.getInstance().encryption(value));
	}
	
	/**
	 * update admin value
	 * @param key
	 * @param value
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static UserInfoDataDAO updateAdminValue(String key, String value)  throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO(PublicTadpoleDefine.systemAdminId, key, value);
		int updateCnt = sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		return userInfoData;
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
		sqlClient.update("userInfoDataUpdate", new UserInfoDataDAO(SessionManager.getUserSeq(), key, value)); //$NON-NLS-1$
		
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
	 * @param txtResultType 
	 */
	public static void updateRDBUserInfoData(String limitSelect, String resultSelect, String queryTimeout, String oraclePlan, 
			String txtRDBNumberColumnIsComman, String txtFontInfo, String txtCommitCount, String txtShownInTheColumn, String txtResultType) throws TadpoleSQLManagerException, SQLException {
		
		updateUserInfoData(PreferenceDefine.SELECT_LIMIT_COUNT, limitSelect);
		updateUserInfoData(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, resultSelect);
		updateUserInfoData(PreferenceDefine.SELECT_QUERY_TIMEOUT, queryTimeout);
		updateUserInfoData(PreferenceDefine.ORACLE_PLAN_TABLE, oraclePlan);
		updateUserInfoData(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA, txtRDBNumberColumnIsComman);
		updateUserInfoData(PreferenceDefine.RDB_RESULT_FONT, txtFontInfo);
		updateUserInfoData(PreferenceDefine.RDB_COMMIT_COUNT, txtCommitCount);
		updateUserInfoData(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, txtShownInTheColumn);
		updateUserInfoData(PreferenceDefine.RDB_RESULT_TYPE, txtResultType);
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
		updateUserInfoData(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE, tabSize);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE, sqlFormatDecode);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE, sqlFormatIn);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE, tabSize);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE, txtNewLineBefeoreAndOr);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE, txtRemoveEmptyLine);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE, txtWordbreak);
		updateUserInfoData(PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE, strTextWidth);
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
		updateUserInfoData(PreferenceDefine.MONGO_DEFAULT_LIMIT, txtLimitCount);
		updateUserInfoData(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT, txtMacCount);
		updateUserInfoData(PreferenceDefine.MONGO_DEFAULT_FIND, txtFindPage);
		updateUserInfoData(PreferenceDefine.MONGO_DEFAULT_RESULT, txtResultPage);
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
		sqlClient.update("userInfoDataUpdate", new UserInfoDataDAO(SessionManager.getUserSeq(), key, value0));
	}
	
	/**
	 * 사용자의 프로필.
	 * 
	 */
	public static void initializeUserPreferenceData(UserDAO userdb) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.startTransaction();
		sqlClient.startBatch();
		
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SECURITY_CREDENTIAL_USE, PreferenceDefine.SECURITY_CREDENTIAL_USE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SECURITY_CREDENTIAL_ACCESS_KEY, Utils.getUniqueID()));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SECURITY_CREDENTIAL_SECRET_KEY, Utils.getUniqueID()));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SYNC_EIDOTR_STATS, PreferenceDefine.SYNC_EIDOTR_STATS_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.EXPORT_DILIMITER, PreferenceDefine.EXPORT_DILIMITER_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SESSION_DFEAULT_PREFERENCE, ""+PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.DEFAULT_HOME_PAGE, PreferenceDefine.DEFAULT_HOME_PAGE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.DEFAULT_HOME_PAGE_USE, PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SELECT_LIMIT_COUNT, ""+PreferenceDefine.SELECT_SELECT_LIMIT_COUNT_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, ""+PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SELECT_QUERY_TIMEOUT, 	""+PreferenceDefine.SELECT_QUERY_TIMEOUT_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.ORACLE_PLAN_TABLE, PreferenceDefine.ORACLE_PLAN_TABLE_VALUE));
		
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.RDB_RESULT_TYPE, PreferenceDefine.RDB_RESULT_TYPE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.RDB_RESULT_FONT, PreferenceDefine.RDB_RESULT_FONT_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA, PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.RDB_COMMIT_COUNT, PreferenceDefine.RDB_COMMIT_COUNT_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE));
		
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.MONGO_DEFAULT_LIMIT, PreferenceDefine.MONGO_DEFAULT_LIMIT_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.MONGO_DEFAULT_MAX_COUNT, PreferenceDefine.MONGO_DEFAULT_MAX_COUNT_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.MONGO_DEFAULT_FIND, PreferenceDefine.MONGO_DEFAULT_FIND_BASIC));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.MONGO_DEFAULT_RESULT, PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE));
		
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE, PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE, PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE, PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE, PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE, PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE, PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE, PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE_VALUE));
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE, PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE_VALUE));
		
		sqlClient.executeBatch();
		sqlClient.commitTransaction();
		sqlClient.endTransaction();
	}
	
	/**
	 * insertUserPreferenceData
	 * 
	 * @param userdb
	 * @param key
	 * @param value
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void insertUserInfoData(UserDAO userdb, String key, String value) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userInfoDataInsert", new UserInfoDataDAO(userdb.getSeq(), key, value)); //$NON-NLS-1$
	}

}