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
package com.hangum.tadpole.engine.define;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

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
	TADPOLE_SYSTEM_MYSQL_DEFAULT,
	
	/** USER DB */
	ORACLE_DEFAULT,
	TIBERO_DEFAULT,
	MSSQL_DEFAULT,
	MSSQL_8_LE_DEFAULT,
	
	MYSQL_DEFAULT,
	MARIADB_DEFAULT,
	SQLite_DEFAULT,
	CUBRID_DEFAULT,
	POSTGRE_DEFAULT,
	ALTIBASE_DEFAULT,
	
	/** hive */
	HIVE_DEFAULT,
	HIVE2_DEFAULT,
	
	/** tajo */
	TAJO_DEFAULT,
	
	/** NO SQL */
	MONGODB_DEFAULT
//	,
	
	/** Aamazon RDS */
//	AMAZONRDS_DEFAULT
	;	
	
	private static final Logger logger = Logger.getLogger(DBDefine.class);
	
	/** 환경정보가 있는 상대 위치 */
	private final static String prefix 			= "com/hangum/tadpole/engine/config/";
	private final static String prefix_system 	= "com/hangum/tadpole/engine/config/internal/system/";	

	/**
	 * db 환경이 있는 정보를 리턴한다.
	 * 
	 * @return
	 */
	public String getLocation() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT: return prefix_system + "TadpoleSystem-SQLite-Config.xml";
			case TADPOLE_SYSTEM_MYSQL_DEFAULT: return prefix_system + "TadpoleSystem-MYSQL-Config.xml";
			
			case ORACLE_DEFAULT:	return prefix + "OracleConfig.xml";
			case TIBERO_DEFAULT:	return prefix + "TiberoConfig.xml";
			case MSSQL_DEFAULT:		return prefix + "MSSQLConfig.xml";
			case MSSQL_8_LE_DEFAULT: return prefix + "MSSQLConfig_8_LE.xml";
			
			case MYSQL_DEFAULT:		return prefix + "MySQLConfig.xml";
			case MARIADB_DEFAULT:		return prefix + "MariaDBConfig.xml";
			
			case SQLite_DEFAULT:		return prefix + "SQLiteConfig.xml";
			case CUBRID_DEFAULT:		return prefix + "CUBRIDConfig.xml";
			case POSTGRE_DEFAULT:		return prefix + "POSTGREConfig.xml";
			case HIVE_DEFAULT:			return prefix + "HIVEConfig.xml";
			case HIVE2_DEFAULT:			return prefix + "HIVE2Config.xml";
			case TAJO_DEFAULT:			return prefix  + "TAJOConfig.xml";
			case ALTIBASE_DEFAULT:	    return prefix + "AltibaseConfig.xml";
			default:
				return "undefine db";
		}
	}
	
	/**
	 * DB TYPE을 얻는다.
	 * @param type
	 * @return
	 * @deprecated can use {@DBDefine#getDBDefine()}
	 */
	public static DBDefine getDBDefine(String type) {
		
		if(type.equalsIgnoreCase("TadpoleSystem")) 		return TADPOLE_SYSTEM_DEFAULT;
		if(type.equalsIgnoreCase("TadpoleSystem_MYSQL")) 	return TADPOLE_SYSTEM_MYSQL_DEFAULT;
		
		else if(type.equalsIgnoreCase("Oracle")) 		return ORACLE_DEFAULT;
		else if(type.equalsIgnoreCase("Tibero")) 		return TIBERO_DEFAULT;
		
		else if(type.equalsIgnoreCase("MSSQL")) 		return MSSQL_DEFAULT;
		else if(type.equalsIgnoreCase("MSSQL_8_LE"))	return MSSQL_8_LE_DEFAULT;
		
		else if(type.equalsIgnoreCase("MySQL")) 		return MYSQL_DEFAULT;
		else if(type.equalsIgnoreCase("MariaDB")) 		return MARIADB_DEFAULT;
		
		else if(type.equalsIgnoreCase("SQLite"))		return SQLite_DEFAULT;
		else if(type.equalsIgnoreCase("Cubrid"))		return CUBRID_DEFAULT;
		else if(type.equalsIgnoreCase("PostgreSQL"))		return POSTGRE_DEFAULT;
		
		else if(type.equalsIgnoreCase("MongoDB"))		return MONGODB_DEFAULT;
//		else if(type.equalsIgnoreCase("AmazonRDS")) 	return AMAZONRDS_DEFAULT;
		else if(type.equalsIgnoreCase("Apache Hive")) 	return HIVE_DEFAULT;
		else if(type.equalsIgnoreCase("Apache Hive2")) 	return HIVE2_DEFAULT;
		
		else if(type.equalsIgnoreCase("Apache Tajo")) 	return TAJO_DEFAULT;
		else if(type.equalsIgnoreCase("Altibase"))       return ALTIBASE_DEFAULT;
		else return null;
	}
	
	/**
	 * DB Typed을 얻습니다.
	 * @param userDB
	 * @return
	 */
	public static DBDefine getDBDefine(UserDBDAO userDB) {
		return getDBDefine(userDB.getDbms_type());
	}
	
	/**
	 * Define default class
	 * @return
	 */
	public String getDriverClass() {
		switch ( this ) {
			case ORACLE_DEFAULT:	return "oracle.jdbc.driver.OracleDriver";
			case TIBERO_DEFAULT:	return "com.tmax.tibero.jdbc.TbDriver";
			
			case MSSQL_DEFAULT:		
			case MSSQL_8_LE_DEFAULT: return "net.sourceforge.jtds.jdbc.Driver";
			
			case MYSQL_DEFAULT:		return "com.mysql.jdbc.Driver";
			case MARIADB_DEFAULT:	return "org.mariadb.jdbc.Driver";
			
			case SQLite_DEFAULT:	return "org.sqlite.JDBC";
			case CUBRID_DEFAULT:	return "cubrid.jdbc.driver.CUBRIDDriver";
			case POSTGRE_DEFAULT:	return "org.postgresql.Driver";	

			case HIVE_DEFAULT:		return "org.apache.hadoop.hive.jdbc.HiveDriver";
			case HIVE2_DEFAULT:		return "org.apache.hive.jdbc.HiveDriver";
			case TAJO_DEFAULT:		return "org.apache.tajo.jdbc.TajoDriver";
			
			case ALTIBASE_DEFAULT:   return "Altibase.jdbc.driver.AltibaseDriver"; 
			
			default:
				return "undefine class";
		}
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
			case TADPOLE_SYSTEM_MYSQL_DEFAULT:	return "jdbc:mysql://%s:%s/%s";
			
			case ORACLE_DEFAULT:	return "jdbc:oracle:thin:@%s:%s:%s";
			case TIBERO_DEFAULT:	return "jdbc:tibero:thin:@%s:%s:%s";
			
			case MSSQL_DEFAULT:		
			case MSSQL_8_LE_DEFAULT: return "jdbc:jtds:sqlserver://%s:%s/%s";
			
			case MYSQL_DEFAULT:		return "jdbc:mysql://%s:%s/%s";
			case MARIADB_DEFAULT:	return "jdbc:mariadb://%s:%s/%s";
			
			case SQLite_DEFAULT:	return "jdbc:sqlite:/%s";
			case CUBRID_DEFAULT:	return "jdbc:cubrid:%s:%s:%s:::";
			/*
			 * postgresql이 ssl 을 지원할 경우 는 ?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory 를 써줘야합니다. 
			 */
			case POSTGRE_DEFAULT:	return "jdbc:postgresql://%s:%s/%s";	
			
			/* http://api.mongodb.org/java/1.2/com/mongodb/DBAddress.html
			 *  
			 * mongodb://[dbuser:dbpassword@]host:port/dbname
			 */
			case MONGODB_DEFAULT:	return "mongodb://%s:%s";//@%s:%s:%s";
			
			case HIVE_DEFAULT:		return "jdbc:hive://%s:%s/%s";
			case HIVE2_DEFAULT:		return "jdbc:hive2://%s:%s/%s";
			
			case TAJO_DEFAULT:		return "jdbc:tajo://%s:%s/%s";
			
			/* Altibase JDBC connection string: jdbc:Altibase://ipaddr.port/dbname */
			case ALTIBASE_DEFAULT:   return "jdbc:Altibase://%s:%s/%s"; 
			
			default:
				return "undefine db";
		}
	}
	
	public String getDBToString() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT:		return "TadpoleSystem";
			case TADPOLE_SYSTEM_MYSQL_DEFAULT: 	return "TadpoleSystem_MYSQL";
		
			case ORACLE_DEFAULT:		return "Oracle";
			case TIBERO_DEFAULT:		return "Tibero";
			
			case MSSQL_DEFAULT:			return "MSSQL";
			case MSSQL_8_LE_DEFAULT:	return "MSSQL_8_LE";
			
			case MYSQL_DEFAULT:			return "MySQL";
			case MARIADB_DEFAULT:		return "MariaDB";
			
			case SQLite_DEFAULT:		return "SQLite";
			case CUBRID_DEFAULT:		return "Cubrid";
			case POSTGRE_DEFAULT:		return "PostgreSQL";
			
			case MONGODB_DEFAULT :  	return "MongoDB";
			
