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
package com.hangum.tadpole.sql.dao.system;

import java.util.Date;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * user_db_resource dao
 * 
 * @author hangum
 *
 */
public class UserDBResourceDAO {
	int resource_seq;
	/** sql, erd */
	String resource_types;
	int user_seq;
	int db_seq;
//	int group_seq;
	String name = "";
	String shared_type = PublicTadpoleDefine.SHARED_TYPE.PUBLIC.toString();
	String description = "";
	
	Date create_time;
	String delYn;
	
	// db object tree 표현을 위해
	UserDBDAO parent;
	
	public UserDBResourceDAO() {
	}

	/**
	 * @return the resource_seq
	 */
	public int getResource_seq() {
		return resource_seq;
	}

	/**
	 * @param resource_seq the resource_seq to set
	 */
	public void setResource_seq(int resource_seq) {
		this.resource_seq = resource_seq;
	}

	/**
	 * @return the resource_types
	 */
	public String getResource_types() {
		return resource_types;
	}

	/**
	 * @param resource_types the resource_types to set
	 */
	public void setResource_types(String resource_types) {
		this.resource_types = resource_types;
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
	}

	/**
	 * @return the db_seq
	 */
	public int getDb_seq() {
		return db_seq;
	}

	/**
	 * @param db_seq the db_seq to set
	 */
	public void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shared_type
	 */
	public String getShared_type() {
		return shared_type;
	}

	/**
	 * @param shared_type the shared_type to set
	 */
	public void setShared_type(String shared_type) {
		this.shared_type = shared_type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public UserDBDAO getParent() {
		return parent;
	}

	public void setParent(UserDBDAO parent) {
		this.parent = parent;
	}
	
	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

//	/**
//	 * @return the group_seq
//	 */
//	public int getGroup_seq() {
//		return group_seq;
//	}
//
//	/**
//	 * @param group_seq the group_seq to set
//	 */
//	public void setGroup_seq(int group_seq) {
//		this.group_seq = group_seq;
//	}


}
