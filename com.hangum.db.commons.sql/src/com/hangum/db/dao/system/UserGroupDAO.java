package com.hangum.db.dao.system;

/**
 * 사용자 그룹
 * 
 * @author hangum
 *
 */
public class UserGroupDAO {
	int seq;
	String name;
	String delYn;
	
	public UserGroupDAO() {
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

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	
	
	
}
