package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.List;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.sql.template.TeadpoleMonitoringTemplateDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * SQL 템플릿  
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_Template {

	/**
	 * 모니터링 템플릿
	 * 
	 * @param dbDefine
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<TeadpoleMonitoringTemplateDAO> getMonitoringTemplate(DBDefine dbDefine) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getDefaultMonitoringSQLTemplate", dbDefine.getDBToString());
	}
	
	/**
	 * save monitoring template
	 * 
	 * @param templateDao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void saveMonitoringTemplate(TeadpoleMonitoringTemplateDAO templateDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.insert("saveMonitoringTemplate", templateDao);
	}

	/**
	 * update monitoring template
	 * 
	 * @param updateUseTemplateDao
	 */
	public static void updateMonitoringTemplate(TeadpoleMonitoringTemplateDAO updateUseTemplateDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.insert("updateUseTemplateDao", updateUseTemplateDao);
	}

}
