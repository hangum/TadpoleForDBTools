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
package com.hangum.tadpole.commons.sql.util;

import java.util.HashMap;
import java.util.Map;

/**
 * RDB type to Java type mapping utils
 * 
 * @author hangum
 * 
 */
public class RDBTypeToJavaTypeUtils {

	
	private static Map<String, Integer> mapTypes = new HashMap<String, Integer>();
	static {
		mapTypes.put("BIT", 		java.sql.Types.BIT);
		mapTypes.put("TINYINT", 	java.sql.Types.TINYINT);
		mapTypes.put("SMALLINT", 	java.sql.Types.SMALLINT);
		mapTypes.put("MEDIUMINT", 	java.sql.Types.INTEGER);
		mapTypes.put("INT", 		java.sql.Types.INTEGER);
		mapTypes.put("INTEGER", 	java.sql.Types.INTEGER);
		mapTypes.put("INT24", 		java.sql.Types.INTEGER);
		mapTypes.put("BIGINT", 		java.sql.Types.BIGINT);
		mapTypes.put("REAL", 		java.sql.Types.DOUBLE);
		mapTypes.put("FLOAT", 		java.sql.Types.FLOAT);
		mapTypes.put("DECIMAL", 	java.sql.Types.DECIMAL);
		mapTypes.put("NUMERIC", 	java.sql.Types.DECIMAL);
		mapTypes.put("DOUBLE", 		java.sql.Types.DOUBLE);
		mapTypes.put("CHAR", 		java.sql.Types.CHAR);
		mapTypes.put("VARCHAR", 	java.sql.Types.VARCHAR);
		mapTypes.put("DATE", 		java.sql.Types.DATE);
		mapTypes.put("TIME", 		java.sql.Types.TIME);
		mapTypes.put("YEAR", 		java.sql.Types.DATE);
		mapTypes.put("TIMESTAMP", 	java.sql.Types.TIMESTAMP);
		mapTypes.put("DATETIME", 	java.sql.Types.TIMESTAMP);
		mapTypes.put("TINYBLOB", 	java.sql.Types.BINARY);
		mapTypes.put("BLOB", 		java.sql.Types.LONGVARBINARY);
		mapTypes.put("MEDIUMBLOB", 	java.sql.Types.LONGVARBINARY);
		mapTypes.put("LONGBLOB", 	java.sql.Types.LONGVARBINARY);
		mapTypes.put("TINYTEXT", 	java.sql.Types.VARCHAR);
		mapTypes.put("TEXT", 		java.sql.Types.LONGVARCHAR);
		mapTypes.put("MEDIUMTEXT", 	java.sql.Types.LONGVARCHAR);
		mapTypes.put("LONGTEXT", 	java.sql.Types.LONGVARCHAR);
		mapTypes.put("GEOMETRY", 	java.sql.Types.BINARY);
		mapTypes.put("BINARY", 		java.sql.Types.BINARY);
		mapTypes.put("VARBINARY", 	java.sql.Types.VARBINARY);
	}
	
	/**
	 * rdb type to java 
	 * 
	 * @param rdbType
	 * @return
	 */
	public static Integer getJavaType(String rdbType) {
		Integer javaType = mapTypes.get(rdbType);
		if(javaType == null) {
			return java.sql.Types.VARCHAR;
		}
	
		return javaType;
	}

}
