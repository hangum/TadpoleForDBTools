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
package com.hangum.tadpole.sql.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserGroupDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * user_db table 관련 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserGroupQuery {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserGroupQuery.class);
	
	/**
	 * user group이 있는지 검사한다.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isUserGroup(String name) {
		UserGroupDAO group = new UserGroupDAO();
		group.setName(name);
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			List<UserGroupDAO> isUserDB = (List<UserGroupDAO>)sqlClient.queryForList("isUserGroup", group);
			
			if(isUserDB.size() >= 1) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		
	}
	
	/**
	 * 신규 유저그룹를 등록합니다.
	 * @param name
	 */
	public static UserGroupDAO newUserGroup(String name) throws Exception {
		UserGroupDAO groupDao = new UserGroupDAO();
		groupDao.setName(name);
		
		// 기존에 등록 되어 있는지 검사한다.
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserGroupDAO> isUserDB = (List<UserGroupDAO>)sqlClient.queryForList("isUserGroup", groupDao); //$NON-NLS-1$
		
		// 존재하면.
		if(isUserDB.size() >= 1) {
			throw new Exception("Already Group exists.");
		}

		// 신규 유저를 등록합니다.
		Integer seq = (Integer)sqlClient.insert("newGroup", groupDao); //$NON-NLS-1$
		groupDao.setSeq(seq);
		
		return groupDao;
	}
	
	/**
	 * group목록을 리턴합니다.
	 * @return
	 * @throws Exception
	 */
	public static List<UserGroupDAO> getGroup() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<UserGroupDAO>)sqlClient.queryForList("userDBList"); //$NON-NLS-1$
	}
	
	/**
	 * group정보를 리턴합니다.
	 * @param groupSeq
	 * @return
	 * @throws Exception
	 */
	public static String findGroupName(int groupSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return ""+sqlClient.queryForObject("findGroupName", groupSeq); //$NON-NLS-1$
	}
	
//	public static void removeUserDB(int seq) throws Exception {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
//		sqlClient.delete("userDBDelete", seq); //$NON-NLS-1$
//		sqlClient.delete("userDBErdDeleteAtDB", seq); //$NON-NLS-1$
//	}
}
