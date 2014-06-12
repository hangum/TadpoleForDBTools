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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.mails.dto.SMTPDTO;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;

/**
 * preference의 일반적인 정보를 얻습니다.
 * 
 * @author hangum
 *
 */
public class GetPreferenceGeneral {
	private static final Logger logger = Logger.getLogger(GetPreferenceGeneral.class);
	
	////////////////// 일반 설정 ///////////////////////////////////////////////////////////////////////////
	
	public static String getValue(String key, Object defaultValue) {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(key);
		if(null == userInfo) return defaultValue.toString();
		else if("".equals(userInfo.getValue0())) return defaultValue.toString();
		
		return userInfo.getValue0();
	}
	
	/**
	 * 
	 * @return
	 */
	public static SMTPDTO getSessionSMTPINFO() {
		SMTPDTO dto = new SMTPDTO();
		
		HttpSession sStore = RWT.getRequest().getSession();
		dto = (SMTPDTO)sStore.getAttribute("smtpinfo");
		
		if(dto == null) {
			dto = new SMTPDTO();
			
			try {
				UserDAO userDao = TadpoleSystem_UserQuery.getAdmin();
				List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData(userDao.getSeq());
				Map<String, UserInfoDataDAO> mapUserInfoData = new HashMap<String, UserInfoDataDAO>();
				for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
					mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
				}
			
				dto.setHost(getValue(mapUserInfoData, PreferenceDefine.SMTP_HOST_NAME, PreferenceDefine.SMTP_HOST_NAME_VALUE));
				dto.setPort(getValue(mapUserInfoData, PreferenceDefine.SMTP_PORT, PreferenceDefine.SMTP_PORT_VALUE));
				dto.setEmail(getValue(mapUserInfoData, PreferenceDefine.SMTP_EMAIL, PreferenceDefine.SMTP_EMAIL_VALUE));
				dto.setPasswd(getValue(mapUserInfoData, PreferenceDefine.SMTP_PASSWD, PreferenceDefine.SMTP_PASSWD_VALUE));
				
				sStore.setAttribute("smtpinfo", dto);
			} catch (Exception e) {
				logger.error("get stmt info", e);
			}
		}
		
		return dto;
	}
	
	public static SMTPDTO getSMTPINFO() {
		SMTPDTO dto = new SMTPDTO();
		
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.getAdmin();
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
			logger.error("get stmt info", e);
		}
		
		return dto;
	}
	
	private static String getValue(Map<String, UserInfoDataDAO> mapUserInfoData, String key, String defaultValue) {
		UserInfoDataDAO userInfoDao = mapUserInfoData.get(key);
		if(null == userInfoDao) return defaultValue;
		else return userInfoDao.getValue0();
	}
	
	/**
	 * session time out 
	 * @return
	 */
	public static String getSessionTimeout() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		return userInfo.getValue0();
	}
	
	/**
	 *export dilimit
	 *
	 * @return
	 */
	public static String getExportDelimit() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.EXPORT_DILIMITER);
		if(null == userInfo) return PreferenceDefine.EXPORT_DILIMITER_VALUE;
		else if("".equals(userInfo.getValue0())) return PreferenceDefine.EXPORT_DILIMITER_VALUE;
		
		return userInfo.getValue0();
	}
	
	/**
	 * default home page
	 * @return
	 */
	public static String getDefaultHomePage() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.DEFAULT_HOME_PAGE);
		if(null == userInfo) return PreferenceDefine.DEFAULT_HOME_PAGE_VALUE;
		else if("".equals(userInfo.getValue0())) return PreferenceDefine.DEFAULT_HOME_PAGE_VALUE;
		
		return userInfo.getValue0();
	}
	
	/**
	 * default home page use
	 * @return
	 */
	public static String getDefaultHomePageUse() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.DEFAULT_HOME_PAGE_USE);
		if(null == userInfo) return PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE;
		else if("".equals(userInfo.getValue0())) return PreferenceDefine.DEFAULT_HOME_PAGE_USE_VALUE;
		
		return userInfo.getValue0();
	}
	
	////////////////// rdb 설정 ////////////////////////////////////////////////////////////////////////////
	/** rdb 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. */ 
	public static int getQueryResultCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT);
		return Integer.parseInt( userInfo.getValue0() );		
	}
	
	/** rdb 쿼리 결과를 page당 처리 하는 카운트 */
	public static int getPageCount() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE);
		return Integer.parseInt( userInfo.getValue0() );
	}
	
	/** query time out */
	public static int getQueryTimeOut() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SELECT_QUERY_TIMEOUT);
		if(null == userInfo) return PreferenceDefine.SELECT_QUERY_TIMEOUT_VALUE;
		return Integer.parseInt( userInfo.getValue0() );
	}
	
	/** rdb 쿼리 결과를 page당 처리 하는 카운트 */
	public static String getPlanTableName() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE);
		return userInfo.getValue0();
	}
	
	/**
	 * RDB ResultSet font info
	 * @return
	 */
	public static String getRDBResultFont() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_FONT);
		return userInfo.getValue0();
	}
	
	///////////////// sql formatter 설정 ////////////////////////////////////////////////////////////////////////////
		/** tab size */	
		public static String getDefaultTabSize() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.DEFAULT_TAB_SIZE_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatDecode() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_DECODE_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatIn() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_IN_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatNewLineBeforeAndOr() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_AND_OR_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatNewLineBeforeComma() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_NEWLINE_BEFAORE_COMMA_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatRemoveEmptyLine() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_REMOVE_EMPTY_LINE_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatWordBreak() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_WORD_BREAK_PREFERENCE);
			return userInfo.getValue0();
		}
		
		public static String getSQLFormatWordWidth() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.SQL_FORMATTER_WORD_WIDTH_PREFERENCE);
			return userInfo.getValue0();
		}
	
	///////////////// mongodb 설정 ////////////////////////////////////////////////////////////////////////////
		/** preference mongodb default limit */
		public static String getMongoDefaultLimit() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_LIMIT);
			return userInfo.getValue0();		
		}
		
		/** preference mongodb default max count */
		public static int getMongoDefaultMaxCount() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
			return Integer.parseInt( userInfo.getValue0() );
		}
		
		/** preference mongodb default find page */
		public static String getMongoDefaultFindPage() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_FIND);
			return userInfo.getValue0();
		}
		
		/** preference mongodb default result page */
		public static String getMongoDefaultResultPage() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.MONGO_DEFAULT_RESULT);
			return userInfo.getValue0();
		}

		/** preference RDB ResultSet number column add comma? */
		public static String getRDBNumberISComma() {
			UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_RESULT_NUMBER_IS_COMMA);
			String strYesNo = "No";
			if (userInfo!=null){
				strYesNo = userInfo.getValue0();
				if(null == strYesNo || "".equals(strYesNo)) {
					return PublicTadpoleDefine.YES_NO.YES.toString();
				}
			}
			
			return strYesNo;
		}
		
		/**
		 * BOOLEAN is RDB ResultSet number column add comma
		 * @return
		 */
		public static boolean getISRDBNumberIsComma() {
			return getRDBNumberISComma().equals(PublicTadpoleDefine.YES_NO.YES.toString())?true:false;
		}
		
}
