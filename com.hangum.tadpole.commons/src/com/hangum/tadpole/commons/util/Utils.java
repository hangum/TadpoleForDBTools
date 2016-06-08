/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 *
 * Tadpole ustils class
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class Utils {
	
	/**
	 * convert line to html
	 * @param str
	 * @return
	 */
	public static String convLineToHtml(String str) {
		if(str == null) return "";

		return StringUtils.replace(StringEscapeUtils.escapeHtml(str), PublicTadpoleDefine.LINE_SEPARATOR, "<br/>");
	}
	
	public static String convHtmlToLine(String str) {
		return StringUtils.replace(StringEscapeUtils.unescapeHtml(str), "<br/>", PublicTadpoleDefine.LINE_SEPARATOR);
	}
	
	/**
	 * unique id 를 넘겨준다. 
	 * 
	 * @param length
	 * @return
	 */
	public static String getUniqueID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * unique id를 digit 만큼 넘겨 준다.
	 * 
	 * @param digit
	 * @return
	 */
	public static String getUniqueDigit(int digit) {
		return getUniqueID().substring(0, digit);
	}
	
}
