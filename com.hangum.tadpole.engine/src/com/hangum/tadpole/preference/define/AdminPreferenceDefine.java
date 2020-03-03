/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.define;

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Define admin preference
 * 
 * @author hangum
 *
 */
public class AdminPreferenceDefine {
	private static final String _PREFIX = "ADMIN_";
	
	/** DBMS timezone */
	public static final String DB_TIME_ZONE = _PREFIX + "DB_TIME_ZONE";
	public static final String DB_TIME_ZONE_VALUE = "";
	
	/** 로그인시 브라우저 IP 사용 여부 */
	public static final String USER_BROWSER_IP = _PREFIX + "USE_BROWSER_IP";
	public static final String USER_BROWSER_IP_VALUE = "true";
	
	/** PRODUCT FILTER */
	public static final String SYSTEM_VIEW_PRODUCT_TYPE_FILTER = _PREFIX + "SYSTEM_VIEW_PRODUCT_TYPE_FILTER";
	public static final String SYSTEM_VIEW_PRODUCT_TYPE_FILTER_VALUE = "";
	
	/** GROUP NAME FILTER */
	public static final String SYSTEM_VIEW_GROUP_NAME_FILTER = _PREFIX + "SYSTEM_VIEW_GROUP_NAME_FILTER";
	public static final String SYSTEM_VIEW_GROUP_NAME_FILTER_VALUE = "";
	
	/** system login method */
	public static final String SYSTEM_LOGIN_METHOD = _PREFIX + "SYSTEM_LOGIN_METHOD";
	public static final String SYSTEM_LOGIN_METHOD_VALUE = PublicTadpoleDefine.LOGIN_INPUT_TYPE.NORMAL.name();
	
	/** ldap example */
	public static final String SYSTEM_LDAP_URL = _PREFIX + "SYSTEM_LDAP_URL";
	public static final String SYSTEM_LDAP_URL_VALUE = "ldap://127.0.0.1:389";
	
	public static final String SYSTEM_LDAP_BINDDN = _PREFIX + "SYSTEM_LDAP_BINDDN";
	public static final String SYSTEM_LDAP_BINDDN_VALUE = "cn=admin,dc=example,dc=com";
	
	public static final String SYSTEM_LDAP_BINDDNPWD = _PREFIX + "SYSTEM_LDAP_BINDDNPWD";
	public static final String SYSTEM_LDAP_BINDDNPWD_VALUE = "";
	
	public static final String SYSTEM_LDAP_BASE_DN = _PREFIX + "SYSTEM_LDAP_BASE_DN";
	public static final String SYSTEM_LDAP_BASE_DN_VALUE = "dc=example,dc=com";
	
	public static final String SYSTEM_LDAP_USER = _PREFIX + "SYSTEM_LDAP_USER";
	public static final String SYSTEM_LDAP_USER_VALUE = "(uid=%s)";
	
	/** 신규 사용자 허락 유무 */
	public static final String NEW_USER_PERMIT = _PREFIX + "NEW_USER_PERMIT";
	public static final String NEW_USER_PERMIT_VALUE = "YES";
	
	/** 신규 디비 등록시 패스워드 저장 유무(YES이면 패스워드 저장) */
	public static final String SAVE_DB_PASSWORD = _PREFIX + "SAVE_DB_PASSWORD";
	public static final String SAVE_DB_PASSWORD_VALUE = "YES";
	
	/** 패스워드 복잡도 유무 */
	public static final String PASSWD_COMPLEXITY = _PREFIX + "PASSWD_COMPLEXITY";
	public static final String PASSWD_COMPLEXITY_VALUE = "NO";
	
	/** 패스워드 최하 길이 */
	public static final String PASSWD_LENGTH_LIMIT = _PREFIX + "PASSWD_LENGTH_LIMIT";
	public static final String PASSWD_LENGTH_LIMIT_VALUE = "8";
	
	/** 패스워드 최대 길이 */
	public static final String PASSWD_DATE_LIMIT = _PREFIX + "PASSWD_DATE_LIMIT";
	public static final String PASSWD_DATE_LIMIT_VALUE = "30";
	
	/** 사용자가 디비 추가 여부 */
	public static final String IS_ADD_DB = _PREFIX + "IS_ADD_DB";
	public static final String IS_ADD_DB_VALUE = "YES";
	
	/** 사용자가 디비 공유 여부 */
	public static final String IS_SHARED_DB = _PREFIX + "IS_SHARED_DB";
	public static final String IS_SHARED_DB_VALUE = "YES";
	
	/** 사용자가 디비를 추가 할수 있는 카운트 */
	public static final String DEFAULT_ADD_DB_CNT = _PREFIX + "DEFAULT_ADD_DB_CNT";
	public static final String DEFAULT_ADD_DB_CNT_VALUE = "50";
	
	/** 가입 일 부터 서비스 사용 제한 일 */
	public static final String SERVICE_DURATION_DAY = _PREFIX + "SERVICE_DURATION_DAY";
	public static final String SERVICE_DURATION_DAY_VALUE = "3650";
	
	/**
	 * Define api server uri
	 */
	public static final String API_SERVER_URL = _PREFIX + "API_SERVER_URL";
	public static final String API_SERVER_URL_VALUE = "http://localhost:8080/api";
	
