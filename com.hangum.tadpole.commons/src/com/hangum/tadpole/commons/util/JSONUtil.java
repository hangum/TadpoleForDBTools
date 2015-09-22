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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
			System.out.println(StringUtils.trimToEmpty(strGson));
			
			if(strGson == null || "null".equals(strGson)) strGson = "";
			
			return strGson;
		} catch(Exception e) {
//			logger.error("pretty json", e);
		}

		return jsonString;

	}
}
