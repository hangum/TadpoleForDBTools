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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * oracle connect test 
 * 
 * @author hangum
 *
 */
public class OracleConnectTest extends AbstractDriverInfo {

	public static void main(String args[]) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@192.168.32.128:1521:XE";
	
			Properties props = new Properties();
			props.setProperty("user", "HR");
			props.setProperty("password", "tadpole");
	
			Connection conn = DriverManager.getConnection(url, props);
			printMetaData(conn.getMetaData());
	
			PreparedStatement preStatement = conn.prepareStatement("select * from v$version");
			ResultSet result = preStatement.executeQuery();
	
			while (result.next()) {
				System.out.println("Information is : " + result.getString(1));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
