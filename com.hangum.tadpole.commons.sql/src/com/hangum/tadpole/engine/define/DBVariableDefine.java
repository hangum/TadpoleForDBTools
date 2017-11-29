/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
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
	public static String[] MONGO_VARIABLE = MYSQL_VARIABLES;
	public static String[] ALTIBASE_VARIABLE = MYSQL_VARIABLES;
}
