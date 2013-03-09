package com.hangum.tadpole.dao.system;

import java.sql.Date;

/**
 * System dao 
 *
 * @author hangum
 *
 */
public class TadpoleSystemDAO {

	int seq;
	String name;
	String major_version;
	String sub_version;	
	String information;
	Date create_time;
	
	public TadpoleSystemDAO() {
	}
	
	public TadpoleSystemDAO(String name, String major_version, String sub_version, String information) {
		this.name = name;
		this.major_version = major_version;
		this.sub_version = sub_version;
		this.information = information;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajor_version() {
		return major_version;
	}

	public void setMajor_version(String major_version) {
		this.major_version = major_version;
	}

	public String getSub_version() {
		return sub_version;
	}

	public void setSub_version(String sub_version) {
		this.sub_version = sub_version;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
}
