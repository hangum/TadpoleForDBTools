package com.hangum.tadpole.sql.query;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.commons.TadpoleSequenceDAO;
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
	 * @throws Exception
	 */
	public static TadpoleSequenceDAO getSequence(TadpoleSequenceDAO dao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		dao = (TadpoleSequenceDAO)sqlClient.queryForObject("lastSequene", dao);
		
		dao.setNo(dao.getNo()+1);
		sqlClient.update("updateSequence", dao);
		
		return dao;
	}

}
