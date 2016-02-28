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

public class TriggerDAO extends StructObjectDAO {
	String Trigger;	
	String Event;
	String Table_name;	
	String Statement;	
	String Timing;	
	String Created;	
	String sql_mode;	
	String Definer;	
	String character_set_client;	
	String collation_connection;	
	String Database; 
	String Collation;
	String Status;
	
	public boolean isValid() {
		return "VALID".equals(Status) || "".equals(Status) || Status == null;
	}

	public void setStatus(String status) {
		Status = status;
	}

	/** sqlite, mssql, cubrid은 name->trigger */
	String name;
	/** msslq, cubrid은 db->database */
	String db;
	
	public TriggerDAO() {
	}

	public String getTrigger() {
		return Trigger;
	}

	public void setTrigger(String trigger) {
		Trigger = trigger;
	}

	public String getEvent() {
		return Event == null ? "" : Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	/**
	 * @return the table_name
	 */
	public String getTable_name() {
		return Table_name;
	}

	/**
	 * @param table_name the table_name to set
	 */
	public void setTable_name(String table_name) {
		Table_name = table_name;
	}

	public String getStatement() {
		return Statement == null ? "" : Statement;
	}

	public void setStatement(String statement) {
		Statement = statement;
	}

	public String getTiming() {
		return Timing == null ? "" : Timing;
	}

	public void setTiming(String timing) {
		Timing = timing;
	}

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
	}

	public String getSql_mode() {
		return sql_mode;
	}

	public void setSql_mode(String sql_mode) {
		this.sql_mode = sql_mode;
	}

	public String getDefiner() {
		return Definer;
	}

	public void setDefiner(String definer) {
		Definer = definer;
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

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
		setTrigger(name);
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
		setDatabase(db);
	}
	
	
}
