//package com.hangum.tadpole.engine.utils;
//
//import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
//import com.hangum.tadpole.preference.define.PreferenceDefine;
//import com.hangum.tadpole.session.manager.SessionManager;
//
//public class InnerGetPreferenceData {
//
//	/** rdb 쿼리 결과를 중에 NULL 값을 처리하는 기준 */
//	public static String getResultNull() {
//		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_NULL, ""+PreferenceDefine.RDB_RESULT_NULL_VALUE);
//		return userInfo.getValue0();
//	}
//
//}
