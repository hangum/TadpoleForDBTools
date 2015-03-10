package com.hangum.tadpole.sql.dao.system;

import java.util.Date;

/**
 * Define tadple_user_db_role table
 * 
 * @author hangum
 *
 */
public class TadpoleUserDbRoleDAO {
	int seq;
	int user_seq;
	int db_seq;
	String role_id;
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
