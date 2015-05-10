package com.hangum.tadpole.hive.core.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient2 {
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	
	protected static void printMetaData(DatabaseMetaData dmd) throws Exception {
		
		System.out.println(dmd.getDatabaseProductName() + " " + dmd.getDatabaseProductVersion() );
		System.out.println(dmd.getDriverName() + " " + dmd.getDriverVersion());
		System.out.println("");

//		System.out.println("\t getIdentifierQuoteString : " +  dmd.getIdentifierQuoteString() );
		System.out.println("\t storesLowerCaseIdentifiers : " +  dmd.storesLowerCaseIdentifiers() );
		System.out.println("\t storesLowerCaseQuotedIdentifiers : " +  dmd.storesLowerCaseQuotedIdentifiers() );
		System.out.println("\t storesMixedCaseIdentifiers : " +  dmd.storesMixedCaseIdentifiers() );
		System.out.println("\t storesMixedCaseQuotedIdentifiers : " +  dmd.storesMixedCaseQuotedIdentifiers() );
		System.out.println("\t storesUpperCaseIdentifiers : " +  dmd.storesUpperCaseIdentifiers() );
		System.out.println("\t storesUpperCaseQuotedIdentifiers : " +  dmd.storesUpperCaseQuotedIdentifiers() );
		System.out.println("-------------------------------------------------------");
		
	}
	 
	  /**
	   * @param args
	   * @throws SQLException
	   */
	  public static void main(String[] args) throws Exception {
	      try {
	      Class.forName(driverName);
	    } catch (ClassNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      System.exit(1);
	    }
	      
	    System.out.println("loaded hive driver...");
	    
	    //replace "hive" here with the name of the user the queries should run as
	    Connection con = DriverManager.getConnection("jdbc:hive2://127.0.0.1:10002/default", "", "");
	    
	    printMetaData(con.getMetaData());
	    
	    Statement stmt = con.createStatement();
	    
//	    stmt.execute("use sbx");
	    
	    String tableName = "testhangum";
	    stmt.execute("drop table if exists " + tableName);
	    stmt.execute("create table " + tableName + " (key int, value string)");
	    // show tables
	    String sql = "show tables '" + tableName + "'";
	    System.out.println("Running: " + sql);
	    ResultSet res = stmt.executeQuery(sql);
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
	 
//	    // load data into table
//	    // NOTE: filepath has to be local to the hive server
//	    // NOTE: /tmp/a.txt is a ctrl-A separated file with two fields per line
//	    String filepath = "/tmp/a.txt";
//	    sql = "load data local inpath '" + filepath + "' into table " + tableName;
//	    System.out.println("Running: " + sql);
//	    stmt.execute(sql);
//	 
//	    // select * query
//	    sql = "select * from " + tableName;
//	    System.out.println("Running: " + sql);
//	    res = stmt.executeQuery(sql);
//	    while (res.next()) {
//	      System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
//	    }
//	 
//	    // regular hive query
//	    sql = "select count(1) from " + tableName;
//	    System.out.println("Running: " + sql);
//	    res = stmt.executeQuery(sql);
//	    while (res.next()) {
//	      System.out.println(res.getString(1));
//	    }
	  }
}
