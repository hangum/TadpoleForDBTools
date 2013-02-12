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
package com.hangum.tadpole.define;

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
	public static final String TMP_GET_PARTDATA = "%s limit %s, %s";
	
	/**
	 * 쿼리 플렌을 정의 합니다.
	 */
	public static final String TMP_EXPLAIN_EXTENDED = "explain extended  ";
	
	/** table create */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE   sample_table   ( \r\n" + 
						 " id INT(11),  \r\n" +
						 " name char(60) default NULL, \r\n" +
						 " PRIMARY KEY (id) \r\n" +
						" );";

	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE \"addathlete\"(\"name\" STRING,\"gender\" STRING,\"nation_code\" STRING,\"event\" STRING)  \r\n" +  
																	" AS LANGUAGE JAVA  \r\n" + 
																	" NAME 'Athlete.insert(java.lang.String, java.lang.String, java.lang.String, java.lang.String)';";	
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION hello (s CHAR(20)) RETURNS CHAR(50) \r\n" + 
																	" RETURN CONCAT('Hello, 's');";
	
	
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER sampelTrigger   \r\n" +
																			" BEFORE INSERT ON tableName  \r\n" +
																			" IF obj.seats > 100000  \r\n" +
																			" EXECUTE REJECT;";
}
