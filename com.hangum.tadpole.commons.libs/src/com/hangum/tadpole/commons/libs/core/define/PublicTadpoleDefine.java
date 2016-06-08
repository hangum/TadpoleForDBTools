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
package com.hangum.tadpole.commons.libs.core.define;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.rap.rwt.RWT;

/**
 * 전역 변수 정의 
 * 
 * @author hangum
 *
 */
public class PublicTadpoleDefine {
	/** url system verion information */
	public static final String URL_SYSTEM_VERION = String.format("?%s%s=%s", SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.RELEASE_DATE);
	
	/** defiee default time zone*/
	public static final String DEFAULT_TIME_ZONE = "Asia/Seoul";
	
	/** defualt log file name*/
	public static final String DEFAULT_LOG_FILE 		= "./logs/tadpole.log";
	public static final String DEFAULT_VELOCITY_LOG_FILE = "./logs/tadpoleVelocity.log";
	
	/** license 파일 */
	public static final String LICENSE_FILE = "TadpoleHub.lic";
	
	/** cookie path */
	public static String _cookiePath = "/";
	
	public static final int systemAdminId = -1;
	
	/** Default resource name */
	public static final String DEFAUL_RESOURCE_NAME = "_TDB_DEF_NAME_";

	/** 시스템 사용 그룹 정의 */
	public static enum SYSTEM_USE_GROUP {PERSONAL, GROUP}  

	/** rest api service key */
	public static final String SERVICE_KEY_NAME = "serviceID";
	
	/** default system encrypt password */
	public static final String SYSTEM_DEFAULT_USER = "hangum@tadpolehub.com";
	public static final String SYSTEM_DEFAULT_PASSWORD = "startService.tdb.son";
	
	/** COOKIE USER ID */
	public static final String TDB_COOKIE_UPDATE_CHECK = "TDB_COOKIE_UPDATE_CHECK";
	public static final String TDB_COOKIE_USER_ID = "TDB_USER_ID";
	public static final String TDB_COOKIE_USER_PWD = "TDB_USER_PWD";
	public static final String TDB_COOKIE_USER_SAVE_CKECK = "TDB_USER_SAVE_CHECK";
	public static final String TDB_COOKIE_USER_LANGUAGE = "TDB_USER_LANGUAGE";

	/**
	 * PLAN Statement ID
	 */
	public static String STATEMENT_ID = "||TDB_STATEMENT_ID||"; //$NON-NLS-1$

	/** 0번째 테이블 컬럼을 선택한다 */
	public static String DEFINE_TABLE_COLUMN_BASE_ZERO = "TDB_BASE_ZERO";
	public static String DEFINE_TABLE_COLUMN_BASE_ZERO_TYPE = "TDB_BASE_TYPE";
	
	/**
	 * 특별 컬럼을 정의 합니다. 
	 */
	public static String SPECIAL_USER_DEFINE_HIDE_COLUMN = "TDB_HIDE";
	
	/**
	 * 분리자
	 */
	public static String DELIMITER = "||TDB-DELIMITER||"; //$NON-NLS-1$
	public static String DELIMITER_DBL = "||TDB-DELIMITER-DBL||"; //$NON-NLS-1$
	
	/** 라인분리자 */
	public static String LINE_SEPARATOR = "\n";//System.getProperty("line.separator"); //$NON-NLS-1$
	public static String DOUBLE_LINE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;
	
	/** DIR SEPARATOR */
	public static char DIR_SEPARATOR = IOUtils.DIR_SEPARATOR;
	
	/** temp dir 
		임시 디렉토리 생성에 오류 있음. 확인 필요.
		java.io.IOException: Directory '/tmpTempTable1458208430419' could not be created 오류 발생.
		환경 설정에 문제인지(prefix 혹은 디렉토리 미지정).. 아니면 코드상의 오류(DIRECTORY-SEPERATOR 가 빠진 문제)인지 확인 필요
	*/
	public static String TEMP_DIR = FileUtils.getTempDirectoryPath() + PublicTadpoleDefine.DIR_SEPARATOR;

	/**  쿼리 구분자 */
	public static final String SQL_DELIMITER = ";"; //$NON-NLS-1$
	
	/** tadpole url */
	public static String TADPOLE_URL = "http://127.0.0.1:%s";
	
	/**
	 * tadpole url
	 * 
	 * @return
	 */
	public static String getTadpoleUrl() {
		String tadpolePort = System.getProperty("org.osgi.service.http.port", "10081"); //$NON-NLS-1$ //$NON-NLS-2$
		return String.format(TADPOLE_URL, tadpolePort);
	}
	
