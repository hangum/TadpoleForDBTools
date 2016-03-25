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
 * postgre DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class PostgreDMLTemplate extends MySQLDMLTemplate {
	public static final String TMP_GET_PARTDATA = "SELECT tdb_a.* FROM (%s) tdb_a OFFSET %s LIMIT %s";

	/** plan_table */	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN (ANALYZE on, VERBOSE on, COSTS on, BUFFERS on, TIMING on) ";
	
	/** plan_table */	
	public static final String TMP_EXPLAIN_EXTENDED_JSON = "EXPLAIN (ANALYZE on, VERBOSE on, COSTS on, BUFFERS on, TIMING on, FORMAT JSON) ";

	/** table */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE emp ( " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	empname text, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	salary integer, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	last_date timestamp, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	last_user text " + PublicTadpoleDefine.LINE_SEPARATOR + 
														");";
	
	/** view  */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW empComedies AS " + PublicTadpoleDefine.LINE_SEPARATOR + 
													   "	SELECT * " + PublicTadpoleDefine.LINE_SEPARATOR + 
													   "	FROM emp " + PublicTadpoleDefine.LINE_SEPARATOR + 
													   "	WHERE empname = 'Comedy';";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE UNIQUE INDEX empname_idx ON emp (empname);";
	
	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE simpleproc (OUT param1 INT) " + PublicTadpoleDefine.LINE_SEPARATOR + 
														  "BEGIN " + PublicTadpoleDefine.LINE_SEPARATOR + 
														  "	SELECT COUNT(*) INTO param1 FROM t; " + PublicTadpoleDefine.LINE_SEPARATOR + 
														  "END;";
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE OR REPLACE FUNCTION increment(i INT) RETURNS INT AS $$ " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "BEGIN " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "	RETURN i + 1; " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "END; " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "$$ LANGUAGE plpgsql; ";
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER emp_stamp BEFORE INSERT OR UPDATE ON films " + PublicTadpoleDefine.LINE_SEPARATOR + 
														" FOR EACH ROW EXECUTE PROCEDURE emp_stamp();";
}
