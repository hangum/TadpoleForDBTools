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
package com.hangum.tadpole.engine.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.TadpoleSystemDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystemQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;

/**
 * Tadpole Application context manager
 * 
 * @author hangum
 *
 */
public class TadpoleApplicationContextManager {
	private static final Logger logger = Logger.getLogger(TadpoleApplicationContextManager.class);
	
	/**
	 * admin system env
	 * 
	 * @return
	 */
	public static Map<String, UserInfoDataDAO> getAdminSystemEnv() {
		ApplicationContext context = RWT.getApplicationContext();
		Map<String, UserInfoDataDAO> mapUserInfoData = (Map<String, UserInfoDataDAO>)context.getAttribute("adminSystemEnv");

		try {
	    	if(mapUserInfoData == null) {
	    		List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData(PublicTadpoleDefine.systemAdminId);
	    		mapUserInfoData = new HashMap<String, UserInfoDataDAO>();
	    		for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
	    			mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
	    		}
	    		
	    		context.setAttribute("adminSystemEnv", mapUserInfoData);
	    	}
		} catch(Exception e) {
			logger.error("admin system env", e);
		}
    	
    	return mapUserInfoData;
	}
	
	/**
	 * Is personal operation type
	 * 
	 * @return
	 */
	public static boolean isPersonOperationType() {
		try {
			TadpoleSystemDAO tsd = getTadpoleSystem();
			if(tsd.getExecute_type().equals(PublicTadpoleDefine.SYSTEM_USE_GROUP.PERSONAL.name())) {
				return true;
			}
		} catch(Exception e) {
			
		}
		return false;
	}

	/**
	 * Get Tadpole system
	 * 
	 * @return
	 * @throws Exception
	 */
	public static TadpoleSystemDAO getTadpoleSystem() throws Exception {
    	ApplicationContext context = RWT.getApplicationContext();
    	TadpoleSystemDAO tsd = (TadpoleSystemDAO)context.getAttribute("getSystemInfo");
    	if(tsd == null) {
    		tsd = TadpoleSystemQuery.getSystemInfo();
    		context.setAttribute("getSystemInfo", tsd);
    	}
    	
    	return tsd;
	}
	
	/**
	 * Get system admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public static UserDAO getSystemAdmin() throws Exception {
		ApplicationContext context = RWT.getApplicationContext();
		UserDAO userDao = (UserDAO)context.getAttribute("getSystemAdmin");
    	if(userDao == null) {
    		userDao = TadpoleSystem_UserQuery.getSystemAdmin();
    		if(userDao != null) context.setAttribute("getSystemAdmin", userDao);
    	}
    	
    	return userDao;
	}
	
	/**
	 * initial System context
	 */
	public static void initSystem() {
		ApplicationContext context = RWT.getApplicationContext();
		context.removeAttribute("getSystemInfo");
		context.removeAttribute("getSystemAdmin");
	}
	
}
