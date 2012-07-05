package com.hangum.db.dao.mysql;

public class TriggerDAO {
	String Trigger;	
	String Event;
	String Table;	
	String Statement;	
	String Timing;	
	String Created;	
	String sql_mode;	
	String Definer;	
	String character_set_client;	
	String collation_connection;	
	String Database; 
	String Collation;
	
	/** mssql, cubrid은 name->trigger */
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
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getTable() {
		return Table;
	}

	public void setTable(String table) {
		Table = table;
	}

	public String getStatement() {
		return Statement;
	}

	public void setStatement(String statement) {
		Statement = statement;
	}

	public String getTiming() {
		return Timing;
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
		return name;
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
