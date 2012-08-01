package com.hangum.tadpole.preference.get;

import com.hangum.db.dao.system.UserInfoDataDAO;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.tadpole.preference.define.PreferenceDefine;

/**
 * preference의 일반적인 정보를 얻습니다.
 * 
 * @author hangum
 *
 */
public class GetPreferenceGeneral {
	////////////////// 일반 설정 ///////////////////////////////////////////////////////////////////////////
	/**
	 * session time out 
	 * @return
	 */
	public static String getSessionTimeout() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		return userInfo.getValue();
	}
	
	/**
	 *export dilimit
	 *
	 * @return
	 */
	public static String getExportDelimit() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EXPORT_DILIMITER);
		if(null == userInfo) return PreferenceDefine.EXPORT_DILIMITER_VALUE;
		else if("".equals(userInfo.getValue())) return PreferenceDefine.EXPORT_DILIMITER_VALUE;
		
		return userInfo.getValue();
	}
	
	////////////////// rdb 설정 ////////////////////////////////////////////////////////////////////////////
	/** rdb 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. */ 
	public static int getQueryResultCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT);
		return Integer.parseInt( userInfo.getValue() );		
	}
	
	/** rdb 쿼리 결과를 page당 처리 하는 카운트 */
	public static int getPageCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		return Integer.parseInt( userInfo.getValue() );
	}
	
	/** rdb 쿼리 결과를 page당 처리 하는 카운트 */
	public static String getPlanTableName() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE);
		return userInfo.getValue();
	}
	
	///////////////// mongodb 설정 ////////////////////////////////////////////////////////////////////////////
	/** preference mongodb default limit */
	public static String getMongoDefaultLimit() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_LIMIT);
		return userInfo.getValue();		
	}
	
	/** preference mongodb default max count */
	public static int getMongoDefaultMaxCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
		return Integer.parseInt( userInfo.getValue() );
	}
	
	/** preference mongodb default find page */
	public static String getMongoDefaultFindPage() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_FIND);
		return userInfo.getValue();
	}
	
	/** preference mongodb default result page */
	public static String getMongoDefaultResultPage() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_RESULT);
		return userInfo.getValue();
	}
}
