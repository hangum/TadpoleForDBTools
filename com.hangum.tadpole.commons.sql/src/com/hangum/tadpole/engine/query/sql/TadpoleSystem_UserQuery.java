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
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.csv.DateUtil;
import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;
import com.hangum.tadpole.commons.exception.TadpoleRuntimeException;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserLoginHistoryDAO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.ibatis.sqlmap.client.SqlMapClient;


/**
 * Define User query.
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserQuery {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserQuery.class);
	
	/**
	 * 모든 유효한 유저 목록을 가져옵니다.
	 * 
	 * @param delyn
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserDAO> getAllUser() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getAllUser"); //$NON-NLS-1$
	}
	
	/**
	 * 모든 유효한 유저 목록을 가져옵니다.
	 * 
	 * @param delyn
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserDAO> getLiveAllUser() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getLiveAllUser"); //$NON-NLS-1$
	}
	
	/**
	 * 신규 유저를 등록합니다.
	 * 
	 * @param inputType
	 * @param email
	 * @param email_key
	 * @param is_email_certification
	 * @param passwd
	 * @param roleType
	 * @param name
	 * @param language
	 * @param timezone
	 * @param approvalYn
	 * @param use_otp
	 * @param otp_secret
	 * @param strAllowIP
	 * @param strIsRegistDb
	 * @param strIsSharedDb
	 * @param intLimitAddDBCnt
	 * @param serviceStart
	 * @param serviceEnd
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO newUser(String inputType, String email, String email_key, String is_email_certification, String passwd, 
								String roleType, String name, String language, String timezone, String approvalYn, String use_otp, String otp_secret,
								String strAllowIP
	) throws TadpoleSQLManagerException, SQLException {
		UserDAO loginDAO = new UserDAO();
		loginDAO.setInput_type(inputType);
		loginDAO.setEmail(email);
		loginDAO.setEmail_key(email_key);
		loginDAO.setIs_email_certification(is_email_certification);
		
		loginDAO.setPasswd(CipherManager.getInstance().encryption(passwd));
		loginDAO.setRole_type(roleType);
		
		loginDAO.setName(name);
		loginDAO.setLanguage(language);
		loginDAO.setTimezone(timezone);
		loginDAO.setApproval_yn(approvalYn);
		loginDAO.setUse_otp(use_otp);
		loginDAO.setOtp_secret(otp_secret);
		loginDAO.setAllow_ip(strAllowIP);
		
		loginDAO.setIs_regist_db(GetAdminPreference.getIsAddDB());
		loginDAO.setIs_shared_db(GetAdminPreference.getIsAddDB());
		loginDAO.setLimit_add_db_cnt(NumberUtils.toInt(GetAdminPreference.getDefaultAddDBCnt()));
		loginDAO.setService_start(new Timestamp(System.currentTimeMillis()));
		loginDAO.setService_end(new Timestamp(DateUtil.afterMonthToMillis(NumberUtils.toInt(GetAdminPreference.getServiceDurationDay()))));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List isUser = sqlClient.queryForList("isUser", email); //$NON-NLS-1$
	
		if(isUser.isEmpty()) {
			UserDAO userdb = (UserDAO)sqlClient.insert("newUser", loginDAO); //$NON-NLS-1$
			TadpoleSystem_UserInfoData.initializeUserPreferenceData(userdb);
			
			return userdb;
		} else {
			throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserQuery_3);
		}
	}
	
	/**
	 * 이메일이 중복된 사용자가 있는지 검사합니다.
	 * 
	 * @param email
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static boolean isDuplication(String email) throws TadpoleSQLManagerException, SQLException {
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List isUser = sqlClient.queryForList("isUser", email); //$NON-NLS-1$
	
		if(isUser.size() == 0) {
			return true;
		}
			
		return false;
	}
	
	/**
	 * search like email 
	 * @param email
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserDAO> findLikeUser(String email) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDAO> listUser = sqlClient.queryForList("findLikeUser", "%" + email + "%"); //$NON-NLS-1$
		
		return listUser;
	}
	
	/**
	 * 사용자 정보를 찾습니다.
	 * 
	 * @param email
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO findUser(String email) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDAO> listUser = sqlClient.queryForList("findUser", email); //$NON-NLS-1$
		
		if(listUser.size() == 0) {
			throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserQuery_0);
		}
		
		return listUser.get(0);
	}
	
	
	/**
	 * 로그인시 email, passwd 확인 
	 * 
	 * @param email
	 * @param passwd
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO login(String email, String passwd) throws TadpoleAuthorityException, TadpoleSQLManagerException, SQLException {
		UserDAO login = new UserDAO();
		login.setEmail(email);
		login.setPasswd(CipherManager.getInstance().encryption(passwd));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDAO userInfo = (UserDAO)sqlClient.queryForObject("login", login); //$NON-NLS-1$
	
		if(null == userInfo) {
			throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserQuery_5);
		} else {
			if(!passwd.equals(CipherManager.getInstance().decryption(userInfo.getPasswd()))) {
				throw new TadpoleAuthorityException(Messages.get().TadpoleSystem_UserQuery_5);
			}
		}
	
		return userInfo;
	}
	
	/**
	 * update email confirm
	 * 
	 * @param email
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateEmailConfirm(String email) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateEmailConfirm", email);
	}
	
	/**
	 * save login history
	 * 
	 * @param userSeq
	 */
	public static void saveLoginHistory(int userSeq) {
		try {
			UserLoginHistoryDAO historyDao = new UserLoginHistoryDAO();
			historyDao.setLogin_ip(RWT.getRequest().getRemoteAddr());
			historyDao.setUser_seq(userSeq);
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			sqlClient.insert("saveLoginHistory", historyDao);
		} catch(Exception e) {
			logger.error("save login history", e);
		}
	}
	
	/**
	 * get login history
	 * 
	 * @param strEmail
	 * @param startTime
	 * @param endTime
	 */
	public static List<UserLoginHistoryDAO> getLoginHistory(String strEmail, long startTime, long endTime) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("email",		strEmail);
		
		if(ApplicationArgumentUtils.isDBServer()) {
			Date dateSt = new Date(startTime);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			queryMap.put("startTime",  formatter.format(dateSt));
			
			Date dateEd = new Date(endTime);
			queryMap.put("endTime", formatter.format(dateEd));			
		} else {
			Date dateSt = new Date(startTime);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			
			queryMap.put("startTime",  formatter.format(dateSt));
			
			Date dateEd = new Date(endTime);
			queryMap.put("endTime", formatter.format(dateEd));
		}
		
		return (List<UserLoginHistoryDAO>)sqlClient.queryForList("getLoginHistory", queryMap);
	}
	
	/**
	 * get admin
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO getSystemAdmin() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserDAO)sqlClient.queryForObject("getSystemAdmin"); //$NON-NLS-1$
	}
	
	/**
	 * group의 manager 정보를 리턴합니다.
	 * 
	 * @param groupSeq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO getGroupManager(int groupSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserDAO)sqlClient.queryForObject("groupManager", groupSeq); //$NON-NLS-1$
	}
	
	/**
	 * admin user가 한명이라면 로그인 화면에서 기본 유저로 설정하기 위해...
	 * 
	 * @return UserDAO
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO loginUserCount() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		Integer isUser = (Integer)sqlClient.queryForObject("loginUserCount"); //$NON-NLS-1$
	
		if(isUser == 1) {
			UserDAO userInfo = (UserDAO)sqlClient.queryForObject("onlyOnUser"); //$NON-NLS-1$
			return userInfo;
		}
	
		return null;
	}
	
	/**
	 * 개인 사용자가 그룹 사용자로 수정
	 * 
	 * @param user
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserPersonToGroup(UserDAO user) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		user.setPasswd(CipherManager.getInstance().encryption(user.getPasswd()));
		sqlClient.update("updateUserPersonToGroup", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저 데이터를 수정
	 * @param user
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserData(UserDAO user) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserPermission", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저의 기본정보를 수정
	 * @param user
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserBasic(UserDAO user) throws TadpoleSQLManagerException, SQLException {
		user.setPasswd(CipherManager.getInstance().encryption(user.getPasswd()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserBasic", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저의 패스워드 번경
	 * @param user
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserPassword(UserDAO user) throws TadpoleSQLManagerException, SQLException {
		user.setPasswd(CipherManager.getInstance().encryption(user.getPasswd()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserPassword", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저의 패스워드 번경
	 * @param user
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserPasswordWithID(UserDAO user) throws TadpoleSQLManagerException, SQLException {
		user.setPasswd(CipherManager.getInstance().encryption(user.getPasswd()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserPasswordWithID", user); //$NON-NLS-1$
	}
	
	/**
	 * 사용자 힌트 변경
	 * 
	 * @param user
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserOTPCode(UserDAO user) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserOTPCode", user); //$NON-NLS-1$
	}
	
	/**
	 * 사용자 정보.
	 * 
	 * @param userSeq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDAO getUserInfo(int userSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserDAO)sqlClient.queryForObject("getUserInfo", userSeq); //$NON-NLS-1$
	}

}
