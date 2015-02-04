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
package com.hangum.tadpole.sql.query;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.exception.TadpoleRuntimeException;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.Messages;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
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
	 * @return
	 * @throws Exception
	 */
	public static List<UserDAO> getAllUser() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getAllUser"); //$NON-NLS-1$
	}
	
	/**
	 * 신규 유저를 등록합니다.
	 * @param email
	 * @param passwd
	 * @param name
	 * @param approvalYn
	 * @param question
	 * @param answer
	 * @param use_opt
	 * @param opt_secret
	 * @return
	 * @throws Exception
	 */
	public static UserDAO newUser(String email, String passwd, String name, String language, String approvalYn, String use_opt, String opt_secret) throws Exception {
		UserDAO loginDAO = new UserDAO(email, name, language, approvalYn, use_opt, opt_secret);
		loginDAO.setPasswd(CipherManager.getInstance().encryption(passwd));
		
//		loginDAO.setSecurity_question(CipherManager.getInstance().encryption(question));
//		loginDAO.setSecurity_answer(CipherManager.getInstance().encryption(answer));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List isUser = sqlClient.queryForList("isUser", email); //$NON-NLS-1$
	
		if(isUser.size() == 0) {
			UserDAO userdb = (UserDAO)sqlClient.insert("newUser", loginDAO); //$NON-NLS-1$
			TadpoleSystem_UserInfoData.insertUserInfoData(userdb);
			
			return userdb;
		} else {
			throw new TadpoleRuntimeException(Messages.TadpoleSystem_UserQuery_3);
		}
	}
	
	/**
	 * 이메일이 중복된 사용자가 있는지 검사합니다.
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static boolean isDuplication(String email) throws Exception {
		
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
	 * @throws Exception
	 */
	public static List<UserDAO> findLikeUser(String email) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDAO> listUser = sqlClient.queryForList("findLikeUser", "%" + email + "%"); //$NON-NLS-1$
		
		return listUser;
	}
	
	/**
	 * 사용자 정보를 찾습니다.
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static UserDAO findUser(String email) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDAO> listUser = sqlClient.queryForList("findUser", email); //$NON-NLS-1$
		
		if(listUser.size() == 0) {
			throw new Exception(Messages.TadpoleSystem_UserQuery_0);
		}
		
		return listUser.get(0);
	}
	
	
	/**
	 * 로그인시 email, passwd 확인 
	 * 
	 * @param email
	 * @param passwd
	 * @throws Exception
	 */
	public static UserDAO login(String email, String passwd) throws Exception {
		UserDAO login = new UserDAO();
		login.setEmail(email);
		login.setPasswd(CipherManager.getInstance().encryption(passwd));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDAO userInfo = (UserDAO)sqlClient.queryForObject("login", login); //$NON-NLS-1$
	
		if(null == userInfo) {
			throw new Exception(Messages.TadpoleSystem_UserQuery_5);
		} else {
			try {
				if(!passwd.equals(CipherManager.getInstance().decryption(userInfo.getPasswd()))) {
					throw new Exception(Messages.TadpoleSystem_UserQuery_5);
				}
			} catch(Exception e) {
				throw new Exception(Messages.TadpoleSystem_UserQuery_5);
			}
		}
	
		return userInfo;
	}
	
	/**
	 * save login history
	 * 
	 * @param userSeq
	 */
	public static void saveLoginHistory(int userSeq) {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			sqlClient.insert("saveLoginHistory", userSeq);
		} catch(Exception e) {
			logger.error("save login history", e);
		}
	}
	
