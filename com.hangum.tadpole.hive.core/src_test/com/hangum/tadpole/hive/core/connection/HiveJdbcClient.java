package com.hangum.tadpole.hive.core.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		try {
			System.out.println("=============== JDBC driver load start.............");
			Class.forName(driverName);
			System.out.println("=============== JDBC driver load end.............");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			System.out.println("===Connection start===========================================");
			Connection con = DriverManager.getConnection("jdbc:hive://127.0.0.1:10000/default", "", "");
			System.out.println("===Connection send===========================================");
			
			Statement stmt = con.createStatement();
			String tableName = "testHiveDriverTable";
			stmt.executeQuery("drop table " + tableName);
			ResultSet res = stmt.executeQuery("create table " + tableName + " (key int, value string)"); 
			// show tables
			String sql = "show tables '" + tableName + "'";
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
			if (res.next()) {
				System.out.println(res.getString(1));
			}
			// describe table
			sql = "describe " + tableName;
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println(res.getString(1) + "\t" + res.getString(2));
			}
	
//			// load data into table
//			// NOTE: filepath has to be local to the hive server
//			// NOTE: /tmp/a.txt is a ctrl-A separated file with two fields per line
//			String filepath = "/tmp/a.txt";
//			sql = "load data local inpath '" + filepath + "' into table "
//					+ tableName;
//			System.out.println("Running: " + sql);
//			res = stmt.executeQuery(sql);
//	
//			// select * query
//			sql = "select * from " + tableName;
//			System.out.println("Running: " + sql);
//			res = stmt.executeQuery(sql);
//			while (res.next()) {
//				System.out.println(String.valueOf(res.getInt(1)) + "\t"
//						+ res.getString(2));
//			}
//	
//			// regular hive query
//			sql = "select count(1) from " + tableName;
//			System.out.println("Running: " + sql);
//			res = stmt.executeQuery(sql);
//			while (res.next()) {
//				System.out.println(res.getString(1));
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
