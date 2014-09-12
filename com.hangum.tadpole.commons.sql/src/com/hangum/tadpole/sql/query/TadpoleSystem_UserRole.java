package com.hangum.tadpole.sql.query;

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
	 * 탈퇴.
	 * @param userSeq
	 * @throws Exception
	 */
	public static void withdrawal(int userSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userWithdrawal", userSeq); //$NON-NLS-1$
		sqlClient.update("dbWithdrawal", userSeq); //$NON-NLS-1$
	}
	
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
	 * 대표 role을 리턴합니다.
	 * 
	 * @param userDao
	 * @return
	 * @throws Exception
	 */
	public static UserRoleDAO representUserRole(UserDAO userDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (UserRoleDAO)sqlClient.queryForObject("representUserRole", userDao.getSeq()); 
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
	
	/**
	 * 사용자 그룹에 속한 유저가 있는지 검사합니다.
	 * 
	 * @param fineGroupSeq
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static boolean findGroupUserRole(int fineGroupSeq, int userSeq) throws Exception {
		UserRoleDAO userRoleDao = new UserRoleDAO();
		userRoleDao.setGroup_seq(fineGroupSeq);
		userRoleDao.setUser_seq(userSeq);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserRoleDAO> groupRoles = (List<UserRoleDAO>)sqlClient.queryForList("findGroupUserRole", userRoleDao); //$NON-NLS-1$
		
		return groupRoles.size() == 0 ? false:true;
	}
	
}
