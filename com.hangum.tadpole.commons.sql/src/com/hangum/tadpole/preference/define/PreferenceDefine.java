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
package com.hangum.tadpole.preference.define;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;

/**
 * Preference name define
 * 
 * @author hangum
 *
 */
public class PreferenceDefine extends AdminPreferenceDefine {
	
	/**시스템이 초기화 되었는지 정의 */
	public static final String IS_TADPOLE_INITIALIZE = "IS_TADPOLE_INITIALIZE";
	
	/** 에디터와 커넥션 뷰를 싱크를 맞출것인지. */
	public static final String SYNC_EIDOTR_STATS = "SYNC_EIDOTR_STATS";
	public static final String SYNC_EIDOTR_STATS_VALUE = "true";
	
	/** export시 데이터 분리자. */
	public static final String EXPORT_DILIMITER = "EXPORT_DILIMITER";
	public static final String EXPORT_DILIMITER_VALUE = ",";
	
	/** default home page */
	public static final String DEFAULT_HOME_PAGE 		= "DEFAULT_HOME_PAGE";
	public static final String DEFAULT_HOME_PAGE_VALUE 	= "http://hangum.github.io/TadpoleForDBTools/index2.html";//https://sites.google.com/site/tadpolefordb/home";//"https://www.github.com/hangum/TadpoleForDBTools/wiki";
//	public static final String DEFAULT_HOME_PAGE_EN_VALUE 	= "https://sites.google.com/site/tadpolefordbtoolsen";
	
	/** default home page use */
	public static final String DEFAULT_HOME_PAGE_USE 		= "DEFAULT_HOME_PAGE_USE";
	public static final String DEFAULT_HOME_PAGE_USE_VALUE 	= "true";
	
	/** RDB Result type */ 
	public static final String RDB_RESULT_TYPE = "RDB_RESULT_TYPE";
	/** RDB Result type 값 */
	public static final String RDB_RESULT_TYPE_VALUE = "Table";
	
	/** RDB Result 값중에 NULL값이 있을 경우 처리 */ 
	public static final String RDB_RESULT_NULL = "RDB_RESULT_NULL";
	/** RDB Result type 값 */
	public static final String RDB_RESULT_NULL_VALUE = "{null}";
	
	/** select 제한  갯수 */ 
	public static final String SELECT_LIMIT_COUNT = "SELECT_LIMIT_COUNT";
	/** select 디폴트 값 */
	public static final int SELECT_SELECT_LIMIT_COUNT_VALUE = 500;
//	/** 한번에 select 할수 있는 최대 값 */
//	public static final int SELECT_SELECT_LIMIT_COUNT_MAX_PREFERENCE_VALUE = 200;
	
	/** 검색 결과 페이지 당 보여주는 갯수 */
	public static final String SELECT_RESULT_PAGE_PREFERENCE = "SELECT_RESULT_PAGE_PREFERENCE";
	/** select 결과를 페이지에 출력 디폴트 값 */
	public static final int SELECT_RESULT_PAGE_PREFERENCE_VALUE = 500;
//	/** select 결과를 페이지에 출력 최대 값 */
//	public static final int SELECT_RESULT_PAGE_MAX_PREFERENCE_VALUE = 400;
	
	/** select 쿼리 타임 아웃  */ 
	public static final String SELECT_QUERY_TIMEOUT = "SELECT_QUERY_TIMEOUT";
	/** select 쿼리 타임 아웃  디폴트 값 */
	public static final int SELECT_QUERY_TIMEOUT_VALUE = 60;
	
	//SQLFormatterPreferencePage 
		/** default tab size */
		public static final String DEFAULT_TAB_SIZE_PREFERENCE = "DEFAULT_TAB_SIZE_PREFERENCE";
		public static final String DEFAULT_TAB_SIZE_PREFERENCE_VALUE = "2";
		
		public static final String SQL_FORMATTER_DECODE_PREFERENCE = "SQL_FORMATTER_DECODE_PREFERENCE";
		public static final String SQL_FORMATTER_DECODE_PREFERENCE_VALUE = "false";
		
		public static final String SQL_FORMATTER_IN_PREFERENCE = "SQL_FORMATTER_IN_PREFERENCE";
		public static final String SQL_FORMATTER_IN_PREFERENCE_VALUE = "false";
		
		public static final String SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE 	= "SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE";
		public static final String SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE_VALUE = "true";
		
		public static final String SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE 	= "SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE";
		public static final String SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE_VALUE = "true";
		
		public static final String SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE 	= "SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE";
		public static final String SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE_VALUE = "true";
		
		public static final String SQL_FORMATTER_WORD_BREAK_PREFERENCE 	= "SQL_FORMATTER_WORD_BREAK_PREFERENCE";
		public static final String SQL_FORMATTER_WORD_BREAK_PREFERENCE_VALUE = "true";
		
		public static final String SQL_FORMATTER_WORD_WIDTH_PREFERENCE 	= "SQL_FORMATTER_WORD_WIDTH_PREFERENCE";
		public static final String SQL_FORMATTER_WORD_WIDTH_PREFERENCE_VALUE = "200";
		
	/** SESSION TIME OUT */
	public static final String SESSION_DFEAULT_PREFERENCE = "SESSION_DFEAULT_PREFERENCE";
	/** SESSION SERVER TIME OUT */
	public static final int SESSION_SERVER_DEFAULT_PREFERENCE_VALUE = 180;
	
	/** SESSION STANDALONE TIME OUT */
	public static final int SESSION_STANDALONE_DEFAULT_PREFERENCE_VALUE = 60 * 24;
	
