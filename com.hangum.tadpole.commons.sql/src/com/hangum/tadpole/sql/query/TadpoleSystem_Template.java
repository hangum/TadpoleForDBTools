package com.hangum.tadpole.sql.query;

import java.util.List;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.sql.template.TeadpoleMonitoringTemplateDAO;
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
	 * @throws Exception
	 */
	public static List<TeadpoleMonitoringTemplateDAO> getMonitoringTemplate(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getDefaultMonitoringSQLTemplate", userDB.getDbms_type());
	}
}
