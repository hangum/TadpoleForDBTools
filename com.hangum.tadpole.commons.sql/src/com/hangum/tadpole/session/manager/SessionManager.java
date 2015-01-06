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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserInfoData;

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
//								/* 자신의 메니저 그룹 */		GROUP_SEQ, 
//								/* 자신이 속한 그룹종류 */	GROUP_SEQS, 
								/* 자신의 유저 seq */		USER_SEQ, 
														LOGIN_EMAIL, 
														LOGIN_PASSWORD, 
														LOGIN_NAME, 
								/* 대표적인 권한 타입 */		REPRESENT_ROLE_TYPE, 
//								/* 자신의 모든 롤 타입 */	ROLE_TYPE, 
														USER_INFO_DATA,
														
														USE_OTP, OTP_SECRET_KEY,
														
														SECURITY_QUESTION,
														SECURITY_ANSWER, PERSPECTIVE}

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
		
//		Map<Integer, String> mapRoleType = new HashMap<Integer, String>();
//		// 내가 속한 모든 그룹 순번이고, 이것은 사용할수 있는 디비를 조회하는 용도로 사용하기 위해 세션에 입력합니다.
//		String strGroupSeqs = "";
//		
//		try {
//			Map<Integer, String> mapUserRole = new HashMap<Integer, String>();
//			for (UserRoleDAO userRoleDAO : TadpoleSystem_UserRole.findUserRole(loginUserDao)) {
//				mapUserRole.put(userRoleDAO.getGroup_seq(), userRoleDAO.getRole_type());
//				
//				if(PublicTadpoleDefine.USER_TYPE.ADMIN.toString().equals(userRoleDAO.getRole_type())) {
//					sStore.setAttribute(NAME.GROUP_SEQ.toString(), userRoleDAO.getGroup_seq());
//				} else if(PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(userRoleDAO.getRole_type())) {
//					sStore.setAttribute(NAME.GROUP_SEQ.toString(), userRoleDAO.getGroup_seq());
//				}
//				
//				strGroupSeqs += userRoleDAO.getGroup_seq() + ",";
//			}
//
//			strGroupSeqs = StringUtils.removeEnd(strGroupSeqs, ",");
//			sStore.setAttribute(NAME.GROUP_SEQS.toString(), strGroupSeqs);
//			
////			// session 에 등록.
//			sStore.setAttribute(NAME.ROLE_TYPE.toString(), mapUserRole);
//			
//			// 본래 자신의  role을 넣습니다.
//			UserRoleDAO representUserRole = TadpoleSystem_UserRole.representUserRole(loginUserDao);
			sStore.setAttribute(NAME.REPRESENT_ROLE_TYPE.toString(), loginUserDao.getRole_type());
//			
//		} catch(Exception e) {
//			logger.error("find user rold", e);
//		}
		
//		sStore.setAttribute(SESSEION_NAME.GROUP_SEQ.toString(), groupSeq);		
		sStore.setAttribute(NAME.USER_SEQ.toString(), loginUserDao.getSeq());
//		sStore.setAttribute(SESSEION_NAME.GROUP_NAME.toString(), groupName);
		sStore.setAttribute(NAME.LOGIN_EMAIL.toString(), loginUserDao.getEmail());
		sStore.setAttribute(NAME.LOGIN_PASSWORD.toString(), loginUserDao.getPasswd());
		sStore.setAttribute(NAME.LOGIN_NAME.toString(), loginUserDao.getName());
//		sStore.setAttribute(NAME.SECURITY_ANSWER.toString(), loginUserDao.getSecurity_answer());
//		sStore.setAttribute(NAME.SECURITY_QUESTION.toString(), loginUserDao.getSecurity_question());
		sStore.setAttribute(NAME.PERSPECTIVE.toString(), "default");
		
		sStore.setAttribute(NAME.USE_OTP.toString(), loginUserDao.getUse_otp());
		sStore.setAttribute(NAME.OTP_SECRET_KEY.toString(), loginUserDao.getOtp_secret());
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
	
//	/**
//	 * 사용자 그룹 seqs를 보내줍니다.
//	 * @return
//	 */
//	public static String getGroupSeqs() {
//		HttpSession sStore = RWT.getRequest().getSession();
//		return (String)sStore.getAttribute(NAME.GROUP_SEQS.toString());
//	}
	
//	public static int getGroupSeq() {
//		HttpSession sStore = RWT.getRequest().getSession();
//		return (Integer)sStore.getAttribute(NAME.GROUP_SEQ.toString());
//	}
	
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
	
//	public static String getGroupName() {
//		HttpSession sStore = RWT.getRequest().getSession();
//		return (String)sStore.getAttribute(SESSEION_NAME.GROUP_NAME.toString());
//	}
	
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
	
	public static String getSecurityQuestion() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.SECURITY_QUESTION.toString());
	}
	
	public static String getSecurityAnswer() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.SECURITY_ANSWER.toString());
	}
	public static String getUseOTP() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.USE_OTP.toString());
	}
	public static String getOTPSecretKey() {
		HttpSession sStore = RWT.getRequest().getSession();
		return (String)sStore.getAttribute(NAME.OTP_SECRET_KEY.toString());
	}
	
//	/**
//	 * db에 해당하는 자신의 role을 가지고 옵니다.
//	 * 
//	 * @param groupSeq
//	 * @return
//	 */
//	public static String getRoleType(UserDBDAO userDB) {
//		HttpSession sStore = RWT.getRequest().getSession();
//		Map<Integer, String> mapUserRole = (Map)sStore.getAttribute(NAME.ROLE_TYPE.toString());
//		
////		return mapUserRole.get(userDB.getGroup_seq());
//		return "";
//	}
//	
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
//	
//	/**
//	 * 사용자의 모든 role type을 리턴합니다.
//	 * @return
//	 */
//	public static Map<Integer, String> getAllRoleType() {
//		HttpSession sStore = RWT.getRequest().getSession();
//		Map<Integer, String> mapUserRole = (Map)sStore.getAttribute(NAME.ROLE_TYPE.toString());
//		
//		return mapUserRole;
//	}
//	
//	public static int getManagerSeq() {
//		HttpSession sStore = RWT.getRequest().getSession();
//		return (Integer)sStore.getAttribute(SESSEION_NAME.MANAGER_SEQ.toString());
//	}

	/**
	 * 초기 접속시 프리퍼런스 정보를 로드합니다.
	 */
	public static void setUserInfos(Map<String, Object> mapUserInfo) {
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
	 * logout 처리를 합니다.
	 */
	public static void logout() {
		try {
			HttpSession sStore = RWT.getRequest().getSession();			
			sStore.setAttribute(NAME.USER_SEQ.toString(), 0);
	     
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
