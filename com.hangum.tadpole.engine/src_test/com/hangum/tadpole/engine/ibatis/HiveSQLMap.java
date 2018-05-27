//package com.hangum.tadpole.engine.ibatis;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.List;
//
//import com.hangum.tadpole.engine.define.DBDefine;
//import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
//import com.hangum.tadpole.engine.manager.internal.map.SQLMap;
//import com.hangum.tadpole.sql.dao.system.UserDBDAO;
//import com.hangum.tadpole.sql.dao.system.UserGroupDAO;
//import com.hangum.tadpole.sql.query.TadpoleSystemInitializer;
//import com.ibatis.sqlmap.client.SqlMapClient;
//
//public class HiveSQLMap {
//
//	private static UserDBDAO getUserDB() {
//		UserDBDAO tadpoleEngineDB = new UserDBDAO();
//		tadpoleEngineDB.setDbms_types(DBDefine.HIVE_DEFAULT.getDBToString());
//		tadpoleEngineDB.setUrl(String.format(
//				DBDefine.HIVE_DEFAULT.getDB_URL_INFO(), "localhost", "10000",
//				"default"));
//		tadpoleEngineDB.setDb("default");
//		tadpoleEngineDB.setDisplay_name("hive default");
//		tadpoleEngineDB.setPasswd(""); //$NON-NLS-1$
//		tadpoleEngineDB.setUsers(""); //$NON-NLS-1$	
//
//		return tadpoleEngineDB;
//	}
//
//	public static void main(String[] args) {
//		try {
//			SqlMapClient client = SQLMap.getInstance(getUserDB());
//			 Connection con = client.getDataSource().getConnection();
//			
//			 Statement stmt = con.createStatement();
//			 String tableName = "testHiveDriverTable";
//			 stmt.executeQuery("drop table " + tableName);
//			 ResultSet res = stmt.executeQuery("create table " + tableName +
//			 " (key int, value string)");
//			 // show tables
//			 String sql = "show tables '" + tableName + "'";
//			 System.out.println("Running: " + sql);
//			 res = stmt.executeQuery(sql);
//			 if (res.next()) {
//				 System.out.println(res.getString(1));
//			 }
//			 // describe table
//			 sql = "describe " + tableName;
//			 System.out.println("Running: " + sql);
//			 res = stmt.executeQuery(sql);
//			 while (res.next()) {
//				 System.out.println(res.getString(1) + "\t" + res.getString(2));
//			 }
//
//			ibatisSelect();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void ibatisSelect() {
//		try {
//			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(getUserDB());
//			List listTable = sqlClient.queryForList("connectionCheck"); //$NON-NLS-1$
//			System.out.println("====================");
//			
//			for (Object object : listTable) {
//				System.out.println(object.toString());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
