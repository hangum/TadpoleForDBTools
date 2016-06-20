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
package com.hangum.tadpole.rdb.core.dialog.table.mysql;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.define.DBDefine;

/**
 * alter define data type
 * 
 * @author hangum
 *
 */
public class ColumnDataTypeDef {
	/**
	 * java.sql.Types에 정의된 모든 데이터 타입의 각 dbms별로 type과 명칭으로 연결한다.
	 */
	public static final int LONGNVARCHAR = -16;
	public static final int NCHAR = -15;
	public static final int NVARCHAR = -9;
	public static final int ROWID = -8;
	public static final int BIT = -7;
	public static final int TINYINT = -6;
	public static final int BIGINT = -5;
	public static final int LONGVARBINARY = -4;
	public static final int VARBINARY = -3;
	public static final int BINARY = -2;
	public static final int LONGVARCHAR = -1;
	public static final int NULL = 0;
	public static final int CHAR = 1;
	public static final int NUMERIC = 2;
	public static final int DECIMAL = 3;
	public static final int INTEGER = 4;
	public static final int SMALLINT = 5;
	public static final int FLOAT = 6;
	public static final int REAL = 7;
	public static final int DOUBLE = 8;
	public static final int VARCHAR = 12;
	public static final int BOOLEAN = 16;
	public static final int DATALINK = 70;
	public static final int DATE = 91;
	public static final int TIME = 92;
	public static final int TIMESTAMP = 93;
	public static final int OTHER = 1111;
	public static final int JAVA_OBJECT = 2000;
	public static final int DISTINCT = 2001;
	public static final int STRUCT = 2002;
	public static final int ARRAY = 2003;
	public static final int BLOB = 2004;
	public static final int CLOB = 2005;
	public static final int REF = 2006;
	public static final int SQLXML = 2009;
	public static final int NCLOB = 2011;

	private static final HashMap<Integer, String> defaultNameMap = new HashMap<Integer, String>() {
		{
			put(LONGNVARCHAR, "LONGNVARCHAR");
			put(NCHAR, "NCHAR");
			put(NVARCHAR, "NVARCHAR");
			put(ROWID, "ROWID");
			put(BIT, "BIT");
			put(TINYINT, "TINYINT");
			put(BIGINT, "BIGINT");
			put(LONGVARBINARY, "LONGVARBINARY");
			put(VARBINARY, "VARBINARY");
			put(BINARY, "BINARY");
			put(LONGVARCHAR, "LONGVARCHAR");
			//put(NULL, "NULL");
			put(CHAR, "CHAR");
			put(NUMERIC, "NUMERIC");
			put(DECIMAL, "DECIMAL");
			put(INTEGER, "INT");
			put(SMALLINT, "SMALLINT");
			put(FLOAT, "FLOAT");
			put(REAL, "REAL");
			put(DOUBLE, "DOUBLE");
			put(VARCHAR, "VARCHAR");
			put(BOOLEAN, "BOOLEAN");
			put(DATALINK, "DATALINK");
			put(DATE, "DATE");
			put(TIME, "TIME");
			put(TIMESTAMP, "TIMESTAMP");
			put(OTHER, "OTHER");
			put(JAVA_OBJECT, "JAVA_OBJECT");
			put(DISTINCT, "DISTINCT");
			put(STRUCT, "STRUCT");
			put(ARRAY, "ARRAY");
			put(BLOB, "BLOB");
			put(CLOB, "CLOB");
			put(REF, "REF");
			put(SQLXML, "SQLXML");
			put(NCLOB, "NCLOB");
		};
	};
	
