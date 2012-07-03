package com.hangum.db.dao.sqlite;

public class SQLiteRefTableDAO {
	String tbl_name;
	String sql;
	
	public SQLiteRefTableDAO() {
	}

	public String getTbl_name() {
		return tbl_name;
	}

	public void setTbl_name(String tbl_name) {
		this.tbl_name = tbl_name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
