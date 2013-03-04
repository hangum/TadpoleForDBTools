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
package com.hangum.tadpole.system;

import java.util.List;

import junit.framework.TestCase;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserGroupDAO;
import com.hangum.tadpole.system.TadpoleSystemInitializer;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery;

/**
 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBQuery 시스템 UserDB 테스트}
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserDBQueryTest extends TestCase {
	public UserDBDAO userDb = null;
	public int groupSeq = 0;
	
	@Override
	protected void setUp() throws Exception {
		try {
			// group information
			boolean isGroup = TadpoleSystem_UserGroupQuery.isUserGroup(TadpoleSystem_UserGroupQueryTest.groupName);
			if(!isGroup) {				
				groupSeq = TadpoleSystem_UserGroupQuery.newUserGroup(TadpoleSystem_UserGroupQueryTest.groupName);				
			} else {
				List<UserGroupDAO> listGroup = TadpoleSystem_UserGroupQuery.getGroup();
				for (UserGroupDAO userGroupDAO : listGroup) {
					if(TadpoleSystem_UserGroupQueryTest.groupName.equals(userGroupDAO.getName())) {
						groupSeq = userGroupDAO.getSeq();
					}
				}
			}
			
		} catch (Exception e) {
			fail("fail user group" + e.getMessage());
			e.printStackTrace();
		}
		
		
		super.setUp();
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBQuery#newUserDB(UserDBDAO) UserDB등록 테스트}
	 */
	public void testNewUserDB() {
		UserDBDAO userDb2 = TadpoleSystemInitializer.getUserDB();
		userDb2.setDisplay_name("junit testName");
		
		try {			
			userDb = TadpoleSystem_UserDBQuery.newUserDB(userDb2, groupSeq);			
			
			assertNotNull(userDb);
		} catch (Exception e) {
			e.printStackTrace();
			fail("newUserDB exception");			
		}
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBQuery#getUserDB(int) 세션의 데이터를 가져오기위한 코드인데 어떻게 테스트 하지?}
	 */
	public void testGetUserDBInt() {
//		try {
//			List<UserDBDAO> listUserDb = TadpoleSystem_UserDBQuery.getUserDB(3);
////			if(listUserDb.size() ==0) fail("getUserDB exception");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("getUserDb exception is " + e.getMessage());
//		}
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBQuery#getUserDB(int) 세션의 데이터를 가져오기위한 코드인데 어떻게 테스트 하지?}
	 */
	public void testGetUserDB() {
//		fail("Not yet implemented");
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBQuery#getSpecificUserDB(int) 세션의 데이터를 가져오기위한 코드인데 어떻게 테스트 하지?}
	 */
	public void testGetSpecificUserDB() {
//		fail("Not yet implemented");
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBQuery#removeUserDB(int) userdb 삭제.}
	 */
	public void testRemoveUserDB() {
		try {
			TadpoleSystem_UserDBQuery.removeUserDB(3);
		} catch (Exception e) {
			e.printStackTrace();
			fail("userDb remove exception");
		}
	}

}
