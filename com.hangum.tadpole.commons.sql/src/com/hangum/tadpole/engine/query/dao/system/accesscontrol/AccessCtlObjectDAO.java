/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system.accesscontrol;

import java.sql.Timestamp;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * access_ctl_object table dao
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 26.
 *
 */
public class AccessCtlObjectDAO {
	int seq;
	int access_seq;
	
	/**
	 * SELECT, INSERT, UPDATE, DELETE, DDL
	 * 
	 * This time only SELECT - hangum. 15.04.27
	 */
	String type; 
	String obj_name = "";
	String dontuse_object = PublicTadpoleDefine.YES_NO.NO.name();
	String detail_obj = "";
	String description;
	String delyn;
	Timestamp create_date;

	/**
	 * 
	 */
	public AccessCtlObjectDAO() {
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the access_seq
	 */
	public int getAccess_seq() {
		return access_seq;
	}

	/**
	 * @param access_seq the access_seq to set
	 */
	public void setAccess_seq(int access_seq) {
		this.access_seq = access_seq;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the obj_name
	 */
	public String getObj_name() {
		return obj_name;
	}

	/**
	 * @param obj_name the obj_name to set
	 */
	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}

	/**
	 * @return the detail_obj
	 */
	public String getDetail_obj() {
		return detail_obj;
	}

	/**
	 * @param detail_obj the detail_obj to set
	 */
	public void setDetail_obj(String detail_obj) {
		this.detail_obj = detail_obj;
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
	 * @return the delyn
	 */
	public String getDelyn() {
		return delyn;
	}

	/**
	 * @param delyn the delyn to set
	 */
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}

	/**
	 * @return the create_date
	 */
	public Timestamp getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the dontuse_object
	 */
	public String getDontuse_object() {
		return dontuse_object;
	}

	/**
	 * @param dontuse_object the dontuse_object to set
	 */
	public void setDontuse_object(String dontuse_object) {
		this.dontuse_object = dontuse_object;
	}

}
