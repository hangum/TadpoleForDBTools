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
 * Oracle DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class OracleDMLTemplate extends MySQLDMLTemplate {
	
	/* 참조 : http://devhome.tistory.com/22 */
	public static final String TMP_GET_PARTDATA = "SELECT * FROM (	" +
												 "    SELECT tdb_a.*, ROWNUM AS tdb_rnum, COUNT(*) OVER() AS tdb_totcnt  " +
												 "    FROM ( %s ) tdb_a " +
												 " 	) WHERE tdb_rnum > %s AND tdb_rnum <= %s";
	
	/** table - oracle */
	public static final String TMP_CREATE_TABLE_STMT = "CREATE TABLE sample_table ( " + PublicTadpoleDefine.LINE_SEPARATOR + 
						 "	id number primary key, " + PublicTadpoleDefine.LINE_SEPARATOR +
						 "	name varchar2(30) " + PublicTadpoleDefine.LINE_SEPARATOR +
						");";

	/** constraints  */
	public static final String  TMP_CREATE_CONSTRAINTS_STMT = "ALTER TABLE table_name "+ PublicTadpoleDefine.LINE_SEPARATOR
			+ " ADD CONSTRAINT constraint_name UNIQUE (column1, column2, ... column_n); "+ PublicTadpoleDefine.LINE_SEPARATOR;
	
	// plan_table	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN PLAN set statement_id = '" + PublicTadpoleDefine.STATEMENT_ID + "' INTO " + PublicTadpoleDefine.DELIMITER + " FOR ";

	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE OR REPLACE PROCEDURE simpleproc2 (param1 out INT) " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "IS " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "BEGIN " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "	SELECT COUNT(*) INTO param1 FROM t; " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "END;";
	/** package */
	public static final String  TMP_CREATE_PACKAGE_STMT = "CREATE OR REPLACE PACKAGE PKG_SAMPLE " + PublicTadpoleDefine.LINE_SEPARATOR +
														 " AS " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " FUNCTION F_TEST RETURN VARCHAR2;  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " PROCEDURE P_TEST (param1 IN VARCHAR2);  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " END;" + PublicTadpoleDefine.LINE_SEPARATOR +
														  " /" + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR +
														  "CREATE or replace PACKAGE BODY PKG_SAMPLE " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " AS " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " FUNCTION F_TEST RETURN VARCHAR2  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " IS  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " BEGIN  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "     return to_char(sysdate, 'yyyymmdd');  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " end;  " + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR +
														  " PROCEDURE P_TEST (param1 IN VARCHAR2)  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " IS  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  " BEGIN  " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "		dbms_output.put_line(param1); " + PublicTadpoleDefine.LINE_SEPARATOR +
														  "END;";
}
