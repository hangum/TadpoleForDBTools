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
package com.hangum.tadpold.commons.libs.core.define;

/**
 * 올챙이 전역 정의 
 * 
 * @author hangum
 *
 */
public class PublicTadpoleDefine {
	/**
	 * 분리자
	 */
	public static String DELIMITER = "||TADPOLE-DELIMITER||";
	
	/** 라인분리자 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	/**  쿼리 구분자 */
	public static final String SQL_DILIMITER = ";";

	/** tadpole url */
	public static String TADPOLE_URL = "http://127.0.0.1:%s/tadpole";//db?startup=tadpole";
	
	/**
	 * tadpole url
	 * 
	 * @return
	 */
	public static String getTadpoleUrl() {
		String tadpolePort = System.getProperty("org.osgi.service.http.port", "10081");
		return String.format(TADPOLE_URL, tadpolePort);
	}
}
