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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * oracle object utils
 * 
 * @author hangum
 *
 */
public class OracleObjectCompileUtils {
	private static final Logger logger = Logger.getLogger(OracleObjectCompileUtils.class);
	/**
	 * view compile
	 * 
	 * @param selection
	 * @param userDB
	 */
	public static String viewCompile(String viewName, UserDBDAO userDB) throws Exception {
		// 티베로가 컴파일시 DEBUG옵션을 지원하지 않는것이 있음.
		
		TableDAO viewDao = new TableDAO();
		if (StringUtils.contains(viewName, '.')){
			//오브젝트명에 스키마 정보가 포함되어 있으면...
			viewDao.setSchema_name(StringUtils.substringBefore(viewName, "."));
			viewDao.setTable_name(SQLUtil.makeIdentifierName(userDB, StringUtils.substringAfter(viewName, ".")));
			viewDao.setSysName(SQLUtil.makeIdentifierName(userDB, StringUtils.substringAfter(viewName, ".")));
		}else{
			// 스키마 정보가 없으면 컨넥션에 있는 스키마 정보 사용.
			viewDao.setSchema_name(userDB.getSchema());
			viewDao.setTable_name(viewName);
			viewDao.setSysName(viewName);
		}
		
		return viewCompile(viewDao, userDB);
	}

	public static String viewCompile(TableDAO viewDao, UserDBDAO userDB) throws Exception {
		String sqlQuery = "ALTER VIEW " + viewDao.getFullName() + " COMPILE "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			javaConn = TadpoleSQLManager.getConnection(userDB);
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			//티베로에는 all_errors가 syscat 스키마에 존재한다. sys.all_errors, syscat.all_errors로 분리하려면 오라클롸 티베로를 옵젝트 컴파일 클래스를 분리해야함. 
			sqlQuery = "Select * From all_Errors where owner = '"+viewDao.getSchema_name()+"' and name='"+ viewDao.getName() +"' and type = 'VIEW' order by type, sequence "; //$NON-NLS-1$ //$NON-NLS-2$
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
		
		//TODO: 모든 DAO에 스키마명을 포함한 오브젝트명을 조회가 가능한 메소드를 추가하여 대체해야함. 
		Map<String,String> paramMap = new HashMap<String,String>();
		if (StringUtils.contains(objName, '.')){
			//오브젝트명에 스키마 정보가 포함되어 있으면...
			paramMap.put("schema_name", StringUtils.substringBefore(objName, "."));
			paramMap.put("object_name", SQLUtil.makeIdentifierName(userDB, StringUtils.substringAfter(objName, ".")));
		}else{
			// 스키마 정보가 없으면 컨넥션에 있는 스키마 정보 사용.
			paramMap.put("schema_name", userDB.getSchema());
			paramMap.put("object_name", SQLUtil.makeIdentifierName(userDB, objName) );
		}
		paramMap.put("full_name", paramMap.get("schema_name") + "." + paramMap.get("object_name"));
		
		if (PublicTadpoleDefine.QUERY_DDL_TYPE.JAVA == actionType){
			//Java object는 컴파일 옵션이 없다.
			return otherObjectCompile(actionType, objType, paramMap, userDB, false);
		}else{
			return otherObjectCompile(actionType, objType, paramMap, userDB, userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT);
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
	public static String otherObjectCompile(PublicTadpoleDefine.QUERY_DDL_TYPE actionType, String objType, Map<String,String> paramMap, UserDBDAO userDB, boolean isDebug) throws Exception {
		String withDebugOption = "";
		if(isDebug) withDebugOption = "DEBUG";
		
		String sqlQuery = String.format("ALTER %s %s COMPILE %s", objType, paramMap.get("full_name"), withDebugOption); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if(logger.isDebugEnabled()) logger.debug("****** compile : " + sqlQuery);
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			javaConn = TadpoleSQLManager.getConnection(userDB);
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			
			sqlQuery = "Select * From all_Errors where owner = nvl('"+paramMap.get("schema_name")+"', user) and name='"+ paramMap.get("object_name") +"' and type = '"+objType+"' order by type, sequence "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			rs = statement.executeQuery(sqlQuery);
			StringBuffer result = new StringBuffer();
			while (rs.next()) {
				result.append(prettyMsg(rs.getString("line"), rs.getString("position"), rs.getString("text"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			return result.toString();
		} catch(Exception e) {
			logger.error("object compile error", e);
			throw e;
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
	public static String packageCompile(String strObjectName, UserDBDAO userDB) throws Exception {
		//TODO: RequestQuery에서 스키마 정보를 포함하는지 확인해야함.  
		ProcedureFunctionDAO packageDao = new ProcedureFunctionDAO();
		if (StringUtils.contains(strObjectName, '.')){
			//오브젝트명에 스키마 정보가 포함되어 있으면...
			packageDao.setSchema_name(StringUtils.substringBefore(strObjectName, "."));
			packageDao.setPackagename(SQLUtil.makeIdentifierName(userDB, StringUtils.substringAfter(strObjectName, ".")));
			packageDao.setName(SQLUtil.makeIdentifierName(userDB, StringUtils.substringAfter(strObjectName, ".")));
		}else{
			// 스키마 정보가 없으면 컨넥션에 있는 스키마 정보 사용.
			packageDao.setSchema_name(userDB.getSchema());
			packageDao.setPackagename(strObjectName);
			packageDao.setName(strObjectName);
		}
		
		return packageCompile(packageDao, userDB, userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT);
	}
	
	public static String packageCompile(ProcedureFunctionDAO packageDao, UserDBDAO userDB) throws Exception {
		return packageCompile(packageDao, userDB, userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT);
	}
	/**
	 * package compile
	 * 
	 * @param objectName
	 * @param userDB
	 */
	public static String packageCompile(ProcedureFunctionDAO packageDao, UserDBDAO userDB, boolean isDebug) throws Exception {
		String withDebugOption = "";
		if(isDebug) withDebugOption = "DEBUG";
		
		String sqlQuery = "ALTER PACKAGE " + packageDao.getFullName() + " COMPILE "+withDebugOption+" SPECIFICATION "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String sqlBodyQuery = "ALTER PACKAGE " + packageDao.getFullName() + " COMPILE "+withDebugOption+" BODY "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		java.sql.Connection javaConn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			javaConn = TadpoleSQLManager.getConnection(userDB);
			
			statement = javaConn.createStatement();
			statement.execute(sqlQuery);
			statement.execute(sqlBodyQuery);
			
			sqlQuery = "Select * From all_Errors where owner = nvl('"+packageDao.getSchema_name()+"', user) and name='"+ packageDao.getName() +"' and type in ('PACKAGE', 'PACKAGE BODY') order by type, sequence "; //$NON-NLS-1$ //$NON-NLS-2$
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
