/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - 자바(소스, 클래스) 객체에 대한 정보
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.rdb;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 데이터베이스에 정의된 자바 객체 정보를 조회한다.
 * 
 * @author nilriri
 * 
 */
public class OracleJavaDAO extends AbstractDAO {

	String object_name;
	String short_name;
	String created;
	String object_type;
	String last_ddl_time;
	String status;

	public OracleJavaDAO() {
	}

	public OracleJavaDAO(UserDBDAO userDB) {
		this.schema_name = userDB.getSchema();
	}

	@Override
	public String getFullName() {
		return schema_name + ".\"" + object_name + "\"";
	}

	@FieldNameAnnotationClass(fieldKey = "object_name")
	public String getObjectName() {
		return object_name == null ? "" : object_name;
	}

	public void setObjectName(String object_name) {
		this.object_name = object_name;
	}

	@FieldNameAnnotationClass(fieldKey = "short_name")
	public String getShortName() {
		return short_name == null ? "" : short_name;
	}

	public void setShortName(String short_name) {
		this.short_name = short_name;
	}

	@FieldNameAnnotationClass(fieldKey = "created")
	public String getCreated() {
		return created;
	}

	public void setCreate(String created) {
		this.created = created;
	}

	@FieldNameAnnotationClass(fieldKey = "object_type")
	public String getObjectType() {
		return object_type;
	}

	public void setObjectType(String object_type) {
		this.object_type = object_type;
	}

	@FieldNameAnnotationClass(fieldKey = "last_ddl_time")
	public String getLast_ddl_time() {
		return last_ddl_time;
	}

	public void setLast_ddl_time(String last_ddl_time) {
		this.last_ddl_time = last_ddl_time;
	}

	@FieldNameAnnotationClass(fieldKey = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
