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
package com.hangum.tadpold.commons.libs.core.define;


/**
 * 올챙이 전역 정의 
 * 
 * @author hangum
 *
 */
public class PublicTadpoleDefine {
	/**
	 * 분리자
	 */
	public static String DELIMITER = "||TADPOLE-DELIMITER||";
	
	/** 라인분리자 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	/**  쿼리 구분자 */
	public static final String SQL_DILIMITER = ";";
	
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
	
	/** 외부 계정으로 올챙이가 접속 할때의 외부 계정 리스트. 현재는 external_account 의 type에 사용. */
	public enum EXTERNAL_ACCOUNT {AMAZONRDS};
	
//	/** 올챙이가 지원 하는 디비 타입을 정의 합니다  */
//	public enum DB_TYPE {DB, NOSQL};

	/** yes, no */
	public static enum YES_NO {YES, NO}; 
	
	/** change resource save */
	public static final String SAVE_FILE = "CHANGE_TADPOLE_RESOURE";
	
	/** erd - select table */
	public static final String SELECT_ERD_TABLE = "SELECT_ERD_TABLE_RESOURE";
	
	/** auto commit 사용여부를 가립니다. */
	public static final String AUTOCOMMIT_USE = "_AUTOCOMMIT_USE";
	
	/** USER TYPE */
	public static enum USER_TYPE {ADMIN, DBA, MANAGER, USER/*, GUEST*/};
	
	/** 에디터를 열때 오픈하는 타입을 적습니다. */
	public static enum EDITOR_OPEN_TYPE {NONE, STRING, FILE};
	
	/** save resource type */
	public static enum RESOURCE_TYPE { ERD, SQL };
	
	/** define SQL, ERD shared type */
	public static enum SHARED_TYPE {PUBLIC, PRIVATE};
	
	/** executed sql history type */
	public static enum EXECUTE_SQL_TYPE {EDITOR, SESSION};
	
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
	
	/** 다이얼로그등의 데이터 수정 상태를 가르킵니다 */
	public static enum DATA_STATUS {NEW, MODIFY, DEL};
	
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
		return isKEY(key, PublicTadpoleDefine.YES_NO.NO.toString());
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
}
