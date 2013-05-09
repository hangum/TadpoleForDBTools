package com.hangum.tadpole.system;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserRoleDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tadpole basic table (User_group_role)
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserRole {
	
	/**
	 * insert data the user_goup_role.
	 * 
	 * @param groupSeq
	 * @param userSeq
	 * @param roleType
	 * @param aprovalYn
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static UserRoleDAO newUserRole(int groupSeq, int userSeq, String roleType, String aprovalYn, String name) throws Exception {
		UserRoleDAO groupRole = new UserRoleDAO();
		groupRole.setGroup_seq(groupSeq);
		groupRole.setUser_seq(userSeq);
		groupRole.setRole_type(roleType);
		groupRole.setApproval_yn(aprovalYn);
		groupRole.setName(name);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		groupRole = (UserRoleDAO)sqlClient.insert("userUserRoleInsert", groupRole); //$NON-NLS-1$
		
		return groupRole;
	}
	
	
}
