///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.system;
//
//import java.util.List;
//
//import junit.framework.TestCase;
//
//import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
//import com.hangum.tadpole.sql.dao.system.UserDBDAO;
//import com.hangum.tadpole.sql.dao.system.UserDBResourceDAO;
//import com.hangum.tadpole.sql.dao.system.UserGroupDAO;
//import com.hangum.tadpole.sql.query.TadpoleSystemInitializer;
//import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
//import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource;
//import com.hangum.tadpole.sql.query.TadpoleSystem_UserGroupQuery;
//
///**
// * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource 시스템 리소스 테스트}
// * 
// * @author hangum
// *
// */
//public class TadpoleSystem_UserDBResourceTest extends TestCase {
//	
//	private UserDBDAO userDb = null;
//	
//	@Override
//	protected void setUp() throws Exception {
//		UserGroupDAO userGroup = new UserGroupDAO();
//		
//		try {
//			// group information
//			boolean isGroup = TadpoleSystem_UserGroupQuery.isUserGroup(TadpoleSystem_UserGroupQueryTest.groupName);
//			if(!isGroup) {				
//				userGroup = TadpoleSystem_UserGroupQuery.newUserGroup(TadpoleSystem_UserGroupQueryTest.groupName);				
//			} else {
//				List<UserGroupDAO> listGroup = TadpoleSystem_UserGroupQuery.getGroup();
//				for (UserGroupDAO userGroupDAO : listGroup) {
//					if(TadpoleSystem_UserGroupQueryTest.groupName.equals(userGroupDAO.getName())) {
////						groupSeq = userGroupDAO.getSeq();
//					}
//				}
//			}
//			
//			UserDBDAO userDb2 = TadpoleSystemInitializer.getUserDB();
//			userDb2.setDisplay_name("junit testName2");			
//			userDb = TadpoleSystem_UserDBQuery.newUserDB(userDb2, userDb2.getUser_seq());
//			
//		} catch (Exception e) {
//			fail("fail user group" + e.getMessage());
//			e.printStackTrace();
//		}
//		
//		
//		super.setUp();
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource#saveResource(UserDBDAO, com.hangum.tadpole.define.PublicTadpoleDefine.RESOURCE_TYPE, String, String) 시스템 리소스 저장 테스트}
//	 */
//	public void testSaveResource() {
////		try {
////			UserDBResourceDAO userDBResource = TadpoleSystem_UserDBResource.saveResource(userDb.getUser_seq(), userDb, PublicTadpoleDefine.RESOURCE_TYPE.SQL, "junit", "junit content");
////		} catch (Exception e) {
////			e.printStackTrace();
////			fail("SaveResouece");
////		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource#updateResource(UserDBResourceDAO, String) 시스템 리소스 수정 테스트}
//	 */
//	public void testUpdateResource() {
//		
//		try {
//			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
//			
//			TadpoleSystem_UserDBResource.updateResource(listResource.get(0), "junit update");
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("updateResource");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource#userDbErdTree(UserDBDAO) 시스템 리소스  테스트}
//	 */
//	public void testUserDbErdTree() {
//		try {
//			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
//			
//			assertEquals(listResource.size() != 0, true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("userDbErdTree");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource#userDBResourceDuplication(com.hangum.tadpole.define.PublicTadpoleDefine.RESOURCE_TYPE, int, int, String) 시스템 리소스 중복 테스트}
//	 */
//	public void testUserDBResourceDuplication() {
//		try {
//			boolean bool = TadpoleSystem_UserDBResource.userDBResourceDuplication(PublicTadpoleDefine.RESOURCE_TYPE.SQL, userDb.getUser_seq(), userDb.getSeq(), "junit");
//			
//			assertEquals(!bool, true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("userDBResourceDuplication");
//		}
//	}
//	
//	/**
//	 * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource#delete(UserDBResourceDAO) 시스템 리소스 삭제 테스트}
//	 */
//	public void testGetResourceData() {
//		try {
//			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
//			String str = TadpoleSystem_UserDBResource.getResourceData(listResource.get(0));
//			if(!str.equals("junit update")) fail("getResource exception");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("getResourceData");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.sql.query.TadpoleSystem_UserDBResource#delete(UserDBResourceDAO) 시스템 리소스 삭제 테스트}
//	 */
//	public void testDelete() {
//		try {
//			List<UserDBResourceDAO> listResource = TadpoleSystem_UserDBResource.userDbErdTree(userDb);
//			TadpoleSystem_UserDBResource.delete(listResource.get(0));			
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("delete");
//		}
//	}
//
//
//}
