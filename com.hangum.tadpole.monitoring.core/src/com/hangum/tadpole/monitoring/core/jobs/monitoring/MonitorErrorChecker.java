package com.hangum.tadpole.monitoring.core.jobs.monitoring;

import org.apache.commons.lang.math.NumberUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;

/**
 * monitoring error checker
 * 
 * @author hangum
 *
 */
public class MonitorErrorChecker {

	/**
	 * 에러인지 검사합니다. 
	 * 
	 * @param jsonObj
	 * @param monitoringIndexDAO
	 * @return
	 */
	public static boolean isError(JsonObject jsonObj, MonitoringIndexDAO monitoringIndexDAO) {
		boolean isResult = false;
		String conditionType 	= monitoringIndexDAO.getCondition_type();
		String conditionValue 	= monitoringIndexDAO.getCondition_value();
		
		String strIndexName = monitoringIndexDAO.getIndex_nm().toLowerCase();
		if("".equals(strIndexName)) return false;
		
		JsonPrimitive jsonValue = jsonObj.getAsJsonPrimitive(strIndexName);
		String strIndexValue = jsonValue != null?jsonValue.getAsString():"0";
		
		isResult = isError(conditionType, conditionValue, strIndexValue);
		
		if(isResult) {
			
			String strExceptIndexNm = monitoringIndexDAO.getException_index_nm().toLowerCase();
			if(!"".equals(strExceptIndexNm)){
				String exceptionConditionType 	= monitoringIndexDAO.getException_condition_type();
				String exceptionConditionValue 	= monitoringIndexDAO.getException_condition_value();
				
				JsonPrimitive jsonExceptionValue = jsonObj.getAsJsonPrimitive(strExceptIndexNm);
				String exceptionIndexValue = jsonExceptionValue != null?jsonExceptionValue.getAsString():"0";
				
				isResult = isError(exceptionConditionType, exceptionConditionValue, exceptionIndexValue);
			}
		}
		
		return isResult;
	}
	
	/**
	 * 
	 * @param conditionType
	 * @param conditionValue
	 * @param strIndexValue
	 * @return
	 */
	private static boolean isError(String conditionType, String conditionValue, String strIndexValue) {
		boolean isResult = false;
		
		if(conditionType.equals("RISE_EXCEPTION")) {
			isResult = true;
		} else if(conditionType.equals("EQUALS")) {
			if(conditionValue.equals(strIndexValue)) isResult =true;
		} else if(conditionType.equals("GREATEST")) {
			if(NumberUtils.toDouble(conditionValue) < NumberUtils.toDouble(strIndexValue)) isResult = true;
		} else if(conditionType.equals("LEAST")) {
			if(NumberUtils.toDouble(conditionValue) > NumberUtils.toDouble(strIndexValue)) isResult = true;
		} else if(conditionType.equals("NOT_CHECK")) {
			isResult = false;
		}
		
		return isResult;
	}

}
