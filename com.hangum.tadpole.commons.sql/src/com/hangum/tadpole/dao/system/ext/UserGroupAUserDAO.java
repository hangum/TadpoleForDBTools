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
package com.hangum.tadpole.dao.system.ext;

import java.util.ArrayList;

import com.hangum.tadpole.dao.system.UserDAO;

/**
 * user group과 user정보 
 * @author hangum
 *
 */
public class UserGroupAUserDAO extends UserDAO {
	/** treeview로 처리하기 위한 */
	public UserGroupAUserDAO parent;
	/** treeviewe로 처리하기위한*/
	public ArrayList child = new ArrayList();
	
	/** user group name */
	private String user_group_name;
	
	public UserGroupAUserDAO() {
	}

	public UserGroupAUserDAO(UserGroupAUserDAO parent) {
		this.parent = parent;
	}
	
	public String getUser_group_name() {
		return user_group_name;
	}

	public void setUser_group_name(String user_group_name) {
		this.user_group_name = user_group_name;
	}
	
	public void setParent(UserGroupAUserDAO parent) {
		this.parent = parent;
	}
	
}
