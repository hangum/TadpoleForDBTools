/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util;

import java.sql.ResultSet;
import java.sql.Statement;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle object utils
 * 
 * @author hangum
 *
 */
public class OracleObjectCompileUtils {
	/**
	 * view compile
	 * 
	 * @param selection
	 * @param userDB
	 */
	public static String viewCompile(String viewName, UserDBDAO userDB) throws Exception {
		String sqlQuery = "ALTER VIEW " + userDB.getUsers() + "." + viewName.trim().toUpperCase() + " COMPILE "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			
			sqlQuery = "Select * From sys.user_Errors where name='"+ viewName.trim().toUpperCase() +"' and type = 'VIEW' order by type, sequence "; //$NON-NLS-1$ //$NON-NLS-2$
			rs = statement.executeQuery(sqlQuery);
			
			StringBuffer result = new StringBuffer();
			while (rs.next()) {
				result.append(prettyMsg(rs.getString("line"), rs.getString("position"), rs.getString("text"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			return result.toString();
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
	}
	
	/**
	 * other object compile
	 * 
	 * @param actionType 
	 * @param objType
	 * @param objName
	 * @param userDB
	 */
	public static String otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE actionType, String objType, String objName, UserDBDAO userDB) throws Exception {
		// 티베로가 컴파일시 DEBUG옵션을 지원하지 않는것이 있음.
		return otherObjectCompile(actionType, objType, objName, userDB, userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT);
	}
	
	/**
	 * other object compile
	 * 
	 * @param actionType 
	 * @param objType
	 * @param objName
	 * @param userDB
	 */
	public static String otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE actionType, String objType, String objName, UserDBDAO userDB, boolean isDebug) throws Exception {
		String withDebugOption = "";
		if(isDebug) withDebugOption = "DEBUG";
		
		String sqlQuery = "ALTER "+objType+" " + userDB.getUsers() + "." + objName.trim().toUpperCase() + " COMPILE " + withDebugOption; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			
			sqlQuery = "Select * From user_Errors where name='"+ objName +"' and type = '"+objType+"' order by type, sequence "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			rs = statement.executeQuery(sqlQuery);
			StringBuffer result = new StringBuffer();
			while (rs.next()) {
				result.append(prettyMsg(rs.getString("line"), rs.getString("position"), rs.getString("text"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			return result.toString();
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
	}
	
	/**
	 * package compile
	 * 
	 * @param objectName
	 * @param userDB
	 */
	public static String packageCompile(String objectName, UserDBDAO userDB) throws Exception {
		return packageCompile(objectName, userDB, userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT);
	}
	/**
	 * package compile
	 * 
	 * @param objectName
	 * @param userDB
	 */
	public static String packageCompile(String objectName, UserDBDAO userDB, boolean isDebug) throws Exception {
		String withDebugOption = "";
		if(isDebug) withDebugOption = "DEBUG";
		
		String sqlQuery = "ALTER PACKAGE " + userDB.getUsers() + "." + objectName.trim().toUpperCase() + " COMPILE "+withDebugOption+" SPECIFICATION "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String sqlBodyQuery = "ALTER PACKAGE " + userDB.getUsers() + "." + objectName.trim().toUpperCase() + " COMPILE "+withDebugOption+" BODY "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			statement.execute(sqlBodyQuery);
			
			sqlQuery = "Select * From user_Errors where name='"+ objectName.trim().toUpperCase() +"' and type in ('PACKAGE', 'PACKAGE BODY') order by type, sequence "; //$NON-NLS-1$ //$NON-NLS-2$
			rs = statement.executeQuery(sqlQuery);
			StringBuffer result = new StringBuffer();
			while (rs.next()) {
				result.append(prettyMsg(rs.getString("line"), rs.getString("position"), rs.getString("text"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
			}
			return result.toString();
		} finally {
			try { rs.close();} catch(Exception e) {}
			try { statement.close();} catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}

		}
	}
	
	/**
	 * pretty msg
	 * 
	 * @param line
	 * @param position
	 * @param msg
	 * @return
	 */
	private static String prettyMsg(String line, String position, String msg) {
		return String.format(Messages.get().OracleObjectCompileUtils_0, line, position, msg);
	}
	
}
