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
package com.hangum.tadpole.preference.define;

/**
 * Define admin preference
 * 
 * @author hangum
 *
 */
public class AdminPreferenceDefine {
	private static final String _PREFIX = "ADMIN_";

	/** 신규 사용자 허락 유무 */
	public static final String NEW_USER_PERMIT = _PREFIX + "NEW_USER_PERMIT";
	public static final String NEW_USER_PERMIT_VALUE = "YES";
	
	/** 사용자가 디비 추가 여부 */
	public static final String IS_ADD_DB = _PREFIX + "IS_ADD_DB";
	public static final String IS_ADD_DB_VALUE = "YES";
	
	/** 사용자가 디비 공유 여부 */
	public static final String IS_SHARED_DB = _PREFIX + "IS_SHARED_DB";
	public static final String IS_SHARED_DB_VALUE = "YES";
	
	/** 사용자가 디비를 추가 할수 있는 카운트 */
	public static final String DEFAULT_ADD_DB_CNT = _PREFIX + "DEFAULT_ADD_DB_CNT";
	public static final String DEFAULT_ADD_DB_CNT_VALUE = "10";
	
	/** 가입 일 부터 서비스 사용 제한 일 */
	public static final String SERVICE_DURATION_DAY = _PREFIX + "SERVICE_DURATION_DAY";
	public static final String SERVICE_DURATION_DAY_VALUE = "3650";
	
	/**
	 * Define api server uri
	 */
	public static final String API_SERVER_URL = _PREFIX + "API_SERVER_URL";
	public static final String API_SERVER_URL_VALUE = "http://localhost:8080/api";

}
