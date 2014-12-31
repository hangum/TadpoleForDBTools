package com.hangum.tadpole.sql.dao.system;

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
	
	

}
