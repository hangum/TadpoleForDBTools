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
package com.hangum.tadpole.preference.get;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.preference.define.AbstractPreference;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * preference의 일반적인 정보를 얻습니다.
 * 
 * @author hangum
 *
 */
public class GetPreferenceGeneral extends AbstractPreference {
	private static final Logger logger = Logger.getLogger(GetPreferenceGeneral.class);
	
	////////////////// 일반 설정 ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * session time out 
	 * @return
	 */
	public static String getSessionTimeout() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, ""+PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE);
		return userInfo.getValue0();
	}
	
	/**
	 *export delimiter
	 *
	 * @return
	 */
	public static String getExportDelimit() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EXPORT_DILIMITER, PreferenceDefine.EXPORT_DILIMITER_VALUE);
		return userInfo.getValue0();
	}
	
	/**
	 * default home page
	 * @return
	 */
	public static String getDefaultHomePage() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.DEFAULT_HOME_PAGE, PreferenceDefine.DEFAULT_HOME_PAGE_VALUE);
		return userInfo.getValue0();
	}
	
	/**
	 * default home page use
	 * @return
	 */
	public static String getDefaultHomePageUse() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.DEFAULT_HOME_PAGE_USE, PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE);
		return userInfo.getValue0();
	}
	
	////////////////// rdb 설정 ////////////////////////////////////////////////////////////////////////////
	/** rdb result type */
	public static String getResultType() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_TYPE, PreferenceDefine.RDB_RESULT_TYPE_VALUE);
		return userInfo.getValue0();		
	}
	
	/** rdb 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. */ 
	public static int getSelectLimitCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT, ""+PreferenceDefine.SELECT_SELECT_LIMIT_COUNT_VALUE);
		return Integer.parseInt( userInfo.getValue0() );		
	}
	
	/** rdb 쿼리 결과를 중에 NULL 값을 처리하는 기준 */
	public static String getResultNull() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_NULL, ""+PreferenceDefine.RDB_RESULT_NULL_VALUE);
		return userInfo.getValue0();
	}
	
	/** rdb 쿼리 결과를 page당 처리 하는 카운트 */
	public static int getPageCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, ""+PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE_VALUE);
		return Integer.parseInt( userInfo.getValue0() );
	}
	
	/** query time out */
	public static int getQueryTimeOut() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_QUERY_TIMEOUT, ""+PreferenceDefine.SELECT_QUERY_TIMEOUT_VALUE);
		return Integer.parseInt( userInfo.getValue0() );
	}
	
	/** rdb 쿼리 결과를 page당 처리 하는 카운트 */
	public static String getPlanTableName() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE, PreferenceDefine.ORACLE_PLAN_TABLE_VALUE);
		return userInfo.getValue0();
	}
	
	/**
	 * RDB ResultSet font info
	 * @return
	 */
	public static String getRDBResultFont() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_FONT, PreferenceDefine.RDB_RESULT_FONT_VALUE);
		return userInfo.getValue0();
	}
	
	/**
	 * RDB Commit count
	 * @return
	 */
	public static String getRDBCommitCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_COMMIT_COUNT, PreferenceDefine.RDB_COMMIT_COUNT_VALUE);
		return userInfo.getValue0();
	}
	
	/**
	 * RDB Character shown in the column
	 * @return
	 */
	public static String getRDBShowInTheColumn() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE);
		return userInfo.getValue0();
	}
	
	///////////////// sql formatter 설정 ////////////////////////////////////////////////////////////////////////////
		/** tab size */	
		public static String getDefaultTabSize() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE, PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatDecode() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE, PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatIn() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE, PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatNewLineBeforeAndOr() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE, PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatNewLineBeforeComma() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE, PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatRemoveEmptyLine() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE, PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatWordBreak() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE, PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatWordWidth() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE, PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE_VALUE);
			return userInfo.getValue0();
		}
	
	///////////////// mongodb 설정 ////////////////////////////////////////////////////////////////////////////
		/** preference mongodb default limit */
		public static String getMongoDefaultLimit() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_LIMIT, PreferenceDefine.MONGO_DEFAULT_LIMIT_VALUE);
			return userInfo.getValue0();		
		}
		
		/** preference mongodb default max count */
		public static int getMongoDefaultMaxCount() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT, PreferenceDefine.MONGO_DEFAULT_MAX_COUNT_VALUE);
			return Integer.parseInt( userInfo.getValue0() );
		}
		
		/** preference mongodb default find page */
		public static String getMongoDefaultFindPage() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_FIND, PreferenceDefine.MONGO_DEFAULT_FIND_BASIC);
			return userInfo.getValue0();
		}
		
		/** preference mongodb default result page */
		public static String getMongoDefaultResultPage() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_RESULT, PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE);
			return userInfo.getValue0();
		}

		/** preference RDB ResultSet number column add comma? */
		public static String getRDBNumberISComma() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA, PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA_VALUE);
			return userInfo.getValue0();
		}
		
		/**
		 * BOOLEAN is RDB ResultSet number column add comma
		 * @return
		 */
		public static boolean getISRDBNumberIsComma() {
			return getRDBNumberISComma().equals(PublicTadpoleDefine.YES_NO.YES.name())?true:false;
		}
		
		public static boolean getSyncEditorStat() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SYNC_EIDOTR_STATS, PreferenceDefine.SYNC_EIDOTR_STATS_VALUE);
			return Boolean.parseBoolean(userInfo.getValue0());
		}

	///////////////// edito setting ////////////////////////////////////////////////////////////////////////////
	public static boolean getEditorAutoSave() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_AUTOSAVE, PreferenceDefine.EDITOR_AUTOSAVE_VALUE);
		return Boolean.parseBoolean(userInfo.getValue0());
	}
		
	public static String getEditorTheme() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_THEME, PreferenceDefine.EDITOR_THEME_VALUE);
		return userInfo.getValue0();
	}
	
	public static String getEditorFontSize() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_FONT_SIZE, PreferenceDefine.EDITOR_FONT_SIZE_VALUE);
		return userInfo.getValue0();
	}
	
	public static boolean getEditorIsWarp() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_IS_WARP, PreferenceDefine.EDITOR_IS_WARP_VALUE);
		return Boolean.parseBoolean(userInfo.getValue0());
	}
	
	public static String getEditorWarpLimitValue() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_WRAP_LIMIT, PreferenceDefine.EDITOR_WRAP_LIMIT_VALUE);
		return userInfo.getValue0();
	}
	
	public static boolean getEditorShowGutter() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_SHOW_GUTTER, PreferenceDefine.EDITOR_SHOW_GUTTER_VALUE);
		return Boolean.parseBoolean(userInfo.getValue0());
	}
	public static boolean getIsMyBatisDollor() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EDITOR_MYBatisDollart, PreferenceDefine.EDITOR_MYBatisDollart_VALUE);
		return Boolean.parseBoolean(userInfo.getValue0());
	}
	
}
