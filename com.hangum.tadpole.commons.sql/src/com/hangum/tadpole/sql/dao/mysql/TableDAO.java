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
package com.hangum.tadpole.sql.dao.mysql;

/**
 * table 정보 
 * 
 * @author hangum
 *
 */
public class TableDAO {
	String name;
	String comment="";
	
	/** hive */
	String tab_name = "";
	
	/** mongoDB */
	String size = "";
	
	public TableDAO() {
	}
	
	public TableDAO(String name, String comment) {
		this.name = name;
		this.comment = comment;
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
	
	public String getTab_name() {
		return tab_name;
	}
	
	public void setTab_name(String tab_name) {
		this.tab_name = tab_name;
		setName(tab_name);
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	

}
