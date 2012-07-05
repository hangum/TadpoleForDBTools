package com.hangum.db.commons.session;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.ISessionStore;

/**
 * tadpole의 session manager입니다
 * 
 * @author hangum
 *
 */
public class SessionManager {
	/**
	 * <pre>
	 * 		MANAGER_SEQ는 그룹의 manager 권한 사용자의 seq 입니다.  seq로  그룹의 db list를 얻기위해 미리 가져옵니다.
	 * 
	 * 
	 * </pre>
	 * 
	 * @author hangum
	 */
	enum SESSEION_NAME {GROUP_SEQ, SEQ, LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_NAME, LOGIN_TYPE, MANAGER_SEQ}
	
	/**
	 * 신규 user의 사용자를 등록
	 * 
	 * @param email
	 * @param name
	 */
	public static void newLogin(int groupSeq, int seq, String email, String password, String name, String userType, int managerSeq) {
		ISessionStore sStore = RWT.getSessionStore();
		
		sStore.setAttribute(SESSEION_NAME.GROUP_SEQ.toString(), groupSeq);
		sStore.setAttribute(SESSEION_NAME.SEQ.toString(), seq);
		sStore.setAttribute(SESSEION_NAME.LOGIN_EMAIL.toString(), email);
		sStore.setAttribute(SESSEION_NAME.LOGIN_PASSWORD.toString(), password);
		sStore.setAttribute(SESSEION_NAME.LOGIN_NAME.toString(), name);
		sStore.setAttribute(SESSEION_NAME.LOGIN_TYPE.toString(), userType);
		sStore.setAttribute(SESSEION_NAME.MANAGER_SEQ.toString(), managerSeq);
	}
	
	public static int getGroupSeq() {
		ISessionStore sStore = RWT.getSessionStore();
		return (Integer)sStore.getAttribute(SESSEION_NAME.GROUP_SEQ.toString());
	}
	
	public static int getSeq() {
		ISessionStore sStore = RWT.getSessionStore();
		return (Integer)sStore.getAttribute(SESSEION_NAME.SEQ.toString());
	}
	
	public static String getEMAIL() {
		ISessionStore sStore = RWT.getSessionStore();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_EMAIL.toString());
	}
	
	public static String getPassword() {
		ISessionStore sStore = RWT.getSessionStore();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_PASSWORD.toString());
	}
	
	public static String getName() {
		ISessionStore sStore = RWT.getSessionStore();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_NAME.toString());
	}
	
	public static String getLoginType() {
		ISessionStore sStore = RWT.getSessionStore();
		return (String)sStore.getAttribute(SESSEION_NAME.LOGIN_TYPE.toString());
	}
	
	public static int getManagerSeq() {
		ISessionStore sStore = RWT.getSessionStore();
		return (Integer)sStore.getAttribute(SESSEION_NAME.MANAGER_SEQ.toString());
	}
	
}
