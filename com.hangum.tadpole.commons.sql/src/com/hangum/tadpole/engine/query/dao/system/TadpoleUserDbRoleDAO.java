package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;

import com.hangum.tadpole.commons.csv.DateUtil;

/**
 * Define tadpole_user_db_role table
 * 
 * @author hangum
 *
 */
public class TadpoleUserDbRoleDAO extends UserDAO {
	int seq;
	int user_seq;
	int db_seq;
	String role_id;
	
	// all access ip
	String access_ip = "*";
	
	/** 리소스 다운로드 여부 */
	String is_resource_download	= "YES";
	
	// default value is 00:00
	Timestamp terms_of_use_starttime 	= new Timestamp(System.currentTimeMillis());

	// default value is 100 years after
	Timestamp terms_of_use_endtime  	= new Timestamp(DateUtil.afterMonthToMillsMonth(12));
	
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

	/**
	 * @return the is_resource_download
	 */
	public String getIs_resource_download() {
		return is_resource_download;
	}

	/**
	 * @param is_resource_download the is_resource_download to set
	 */
	public void setIs_resource_download(String is_resource_download) {
		this.is_resource_download = is_resource_download;
	}
	
}
