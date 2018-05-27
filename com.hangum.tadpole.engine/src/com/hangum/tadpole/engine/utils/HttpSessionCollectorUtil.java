/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hangum.tadpole.session.manager.SessionManager;

/**
 * 사용자 session 을 저장하고 관리하는 유틸 클래스이다.
 * 
 * @author hangum
 *
 */
public class HttpSessionCollectorUtil {
	private static final Logger logger = Logger.getLogger(HttpSessionCollectorListener.class);
	private static HttpSessionCollectorUtil sessionCollector = null;//new HttpSessionCollectorUtil();
	public static enum COLLECT_KEY {SESSION, TIMEOUT};
	
	/** id, httpsession */
	private final Map<String, Map<String, Object>> mapSession = new ConcurrentHashMap<String, Map<String, Object>>();
	
	private HttpSessionCollectorUtil() {};
	
	/**
	 * get instance
	 * 
	 * @return
	 */
	public static HttpSessionCollectorUtil getInstance() {
		if(sessionCollector == null) {
			sessionCollector = new HttpSessionCollectorUtil();
			
			Thread httpSessionChecker = new Thread(new SessionLiveChecker(), "Tadpole_HttpSessionChecker");
			httpSessionChecker.start();
		}
		return sessionCollector;
	}
	
	/**
	 * session created
	 * 
	 * @param id
	 * @param session
	 * @param intMinuteTimeOut
	 */
	public void sessionCreated(String id, HttpSession session, int intMinuteTimeOut) {
		if(logger.isDebugEnabled()) logger.debug(String.format("---> [login]%s->%s", id, session.getId()));
		
		Map<String, Object> mapUserData = new HashMap<String, Object>();
		mapUserData.put(COLLECT_KEY.SESSION.name(), session);
		mapUserData.put(COLLECT_KEY.TIMEOUT.name(), intMinuteTimeOut);
		
		mapSession.put(id, mapUserData);
	}

	/**
	 * session destoryed
	 * 
	 * @param id
	 */
	public void sessionDestroyed(final String strEmail) {
		Map<String, Object> mapUserData = mapSession.remove(strEmail);
		
		try {
			HttpSession httpSesssion = (HttpSession)mapUserData.get(COLLECT_KEY.SESSION.name());
			httpSesssion.invalidate();
		} catch(Throwable e) {
			logger.error(String.format("System invalidate user %s, messages %s", strEmail, e.getMessage()));
		} finally {
			if(logger.isDebugEnabled()) logger.debug("========= remove connection start " + strEmail);
			SessionManager.removeConnection(strEmail);
			if(logger.isDebugEnabled()) logger.debug("========= remove connection end ");
		}
		
	}

	/**
	 * get all session
	 * 
	 * @return
	 */
	public Map<String, Map<String, Object>> getSessions() {
		return mapSession;
	}

	/**
	 * find id to session
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> find(String strEmail) {
		return mapSession.get(strEmail);
	}

	/**
	 * find session
	 * 
	 * @param strEmail
	 * @return
	 */
	public HttpSession findSession(String strEmail) {
		Map<String, Object> mapUserData = find(strEmail);
		if(mapUserData != null) {
			return (HttpSession)mapUserData.get(COLLECT_KEY.SESSION.name());
		}
		return null;
	}	
}

/**
 * HttpSession live checker 
 * @author hangum
 *
 */
class SessionLiveChecker implements Runnable {
	private static final Logger logger = Logger.getLogger(HttpSessionCollectorListener.class);
	
	public SessionLiveChecker() {
	}
	
	@Override
	public void run() {
		
		while(true) {
//			// 10 분에 한번씩 Thread 검사.
//			try { Thread.sleep((60 * 1000) * 1); } catch(Exception e) {};
			
			Map<String, Map<String, Object>> allUserSession = HttpSessionCollectorUtil.getInstance().getSessions();
			final List<String> listIDs = new ArrayList<String>(allUserSession.keySet());
			for (String id : listIDs) {
				Map<String, Object> mapUserData = allUserSession.get(id);
				HttpSession httpSession = (HttpSession)mapUserData.get(HttpSessionCollectorUtil.COLLECT_KEY.SESSION.name());
				Integer intTimeOut = (Integer)mapUserData.get(HttpSessionCollectorUtil.COLLECT_KEY.TIMEOUT.name());
				long userTime = intTimeOut * 60 * 1000;
				try {
					long gapTime = System.currentTimeMillis() - httpSession.getLastAccessedTime();
				
					if(logger.isDebugEnabled()) {
						logger.debug("=========== session live checker ===============");
						logger.debug(String.format("[session user] id=%s", id));
						logger.debug(String.format("[userTime]%s[gapTime]%s", userTime, gapTime));
						logger.debug("=========== session live checker ===============");
					}
					
					if(gapTime > userTime) {
						if(logger.isDebugEnabled()) logger.debug("[session invalidate is ]" + id);
						HttpSessionCollectorUtil.getInstance().sessionDestroyed(id);
					}
					
				// session 이 만료되어서 시간을 가져 올수 없는 상태이므로 세션과 커넥션을 처리합니다.
				} catch(IllegalStateException e) {
					if(logger.isDebugEnabled()) logger.debug("[ise][session invalidate is ]" + id);
					HttpSessionCollectorUtil.getInstance().sessionDestroyed(id);
				}
			}
			
			// 엔진의 설정 정보를 디비에서 가져와서 동기화(?)
			// TDDO 여기서 해야하나도 싶고요.(hangum)
//			final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//			shell.getDisplay().syncExec(new Runnable() {
//				@Override
//				public void run() {
//					try{
//						TadpoleApplicationContextManager.initAdminSystemSetting();
//					} catch(Exception e) {
//						logger.error("re initialize system setting", e);
//					}
//				}
//			});
			
			// 10 분에 한번씩 Thread 검사.
			try { Thread.sleep((60 * 1000) * 10); } catch(Exception e) {};
		} // while 
		
	}
}
