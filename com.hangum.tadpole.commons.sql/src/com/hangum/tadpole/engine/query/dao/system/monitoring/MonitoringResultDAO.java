package com.hangum.tadpole.engine.query.dao.system.monitoring;

import java.sql.Timestamp;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * monitoring result
 * 
 * @author hangum
 *
 */
public class MonitoringResultDAO {
	// 보기 용 및 감시용.
	UserDBDAO userDB;
	MonitoringIndexDAO monitoringIndexDAO;
	
	String title;
	String description;
	String advice;

	// -----------------------------------------
	int seq;
	
	String unique_id;
	int relation_id;
	
	int monitoring_seq;
	int monitoring_index_seq;
	String monitoring_type;
	String kpi_type;
	
	int user_seq;
	int db_seq;
	
	String result;
	String index_value;
	
	String system_description;
	String user_description;
	String after_description;
	
	Timestamp create_time;
	Timestamp mod_time;
	
	String query_result	= "";
	String snapshot = "";
    
	public MonitoringResultDAO() {
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
	 * @return the unique_id
	 */
	public String getUnique_id() {
		return unique_id;
	}

	/**
	 * @param unique_id the unique_id to set
	 */
	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}
	
	/**
	 * @return the relation_id
	 */
	public int getRelation_id() {
		return relation_id;
	}

	/**
	 * @param relation_id the relation_id to set
	 */
	public void setRelation_id(int relation_id) {
		this.relation_id = relation_id;
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
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the index_value
	 */
	public String getIndex_value() {
		return index_value;
	}

	/**
	 * @param index_value the index_value to set
	 */
	public void setIndex_value(String index_value) {
		this.index_value = index_value;
	}

	/**
	 * @return the system_description
	 */
	public String getSystem_description() {
		return system_description;
	}

	/**
	 * @param system_description the system_description to set
	 */
	public void setSystem_description(String system_description) {
		this.system_description = system_description;
	}

	/**
	 * @return the user_description
	 */
	public String getUser_description() {
		return user_description;
	}

	/**
	 * @param user_description the user_description to set
	 */
	public void setUser_description(String user_description) {
		this.user_description = user_description;
	}

	/**
	 * @return the after_description
	 */
	public String getAfter_description() {
		return after_description;
	}

	/**
	 * @param after_description the after_description to set
	 */
	public void setAfter_description(String after_description) {
		this.after_description = after_description;
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

	/**
	 * @return the query_result
	 */
	public String getQuery_result() {
		return query_result;
	}

	/**
	 * @param query_result the query_result to set
	 */
	public void setQuery_result(String query_result) {
		this.query_result = query_result;
	}

	/**
	 * @return the userDB
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}

	/**
	 * @param userDB the userDB to set
	 */
	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	/**
	 * @return the monitoringIndexDAO
	 */
	public MonitoringIndexDAO getMonitoringIndexDAO() {
		return monitoringIndexDAO;
	}

	/**
	 * @param monitoringIndexDAO the monitoringIndexDAO to set
	 */
	public void setMonitoringIndexDAO(MonitoringIndexDAO monitoringIndexDAO) {
		this.monitoringIndexDAO = monitoringIndexDAO;
	}

	/**
	 * @return the user_seq
	 */
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
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
	 * @return the snapshot
	 */
	public String getSnapshot() {
		return snapshot;
	}

	/**
	 * @param snapshot the snapshot to set
	 */
	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
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
	
	
}
