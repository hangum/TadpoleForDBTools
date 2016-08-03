/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - 테이블스페이스 정보
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.rdb;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 테이블스페이스 정보를 조회한다.
 * 
 * @author nilriri
 * 
 */
public class OracleTablespaceDAO extends AbstractDAO {
	String tablespace_name;
	String status;
	String contents;
	String extent_management;
	String bigfile;
	long mb_size;
	long mb_free;
	long mb_used;
	long pct_free;
	long pct_used;
	long mb_max;
    
	public OracleTablespaceDAO() {
	}

	public OracleTablespaceDAO(UserDBDAO userDB) {
		this.schema_name = userDB.getSchema();
	}

	@Override
	public String getFullName() {
		return tablespace_name;
	}

	@FieldNameAnnotationClass(fieldKey = "tablespace_name")
	public String getTablespace_name() {
		return tablespace_name;
	}

	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}

	@FieldNameAnnotationClass(fieldKey = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@FieldNameAnnotationClass(fieldKey = "contents")
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@FieldNameAnnotationClass(fieldKey = "extent_management")
	public String getExtent_management() {
		return extent_management;
	}

	public void setExtent_management(String extent_management) {
		this.extent_management = extent_management;
	}

	@FieldNameAnnotationClass(fieldKey = "bigfile")
	public String getBigfile() {
		return bigfile;
	}

	public void setBigfile(String bigfile) {
		this.bigfile = bigfile;
	}

	@FieldNameAnnotationClass(fieldKey = "mb_size")
	public long getMb_size() {
		return mb_size;
	}

	public void setMb_size(long mb_size) {
		this.mb_size = mb_size;
	}

	@FieldNameAnnotationClass(fieldKey = "mb_free")
	public long getMb_free() {
		return mb_free;
	}

	public void setMb_free(long mb_free) {
		this.mb_free = mb_free;
	}

	@FieldNameAnnotationClass(fieldKey = "mb_used")
	public long getMb_used() {
		return mb_used;
	}

	public void setMb_used(long mb_used) {
		this.mb_used = mb_used;
	}

	@FieldNameAnnotationClass(fieldKey = "pct_free")
	public long getPct_free() {
		return pct_free;
	}

	public void setPct_free(long pct_free) {
		this.pct_free = pct_free;
	}

	@FieldNameAnnotationClass(fieldKey = "pct_used")
	public long getPct_used() {
		return pct_used;
	}

	public void setPct_used(long pct_used) {
		this.pct_used = pct_used;
	}

	@FieldNameAnnotationClass(fieldKey = "mb_max")
	public long getMb_max() {
		return mb_max;
	}

	public void setMb_max(long mb_max) {
		this.mb_max = mb_max;
	}

}
