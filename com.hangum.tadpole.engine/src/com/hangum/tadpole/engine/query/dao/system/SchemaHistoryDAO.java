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

import java.sql.Timestamp;

/**
 * Schema_history table
 * 
 * @author hangum
 *
 */
public class SchemaHistoryDAO {

	int seq;
	int db_seq;
	int user_seq;
	String name;
	
	/** 작업이 일어 난 곳 */
	String work_type = "";
	/**
	 * 객체 이름
	 * DDL문일 경우 공백으로 비워 둡니다.(아직은 이름을 알기 어려웁기 때문에.. 할수 있다면 DROP, ALTER)
	 */
	String object_id = "";

	/** insert, update, delete, ddl 타입 */
	String object_type = "";
	
	Timestamp create_date;
	Timestamp update_date;
	String del_yn;
	String ipaddress;
	
	public SchemaHistoryDAO() {
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the seq
	 */
	public final int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public final void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the db_seq
	 */
	public final int getDb_seq() {
		return db_seq;
	}

	/**
	 * @param db_seq the db_seq to set
	 */
	public final void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
	}

	/**
	 * @return the user_seq
	 */
	public final int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public final void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the object_id
	 */
	public final String getObject_id() {
		return object_id;
	}

	/**
	 * @param object_id the object_id to set
	 */
	public final void setObject_id(String object_id) {
		this.object_id = object_id;
	}

	/**
	 * @return the work_type
	 */
	public final String getWork_type() {
		return work_type;
	}

	/**
	 * @param work_type the work_type to set
	 */
	public final void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	/**
	 * @return the object_type
	 */
	public final String getObject_type() {
		return object_type;
	}

	/**
	 * @param object_type the object_type to set
	 */
	public final void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	/**
	 * @return the create_date
	 */
	public final Timestamp getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public final void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the update_date
	 */
	public final Timestamp getUpdate_date() {
		return update_date;
	}

	/**
	 * @param update_date the update_date to set
	 */
	public final void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}

	/**
	 * @return the del_yn
	 */
	public final String getDel_yn() {
		return del_yn;
	}

	/**
	 * @param del_yn the del_yn to set
	 */
	public final void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}

	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	
	
}
