package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.util.DateUtil;
import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.bill.AssignedServiceDAO;
import com.hangum.tadpole.engine.query.dao.system.bill.UserBillDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_Bill {

	/**
	 * user bill list
	 * 
	 * @return
	 */
	public static List<UserBillDAO> getBillList() throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", SessionManager.getUserSeq());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("userBillList", queryMap);
	}
	
	/**
	 * insert bill
	 * @param dao
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static UserBillDAO insertBill(UserBillDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return (UserBillDAO)sqlClient.insert("insertBill", dao); //$NON-NLS-1$
	}
	
	/**
	 * start service
	 * 
	 * @param dao
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void startService(UserBillDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateStartService", dao); //$NON-NLS-1$
	}
	
	
	/**
	 * 이미 서비스에 사용자가 추가되었는지 검사한다.
	 * 
	 * @param bill 
	 * @param assignedService
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void insertStartUserService(UserBillDAO bill, UserDAO user, AssignedServiceDAO assignedService) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		
		List<AssignedServiceDAO> list = sqlClient.queryForList("getAlreadyService", assignedService);
		if(bill.getEa() > list.size()) {
			for (AssignedServiceDAO asObj : list) {
				if(asObj.getUser_seq() == user.getSeq()) {
					throw new Exception(Messages.get().AlreadyUseService);
				}
			}
			sqlClient.insert("insertStartUserService", assignedService);
			
			// 마지막 결제 정보를 변경한
			updateUserInfo(user, assignedService);

		} else {
			throw new Exception(Messages.get().overflowUseService);
		}
	}
	
	/**
	 * 마지막 사용자 정보를 업데이트한다.
	 * 
	 * @param user
	 * @param assignedService
	 * @throws Exception
	 */
	private static void updateUserInfo(UserDAO user, AssignedServiceDAO assignedService)  throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		
		// user의 마지막 정보를 가져온다.
		UserDAO userDaoLastInfo = TadpoleSystem_UserQuery.findUser(user.getEmail());

		// 사용자 서비스를 늘려준다.
		if(userDaoLastInfo.getService_end().getTime() < assignedService.getService_end_data().getTime()) {
			sqlClient.update("updateUserServiceDate", assignedService);
		}
		
		// 사용자 디비 추가 제한이 5보다 적으면..
		if(userDaoLastInfo.getLimit_add_db_cnt() < 5) {
			sqlClient.update("updateUserLimitDBCnt", assignedService);
		}
	}
	
	/**
	 * 각 사용자의 서비스 리스트
	 * @param dao
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<AssignedServiceDAO> getUserList(UserBillDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getEachServiceUser", dao);
	}
	
	/**
	 * delete user service
	 * 
	 * @param dao
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void deleteUserService(AssignedServiceDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("deleteUserService", dao);
		
		// 사용자 플랜중에 가장 큰 플랜으로 업데이트 한다.
		Timestamp endDate = calcMaxServiceDate(dao.getUser_seq());
		AssignedServiceDAO assignDao = new AssignedServiceDAO();
		assignDao.setUser_seq(dao.getUser_seq());
		assignDao.setService_end_data(endDate);
		sqlClient.update("updateUserServiceDate", assignDao);
	}
	
	/**
	 * calculate max service date
	 * 
	 * @param userSeq
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static Timestamp calcMaxServiceDate(int userSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		Object objDao = sqlClient.queryForObject("calcMaxServiceDate", userSeq);
		if(objDao != null) {
			AssignedServiceDAO dao = (AssignedServiceDAO)objDao;
			
			return dao.getService_end_data();
		} else {
			return new Timestamp(DateUtil.afterMonthToMillis(1));
		}
		
	}
}
