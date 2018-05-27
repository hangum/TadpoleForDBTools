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

import junit.framework.TestCase;

/**
 * {@link com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery 시스템엔진디비 유저 테스트}
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserQueryTest extends TestCase {
//	public static String email = "test.admin@gmail.com";
//	public static String passwd = "test1234";
//	public static String name = "cho test";
//	public static USER_TYPE userType = USER_TYPE.ADMIN;
//	
//	public static UserDAO defaultUserDAO = new UserDAO(email, passwd, name, null);
//			
//	
//	@Override
//	protected void setUp() throws Exception {
//		boolean isGroupExist = false;
//		try {
//			List<UserGroupDAO> listGroup = TadpoleSystem_UserGroupQuery.getGroup();
//			for (UserGroupDAO userGroupDAO : listGroup) {
//				if(TadpoleSystem_UserGroupQueryTest.groupName.equals(userGroupDAO.getName())) {
//					isGroupExist = true;
//					break;
//				}
//			}
//			
//			if(!isGroupExist) {
//				groupSeq = TadpoleSystem_UserGroupQuery.newUserGroup(TadpoleSystem_UserGroupQueryTest.groupName);
//			}
//			
//		} catch (Exception e) {
//			fail("fail list user group");			
//			e.printStackTrace();
//		}
//		
//		super.setUp();
//	}
//	
////	/**
////	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#deleteUser(UserDAO) 유저삭제} 
////	 */
////	public void testDeleteUser() {
////		try {
////			TadpoleSystem_UserQuery.deleteUser(defaultUserDAO);
////		} catch (Exception e) {
////			fail("delete user");
////		}
////	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#newUser(int, String, String, String, USER_TYPE) 시스템엔진디비 유저 입력}
//	 */
//	public void testNewUserIntStringStringStringUSER_TYPE() {
//		
//		try {
//			defaultUserDAO = TadpoleSystem_UserQuery.newUser(email, passwd, name, userType);
//			
//			assertEquals(defaultUserDAO != null, true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("fail add user");		
//		}
//	
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#newUser(int, String, String, String, String, String) 유저 중복입력 테스트}
//	 */
//	public void testNewUserIntStringStringStringStringString() {
//		
//		try {
//			defaultUserDAO = TadpoleSystem_UserQuery.newUser(email, passwd, name, "Yes");
//			
//			fail("fail user duplication ");
//		} catch (Exception e) {
//			// success test		
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#newUser(int, String, String, String, String) 유저 중복입력 테스트}
//	 */
//	public void testNewUserIntStringStringStringString() {
//		try {
//			defaultUserDAO = TadpoleSystem_UserQuery.newUser(groupSeq, email, passwd, name, userType.toString());
//			
//			fail("fail user duplication ");
//		} catch (Exception e) {
//			// success test		
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#isDuplication(String) 유저 중복 테스트}
//	 */
//	public void testIsDuplication() {
//		try {
//			boolean isDuplication = TadpoleSystem_UserQuery.isDuplication(email);
//
//			assertEquals(isDuplication, false);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("isDuplication exception");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#login(String, String) 로그인테스트}
//	 */
//	public void testLogin() {
//		try {
//			UserDAO user = TadpoleSystem_UserQuery.login(email, passwd);
//			assertEquals(user == null, false);
//		} catch (Exception e) {			
////			e.printStackTrace();
////			fail("login test fail");
//			// 관리자 인증 안되었을경우 처리 필요			
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#login(String, String) 로그인테스트}
//	 */
//	public void testGetGroupManager() {
//		try {
//			UserDAO user = TadpoleSystem_UserQuery.getGroupManager(groupSeq);
//
//		} catch (Exception e) {			
//			e.printStackTrace();
//			fail("GetGroupManager test fail");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#loginUserCount admin user가 한명이라면 로그인 화면에서 기본 유저로 설정 테스트}
//	 */
//	public void testLoginUserCount() {
//		try {
//			UserDAO user = TadpoleSystem_UserQuery.loginUserCount();
//
//		} catch (Exception e) {			
//			e.printStackTrace();
//			fail("LoginUserCount test fail");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#getUserListPermission 유저 카운트}
//	 */
//	public void testGetUserListPermission() {
//		try {
//			List<UserGroupAUserDAO> userList = TadpoleSystem_UserQuery.getUserListPermission();
//			assertEquals(userList.size() == 0, false);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			fail("getUserListPermission test fail");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#getUserListPermission(int) 그룹 유저 카운트 테스트}
//	 */
//	public void testGetUserListPermissionInt() {
//		try {
//			List<UserGroupAUserDAO> userList = TadpoleSystem_UserQuery.getUserListPermission(groupSeq);
//			assertEquals(userList.size() == 0, false);
//		} catch (Exception e) {			
//			e.printStackTrace();
//			fail("getUserListPermission test fail");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#updateUserData(UserDAO) 업데이트 유저 테스트}
//	 */
//	public void testUpdateUserData() {
//		try {
//			TadpoleSystem_UserQuery.updateUserData(defaultUserDAO);
//			
//		} catch (Exception e) {			
//			e.printStackTrace();
//			fail("UpdateUserData test fail");
//		}
//	}
//
//	/**
//	 * {@link com.hangum.tadpole.system.TadpoleSystem_UserQuery#updateUserPassword(UserDAO) 업데이트 유저 패스워드 테스트}
//	 */
//	public void testUpdateUserPassword() {
//		try {
//			TadpoleSystem_UserQuery.updateUserPassword(defaultUserDAO);
//			
//		} catch (Exception e) {			
//			e.printStackTrace();
//			fail("updateUserPassword test fail");
//		}
//	}

}
