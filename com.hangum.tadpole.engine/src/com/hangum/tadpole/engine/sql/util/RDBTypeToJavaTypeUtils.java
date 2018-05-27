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
package com.hangum.tadpole.engine.sql.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * RDB type to Java type mapping utils
 * 
 * @author hangum
 * 
 */
public class RDBTypeToJavaTypeUtils {
	private static final Logger logger = Logger.getLogger(RDBTypeToJavaTypeUtils.class);

	private static Map<String, Integer> mapTypes = new HashMap<String, Integer>();
	private static String[] oracleParams = new String[] {"VARCHAR2","CHAR","NUMBER","DECIMAL","DATE","TIME","DATETIME","INTEGER","LONG","FLOAT","DOUBLE"};
	private static String[] mssqlParams = new String[] {"VARCHAR","CHAR","NUMERIC","DATE","INT","LONG","FLOAT","DOUBLE"};
	private static String[] etcParams = new String[] {"VARCHAR","CHAR","DATE","INT","LONG"};
	static {
		mapTypes.put("BIT", java.sql.Types.BIT);
		mapTypes.put("TINYINT", java.sql.Types.TINYINT);
		mapTypes.put("SMALLINT", java.sql.Types.SMALLINT);
		mapTypes.put("MEDIUMINT", java.sql.Types.INTEGER);
		mapTypes.put("INT", java.sql.Types.INTEGER);
		mapTypes.put("INTEGER", java.sql.Types.INTEGER);
		mapTypes.put("INT24", java.sql.Types.INTEGER);
		mapTypes.put("BIGINT", java.sql.Types.BIGINT);
		mapTypes.put("REAL", java.sql.Types.REAL);
		mapTypes.put("FLOAT", java.sql.Types.FLOAT);
		mapTypes.put("DECIMAL", java.sql.Types.DECIMAL);
		mapTypes.put("NUMERIC", java.sql.Types.DECIMAL);
		mapTypes.put("DOUBLE", java.sql.Types.DOUBLE);
		mapTypes.put("CHAR", java.sql.Types.CHAR);
		mapTypes.put("VARCHAR", java.sql.Types.VARCHAR);
		mapTypes.put("VARCHAR2", java.sql.Types.VARCHAR);
		mapTypes.put("DATE", java.sql.Types.DATE);
		mapTypes.put("TIME", java.sql.Types.TIME);
		mapTypes.put("YEAR", java.sql.Types.DATE);
		mapTypes.put("TIMESTAMP", java.sql.Types.TIMESTAMP);
		mapTypes.put("DATETIME", java.sql.Types.TIMESTAMP);
		mapTypes.put("TINYBLOB", java.sql.Types.BINARY);
		mapTypes.put("BLOB", java.sql.Types.BLOB);//LONGVARBINARY);
		mapTypes.put("MEDIUMBLOB", java.sql.Types.LONGVARBINARY);
		mapTypes.put("LONGBLOB", java.sql.Types.LONGVARBINARY);
		mapTypes.put("TINYTEXT", java.sql.Types.VARCHAR);
		mapTypes.put("TEXT", java.sql.Types.LONGVARCHAR);
		mapTypes.put("MEDIUMTEXT", java.sql.Types.LONGVARCHAR);
		mapTypes.put("LONGTEXT", java.sql.Types.LONGVARCHAR);
		mapTypes.put("GEOMETRY", java.sql.Types.BINARY);
		mapTypes.put("BINARY", java.sql.Types.BINARY);
		mapTypes.put("VARBINARY", java.sql.Types.VARBINARY);
		mapTypes.put("NUMBER", java.sql.Types.DECIMAL);
		mapTypes.put("LONG", java.sql.Types.LONGNVARCHAR);
		mapTypes.put("SYS_REFCURSOR", -10);// OracleTypes.CURSOR);

		// pgsql JSON type
		mapTypes.put("JSON", 1111);
	}

	/**
	 * rdb type to java
	 * 
	 * @param rdbType
	 * @return
	 */
	public static Integer getJavaType(String rdbType) {
//		if(logger.isDebugEnabled()) logger.debug("rdb type is " + rdbType);
		
		Integer javaType = mapTypes.get(rdbType);
		if (javaType == null) {
//			logger.info("SQL type to Java type not found is" + rdbType);
			return java.sql.Types.VARCHAR;
		}

		return javaType;
	}

	/**
	 * java type to rdb
	 * 
	 * @param rdbType
	 * @return
	 */
	public static String getRDBType(int javaType) {
		for (String key :mapTypes.keySet()){
//			logger.debug("==> "+ key +":" + mapTypes.get(key) + ":" + javaType);
			if (mapTypes.get(key).equals(javaType)) {
				return key;
			}
		}
		return "";
	}

	/**
	 * 숫자 컬럼인지
	 * 
	 * @param sqlType
	 * @return
	 */
	public static boolean isNumberType(int sqlType) {
		switch (sqlType) {
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.SMALLINT:
		case Types.TINYINT:
		case Types.REAL:
			return true;
		}

		return false;
	}
	
	/**
	 * Date 컬럼인지
	 * 
	 * @param sqlType
	 * @return
	 */
	public static boolean isDateType(int sqlType) {
		switch (sqlType) {
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return true;
		}

		return false;
	}
	
	public static boolean isDateType(String rdbType) {
		if(rdbType == null) return false;
		
		// 데이터 타입하고 데이터 사이즈가 함께 있을경우.. decimal(8) 
		if(StringUtils.contains(rdbType, "(")){
			rdbType = StringUtils.substringBefore(rdbType, "(");
		}
		
		Integer intType = mapTypes.get(rdbType.toUpperCase());
		if(intType == null) return false;
		return isDateType(intType);
	}

	public static boolean isNumberType(String rdbType) {
		if(rdbType == null) return false;
		
		// 데이터 타입하고 데이터 사이즈가 함께 있을경우.. decimal(8) 
		if(StringUtils.contains(rdbType, "(")){
			rdbType = StringUtils.substringBefore(rdbType, "(");
		}
		
		Integer intType = mapTypes.get(rdbType.toUpperCase());
		if(intType == null) return false;
		return isNumberType(intType);
	}

	/**
	 * 문자 컬럼인지
	 * 
	 * @param sqlType
	 * @return
	 */
	public static boolean isCharType(int sqlType) {
		switch (sqlType) {
		case Types.CHAR:
		case Types.VARCHAR:
			return true;
		}

		return false;
	}
	
	public static boolean isCharType(String rdbType) {
		return isCharType(mapTypes.get(rdbType));
	}

 /**
  * 데이터베이스 종류에 따라 파라미터 입력대화창을 지원하는 데이터 타입을 리턴한다.
  * @param userDB
  * @return
  */
	public static String[] supportParameterTypes(UserDBDAO userDB) {
		if (DBDefine.ORACLE_DEFAULT == userDB.getDBDefine()) {
			return oracleParams;
		}else if (DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			return mssqlParams;
		}else {
			return etcParams;
		}
	}
	
	public static int getIndex(UserDBDAO userDB, String rdbType) {
		int index = -1;
		for(String key : supportParameterTypes(userDB)){
			index++;
			if(key.equals(rdbType)) return index;
		}
		return 0;
	}

}
