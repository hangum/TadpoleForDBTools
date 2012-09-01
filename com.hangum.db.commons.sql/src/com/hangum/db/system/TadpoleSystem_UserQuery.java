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
package com.hangum.db.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.Messages;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.system.UserDAO;
import com.hangum.db.dao.system.ext.UserGroupAUserDAO;
import com.hangum.db.define.Define;
import com.hangum.db.exception.TadpoleRuntimeException;
import com.ibatis.sqlmap.client.SqlMapClient;


/**
 * tadpole system query를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserQuery {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserQuery.class);
	
	/**
	 * 신규 user를 추가합니다.
	 * 
	 * @param email
	 * @param passwd
	 * @param name
	 * @throws Exception
	 */
	public static UserDAO newUser(int groupSeq, String email, String passwd, String name, Define.USER_TYPE userType) throws Exception {
		return newUser(groupSeq, email, passwd, name, userType.toString());
	}
	
	/**
	 * 신규 유저를 등록합니다.
	 * @param groupSeq
	 * @param email
	 * @param passwd
	 * @param name
	 * @param type
	 * @param approvalYn
	 * @return
	 * @throws Exception
	 */
	public static UserDAO newUser(int groupSeq, String email, String passwd, String name, String type, String approvalYn) throws Exception {
		UserDAO loginDAO = new UserDAO(groupSeq, email, passwd, name, type, approvalYn);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		List isUser = sqlClient.queryForList("isUser", email); //$NON-NLS-1$
	
		if(isUser.size() == 0) {
			UserDAO userdb = (UserDAO)sqlClient.insert("newUser", loginDAO); //$NON-NLS-1$
			TadpoleSystem_UserInfoData.insertUserInfoData(userdb);
			
			return userdb;
		} else {
			throw new TadpoleRuntimeException(Messages.TadpoleSystem_UserQuery_2);
		}
	}
	
	/**
	 * 신규 유저를 등록합니다.
	 * @param email
	 * @param pass
	 * @param name
	 * @param type user-type
	 */
	public static UserDAO newUser(int groupSeq, String email, String passwd, String name, String type) throws Exception {
		return newUser(groupSeq, email, passwd, name, type, Define.YES_NO.NO.toString());
	}
	
	
	/**
	 * 이메일이 중복된 사용자가 있는지 검사합니다.
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static boolean isDuplication(String email) throws Exception {
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		List isUser = sqlClient.queryForList("isUser", email); //$NON-NLS-1$
	
		if(isUser.size() == 0) {
			return true;
		}
			
		return false;
	}
	
	
	/**
	 * 로그인시 email, passwd 확인 
	 * 
	 * @param email
	 * @param passwd
	 * @throws Exception
	 */
	public static UserDAO login(String email, String passwd) throws Exception {
		UserDAO login = new UserDAO(email, passwd);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		UserDAO userInfo = (UserDAO)sqlClient.queryForObject("login", login); //$NON-NLS-1$
	
		if(null == userInfo) {
			throw new Exception(Messages.TadpoleSystem_UserQuery_9);
		} else if("NO".equals( userInfo.getApproval_yn())) { //$NON-NLS-1$
			throw new Exception(Messages.TadpoleSystem_UserQuery_1);
		}
	
		return userInfo;
	}
	
	/**
	 * group의 manager 정보를 리턴합니다.
	 * 
	 * @param groupSeq
	 * @return
	 * @throws Exception
	 */
	public static UserDAO getGroupManager(int groupSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return (UserDAO)sqlClient.queryForObject("groupManager", groupSeq); //$NON-NLS-1$
	}
	
	/**
	 * admin user가 한명이라면 로그인 화면에서 기본 유저로 설정하기 위해...
	 * 
	 * @return UserDAO
	 * @throws Exception
	 */
	public static UserDAO loginUserCount() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return sqlClient.queryForList("userListPermissions"); //$NON-NLS-1$
	}
	
	/**
	 * group의 사용자를 리턴한다.
	 * @param groupSeq
	 * @return
	 * @throws Exception
	 */
	public static List<UserGroupAUserDAO> getUserListPermission(int groupSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return sqlClient.queryForList("userListGroup", groupSeq); //$NON-NLS-1$
	}
	
	/**
	 * 유저 데이터를 수정
	 * @param user
	 * @throws Exception
	 */
	public static void updateUserData(UserDAO user) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.update("updateUserPermission", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저의 패스워드 번경
	 * @param user
	 * @throws Exception
	 */
	public static void updateUserPassword(UserDAO user) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.update("updateUserPassword", user); //$NON-NLS-1$
	}
	
	/**
	 * 유저삭제
	 * @param user
	 * @throws Exception
	 */
	public static void deleteUser(UserDAO user) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.update("deleteUser", user); //$NON-NLS-1$
	}

}
