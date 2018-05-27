/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.initialize;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.preference.define.AdminPreferenceDefine;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Tadpole DB Hub system initialize
 * 
 * @author hangum
 *
 */
public class TadpoleHubStartupInitializer {

	/**
	 * initialize Tadpole Config file and load JDBC driver
	 */
	public static void initializeLoadConfigAndJDBCDriver() {
		// load default config file
		LoadConfigFile.initializeConfigFile();
		
		// load jdbc driver
		JDBCDriverLoader.initializeJDBCDriver();
	}
	
	/**
	 * 시스템 초기 값을 설정합니다.
	 * 
	 * @throws Exception
	 */
	public static void initializeSystemValue() throws TadpoleSQLManagerException, SQLException {
		/* define login type */
		Properties prop = LoadConfigFile.getConfigFile();
//		String txtLoginMethod = StringUtils.trim(prop.getProperty("LOGIN_METHOD", AdminPreferenceDefine.SYSTEM_LOGIN_METHOD_VALUE));
//		UserInfoDataDAO userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, txtLoginMethod);
//		GetAdminPreference.updateAdminSessionData(AdminPreferenceDefine.SYSTEM_LOGIN_METHOD, userInfoDao);
		
		/** 뷰에 보여주어야할 프러덕 필터 값을 가져온다 */
		GetAdminPreference.updateAdminSessionData(new UserInfoDataDAO(PublicTadpoleDefine.systemAdminId, AdminPreferenceDefine.SYSTEM_VIEW_PRODUCT_TYPE_FILTER, 
						StringUtils.trim(prop.getProperty("tadpole.db.producttype.remove.filter", ""))
			)
		);
		
		/** 뷰에 보여주어야할 그룹이름 필터 값을 가져온다 */
		GetAdminPreference.updateAdminSessionData(new UserInfoDataDAO(PublicTadpoleDefine.systemAdminId, AdminPreferenceDefine.SYSTEM_VIEW_GROUP_NAME_FILTER,
						StringUtils.trim(prop.getProperty("tadpole.db.groupname.remove.filter", ""))
				)
		);
		
		/** cert user info */
		PublicTadpoleDefine.CERT_USER_INFO = StringUtils.trim(prop.getProperty("CERT_USER_INFO", ""));
	}
}
