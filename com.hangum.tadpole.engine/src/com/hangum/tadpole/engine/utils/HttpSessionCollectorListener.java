/*******************************************************************************
 * Copyright (c) 2016 hangum.
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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * HTTP session collector
 */
public class HttpSessionCollectorListener implements HttpSessionListener {
	private static final Logger logger = Logger.getLogger(HttpSessionCollectorListener.class);
	private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		if(logger.isDebugEnabled()) logger.debug("---> [login]" + session.getId());
		sessions.put(session.getId(), session);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		if(logger.isDebugEnabled()) logger.debug("---> [logout]" + event.getSession().getId());
		sessions.remove(event.getSession().getId());
	}

	public static Map<String, HttpSession> getSessions() {
		return sessions;
	}

	public static HttpSession find(String sessionId) {
		return sessions.get(sessionId);
	}

}
