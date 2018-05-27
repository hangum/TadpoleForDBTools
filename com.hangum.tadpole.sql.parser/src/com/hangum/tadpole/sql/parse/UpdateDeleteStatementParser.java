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
package com.hangum.tadpole.sql.parse;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.dao.SQLStatementStruct;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * update delete statment parse
 * 
 * @author hangum
 *
 */
public class UpdateDeleteStatementParser {
	private static final Logger logger = Logger.getLogger(UpdateDeleteStatementParser.class);
	
	/**
	 * SQL statement parser
	 * 
	 * @param userDB
	 * @param strSQL
	 * @return
	 * @throws Exception
	 */
	public static SQLStatementStruct getParse(UserDBDAO userDB, String strSQL) throws Exception {
		SQLStatementStruct sqlStatementStruce = new SQLStatementStruct();
	
		try {
			Statement statement = CCJSqlParserUtil.parse(strSQL);
			if(statement instanceof Select) {
				throw new Exception("Please update or delete statement.");
			} else if(statement instanceof Update) {
				Update updateStmt = (Update) statement;
				sqlStatementStruce.setObjectName(updateStmt.getTables().get(0).getFullyQualifiedName());
				
				if(updateStmt.getWhere() == null) sqlStatementStruce.setWhere("");
				else sqlStatementStruce.setWhere(updateStmt.getWhere().toString());
			} else if(statement instanceof Delete) {
				Delete deleteStmt = (Delete) statement;
				sqlStatementStruce.setObjectName(deleteStmt.getTable().getFullyQualifiedName());
				
				if(deleteStmt.getWhere() == null) sqlStatementStruce.setWhere("");
				else sqlStatementStruce.setWhere(deleteStmt.getWhere().toString());
			}
			
		} catch(Exception e) {
			logger.error("update delete parser", e);
			throw new Exception("SQL parse exception. " + e.getMessage());
		}
		
		return sqlStatementStruce;
	}
}
