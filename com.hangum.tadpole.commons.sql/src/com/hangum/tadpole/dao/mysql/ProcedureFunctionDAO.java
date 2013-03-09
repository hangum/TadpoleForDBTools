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

public class ProcedureFunctionDAO {
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
		return Definer;
	}

	public void setDefiner(String definer) {
		Definer = definer;
	}

	public String getModified() {
		return Modified;
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
		return Security_type;
	}

	public void setSecurity_type(String security_type) {
		Security_type = security_type;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public String getCharacter_set_client() {
		return character_set_client;
	}

	public void setCharacter_set_client(String character_set_client) {
		this.character_set_client = character_set_client;
	}

	public String getCollation_connection() {
		return collation_connection;
	}

	public void setCollation_connection(String collation_connection) {
		this.collation_connection = collation_connection;
	}

	public String getDatabase() {
		return Database;
	}

	public void setDatabase(String database) {
		Database = database;
	}

	public String getCollation() {
		return Collation;
	}

	public void setCollation(String collation) {
		Collation = collation;
	}
	
	
}
