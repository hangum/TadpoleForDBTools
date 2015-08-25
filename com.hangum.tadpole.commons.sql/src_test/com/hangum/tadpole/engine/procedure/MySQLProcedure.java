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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class MySQLProcedure {

	/**
	 * http://d2.naver.com/helloworld/1321
	 * http://kwonnam.pe.kr/wiki/database/mysql/jdbc
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.29.138:34306/tadpole20?connectTimeout=4000&socketTimeout=5000", "root", "tadpole");
			

			CallableStatement stat = conn.prepareCall("call simpleproc(?)");
			stat.registerOutParameter(1, Types.TINYINT);
			stat.execute();

			System.out.println(stat.getInt(1));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
