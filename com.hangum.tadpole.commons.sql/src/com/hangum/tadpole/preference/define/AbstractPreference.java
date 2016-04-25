/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.preference.define;

import java.util.Map;

import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * abstract preference
 * 
 * @author hangum
 *
 */
public class AbstractPreference {

	/**
	 * get value preference value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String key, Object defaultValue) {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(key);
		if(null == userInfo) return defaultValue.toString();
		else if("".equals(userInfo.getValue0())) return defaultValue.toString();
		
		return userInfo.getValue0();
	}
	
	/**
	 * get value preference value
	 * 
	 * @param mapUserInfoData
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(Map<String, UserInfoDataDAO> mapUserInfoData, String key, String defaultValue) {
		UserInfoDataDAO userInfoDao = mapUserInfoData.get(key);
		if(null == userInfoDao) return defaultValue;
		else return userInfoDao.getValue0();
	}

}
