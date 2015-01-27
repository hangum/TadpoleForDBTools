package com.hangum.tadpole.sql.dao.system.sql.template;

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
    String title;
    String description;
    String query;
    String index_nm;
    String condition_type;
    String condition_value;
    String after_type;
    String monitoring_type;
    String delyn 		= "";
    
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
	
	
}