	/**
	 * Define Tadpole Support DB
	 */
	public static final String TADPOLE_SUPPORT_DB = _PREFIX + "TADPOLE_SUPPORT_DB";
	public static final String TADPOLE_SUPPORT_DB_VALUE = "MariaDB,MongoDB,MSSQL,MySQL,Oracle,PostgreSQL,Tibero,Elasticsearch,Altibase";
	
	/**
	 * 디비연결시 사용자에게 묻기.
	 */
	public static final String DB_CONNECTION_ASK = _PREFIX + "DB_CONNECTION_ASK";
	public static final String DB_CONNECTION_ASK_VALUE = "NO";
	
	/**
	 * Define monitoring
	 */
	public static final String SUPPORT_MONITORING = _PREFIX + "SUPPORT_MONITORING";
	public static final String SUPPORT_MONITORING_VALUE = "NO";
	
	/**
	 * 사용자 프리퍼런스 지정 여부
	 */
	public static final String IS_PREFERENCE_MODIFY = _PREFIX + "IS_PREFERENCE_MODIFY";
	public static final String IS_PREFERENCE_MODIFY_VALUE = "YES";
	
	// set smtp information
		public static final String MAIL_LOGIN_TYPE			= "MAIL_LOGIN_TYPE";
		public static final String MAIL_LOGIN_TYPE_VALUE	= "NONE";
		
		public static final String MAIN_DOMAIN				= "MAIN_DOMAIN";
		public static final String MAIN_DOMAIN_VALUE		= "";
		
		public static final String SENDGRID_API_NAME 		= "SENDGRID_API_NAME";
		public static final String SENDGRID_API_VALUE 		= "";
		
		public static final String SMTP_STARTTLS_ENABLE		= "SMTP_STARTTLS_ENABLE";
		public static final String SMTP_STARTTLS_ENABLE_VALUE	= "NO";
		
		public static final String SMTP_IS_AUTH 			= "SMTP_IS_AUTH";
		public static final String SMTP_IS_AUTH_VALUE 		= "NO";
	
		public static final String SMTP_HOST_NAME 		= "SMTP_HOST_NAME";
		public static final String SMTP_HOST_NAME_VALUE = "smtp.gmail.com";
		
		public static final String SMTP_PORT 			= "SMTP_PORT";
		public static final String SMTP_PORT_VALUE 		= "465";
		
		public static final String SMTP_EMAIL 			= "SMTP_EMAIL";
		public static final String SMTP_EMAIL_VALUE 	= "";
		
		public static final String SMTP_PASSWD 			= "SMTP_PASSWD";
		public static final String SMTP_PASSWD_VALUE 	= "";

	/** 홈페이지 */
	public static final String DEFAULT_HOME = _PREFIX + "DEFAULT_HOME";
	public static final String DEFAULT_HOME_VALUE = "https://github.com/hangum/TadpoleForDBTools";
	/** 홈페이지 오픈 여부 */
	public static final String DEFAULT_HOME_OPEN = _PREFIX + "DEFAULT_HOME_OPEN";
	public static final String DEFAULT_HOME_OPEN_VALUE = "false";
	
	/** 쿼리 결과 저장 여부 */
	public static final String DEFAULT_QUERY_RESULT_SAVED = _PREFIX + "DEFAULT_QUERY_RESULT_SAVED";
	public static final String DEFAULT_QUERY_RESULT_SAVED_VALUE = "";

	/** 
	 * 리소스 다운로드 여부
	 */
	public static final String IS_RESOURCE_DOWNLOAD = _PREFIX + "IS_RESOURCE_DOWNLOAD";
	public static final String IS_RESOURCE_DOWNLOAD_VALUE = "YES";
	
	/** 
	 * 사용자가 OTP를 사용할지 여부
	 */
	public static final String IS_DEFAULT_OTP = _PREFIX + "IS_DEFAULT_OTP";
	public static final String IS_DEFAULT_OTP_VALUE = "NO";
	
	/** 
	 * 쿼리 결과 다운로드 한계 지정 여부
	 * -1이면 전체 다운로드
	 */
	public static final String QUERY_RESULT_DOWNLOAD_LIMIT = _PREFIX + "QUERY_RESULT_DOWNLOAD_LIMIT";
	public static final String QUERY_RESULT_DOWNLOAD_LIMIT_VALUE = "100000";

	/** 
	 * 패스워드 관리 시스템을 사용할 경우 캐쉬 사용 여부를 기록한다.
	 */
	public static final String IS_PASSWD_CACHE = _PREFIX + "IS_PASSWD_CACHE";
	public static final String IS_PASSWD_CACHE_VALUE = "YES";
	
	/**
	 * 사용자 SQL 헤더에 TADPOLE 코멘트 추가 여부
	 * 예를들어 : {@code SessionManager#getHeadComment()}
	 */
	public static final String ADD_TADPOLE_SQL_HEAD = _PREFIX + "ADD_TADPOLE_SQL_HEAD";
	public static final String ADD_TADPOLE_SQL_HEAD_VALUE = "NO";	
}
