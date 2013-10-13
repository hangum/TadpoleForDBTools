package com.hangum.tadpole.sql.system;

import java.util.List;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserRoleDAO;
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
	
	/**
	 * 사용자 user_role중에 admin, dab, manager 롤을 찾습니다.
	 * 
	 * @param loginUserDao
	 * @return
	 * @throws Exception
	 */
	public static List<UserRoleDAO> findUserRole(UserDAO loginUserDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserRoleDAO> groupRoles = (List<UserRoleDAO>)sqlClient.queryForList("findUserRole", loginUserDao.getSeq()); //$NON-NLS-1$
		
		return groupRoles;
	}
	
}
