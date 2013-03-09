/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.sql.define;

import java.util.ArrayList;
import java.util.List;

/**
 * 지원하는 디비를 정의한다.
 * 
 * <pre>
 * db의 각 driver는 aquafold에 있는 db drvier를 사용한다(http://docs.aquafold.com/ads/7.0/F2A7423C-D740-9472-7FEB-46856150CB3E.html)
 * 
 * sqlite는 jdbc driver 			:	http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC
 * 			일반적인 내용 			:	http://en.wikibooks.org/wiki/Java_JDBC_using_SQLite/Introduction
 * 			sqlite meta data 	:	http://en.wikibooks.org/wiki/Java_JDBC_using_SQLite/Metadata
 *  를 참고합니다.
 *  
 * MSSQL 은 다음을 지원합니다.(http://www.microsoft.com/download/en/details.aspx?displaylang=en&id=11774)
	Microsoft® SQL Server® 2012
	Microsoft® SQL Server® 2008 R2
	Microsoft® SQL Server® 2008
	Microsoft® SQL Server® 2005
	Microsoft® SQL AzureTM
 * </pre>
 * 
 * @author hangum
 *
 */
public enum DBDefine {
	/** Tadpole System DB */
	TADPOLE_SYSTEM_DEFAULT,
	TADPOLE_SYSTEM_CUBRID_DEFAULT,
	
	/** USER DB */
	ORACLE_DEFAULT,
	MSSQL_DEFAULT,
	MSSQL_8_LE_DEFAULT,
	
	MYSQL_DEFAULT,
	SQLite_DEFAULT,
	CUBRID_DEFAULT,
	POSTGRE_DEFAULT,
	
	/** NO SQL */
	MONGODB_DEFAULT
	;	
	
	/** 환경정보가 있는 상대 위치 */
	private final static String prefix 			= "com/hangum/tadpole/commons/sql/config/";
	private final static String prefix_system 	= "com/hangum/tadpole/commons/sql/config/internal/system/";	

	/**
	 * db 환경이 있는 정보를 리턴한다.
	 * 
	 * @return
	 */
	public String getLocation() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT: return prefix_system + "TadpoleSystem-SQLite-Config.xml";
			case TADPOLE_SYSTEM_CUBRID_DEFAULT: return prefix_system + "TadpoleSystem-CUBRID-Config.xml";
			
			case ORACLE_DEFAULT:	return prefix + "OracleConfig.xml";
			case MSSQL_DEFAULT:		return prefix + "MSSQLConfig.xml";
			case MSSQL_8_LE_DEFAULT: return prefix + "MSSQLConfig_8_LE.xml";
			
			case MYSQL_DEFAULT:		return prefix + "MySQLConfig.xml";
			case SQLite_DEFAULT:		return prefix + "SQLiteConfig.xml";
			case CUBRID_DEFAULT:		return prefix + "CUBRIDConfig.xml";
			case POSTGRE_DEFAULT:		return prefix + "POSTGREConfig.xml";
			default:
				return "undefine db";
		}
	}
	
	/**
	 * DB TYPE을 얻는다.
	 * @param type
	 * @return
	 */
	public static DBDefine getDBDefine(String type) {
		
		if(type.equals("TadpoleSystem")) 	return TADPOLE_SYSTEM_DEFAULT;
		if(type.equals("TadpoleSystem_CUBRID")) 	return TADPOLE_SYSTEM_CUBRID_DEFAULT;
		
		else if(type.equals("Oracle")) 		return ORACLE_DEFAULT;
		
		else if(type.equals("MSSQL")) 		return MSSQL_DEFAULT;
		else if(type.equals("MSSQL_8_LE"))	return MSSQL_8_LE_DEFAULT;
		
		else if(type.equals("MySQL")) 		return MYSQL_DEFAULT;
		else if(type.equals("SQLite"))		return SQLite_DEFAULT;
		else if(type.equals("CUBRID"))		return CUBRID_DEFAULT;
		else if(type.equals("POSTGRE"))		return POSTGRE_DEFAULT;
		
		else if(type.equals("MONGODB"))		return MONGODB_DEFAULT;
		else return null;
	}
	
	/**
	 * DB URL INFO를 얻는다.
	 * 
	 * @param type
	 * @return
	 */
	public String getDB_URL_INFO() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT:		return "jdbc:sqlite:/%s";
			case TADPOLE_SYSTEM_CUBRID_DEFAULT:	return "jdbc:CUBRID:%s:%s:%s:::";
			
			case ORACLE_DEFAULT:	return "jdbc:oracle:thin:@%s:%s:%s";
			
			case MSSQL_DEFAULT:		return "jdbc:jtds:sqlserver://%s:%s/%s";
			case MSSQL_8_LE_DEFAULT: return "jdbc:jtds:sqlserver://%s:%s/%s";
			
			case MYSQL_DEFAULT:		return "jdbc:mysql://%s:%s/%s";
			case SQLite_DEFAULT:	return "jdbc:sqlite:/%s";
			case CUBRID_DEFAULT:	return "jdbc:CUBRID:%s:%s:%s:::";
			case POSTGRE_DEFAULT:	return "jdbc:postgresql://%s:%s/%s";	
			
			/** http://api.mongodb.org/java/1.2/com/mongodb/DBAddress.html */
			case MONGODB_DEFAULT:		return "%s:%s/%s";
			
			default:
				return "undefine db";
		}
	}
	
	public String getDBToString() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT:		return "TadpoleSystem";
			case TADPOLE_SYSTEM_CUBRID_DEFAULT: return "TadpoleSystem_CUBRID";
		
			case ORACLE_DEFAULT:	return "Oracle";
			
			case MSSQL_DEFAULT:			return "MSSQL";
			case MSSQL_8_LE_DEFAULT:	return "MSSQL_8_LE";
			
			case MYSQL_DEFAULT:		return "MySQL";
			case SQLite_DEFAULT:	return "SQLite";
			case CUBRID_DEFAULT:	return "CUBRID";
			case POSTGRE_DEFAULT:	return "POSTGRE";
			
			case MONGODB_DEFAULT :  return "MONGODB";
			default:
				return "undefine db";
		}
	}
	
	/**
	 * 사용자 디비를 리턴한다.
	 * @return
	 */
	public static List<DBDefine> userDBValues() {
		List<DBDefine> supportDb = new ArrayList<DBDefine>(); 
		supportDb.add(CUBRID_DEFAULT);
		
		supportDb.add(MONGODB_DEFAULT);
		
		supportDb.add(MYSQL_DEFAULT);		
		supportDb.add(MSSQL_DEFAULT);		
		
		supportDb.add(ORACLE_DEFAULT);
		supportDb.add(POSTGRE_DEFAULT);
		supportDb.add(SQLite_DEFAULT);		
		
		return supportDb;
	}
}
