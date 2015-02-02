package com.hangum.tadpole.sql.dao.system.monitoring;

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
	String condition_type;
	String monitoring_type;
	String after_type;
	String index_nm;
	String condition_value;
	String param_1_column;
	String param_1_init_value;
	String param_2_column;
	String param_2_init_value;
	
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
	 * @return the param_1_column
	 */
	public String getParam_1_column() {
		return param_1_column;
	}

	/**
	 * @param param_1_column the param_1_column to set
	 */
	public void setParam_1_column(String param_1_column) {
		this.param_1_column = param_1_column;
	}

	/**
	 * @return the param_1_init_value
	 */
	public String getParam_1_init_value() {
		return param_1_init_value;
	}

	/**
	 * @param param_1_init_value the param_1_init_value to set
	 */
	public void setParam_1_init_value(String param_1_init_value) {
		this.param_1_init_value = param_1_init_value;
	}

	/**
	 * @return the param_2_column
	 */
	public String getParam_2_column() {
		return param_2_column;
	}

	/**
	 * @param param_2_column the param_2_column to set
	 */
	public void setParam_2_column(String param_2_column) {
		this.param_2_column = param_2_column;
	}

	/**
	 * @return the param_2_init_value
	 */
	public String getParam_2_init_value() {
		return param_2_init_value;
	}

	/**
	 * @param param_2_init_value the param_2_init_value to set
	 */
	public void setParam_2_init_value(String param_2_init_value) {
		this.param_2_init_value = param_2_init_value;
	}

}
