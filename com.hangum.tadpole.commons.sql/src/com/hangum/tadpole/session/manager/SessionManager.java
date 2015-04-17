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
package com.hangum.tadpole.session.manager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;

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
	public static enum NAME {	
								/* 자신의 유저 seq */		USER_SEQ, 
														LOGIN_EMAIL, 
														LOGIN_PASSWORD, 
														LOGIN_NAME, 
								/* 대표적인 권한 타입 */		REPRESENT_ROLE_TYPE, 
														USER_INFO_DATA,
														
														USE_OTP, OTP_SECRET_KEY,
														
														UNLOCK_DB_LIST,
														
														PERSPECTIVE
														}

	/**
	 * is login?
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		if(getUserSeq() == 0) return false;
		else return true;
	}
	
	/**
	 * Update session information.<br>
	 * <br>
	 * Session uses the information in multiple places(preference, user info etc.). 
	 * So when updating the information stored in the Session, 
	 * you must update the information given session.
	 * 
	 * @param key Session Attribute name
	 * @param value Object
	 */
	public static void updateSessionAttribute(String key, Object value) {
		HttpSession sStore = RWT.getRequest().getSession();
		sStore.setAttribute(key, value);
	}
	
	/**
	 * 사용자를 session에 등록
	 * 
	 * @param loginUserDao
	 */
	public static void addSession(UserDAO loginUserDao) {
		HttpSession sStore = RWT.getRequest().getSession();
		sStore.setAttribute(NAME.REPRESENT_ROLE_TYPE.toString(), loginUserDao.getRole_type());
		sStore.setAttribute(NAME.USER_SEQ.toString(), loginUserDao.getSeq());
		sStore.setAttribute(NAME.LOGIN_EMAIL.toString(), loginUserDao.getEmail());
		sStore.setAttribute(NAME.LOGIN_PASSWORD.toString(), loginUserDao.getPasswd());
		sStore.setAttribute(NAME.LOGIN_NAME.toString(), loginUserDao.getName());
		sStore.setAttribute(NAME.PERSPECTIVE.toString(), "default");
		
		sStore.setAttribute(NAME.USE_OTP.toString(), loginUserDao.getUse_otp());
		sStore.setAttribute(NAME.OTP_SECRET_KEY.toString(), loginUserDao.getOtp_secret());
		
		sStore.setAttribute(NAME.UNLOCK_DB_LIST.name(), new ArrayList<Integer>());
	}
	
	/**
	 * set password
	 * 
	 * @param strPasswd
	 */
	public static void setPassword(String strPasswd) {
		HttpSession sStore = RWT.getRequest().getSession();
		sStore.setAttribute(NAME.LOGIN_PASSWORD.toString(), strPasswd);
	}
	
	public static void setUesrSeq(int seq) {
		HttpSession sStore = RWT.getRequest().getSession();
		sStore.setAttribute(NAME.USER_SEQ.toString(), seq);
	}
	public static int getUserSeq() {
		HttpSession sStore = RWT.getRequest().getSession();
		Object obj = sStore.getAttribute(NAME.USER_SEQ.toString());
		
		if(obj == null) return 0;
		else return (Integer)obj;
	}
	
	public static String getEMAIL() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.LOGIN_EMAIL.toString());
	}
	
	public static String getPassword() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.LOGIN_PASSWORD.toString());
	}
	
	public static String getName() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.LOGIN_NAME.toString());
	}
	
	public static String getUseOTP() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.USE_OTP.toString());
	}
	public static String getOTPSecretKey() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.OTP_SECRET_KEY.toString());
	}
	
	/**
	 * 자신이 대표 권한을 리턴합니다.
	 * 
	 * <pre>
	 * 권한 중복일 경우
	 * admin이면서 manager일수는 없습니다. 
	 * 	1) admin
	 *  2) manager 
	 *  3) dba 
	 *  4) user
	 * 
	 * group당 manager권한은 반듯이 하나입니다.
	 * manager권한이 정지되면 그룹을 수정 못하는 것으로.
	 * </pre>
	 * 
	 * @return
	 */
	public static String getRepresentRole() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.REPRESENT_ROLE_TYPE.toString());
	}
	
	public static boolean isAdmin() {
		return PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN.toString().equals(getRepresentRole()) ? true : false;
	}

	/**
	 * 초기 접속시 사용자의 모든 프리퍼런스 데이터를 설정합니다. 
	 */
	public static void setUserAllPreferenceData(Map<String, Object> mapUserInfo) {
		HttpSession sStore = RWT.getRequest().getSession();
		sStore.setAttribute(NAME.USER_INFO_DATA.toString(), mapUserInfo);		
	}
	
	/**
	 * 기존 세션 정보를 추가합니다. 
	 * @param key
	 * @param obj
	 */
	public static void setUserInfo(String key, String obj) {
		
		HttpSession sStore = RWT.getRequest().getSession();
		Map<String, Object> mapUserInfoData = (Map<String, Object>)sStore.getAttribute(NAME.USER_INFO_DATA.toString());
		UserInfoDataDAO userInfoDataDAO = (UserInfoDataDAO)mapUserInfoData.get(key);
		if(userInfoDataDAO == null) {
			userInfoDataDAO = new UserInfoDataDAO();
			userInfoDataDAO.setName(key);
			userInfoDataDAO.setUser_seq(SessionManager.getUserSeq());
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
		
		sStore.setAttribute(NAME.USER_INFO_DATA.toString(), mapUserInfoData);
	}
	
	/**
	 * 사용자 User 정보 .
	 * 
	 * @param key
	 * @return
	 */
	public static UserInfoDataDAO getUserInfo(String key) {
		HttpSession sStore = RWT.getRequest().getSession();
		Map<String, Object> mapUserInfoData = (Map<String, Object>)sStore.getAttribute(NAME.USER_INFO_DATA.toString());
		
		return (UserInfoDataDAO)mapUserInfoData.get(key);
	}
	
	/**
	 * set unlock db list
	 * @param userDB
	 * @return
	 */
	public static boolean setUnlokDB(final UserDBDAO userDB) {
		HttpSession sStore = RWT.getRequest().getSession();
		List<Integer> listUnlockDB = (List)sStore.getAttribute(NAME.UNLOCK_DB_LIST.name());
		
		return listUnlockDB.add(userDB.getSeq());
	}
	
	/**
	 * is unlock db 
	 * @param userDB
	 * @return
	 */
	public static boolean isUnlockDB(final UserDBDAO userDB) {
		HttpSession sStore = RWT.getRequest().getSession();
		List<Integer> listUnlockDB = (List)sStore.getAttribute(NAME.UNLOCK_DB_LIST.name());
		
		return listUnlockDB.contains(userDB.getSeq());
	}
	
	/**
	 * logout 처리를 합니다.
	 */
	public static void logout() {
		try {
			HttpSession sStore = RWT.getRequest().getSession();			
			sStore.setAttribute(NAME.USER_SEQ.toString(), 0);
			sStore.invalidate();
	     
	     	String browserText = MessageFormat.format("parent.window.location.href = \"{0}\";", "");
	     	JavaScriptExecutor executor = RWT.getClient().getService( JavaScriptExecutor.class );
	     	executor.execute("setTimeout('"+browserText+"', 50)" );
		} catch(Exception e) {
			// ignore exception
		}
	}
	
	/**
	 * 사용자 session을 invalidate시킵니다.
	 */
	public static void invalidate() {
		try {
			HttpSession sStore = RWT.getRequest().getSession();
			
			HttpSessionContext hsc = sStore.getSessionContext();
			Enumeration ids = hsc.getIds();
			while(ids.hasMoreElements()) {
				String id = (String)ids.nextElement();
				
				if(logger.isDebugEnabled()) logger.debug("==========================> " + hsc.getSession(id));
			}
			
		} catch(Exception e) {
			logger.error("user session invalidate", e);
		}
	}
	
	public static String getPerspective() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(NAME.PERSPECTIVE.toString());
		return userInfo == null ? "" : userInfo.getValue0();
	}
	
	public static void setPerspective(String persp) { 
		// db update 
		try {
			TadpoleSystem_UserInfoData.updateUserInfoData(NAME.PERSPECTIVE.toString(), persp);
			// session update
			SessionManager.setUserInfo(NAME.PERSPECTIVE.toString(), persp);
			SessionManager.resetPerspective();
		} catch (Exception e) {
			logger.error("Error change perspective", e);
		}
	}
	
	public static void resetPerspective() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			window.getActivePage().resetPerspective();	
		}
	}
}
