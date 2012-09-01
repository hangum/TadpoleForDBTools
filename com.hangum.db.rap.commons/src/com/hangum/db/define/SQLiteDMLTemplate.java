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
 * SQLite의 디비의 쿼리를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class SQLiteDMLTemplate {
	/**  
	 * sqlite 을 수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = "select * from ( %s ) _res__ult_ limit %s,%s;";
	
	public static final String TMP_EXPLAIN_EXTENDED = "explain  ";
	
	/** table */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE   sample_table   ( \r\n" + 
						 " id INTEGER NOT NULL,  \r\n" +
						 " name char(60) default NULL, \r\n" +
						 " PRIMARY KEY (id) \r\n" +
						" );";

	/** view  */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW   view_name   AS \r\n" + 
																" SELECT   columns   FROM   table;";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE INDEX  index_name  \r\n  ON table_name ( columns );";
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER testref BEFORE INSERT ON test1 \r\n" +
																"	  FOR EACH ROW BEGIN  \r\n" +
																"	    INSERT INTO test2 SET a2 = NEW.a1;  \r\n" +
																"	    DELETE FROM test3 WHERE a3 = NEW.a1;  \r\n" +
																"	    UPDATE test4 SET b4 = b4 + 1 WHERE a4 = NEW.a1;  \r\n" +
																"	  END;";
}
