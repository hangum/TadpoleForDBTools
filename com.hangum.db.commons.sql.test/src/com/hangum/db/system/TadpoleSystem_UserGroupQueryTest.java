package com.hangum.db.system;

import java.util.List;

import com.hangum.db.dao.system.UserGroupDAO;

import junit.framework.TestCase;

/**
 * <code>com.hangum.db.system.TadpoleSystem_UserGroupQuery</code>
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserGroupQueryTest extends TestCase {
	
	private String groupName = "sample test group";
	
	public void testNewUserDB() {
		try {
			boolean isGroup = TadpoleSystem_UserGroupQuery.isUserGroup(groupName);
			if(isGroup) {
				
				int group = TadpoleSystem_UserGroupQuery.newUserDB(groupName);
				
			}
			
		} catch (Exception e) {
			fail("fail new user group" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	public void testIsUserGroup() {
		boolean isGroup = TadpoleSystem_UserGroupQuery.isUserGroup(groupName);
		if(isGroup) fail("fail dup user group");
	}

	public void testGetGroup() {
		try {
			List<UserGroupDAO> listGroup = TadpoleSystem_UserGroupQuery.getGroup();
			
		} catch (Exception e) {
			fail("fail list user group");			
			e.printStackTrace();
		}

	}

}