	private static final HashMap<Integer, String> mysqlNameMap = new HashMap<Integer, String>() {
		{
			put(TINYINT, "TINYINT");
			put(SMALLINT, "SMALLINT");
//			put(MEDIUMINT, "MEDIUMINT");
			
			put(LONGNVARCHAR, "LONGNVARCHAR");
			put(NCHAR, "NCHAR");
			put(NVARCHAR, "NVARCHAR");
			put(ROWID, "ROWID");
			put(BIT, "BIT");
			put(TINYINT, "TINYINT");
			put(BIGINT, "BIGINT");
			put(LONGVARBINARY, "LONGVARBINARY");
			put(VARBINARY, "VARBINARY");
			put(BINARY, "BINARY");
			put(LONGVARCHAR, "LONGVARCHAR");
			//put(NULL, "NULL");
			put(CHAR, "CHAR");
			put(NUMERIC, "NUMERIC");
			put(DECIMAL, "DECIMAL");
			put(INTEGER, "INTEGER");
			put(SMALLINT, "SMALLINT");
			put(FLOAT, "FLOAT");
			put(REAL, "REAL");
			put(DOUBLE, "DOUBLE");
			put(VARCHAR, "VARCHAR2");
			put(BOOLEAN, "BOOLEAN");
			put(DATALINK, "DATALINK");
			put(DATE, "DATE");
			put(TIME, "TIME");
			put(TIMESTAMP, "TIMESTAMP");
			put(OTHER, "OTHER");
			put(JAVA_OBJECT, "JAVA_OBJECT");
			put(DISTINCT, "DISTINCT");
			put(STRUCT, "STRUCT");
			put(ARRAY, "ARRAY");
			put(BLOB, "BLOB");
			put(CLOB, "CLOB");
			put(REF, "REF");
			put(SQLXML, "SQLXML");
			put(NCLOB, "NCLOB");
		};
	};

	private static final HashMap<Integer, String> oracleNameMap = new HashMap<Integer, String>() {
		{
			put(LONGNVARCHAR, "LONGNVARCHAR");
			put(NCHAR, "NCHAR");
			put(NVARCHAR, "NVARCHAR");
			put(ROWID, "ROWID");
			put(BIT, "BIT");
			put(TINYINT, "TINYINT");
			put(BIGINT, "BIGINT");
			put(LONGVARBINARY, "LONGVARBINARY");
			put(VARBINARY, "VARBINARY");
			put(BINARY, "BINARY");
			put(LONGVARCHAR, "LONGVARCHAR");
			//put(NULL, "NULL");
			put(CHAR, "CHAR");
			put(NUMERIC, "NUMERIC");
			put(DECIMAL, "DECIMAL");
			put(INTEGER, "INTEGER");
			put(SMALLINT, "SMALLINT");
			put(FLOAT, "FLOAT");
			put(REAL, "REAL");
			put(DOUBLE, "DOUBLE");
			put(VARCHAR, "VARCHAR2");
			put(BOOLEAN, "BOOLEAN");
			put(DATALINK, "DATALINK");
			put(DATE, "DATE");
			put(TIME, "TIME");
			put(TIMESTAMP, "TIMESTAMP");
			put(OTHER, "OTHER");
			put(JAVA_OBJECT, "JAVA_OBJECT");
			put(DISTINCT, "DISTINCT");
			put(STRUCT, "STRUCT");
			put(ARRAY, "ARRAY");
			put(BLOB, "BLOB");
			put(CLOB, "CLOB");
			put(REF, "REF");
			put(SQLXML, "SQLXML");
			put(NCLOB, "NCLOB");
		};
	};

	/**
	 * 데이터 타입을 받아서 문자 데이터를 저장하는 데이터 타입인지 판단하여 리턴한다.
	 * 테이블 생성시 데이터 사이즈를 명시적으로 생성해야 하는지 여부를 판단한다.
	 * @param type
	 * @return 
	 */
	public static boolean isCharType(int type){
		switch(type){
		case NCHAR 				:	
		case NVARCHAR 			:	
		case CHAR 				:	
		case VARCHAR 			:	
			return true;
		}
		return false;
	}
	
	public static boolean isNumericType(int type){
		switch(type){
		case TINYINT 			:	
		case BIGINT 			:	
		case BINARY 			:	
		case NUMERIC 			:	
		case DECIMAL 			:	
		case INTEGER 			:	
		case SMALLINT 			:	
		case FLOAT 				:	
		case REAL 				:	
		case DOUBLE 			:	
			return true;
		}
		return false;
	}
	
	public static boolean isDateType(int type){
		switch(type){
		case DATE 				:	
		case TIME 				:	
		case TIMESTAMP 			:	
			return true;
		}
		return false;
	}
	
	public static boolean isBooleanType(int type){
		switch(type){
		case BIT 				:	
		case BOOLEAN 			:	
			return true;
		}
		return false;
	}
	
