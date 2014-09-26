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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class MariaSQLTest extends AbstractDriverInfo {
	public static void main(String[] args) throws Exception {

		String url = "jdbc:mariadb://192.168.32.128:4306/test";
		String usr = "root";
		String pwd = "tadpole";

		Class.forName("org.mariadb.jdbc.Driver");

		// -- 1
		Connection conn = DriverManager.getConnection(url, usr, pwd);
		printMetaData(conn.getMetaData());
		
		conn.close();

	}
}
