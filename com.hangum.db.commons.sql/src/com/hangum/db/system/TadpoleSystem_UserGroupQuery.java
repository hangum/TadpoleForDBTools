/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.system.UserGroupDAO;
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
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
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
	public static int newUserGroup(String name) throws Exception {
		UserGroupDAO group = new UserGroupDAO();
		group.setName(name);
		
		// 기존에 등록 되어 있는지 검사한다
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		List<UserGroupDAO> isUserDB = (List<UserGroupDAO>)sqlClient.queryForList("isUserGroup", group); //$NON-NLS-1$
		
		// 존재하
		if(isUserDB.size() >= 1) {
			return -999;
		}

		// 신규 유저를 등록합니다.
		Integer seq = (Integer)sqlClient.insert("newGroup", group); //$NON-NLS-1$
		
		return seq.intValue();
	}
	
	/**
	 * group목록을 리턴합니다.
	 * @return
	 * @throws Exception
	 */
	public static List<UserGroupDAO> getGroup() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return (List<UserGroupDAO>)sqlClient.queryForList("userDBList"); //$NON-NLS-1$
	}
	
	/**
	 * group정보를 리턴합니다.
	 * @param groupSeq
	 * @return
	 * @throws Exception
	 */
	public static String findGroupName(int groupSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return ""+sqlClient.queryForObject("findGroupName", groupSeq); //$NON-NLS-1$
	}
	
//	public static void removeUserDB(int seq) throws Exception {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
//		sqlClient.delete("userDBDelete", seq); //$NON-NLS-1$
//		sqlClient.delete("userDBErdDeleteAtDB", seq); //$NON-NLS-1$
//	}
}
