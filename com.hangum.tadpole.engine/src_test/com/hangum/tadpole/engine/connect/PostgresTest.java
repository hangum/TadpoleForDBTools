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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class PostgresTest extends AbstractDriverInfo {
	public static void main(String[] args) throws Exception {

		try {

			String url = "jdbc:postgresql://localhost:5432/hangum";
			String usr = "hangum";
			String pwd = "";

			Class.forName("org.postgresql.Driver");

			// -- 1
			Connection conn = DriverManager.getConnection(url, usr, pwd);

			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT id, LENGTH(name), age, address, salary, join_date, SUBSTR(TO_CHAR(join_date,'YYYY-MM-DD'),1,4)  FROM public.company");
			
			ResultSet rs = stmt.executeQuery("SELECT a.id + 1 as id, name as nm, age, address, salary, join_date FROM company a, department b");
			
			ResultSetMetaData rsm = rs.getMetaData();
			for(int i=0; i<rsm.getColumnCount(); i++) {
				System.out.println("==> catalogname : " + rsm.getSchemaName(i+1) + "\t--->[table name]"
							+ rsm.getTableName(i+1));
			}
			
//			while (rs.next()) {
//				int id = rs.getInt("empname");
//				String name = rs.getString("salary");
//				int age = rs.getInt("last_date");
//				String address = rs.getString("last_user");
//				System.out.println("ID = " + id);
//				System.out.println("NAME = " + name);
//				System.out.println("AGE = " + age);
//				System.out.println("ADDRESS = " + address);
//				System.out.println();
//			}
			rs.close();
			stmt.close();
			conn.close();

			// // -- 2
			// url = "jdbc:postgresql://localhost/test";
			// conn = DriverManager.getConnection(url, usr, pwd);
			// System.out.println(conn);
			// conn.close();
			//
			// // -- 3
			// url = "jdbc:postgresql://localhost:5432/test";
			// conn = DriverManager.getConnection(url, usr, pwd);
			// System.out.println(conn);
			// conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
