/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.editors.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.preference.define.PreferenceDefine;

/**
 * 어드민 일반 유저의 설정 값을 일반 모든 유저에게 바꾸준다.
 * 
 * @author hangum
 *
 */
public class ChangeUserPreferenceValue {
	private static final Logger logger = Logger.getLogger(ChangeUserPreferenceValue.class);
	private static String strUpdate = "UPDATE user_info_data SET value0 = ? WHERE user_seq != -1 AND name = ?";;
	
	/**
	 * RDB 설정 값을 바꾸어준다.
	 */
	public void changePreferenceValue() {
		
		try {
			// 시스템 어드민을 찾는다.
			UserDAO userDao = TadpoleSystem_UserQuery.getSystemAdmin();
			
			// 시스템 어드민의 RDB value를 가져온다.
			List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData(userDao.getSeq());
			Map<String, String> mapUserInfo = new HashMap<>();
			for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {
				mapUserInfo.put(userInfoDataDAO.getName(), userInfoDataDAO.getValue0());
			}
			
			// change rdb value
			rdbChangeValue(mapUserInfo);
			
			// change mongo db
			mongoChangeValue(mapUserInfo);
			
			// change session time out
			userChangeValue(mapUserInfo);
			
			MessageDialog.openInformation(null, CommonMessages.get().Confirm, CommonMessages.get().ModifyMessage);
			
		} catch(Exception e) {
			logger.error("RDB Value update eception", e);
			MessageDialog.openError(null, CommonMessages.get().Error, "수정하는 중에 " + e.getMessage());
		}
		
	}
	
	/**
	 * 사용자 값 변경
	 * @param mapUserInfo
	 * @throws Exception
	 */
	private void userChangeValue(Map<String, String> mapUserInfo) throws Exception {
		List<Object> listParameter = new ArrayList<>();
		listParameter.add(mapUserInfo.get(PreferenceDefine.SESSION_DFEAULT_PREFERENCE));
		listParameter.add(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
	}
	
	/**
	 * change value
	 * 
	 * @param mapUserInfo
	 * @throws Exception
	 */
	private void mongoChangeValue(Map<String, String> mapUserInfo) throws Exception {
		List<Object> listParameter = new ArrayList<>();
		listParameter.add(mapUserInfo.get(PreferenceDefine.MONGO_DEFAULT_LIMIT));
		listParameter.add(PreferenceDefine.MONGO_DEFAULT_LIMIT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT));
		listParameter.add(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.MONGO_DEFAULT_FIND));
		listParameter.add(PreferenceDefine.MONGO_DEFAULT_FIND);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.MONGO_DEFAULT_RESULT));
		listParameter.add(PreferenceDefine.MONGO_DEFAULT_RESULT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
	}
	
	/**
	 * Change rdb value
	 * 
	 * @param mapUserInfo
	 * @throws Exception
	 */
	private void rdbChangeValue(Map<String, String> mapUserInfo) throws Exception {
		List<Object> listParameter = new ArrayList<>();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_QUERY_PROFILLING));
		listParameter.add(PreferenceDefine.RDB_QUERY_PROFILLING);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_RESULT_TYPE));
		listParameter.add(PreferenceDefine.RDB_RESULT_TYPE);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.SELECT_LIMIT_COUNT));
		listParameter.add(PreferenceDefine.SELECT_LIMIT_COUNT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE));
		listParameter.add(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_RESULT_NULL));
		listParameter.add(PreferenceDefine.RDB_RESULT_NULL);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.SELECT_QUERY_TIMEOUT));
		listParameter.add(PreferenceDefine.SELECT_QUERY_TIMEOUT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);

		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.ORACLE_PLAN_TABLE));
		listParameter.add(PreferenceDefine.ORACLE_PLAN_TABLE);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA));
		listParameter.add(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_RESULT_FONT));
		listParameter.add(PreferenceDefine.RDB_RESULT_FONT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_COMMIT_COUNT));
		listParameter.add(PreferenceDefine.RDB_COMMIT_COUNT);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
		
		listParameter.clear();
		listParameter.add(mapUserInfo.get(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN));
		listParameter.add(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN);
		QueryUtils.runSQLOther(TadpoleSystemInitializer.getUserDB(), strUpdate, listParameter);
	}
}
