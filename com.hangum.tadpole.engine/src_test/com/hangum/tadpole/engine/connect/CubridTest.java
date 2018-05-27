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
import java.sql.SQLException;
import java.sql.Statement;

public class CubridTest {
	public static void main(String[] args) throws Exception {

		Connection conn = null;

		Statement stmt = null;

		ResultSet rs = null;

		try {
			// Connect to CUBRID
			System.out.println("Start cubrid...............................");

			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection("jdbc:cubrid:172.16.187.132:33000:demodb:::", "dba", "");

			String sql = "select name, players from event";
			System.out.println("success cubrid connect");
			
			stmt = conn.createStatement();
			System.out.println("succes execute query ");

			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				String name = rs.getString("name");

				String players = rs.getString("players");

				System.out.println("name ==> " + name);

				System.out.println("Number of players==> " + players);

				System.out.println("\n=========================================\n");
			}

			rs.close();

			stmt.close();

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());

		} finally {

			if (conn != null)
				conn.close();

		}

	}
}
