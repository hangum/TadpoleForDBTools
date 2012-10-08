/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.dao.mysql;


/**
 * talble의 컬럼을 정의 
 * <pre>
 * 		각 디비마다 테이블을 정의하는 구조가 틀리므로 모든 기준이 되는 디비는 mysql입니다.
 * 		
 * 		mysql, oracle을 제외한 다른 디비는 데이터를 메핑할때 mysql, oracle의 컬럼을 기준으로 매핑해야합니다.
 * 
 * 		예를 들어 sqlite의 컬럼 명이 name인데, 이것이 mysql에서는 Field에 매핑 되어야 하므로 코드에서는 다음과 같은 구조가 있어야 합니다.
 * 		public void setName(String name) {
			this.name = name;
			setField(name);
		}
 * </pre>
 * 
 * @author hangum
 *
 */
public class TableColumnDAO {
	// MySQL, Oracle 사용하는 컬
	String Field = "";
	String Type= "";
	String Null= "";
	String Key= "";
	String Default= "";
	String Extra= "";
	
	// SQLite에서 사용하는 컬
	String cid= "";
	String name= "";
//	String type= "";
	String notnull= "";
	String dflt_value= "";
	String pk= "";
	
	// mssql key
	String mskey = "";
	
	/** table column의 comment */
	String comment = "";
	
	public TableColumnDAO() {
	}
	
	public TableColumnDAO(String name, String type, String index) {
		this.Field = name;
		this.Type = type;
		this.Key = index;
	}

	public String getField() {
		return Field;
	}

	public void setField(String field) {
		Field = field;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
	

	public String getNull() {
		return Null;
	}

	public void setNull(String null1) {
		Null = null1;
	}
	
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getDefault() {
		return Default;
	}

	public void setDefault(String default1) {
		Default = default1;
	}

	public String getExtra() {
		return Extra;
	}

	public void setExtra(String extra) {
		Extra = extra;
	}

	
	////////////////////////////////////////////
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setField(name);
	}

	public String getNotnull() {
		return notnull;
	}

	public void setNotnull(String notnull) {
		this.notnull = notnull;
		setNull("0".equals(this.notnull)?"YES":"NO");
	}

	public String getDflt_value() {
		return dflt_value;
	}

	public void setDflt_value(String dflt_value) {
		this.dflt_value = dflt_value;
		setDefault(dflt_value);
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;

		if("1".equals(pk)) setNull("NO");
		setKey("1".equals(pk)?"PRI":"");
	}

	//[mssql]///////////////////////////
	public String getMskey() {
		return mskey;
	}

	public void setMskey(String mskey) {
		this.mskey = mskey;
		setKey(null != mskey?"PRI":"");
	}

	// 공통 table comment
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
