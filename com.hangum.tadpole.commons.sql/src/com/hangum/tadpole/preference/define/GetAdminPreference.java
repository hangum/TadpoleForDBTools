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

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

import com.hangum.tadpole.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;

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
	 * db_time_zone
	 * @return
	 */
	public static String getDBTimezone() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.DB_TIME_ZONE, AdminPreferenceDefine.DB_TIME_ZONE_VALUE);
	}
	
	/**
	 * system login method
	 * @return
	 */
	public static String getLoginMethod() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, AdminPreferenceDefine.SYSTEM_LOGIN_METHOD_VALUE);
	}
	
	/**
	 * system LDAP url
	 * @return
	 */
	public static String getLDAPURL() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SYSTEM_LDAP_URL, AdminPreferenceDefine.SYSTEM_LDAP_URL_VALUE);
	}
	
	/**
	 * system LDAP User
	 * @return
	 */
	public static String getLDAPUser() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SYSTEM_LDAP_USER, AdminPreferenceDefine.SYSTEM_LDAP_USER_VALUE);
	}
	
	/**
	 * system LDAP authentication
	 * @return
	 */
	public static String getLDAPAuthentication() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SYSTEM_LDAP_AUTHENTICATION, AdminPreferenceDefine.SYSTEM_LDAP_AUTHENTICATION_VALUE);
	}
	
	/**
	 * api server uri
	 * @return
	 */
	public static String getApiServerURL() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.API_SERVER_URL, AdminPreferenceDefine.API_SERVER_URL_VALUE);
	}

	/**
	 * 디비 연결시 묻는 타입
	 * 
	 * @return
	 */
	public static String getConnectionAskType() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.DB_CONNECTION_ASK, AdminPreferenceDefine.DB_CONNECTION_ASK_VALUE);
	}
	
	/**
	 * 신규 사용자 등록이 어드민의 허락이 필요하면 디비에 등록할때는 NO를 입력, 필요치 않으면 YES를 입력.
	 * 디폴트는 YES이므로 어드민 허락이 필요치 않다. 
	 * 
	 * @return
	 */
	public static String getNewUserPermit() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.NEW_USER_PERMIT, AdminPreferenceDefine.NEW_USER_PERMIT_VALUE);
	}

	/**
	 * 신규 디비 등록시 디비 패스워드가 저장 여부
	 * YES이면 패스워드 저장이다.
	 * @return
	 */
	public static String getSaveDBPassword() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SAVE_DB_PASSWORD, AdminPreferenceDefine.SAVE_DB_PASSWORD_VALUE);
	}
	
	/**
	 * 사용자 디폴트 홈페이지.
	 * @return
	 */
	public static String getHomePage() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.DEFAULT_HOME, AdminPreferenceDefine.DEFAULT_HOME_VALUE);
	}
	
	/**
	 * 사용자 디폴트 홈페이지 오픈 여부
	 * @return
	 */
	public static String getHomePageOpen() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.DEFAULT_HOME_OPEN, AdminPreferenceDefine.DEFAULT_HOME_OPEN_VALUE);
	}
	
	/**
	 * 패스워드 복잡도 유무(NO)
	 * @return
	 */
	public static String getPasswdComplexity() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.PASSWD_COMPLEXITY, AdminPreferenceDefine.PASSWD_COMPLEXITY_VALUE);
	}
	
	/**
	 * 패스워드 최소 길이
	 * @return
	 */
	public static String getPasswdLengthLimit() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.PASSWD_LENGTH_LIMIT, AdminPreferenceDefine.PASSWD_LENGTH_LIMIT_VALUE);
	}
	
	/**
	 * 패스워드 변경 최하 일자
	 * @return
	 */
	public static String getPasswdDateLimit() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.PASSWD_DATE_LIMIT, AdminPreferenceDefine.PASSWD_DATE_LIMIT_VALUE);
	}
	
	/**
	 * 사용자가 디비를 추가 할 수 있는지 여부
	 * @return
	 */
	public static String getIsAddDB() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.IS_ADD_DB, AdminPreferenceDefine.IS_ADD_DB_VALUE);
	}
	
	/**
	 * 사용자가 디비를 공유 할 수있는지 여부 
	 *
	 * @return
	 */
	public static String getIsSharedDB() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.IS_SHARED_DB, AdminPreferenceDefine.IS_SHARED_DB_VALUE);
	}
	
	/**
	 * 사용자가 기본으로 디비를 추가할 수 있는 갯수
	 * 
	 * @return
	 */
	public static String getDefaultAddDBCnt() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.DEFAULT_ADD_DB_CNT, AdminPreferenceDefine.DEFAULT_ADD_DB_CNT_VALUE);
	}
	
	/**
	 * 기본 사용자 서비스 사용가능 일
	 * 
	 * @return
	 */
	public static String getServiceDurationDay() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SERVICE_DURATION_DAY, AdminPreferenceDefine.SERVICE_DURATION_DAY_VALUE);
	}
	
	/**
	 * 기본 사용자 서비스 사용가능 일
	 * 
	 * @return
	 */
	public static String getSupportMonitoring() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SUPPORT_MONITORING, AdminPreferenceDefine.SUPPORT_MONITORING_VALUE);
	}
	
	/**
	 * 사용자가 프리퍼런스를 수정 여부 지정.
	 * @return
	 */
	public static String getIsPreferenceModify() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.IS_PREFERENCE_MODIFY, AdminPreferenceDefine.IS_PREFERENCE_MODIFY_VALUE);
	}
	
	/**
	 * 사용자가 리소스 다운로드 받을수 없는지 유무
	 * @return
	 */
	public static String getIsDefaultDonwload() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.IS_RESOURCE_DOWNLOAD, AdminPreferenceDefine.IS_RESOURCE_DOWNLOAD_VALUE);
	}
	
	/**
	 * 메일 사용 타입을 지정한다. 
	 * 없음, sendgrid, smtp
	 * @return
	 */
	public static String getMailUsingType() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.MAIL_LOGIN_TYPE, AdminPreferenceDefine.MAIL_LOGIN_TYPE_VALUE);		
	}
	
	/**
	 * 어드민이 지정한 프러럭타입 필터 값을 뺀다. 
	 * 
	 * @return
	 */
	public static String getViewProductTypeFilter() {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		return getAdminValue(mapUserInfoData, AdminPreferenceDefine.SYSTEM_VIEW_PRODUCT_TYPE_FILTER, AdminPreferenceDefine.SYSTEM_VIEW_PRODUCT_TYPE_FILTER_VALUE);		
	}
	
	/**
	 * update admin data
	 * @param key
	 * @param userInfoDao
	 */
	public static void updateAdminSessionData(String key, UserInfoDataDAO userInfoDao) {
		Map<String, UserInfoDataDAO> mapUserInfoData = TadpoleApplicationContextManager.getAdminSystemEnv();
		mapUserInfoData.put(key, userInfoDao);
	}
	
	/**
	 * 
	 * @return
	 */
	public static SMTPDTO getSessionSMTPINFO() {
		SMTPDTO dto = new SMTPDTO();
		
		ApplicationContext context = RWT.getApplicationContext();
		dto = (SMTPDTO)context.getAttribute("smtpinfo"); //$NON-NLS-1$
		
		if(dto == null) {
			dto = getSMTPINFO();
			context.setAttribute("smtpinfo", dto); //$NON-NLS-1$
		}
		
		return dto;
	}
	
	/**
	 * set session smtp info
	 * 
	 * @param dto
	 */
	public static void setSessionSmtpInfo(SMTPDTO smtpDto) {
		ApplicationContext context = RWT.getApplicationContext();
		context.setAttribute("smtpinfo", smtpDto);
	}
	
	/**
	 * get smtp
	 * @return
	 */
	private static SMTPDTO getSMTPINFO() {
		SMTPDTO dto = new SMTPDTO();
		
		try {
			List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData(-1);
			Map<String, UserInfoDataDAO> mapUserInfoData = new HashMap<String, UserInfoDataDAO>();
			for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
				mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
			}
		
			dto.setLoginMethodType(getAdminValue(mapUserInfoData, AdminPreferenceDefine.MAIL_LOGIN_TYPE, AdminPreferenceDefine.MAIL_LOGIN_TYPE_VALUE));
			dto.setDomain(getAdminValue(mapUserInfoData, AdminPreferenceDefine.MAIN_DOMAIN, AdminPreferenceDefine.MAIN_DOMAIN_VALUE));
			dto.setSendgrid_api(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SENDGRID_API_NAME, AdminPreferenceDefine.SENDGRID_API_VALUE));
			
			dto.setStarttls_enable(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SMTP_STARTTLS_ENABLE, AdminPreferenceDefine.SMTP_STARTTLS_ENABLE_VALUE));
			dto.setIsAuth(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SMTP_IS_AUTH, AdminPreferenceDefine.SMTP_IS_AUTH_VALUE));
			
			dto.setHost(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SMTP_HOST_NAME, AdminPreferenceDefine.SMTP_HOST_NAME_VALUE));
			dto.setPort(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SMTP_PORT, AdminPreferenceDefine.SMTP_PORT_VALUE));
			dto.setEmail(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SMTP_EMAIL, AdminPreferenceDefine.SMTP_EMAIL_VALUE));
			dto.setPasswd(getAdminValue(mapUserInfoData, AdminPreferenceDefine.SMTP_PASSWD, AdminPreferenceDefine.SMTP_PASSWD_VALUE));
			
		} catch (Exception e) {
			logger.error("get stmt info", e); //$NON-NLS-1$
		}
		
		return dto;
	}
}
