package com.hangum.tadpole.engine.query.dao.system.bill;

import java.sql.Timestamp;

/**
 * assigned service 
 * 
 * @author hangum
 *
 */
public class AssignedServiceDAO {
	private int seq;
	private int bill_seq;
	private int user_seq;
	private Timestamp service_end_data;
	private String del_yn;
	private String description;
	
	public AssignedServiceDAO() {
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
	 * @return the bill_seq
	 */
	public int getBill_seq() {
		return bill_seq;
	}

	/**
	 * @param bill_seq the bill_seq to set
	 */
	public void setBill_seq(int bill_seq) {
		this.bill_seq = bill_seq;
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
	 * @return the service_end_data
	 */
	public Timestamp getService_end_data() {
		return service_end_data;
	}

	/**
	 * @param service_end_data the service_end_data to set
	 */
	public void setService_end_data(Timestamp service_end_data) {
		this.service_end_data = service_end_data;
	}

	/**
	 * @return the del_yn
	 */
	public String getDel_yn() {
		return del_yn;
	}

	/**
	 * @param del_yn the del_yn to set
	 */
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
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

}
