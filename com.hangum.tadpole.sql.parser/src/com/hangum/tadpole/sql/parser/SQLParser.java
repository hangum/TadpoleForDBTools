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
package com.hangum.tadpole.sql.parser;

import java.util.HashMap;
import java.util.Map;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.QUERY_TYPE;

/**
 * SQL parser
 * 
 * @author hangum
 *
 */
public class SQLParser {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLParser.class);
	

	public static SQLParserVO sqlParser(String strSql) {
		SQLParserVO sqlParserVO = new SQLParserVO();
		PublicTadpoleDefine.QUERY_TYPE queryType = PublicTadpoleDefine.QUERY_TYPE.UNKNOWN;
		
		try {
			Statement statement = CCJSqlParserUtil.parse(strSql);
			if(statement instanceof Select) {
				queryType = PublicTadpoleDefine.QUERY_TYPE.SELECT;
				
				Select select = (Select)statement;
				
				
//				logger.debug( "table name is " + ((Select) statement).getTable().getName() );
				
			} else if(statement instanceof Insert) {
				queryType = PublicTadpoleDefine.QUERY_TYPE.INSERT;
			} else if(statement instanceof Update) {
				queryType = PublicTadpoleDefine.QUERY_TYPE.UPDATE;
			} else if(statement instanceof Delete) {
				queryType = PublicTadpoleDefine.QUERY_TYPE.DELETE;
			} else {
				queryType = PublicTadpoleDefine.QUERY_TYPE.DDL;
			}
			
		} catch (Throwable e) {
			logger.error(String.format("sql parse exception. [ %s ]", strSql),  e);
			queryType = PublicTadpoleDefine.QUERY_TYPE.UNKNOWN;
		}
		
		return sqlParserVO;
	}
}
