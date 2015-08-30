/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.engine.sql.paremeter.SQLNamedParameterUtil;

/**
 * RESTful API UTILS
 * 
 * @author hangum
 *
 */
public class RESTfulAPIUtils {
	
	/**
	 * Return SQL Paramter
	 * 
	 * @param strSQL
	 * @return
	 */
	public static String getParameter(String strSQL) {
		String strArguments = "";
		SQLNamedParameterUtil oracleNamedParamUtil = SQLNamedParameterUtil.getInstance();
		oracleNamedParamUtil.parse(strSQL);
		Map<Integer, String> mapIndex = oracleNamedParamUtil.getMapIndexToName();
		if(!mapIndex.isEmpty()) {
			for(String strParam : mapIndex.values()) {
				strArguments += String.format("%s={%s_value}&", strParam, strParam);	
			}
			strArguments = StringUtils.removeEnd(strArguments, "&");
			 
		} else {
			strArguments = "1={FirstParameter}&2={SecondParameter}";
		}
		
		return strArguments;
	}

	/**
	 * make url
	 * 
	 * @param strSQL
	 * @param strRestURL
	 * @return
	 */
	public static String makeURL(String strSQL, String strRestURL) {
		HttpServletRequest httpRequest = RWT.getRequest();
		String strServerURL = String.format("http://%s:%s%s", httpRequest.getLocalName(), httpRequest.getLocalPort(), httpRequest.getServletPath());
		
		return String.format("%s%s?%s", 
								strServerURL + "api/rest/base", 
								strRestURL,
								getParameter(strSQL));
	}
	
	/**
	 * user validate
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validateURL(String url) {
		Pattern p = Pattern.compile("[/][-A-Za-z0-9+&amp;@#/%=~_()|]{2}", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(url);
	
		return m.find();
	}
	
}
