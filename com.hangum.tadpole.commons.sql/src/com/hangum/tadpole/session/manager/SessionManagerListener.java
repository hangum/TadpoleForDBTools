package com.hangum.tadpole.session.manager;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.hangum.tadpole.session.manager.SessionManager;

/**
 * collect login session
 * 
 * @author hangum
 *
 */
public class SessionManagerListener implements HttpSessionListener {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SessionManagerListener.class);

	private static Map<String, String> sessionIds = new HashMap<String, String>();
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.debug("new user login " + se.getSession().getId());
		
		Object email = se.getSession().getAttribute(SessionManager.NAME.LOGIN_EMAIL.toString());
		if(email != null) {
			sessionIds.put(email.toString(), se.getSession().getId());
		}
		
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		Object email = se.getSession().getAttribute(SessionManager.NAME.LOGIN_EMAIL.toString());
		if(email != null) {
			sessionIds.remove(email.toString());
		}
	}
	
	/**
	 * find session id
	 * 
	 * @param email
	 * @return session id
	 */
	public static String getSessionIds(String email) {
		return sessionIds.get(email);
	}

}