	public static boolean isOtherObjectType(int type){
		switch(type){
		case LONGNVARCHAR 		:	
		case ROWID 				:	
		case LONGVARBINARY 		:	
		case VARBINARY 			:	
		case LONGVARCHAR 		:	
		case DATALINK 			:	
		case OTHER 				:	
		case JAVA_OBJECT 		:	
		case DISTINCT 			:	
		case STRUCT 			:	
		case ARRAY 				:	
		case BLOB 				:	
		case CLOB 				:	
		case REF 				:	
		case SQLXML 			:	
		case NCLOB 				:	
			return true;
		}
		return false;
	}
	
//	                                                                                                                              
	
	/**
	 * 콤보박스에 넣어줄 전체 데이터타입의 명칭을 조회하여 리턴한다.
	 * @param dbDef
	 * @return
	 */
	public static String[] getAllTypeNames(DBDefine dbDef) {
		String result = "";
		switch (dbDef) {
		case ORACLE_DEFAULT:
			for (String name : oracleNameMap.values()) {
				result += name + ",";
			}
			return StringUtils.split(result, ",");
//		case AMAZONRDS_DEFAULT:
		case CUBRID_DEFAULT:
		case HIVE2_DEFAULT:
		case HIVE_DEFAULT:
		case MARIADB_DEFAULT:
		case MONGODB_DEFAULT:
		case MSSQL_8_LE_DEFAULT:
		case MSSQL_DEFAULT:
		case MYSQL_DEFAULT:
		case POSTGRE_DEFAULT:
		case SQLite_DEFAULT:
		case TADPOLE_SYSTEM_DEFAULT:
		case TADPOLE_SYSTEM_MYSQL_DEFAULT:
		case TAJO_DEFAULT:
		default:
			for (String name : defaultNameMap.values()) {
				result += name + ",";
			}
			return StringUtils.split(result, ",");
		}
	}

	/**
	 * 데이터 타입으로 콤보박스 인덱스를 조회한다.
	 * @param dbDef
	 * @param dataType
	 * @return
	 */
	public static int getIndexByType(DBDefine dbDef, int dataType) {
		int index = -1;
		try {
			
			switch (dbDef) {
			case ORACLE_DEFAULT:

				for (java.util.Map.Entry<Integer, String> entry : oracleNameMap
						.entrySet()) {
					index++;
					if (dataType == entry.getKey()) {
						return index;
					}
				}
				return index;
			default:
				for (java.util.Map.Entry<Integer, String> entry : defaultNameMap
						.entrySet()) {
					index++;
					if (dataType == entry.getKey()) {
						return index;
					}
				}
				return index;
			}
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * 데이터타입 콤보에서 인덱스 값을 이용해 타입코드를 조회한다.
	 * @param dbDef
	 * @param typeIndex
	 * @return
	 */
	public static int getTypeByIndex(DBDefine dbDef, int typeIndex) {
		int index = 0;
		try {
			
			switch (dbDef) {
			case ORACLE_DEFAULT:

				for (java.util.Map.Entry<Integer, String> entry : oracleNameMap.entrySet()) {
					if (typeIndex == index++) {
						return entry.getKey();
					}
				}
				return java.sql.Types.NULL;
			default:
				for (java.util.Map.Entry<Integer, String> entry : defaultNameMap.entrySet()) {
					if (typeIndex == index) {
						return entry.getKey();
					}
					index++;
				}
				return java.sql.Types.NULL;
			}
		} catch (Exception e) {
			return java.sql.Types.NULL;
		}
	}

	/**
	 * dbms종류와 데이터 타입을 가지고 데이터 타입 명칭을 조회.
	 * @param dbDef
	 * @param dataType
	 * @return
	 */
	public static String getTypeName(DBDefine dbDef, int dataType) {
		try {
			switch (dbDef) {
//			case AMAZONRDS_DEFAULT:
//				return (String) defaultNameMap.get(dataType);
			case CUBRID_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case HIVE2_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case HIVE_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case MARIADB_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case MONGODB_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case MSSQL_8_LE_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case MSSQL_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case MYSQL_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case ORACLE_DEFAULT:
				return (String) oracleNameMap.get(dataType);
			case POSTGRE_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case SQLite_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case TADPOLE_SYSTEM_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case TADPOLE_SYSTEM_MYSQL_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			case TAJO_DEFAULT:
				return (String) defaultNameMap.get(dataType);
			default:
				return (String) defaultNameMap.get(dataType);
			}
		} catch (Exception e) {
			return "NotDef:" + dataType;
		}
	}
}