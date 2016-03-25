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
package com.hangum.tadpole.engine.sql.template;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * cubrid 디비의 쿼리를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class CubridDMLTemplate extends MySQLDMLTemplate {
	/**  
	 * cubrid 을 수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = "SELECT tdb_a.* FROM (%s) tdb_a LIMIT %s, %s";
	
	/**
	 * 쿼리 플렌을 정의 합니다.
	 */
	public static final String TMP_EXPLAIN_EXTENDED = "explain extended ";
	
	/** table create */
	public static final String TMP_CREATE_TABLE_STMT = "CREATE TABLE sample_table ( " + PublicTadpoleDefine.LINE_SEPARATOR + 
						 "	id INT(11), " + PublicTadpoleDefine.LINE_SEPARATOR +
						 "	name char(60) default NULL, " + PublicTadpoleDefine.LINE_SEPARATOR +
						 "	PRIMARY KEY (id) " + PublicTadpoleDefine.LINE_SEPARATOR +
						");";

	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE \"addathlete\"(\"name\" STRING,\"gender\" STRING,\"nation_code\" STRING,\"event\" STRING) " + PublicTadpoleDefine.LINE_SEPARATOR +  
															"	AS LANGUAGE JAVA " + PublicTadpoleDefine.LINE_SEPARATOR + 
															"	NAME 'Athlete.insert(java.lang.String, java.lang.String, java.lang.String, java.lang.String)';";
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION hello (s CHAR(20)) RETURNS CHAR(50) " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"RETURN CONCAT('Hello, 's');";
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER sampelTrigger " + PublicTadpoleDefine.LINE_SEPARATOR +
														"	BEFORE INSERT ON tableName " + PublicTadpoleDefine.LINE_SEPARATOR +
														"	IF obj.seats > 100000 " + PublicTadpoleDefine.LINE_SEPARATOR +
														"EXECUTE REJECT;";
}