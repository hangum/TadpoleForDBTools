package com.hangum.tadpole.monitoring.core.jobs.monitoring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_monitoring;
import com.hangum.tadpole.monitoring.core.manager.cache.MonitoringCacheRepository;
import com.hangum.tadpole.monitoring.core.manager.event.EventManager;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine;
import com.hangum.tadpole.monitoring.core.utils.Utils;

/**
 * Tadpole monitoring job
 * 
 * @author hangum
 *
 */
public class MonitoringJob implements Job {
	private static final Logger logger = Logger.getLogger(MonitoringJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<MonitoringResultDAO> listMonitoringResult = new ArrayList<MonitoringResultDAO>();

		try {
			String strRelationUUID = UUID.randomUUID().toString();
			
			for (MonitoringIndexDAO monitoringIndexDAO : TadpoleSystem_monitoring.getAllMonitoringList()) {
				if(logger.isDebugEnabled()) logger.debug("==[title]===> " + monitoringIndexDAO.getTitle());
				UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(monitoringIndexDAO.getDb_seq());
				
				MonitoringResultDAO resultDao = null;
				try {
					JsonArray jsonArray = Utils.selectToJson(userDB, monitoringIndexDAO);
					for(int i=0; i<jsonArray.size(); i++) {
						resultDao = new MonitoringResultDAO();

						resultDao.setUnique_id(UUID.randomUUID().toString());
						resultDao.setUserDB(userDB);
						
						resultDao.setMonitoring_seq(monitoringIndexDAO.getMonitoring_seq());
						resultDao.setMonitoring_index_seq(monitoringIndexDAO.getSeq());
						resultDao.setMonitoring_type(monitoringIndexDAO.getMonitoring_type());
						resultDao.setKpi_type(monitoringIndexDAO.getKpi_type());
						
						JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
						JsonPrimitive jsonValue = jsonObj.getAsJsonPrimitive(monitoringIndexDAO.getIndex_nm().toLowerCase());
						String strIndexValue = jsonValue != null?jsonValue.getAsString():"";
						resultDao.setIndex_value(strIndexValue);
						
						//결과를 저장한다.
						boolean isError = MonitorErrorChecker.isError(jsonObj, monitoringIndexDAO);
						resultDao.setResult(isError?MonitoringDefine.MONITORING_STATUS.WARRING.getName():MonitoringDefine.MONITORING_STATUS.CLEAN.getName());
						resultDao.setSystem_description(String.format("%s %s %s", monitoringIndexDAO.getCondition_value(), monitoringIndexDAO.getCondition_type(), strIndexValue));
						
						resultDao.setUser_seq(userDB.getUser_seq());
						resultDao.setDb_seq(userDB.getSeq());
						
						if(PublicTadpoleDefine.YES_NO.YES.name().equals(monitoringIndexDAO.getIs_result_save())) {
							resultDao.setQuery_result(jsonObj.toString());
						}
						resultDao.setMonitoringIndexDAO(monitoringIndexDAO);
						resultDao.setCreate_time(new Timestamp(System.currentTimeMillis()));
						if(isError && PublicTadpoleDefine.YES_NO.YES.name().equals(monitoringIndexDAO.getIs_snapshot_save())) {
							resultDao.setSnapshot(Utils.getDBVariable(userDB));	
						}
						
						// 후속작업을 위해 사용자 별로 모니터링 데이터를 모읍니다.
						listMonitoringResult.add(resultDao);
					}	// end for
					
					// update index parameter.
					updateParameterValue(monitoringIndexDAO, jsonArray);

				} catch (Exception e) {
					logger.error("monitoring Job exception " + monitoringIndexDAO.getTitle(), e);
					
					// 오류를 모니터링 항목에 넣습니다.
					resultDao = new MonitoringResultDAO();
					resultDao.setUnique_id(UUID.randomUUID().toString());
					resultDao.setUserDB(userDB);
					
					resultDao.setMonitoring_seq(monitoringIndexDAO.getMonitoring_seq());
					resultDao.setMonitoring_index_seq(monitoringIndexDAO.getSeq());
					resultDao.setMonitoring_type(monitoringIndexDAO.getMonitoring_type());
					resultDao.setKpi_type(monitoringIndexDAO.getKpi_type());
					
					resultDao.setIndex_value("0");
					
					//결과를 저장한다.
					resultDao.setResult(MonitoringDefine.MONITORING_STATUS.CRITICAL.toString());
					resultDao.setSystem_description(e.getMessage());
					
					resultDao.setUser_seq(userDB.getUser_seq());
					resultDao.setDb_seq(userDB.getSeq());
					
					resultDao.setQuery_result("");
					resultDao.setMonitoringIndexDAO(monitoringIndexDAO);
					resultDao.setCreate_time(new Timestamp(System.currentTimeMillis()));
					if(PublicTadpoleDefine.YES_NO.YES.name().equals(monitoringIndexDAO.getIs_snapshot_save())) {
						resultDao.setSnapshot(Utils.getDBVariable(userDB));
					}
					
					// 후속작업을 위해 사용자 별로 모니터링 데이터를 모읍니다.
					listMonitoringResult.add(resultDao);
				}
			}
		} catch(Exception e) {
			logger.error("get monitoring list", e);
		}
		
		afterProceedingResultData(listMonitoringResult);
	}
	
