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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;

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
	private final Map<String, Map<String, Object>> mapSession = new HashMap<String, Map<String, Object>>();
	
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
	public void sessionDestroyed(String strEmail) {
		if(logger.isDebugEnabled()) logger.debug(String.format("---> [login]%s", strEmail));
		Map<String, Object> mapUserData = mapSession.remove(strEmail);
		HttpSession httpSesssion = (HttpSession)mapUserData.get(COLLECT_KEY.SESSION.name());
		
		if(logger.isDebugEnabled()) logger.debug("=== remove user connection ");
		try {
			TadpoleSQLManager.removeAllInstance(strEmail);
			TadpoleSQLTransactionManager.executeRollback(strEmail);
		} catch(Exception e) {
			logger.error("remove user connection", e);
		}
		
		// 로그 아웃이 되었다면 exception이 나올것이ㅏ.
		try {
			httpSesssion.invalidate();
		} catch(Exception e) {
			logger.error(String.format("System invalidate user %s", strEmail), e);
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
class SessionLiveChecker implements Runnable{
	private static final Logger logger = Logger.getLogger(HttpSessionCollectorListener.class);
	
	public SessionLiveChecker() {
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			Map<String, Map<String, Object>> allUserSession = HttpSessionCollectorUtil.getInstance().getSessions();
			Set<String> keys = allUserSession.keySet();
			for(String id : keys) {
				Map<String, Object> mapUserData = allUserSession.get(id);
				HttpSession httpSession = (HttpSession)mapUserData.get(HttpSessionCollectorUtil.COLLECT_KEY.SESSION.name());
				Integer intTimeOut = (Integer)mapUserData.get(HttpSessionCollectorUtil.COLLECT_KEY.TIMEOUT.name());
				long userTime = intTimeOut * 60 * 1000;
				try {
					long gapTime = System.currentTimeMillis() - httpSession.getLastAccessedTime();
				
					if(logger.isDebugEnabled()) {
						logger.debug("=========== session live checker ===============");
						logger.debug("[session user is ]" + id);
						logger.debug("[userTime]" + userTime);
						logger.debug("[gapTime]" + gapTime);
						logger.debug("=========== session live checker ===============");
					}
					
					if(gapTime > userTime) {
						if(logger.isDebugEnabled()) logger.debug("[session invalidate is ]" + id);
						HttpSessionCollectorUtil.getInstance().sessionDestroyed(id);
					}
					
				// session 이 만료되어서 시간을 가져 올수 없는 상태이므로 세션과 커넥션을 처리합니다.
				} catch(IllegalStateException e) {
					HttpSessionCollectorUtil.getInstance().sessionDestroyed(id);
				}
			}
			
			// 30 분에 한번씩 Thread 검사.
			try { Thread.sleep((60 * 1000) * 30); } catch(Exception e) {};
		}
		
	}
}
