package com.hangum.tadpole.engine.query.dao.system.monitoring;

import java.sql.Timestamp;

/**
 * monitoring dashboard dao
 * 
 * @author hangum
 *
 */
public class MonitoringDashboardDAO {
	String title;
	String description;
	String advice;
	
	String display_name;
	int db_seq;
	int monitoring_seq;
	int monitoring_index_seq;
	String monitoring_type; 
	String kpi_type;
	
	Timestamp start_time;
	
	int warring_cnt;
	int critical_cnt;

	public MonitoringDashboardDAO() {
	}

	/**
	 * @return the advice
	 */
	public String getAdvice() {
		return advice;
	}

	/**
	 * @param advice the advice to set
	 */
	public void setAdvice(String advice) {
		this.advice = advice;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the display_name
	 */
	public String getDisplay_name() {
		return display_name;
	}

	/**
	 * @param display_name the display_name to set
	 */
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the db_seq
	 */
	public int getDb_seq() {
		return db_seq;
	}

	/**
	 * @param db_seq the db_seq to set
	 */
	public void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
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
	 * @return the monitoring_index_seq
	 */
	public int getMonitoring_index_seq() {
		return monitoring_index_seq;
	}

	/**
	 * @param monitoring_index_seq the monitoring_index_seq to set
	 */
	public void setMonitoring_index_seq(int monitoring_index_seq) {
		this.monitoring_index_seq = monitoring_index_seq;
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

	/**
	 * @return the start_time
	 */
	public Timestamp getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}

	/**
	 * @return the warring_cnt
	 */
	public int getWarring_cnt() {
		return warring_cnt;
	}

	/**
	 * @param warring_cnt the warring_cnt to set
	 */
	public void setWarring_cnt(int warring_cnt) {
		this.warring_cnt = warring_cnt;
	}

	/**
	 * @return the critical_cnt
	 */
	public int getCritical_cnt() {
		return critical_cnt;
	}

	/**
	 * @param critical_cnt the critical_cnt to set
	 */
	public void setCritical_cnt(int critical_cnt) {
		this.critical_cnt = critical_cnt;
	}

}
