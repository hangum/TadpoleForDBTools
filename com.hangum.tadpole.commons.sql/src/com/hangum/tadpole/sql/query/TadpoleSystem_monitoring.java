package com.hangum.tadpole.sql.query;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringMainDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.sql.dao.system.sql.template.TeadpoleMonitoringTemplateDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * monitoring 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_monitoring {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_monitoring.class);
	
	/**
	 * update parameter
	 * 
	 * @param dao
	 * @throws Exception
	 */
	public static void updateParameter(MonitoringIndexDAO dao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateIndexParameter", dao);
	}
	
	/**
	 * monitoring list
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<MonitoringIndexDAO> getMonitoring() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getAllMonitoringList");
	}

	/**
	 * 모니터링 데이터를 저장합니다. 
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static void saveMonitoring(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		// 현재는 템플릿 테이블에서 읽어서 저장하도록 합니다. 
		 List<TeadpoleMonitoringTemplateDAO> listSQLTemplate = TadpoleSystem_Template.getMonitoringTemplate(userDB);
		 
		 for (TeadpoleMonitoringTemplateDAO tadpoleSQLTemplateDAO : listSQLTemplate) {
			 MonitoringMainDAO mainDao = new MonitoringMainDAO();
			 mainDao.setUser_seq(userDB.getUser_seq());
			 mainDao.setDb_seq(userDB.getSeq());
			 mainDao.setRead_method("SQL");
			 mainDao.setTitle(tadpoleSQLTemplateDAO.getTitle());
			 mainDao.setDescription(tadpoleSQLTemplateDAO.getDescription());
			 mainDao.setCron_exp("*/10 * * * * ?");
			 mainDao.setQuery(tadpoleSQLTemplateDAO.getQuery());
			 mainDao.setIs_result_save(PublicTadpoleDefine.YES_NO.YES.toString());
			 
			 mainDao = (MonitoringMainDAO)sqlClient.insert("insertMonitoringMain", mainDao);
			 MonitoringIndexDAO indexDao = new MonitoringIndexDAO();
			 indexDao.setMonitoring_seq(mainDao.getSeq());
			 indexDao.setCondition_type(tadpoleSQLTemplateDAO.getCondition_type());
			 indexDao.setMonitoring_type(tadpoleSQLTemplateDAO.getMonitoring_type());
			 indexDao.setAfter_type(tadpoleSQLTemplateDAO.getAfter_type());
			 indexDao.setIndex_nm(tadpoleSQLTemplateDAO.getIndex_nm());
			 indexDao.setCondition_value(tadpoleSQLTemplateDAO.getCondition_value());

			 indexDao.setParam_1_column(tadpoleSQLTemplateDAO.getParam_1_column());
			 indexDao.setParam_1_init_value(tadpoleSQLTemplateDAO.getParam_1_init_value());
			 indexDao.setParam_2_column(tadpoleSQLTemplateDAO.getParam_2_column());
			 indexDao.setParam_2_init_value(tadpoleSQLTemplateDAO.getParam_2_init_value());
			 
			 sqlClient.insert("insertMonitoringIndex", indexDao);
		}
	}

	/**
	 * MonitoringResult save
	 * 
	 * @param listMonitoringIndex
	 */
	public static void saveMonitoringResult(List<MonitoringResultDAO> listMonitoringIndex) {
		SqlMapClient sqlClient = null;
		
		try {
			sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			sqlClient.startTransaction();
			sqlClient.startBatch();
		
			for (MonitoringResultDAO resultDAO : listMonitoringIndex) {
				sqlClient.insert("insertMonitoringResult", resultDAO);
			}
			sqlClient.executeBatch();
			sqlClient.commitTransaction();
		} catch(Exception e) {
			logger.error("Monitoring result save exception", e);
		} finally {
			try {
				if(sqlClient != null) sqlClient.endTransaction();
			} catch(Exception e) {
				logger.error("saveMonitoring Result ", e);
			}
		}
	}

}
