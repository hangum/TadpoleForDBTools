///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.application.internal.redirect;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.eclipse.core.runtime.Platform;
//import org.osgi.framework.Bundle;
//
///**
// * If invalid URL come, i change to the correct URL.
// *  http://wiki.eclipse.org/RAP/FAQ#How_to_access_a_RAP_application_without_specifying_a_servlet_name.3F
// * 
// * @author hangum
// *
// */
//public class RedirectServlet extends HttpServlet {
//	private static final Logger logger = Logger.getLogger(RedirectServlet.class);
//	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		redirect(request, response);
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		redirect(request, response);
//	}
//
//	private static void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		if(!isStandardalone()) {			
//			response.sendRedirect(response.encodeRedirectURL("/tadpole/tadpole"));
//		} else {
//			response.sendRedirect(response.encodeRedirectURL("/tadpole"));
//		}
//	}
//	
//	/**
//	  * Is standalone mode? 
//	  * 
//	  * @return
//	  */
//	private static boolean isStandardalone() {
//		 Bundle bundle = Platform.getBundle("org.eclipse.jetty.server"); //$NON-NLS-1$
//	     if(bundle != null) return true;
//	     
//	     return false;
//	}
//	
//}
