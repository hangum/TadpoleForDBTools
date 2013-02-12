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

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonString);
			return gson.toJson(je);
		} catch(Exception e) {
			logger.error("pretty json", e);
		}

		return jsonString;

	}
}
