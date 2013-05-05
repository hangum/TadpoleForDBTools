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
package com.hangum.tadpole.util.tables;

import java.sql.Types;

/**
 * sqltype tuils
 * 
 * @author hangum
 *
 */
public class SQLTypeUtils {

	/**
	 * 숫자 컬럼인지
	 * 
	 * @param sqlType
	 * @return
	 */
	public static boolean isNumberType(int sqlType) {
		switch(sqlType) {
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.SMALLINT:
		case Types.TINYINT:
			return true;
		}
		
		return false;
	}

}
