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
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.dao.system.UserGroupDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.system.TadpoleSystemInitializer;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.system.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.system.TadpoleSystem_UserGroupQuery;

/**
 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource 시스템 리소스 테스트}
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserDBResourceTest extends TestCase {
	
	private UserDBDAO userDb = null;
	
	@Override
	protected void setUp() throws Exception {
		int groupSeq = 0;
		
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
			
			UserDBDAO userDb2 = TadpoleSystemInitializer.getUserDB();
			userDb2.setDisplay_name("junit testName2");			
			userDb = TadpoleSystem_UserDBQuery.newUserDB(userDb2, groupSeq);
			
		} catch (Exception e) {
			fail("fail user group" + e.getMessage());
			e.printStackTrace();
		}
		
		
		super.setUp();
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource#saveResource(UserDBDAO, com.hangum.tadpole.define.Define.RESOURCE_TYPE, String, String) 시스템 리소스 저장 테스트}
	 */
	public void testSaveResource() {
		try {
			UserDBResourceDAO userDBResource = TadpoleSystem_UserDBResource.saveResource(userDb.getUser_seq(), userDb, Define.RESOURCE_TYPE.SQL, "junit", "junit content");
		} catch (Exception e) {
			e.printStackTrace();
			fail("SaveResouece");
		}
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource#updateResource(UserDBResourceDAO, String) 시스템 리소스 수정 테스트}
	 */
	public void testUpdateResource() {
		
		try {
			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
			
			TadpoleSystem_UserDBResource.updateResource(listResource.get(0), "junit update");
		} catch (Exception e) {
			e.printStackTrace();
			fail("updateResource");
		}
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource#userDbErdTree(UserDBDAO) 시스템 리소스  테스트}
	 */
	public void testUserDbErdTree() {
		try {
			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
			
			assertEquals(listResource.size() != 0, true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("userDbErdTree");
		}
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource#userDBResourceDuplication(com.hangum.tadpole.define.Define.RESOURCE_TYPE, int, int, String) 시스템 리소스 중복 테스트}
	 */
	public void testUserDBResourceDuplication() {
		try {
			boolean bool = TadpoleSystem_UserDBResource.userDBResourceDuplication(Define.RESOURCE_TYPE.SQL, userDb.getUser_seq(), userDb.getSeq(), "junit");
			
			assertEquals(!bool, true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("userDBResourceDuplication");
		}
	}
	
	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource#delete(UserDBResourceDAO) 시스템 리소스 삭제 테스트}
	 */
	public void testGetResourceData() {
		try {
			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
			String str = TadpoleSystem_UserDBResource.getResourceData(listResource.get(0));
			if(!str.equals("junit update")) fail("getResource exception");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("getResourceData");
		}
	}

	/**
	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserDBResource#delete(UserDBResourceDAO) 시스템 리소스 삭제 테스트}
	 */
	public void testDelete() {
		try {
			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
			TadpoleSystem_UserDBResource.delete(listResource.get(0));			
		} catch (Exception e) {
			e.printStackTrace();
			fail("delete");
		}
	}


}