	/** 외부 계정으로 올챙이가 접속 할때의 외부 계정 리스트. 현재는 external_account 의 type에 사용. */
	public enum EXTERNAL_ACCOUNT {AMAZONRDS};
	
//	/** NULL VALUE */
//	public static final String DEFINE_NULL_VALUE = "{null}";
	
	/** user login type */
	public static enum INPUT_TYPE {NORMAL, GOOGLE_OAUTH, LDAP};
	
	/** yes, no */
	public static enum YES_NO {YES, NO}; 
	
	/** Success, Fail */
	public static enum SUCCESS_FAIL {S, F};
	
	/** change resource save */
	public static final String SAVE_FILE = "CHANGE_TADPOLE_RESOURE"; //$NON-NLS-1$
	/** change add new db */
	public static final String ADD_DB = "CHANGE_TADPOLE_ADD_DB";
	
	/** erd - select table */
	public static final String SELECT_ERD_TABLE = "SELECT_ERD_TABLE_RESOURE"; //$NON-NLS-1$
	
	/** auto commit 사용여부를 가립니다. */
	public static final String AUTOCOMMIT_USE = "_AUTOCOMMIT_USE"; //$NON-NLS-1$
	
	/** Tadpole support browser list  */
	public static enum TADPOLE_SUPPORT_BROWSER {EDGE, FIREFOX, CHROME, SAFARI, IE};
	
	/** 
	 * This variable is user_role_table. 
	 */
	public static enum USER_ROLE_TYPE {SYSTEM_ADMIN, ADMIN, DBA, MANAGER, USER, GUEST};
	
	/**
	 * Setting SQL Client Info
	 * @return
	 */
	public static Properties getSQLClientInfo() {
		Properties prop = new Properties();
		prop.setProperty("ApplicationName", String.format("%s %s %s", SystemDefine.NAME, SystemDefine.MAJOR_VERSION, SystemDefine.RELEASE_DATE));
		prop.setProperty("ClientUser", 		RWT.getRequest().getRemoteHost());
		prop.setProperty("ClientHostname", 	RWT.getRequest().getLocalAddr());
		
		return prop;
	}
	
	/**
	 * ace editor theme list
		https://docs.c9.io/docs/syntax-highlighting-themes
	*/
	private static Map<String, String> mapTheme = new HashMap<String, String>();
	public static Map<String, String> getMapTheme() {
		if(mapTheme.isEmpty()) {
			mapTheme.put("Chrome", 			"chrome");
			mapTheme.put("Clouds", 			"clouds");
			mapTheme.put("Clouds Midnight", "clouds_midnight");
			mapTheme.put("Cobalt", 			"cobalt");
			mapTheme.put("Crimson Editor", 	"crimson_editor");
			mapTheme.put("Dawn", 			"dawn");
			mapTheme.put("Eclipse", 		"eclipse");
			mapTheme.put("Idle Fingers", 	"idle_fingers");
			mapTheme.put("Kr Theme", 		"kr_theme");
			mapTheme.put("Merbivore", 		"merbivore");
			mapTheme.put("Merbivore Soft", 	"merbivore_soft");
			mapTheme.put("Mono Industrial", "mono_industrial");
			mapTheme.put("Monokai", 		"monokai");
			mapTheme.put("Pastel On Dark", 	"pastel_on_dark");
			mapTheme.put("Solarized Dark", 	"solarized_dark");
			mapTheme.put("Solarized Light", "solarized_light");
			mapTheme.put("TextMate", 		"textmate");
			mapTheme.put("Tomorrow", 		"tomorrow");
			mapTheme.put("Tomorrow Night", 	"tomorrow_night");
			mapTheme.put("Tomorrow Night Blue", 	"tomorrow_night_blue");
			mapTheme.put("Tomorrow Night Bright", 	"tomorrow_night_bright");
			mapTheme.put("Tomorrow Night Eighties", "tomorrow_night_eighties");
			mapTheme.put("Twilight", 				"twilight");
			mapTheme.put("Vibrant Inkv", 			"vibrant_inkv");
		}
		return mapTheme;
	}
	
	/**
	 * db operation type
	 * 
	 * @author hangum
	 *
	 */
	public enum DBOperationType {
		PRODUCTION("Production Sever"), 
		DEVELOP("Develop Sever"), 
		TEST("Test Sever"),
		BACKUP("Backup Sever"),
		OTHERS("Others Sever");

