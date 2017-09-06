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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.ACCEAS_CTL_DDL_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.FILTER_TYPE;

/**
 * 오브젝트 접근제어타입을 지정합니다. 
 * 
 * SELECT 접근을 막을 것이고 스키마는 뭐며, 오브젝트 타입은 뭐며, 오브젝트 이름은 뭐다는 식입니다.
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 26.
 *
 */
public class AccessCtlObjectDAO {
	int seq;
	int db_role_seq;
	
	/**
	 * 접근제어 SELECT, INSERT, UPDATE, DELETE, DDL
	 * 
	 * This time only SELECT - hangum. 15.04.27
	 */
	String type; 
	
	String obj_schema = "";
	
	/** 
	 * table, view, function, procedure
	 * {@code PublicTadpoleDefine#ACCEAS_CTL_DDL_TYPE} 
	 */
	String obj_type	= ACCEAS_CTL_DDL_TYPE.TABLEoVIEW.name();
	
	String obj_name = "";
	String filter_type = FILTER_TYPE.INCLUDE.name();
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
	 * @return the db_role_seq
	 */
	public int getDb_role_seq() {
		return db_role_seq;
	}

	/**
	 * @param db_role_seq the db_role_seq to set
	 */
	public void setDb_role_seq(int db_role_seq) {
		this.db_role_seq = db_role_seq;
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
	 * @return the obj_schema
	 */
	public String getObj_schema() {
		return obj_schema;
	}

	/**
	 * @param obj_schema the obj_schema to set
	 */
	public void setObj_schema(String obj_schema) {
		this.obj_schema = obj_schema;
	}

	/**
	 * @return the obj_type
	 */
	public String getObj_type() {
		return obj_type;
	}

	/**
	 * @param obj_type the obj_type to set
	 */
	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
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
	 * @return the filter_type
	 */
	public String getFilter_type() {
		return filter_type;
	}

	/**
	 * @param filter_type the filter_type to set
	 */
	public void setFilter_type(String filter_type) {
		this.filter_type = filter_type;
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

}
