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

import org.apache.log4j.Logger;

/**
 * Define database group
 * reference {@code DBDefine}
 * 
 * @author hangum
 *
 */
public enum DBGroupDefine {
	/* oracle, tibero */
	ORACLE_GROUP,
	/* mssql 8le, mssql */
	MSSQL_GROUP,
	/* mysql, mariadb */
	MYSQL_GROUP,
	/* postgre, agens */
	POSTGRE_GROUP,
	/* hive, hive2 */
	HIVE_GROUP,
	/* sqlite */
	SQLITE_GROUP,
	/* tajo */
	TAJO_GROUP,
	/* mongodb */
	MONGODB_GROUP,
	/* cubrid */
	CUBRID_GROUP,
	/* altibase */
	ALTIBASE_GROUP
	;
	
	private static final Logger logger = Logger.getLogger(DBGroupDefine.class);
	
	public static DBGroupDefine getGroup(DBDefine dbDefine) {
		if(dbDefine == DBDefine.ORACLE_DEFAULT || dbDefine == DBDefine.TIBERO_DEFAULT) {
			return ORACLE_GROUP;
		} else if(dbDefine == DBDefine.MSSQL_DEFAULT || dbDefine == DBDefine.MSSQL_8_LE_DEFAULT) {
			return MSSQL_GROUP;
		} else if(dbDefine == DBDefine.MYSQL_DEFAULT || dbDefine == DBDefine.MARIADB_DEFAULT) {
			return MYSQL_GROUP;
		} else if(dbDefine == DBDefine.POSTGRE_DEFAULT || dbDefine == DBDefine.AGENSGRAPH_DEFAULT) {
			return POSTGRE_GROUP;
		} else if(dbDefine == DBDefine.HIVE_DEFAULT || dbDefine == DBDefine.HIVE2_DEFAULT) {
			return HIVE_GROUP;
		} else if(dbDefine == DBDefine.SQLite_DEFAULT) {
			return SQLITE_GROUP;
		} else if(dbDefine == DBDefine.CUBRID_DEFAULT) {
			return CUBRID_GROUP;
		} else if(dbDefine == DBDefine.TAJO_DEFAULT) {
			return TAJO_GROUP;
		} else if(dbDefine == DBDefine.ALTIBASE_DEFAULT) {
			return ALTIBASE_GROUP;
		} else {
			return MONGODB_GROUP;
		}
	}
	
}
