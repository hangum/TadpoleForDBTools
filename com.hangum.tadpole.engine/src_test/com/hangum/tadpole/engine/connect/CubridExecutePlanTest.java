///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.engine.connect;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import cubrid.jdbc.driver.CUBRIDStatement;
//
//public class CubridExecutePlanTest {
//
//	public static void main(String arg[]) throws Exception {
//		Connection conn = null;
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
//		try {
//			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
//			conn = DriverManager.getConnection(
//					"jdbc:cubrid:localhost:33000:demodb:::", "", "");
//			conn.setAutoCommit(false); // 플랜 정보를 가져오기 위해서는 auto commit을 false로
//										// 설정해야 함.
//			String sql = "select /*+ recompile */ host_nation from olympic where host_year = ?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, 2004);
//			((CUBRIDStatement) pstmt).setQueryInfo(true);
//			rs = pstmt.executeQuery();
//			String plan = ((CUBRIDStatement) pstmt).getQueryplan(); // 수행한 질의 플랜
//																	// 정보를 가져오는
//																	// 메소드.
//			while (rs.next())
//				System.out.println("host_nation : " + rs.getString(1));
//			conn.commit();
//			System.out.println("plan : " + plan);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (rs != null)
//				rs.close();
//			if (pstmt != null)
//				pstmt.close();
//			if (conn != null)
//				conn.close();
//		}
//	}
//
//}
