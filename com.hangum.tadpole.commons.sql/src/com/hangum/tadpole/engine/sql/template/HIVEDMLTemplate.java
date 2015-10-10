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
 * Define HIVE DML
 * 
 * ref) https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DDL
 * 
 * @author hangum
 *
 */
public class HIVEDMLTemplate extends AbstractDMLTemplate {
	
	/**  
	 * preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = " %s limit %s,%s";
	
	public static final String TMP_EXPLAIN_EXTENDED = "explain ";
	
	/** table - mysql */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE sample_table ( " + PublicTadpoleDefine.LINE_SEPARATOR + 
						 "	id int,   " + PublicTadpoleDefine.LINE_SEPARATOR +
						 "	name string  " + PublicTadpoleDefine.LINE_SEPARATOR +
						");";
	
	/** view */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW view_name AS " + PublicTadpoleDefine.LINE_SEPARATOR + 
													"	SELECT columns FROM table_name;";

	/** alter view template */
	public static final String TMP_ALTER_VIEW_STMT = "ALTER ALGORITHM=UNDEFINED DEFINER='%s'@'%' " + PublicTadpoleDefine.LINE_SEPARATOR +
													"	SQL SECURITY DEFINER VIEW '%s' AS " + PublicTadpoleDefine.LINE_SEPARATOR;
	
}
