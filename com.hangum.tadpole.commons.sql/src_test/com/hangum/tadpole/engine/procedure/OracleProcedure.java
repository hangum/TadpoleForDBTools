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
package com.hangum.tadpole.engine.procedure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;


/**
 * http://www.mkyong.com/oracle/oracle-stored-procedures-hello-world-examples/
 * @author hangum
 *
 */
public class OracleProcedure {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String strQuery = "CREATE OR REPLACE PROCEDURE procOneINOUTParameter(genericParam IN OUT VARCHAR2) " +
				 " IS " +
				 "		BEGIN " +
				 "		  genericParam := 'Hello World INOUT parameter ' || genericParam; " +
				 "		END;";
		
		Connection conn = null;
		Statement stmt  = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.32.128:1521:XE", "HR", "tadpole");

			stmt = conn.createStatement();
	        int code = stmt.executeUpdate(strQuery);
	        System.out.println("[result]" + code);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

	}

}
