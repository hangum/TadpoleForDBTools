package com.hangum.tadpole.commons.sql.procedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class MySQLProcedure {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://14.63.212.152:13306/tester", "tester", "1234");

			CallableStatement stat = conn.prepareCall("call simpleproc(?)");
			stat.registerOutParameter(1, Types.TINYINT);
			stat.execute();

			System.out.println(stat.getByte(2));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
