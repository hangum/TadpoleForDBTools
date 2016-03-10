package com.hangum.tadpole.engine.define;

/**
 * Define Database variable util
 * 
 * @author hangum
 */
public class DBVariableDefine {
	public static String[] MYSQL_VARIABLES = {"SHOW GLOBAL VARIABLES", "SHOW GLOBAL STATUS", "SHOW ENGINE INNODB STATUS", "SHOW SLAVE STATUS"};
	public static String[] MARIA_VARIABLES = MYSQL_VARIABLES;
	public static String[] ORACLE_VARIABLES = MYSQL_VARIABLES;
	public static String[] PGSQL_VARIABLES = MYSQL_VARIABLES;
	public static String[] CUBRID_VARIABLES = MYSQL_VARIABLES;
	public static String[] MSSQL_VARIABLES = MYSQL_VARIABLES;
	public static String[] SQLITE_VARIABLES = MYSQL_VARIABLES;
	
	public static String[] HIVE_VARIABLE = MYSQL_VARIABLES;
	public static String[] HIVE2_VARIABLE = MYSQL_VARIABLES;
	public static String[] TAJO_VARIABLE = MYSQL_VARIABLES;
	public static String[] MONGO_VARIABLE = MYSQL_VARIABLES;
	public static String[] ALTIBASE_VARIABLE = MYSQL_VARIABLES;
}
