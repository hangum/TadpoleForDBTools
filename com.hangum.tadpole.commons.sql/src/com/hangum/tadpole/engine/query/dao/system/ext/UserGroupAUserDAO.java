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
package com.hangum.tadpole.engine.query.dao.system.ext;

import java.util.ArrayList;

import com.hangum.tadpole.engine.query.dao.system.UserDAO;

/**
 * user group과 user정보 
 * @author hangum
 *
 */
public class UserGroupAUserDAO extends UserDAO {
	/** treeview로 처리하기 위한 */
	public UserGroupAUserDAO parent;

	private int group_seq;
	private int user_seq;
	private String role_type = "";
	
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

	/**
	 * @return the role_type
	 */
	public String getRole_type() {
		return role_type;
	}

	/**
	 * @param role_type the role_type to set
	 */
	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	/**
	 * @return the group_seq
	 */
	public int getGroup_seq() {
		return group_seq;
	}

	/**
	 * @param group_seq the group_seq to set
	 */
	public void setGroup_seq(int group_seq) {
		this.group_seq = group_seq;
	}

	/**
	 * @return the user_seq
	 */
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
		
		super.setSeq(user_seq);
	}
	
	
}
