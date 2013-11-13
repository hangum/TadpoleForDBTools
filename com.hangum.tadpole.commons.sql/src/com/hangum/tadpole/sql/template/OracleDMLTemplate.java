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
package com.hangum.tadpole.sql.template;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * Oracle DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class OracleDMLTemplate extends MySQLDMLTemplate {
	public static final String TMP_GET_PARTDATA = "select * from (%s) where ROWNUM > %s and ROWNUM <= %s";

	// plan_table	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN PLAN INTO " + PublicTadpoleDefine.DELIMITER + " FOR ";

	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE or replace PROCEDURE simpleproc2 (param1 out INT) " + PublicTadpoleDefine.LINE_SEPARATOR +
																	 " is " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " BEGIN  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  "  SELECT COUNT(*) INTO param1 FROM t;  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " END;";
	/** package */
	public static final String  TMP_CREATE_PACKAGE_STMT = "CREATE or replace PACKAGE PKG_SAMPLE " + PublicTadpoleDefine.LINE_SEPARATOR +
																	 " as " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " FUNCTION F_TEST RETURN VARCHAR2;  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " PROCEDURE P_TEST (param1 IN VARCHAR2);  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " END;" + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " /" + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR +
																	  "CREATE or replace PACKAGE BODY PKG_SAMPLE " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " as " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " FUNCTION F_TEST RETURN VARCHAR2  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " is  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " begin  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  "     return to_char(sysdate, 'yyyymmdd');  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " end;  " + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " PROCEDURE P_TEST (param1 IN VARCHAR2)  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " is  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " begin  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  "     dbms_output.put_line(param1); " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " end;  " + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " /" + PublicTadpoleDefine.LINE_SEPARATOR ;
}