		private String typeName;
		
		private DBOperationType(String typeName) {
			this.typeName = typeName;
		}
		
		public String getTypeName() {
			return typeName;
		}
		
		public static DBOperationType getNameToType(String name) {
			if(PRODUCTION.typeName.equals(name)) 	return PRODUCTION;
			else if(DEVELOP.typeName.equals(name)) 	return DEVELOP;
			else if(TEST.typeName.equals(name)) 	return TEST;
			else if(BACKUP.typeName.equals(name)) 	return BACKUP;
			else return OTHERS;
		}
	};

	/** 에디터를 열때 오픈하는 타입을 적습니다. */
	public static enum EDITOR_OPEN_TYPE {NONE, STRING, FILE};
	
	/** save resource type */
	public static enum RESOURCE_TYPE {ERD, SQL, AUTO_SQL};
	
	/** define SQL, ERD shared type */
	public static enum SHARED_TYPE {PUBLIC, PRIVATE};
	
	/** executed sql history type */
	public static enum EXECUTE_SQL_TYPE {EDITOR, SESSION, API};

	/** 쿼리 실행 결과  */
	public static enum QUERY_EXECUTE_STATUS {SUCCESS, USER_INTERRUPT, SQL_EXCEPTION, UNKNOW_EXCEPTION};
	
	/** 데이터 수정 상태를 가르킵니다 */
	public static enum DATA_STATUS {NEW, MODIFY, DEL};

	/** objec explorer에서 정의한 action */
	public static enum OBJECT_TYPE {
		TABLES, 
		VIEWS, 
		SYNONYM,
		INDEXES, 
		CONSTRAINTS,
		PROCEDURES,
		PROCEDURE_PARAMETER,
		FUNCTIONS, 
		TRIGGERS,
		COLLECTIONS,
		JAVASCRIPT,
		PACKAGES,
		SCHEDULE
	};

	/** sql type - http://www.orafaq.com/faq/what_are_the_difference_between_ddl_dml_and_dcl_commands */
	public static enum SQL_TYPE {DDL, DML};//, DCL, TCL};

	/** query type */
	public static enum QUERY_DML_TYPE {SELECT, EXPLAIN_PLAN, INSERT, UPDATE, DELETE, UNKNOWN};
	
	/** query ddl type */
	public static enum QUERY_DDL_STATUS {CREATE, ALTER, DROP, UNKNOWN};
	public static enum QUERY_DDL_TYPE 	{TABLE, VIEW, INDEX, PROCEDURE, FUNCTION, TRIGGER, PACKAGE, SYNONYM, UNKNOWN};
	
	public static String[] DB_PRIMARY_KEY = {
											"PRI", 
											"PK", 
											"PRIMARY KEY",	// pgsql
											};
	
	public static String[] DB_FOREIGN_KEY = {
											"FK", 
											"FOREIGN KEY",	// pgsql
											};
	
	public static String[] DB_MULTI_KEY = {
											"MUL",
											"PRIMARY KEY,FOREIGN KEY"	// pgsql
										};
	
	/**
	 * is primary key
	 * @param key
	 * @return
	 */
	public static boolean isPK(String key) {
		for(String searchKey : DB_PRIMARY_KEY) {
			if(searchKey.equalsIgnoreCase(key)) return true;
		}
		
		return false;
	}
	
	/**
	 * is foreign key
	 * @param key
	 * @return
	 */
	public static boolean isFK(String key) {
		for(String searchKey : DB_FOREIGN_KEY) {
			if(searchKey.equalsIgnoreCase(key)) return true;
		}
		
		return false;
	}
	
	/**
	 * is multi key
	 * @param key
	 * @return
	 */
	public static boolean isMUL(String key) {
		for(String searchKey : DB_MULTI_KEY) {
			if(searchKey.equalsIgnoreCase(key)) return true;
		}
		
		return false;
	}
	/**
	 * is key
	 * @param key
	 * @return
	 */
	public static boolean isKEY(String key) {
		return isKEY(key, YES_NO.NO.name());
	}
	public static boolean isKEY(String key, String isNull) {
		boolean isReturn = true;

		// 컬럼이 null허용이면 false
		if(isPK(key)) if("YES".equals(isNull)) return false; //$NON-NLS-1$
		if(isFK(key)) if("YES".equals(isNull)) return false; //$NON-NLS-1$
		if(isMUL(key)) if("YES".equals(isNull)) return false; //$NON-NLS-1$
		
		return isReturn;
	}
}
