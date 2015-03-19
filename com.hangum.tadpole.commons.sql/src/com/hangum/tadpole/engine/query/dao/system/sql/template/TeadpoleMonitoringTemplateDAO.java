package com.hangum.tadpole.engine.query.dao.system.sql.template;

import java.sql.Timestamp;

/**
 * SQL monitoring dao
 * 
 * @author hangum
 *
 */
public class TeadpoleMonitoringTemplateDAO {
	int     seq;
	int 	user_seq;
    String db_type;
    String db_ver;
    String template_type="";
    
    String title = "";
    String description = "";

    String advice = "";
    String is_result_save = "";
    String is_snapshot_save = "";
    
    String query = "";

    String index_nm = "";
    String condition_type = "";
    String condition_value = "";
    
    String exception_index_nm = "";
    String exception_condition_type = "";
    String exception_condition_value = "";
    
    String param_1_column = "";
	String param_1_init_value = "";
	String param_2_column = "";
	String param_2_init_value = "";
    
    String after_type = "";
    String monitoring_type = "";
    String kpi_type = "";
    
    Timestamp create_time;
    String delyn;
    
	public TeadpoleMonitoringTemplateDAO() {
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
	 * @return the db_type
	 */
	public String getDb_type() {
		return db_type;
	}

	/**
	 * @param db_type the db_type to set
	 */
	public void setDb_type(String db_type) {
		this.db_type = db_type;
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
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
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
	 * @return the delyn
	 */
	public String getDelyn() {
		return delyn;
	}

	/**
	 * @param delyn the delyn to set
	 */
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}

	/**
	 * @return the db_ver
	 */
	public String getDb_ver() {
		return db_ver;
	}

	/**
	 * @param db_ver the db_ver to set
	 */
	public void setDb_ver(String db_ver) {
		this.db_ver = db_ver;
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
	 * @return the template_type
	 */
	public String getTemplate_type() {
		return template_type;
	}

	/**
	 * @param template_type the template_type to set
	 */
	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
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
	 * @return the is_result_save
	 */
	public String getIs_result_save() {
		return is_result_save;
	}

	/**
	 * @param is_result_save the is_result_save to set
	 */
	public void setIs_result_save(String is_result_save) {
		this.is_result_save = is_result_save;
	}

	/**
	 * @return the is_snapshot_save
	 */
	public String getIs_snapshot_save() {
		return is_snapshot_save;
	}

	/**
	 * @param is_snapshot_save the is_snapshot_save to set
	 */
	public void setIs_snapshot_save(String is_snapshot_save) {
		this.is_snapshot_save = is_snapshot_save;
	}
	
}
