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
