package com.hangum.tadpole.engine.connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteTest extends AbstractDriverInfo {

	public static void main(String args[]) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			printMetaData(c.getMetaData());
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

}
