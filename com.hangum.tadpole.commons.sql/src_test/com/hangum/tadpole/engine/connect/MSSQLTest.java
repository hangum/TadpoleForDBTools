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
package com.hangum.tadpole.engine.connect;

import java.sql.DatabaseMetaData;


public class MSSQLTest extends AbstractDriverInfo {
	private java.sql.Connection con = null;
	private final String url = "jdbc:jtds:sqlserver://";
	private final String serverName = "192.168.32.128";
	private final String portNumber = "1433";
	private final String databaseName = "northwind";
	private final String userName = "sa";
	private final String password = "tadpole";
	// Informs the driver to use server a side-cursor,
	// which permits more than one active statement
	// on a connection.
//	private final String selectMethod = "cursor";

	// Constructor
	public MSSQLTest() {
	}

	private String getConnectionUrl() {
		return url + serverName + ":" + portNumber + "/"+ databaseName;// + ";selectMethod=" + selectMethod + Define.SQL_DILIMITER;
	}

	private java.sql.Connection getConnection() {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			con = java.sql.DriverManager.getConnection(getConnectionUrl(), userName, password);
			if (con != null) System.out.println("Connection Successful!");
			
			printMetaData(con.getMetaData());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Trace in getConnection() : "
					+ e.getMessage());
		}
		return con;
	}

	/*
	 * Display the driver properties, database details
	 */

	public void displayDbProperties() {
		java.sql.DatabaseMetaData dm = null;
		java.sql.ResultSet rs = null;
		try {
			con = this.getConnection();
			if (con != null) {
				dm = con.getMetaData();
				System.out.println("Driver Information");
				System.out.println("\tDriver Name: " + dm.getDriverName());
				System.out.println("\tDriver Version: " + dm.getDriverVersion());
				System.out.println("\nDatabase Information ");
				System.out.println("\tDatabase Name: " + dm.getDatabaseProductName());
				System.out.println("\tDatabase Version: " + dm.getDatabaseProductVersion());
				System.out.println("Avalilable Catalogs ");
				
				rs = dm.getCatalogs();
				while (rs.next()) {
					System.out.println("\tcatalog: " + rs.getString(1));
				}
				rs.close();
				rs = null;
				closeConnection();
			} else
				System.out.println("Error: No active Connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dm = null;
	}

	private void closeConnection() {
		try {
			if (con != null)
				con.close();
			con = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		MSSQLTest myDbTest = new MSSQLTest();
		myDbTest.displayDbProperties();
	}
}
