package com.hangum.db.dao.system;

/** 
 * user db resource data
 * 
 * @author hangum
 *
 */
public class UserDBResourceDataDAO {
	int seq;
	int user_db_resource_seq;
	String data;
	
	public UserDBResourceDataDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getUser_db_resource_seq() {
		return user_db_resource_seq;
	}

	public void setUser_db_resource_seq(int user_db_resource_seq) {
		this.user_db_resource_seq = user_db_resource_seq;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