//	/**
//	 * check security hint
//	 *  
//	 * @param email
//	 * @param question
//	 * @param answer
//	 * @return
//	 * @throws Exception
//	 */
//	public static UserDAO checkSecurityHint(String email, String question, String answer) throws Exception {
//		UserDAO login = new UserDAO();
//		login.setEmail(email);
////		login.setSecurity_question(CipherManager.getInstance().encryption(question));
////		login.setSecurity_answer(CipherManager.getInstance().encryption(answer));
//		
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		UserDAO userInfo = (UserDAO)sqlClient.queryForObject("checkSecurityHint", login); //$NON-NLS-1$
//	
//		if(null == userInfo) {
//			throw new Exception(Messages.TadpoleSystem_UserQuery_5);
//		} else if(PublicTadpoleDefine.YES_NO.NO.toString().equals( userInfo.getApproval_yn())) { //$NON-NLS-1$
//			throw new Exception(Messages.TadpoleSystem_UserQuery_6);
//		} else {
//			if(question.equals(CipherManager.getInstance().decryption(userInfo.getSecurity_question())) &&
//					answer.equals(CipherManager.getInstance().decryption(userInfo.getSecurity_answer())) ) {
//				return userInfo;
//			} else {
//				throw new Exception(Messages.TadpoleSystem_UserQuery_5);
//			}
//		}
//	}
	
	/**
	 * get admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public static UserDAO getSystemAdmin() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserDAO)sqlClient.queryForObject("getSystemAdmin"); //$NON-NLS-1$
	}
	
	/**
	 * group의 manager 정보를 리턴합니다.
	 * 
	 * @param groupSeq
	 * @return
	 * @throws Exception
	 */
	public static UserDAO getGroupManager(int groupSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserDAO)sqlClient.queryForObject("groupManager", groupSeq); //$NON-NLS-1$
	}
	
	/**
	 * admin user가 한명이라면 로그인 화면에서 기본 유저로 설정하기 위해...
	 * 
	 * @return UserDAO
	 * @throws Exception
	 */
	public static UserDAO loginUserCount() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		Integer isUser = (Integer)sqlClient.queryForObject("loginUserCount"); //$NON-NLS-1$
	
		if(isUser == 1) {
			UserDAO userInfo = (UserDAO)sqlClient.queryForObject("onlyOnUser"); //$NON-NLS-1$
			return userInfo;
		}
	
		return null;
	}
	
	/**
	 * 사용자 전체를 리턴한다.
	 * @return
	 * @throws Exception
	 */
	public static List<UserGroupAUserDAO> getUserListPermission() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("userListPermissions"); //$NON-NLS-1$
	}
	
	/**
	 * group의 사용자를 리턴한다.
	 * @param groupSeq
	 * @return
	 * @throws Exception
	 */
	public static List<UserGroupAUserDAO> getUserListPermission(String groupSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("userListGroup", groupSeq); //$NON-NLS-1$
	}
	
	/**
	 * 유저 데이터를 수정
	 * @param user
	 * @throws Exception
	 */
	public static void updateUserData(UserDAO user) throws Exception {
//		user.setSecurity_question(CipherManager.getInstance().encryption(user.getSecurity_question()));
//		user.setSecurity_answer(CipherManager.getInstance().encryption(user.getSecurity_answer()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserPermission", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저의 패스워드 번경
	 * @param user
	 * @throws Exception
	 */
	public static void updateUserPassword(UserDAO user) throws Exception {
		user.setPasswd(CipherManager.getInstance().encryption(user.getPasswd()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserPassword", user); //$NON-NLS-1$
	}
	
	/**
	 * 사용자 힌트 변경
	 * 
	 * @param user
	 * @throws Exception
	 */
	public static void updateUserOTPCode(UserDAO user) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserOTPCode", user); //$NON-NLS-1$
	}

//	/**
//	 * 사용자 힌트 변경
//	 * 
//	 * @param user
//	 * @throws Exception
//	 */
//	public static void updateUserSecurityHint(UserDAO user) throws Exception {
//		user.setSecurity_question(CipherManager.getInstance().encryption(user.getSecurity_question()));
//		user.setSecurity_answer(CipherManager.getInstance().encryption(user.getSecurity_answer()));
//		
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		sqlClient.update("updateUserSecurityHint", user); //$NON-NLS-1$
//	}
	
	/**
	 * 사용자 정보.
	 * 
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static UserDAO getUserInfo(int userSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserDAO)sqlClient.queryForObject("getUserInfo", userSeq); //$NON-NLS-1$
	}
	
//	/**
//	 * 유저삭제
//	 * @param user
//	 * @throws Exception
//	 */
//	public static void deleteUser(UserDAO user) throws Exception {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
//		sqlClient.update("deleteUser", user); //$NON-NLS-1$
//	}

}
