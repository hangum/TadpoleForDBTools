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
package com.hangum.tadpole.sql.format;

import kry.sql.format.ISqlFormat;
import kry.sql.format.SqlFormat;

import org.apache.log4j.Logger;

import com.hangum.tadpole.sql.rule.SQLFormatRule;


/**
 * SQL을 포메팅합니다.
 * 
 * @author hangum
 *
 */
public class SQLFormater {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLFormater.class);
	
	/**
	 * sql formatting 합니다.
	 * 
	 * @param dbType
	 * @param strOriginalSQL
	 * @return
	 * @throws Exception
	 */
	public static String format(String strOriginalSQL) throws Exception {
		
		try {
			ISqlFormat formatter = new SqlFormat(SQLFormatRule.getSqlFormatRule());
			return formatter.format(strOriginalSQL, 0);
		} catch (Exception e) {
			logger.error("sql format exception", e);
			
			return strOriginalSQL;
		}
	}
}
