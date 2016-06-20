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
			String url = "jdbc:oracle:thin:@192.168.29.128:1521:XE";
	
			Properties props = new Properties();
			props.put("user", "HR");
			props.put("password", "tadpole");
			props.put("ResultSetMetaDataOptions", "1");
	
			Connection conn = DriverManager.getConnection(url, props);
//			printMetaData(conn.getMetaData());
	
			PreparedStatement preStatement = conn.prepareStatement("SELECT DEPARTMENT_ID, DEPARTMENT_NAME, MANAGER_ID, LOCATION_ID  FROM DEPARTMENTS  t");//select * from v$version");
			ResultSet result = preStatement.executeQuery();
			
//			ResultSetMetaData rsm = result.getMetaData();
//			OracleResultSetMetaData orsm = (OracleResultSetMetaData)rsm;
//			System.out.println("Table name is " + rsm.getTableName(1) + "." + " column is " + rsm.getColumnName(1)) ;
//			System.out.println("Table name is " + orsm.getTableName(1) + "." + orsm.getCatalogName(1)) ;
			
	
			while (result.next()) {
				System.out.println("Information is : " + result.getString(1) + ", table name is " + result.getString(1));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
