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

public class PostgresTest extends AbstractDriverInfo {
	public static void main(String[] args) throws Exception {

		String url = "jdbc:postgresql://localhost/hangum";
		String usr = "hangum";
		String pwd = "";

		Class.forName("org.postgresql.Driver");

		// -- 1
		Connection conn = DriverManager.getConnection(url, usr, pwd);
		printMetaData(conn.getMetaData());
		
		conn.close();

//		// -- 2
//		url = "jdbc:postgresql://localhost/test";
//		conn = DriverManager.getConnection(url, usr, pwd);
//		System.out.println(conn);
//		conn.close();
//
//		// -- 3
//		url = "jdbc:postgresql://localhost:5432/test";
//		conn = DriverManager.getConnection(url, usr, pwd);
//		System.out.println(conn);
//		conn.close();

	}
}
