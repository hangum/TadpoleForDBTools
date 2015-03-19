package com.hangum.tadpole.engine.query.dao.system.monitoring;

import java.sql.Timestamp;

/**
 * monitoring main
 * 
 * @author hangum
 *
 */
public class MonitoringMainDAO {
	String email;
	
	int seq;
	int user_seq;
	int db_seq;
	
	String read_method;
	String title;
	String description;
	String advice;
	String cron_exp;

	String query;
	String param_1_column;
	String param_1_init_value;
	String param_2_column;
	String param_2_init_value;

	String is_result_save;
	String is_snapshot_save = "";
	
	String receiver;
	Timestamp create_time;
	Timestamp mod_time;
	String delyn;
	
	public MonitoringMainDAO() {
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
	 * @return the cron_exp
	 */
	public String getCron_exp() {
		return cron_exp;
	}

	/**
	 * @param cron_exp the cron_exp to set
	 */
	public void setCron_exp(String cron_exp) {
		this.cron_exp = cron_exp;
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
	 * @return the read_method
	 */
	public String getRead_method() {
		return read_method;
	}

	/**
	 * @param read_method the read_method to set
	 */
	public void setRead_method(String read_method) {
		this.read_method = read_method;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
