/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.template;

/**
 * redshift DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class RedShiftDMLTemplate extends PostgreDMLTemplate {

	/** plan_table */	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN ";

}
