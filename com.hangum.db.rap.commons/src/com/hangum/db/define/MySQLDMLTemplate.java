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
 * MySQL 디비의 쿼리를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class MySQLDMLTemplate {
	public static final String LINE_SEPARATION = "\r\n";
	
	/**  
	 * mysql 을 수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = " %s limit %s,%s";//"select * from ( %s ) _res__ult_ limit %s,%s";
	
	public static final String TMP_EXPLAIN_EXTENDED = "explain extended  ";
	
	/** table - mysql */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE   sample_table   (  " + LINE_SEPARATION + 
						 " id INT(11) default NULL auto_increment,   " + LINE_SEPARATION +
						 " name char(60) default NULL,  " + LINE_SEPARATION +
						 " PRIMARY KEY (id)  " + LINE_SEPARATION +
						" );";

	
	/** view -mysql */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW   view_name   AS  " + LINE_SEPARATION + 
																" SELECT   columns   FROM   table;";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE INDEX  index_name \r\n  ON table_name ( columns );";
	
	
	
	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE simpleproc (OUT param1 INT) " + LINE_SEPARATION +
																	  " BEGIN  " + LINE_SEPARATION +
																	  "  SELECT COUNT(*) INTO param1 FROM t;  " + LINE_SEPARATION +
																	  " END;";
	
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION hello (s CHAR(20)) RETURNS CHAR(50)  " + LINE_SEPARATION + 
																	" RETURN CONCAT('Hello, 's');";
	
	
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER testref BEFORE INSERT ON test1  " + LINE_SEPARATION +
																"	  FOR EACH ROW BEGIN   " + LINE_SEPARATION +
																"	    INSERT INTO test2 SET a2 = NEW.a1;   " + LINE_SEPARATION +
																"	    DELETE FROM test3 WHERE a3 = NEW.a1;   " + LINE_SEPARATION +
																"	    UPDATE test4 SET b4 = b4 + 1 WHERE a4 = NEW.a1;   " + LINE_SEPARATION +
																"	  END;";
}
