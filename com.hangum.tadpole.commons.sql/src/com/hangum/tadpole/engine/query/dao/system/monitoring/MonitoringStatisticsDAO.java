package com.hangum.tadpole.engine.query.dao.system.monitoring;

import java.sql.Timestamp;

/**
 * statics of five minute
 * 
 * @author hangum
 *
 */
public class MonitoringStatisticsDAO {
	
	int seq;
    int monitoring_seq;
    int type;
    String title;
    int avr;
    int suc_count;
    int fail_count;
    Timestamp create_time;

	public MonitoringStatisticsDAO() {
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the avr
	 */
	public int getAvr() {
		return avr;
	}

	/**
	 * @param avr the avr to set
	 */
	public void setAvr(int avr) {
		this.avr = avr;
	}

	/**
	 * @return the suc_count
	 */
	public int getSuc_count() {
		return suc_count;
	}

	/**
	 * @param suc_count the suc_count to set
	 */
	public void setSuc_count(int suc_count) {
		this.suc_count = suc_count;
	}

	/**
	 * @return the fail_count
	 */
	public int getFail_count() {
		return fail_count;
	}

	/**
	 * @param fail_count the fail_count to set
	 */
	public void setFail_count(int fail_count) {
		this.fail_count = fail_count;
	}

	/**
	 * @return the create_time
	 */
	public Timestamp getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

}
