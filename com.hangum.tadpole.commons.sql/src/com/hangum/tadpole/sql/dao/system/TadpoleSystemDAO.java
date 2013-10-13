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

import java.sql.Date;

/**
 * System dao 
 *
 * @author hangum
 *
 */
public class TadpoleSystemDAO {

	int seq;
	String name;
	String major_version;
	String sub_version;	
	String information;
	Date create_time;
	
	public TadpoleSystemDAO() {
	}
	
	public TadpoleSystemDAO(String name, String major_version, String sub_version, String information) {
		this.name = name;
		this.major_version = major_version;
		this.sub_version = sub_version;
		this.information = information;
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

	public String getMajor_version() {
		return major_version;
	}

	public void setMajor_version(String major_version) {
		this.major_version = major_version;
	}

	public String getSub_version() {
		return sub_version;
	}

	public void setSub_version(String sub_version) {
		this.sub_version = sub_version;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
}
