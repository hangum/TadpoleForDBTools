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

import com.hangum.tadpole.dao.system.UserGroupDAO;
import com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery;

import junit.framework.TestCase;

/**
 * {@link com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery 유저그룹테스트}
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserGroupQueryTest extends TestCase {
	
	/** test user group */
	public static String groupName = "sample test group";
	
	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery#newUserGroup(String) 신규 유저그룹 등록 테스트}
	 */
	public void testNewUserGroup() {
		try {
			boolean isGroup = TadpoleSystem_UserGroupQuery.isUserGroup(groupName);
			if(!isGroup) {
				
				int group = TadpoleSystem_UserGroupQuery.newUserGroup(groupName);
				
			}
			
		} catch (Exception e) {
			fail("fail new user group" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery#isUserGroup(String) 유저그룹 존재}
	 */
	public void testIsUserGroup() {
		boolean isGroup = TadpoleSystem_UserGroupQuery.isUserGroup(groupName);
		assertEquals(isGroup, true);
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery#getGroup() 유저그룹 리스트 테스트}
	 */
	public void testGetGroup() {
		boolean isGroupExist = false;
		try {
			List<UserGroupDAO> listGroup = TadpoleSystem_UserGroupQuery.getGroup();
			for (UserGroupDAO userGroupDAO : listGroup) {
				if(groupName.equals(userGroupDAO.getName())) {
					isGroupExist = true;
				}
			}
			
			assertEquals(isGroupExist, true);
			
		} catch (Exception e) {
			fail("fail list user group");			
			e.printStackTrace();
		}

	}

}
