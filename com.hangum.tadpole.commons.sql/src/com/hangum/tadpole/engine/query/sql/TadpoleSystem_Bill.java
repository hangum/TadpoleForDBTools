package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
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
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("userBillList", queryMap);
	}
	
	/**
	 * insert bill
	 * @param dao
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void insertBill(UserBillDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("insertBill", dao); //$NON-NLS-1$
	}
}
