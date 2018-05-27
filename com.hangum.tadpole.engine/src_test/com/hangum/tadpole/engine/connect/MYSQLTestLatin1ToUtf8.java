package com.hangum.tadpole.engine.connect;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * 디비, 테이블 캐릭터셋 모두 utf8이고 클라이언트 캐릭터 셋은 latin1 인 상태에서 테드폴허브 설정
 * 
 * @author hangum
 *
 */
public class MYSQLTestLatin1ToUtf8 extends AbstractDriverInfo {
	public static void main(String[] args) throws Exception {

		String url = "jdbc:mysql://192.168.216.129:3306/tadpole?useUnicode=false&characterEncoding=latin1";//&characterSetResults=latin1";
//					  jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf8
		Class.forName("com.mysql.jdbc.Driver");
		try {
			Connection conn = DriverManager.getConnection(url, "root", "tadpole");
			java.sql.Statement st = null;
			ResultSet rs = null;
			st = conn.createStatement();
			st.execute("set names latin1");
			rs = st.executeQuery("show variables like 'char%'");
			while (rs.next()) {
				System.out.println(rs.getString(1) + ":" + rs.getString(2));
			}
			
//			st.executeUpdate("insert into uut1 (id, name) values (1, '력한글');");
			String[] charSet = { "utf-8", "euc-kr", "ksc5601", "iso-8859-1", "x-windows-949", "cp1252", "latin1"};
			System.out.println("------------------------------------------------------------");
			rs = st.executeQuery("SELECT name, summary FROM ccontentinfo");
			while (rs.next()) {
				String str = rs.getString(2);
				System.out.println(new String(str.getBytes("latin1"), "utf-8"));

//				for (int i = 0; i < charSet.length; i++) {
//					for (int j = 0; j < charSet.length; j++) {
//						try {
//							System.out.println("[" + charSet[i] + "," + charSet[j] + "] = "
//									+ new String(str.getBytes(charSet[i]), charSet[j]));
//						} catch (UnsupportedEncodingException e) {
//							e.printStackTrace();
//						}
//					}
//				}
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
