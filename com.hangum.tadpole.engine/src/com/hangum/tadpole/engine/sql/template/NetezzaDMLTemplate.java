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

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * postgre DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class NetezzaDMLTemplate extends PostgreDMLTemplate {

	/** table */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE emp ( " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	code        CHARACTER(5) CONSTRAINT firstkey PRIMARY KEY, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	title       CHARACTER VARYING(40) NOT NULL, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	did         DECIMAL(3) NOT NULL, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	date_prod   DATE, " + PublicTadpoleDefine.LINE_SEPARATOR + 
														"	kind        CHAR(10), " + PublicTadpoleDefine.LINE_SEPARATOR +
														"	len         INTERVAL HOUR TO MINUTE " + PublicTadpoleDefine.LINE_SEPARATOR +
														");";
	
	/** view  */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW empComedies AS " + PublicTadpoleDefine.LINE_SEPARATOR + 
													   "	SELECT * " + PublicTadpoleDefine.LINE_SEPARATOR + 
													   "	FROM emp " + PublicTadpoleDefine.LINE_SEPARATOR + 
													   "	WHERE empname = 'Comedy';";
	
}
