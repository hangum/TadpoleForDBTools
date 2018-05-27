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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.utils.VelocityUtils;
import com.hangum.tadpole.commons.util.DateUtil;
import com.hangum.tadpole.engine.sql.paremeter.lang.OracleStyleSQLNamedParameterUtil;
import com.hangum.tadpole.preference.define.GetAdminPreference;

/**
 * RESTful API UTILS
 * 
 * @author hangum
 *
 */
public class RESTfulAPIUtils {
	private static final Logger logger = Logger.getLogger(RESTfulAPIUtils.class);

	/** define variable */
	public enum TDB_DATA_TYPE {_VARCHAR, _BIT, _NUM, _TINYINT, _SMALLINT, _INT, _BIGINT, _FLOAT, _DOUBLE, _VARBINARY, _DATE, _TIME};
	
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
			Map<String, Object> params = RESTfulAPIUtils.maekArgumentTOMap(strArgument);
			
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
	public static Map<String, Object> maekArgumentTOMap(String strArgument) throws RESTFULUnsupportedEncodingException {
		if(StringUtils.split(strArgument, "&") == null) return new HashMap<String, Object>();
		
		if(logger.isDebugEnabled()) logger.debug("original URL is ===> " + strArgument);
		Map<String, Object> params = new HashMap<String, Object>();
		
		try {
			for (String param : StringUtils.split(strArgument, "&")) {
				String pair[] = StringUtils.split(param, "=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				
				Object value = null;
				if (pair.length > 1) {
					try {
						value = convertExistObject(key, pair[1]);
					} catch(Exception e) {
						logger.error("conver type casting exception" + e.getMessage());
						value = pair[1];
					}
				}
	
				params.put(key, value);
			}
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1);
			throw new RESTFULUnsupportedEncodingException(e1.getMessage());
		}
		
		return params;
	}
		
	/**
	 * 
	 * @param variableType
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Object convertExistObject(String variableType, String value) throws UnsupportedEncodingException {
		value = URLDecoder.decode(value, "UTF-8");
		
		if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._BIT.name())) {
			return new Boolean(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._NUM.name())) {
			return new BigDecimal(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._TINYINT.name())) {
			return new Byte(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._SMALLINT.name())) {
			return new Short(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._INT.name())) {
			return new Integer(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._BIGINT.name())) {
			return new Long(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._FLOAT.name())) {
			return new Float(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._DOUBLE.name())) {
			return new Double(value);
			
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._VARBINARY.name())) {
			return new Byte(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._DATE.name())) {
			return DateUtil.convertToDate(value);
		} else if(StringUtils.startsWithIgnoreCase(variableType, TDB_DATA_TYPE._TIME.name())) {
			return new Timestamp(new Long(value).longValue());
		}
		
		return value;
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
	public static List<Object> makeArgumentToOracleList(Map<Integer, String> mapIndex, String strArgument) throws RESTFulArgumentNotMatchException, RESTFULUnsupportedEncodingException {
		List<Object> listParam = new ArrayList<Object>();
		
		Map<String, Object> params = maekArgumentTOMap(strArgument);
		
		for(int i=1; i<=mapIndex.size(); i++ ) {
			String strKey = mapIndex.get(i);
			
			if(!params.containsKey(strKey)) {
				throw new RESTFulArgumentNotMatchException("SQL Parameter not found. Must have to key name is " + strKey);
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
	public static List<Object> makeArgumentToJavaList(String strArgument) throws RESTFULUnsupportedEncodingException {
		List<Object> listParam = new ArrayList<Object>();
		
		Map<String, Object> params = maekArgumentTOMap(strArgument);

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
		if("".equals(strSQL)) return "";
		
		String strArguments = "";
		OracleStyleSQLNamedParameterUtil oracleNamedParamUtil = new OracleStyleSQLNamedParameterUtil();
		oracleNamedParamUtil.parse(strSQL);
		Map<Integer, String> mapIndex = oracleNamedParamUtil.getMapIndexToName();
		if(!mapIndex.isEmpty()) {
			for(String strParam : mapIndex.values()) {
				strArguments += String.format("%s={%s}&", strParam, strParam);	
			}
			strArguments = StringUtils.removeEnd(strArguments, "&");
			 
		} else {
			strArguments = "";//1={FirstParameter}&2={SecondParameter}";
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
		String strServerURL = GetAdminPreference.getApiServerURL();
		
		return String.format("%s%s?%s", 
								strServerURL, 
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
