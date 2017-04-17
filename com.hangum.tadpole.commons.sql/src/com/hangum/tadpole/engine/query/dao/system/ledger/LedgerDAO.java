package com.hangum.tadpole.engine.query.dao.system.ledger;

import java.sql.Timestamp;

public class LedgerDAO {
	int seq;
	int user_seq; 
	String cp_seq;
	String comment;
	String request_query;
	Timestamp create_time;
	String result_yn;
	String result_msg;
	
	public LedgerDAO() {
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
	 * @return the cp_seq
	 */
	public String getCp_seq() {
		return cp_seq;
	}

	/**
	 * @param cp_seq the cp_seq to set
	 */
	public void setCp_seq(String cp_seq) {
		this.cp_seq = cp_seq;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the request_query
	 */
	public String getRequest_query() {
		return request_query;
	}

	/**
	 * @param request_query the request_query to set
	 */
	public void setRequest_query(String request_query) {
		this.request_query = request_query;
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
	 * @return the result_yn
	 */
	public String getResult_yn() {
		return result_yn;
	}

	/**
	 * @param result_yn the result_yn to set
	 */
	public void setResult_yn(String result_yn) {
		this.result_yn = result_yn;
	}

	/**
	 * @return the result_msg
	 */
	public String getResult_msg() {
		return result_msg;
	}

	/**
	 * @param result_msg the result_msg to set
	 */
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
	
}
