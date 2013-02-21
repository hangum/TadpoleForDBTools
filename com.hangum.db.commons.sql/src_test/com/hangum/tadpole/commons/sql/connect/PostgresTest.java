package com.hangum.tadpole.commons.sql.connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresTest {
	public static void main(String[] args) throws Exception {

		String url = "jdbc:postgresql:test";
		String usr = "tadpole";
		String pwd = "tadpole";

		Class.forName("org.postgresql.Driver");

		// -- 1
		Connection conn = DriverManager.getConnection(url, usr, pwd);
		System.out.println(conn);
		conn.close();

		// -- 2
		url = "jdbc:postgresql://localhost/test";
		conn = DriverManager.getConnection(url, usr, pwd);
		System.out.println(conn);
		conn.close();

		// -- 3
		url = "jdbc:postgresql://localhost:5432/test";
		conn = DriverManager.getConnection(url, usr, pwd);
		System.out.println(conn);
		conn.close();

	}
}
