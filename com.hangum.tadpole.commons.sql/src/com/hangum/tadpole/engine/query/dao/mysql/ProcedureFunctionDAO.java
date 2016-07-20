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

import org.apache.commons.lang.StringUtils;

public class ProcedureFunctionDAO extends StructObjectDAO {
	
	String Db;
	String Name;
	String Type;
	String Definer;
	String Modified;
	String Created;
	String Security_type;
	String Comment;
	String character_set_client;
	String collation_connection;
	String Database;
	String Collation;
	String Status;
	String packagename;
	int overload; //TODO:함수 오버로딩 여부에 대한 예외사항 추가.
	
	/**
	 * @return the packagename
	 */
	public String getPackagename() {
		return packagename == null ? "" : packagename ;
	}

	/**
	 * @param packagename the packagename to set
	 */
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public boolean isValid() {
		return "VALID".equalsIgnoreCase(Status) || Status == null || "".equals(Status);
	}

	public void setStatus(String status) {
		Status = status;
	}

	public ProcedureFunctionDAO() {
	
	}

	public String getDb() {
		return Db;
	}

	public void setDb(String db) {
		Db = db;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getDefiner() {
		return Definer == null ? "" : Definer;
	}

	public void setDefiner(String definer) {
		Definer = definer;
	}

	public String getModified() {
		return Modified == null ? "" : Modified;
	}

	public void setModified(String modified) {
		Modified = modified;
	}

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
	}

	public String getSecurity_type() {
		return Security_type == null ? "" : Security_type;
	}

	public void setSecurity_type(String security_type) {
		Security_type = security_type;
	}

	public String getComment() {
		return Comment == null ? "" : Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public String getCharacter_set_client() {
		return character_set_client == null ? "" : character_set_client;
	}

	public void setCharacter_set_client(String character_set_client) {
		this.character_set_client = character_set_client;
	}

	public String getCollation_connection() {
		return collation_connection == null ? "" : collation_connection;
	}

	public void setCollation_connection(String collation_connection) {
		this.collation_connection = collation_connection;
	}

	public String getDatabase() {
		return Database == null ? "" : Database;
	}

	public void setDatabase(String database) {
		Database = database;
	}

	public String getCollation() {
		return Collation == null ? "" : Collation;
	}

	public void setCollation(String collation) {
		Collation = collation;
	}

	public int getOverload() {
		return overload;
	}

	public void setOverload(int overload) {
		this.overload = overload;
	}
	
	public String getFullName(boolean isPackage) {
		if(isPackage) {
			if(StringUtils.isEmpty(this.getSchema_name())){
				return this.getPackagename();
			}else{
				return String.format("%s.%s", this.getSchema_name(), this.getPackagename());
			}
		}else{
			return this.getFullName();
		}
	}
	
	@Override
	public String getFullName() {
		if(StringUtils.isEmpty(this.getSchema_name())) {
			return this.getSysName();
		}else{
			return String.format("%s.%s", this.getSchema_name(), this.getSysName());
		}
	}
	
}
