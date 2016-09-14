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
	
	
	
}
