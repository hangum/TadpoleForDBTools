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
package com.hangum.tadpole.db.db2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DB2Test {
	static {
	    try {
	        Class.forName("com.ibm.db2.jcc.DB2Driver");
	    } catch ( Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Start db2 connection.........................................");
		String url = "jdbc:db2://192.168.61.130:50000/sample";
		String user_id="db2admin";
		String password = "tadpole";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
		    conn = DriverManager.getConnection(url,user_id,password);
		    stmt  = conn.createStatement();
		    System.out.println("\tconnection successful.....................");
		    rs = stmt.executeQuery("select * from administrator.act");// sysibm.sysdummy1");
		    
			ResultSetMetaData  rsm = rs.getMetaData();
			int columnCount = rs.getMetaData().getColumnCount();
			
			System.out.println("### [Table] [start ]### [column count]" + rsm.getColumnCount() + "#####################################################################################################");
			for(int i=0;i<rs.getMetaData().getColumnCount(); i++) {
				System.out.println("\t ==[column start]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
				System.out.println("\tColumnLabel  		:  " 	+ rsm.getColumnLabel(i+1));
				
				System.out.println("\t AutoIncrement  	:  " 	+ rsm.isAutoIncrement(i+1));
				System.out.println("\t Nullable		  	:  " 	+ rsm.isNullable(i+1));
				System.out.println("\t CaseSensitive  	:  " 	+ rsm.isCaseSensitive(i+1));
				System.out.println("\t Currency		  	:  " 	+ rsm.isCurrency(i+1));
				
				System.out.println("\t DefinitelyWritable :  " 	+ rsm.isDefinitelyWritable(i+1));
				System.out.println("\t ReadOnly		  	:  " 	+ rsm.isReadOnly(i+1));
				System.out.println("\t Searchable		  	:  " 	+ rsm.isSearchable(i+1));
				System.out.println("\t Signed			  	:  " 	+ rsm.isSigned(i+1));
//				System.out.println("\t Currency		  	:  " 	+ rsm.isWrapperFor(i+1));
				System.out.println("\t Writable		  	:  " 	+ rsm.isWritable(i+1));
				
				System.out.println("\t ColumnClassName  	:  " 	+ rsm.getColumnClassName(i+1));
				System.out.println("\t CatalogName  		:  " 	+ rsm.getCatalogName(i+1));
				System.out.println("\t ColumnDisplaySize  :  " 	+ rsm.getColumnDisplaySize(i+1));
				System.out.println("\t ColumnType  		:  " 	+ rsm.getColumnType(i+1));
				System.out.println("\t ColumnTypeName 	:  " 	+ rsm.getColumnTypeName(i+1));
				
				System.out.println("\t Precision 			:  " 	+ rsm.getPrecision(i+1));
				System.out.println("\t Scale			 	:  " 	+ rsm.getScale(i+1));
				System.out.println("\t SchemaName		 	:  " 	+ rsm.getSchemaName(i+1));
				System.out.println("\t TableName		 	:  " 	+ rsm.getTableName(i+1));
				System.out.println("\t ==[column end]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
			}
		   
		    while(rs.next()) {
//		        int id = rs.getInt(1);
		        String name = rs.getString(1);
		        System.out.println(name);
		    }

		} catch ( SQLException e) {
		    e.printStackTrace();
		}

		try {
		    rs.close();
		    stmt.close();
		    conn.close();
		} catch ( Exception e) {
		    e.printStackTrace();
		}
	}

}
