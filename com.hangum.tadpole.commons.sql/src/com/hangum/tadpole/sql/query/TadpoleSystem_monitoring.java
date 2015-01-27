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
			 
			 sqlClient.insert("insertMonitoringIndex", indexDao);
		}
	}

	/**
	 * MonitoringResult save
	 * 	after monitoring result data
	 * - save data
	 * - user notification
	 * - cache monitoring data 
	 * 
	 * @param strEmail
	 * @param listMonitoringIndex
	 */
	public static void saveMonitoringResult(String strEmail, List<MonitoringIndexDAO> listMonitoringIndex) {//MonitoringIndexDAO indexDAO, JsonObject jsonObj, String strIndexValue, boolean boolSuccess) {
		
		for (MonitoringIndexDAO indexDAO : listMonitoringIndex) {
			MonitoringResultDAO resultDao = new MonitoringResultDAO();

			resultDao.setMonitoring_seq(indexDAO.getMonitoring_seq());
			resultDao.setMonitoring_index_seq(indexDAO.getSeq());
			resultDao.setResult(indexDAO.isError()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
			
			JsonObject jsonObj = indexDAO.getResultJson();
			String strIndexValue = jsonObj.get(indexDAO.getIndex_nm().toLowerCase()).getAsString();
			resultDao.setIndex_value(strIndexValue);
			resultDao.setSystem_description(String.format("%s %s %s", indexDAO.getCondition_value(), indexDAO.getCondition_type(), strIndexValue));
			resultDao.setQuery_result(jsonObj.toString());

			try {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
				sqlClient.insert("insertMonitoringResult", resultDao);
			} catch(Exception e) {
				logger.error("Monitoring result save exception", e);
			}
			
			// 캐쉬에 마지막 데이터를 쌓아 놓습니다. 
			
			
			// 실패일 경우 후속 처리를 한다.(세션 삭제, 이메일 보내기 등)
			if(indexDAO.isError()) {
				
//				try {
//					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//					sqlClient.insert("insertMailBag", resultDao);
//				} catch(Exception e) {
//					logger.error("save image bag", e);
//				}
			}
			
		}
	}

}
