package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.commons.TadpoleSequenceDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tadpole System sequence
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_Sequence {
	public static final String KEY_MONITORING = "MONITORING";

	/**
	 * sequenc
	 *
	 * @param dao
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static TadpoleSequenceDAO getSequence(TadpoleSequenceDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		dao = (TadpoleSequenceDAO)sqlClient.queryForObject("lastSequene", dao);
		
		dao.setNo(dao.getNo()+1);
		sqlClient.update("updateSequence", dao);
		
		return dao;
	}

}
