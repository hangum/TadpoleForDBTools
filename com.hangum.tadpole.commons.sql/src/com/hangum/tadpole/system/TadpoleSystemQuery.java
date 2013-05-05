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
package com.hangum.tadpole.system;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.TadpoleSystemDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tadpole System query
 * 
 * @author hangum
 *
 */
public class TadpoleSystemQuery {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystemQuery.class);
	
	/**
	 * 신규 유저를 등록합니다.	
	 * @throws Exception
	 */
	public static void newSystemInfo(String name, String major_version, String sub_version, String information) throws Exception {
		TadpoleSystemDAO dao = new TadpoleSystemDAO(name, major_version, sub_version, information);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("newSystem", dao); //$NON-NLS-1$
	}
	
	/**
	 * 운영중인 시스템 정보를 얻습니다.
	 *  
	 * @return
	 * @throws Exception
	 */
	public static TadpoleSystemDAO getSystemInfo() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (TadpoleSystemDAO)sqlClient.queryForObject("system_information");
	}
	
	/**
	 * update system version information
	 * 
	 * @param major_version
	 * @param sub_version
	 * @throws Exception
	 */
	public static void updateSystemVersion(String major_version, String sub_version) throws Exception {
		TadpoleSystemDAO dao = new TadpoleSystemDAO("", major_version, sub_version, "");
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("update_system", dao);
	}

}
