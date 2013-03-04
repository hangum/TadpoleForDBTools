/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.dao;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * manager server list
 * 
 * @author hangumNote
 *
 */
public class ManagerListDTO {
	String name;
//	DBDefine dbType;
	List<UserDBDAO> managerList = new ArrayList<UserDBDAO>();
	
	public ManagerListDTO() {
	}
	
	public ManagerListDTO(String name) {
		this.name = name;
//		this.dbType = dbType;
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
	
//	public DBDefine getDbType() {
//		return dbType;
//	}
}
