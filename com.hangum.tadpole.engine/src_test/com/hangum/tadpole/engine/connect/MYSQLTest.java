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

public class MYSQLTest extends AbstractDriverInfo {
	public static void main(String[] args) throws Exception {

		String url = "jdbc:mysql://14.63.212.152:13306/tester";
		String usr = "tester";
		String pwd = "1234";
//		String url = "jdbc:mysql://192.168.32.128:3306/tadpole";
//		String usr = "root";
//		String pwd = "tadpole";

		Class.forName("com.mysql.jdbc.Driver");

		// -- 1
		Connection conn = DriverManager.getConnection(url, usr, pwd);
		printMetaData(conn.getMetaData());
		
		conn.close();

	}
}
