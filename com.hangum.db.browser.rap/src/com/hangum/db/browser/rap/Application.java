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
package com.hangum.db.browser.rap;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.hangum.tadpole.preference.define.PreferenceDefine;


/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {

	public int createUI() {

		initSession();

		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();		
		return PlatformUI.createAndRunWorkbench( display, advisor );
	}
	
	/**
	 * session을 초기화 합니다.
	 * 
	 */
	private void initSession() {
		ISessionStore iss = RWT.getSessionStore();
		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		int sessionTimeOut = store.getInt(PreferenceDefine.SESSION_DFEAULT_PREFERENCE);
		
		if(sessionTimeOut <= 0) {
			// server mode일 경우 default 20분동안 session을 유지 합니다.
			// standalone mode일 경우 24시간 유지 합니다.
			//
//			if(StartupUtils.isStandaloneMode()) {
				iss.getHttpSession().setMaxInactiveInterval( 60 * 60 * 24 );
//			} else {
//				iss.getHttpSession().setMaxInactiveInterval( 20 * 60 );
//			}
		} else {
			iss.getHttpSession().setMaxInactiveInterval(sessionTimeOut * 60);
		}
		
		HttpSessionContext hsc = iss.getHttpSession().getSessionContext();
		Enumeration enumS = hsc.getIds();
		while(enumS.hasMoreElements()) {
			String id = enumS.nextElement().toString();
			
			HttpSession hs = hsc.getSession(id);
			System.out.println(hs.getId());
		}
		
		
//		String sessionId = iss.getHttpSession().getId();
//		long longCreatTime = iss.getHttpSession().getCreationTime();
//		boolean isNew = iss.getHttpSession().isNew();
//		
//		HttpServletRequest servletRequest = RWT.getRequest();
//		System.out.println( servletRequest.getLocalAddr() + ":" + servletRequest.getLocalName() );
		
//		System.out.println("[is New]" + isNew);
//		System.out.println("[session] " + sessionId);
//		System.out.println("[create time]" + new Date(longCreatTime));
//		iss.setAttribute("USER_SESSION", servletRequest.getLocalAddr() + ":" + servletRequest.getLocalName());
		
	}	// end method
}
