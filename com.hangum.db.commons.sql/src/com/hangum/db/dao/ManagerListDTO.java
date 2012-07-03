package com.hangum.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;

/**
 * manager server list
 * 
 * @author hangumNote
 *
 */
public class ManagerListDTO {
	String name;
	DBDefine dbType;
	List<UserDBDAO> managerList = new ArrayList<UserDBDAO>();
	
	public ManagerListDTO() {
	}
	
	public ManagerListDTO(String name, DBDefine dbType) {
		this.name = name;
		this.dbType = dbType;
	}
	
	public void addLogin(UserDBDAO dbInfo) {
		dbInfo.setParent(this);
		managerList.add(dbInfo);
	}
	
	public void removeDB(UserDBDAO dbInfo) {
		managerList.remove(dbInfo);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<UserDBDAO> getManagerList() {
		return managerList;
	}
	
	public DBDefine getDbType() {
		return dbType;
	}
}
