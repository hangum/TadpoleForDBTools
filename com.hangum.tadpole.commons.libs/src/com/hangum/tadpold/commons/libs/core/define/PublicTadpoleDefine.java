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

import org.apache.commons.io.IOUtils;

import com.hangum.tadpold.commons.libs.core.Messages;

/**
 * 올챙이 전역 정의 
 * 
 * @author hangum
 *
 */
public class PublicTadpoleDefine {
	/**
	 * PLAN Statement ID
	 */
	public static String STATEMENT_ID = "||TADPOLE-STATEMENT_ID||"; //$NON-NLS-1$
	
	/**
	 * 분리자
	 */
	public static String DELIMITER = "||TADPOLE-DELIMITER||"; //$NON-NLS-1$
	
	/** 라인분리자 */
	public static String LINE_SEPARATOR = "\n";//System.getProperty("line.separator"); //$NON-NLS-1$
	public static String DOUBLE_LINE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;
	
	/** DIR SEPARATOR */
	public static char DIR_SEPARATOR = IOUtils.DIR_SEPARATOR;

	/**  쿼리 구분자 */
	public static final String SQL_DELIMITER = ";"; //$NON-NLS-1$
	
	/** tadpole url */
	public static String TADPOLE_URL = "http://127.0.0.1:%s/tadpole";//db?startup=tadpole"; //$NON-NLS-1$
	
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
	
	/** NULL VALUE */
	public static final String DEFINE_NULL_VALUE = "{null}";
	
	/** 쿼리 실행 결과  */
	public static enum QUERY_EXECUTE_STATUS {SUCCESS, USER_INTERRUPT, SQL_EXCEPTION, UNKNOW_EXCEPTION};

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
	public static enum TADPOLE_SUPPORT_BROWSER {FIREFOX, CHROME, SAFARI, IE};
	
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
		SYNONYM,
		INDEXES, 
		PROCEDURES,
		PROCEDURE_PARAMETER,
		FUNCTIONS, 
		TRIGGERS,
		COLLECTIONS,
		JAVASCRIPT,
		PACKAGES
	};
	
	/** 다이얼로그등의 데이터 수정 상태를 가르킵니다 */
	public static enum DATA_STATUS {NEW, MODIFY, DEL};
	
	/** 디비들의 키 이름을 정의합니다 */
//	public static enum DB_KEY {PRI, PK, FK, MUL, UNI};
	
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
		return isKEY(key, YES_NO.NO.toString());
	}
	public static boolean isKEY(String key, String isNull) {
		boolean isReturn = true;

		// 컬럼이 null허용이면 false
		if(isPK(key)) if("YES".equals(isNull)) return false; //$NON-NLS-1$
		if(isFK(key)) if("YES".equals(isNull)) return false; //$NON-NLS-1$
		if(isMUL(key)) if("YES".equals(isNull)) return false; //$NON-NLS-1$
		
		return isReturn;
	}
	
	/**
	 * This is questions for security hint.<br>
	 * Maybe you will put the value in the DB for common code.
	 * 
	 * @author billygoo
	 *
	 */
	public enum SecurityHint {
		QUESTION1(Messages.PublicTadpoleDefine_0, 0), 
		QUESTION2(Messages.PublicTadpoleDefine_1, 1), 
		QUESTION3(Messages.PublicTadpoleDefine_2, 2), 
		QUESTION4(Messages.PublicTadpoleDefine_3, 3),
		QUESTION5(Messages.PublicTadpoleDefine_4, 4),
		QUESTION6(Messages.PublicTadpoleDefine_5, 5),
		QUESTION7(Messages.PublicTadpoleDefine_6, 6), 
		QUESTION8(Messages.PublicTadpoleDefine_7, 7);
		
		private String value;
		private int order;
		
		private SecurityHint(String value, int order) {
			this.value = value;
			this.order = order;
		}
		
		public String getKey() {
			return super.toString();
		}
		
		public int getOrderIndex() {
			return order;
		}

		@Override
		public String toString() {
			return this.value;
		}
	};
}
