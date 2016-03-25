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
 * Define TAJO DML
 * 
 * ref) http://tajo.incubator.apache.org/tajo-0.2.0-doc.html#GettingStarted
 * 
 * @author hangum
 *
 */
public class TAJODMLTemplate extends AbstractDMLTemplate {
	
	/**  
	 * preference에서 정의한 쿼리가 초과 되었을때 sub query를 수행합니다.
	 */	
	public static final String TMP_GET_PARTDATA = " %s limit %s,%s";
	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN GLOBAL ";
	
	/** table - tajo */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE EXTERNAL TABLE table1 ( "  + PublicTadpoleDefine.LINE_SEPARATOR +
														"	id INT, " + PublicTadpoleDefine.LINE_SEPARATOR +
														"	name TEXT, " + PublicTadpoleDefine.LINE_SEPARATOR +
														"	score FLOAT, " + PublicTadpoleDefine.LINE_SEPARATOR +
														"	type TEXT " + PublicTadpoleDefine.LINE_SEPARATOR +
														") USING CSV WITH ('csvfile.delimiter'='|') LOCATION 'file:/home/x/table1';";
}
