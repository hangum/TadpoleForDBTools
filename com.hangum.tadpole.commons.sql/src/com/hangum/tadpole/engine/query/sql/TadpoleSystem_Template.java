package com.hangum.tadpole.engine.query.sql;

import java.util.List;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
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
	 * @throws Exception
	 */
	public static List<TeadpoleMonitoringTemplateDAO> getMonitoringTemplate(DBDefine dbDefine) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getDefaultMonitoringSQLTemplate", dbDefine.getDBToString());
	}
	
	/**
	 * save monitoring template
	 * 
	 * @param templateDao
	 * @throws Exception
	 */
	public static void saveMonitoringTemplate(TeadpoleMonitoringTemplateDAO templateDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("saveMonitoringTemplate", templateDao);
	}

	/**
	 * update monitoring template
	 * 
	 * @param updateUseTemplateDao
	 */
	public static void updateMonitoringTemplate(TeadpoleMonitoringTemplateDAO updateUseTemplateDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("updateUseTemplateDao", updateUseTemplateDao);
	}

}
