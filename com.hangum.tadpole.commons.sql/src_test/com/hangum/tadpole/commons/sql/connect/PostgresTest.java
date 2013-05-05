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
