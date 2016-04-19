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
 * MS_SQL 디비의 쿼리를 정의 합니다.
 * 
 * @author hangum
 *
 */
public class MSSQLDMLTemplate extends AbstractDMLTemplate {
	/**  
	 * mssql 을 수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 * 
	 * http://elenoa.tistory.com/384
	 */	
	public static final String TMP_GET_PARTDATA = "SELECT * FROM ( %s ) WHERE ROWNUM > %s AND ROWNUM <= %s ORDER BY ROWNUM";
	
	
	public static final String TMP_EXPLAIN_EXTENDED = "SET SHOWPLAN_TEXT ";
	
	/** table  */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE sample_table ( " + PublicTadpoleDefine.LINE_SEPARATOR +  
						 "	id INT  NOT NULL,  " + PublicTadpoleDefine.LINE_SEPARATOR + 
						 "	name char(60) default NULL, " + PublicTadpoleDefine.LINE_SEPARATOR + 
						 "	PRIMARY KEY (id) " + PublicTadpoleDefine.LINE_SEPARATOR + 
						");";

	/** view  */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW view_name AS " + PublicTadpoleDefine.LINE_SEPARATOR +  
														"	SELECT columns FROM table;";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE INDEX  index_name " + PublicTadpoleDefine.LINE_SEPARATOR +
													"	ON table_name ( columns );";
	
	/** constratints */
	public static final String  TMP_CREATE_CONSTRAINTS_STMT = "ALTER TABLE schema.sample_table  " + PublicTadpoleDefine.LINE_SEPARATOR 
													        + "	ADD CONSTRAINT constraints_name UNIQUE (column_name1, ... column_name); " + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR 
													        + "ALTER TABLE dbo.DocExc   " + PublicTadpoleDefine.LINE_SEPARATOR 
													        + "   ADD ColumnD int NULL  " + PublicTadpoleDefine.LINE_SEPARATOR 
													        + "   CONSTRAINT CHK_ColumnD_DocExc   " + PublicTadpoleDefine.LINE_SEPARATOR 
													        + "   CHECK (ColumnD > 10 AND ColumnD < 50);   " + PublicTadpoleDefine.LINE_SEPARATOR ;
	
	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE simpleproc " + PublicTadpoleDefine.LINE_SEPARATOR + 
														  "	AS " + PublicTadpoleDefine.LINE_SEPARATOR + 
														  "		SELECT COUNT(*) INTO param1 FROM t;";
	
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION fc_sample_table5 (@v_name VARCHAR ) " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"RETURNS TABLE	" + PublicTadpoleDefine.LINE_SEPARATOR + 
														"AS	" + PublicTadpoleDefine.LINE_SEPARATOR + 
														"RETURN (	" + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	SELECT  *	" + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	FROM  sample_table" + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	WHERE name = @v_name" + PublicTadpoleDefine.LINE_SEPARATOR + 
														");";
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER reminder1 " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"ON sample_table " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	AFTER INSERT, UPDATE  " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	AS RAISERROR ('Notify Customer Relations', 16, 10); ";
}
