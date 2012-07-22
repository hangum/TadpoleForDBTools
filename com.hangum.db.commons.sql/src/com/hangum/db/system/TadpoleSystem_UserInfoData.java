package com.hangum.db.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.system.UserDAO;
import com.hangum.db.dao.system.UserInfoDataDAO;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.tadpole.preference.define.PreferenceDefine;
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return sqlClient.queryForObject("getUserInfoData", SessionManager.getSeq()); //$NON-NLS-1$
	}
	
	/**
	 * user info data insert
	 * 
	 * @param listUserData
	 * @throws Exception
	 */
	public static void insertUserInfoData(List<UserInfoDataDAO> listUserData) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.insert("userInfoDataInsert", listUserData); //$NON-NLS-1$
	}

	/**
	 * general 정보를 저장합니다.
	 * 
	 * @param sessionTimeOut
	 * @throws Exception
	 */
	public static void updateGeneralUserInfoData(String sessionTimeOut) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		userInfoData.setValue(sessionTimeOut);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
	}
	
	/**
	 * 신규 사용자의 기본 유저 데이터 정보를 저장합니다.
	 * 
	 * @param userdb
	 * @param limitSelect
	 * @param resultSelect
	 * @param oraclePlan
	 */
	public static void updateRDBUserInfoData(String limitSelect, String resultSelect, String oraclePlan) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SELECT_LIMIT_COUNT);
		userInfoData.setValue(limitSelect);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// 검색 결과 페이지 당 보여주는 갯수 
		userInfoData.setName(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		userInfoData.setValue(resultSelect);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// ORACLE PLAN TABLE 
		userInfoData.setName(PreferenceDefine.ORACLE_PLAN_TABLE);
		userInfoData.setValue(oraclePlan);
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(SessionManager.getSeq());
		
		// 	MONGO_DEFAULT_LIMIT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_LIMIT);
		userInfoData.setValue(txtLimitCount);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_MAX_COUNT 
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
		userInfoData.setValue(txtMacCount);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_FIND
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_FIND);
		userInfoData.setValue(txtFindPage);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_RESULT 
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_RESULT);
		userInfoData.setValue(txtResultPage);
		sqlClient.update("userInfoDataUpdate", userInfoData); //$NON-NLS-1$
		
	}
	
	/**
	 * 신규 사용자의 기본 유저 데이터 정보를 저장합니다.
	 * 
	 */
	public static void insertUserInfoData(UserDAO userdb) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		UserInfoDataDAO userInfoData = new UserInfoDataDAO();
		userInfoData.setUser_seq(userdb.getSeq());
		
		//SESSION TIME OUT 
		userInfoData.setName(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		userInfoData.setValue(""+PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// 	select 제한  갯수		
		userInfoData.setName(PreferenceDefine.SELECT_LIMIT_COUNT);
		userInfoData.setValue(""+PreferenceDefine.SELECT_SELECT_LIMIT_COUNT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// 검색 결과 페이지 당 보여주는 갯수 
		userInfoData.setName(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		userInfoData.setValue(""+PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// ORACLE PLAN TABLE 
		userInfoData.setName(PreferenceDefine.ORACLE_PLAN_TABLE);
		userInfoData.setValue(PreferenceDefine.ORACLE_PLAN_TABLE_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_LIMIT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_LIMIT);
		userInfoData.setValue(PreferenceDefine.MONGO_DEFAULT_LIMIT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_MAX_COUNT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
		userInfoData.setValue(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT_VALUE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$		
		
		// MONGO_DEFAULT_FIND
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_FIND);
		userInfoData.setValue(PreferenceDefine.MONGO_DEFAULT_FIND_BASIC);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
		// MONGO_DEFAULT_RESULT
		userInfoData.setName(PreferenceDefine.MONGO_DEFAULT_RESULT);
		userInfoData.setValue(PreferenceDefine.MONGO_DEFAULT_RESULT_TREE);
		sqlClient.insert("userInfoDataInsert", userInfoData); //$NON-NLS-1$
		
	}
	
}
 