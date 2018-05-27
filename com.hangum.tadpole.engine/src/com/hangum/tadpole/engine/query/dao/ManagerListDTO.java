/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * manager server list
 * 
 * @author hangum
 *
 */
public class ManagerListDTO {
	String name;
	List<UserDBDAO> managerList = new ArrayList<UserDBDAO>();
	
	public ManagerListDTO() {
	}
	
	public ManagerListDTO(String name) {
		this.name = name;
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
}
