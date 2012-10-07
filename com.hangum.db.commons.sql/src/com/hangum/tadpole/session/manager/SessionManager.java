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
package com.hangum.tadpole.session.manager;

import org.apache.log4j.Logger;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.RWT;

import com.hangum.tadpole.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery;
import com.hangum.tadpole.system.TadpoleSystem_UserInfoData;

/**
 * tadpole의 session manager입니다
 * 
 * @author hangum
 *
 */
public class SessionManager {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SessionManager.class);

	/**
	 * <pre>
	 * 		MANAGER_SEQ는 그룹의 manager 권한 사용자의 seq 입니다.  seq로  그룹의 db list를 얻기위해 미리 가져옵니다.
	 * </pre>
	 * 
	 * @author hangum
	 */
	enum SESSEION_NAME {GROUP_SEQ, SEQ, GROUP_NAME, LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_NAME, LOGIN_TYPE, MANAGER_SEQ, USER_INFO_DATA}
	
	/**
	 * 신규 user의 사용자를 등록
	 * 
	 * @param groupSeq
	 * @param seq
	 * @param email
	 * @param password
	 * @param name
	 * @param userType
	 * @param managerSeq
	 */
	public static void newLogin(int groupSeq, int seq, String email, String password, String name, String userType, int managerSeq) {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		
		String groupName = "";
		try {
			groupName = TadpoleSystem_UserGroupQuery.findGroupName(groupSeq);
		} catch(Exception e) {
			logger.error("Session group name", e);
		}
		
		sStore.setAttribute(SESSEION_NAME.GROUP_SEQ.toString(), groupSeq);		
		sStore.setAttribute(SESSEION_NAME.SEQ.toString(), seq);
		sStore.setAttribute(SESSEION_NAME.GROUP_NAME.toString(), groupName);
		sStore.setAttribute(SESSEION_NAME.LOGIN_EMAIL.toString(), email);
		sStore.setAttribute(SESSEION_NAME.LOGIN_PASSWORD.toString(), password);
		sStore.setAttribute(SESSEION_NAME.LOGIN_NAME.toString(), name);
		sStore.setAttribute(SESSEION_NAME.LOGIN_TYPE.toString(), userType);
		sStore.setAttribute(SESSEION_NAME.MANAGER_SEQ.toString(), managerSeq);
	}
	
	public static int getGroupSeq() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (Integer)sStore.getAttribute(SESSEION_NAME.GROUP_SEQ.toString());
	}
	
	public static int getSeq() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		Object obj = sStore.getAttribute(SESSEION_NAME.SEQ.toString());
		
		if(obj == null) return 0;
		else return (Integer)obj;
	}
	
	public static String getGroupName() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (String)sStore.getAttribute(SESSEION_NAME.GROUP_NAME.toString());
	}
	
	public static String getEMAIL() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_EMAIL.toString());
	}
	
	public static String getPassword() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_PASSWORD.toString());
	}
	
	public static String getName() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_NAME.toString());
	}
	
	public static String getLoginType() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_TYPE.toString());
	}
	
	public static int getManagerSeq() {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		return (Integer)sStore.getAttribute(SESSEION_NAME.MANAGER_SEQ.toString());
	}

	/**
	 * 초기 접속시 프리퍼런스 정보를 로드합니다.
	 */
	public static void setUserInfos(Map<String, Object> mapUserInfo) {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		sStore.setAttribute(SESSEION_NAME.USER_INFO_DATA.toString(), mapUserInfo);		
	}
	
	/**
	 * 기존 세션 정보를 추가합니다. 
	 * @param key
	 * @param obj
	 */
	public static void setUserInfo(String key, String obj) {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		Map<String, Object> mapUserInfoData = (Map<String, Object>)sStore.getAttribute(SESSEION_NAME.USER_INFO_DATA.toString());
		UserInfoDataDAO userInfoDataDAO = (UserInfoDataDAO)mapUserInfoData.get(key);
		if(userInfoDataDAO == null) {
			userInfoDataDAO = new UserInfoDataDAO();
			userInfoDataDAO.setName(key);
			userInfoDataDAO.setUser_seq(SessionManager.getSeq());
			userInfoDataDAO.setValue0(obj);
		
			try {
				TadpoleSystem_UserInfoData.insertUserInfoData(userInfoDataDAO);
			} catch(Exception e) {
				logger.error("User data save exception [key]" + key + "[value]" + obj, e);
			}
		} else {
			userInfoDataDAO.setValue0(obj);
		}
			
		mapUserInfoData.put(key, userInfoDataDAO);
		
		sStore.setAttribute(SESSEION_NAME.USER_INFO_DATA.toString(), mapUserInfoData);
	}
	
	/**
	 * 사용자 User 정보 .
	 * 
	 * @param key
	 * @return
	 */
	public static UserInfoDataDAO getUserInfo(String key) {
		HttpSession sStore = RWT.getSessionStore().getHttpSession();
		Map<String, Object> mapUserInfoData = (Map<String, Object>)sStore.getAttribute(SESSEION_NAME.USER_INFO_DATA.toString());
		
		return (UserInfoDataDAO)mapUserInfoData.get(key);
	}
	
	/**
	 * logout 처리를 합니다.
	 */
	public static void logout() {
		try {
			HttpSession sStore = RWT.getSessionStore().getHttpSession();
			sStore.invalidate();
		} catch(Exception e) {
			// ignor exception
		}
	}
}
