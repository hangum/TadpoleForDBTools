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
import com.hangum.tadpole.db.dynamodb.core.manager.DynamoDBManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * TadpoleDBHub에서 지원하는 디비를 정의한다.
 * 
 * @author hangum
 *
 */
public enum DBDefine {
	/** Tadpole System DB */
	TADPOLE_SYSTEM_DEFAULT,
	TADPOLE_SYSTEM_MYSQL_DEFAULT,
	
	/** USER DB */
	DYNAMODB_DEFAULT,
	ORACLE_DEFAULT,
	TIBERO_DEFAULT,
	MSSQL_DEFAULT,
	MSSQL_8_LE_DEFAULT,
	
	MYSQL_DEFAULT,
	MARIADB_DEFAULT,
	SQLite_DEFAULT,
	CUBRID_DEFAULT,
	AMAZON_REDSHIFT_DEFAULT,
	POSTGRE_DEFAULT,
	ALTIBASE_DEFAULT,
	NETEZZA_DEFAULT,
	
	/** hive */
	HIVE_DEFAULT,
	HIVE2_DEFAULT,
	
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
			
			case DYNAMODB_DEFAULT:		return prefix + "DynamoConfig.xml";
			case ORACLE_DEFAULT:		return prefix + "OracleConfig.xml";
			case TIBERO_DEFAULT:		return prefix + "TiberoConfig.xml";
			case MSSQL_DEFAULT:			return prefix + "MSSQLConfig.xml";
			case MSSQL_8_LE_DEFAULT: 	return prefix + "MSSQLConfig_8_LE.xml";
			
			case MYSQL_DEFAULT:			return prefix + "MySQLConfig.xml";
			case MARIADB_DEFAULT:		return prefix + "MariaDBConfig.xml";
			
			case SQLite_DEFAULT:		return prefix + "SQLiteConfig.xml";
			case CUBRID_DEFAULT:		return prefix + "CUBRIDConfig.xml";
			case POSTGRE_DEFAULT:		return prefix + "POSTGREConfig.xml";
			case HIVE_DEFAULT:			return prefix + "HIVEConfig.xml";
			case HIVE2_DEFAULT:			return prefix + "HIVE2Config.xml";
			case ALTIBASE_DEFAULT:	    return prefix + "AltibaseConfig.xml";
			case AMAZON_REDSHIFT_DEFAULT: return prefix + "AmazonRedshiftConfig.xml";
			case NETEZZA_DEFAULT:		return prefix + "NetezzaConfig.xml";
			default:
				return "Doesn't define database configuration";
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
		
		else if(type.equalsIgnoreCase("DynamoDB")) 		return DYNAMODB_DEFAULT;
		else if(type.equalsIgnoreCase("Oracle")) 		return ORACLE_DEFAULT;
		else if(type.equalsIgnoreCase("Tibero")) 		return TIBERO_DEFAULT;
		
		else if(type.equalsIgnoreCase("MSSQL")) 		return MSSQL_DEFAULT;
		else if(type.equalsIgnoreCase("MSSQL_8_LE"))	return MSSQL_8_LE_DEFAULT;
		
		else if(type.equalsIgnoreCase("MySQL")) 		return MYSQL_DEFAULT;
		else if(type.equalsIgnoreCase("MariaDB")) 		return MARIADB_DEFAULT;
		
		else if(type.equalsIgnoreCase("SQLite"))		return SQLite_DEFAULT;
		else if(type.equalsIgnoreCase("Cubrid"))		return CUBRID_DEFAULT;
		else if(type.equalsIgnoreCase("PostgreSQL"))	return POSTGRE_DEFAULT;
		
		else if(type.equalsIgnoreCase("MongoDB"))		return MONGODB_DEFAULT;
//		else if(type.equalsIgnoreCase("AmazonRDS")) 	return AMAZONRDS_DEFAULT;
		else if(type.equalsIgnoreCase("Apache Hive")) 	return HIVE_DEFAULT;
		else if(type.equalsIgnoreCase("Apache Hive2")) 	return HIVE2_DEFAULT;
		
		else if(type.equalsIgnoreCase("Altibase"))       return ALTIBASE_DEFAULT;
		else if(type.equalsIgnoreCase("RedShift"))       return AMAZON_REDSHIFT_DEFAULT;
		else if(type.equalsIgnoreCase("Netezza"))       return NETEZZA_DEFAULT;
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
			case NETEZZA_DEFAULT:	return "org.netezza.Driver";
			
			case ALTIBASE_DEFAULT:   return "com.amazon.redshift.jdbc.Driver";
			case AMAZON_REDSHIFT_DEFAULT : return "";
			
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
			
			case DYNAMODB_DEFAULT:	return DynamoDBManager.CONNECTION_URL;//
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
			
			/* Altibase JDBC connection string: jdbc:Altibase://ipaddr.port/dbname */
			case ALTIBASE_DEFAULT:   return "jdbc:Altibase://%s:%s/%s";

			case AMAZON_REDSHIFT_DEFAULT : return "jdbc:redshift://%s:%s/%s";
			
