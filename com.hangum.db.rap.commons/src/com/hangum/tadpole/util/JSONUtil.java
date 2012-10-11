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

/**
 * json util
 * 
 */
public class JSONUtil {
	
	/**
	 * json normal string to pretty string
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String getPretty(String jsonString) {
		if(jsonString == null) return "";

		final String INDENT = "    ";
		StringBuffer prettyJsonSb = new StringBuffer();

		int indentDepth = 0;
		String targetString = null;
		for (int i = 0; i < jsonString.length(); i++) {
			targetString = jsonString.substring(i, i + 1);
			if (targetString.equals("{") || targetString.equals("[")) {
				prettyJsonSb.append(targetString).append("\n");
				indentDepth++;
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			} else if (targetString.equals("}") || targetString.equals("]")) {
				prettyJsonSb.append("\n");
				indentDepth--;
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
				prettyJsonSb.append(targetString);
			} else if (targetString.equals(",")) {
				prettyJsonSb.append(targetString);
				prettyJsonSb.append("\n");
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			} else {
				prettyJsonSb.append(targetString);
			}

		}

		return prettyJsonSb.toString();

	}
}
