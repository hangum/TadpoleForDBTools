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

/**
 * commons schema object
 * 
 * @author hangum
 *
 */
public abstract class StructObjectDAO {
	protected String schema_name = "";
	//TODO: sysname 을 fullname으로 사용하도록 개선해야함
	// 한글이나 특수문자등이 포함된 객체명...
	
	/**
	 * @return the schema_name
	 */
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
	 * 스키마.오브젝트명 으로 리턴한다.
	 * 스키마명이 없으면 오브젝트 명을 리턴한다.
	 * @return
	 */
	public abstract String getFullName();

}
