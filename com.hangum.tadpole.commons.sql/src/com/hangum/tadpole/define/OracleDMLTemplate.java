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
 * Oracle DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class OracleDMLTemplate extends MySQLDMLTemplate {
	public static final String TMP_GET_PARTDATA = "select * from (%s) where ROWNUM > %s and ROWNUM <= %s";

	// plan_table	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN PLAN INTO %s FOR ";

	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE or replace PROCEDURE simpleproc2 (param1 out INT) " + PublicTadpoleDefine.LINE_SEPARATOR +
																	 " is " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " BEGIN  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  "  SELECT COUNT(*) INTO param1 FROM t;  " + PublicTadpoleDefine.LINE_SEPARATOR +
																	  " END;";
}
