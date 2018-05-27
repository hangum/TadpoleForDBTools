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

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * abstract preference
 * 
 * @author hangum
 *
 */
public class AbstractPreference {
	private static final Logger logger = Logger.getLogger(AbstractPreference.class);

	/**
	 * get value preference value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String key, Object defaultValue) {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(key, ""+defaultValue);
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
	public static String getAdminValue(Map<String, UserInfoDataDAO> mapUserInfoData, String key, String defaultValue) {
		UserInfoDataDAO userInfoDao = mapUserInfoData.get(key);
		if(null == userInfoDao) {
			userInfoDao = new UserInfoDataDAO(-1, key, defaultValue);
			try {
				TadpoleSystem_UserInfoData.insertUserInfoData(userInfoDao);
			} catch(Exception e) {
				logger.error("User data save exception [key]" + key + "[value]" + defaultValue, e);
			}
			mapUserInfoData.put(key, userInfoDao);
			
			return defaultValue;
		} else {
			return userInfoDao.getValue0();
		}
	}

}
