/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system;

/**
 * schedule
 * 
 * @author hangum
 *
 */
public class ScheduleDAO {
	int seq;
	int schedule_main_seq;
	int send_seq;
	String name = "";
	String description = "";
	String delyn = "";
	
	String sql = "";
	
	public ScheduleDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getSchedule_main_seq() {
		return schedule_main_seq;
	}

	public void setSchedule_main_seq(int schedule_main_seq) {
		this.schedule_main_seq = schedule_main_seq;
	}
	
	public int getSend_seq() {
		return send_seq;
	}

	public void setSend_seq(int send_seq) {
		this.send_seq = send_seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDelyn() {
		return delyn;
	}

	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
