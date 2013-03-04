package com.hangum.tadpole.commons.sql.connect;

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
public class OracleConnectTest {

	public static void main(String args[]) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@192.168.61.129:1521:XE";
	
			Properties props = new Properties();
			props.setProperty("user", "scott");
			props.setProperty("password", "tiger");
	
			Connection conn = DriverManager.getConnection(url, props);
	
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