//			case AMAZONRDS_DEFAULT: 	return "AmazonRDS";
			
			case HIVE_DEFAULT: 			return "Apache Hive";
			case HIVE2_DEFAULT: 			return "Apache Hive2";
			
			case TAJO_DEFAULT: 			return "Apache Tajo";
			case ALTIBASE_DEFAULT:      return "Altibase";
			default:
				return "undefine db";
		}
	}
	
	/**
	 * 에디터에서 사용할 확장자를 만듭니다.
	 * @return
	 */
	public String getExt() {
		String extension = ""; //$NON-NLS-1$
		
		if(this == DBDefine.MYSQL_DEFAULT) {
			extension += "mysql"; //$NON-NLS-1$
		} else if(this == DBDefine.MARIADB_DEFAULT) {
			extension += "mariadb"; //$NON-NLS-1$
		} else if(this == DBDefine.ORACLE_DEFAULT) {
			extension += "oracle"; //$NON-NLS-1$
		} else if(this == DBDefine.MSSQL_DEFAULT || this == DBDefine.MSSQL_8_LE_DEFAULT) {
			extension += "mssql"; //$NON-NLS-1$
		} else if(this == DBDefine.SQLite_DEFAULT) {
			extension += "sqlite"; //$NON-NLS-1$
		} else if(this == DBDefine.HIVE_DEFAULT || this == DBDefine.HIVE2_DEFAULT) {
			extension += "hql"; //$NON-NLS-1$
		} else if(this == DBDefine.POSTGRE_DEFAULT) {
			extension += "pgsql"; //$NON-NLS-1$
		} else if(this == DBDefine.CUBRID_DEFAULT) {
			extension += "cubrid"; //$NON-NLS-1$
		} else if(this == DBDefine.TAJO_DEFAULT) {
			extension += "tajo"; //$NON-NLS-1$
		} else if(this == DBDefine.ALTIBASE_DEFAULT) {
			extension += "altibase";
		} else if(this == DBDefine.TIBERO_DEFAULT) {
			extension += "tibero";
		} else if(this == DBDefine.MONGODB_DEFAULT) {
			extension += "mongo";
		} else {
			extension += "sql"; //$NON-NLS-1$
		}
		
		return extension;
	}
	
	/** 
	 * define System variable query
	 * @return
	 */
	public String[] getSystemVariableQuery() {
		switch ( this ) {
		case ORACLE_DEFAULT:		return DBVariableDefine.ORACLE_VARIABLES;
		case TIBERO_DEFAULT:		return DBVariableDefine.ORACLE_VARIABLES;
		
		case MSSQL_DEFAULT:			return DBVariableDefine.MSSQL_VARIABLES;
		case MSSQL_8_LE_DEFAULT:	return DBVariableDefine.MSSQL_VARIABLES;
		
		case MYSQL_DEFAULT:			return DBVariableDefine.MYSQL_VARIABLES;
		case MARIADB_DEFAULT:		return DBVariableDefine.MARIA_VARIABLES;
		
		case SQLite_DEFAULT:		return DBVariableDefine.SQLITE_VARIABLES;
		case CUBRID_DEFAULT:		return DBVariableDefine.CUBRID_VARIABLES;
		case POSTGRE_DEFAULT:		return DBVariableDefine.PGSQL_VARIABLES;
		
		case MONGODB_DEFAULT :  	return DBVariableDefine.MONGO_VARIABLE;		
		case HIVE_DEFAULT: 			return DBVariableDefine.HIVE_VARIABLE;
		case HIVE2_DEFAULT: 		return DBVariableDefine.HIVE2_VARIABLE;
		
		case TAJO_DEFAULT: 			return DBVariableDefine.TAJO_VARIABLE;
		case ALTIBASE_DEFAULT:      return DBVariableDefine.ALTIBASE_VARIABLE;
		default:
			return new String[]{};
		}
	}
	
	/**
	 * 사용자 디비를 리턴한다.
	 * @return
	 */
	public static List<DBDefine> userDBValues() {
		List<DBDefine> listSupportDb = new ArrayList<DBDefine>();
		
		if(ApplicationArgumentUtils.isUseDB()) {
			
			try {
				String strUserDB = ApplicationArgumentUtils.getUseDB();
				if("all".equalsIgnoreCase(strUserDB)) listSupportDb = allUserUseDB();
				else {
					String[] arryUseDBTypes = StringUtils.split(strUserDB, ",");
					
					for (String strDBTyps : arryUseDBTypes) {
						DBDefine tmpDBDefine = getDBDefine(strDBTyps);
						if(tmpDBDefine != null) {
							listSupportDb.add(tmpDBDefine); 
						} else {
							logger.error("*** Error : Not support DB. " + strDBTyps);
						}
					}
				}
				
			} catch (Exception e) {
				logger.error("System initialize exception", e);
				System.exit(0);
			}
			
		} else {
			listSupportDb = allUserUseDB();
		}
		
		return listSupportDb;
	}
	
	/**
	 * get driver list
	 * 
	 * @return
	 */
	public static List<DBDefine> getDriver() {
		List<DBDefine> listSupportDb = userDBValues();
		
//		listSupportDb.remove(DBDefine.AMAZONRDS_DEFAULT);
		listSupportDb.remove(DBDefine.TAJO_DEFAULT);
		listSupportDb.remove(DBDefine.HIVE_DEFAULT);
		return listSupportDb;
	}
	
	/**
	 * 사용자가 사용할 수 있는 모든 디비.
	 * @return
	 */
	private static List<DBDefine> allUserUseDB() {
		List<DBDefine> supportDb = new ArrayList<DBDefine>();

		supportDb.add(ALTIBASE_DEFAULT);
		supportDb.add(HIVE_DEFAULT);
//		supportDb.add(AMAZONRDS_DEFAULT);
		supportDb.add(TAJO_DEFAULT);
		
		supportDb.add(CUBRID_DEFAULT);
		
		supportDb.add(MONGODB_DEFAULT);
		
		supportDb.add(MARIADB_DEFAULT);
		supportDb.add(MYSQL_DEFAULT);		
		supportDb.add(MSSQL_DEFAULT);		
		
		supportDb.add(ORACLE_DEFAULT);
		supportDb.add(TIBERO_DEFAULT);
		supportDb.add(POSTGRE_DEFAULT);
		supportDb.add(SQLite_DEFAULT);
		
		return supportDb;
	}
}
