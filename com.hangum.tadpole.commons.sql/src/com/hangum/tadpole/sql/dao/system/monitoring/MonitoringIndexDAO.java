package com.hangum.tadpole.sql.dao.system.monitoring;

import java.sql.Timestamp;

import com.google.gson.JsonObject;

/**
 * 모니터 지표를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class MonitoringIndexDAO extends MonitoringMainDAO {
	
	int seq;
	int monitoring_seq;
	String condition_type;
	String monitoring_type;
	String after_type;
	String index_nm;
	String condition_value;
	Timestamp create_time 	= new Timestamp(System.currentTimeMillis());
	Timestamp mod_time 		= new Timestamp(System.currentTimeMillis());
	String delyn;
	
	/** 모니터링 결과로 화면에 출력하기 위해 저장한다. */
	JsonObject resultJson;
	boolean error = false;

	public MonitoringIndexDAO() {
	}

	/**
	 * @return the monitoring_seq
	 */
	public int getMonitoring_seq() {
		return monitoring_seq;
	}

	/**
	 * @param monitoring_seq the monitoring_seq to set
	 */
	public void setMonitoring_seq(int monitoring_seq) {
		this.monitoring_seq = monitoring_seq;
	}

	/**
	 * @return the condition_type
	 */
	public String getCondition_type() {
		return condition_type;
	}

	/**
	 * @param condition_type the condition_type to set
	 */
	public void setCondition_type(String condition_type) {
		this.condition_type = condition_type;
	}

	/**
	 * @return the monitoring_type
	 */
	public String getMonitoring_type() {
		return monitoring_type;
	}

	/**
	 * @param monitoring_type the monitoring_type to set
	 */
	public void setMonitoring_type(String monitoring_type) {
		this.monitoring_type = monitoring_type;
	}

	/**
	 * @return the after_type
	 */
	public String getAfter_type() {
		return after_type;
	}

	/**
	 * @param after_type the after_type to set
	 */
	public void setAfter_type(String after_type) {
		this.after_type = after_type;
	}

	/**
	 * @return the index_nm
	 */
	public String getIndex_nm() {
		return index_nm;
	}

	/**
	 * @param index_nm the index_nm to set
	 */
	public void setIndex_nm(String index_nm) {
		this.index_nm = index_nm;
	}

	/**
	 * @return the condition_value
	 */
	public String getCondition_value() {
		return condition_value;
	}

	/**
	 * @param condition_value the condition_value to set
	 */
	public void setCondition_value(String condition_value) {
		this.condition_value = condition_value;
	}

	/**
	 * @return the resultJson
	 */
	public JsonObject getResultJson() {
		return resultJson;
	}

	/**
	 * @param resultJson the resultJson to set
	 */
	public void setResultJson(JsonObject resultJson) {
		this.resultJson = resultJson;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}


}
