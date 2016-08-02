/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.mysql;

import com.hangum.tadpole.engine.query.dao.rdb.FieldNameAnnotationClass;

/**
 * commons schema object
 * 
 * @author hangum
 *
 */
public abstract class StructObjectDAO {
	protected String schema_name = "";
	
	/** 
	 * 시스템에서 쿼리에 사용할 이름을 정의 .
	 * 보여줄때는 {@link TableDAO#name}을 사용하고, 쿼리를 사용할때는 . 
	 * 
	 * 자세한 사항은 https://github.com/hangum/TadpoleForDBTools/issues/412 를 참고합니다.
	 */
	protected String sysName = "";
	
	/**
	 * @return the schema_name
	 */
	@FieldNameAnnotationClass(fieldKey = "schema_name")
	public String getSchema_name() {
		return schema_name;
	}

	/**
	 * @param schema_name the schema_name to set
	 */
	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}
	
	/**
	 * @return the sysName
	 */
	public final String getSysName() {
		return sysName;
	}

	/**
	 * @param sysName the sysName to set
	 */
	public final void setSysName(String sysName) {
		this.sysName = sysName;
	}
	
	/**
	 * 스키마.오브젝트명 으로 리턴한다.
	 * 스키마명이 없으면 오브젝트 명을 리턴한다.
	 * @return
	 */
	public abstract String getFullName();

}
