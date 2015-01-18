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
			String url = "jdbc:oracle:thin:@172.16.187.132:1521:XE";
	
			Properties props = new Properties();
			props.setProperty("user", "HR");
			props.setProperty("password", "tadpole");
	
			Connection conn = DriverManager.getConnection(url, props);
			printMetaData(conn.getMetaData());
	
			PreparedStatement preStatement = conn.prepareStatement("SELECT MKT_ID, NAME, shape FROM COLA_MARKETS");//select * from v$version");
			ResultSet result = preStatement.executeQuery();
	
			while (result.next()) {
//				Geometry sample
//				STRUCT st = (STRUCT)result.getObject(3);
//				JGeometry j_geom = JGeometry.load(st);
//				
//				WKT wkt = new WKT();
//				System.out.println(new String(wkt.fromJGeometry(j_geom)));
				
				System.out.println("Result is : " + result.getString(1) + ", " + result.getString(2) + ", " + result.getString(3));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
