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
package com.hangum.tadpole.util;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;

/**
 * 
 * @author hangum
 *
 */
public class RAPLocaleUtils {

	public static void changeLocale( Locale locale ) {
		
		try {
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("1. [현재의 Locale]===>" + RWT.getLocale().toString());
//			System.out.println("1. [수정된 Locale]===>" +locale);
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
//			System.out.println("#########################################################################################");
			
			RWT.setLocale( locale );
			
//			final String url = createUrl( locale  );
//			// Uses a non-public API, see http://bugs.eclipse.org/342995
//			JSExecutor.executeJS( "parent.window.location.href=\"" + url + "\";" );
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		 
	private static String createUrl( String locale ) {
		StringBuffer url = new StringBuffer();
		url.append( RWT.getRequest().getContextPath() );
		url.append( RWT.getRequest().getServletPath() );  
		url.append( "?locale=" );
		url.append( locale );
		return RWT.getResponse().encodeURL( url.toString() );
	}

}
