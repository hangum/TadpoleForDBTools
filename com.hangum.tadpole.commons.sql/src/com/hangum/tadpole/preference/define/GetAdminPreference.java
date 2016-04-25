/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.define;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;

/**
 * get administrator preference
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 17.
 *
 */
public class GetAdminPreference extends AbstractPreference {
	private static final Logger logger = Logger.getLogger(GetAdminPreference.class);
	
	/**
	 * 사용자 정보를 저장한다.
	 * @return
	 */
	public static String getNewUserPermit() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getValue(mapUserInfoData, AdminPreferenceDefine.NEW_USER_PERMIT, AdminPreferenceDefine.NEW_USER_PERMIT_VALUE);
	}
	
	/**
	 * 사용자가 디비를 추가 할 수 있는지 여부
	 * @return
	 */
	public static String getIsAddDB() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getValue(mapUserInfoData, AdminPreferenceDefine.IS_ADD_DB, AdminPreferenceDefine.IS_ADD_DB_VALUE);
	}
	
	/**
	 * 사용자가 디비를 공유 할 수있는지 여부 
	 *
	 * @return
	 */
	public static String getIsSharedDB() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getValue(mapUserInfoData, AdminPreferenceDefine.IS_SHARED_DB, AdminPreferenceDefine.IS_SHARED_DB_VALUE);
	}
	
	/**
	 * 사용자가 기본으로 디비를 추가할 수 있는 갯수
	 * 
	 * @return
	 */
	public static String getDefaultAddDBCnt() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getValue(mapUserInfoData, AdminPreferenceDefine.DEFAULT_ADD_DB_CNT, AdminPreferenceDefine.DEFAULT_ADD_DB_CNT_VALUE);
	}
	
	/**
	 * 기본 사용자 서비스 사용가능 일
	 * 
	 * @return
	 */
	public static String getServiceDurationDay() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getValue(mapUserInfoData, AdminPreferenceDefine.SERVICE_DURATION_DAY, AdminPreferenceDefine.SERVICE_DURATION_DAY_VALUE);
	}
	
	
	/**
	 * update admin data
	 * @param key
	 * @param userInfoDao
	 */
	public static void updateAdminData(String key, UserInfoDataDAO userInfoDao) {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		mapUserInfoData.put(key, userInfoDao);
	}
	
	/**
	 * 
	 * @return
	 */
	public static SMTPDTO getSessionSMTPINFO() throws Exception {
		SMTPDTO dto = new SMTPDTO();
		
		HttpSession sStore = RWT.getRequest().getSession();
		dto = (SMTPDTO)sStore.getAttribute("smtpinfo"); //$NON-NLS-1$
		
		if(dto == null) {
			dto = new SMTPDTO();
			
//			try {
			UserDAO userDao = TadpoleApplicationContextManager.getSystemAdmin();
			List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData(userDao.getSeq());
			Map<String, UserInfoDataDAO> mapUserInfoData = new HashMap<String, UserInfoDataDAO>();
			for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
				mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
			}
			
			String strHost = getValue(mapUserInfoData, PreferenceDefine.SMTP_HOST_NAME, PreferenceDefine.SMTP_HOST_NAME_VALUE);
			String strPort = getValue(mapUserInfoData, PreferenceDefine.SMTP_PORT, PreferenceDefine.SMTP_PORT_VALUE);
			String strEmail = getValue(mapUserInfoData, PreferenceDefine.SMTP_EMAIL, PreferenceDefine.SMTP_EMAIL_VALUE);
			String strPwd = getValue(mapUserInfoData, PreferenceDefine.SMTP_PASSWD, PreferenceDefine.SMTP_PASSWD_VALUE);
		
			dto.setHost(strHost);
			dto.setPort(strPort);
			dto.setEmail(strEmail);
			dto.setPasswd(strPwd);
			
			if("".equals(strHost) | "".equals(strPort) | "".equals(strEmail) | "".equals(strPwd)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				throw new Exception("Doesn't setting is SMTP Server.");
			}
			
			sStore.setAttribute("smtpinfo", dto); //$NON-NLS-1$
//			} catch (Exception e) {
//				logger.error("get stmt info", e);
//			}
		}
		
		return dto;
	}
	
	public static SMTPDTO getSMTPINFO() {
		SMTPDTO dto = new SMTPDTO();
		
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.getSystemAdmin();
			List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData(userDao.getSeq());
			Map<String, UserInfoDataDAO> mapUserInfoData = new HashMap<String, UserInfoDataDAO>();
			for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
				mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
			}
		
			dto.setHost(getValue(mapUserInfoData, PreferenceDefine.SMTP_HOST_NAME, PreferenceDefine.SMTP_HOST_NAME_VALUE));
			dto.setPort(getValue(mapUserInfoData, PreferenceDefine.SMTP_PORT, PreferenceDefine.SMTP_PORT_VALUE));
			dto.setEmail(getValue(mapUserInfoData, PreferenceDefine.SMTP_EMAIL, PreferenceDefine.SMTP_EMAIL_VALUE));
			dto.setPasswd(getValue(mapUserInfoData, PreferenceDefine.SMTP_PASSWD, PreferenceDefine.SMTP_PASSWD_VALUE));
			
		} catch (Exception e) {
			logger.error("get stmt info", e); //$NON-NLS-1$
		}
		
		return dto;
	}
	
}
