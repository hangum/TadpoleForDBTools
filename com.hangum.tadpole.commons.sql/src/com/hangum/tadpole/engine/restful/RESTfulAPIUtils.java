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
package com.hangum.tadpole.engine.restful;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.libs.core.utils.VelocityUtils;
import com.hangum.tadpole.engine.sql.paremeter.lang.OracleStyleSQLNamedParameterUtil;

/**
 * RESTful API UTILS
 * 
 * @author hangum
 *
 */
public class RESTfulAPIUtils {
	private static final Logger logger = Logger.getLogger(RESTfulAPIUtils.class);
	
	/**
	 * make original sql
	 * 
	 * @param strName
	 * @param strSQLs
	 * @param strArgument
	 * @return
	 */
	public static String makeTemplateTOSQL(String strName, String strSQLs, String strArgument) throws SQLTemplateException {
		String strResult = "";
		try {
			Map<String, String> params = RESTfulAPIUtils.maekArgumentTOMap(strArgument);
			
			strResult = VelocityUtils.getTemplate(strName, strSQLs, params);
			if(logger.isDebugEnabled()) logger.debug(strResult);
			
		} catch(Exception e) {
			logger.error("Make Template", e);
			throw new SQLTemplateException(e.getMessage());
		}
		
		return strResult;
	}
	
	/**
	 * make argument to map
	 * @param strArgument
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> maekArgumentTOMap(String strArgument) throws UnsupportedEncodingException {
		if(logger.isDebugEnabled()) logger.debug("original URL is ===> " + strArgument);
		
		Map<String, String> params = new HashMap<String, String>();
		for (String param : StringUtils.split(strArgument, "&")) {
			String pair[] = StringUtils.split(param, "=");
			String key = URLDecoder.decode(pair[0], "UTF-8");
			String value = "";
			if (pair.length > 1) {
				try {
					value = URLDecoder.decode(pair[1], "UTF-8");
				} catch(Exception e) {
					value = pair[1];
				}
			}

			params.put(key, value);
		}
		
		return params;
	}
	
	/**
	 * Return oracle style argument to java list
	 * 
	 * @param mapIndex
	 * @param strArgument
	 * @return
	 * @throws RESTFulArgumentNotMatchException
	 * @throws UnsupportedEncodingException
	 */
	public static List<Object> makeArgumentToOracleList(Map<Integer, String> mapIndex, String strArgument) throws RESTFulArgumentNotMatchException, UnsupportedEncodingException {
		List<Object> listParam = new ArrayList<Object>();
		
		Map<String, String> params = maekArgumentTOMap(strArgument);
		
		for(int i=1; i<=mapIndex.size(); i++ ) {
			String strKey = mapIndex.get(i);
			
			if(!params.containsKey(strKey)) {
				throw new RESTFulArgumentNotMatchException("SQL Parameter not found. key name is " + strKey);
			} else {
				listParam.add( params.get(strKey) );
			}
		}
		return listParam;
	}

	/**
	 * return argument to java list
	 * 
	 * @param strArgument
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static List<Object> makeArgumentToJavaList(String strArgument) throws UnsupportedEncodingException {
		List<Object> listParam = new ArrayList<Object>();
		
		Map<String, String> params = maekArgumentTOMap(strArgument);

		// assume this count... no way i'll argument is over 100..... --;;
		for(int i=1; i<100; i++) {
			if(params.containsKey(String.valueOf(i))) {
				listParam.add(params.get(""+i));
			} else {
				break;
			}
		}

		return listParam;
	}
	
	/**
	 * Return SQL Paramter
	 * 
	 * @param strSQL
	 * @return
	 */
	public static String getParameter(String strSQL) {
		String strArguments = "";
		OracleStyleSQLNamedParameterUtil oracleNamedParamUtil = OracleStyleSQLNamedParameterUtil.getInstance();
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