	/**
	 * update index parameter
	 * 
	 * @param monitoringIndexDAO
	 * @param jsonArray
	 */
	private void updateParameterValue(MonitoringIndexDAO monitoringIndexDAO, JsonArray jsonArray) {
		if(StringUtils.isNotEmpty(monitoringIndexDAO.getParam_1_column())) {
			if(jsonArray.size() != 0) {
				JsonObject jsonObj = (JsonObject)jsonArray.get(jsonArray.size()-1);
				
				JsonElement jsonValue = jsonObj.get(monitoringIndexDAO.getParam_1_column().toLowerCase());
				String strIndexValue = jsonValue != null?jsonValue.getAsString():"";
				
				JsonElement jsonValue2 = jsonObj.get(monitoringIndexDAO.getParam_2_column());
				String strIndexValue2 = jsonValue2 != null?jsonValue2.getAsString():"";
				
				monitoringIndexDAO.setParam_1_init_value(strIndexValue);
				monitoringIndexDAO.setParam_2_init_value(strIndexValue2);
				
				try {
					TadpoleSystem_monitoring.updateParameter(monitoringIndexDAO);
				} catch (Exception e) {
					logger.error("Update index parameter", e);
				}
			}
		}
	}
	
	/**
	 * after proceeding monitoring result data
	 * - save data
	 * - user notification
	 * - cache monitoring data 
	 * 
	 * @param listMonitoringData
	 */
	private void afterProceedingResultData(List<MonitoringResultDAO> listMonitoringData) {

		// 데이터를 저장한다.
		TadpoleSystem_monitoring.saveMonitoringResult(listMonitoringData);
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		// 후속 작업을 위해 사용자 별로 모니터링 데이터를 담습니다. 
		Map<String, List<MonitoringResultDAO>> mapMonitoringData = new HashMap<>();
		List<MonitoringResultDAO> listErrorMonitoringResult = new ArrayList<MonitoringResultDAO>();
		
		{
			List<MonitoringResultDAO> listMonitoringResult;
			String strEmail = "";
			for (MonitoringResultDAO resultDao : listMonitoringData) {
				
				if(!MonitoringDefine.MONITORING_STATUS.CLEAN.getName().equals( resultDao.getResult() )) {
					listErrorMonitoringResult.add(resultDao);
				}
				
				strEmail = resultDao.getMonitoringIndexDAO().getEmail();
				if(mapMonitoringData.get(strEmail) == null) {
					listMonitoringResult = new ArrayList<MonitoringResultDAO>(); 
					listMonitoringResult.add(resultDao);
					mapMonitoringData.put(strEmail, listMonitoringResult);
				} else {
					mapMonitoringData.get(strEmail).add(resultDao);
				}
			}
		}
		
		// 사용자별로 담아 cache 메니저에 데이터를 전달한다.
		sendCacheManager(mapMonitoringData);
		
		// 이벤트 처리를 처리 모듈에 보낸다.
		sendEventManager(listErrorMonitoringResult);	
	}
	
	/**
	 * send cache Manager
	 * 
	 * @param mapMonitoringData
	 */
	private void sendCacheManager(Map<String, List<MonitoringResultDAO>> mapMonitoringData) {
		Set<String> emailSet = mapMonitoringData.keySet();
		for (String strEmail : emailSet) {
			MonitoringCacheRepository cacheInstance = MonitoringCacheRepository.getInstance();
			cacheInstance.put(strEmail, mapMonitoringData.get(strEmail));
		}
	}
	
	/**
	 * send event manager
	 * 
	 * @param listErrorMonitoringResult
	 */
	private void sendEventManager(List<MonitoringResultDAO> listErrorMonitoringResult) {
		if(!listErrorMonitoringResult.isEmpty()) {
			EventManager.getInstance().proceedEvent(listErrorMonitoringResult);
		}
	}
	
}
