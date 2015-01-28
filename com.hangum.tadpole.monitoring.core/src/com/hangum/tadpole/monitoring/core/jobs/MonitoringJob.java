package com.hangum.tadpole.monitoring.core.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hangum.tadpole.monitoring.core.cache.MonitoringCacheRepository;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_monitoring;
import com.hangum.tadpole.sql.util.QueryUtils;

/**
 * Moitoring job
 * 
 * @author hangum
 *
 */
public class MonitoringJob implements Job {
	private static final Logger logger = Logger.getLogger(MonitoringJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Monitoring Job start....");

		// 후속 작업을 위해 사용자 별로 모니터링 데이터를 담습니다. 
		Map<String, List<MonitoringIndexDAO>> mapMonitoringData = new HashMap<>();

		try {
			List<MonitoringIndexDAO> listMonitoring = new ArrayList<MonitoringIndexDAO>();
			for (MonitoringIndexDAO monitoringIndexDAO : TadpoleSystem_monitoring.getMonitoring()) {
				if(logger.isDebugEnabled()) logger.debug("==[title]===> " + monitoringIndexDAO.getTitle());
				UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(monitoringIndexDAO.getDb_seq());
				monitoringIndexDAO.setUserDB(userDB);

				try {
					JsonArray jsonArray = QueryUtils.selectToJson(userDB, monitoringIndexDAO);
					for(int i=0; i<jsonArray.size(); i++) {
						JsonObject jsonObj = (JsonObject)jsonArray.get(i);
						
						JsonElement jsonValue = jsonObj.get(monitoringIndexDAO.getIndex_nm().toLowerCase());
						String strIndexValue = jsonValue != null?jsonValue.getAsString():"";
						if(logger.isDebugEnabled()) {
							logger.debug("\t result is " + jsonObj.toString());
							logger.debug("\t check values is " + monitoringIndexDAO.getTitle() + " : " + strIndexValue);
						}
						monitoringIndexDAO.setIndex_value(strIndexValue);
						
						//결과를 저장한다.
						boolean isError = !isErrorCheck(strIndexValue, monitoringIndexDAO);
						monitoringIndexDAO.setError(isError);
						monitoringIndexDAO.setResultJson(jsonObj);
					}
					
					// update index parameter.
					updateParameterValue(monitoringIndexDAO, jsonArray);

				} catch (Exception e) {
					logger.error("monitoring Job exception " + monitoringIndexDAO.getTitle(), e);
					
					// 오류를 모니터링 항목에 넣습니다.
				}
				
				// 후속작업을 위해 사용자 별로 모니터링 데이터를 모읍니다.
				String strEmail = monitoringIndexDAO.getEmail();
				if(mapMonitoringData.get(strEmail) == null) {
					listMonitoring = new ArrayList<MonitoringIndexDAO>(); 
					listMonitoring.add(monitoringIndexDAO);
					mapMonitoringData.put(strEmail, listMonitoring);
				} else {
					mapMonitoringData.get(strEmail).add(monitoringIndexDAO);
				}
			}
		} catch(Exception e) {
			logger.error("get monitoring list", e);
		}
		
		saveResultData(mapMonitoringData);
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
				
				JsonElement jsonValue = jsonObj.get(monitoringIndexDAO.getParam_1_column());
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
	 * after monitoring result data
	 * - save data
	 * - user notification
	 * - cache monitoring data 
	 * 
	 * @param mapMonitoringData
	 */
	private void saveResultData(Map<String, List<MonitoringIndexDAO>> mapMonitoringData) {
		Set<String> emailSet = mapMonitoringData.keySet();
		for (String strEmail : emailSet) {
			List<MonitoringIndexDAO> listMonitoringIndex = mapMonitoringData.get(strEmail);
			
			MonitoringCacheRepository cacheInstance = MonitoringCacheRepository.getInstance();
			cacheInstance.put(strEmail, listMonitoringIndex);

			TadpoleSystem_monitoring.saveMonitoringResult(strEmail, listMonitoringIndex);
		}
	}
	
	/**
	 * 에러인지 검사합니다. 
	 * 
	 * @param strIndelValue
	 * @param monitoringIndexDAO
	 * @return
	 */
	private boolean isErrorCheck(String strIndelValue, MonitoringIndexDAO monitoringIndexDAO) {
		String conditionType 	= monitoringIndexDAO.getCondition_type();
		String conditionValue 	= monitoringIndexDAO.getCondition_value();
		
		if(conditionType.equals("RISE_EXCEPTION")) {
			return true;
		} else if(conditionType.equals("EQUALS")) {
			if(conditionValue.equals(strIndelValue)) return true;
		} else if(conditionType.equals("GREATEST")) {
			if(NumberUtils.toDouble(conditionValue) > NumberUtils.toDouble(strIndelValue)) return true;
		} else {
			if(NumberUtils.toDouble(conditionValue) < NumberUtils.toDouble(strIndelValue)) return true;
		}
		
		return false;
	}

}
