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
	TableDAO tableDao;
	
	String sysName = "";
	
	// MySQL, Oracle 사용하는 컬럼.
	String Field = "";
	String Type= "";
	String Null= "";
	String Key= "";
	String Default= "";
	String Extra= "";
	String collation_name = "";
	
	// SQLite에서 사용하는 컬럼.
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
	
	// hive
	String col_name = "";
	String data_type = "";
	
	/**
	 * column value
	 * 컬럼에 실제 들어 있는 값을 정의 합니다. 
	 */
	Object col_value = "";
	
	public TableColumnDAO() {
	}
	
	public TableColumnDAO(String name, String type, String index) {
		this.Field = name;
		this.Type = type;
		this.Key = index;
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

	public String getField() {
		return Field == null ? "" : Field;
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
		return name == null || "".equals(name) ? this.getField() : name ;
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
	
	public String getCol_name() {
		return col_name;
	}
	
	public void setCol_name(String col_name) {
		this.col_name = col_name;
		setField(col_name);
	}
	
	
	public String getData_type() {
		return data_type;
	}
	
	public void setData_type(String data_type) {
		this.data_type = data_type;
		setType(data_type);
	}

	/**
	 * @return the col_value
	 */
	public Object getCol_value() {
		return col_value;
	}

	/**
	 * @param col_value the col_value to set
	 */
	public void setCol_value(Object col_value) {
		this.col_value = col_value;
	}

	/**
	 * @return the tableDao
	 */
	public TableDAO getTableDao() {
		return tableDao;
	}

	/**
	 * @param tableDao the tableDao to set
	 */
	public void setTableDao(TableDAO tableDao) {
		this.tableDao = tableDao;
	}

	/**
	 * @return the collation_name
	 */
	public String getCollation_name() {
		return collation_name;
	}

	/**
	 * @param collation_name the collation_name to set
	 */
	public void setCollation_name(String collation_name) {
		this.collation_name = collation_name;
	}

}
