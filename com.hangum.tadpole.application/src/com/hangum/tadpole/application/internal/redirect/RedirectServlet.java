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
package com.hangum.tadpole.application.internal.redirect;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * rap1.5의 url을 2.0의 url로 리다이렉트합니다.
 * 즉, rap에서 1.5에서는 /tadpole/db?startup=tadpole 호출하는 방식이었으나 2.0에서는 /tadpole만 호출하면 되는 방식이어서 수정합니다.
 * 
 *  http://wiki.eclipse.org/RAP/FAQ#How_to_access_a_RAP_application_without_specifying_a_servlet_name.3F
 * 
 * @author hangum
 *
 */
public class RedirectServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(RedirectServlet.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		redirect(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		redirect(request, response);
	}

	
	private static void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// tomcat에서 실행했는지.. 아니면 standalone모드인지...
		
		if(!isStandardalone()) {			
			response.sendRedirect(response.encodeRedirectURL("/tadpole/tadpole"));
		} else {
			response.sendRedirect(response.encodeRedirectURL("/tadpole"));
		}
	}
	
	/**
	  * standalone mode인지 
	  * 
	  * @return
	  */
	private static boolean isStandardalone() {
		 Bundle bundle = Platform.getBundle("org.eclipse.jetty.server"); //$NON-NLS-1$
	     if(bundle != null) return true;
	     
	     return false;
	}
	
}
