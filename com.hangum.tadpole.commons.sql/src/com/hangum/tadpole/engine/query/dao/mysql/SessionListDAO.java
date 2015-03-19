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
package com.hangum.tadpole.engine.query.dao.mysql;

/**
 * session list dao
 * ps) MySQL command result : SHOW PROCESSLIST;
 * 
 * @author hangum
 *
 */
public class SessionListDAO {
	
	String id;
	String user;
	String host;
	String db;
	String command;
	String time;
	String state;
	String info;
	
	// postgresql start ------------------------------
	/** pg_user is original user column */
	String pg_user;
	/** pg_time is orginal time column */
	String pg_time;
	// postgresql end ------------------------------

	public SessionListDAO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the pg_user
	 */
	public String getPg_user() {
		return pg_user;
	}

	/**
	 * @param pg_user the pg_user to set
	 */
	public void setPg_user(String pg_user) {
		this.pg_user = pg_user;
		
		setUser(pg_user);
	}

	/**
	 * @return the pg_time
	 */
	public String getPg_time() {
		return pg_time;
	}

	/**
	 * @param pg_time the pg_time to set
	 */
	public void setPg_time(String pg_time) {
		this.pg_time = pg_time;
		
		setTime(pg_time);
	}
	
}
