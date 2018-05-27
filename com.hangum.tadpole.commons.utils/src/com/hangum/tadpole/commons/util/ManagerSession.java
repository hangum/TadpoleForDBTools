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
package com.hangum.tadpole.commons.util;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

/**
 * session 관리
 * 
 * @author hangum
 *
 */
public class ManagerSession {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ManagerSession.class);

//	/**
//	 * session관리
//	 */
//	public static void sessionManager() {
//		
//		HttpSession rootSession = RWT.getRequest().getSession(true);				
////		String id 				= session.getId();
////		long createTime 		= session.getCreationTime();
////		long lastAccessedTime 	= session.getLastAccessedTime();
//		
//		// 전체 로그인 된 session 리스트를 가져옵니다.
//		HttpSessionContext context = rootSession.getSessionContext();
//		Enumeration ids = context.getIds();
//		
//		int i =0;
//		while(ids.hasMoreElements()) {
//			i++;
//			
//			String id = (String) ids.nextElement();
//				HttpSession session = context.getSession(id);
//
//			String user_id = (String)session.getValue("userID");
//			logger.debug("(" + i + ")" + id );
//			logger.debug("  userID = [" + user_id + "]");  
//		}
//		
//		logger.debug("total connect user is " + i);
//		
//		ServletContext sc = RWT.getRequest().getServletContext();
//	}
	
	/**
	 * logout 처리를 합니다.
	 */
	public static void logout() {
		try {
			HttpSession sStore = RWT.getRequest().getSession();
			sStore.invalidate();
		} catch(Exception e) {
			// ignore exception
		}
	}
	
}