	/** MAX SESSION TIME OUT */
	public static final int SESSION_DEFAULT_MAX_PREFERENEC_VALUE = 999999;
	
	
	/** ORACLE PLAN TABLE */
	public static final String ORACLE_PLAN_TABLE = "ORACLE_PLAN_TABLE";
	/** ORACLE PLAN TABLE VALUE */
	public static final String ORACLE_PLAN_TABLE_VALUE = "PLAN_TABLE";
	
	/** login history */
	public static final String LOGIN_HISTORY_PREFERENCE = "LOGIN_HISTORY_PREFERENCE";
	
	/** mongodb limit */
	public static final String MONGO_DEFAULT_LIMIT = "MONGO_DEFAULT_LIMIT_COUNT";
	public static final String MONGO_DEFAULT_LIMIT_VALUE = "30";
	
	/** monodb max */
	public static final String MONGO_DEFAULT_MAX_COUNT = "MONGO_DEFAULT_MAX_COUNT";
	public static final String MONGO_DEFAULT_MAX_COUNT_VALUE = "200";
	
	/** mongodb find page */ 
	public static final String MONGO_DEFAULT_FIND = "MONGO_DEFAULT_FIND_PAGE";
	public static final String MONGO_DEFAULT_FIND_BASIC = "MONGO_DEFAULT_FIND_PAGE_SEARCH";
	public static final String MONGO_DEFAULT_FIND_EXTEND = "MONGO_DEFAULT_FIND_PAGE_EXTEND";
	
	/** mongodb default result page */
	public static final String MONGO_DEFAULT_RESULT 	= "MONGO_DEFAULT_RESULT_PAGE";
	public static final String MONGO_DEFAULT_RESULT_TREE = "MONGO_DEFAULT_RESULT_PAGE_TREE";
	public static final String MONGO_DEFAULT_RESULT_TABLE = "MONGO_DEFAULT_RESULT_PAGE_TABLE";
	public static final String MONGO_DEFAULT_RESULT_TEXT = "MONGO_DEFAULT_RESULT_PAGE_TEXT";
	
	/** RDB 결과 테이블의 결과에서 ,를 붙일 것인지 지정 할 수 있도록 */
	public static final String RDB_RESULT_NUMBER_IS_COMMA = "RDB_RESULT_NUMBER_IS_COMMA";
	public static final String RDB_RESULT_NUMBER_IS_COMMA_VALUE = PublicTadpoleDefine.YES_NO.YES.name();
	
	/** RDB 결과 테이블의 폰트를 설정합니다 */
	public static final String RDB_RESULT_FONT = "RDB_RESULT_FONT";
	public static final String RDB_RESULT_FONT_VALUE = "";
	
	/** RDB COMMIT COUNT 설정합니다 */
	public static final String RDB_COMMIT_COUNT = "RDB_COMMIT_COUNT";
	public static final String RDB_COMMIT_COUNT_VALUE = "1000";
	
	/** RDB의 결과 화면에서 컬럼에서 보여지는 캐릭터 숫자. */
	public static final String RDB_CHARACTER_SHOW_IN_THE_COLUMN = "RDB_CHARACTER_SHOW_IN_THE_COLUMN";
	public static final String RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE = "-1";
	
	/** define Amazon key */ 
	public static final String AMAZON_ACCESS_NAME = "AMAZON_ACCESS_NAME";
	public static final String AMAZON_ACCESS_VALUE = "";
	
	public static final String AMAZON_SECRET_NAME = "AMAZON_SECRET_NAME";
	public static final String AMAZON_SECRET_VALUE = "";
	
	/** api server information */
		public static final String SECURITY_CREDENTIAL_USE				= "SECURITY_CREDENTIAL_USE";
		public static final String SECURITY_CREDENTIAL_USE_VALUE		= PublicTadpoleDefine.YES_NO.NO.name();
		
		public static final String SECURITY_CREDENTIAL_ACCESS_KEY 		= "SECURITY_CREDENTIAL_ACCESS_KEY";
		public static final String SECURITY_CREDENTIAL_ACCESS_KEY_VALUE = Utils.getUniqueID();
		
		public static final String SECURITY_CREDENTIAL_SECRET_KEY 		= "SECURITY_CREDENTIAL_SECRET_KEY";
		public static final String SECURITY_CREDENTIAL_SECRET_KEY_VALUE = Utils.getUniqueID();
		
	/** editor info */
	public static final String EDITOR_CHANGE_EVENT = "_EDITOR_CHANGE_EVENT_";
	
	public static final String EDITOR_AUTOSAVE 			= "EDITOR_AUTOSAVE";
	public static final String EDITOR_AUTOSAVE_VALUE	= "false";
	
	public static final String EDITOR_THEME 		= "EDITOR_THEME";
	public static final String EDITOR_THEME_VALUE	= "Crimson Editor";
	
	public static final String EDITOR_FONT_SIZE 		= "EDITOR_FONT_SIZE";
	public static final String EDITOR_FONT_SIZE_VALUE 	= "12";
	
	public static final String EDITOR_IS_WARP 		= "EDITOR_IS_WARP";
	public static final String EDITOR_IS_WARP_VALUE = "false";
	
	public static final String EDITOR_WRAP_LIMIT 		= "EDITOR_WRAP_LIMIT";
	public static final String EDITOR_WRAP_LIMIT_VALUE 	= "300";
	
	public static final String EDITOR_SHOW_GUTTER = "EDITOR_SHOW_GUTTER";
	public static final String EDITOR_SHOW_GUTTER_VALUE = "true";
	
	public static final String EDITOR_MYBatisDollart = "EDITOR_MYBatisDollart";
	public static final String EDITOR_MYBatisDollart_VALUE = "false";
		
}
