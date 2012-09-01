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
package com.hangum.db.define;

/**
 * MS_SQL 디비의 쿼리를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class MSSQLDMLTemplate {
	/**  
	 * mssql 을 수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = "select * from ( %s ) _res__ult_ limit %s,%s";
//	"select * from ( %s ) where ROWNUM > %s and ROWNUM <= %s";
	
	
	public static final String TMP_EXPLAIN_EXTENDED = "explain extended  ";
	
	/** table  */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE sample_table ( \r\n" + 
						 " id INT  NOT NULL,  \r\n" +
						 " name char(60) default NULL, \r\n" +
						 " PRIMARY KEY (id) \r\n" +
						" );";

	
	/** view  */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW   view_name   AS \r\n" + 
																" SELECT   columns   FROM   table;";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE INDEX  index_name \r\n  ON table_name ( columns );";
	
	
	
	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE simpleproc \r\n" +
																	  " AS \r\n"+
																	  "  SELECT COUNT(*) INTO param1 FROM t;";
	
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION fc_sample_table5 (@v_name VARCHAR ) \n" +
														"	RETURNS TABLE	\n" +
														"	AS	\n" +
														"	RETURN (	\n" +
														"	 SELECT  *	\n" +
														"	 FROM  sample_table\n" +
														"	 WHERE name = @v_name\n" +
														"	); ";
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER reminder1 \n" +
														"	ON sample_table \n" +
														"	AFTER INSERT, UPDATE  \n" +
														"	AS RAISERROR ('Notify Customer Relations', 16, 10); ";
}
