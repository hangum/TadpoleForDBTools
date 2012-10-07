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
package com.hangum.db.util;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;

import org.eclipse.rwt.RWT;

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

	/**
	 * session관리
	 */
	public static void sessionManager() {
		
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
		
		ServletContext sc = RWT.getRequest().getServletContext();
		
	}
}
