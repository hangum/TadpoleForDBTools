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
package com.hangum.tadpole.define;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * Define HIVE DML
 * 
 * @author hangum
 *
 */
public class HIVEDMLTemplate {
	
	/**  
	 * mysql 을 수행할때 preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = " %s limit %s,%s";
	
	public static final String TMP_EXPLAIN_EXTENDED = "explain  ";
	
	/** table - mysql */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE   sample_table   (  " + PublicTadpoleDefine.LINE_SEPARATOR + 
						 " id int,   " + PublicTadpoleDefine.LINE_SEPARATOR +
						 " dtDontQuery string, " + PublicTadpoleDefine.LINE_SEPARATOR +
						 " name string  " + PublicTadpoleDefine.LINE_SEPARATOR +
						" );";
	
	/** view */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW   view_name   AS  " + PublicTadpoleDefine.LINE_SEPARATOR + 
																" SELECT   columns   FROM   table;";

	/** alter view template */
	public static final String TMP_ALTER_VIEW_STMT = "ALTER ALGORITHM=UNDEFINED DEFINER='%s'@'%' " + PublicTadpoleDefine.LINE_SEPARATOR +
													" SQL SECURITY DEFINER VIEW '%s' AS " + PublicTadpoleDefine.LINE_SEPARATOR +
													"";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE INDEX  index_name \r\n  ON table_name ( columns );";
	
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION hello (s CHAR(20)) RETURNS CHAR(50)  " + PublicTadpoleDefine.LINE_SEPARATOR + 
																	" RETURN CONCAT('Hello, 's');";
	
	
}
