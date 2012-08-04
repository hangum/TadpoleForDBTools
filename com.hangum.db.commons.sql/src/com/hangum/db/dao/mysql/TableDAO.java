package com.hangum.db.dao.mysql;

/**
 * table 정보 
 * 
 * @author hangum
 *
 */
public class TableDAO {
	String name;
	String comment;
	
	public TableDAO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
