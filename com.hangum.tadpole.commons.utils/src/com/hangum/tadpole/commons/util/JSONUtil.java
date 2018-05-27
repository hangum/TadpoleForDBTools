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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hangum.tadpole.commons.util.inner.JSONFlattener;

/**
 * json util
 * 
 */
public class JSONUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JSONUtil.class);
	
	/**
	 * get json to table data
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, String>> getJsonToTable(String jsonString) {
		List<Map<String, String>> flatJson = JSONFlattener.parseJson(jsonString);
		
		return flatJson;
	}
	
	/**
     * Get the json header.
     *
     * @param flatJson
     * @return a Set of headers
     */
    public static Set<String> collectHeaders(List<Map<String, String>> flatJson) {
    	Set<String> headers = new TreeSet<String>();
        for (Map<String, String> map : flatJson) {
        	headers.addAll(map.keySet());
        }

        return headers;
    }
	
	/**
	 * json normal string to pretty string
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String getPretty(String jsonString) {
		if(jsonString == null) return "";

//		if(logger.isDebugEnabled()) {
//			logger.debug("======================================================");
//			logger.debug("==> ["+ jsonString + "]");
//			logger.debug("======================================================");
//		}
		try {
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(StringUtils.trim(jsonString));
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String strGson = gson.toJson(je);
//			System.out.println(StringUtils.trimToEmpty(strGson));
			
			if(strGson == null || "null".equals(strGson)) strGson = "";
			
			return strGson;
		} catch(Exception e) {
//			logger.error("pretty json", e);
		}

		return jsonString;

	}
}
