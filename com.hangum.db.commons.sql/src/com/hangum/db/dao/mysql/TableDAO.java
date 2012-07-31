package com.hangum.db.dao.mysql;

/**
 * table 정보 
 * 
 * @author hangum
 *
 */
public class TableDAO {
	String name;
	String desc;
	
	public TableDAO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


}
