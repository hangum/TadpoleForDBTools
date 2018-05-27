package com.hangum.tadpole.engine.query.dao.system.monitoring;

import java.sql.Timestamp;

/**
 * 모니터 지표를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class MonitoringIndexDAO extends MonitoringMainDAO {
	
	int seq;
	int monitoring_seq;
	String monitoring_type;
	
	String kpi_type;
	
	String after_type;

	String index_nm;
	String condition_type;
	String condition_value;
	
    String exception_index_nm;
    String exception_condition_type;
    String exception_condition_value;

    Timestamp create_time 	= new Timestamp(System.currentTimeMillis());
	Timestamp mod_time 		= new Timestamp(System.currentTimeMillis());
	String delyn;

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
	 * @return the exception_index_nm
	 */
	public String getException_index_nm() {
		return exception_index_nm;
	}

	/**
	 * @param exception_index_nm the exception_index_nm to set
	 */
	public void setException_index_nm(String exception_index_nm) {
		this.exception_index_nm = exception_index_nm;
	}

	/**
	 * @return the exception_condition_type
	 */
	public String getException_condition_type() {
		return exception_condition_type;
	}

	/**
	 * @param exception_condition_type the exception_condition_type to set
	 */
	public void setException_condition_type(String exception_condition_type) {
		this.exception_condition_type = exception_condition_type;
	}

	/**
	 * @return the exception_condition_value
	 */
	public String getException_condition_value() {
		return exception_condition_value;
	}

	/**
	 * @param exception_condition_value the exception_condition_value to set
	 */
	public void setException_condition_value(String exception_condition_value) {
		this.exception_condition_value = exception_condition_value;
	}

	/**
	 * @return the mod_time
	 */
	public Timestamp getMod_time() {
		return mod_time;
	}

	/**
	 * @param mod_time the mod_time to set
	 */
	public void setMod_time(Timestamp mod_time) {
		this.mod_time = mod_time;
	}

	/**
	 * @return the kpi_type
	 */
	public String getKpi_type() {
		return kpi_type;
	}

	/**
	 * @param kpi_type the kpi_type to set
	 */
	public void setKpi_type(String kpi_type) {
		this.kpi_type = kpi_type;
	}
	
	
}
