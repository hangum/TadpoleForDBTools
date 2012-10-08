/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.define;

/**
 * Oracle DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class OracleDMLTemplate extends MySQLDMLTemplate {
	public static final String TMP_GET_PARTDATA = "%s where ROWNUM > %s and ROWNUM <= %s";

	// plan_table	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN PLAN INTO %s FOR ";

	
}