			case NETEZZA_DEFAULT:	return "jdbc:netezza://%s/%s";
			default:
				return "undefine db";
		}
	}
	
	public String getDBToString() {
		switch ( this ) {
			case TADPOLE_SYSTEM_DEFAULT:		return "TadpoleSystem";
			case TADPOLE_SYSTEM_MYSQL_DEFAULT: 	return "TadpoleSystem_MYSQL";
		
			case DYNAMODB_DEFAULT: 		return "DynamoDB";
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
			
			case ALTIBASE_DEFAULT:      return "Altibase";
			case AMAZON_REDSHIFT_DEFAULT : return "RedShift";
			case NETEZZA_DEFAULT:		return "Netezza";
			default:
				return "undefine db";
		}
	}
	
	/**
	 * 디비의 기본 validate 쿼리를 정의합니다.
	 * @return
	 */
	public String getValidateQuery(boolean isTransaction) {
		String strConnection = "TadpoleHub_None";
		if(isTransaction) strConnection = "TadpoleHub_Tran";
		
		if(this == MYSQL_DEFAULT || this == MARIADB_DEFAULT) {
			return String.format("SELECT '%s'", strConnection);
		} else if(this == ORACLE_DEFAULT || this == TIBERO_DEFAULT) {
			return String.format("SELECT '%s' FROM dual", strConnection);
		} else if(this == MSSQL_DEFAULT || this == MSSQL_8_LE_DEFAULT) {
			return String.format("SELECT '%s'", strConnection);
		} else if(this == SQLite_DEFAULT) {
			return "SELECT name FROM sqlite_master where 1 = 0";
		} else if(this == HIVE_DEFAULT || this == HIVE2_DEFAULT) {
			return "show databases";
		} else if(this == POSTGRE_DEFAULT || this == AMAZON_REDSHIFT_DEFAULT) {
			return String.format("SELECT '%s'", strConnection);
		} else if(this == CUBRID_DEFAULT) {
			return String.format("select '%s' from db_root", strConnection);
		} else if(this == ALTIBASE_DEFAULT) {
			return String.format("SELECT '%s'", strConnection);
		} else if(this == NETEZZA_DEFAULT) {
			return "SELECT version()";
		} else {
			return "SELECT 1";
		}
	}
	
	/**
	 * 에디터에서 사용할 확장자를 만듭니다.
	 * @return
	 */
	public String getExt() {
		String extension = ""; //$NON-NLS-1$
		
		if(this == MYSQL_DEFAULT) {
			extension += "mysql"; //$NON-NLS-1$
		} else if(this == MARIADB_DEFAULT) {
			extension += "mariadb"; //$NON-NLS-1$
		} else if(this == ORACLE_DEFAULT) {
			extension += "oracle"; //$NON-NLS-1$
		} else if(this == MSSQL_DEFAULT || this == MSSQL_8_LE_DEFAULT) {
			extension += "mssql"; //$NON-NLS-1$
		} else if(this == SQLite_DEFAULT) {
			extension += "sqlite"; //$NON-NLS-1$
		} else if(this == HIVE_DEFAULT || this == HIVE2_DEFAULT) {
			extension += "hql"; //$NON-NLS-1$
		} else if(this == POSTGRE_DEFAULT) {
			extension += "pgsql"; //$NON-NLS-1$
		} else if(this == CUBRID_DEFAULT) {
			extension += "cubrid"; //$NON-NLS-1$
		} else if(this == ALTIBASE_DEFAULT) {
			extension += "altibase";
		} else if(this == TIBERO_DEFAULT) {
			extension += "tibero";
		} else if(this == MONGODB_DEFAULT) {
			extension += "mongo";
		} else if(this == AMAZON_REDSHIFT_DEFAULT) {
			extension += "RedShift";
		} else if(this == DBDefine.NETEZZA_DEFAULT) {
			extension += "Netezza";
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
		case POSTGRE_DEFAULT:		
		case AMAZON_REDSHIFT_DEFAULT:
		case NETEZZA_DEFAULT:
									return DBVariableDefine.PGSQL_VARIABLES;
		
		case MONGODB_DEFAULT :  	return DBVariableDefine.MONGO_VARIABLE;		
		case HIVE_DEFAULT: 			return DBVariableDefine.HIVE_VARIABLE;
		case HIVE2_DEFAULT: 		return DBVariableDefine.HIVE2_VARIABLE;
		
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
		
//		listSupportDb.remove(AMAZONRDS_DEFAULT);
		listSupportDb.remove(HIVE_DEFAULT);
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
		
		supportDb.add(CUBRID_DEFAULT);
		supportDb.add(DYNAMODB_DEFAULT);

		supportDb.add(MARIADB_DEFAULT);
		supportDb.add(MONGODB_DEFAULT);
		supportDb.add(MSSQL_DEFAULT);
		supportDb.add(MYSQL_DEFAULT);
		supportDb.add(NETEZZA_DEFAULT);
		
		supportDb.add(ORACLE_DEFAULT);
		supportDb.add(TIBERO_DEFAULT);
		
		supportDb.add(AMAZON_REDSHIFT_DEFAULT);
		supportDb.add(POSTGRE_DEFAULT);

		supportDb.add(SQLite_DEFAULT);
		
		return supportDb;
	}
}
