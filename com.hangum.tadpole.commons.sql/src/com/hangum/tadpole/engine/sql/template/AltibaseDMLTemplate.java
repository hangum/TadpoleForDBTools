/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     sun.han - Initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.template;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * Queries for Altibase
 * 
 * @author sun.han
 */
public class AltibaseDMLTemplate extends AbstractDMLTemplate {
	/**  
	 * Altibase  수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = " %s limit %s,%s";
	
	/**
	 * explain  
	 */
	public static final String TMP_EXPLAIN_EXTENDED = "explain extended ";
	
	/**
	 * explain FORMAT=json (greate than 5.6) 
	 */
	public static final String TMP_EXPLAIN_EXTENDED_JSON = "explain format=json ";
	
	/** table - Altibase */
	public static final String  TMP_CREATE_TABLE_STMT = 
						           "CREATE TABLE table_name ( " + PublicTadpoleDefine.LINE_SEPARATOR + 
						           "	column_1   CHAR(100) NOT NULL, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_2   NCHAR(100), " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_3   VARCHAR(100) DEFAULT 'varchar', " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_4   NVARCHAR(100), " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_5   SMALLINT, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_6   INTEGER NOT NULL, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_7   REAL, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_8   DOUBLE, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_9   NUMERIC, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_10  NUMBER, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_11  DECIMAL, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_12  FLOAT, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_13  DATE, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_14  BIT, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_15  BYTE, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_16  NIBBLE, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_17  BLOB, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	column_18  CLOB, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	PRIMARY KEY " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	( " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	  column_1, " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	  column_6 " + PublicTadpoleDefine.LINE_SEPARATOR +
						           "	)" + PublicTadpoleDefine.LINE_SEPARATOR +
						           ") /* TABLESPACE tablespace_name */;" + PublicTadpoleDefine.LINE_SEPARATOR;
	
	/** view -Altibase */
	public static final String  TMP_CREATE_VIEW_STMT = 
			                     "CREATE OR REPLACE VIEW view_name AS " + PublicTadpoleDefine.LINE_SEPARATOR + 
									"	SELECT column FROM table_name;";

	/** alter view template */
	public static final String TMP_ALTER_VIEW_STMT = "";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = 
			                     "CREATE INDEX  index_name "+ PublicTadpoleDefine.LINE_SEPARATOR +  
									"	ON table_name ( column_1, column_2 );";
	
	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = 
			                     "CREATE OR REPLACE PROCEDURE procedure_name (param1 OUT NUMBER) IS" + PublicTadpoleDefine.LINE_SEPARATOR +
									"  BEGIN " + PublicTadpoleDefine.LINE_SEPARATOR +
									"    SELECT COUNT(*) INTO param1 FROM table_name; " + PublicTadpoleDefine.LINE_SEPARATOR +
									"  END;";
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = 
			                    "CREATE OR REPLACE FUNCTION function_name  " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "( " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "  param_1 IN INTEGER " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    ") " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "RETURN INTEGER " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "AS " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "  V1 NUMBER; " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "BEGIN " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "  v1 := param_1; " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "  SYSTEM_.PRINTLN('V1 = [' || V1 || ']'); " + PublicTadpoleDefine.LINE_SEPARATOR + 
			                    "  RETURN 1; " + PublicTadpoleDefine.LINE_SEPARATOR + 
								   "END;";
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = 
			                    "CREATE TRIGGER  trigger_name " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "    AFTER INSERT ON table_name " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "    REFERENCING NEW ROW NEW_ROW " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "    FOR EACH ROW " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "AS " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "    RTN INTEGER; " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "    BEGIN " + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "    RTN := SENDMSG('127.0.0.1', 12345, 'insert into table_name : '||NEW_ROW.column_1||','||NEW_ROW.column_2, 1);" + PublicTadpoleDefine.LINE_SEPARATOR +
			                    "END;";
			                    

}
