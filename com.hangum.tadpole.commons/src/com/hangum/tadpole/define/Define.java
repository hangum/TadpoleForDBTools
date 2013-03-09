/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.define;


/**
 * Tadpole System Define
 * 
 * @author hangum
 *
 */
public class Define {
	/** 올챙이가 지원 하는 디비 타입을 정의 합니다  */
	public enum DB_TYPE {DB, NOSQL};

	/** yes, no */
	public static enum YES_NO {YES, NO}; 
	
	/** change resource save */
	public static final String SAVE_FILE = "CHANGE_TADPOLE_RESOURE";
	
	/** erd - select table */
	public static final String SELECT_ERD_TABLE = "SELECT_ERD_TABLE_RESOURE";
	
	/** USER TYPE */
	public static enum USER_TYPE {ADMIN, MANAGER, DBA, USER, GUEST};
	
	/** 에디터를 열때 오픈하는 타입을 적습니다. */
	public static enum EDITOR_OPEN_TYPE {NONE, STRING, FILE};
	
	/** save resource type */
	public static enum RESOURCE_TYPE { ERD, SQL, USER_EXECUTE_QUERY };
	
	/** 라인 분리자 */
	public static final String LINE_SEPARATOR = "\r\n";
	
	/**  쿼리 구분자 */
	public static final String SQL_DILIMITER = ";";
	
	/** objec explorer에서 정의한 action */
	public static enum DB_ACTION {
		TABLES, 
		VIEWS, 
		INDEXES, 
		PROCEDURES, 
		FUNCTIONS, 
		TRIGGERS,
		COLLECTIONS,
		JAVASCRIPT
	};
	
	/** query editor에서 실행 모드 */
	public static enum QUERY_MODE {DEFAULT, EXPLAIN_PLAN};

	/** sql을 각 언어로 컨버팅 해줍니다 */
	public static enum SQL_TO_APPLICATION {Java_StringBuffer, PHP};
	
	/** 디비들의 키 이름을 정의합니다 */
	public static enum DB_KEY {PRI, PK, FK, MUL, UNI};
	public static boolean isPK(String key) {
		if(DB_KEY.PRI.toString().equals(key)) return true;
		if(DB_KEY.PK.toString().equals(key)) return true;
		
		return false;
	}
	public static boolean isFK(String key) {
		if(DB_KEY.FK.toString().equals(key)) return true;
		return false;
	}
	public static boolean isMUL(String key) {
		if(DB_KEY.MUL.toString().equals(key)) return true;
		return false;
	}
	public static boolean isKEY(String key) {
		return isKEY(key, Define.YES_NO.NO.toString());
	}
	public static boolean isKEY(String key, String isNull) {
		for(DB_KEY dbKEY : DB_KEY.values()) {
			if(dbKEY.toString().equals(key)) {
				// 컬럼이 null허용이면 false
				if("YES".equals(isNull)) return false;
				return true;
			}
		}
		
		return false;
	}
	
	/** tadpole url */
	public static String TADPOLE_URL = "http://127.0.0.1:%s/tadpole";//db?startup=tadpole";
	
	/**
	 * tadpole url
	 * 
	 * @return
	 */
	public static String getTadpoleUrl() {
		String tadpolePort = System.getProperty("org.osgi.service.http.port", "10081");
		return String.format(TADPOLE_URL, tadpolePort);
	}
}
