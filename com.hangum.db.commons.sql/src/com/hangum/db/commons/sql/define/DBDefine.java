package com.hangum.db.commons.sql.define;

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
	
	/* USER DB */
	ORACLE_DEFAULT,
	MSSQL_DEFAULT,
//	DB2_DEFAULT,
//	SYBASE_IQ_DEFAULT,
//	SYBASE_ASE_DEFAULT,
	MYSQL_DEFAULT,
//	Altibase,
//	Informix,
//	Teradata,
//	Tibero,
	SQLite_DEFAULT,
	CUBRID_DEFAULT, 
	
	/* NO SQL */
	MONGODB_DEFAULT
	;	
	

	/** 환경정보가 있는 상대 위치 */
	private final static String prefix = "com/hangum/db/commons/sql/config/";

	/**
	 * db 환경이 있는 정보를 리턴한다.
	 * 
	 * @return
	 */
	public String getLocation() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT: return prefix + "TadpoleSystem-SQLite-Config.xml";
			
			case ORACLE_DEFAULT:	return prefix + "OracleConfig.xml";
			case MSSQL_DEFAULT:		return prefix + "MSSQLConfig.xml";
//			case DB2_DEFAULT:		return prefix + "DB2Config.xml";
//			case SYBASE_IQ_DEFAULT: return prefix + "SYBASE_IQ_Config.xml";
//			case SYBASE_ASE_DEFAULT: return prefix + "SYBASE_ASE_Config.xml";
			case MYSQL_DEFAULT:		return prefix + "MySQLConfig.xml";
//			case Altibase:			return prefix + "AltibaseConfig.xml";
//			case Informix:			return prefix + "InformixConfig.xml";
//			case Teradata:			return prefix + "TeradataConfig.xml";
//			case Tibero:			return prefix + "TiberoConfig.xml";
			case SQLite_DEFAULT:		return prefix + "SQLiteConfig.xml";
			case CUBRID_DEFAULT:		return prefix + "CUBRIDConfig.xml";
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
		
		else if(type.equals("Oracle")) 		return ORACLE_DEFAULT;
		else if(type.equals("MSSQL")) 		return MSSQL_DEFAULT;
//		else if(type.equals("DB2(Unix)")) 	return DB2_DEFAULT;
//		else if(type.equals("SybaseIQ")) 	return SYBASE_IQ_DEFAULT;
//		else if(type.equals("SybaseASE")) 	return SYBASE_ASE_DEFAULT;
		else if(type.equals("MySQL")) 		return MYSQL_DEFAULT;
//		else if(type.equals("Altibase")) 	return Altibase;
//		else if(type.equals("Informix")) 	return Informix;
//		else if(type.equals("Teradata")) 	return Teradata;
//		else if(type.equals("Tibero")) 		return Tibero;
		else if(type.equals("SQLite"))		return SQLite_DEFAULT;
		else if(type.equals("CUBRID"))		return CUBRID_DEFAULT;
		
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
			case TADPOLE_SYSTEM_DEFAULT:	return "jdbc:sqlite:/%s";
			
			case ORACLE_DEFAULT:	return "jdbc:oracle:thin:@%s:%s:%s";
			case MSSQL_DEFAULT:		return "jdbc:jtds:sqlserver://%s:%s/%s";
//			case DB2_DEFAULT:		return "jdbc:mysql://#IP#:#PORT#/#SID#";
//			case SYBASE_IQ_DEFAULT: return "jdbc:mysql://#IP#:#PORT#/#SID#";
//			case SYBASE_ASE_DEFAULT: return "jdbc:mysql://#IP#:#PORT#/#SID#";
			case MYSQL_DEFAULT:		return "jdbc:mysql://%s:%s/%s";
//			case Altibase:			return "jdbc:mysql://#IP#:#PORT#/#SID#?Unicode=true&characterEncoding=EUC_KR";
//			case Informix:			return "jdbc:mysql://#IP#:#PORT#/#SID#";
//			case Teradata:			return "jdbc:mysql://#IP#:#PORT#/#SID#";
//			case Tibero:			return "jdbc:mysql://#IP#:#PORT#/#SID#";
			case SQLite_DEFAULT:			return "jdbc:sqlite:/%s";
			case CUBRID_DEFAULT:			return "jdbc:CUBRID:%s:%s:%s:::";
			/** http://api.mongodb.org/java/1.2/com/mongodb/DBAddress.html */
			case MONGODB_DEFAULT:		return "%s:%s/%s";
			
			default:
				return "undefine db";
		}
	}
	
	public String getDBToString() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT:	return "TadpoleSystem";
		
			case ORACLE_DEFAULT:	return "Oracle";
			case MSSQL_DEFAULT:		return "MSSQL";
//			case DB2_DEFAULT:		return "DB2";
//			case SYBASE_IQ_DEFAULT: return "Sybase IQ";
//			case SYBASE_ASE_DEFAULT: return "Sybase ASE";
			case MYSQL_DEFAULT:		return "MySQL";
//			case Altibase:			return "Altibase";
//			case Informix:			return "Informix";
//			case Teradata:			return "Teradata";
//			case Tibero:			return "Tibero";
			case SQLite_DEFAULT:			return "SQLite";
			case CUBRID_DEFAULT:		return "CUBRID";
			
			case MONGODB_DEFAULT : return "MONGODB";
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
		supportDb.add(SQLite_DEFAULT);		
		
		return supportDb;
	}
}
