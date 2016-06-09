package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Define tadpole_user_db_role table
 * 
 * @author hangum
 *
 */
public class TadpoleUserDbRoleDAO {
	int seq;
	int user_seq;
	int db_seq;
	String role_id;
	
	// all access ip
	String access_ip = "*";
	
	// default value is 00:00
	Timestamp terms_of_use_starttime 	= new Timestamp(System.currentTimeMillis());

	// default value is 100 years after
	Timestamp terms_of_use_endtime  	= new Timestamp(System.currentTimeMillis());// + (10 * 365 * 24 * 60 * 60 * 1000));
	
	String delYn;
	Date create_time;
	
	/////////////////////////
	String email;
	String name;
	
	//
	UserDBDAO parent;

	public TadpoleUserDbRoleDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getUser_seq() {
		return user_seq;
	}

	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	public int getDb_seq() {
		return db_seq;
	}

	public void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the access_ip
	 */
	public String getAccess_ip() {
		return access_ip;
	}

	/**
	 * @param access_ip the access_ip to set
	 */
	public void setAccess_ip(String access_ip) {
		this.access_ip = access_ip;
	}
	
	/**
	 * @return the terms_of_use_starttime
	 */
	public Timestamp getTerms_of_use_starttime() {
		return terms_of_use_starttime;
	}

	/**
	 * @param terms_of_use_starttime the terms_of_use_starttime to set
	 */
	public void setTerms_of_use_starttime(Timestamp terms_of_use_starttime) {
		this.terms_of_use_starttime = terms_of_use_starttime;
	}

	/**
	 * @return the terms_of_use_endtime
	 */
	public Timestamp getTerms_of_use_endtime() {
		return terms_of_use_endtime;
	}

	/**
	 * @param terms_of_use_endtime the terms_of_use_endtime to set
	 */
	public void setTerms_of_use_endtime(Timestamp terms_of_use_endtime) {
		this.terms_of_use_endtime = terms_of_use_endtime;
	}

	/**
	 * @return the parent
	 */
	public UserDBDAO getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(UserDBDAO parent) {
		this.parent = parent;
	}
	
	
}
