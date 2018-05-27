/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.utils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * rcptt 테스트 하기위한 초기 유틸
 * 
 * @author hangum
 *
 */
public class RCTPPTest {
	private static final Logger logger = Logger.getLogger(RCTPPTest.class);

	public static void initializeTest () {
		// 테스트를 위해서 사용하는 코드.
    	if(ApplicationArgumentUtils.isTestLogin()) {
    		// en
    		RWT.getUISession().setLocale(Locale.ENGLISH);
    		
    		UserDAO userDao = new UserDAO();
    		userDao.setSeq(-1);
    		userDao.setName("test user");
    		userDao.setEmail("_admin@tadpolehub.com");
    		userDao.setRole_type("SYSTEM_ADMIN");
    		userDao.setIs_modify_perference("YES");
    		userDao.setService_start(new Timestamp(System.currentTimeMillis()-100000));
    		userDao.setService_end(new Timestamp(System.currentTimeMillis()+100000000));
    		SessionManager.addSession(userDao, SessionManager.LOGIN_IP_TYPE.BROWSER_IP.name(), "127.0.0.1");
    		initializeUserSession();
    	}
	}
	
	/**
     * initialize user session
     */
    private static void initializeUserSession() {
    	try {
			// Stored user session.
			List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData();
			Map<String, Object> mapUserInfoData = new HashMap<String, Object>();
			for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
				mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
			}
			SessionManager.setUserAllPreferenceData(mapUserInfoData);
			
			initSession();
			
		} catch(Exception e) {
			logger.error("session set", e); //$NON-NLS-1$
		}
    }
    
    /**
	 * Set initialize session
	 */
	private static void initSession() {
		HttpSession iss = RWT.getUISession().getHttpSession();
		
		final int sessionTimeOut = 60 * 1000 * 60 * 24;
		if(sessionTimeOut <= 0) {
			iss.setMaxInactiveInterval(90 * 60);
		} else {
			iss.setMaxInactiveInterval(sessionTimeOut * 60);
		}
		
//		// user logout
//		RWT.getUISession().addUISessionListener( new UISessionListener() {
//			public void beforeDestroy( UISessionEvent event ) {
//				logger.info(String.format("User has logout. session id is %s", event.getUISession().getId()));
//			}
//		});
	}

}
