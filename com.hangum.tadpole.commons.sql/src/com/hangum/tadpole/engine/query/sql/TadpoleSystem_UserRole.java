package com.hangum.tadpole.engine.query.sql;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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
	 * 해당 디비에 사용자 롤이 추가 되어 있는지 검사합니다. 
	 * 
	 * @param userDB
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static boolean isDBAddRole(UserDBDAO userDB, UserDAO user) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		Map<String, Integer> mapParameter = new HashMap<>();
		mapParameter.put("db_seq", userDB.getSeq());
		mapParameter.put("user_seq", user.getSeq());
		
		List roleList = sqlClient.queryForList("isDBAddRole", mapParameter);
		return roleList.size()==0;
	}
	
	/**
	 * insert table
	 * 
	 * @param userSeq
	 * @param dbSeq
	 * @param roleType
	 * @throws Exception
	 */
	public static TadpoleUserDbRoleDAO insertTadpoleUserDBRole(int userSeq, int dbSeq, String roleType) throws Exception {
		TadpoleUserDbRoleDAO userDBRoleDao = new TadpoleUserDbRoleDAO();
		userDBRoleDao.setUser_seq(userSeq);
		userDBRoleDao.setDb_seq(dbSeq);
		userDBRoleDao.setRole_id(roleType);
		userDBRoleDao.setTerms_of_use_endtime(new Timestamp(System.currentTimeMillis()));
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 100);
		userDBRoleDao.setTerms_of_use_endtime(new Timestamp(cal.getTimeInMillis()));
		
		// Insert tadpole_user_db_role table. 
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		TadpoleUserDbRoleDAO roleDao = (TadpoleUserDbRoleDAO)sqlClient.insert("userDBRoleInsert", userDBRoleDao);
		
		return roleDao;
	}
	
	
	/**
	 * insert tadpole_user_db_role table
	 * 
	 * @param userSeq
	 * @param dbSeq
	 * @param roleId
	 * @param accessIp
	 * @param terms_of_use_starttime
	 * @param terms_of_use_endtime
	 * 
	 * @throws Exception
	 */
	public static void insertTadpoleUserDBRole(int userSeq, int dbSeq, String roleType, String accessIp, Timestamp terms_of_use_starttime, Timestamp terms_of_use_endtime) throws Exception {
		TadpoleUserDbRoleDAO userDBRoleDao = new TadpoleUserDbRoleDAO();
		userDBRoleDao.setUser_seq(userSeq);
		userDBRoleDao.setDb_seq(dbSeq);
		userDBRoleDao.setRole_id(roleType);
		userDBRoleDao.setAccess_ip(accessIp);
		userDBRoleDao.setTerms_of_use_starttime(terms_of_use_starttime);
		userDBRoleDao.setTerms_of_use_endtime(terms_of_use_endtime);
		
		// Insert tadpole_user_db_role table. 
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		userDBRoleDao = (TadpoleUserDbRoleDAO)sqlClient.insert("userDBRoleInsert", userDBRoleDao);
		
		// save db access control
		TadpoleSystem_AccessControl.saveDBAccessControl(userDBRoleDao);
	}
	
	/**
	 * update user role
	 * 
	 * @param userDBRoleDao
	 */
	public static void updateTadpoleUserDBRole(TadpoleUserDbRoleDAO userDBRoleDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("userDBRoleUpdate", userDBRoleDao);
	}

	/**
	 * user list
	 * @param userDB
	 */
	public static List getUserRoleList(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getUserRoleList", userDB);
	}

//	/**
//	 * insert data the user_goup_role.
//	 * 
//	 * @param groupSeq
//	 * @param userSeq
//	 * @param roleType
//	 * @param aprovalYn
//	 * @param name
//	 * @return
//	 * @throws Exception
//	 */
//	public static UserRoleDAO newUserRole(/*int groupSeq,*/ int userSeq, String roleType, String aprovalYn, String name) throws Exception {
//		UserRoleDAO groupRole = new UserRoleDAO();
////		groupRole.setGroup_seq(groupSeq);
//		groupRole.setUser_seq(userSeq);
//		groupRole.setRole_type(roleType);
//		groupRole.setApproval_yn(aprovalYn);
//		groupRole.setName(name);
//		
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		groupRole = (UserRoleDAO)sqlClient.insert("userUserRoleInsert", groupRole); //$NON-NLS-1$
//		
//		return groupRole;
//	}
//	
//	/**
//	 * usergroup withdrawal 
//	 * @param groupseq
//	 * @param userSeq
//	 * @throws Exception
//	 */
//	public static void withdrawalUserRole(int groupSeq, int userSeq) throws Exception {
//		UserRoleDAO userRole = new UserRoleDAO();
//		userRole.setGroup_seq(groupSeq);
//		userRole.setUser_seq(userSeq);
//		
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		sqlClient.update("withdrawalUserRole", userRole); //$NON-NLS-1$
//	}
//	
//	/**
//	 * 대표 role을 리턴합니다.
//	 * 
//	 * @param userDao
//	 * @return
//	 * @throws Exception
//	 */
//	public static UserRoleDAO representUserRole(UserDAO userDao) throws Exception {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		return (UserRoleDAO)sqlClient.queryForObject("representUserRole", userDao.getSeq()); 
//	}
//	
//	/**
//	 * 사용자 user_role중에 admin, dab, manager 롤을 찾습니다.
//	 * 
//	 * @param loginUserDao
//	 * @return
//	 * @throws Exception
//	 */
//	public static List<UserRoleDAO> findUserRole(UserDAO loginUserDao) throws Exception {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		List<UserRoleDAO> groupRoles = (List<UserRoleDAO>)sqlClient.queryForList("findUserRole", loginUserDao.getSeq()); //$NON-NLS-1$
//		
//		return groupRoles;
//	}
//	
//	/**
//	 * 사용자 그룹에 속한 유저가 있는지 검사합니다.
//	 * 
//	 * @param fineGroupSeq
//	 * @param userSeq
//	 * @return
//	 * @throws Exception
//	 */
//	public static boolean findGroupUserRole(int fineGroupSeq, int userSeq) throws Exception {
//		UserRoleDAO userRoleDao = new UserRoleDAO();
//		userRoleDao.setGroup_seq(fineGroupSeq);
//		userRoleDao.setUser_seq(userSeq);
//		
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		List<UserRoleDAO> groupRoles = (List<UserRoleDAO>)sqlClient.queryForList("findGroupUserRole", userRoleDao); //$NON-NLS-1$
//		
//		return groupRoles.size() == 0 ? false:true;
//	}
	
}
