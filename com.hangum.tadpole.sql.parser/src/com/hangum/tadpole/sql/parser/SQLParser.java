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

import kry.sql.format.ISqlFormatRule;
import kry.sql.format.SqlFormatRule;

import org.apache.log4j.Logger;

import zigen.sql.parser.ASTVisitorToString;
import zigen.sql.parser.INode;
import zigen.sql.parser.ISqlParser;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTRoot;

import com.hangum.tadpole.sql.rule.SQLFormatRule;

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
	
	/**
	 * sql parser
	 * 
	 * @param sql
	 * @return
	 */
	public static INode parserSql(String sql) {
		INode node = new ASTRoot();
		ISqlParser parser = null;
		
		try {
			parser = new SqlParser(sql, SQLFormatRule.getSqlFormatRule());
			parser.parse(node);
			
		} catch(Exception e) {
			logger.error("SQL Parser exeception", e);
		} finally {
			if(parser != null) parser = null;
		}
		
		return node;
	}
//	
//	public static SqlFormatRule getSqlFormatRule() {
//		SqlFormatRule rule = new SqlFormatRule();
//		rule.setRemoveEmptyLine(true);
//
//		rule.setOutSqlSeparator(SqlFormatRule.SQL_SEPARATOR_SEMICOLON);
//		rule.setRemoveEmptyLine(true);
//		rule.setIndentEmptyLine(true);
//		rule.setConvertName(ISqlFormatRule.CONVERT_STRING_NONE);
//		rule.setConvertKeyword(ISqlFormatRule.CONVERT_STRING_NONE);
//		rule.setNewLineBeforeAndOr(false);
//		rule.setNewLineBeforeComma(false);
//		
//		rule.setWordBreak(false);
//		
//		return rule;
//	}
//	
//	public static void main(String[] args) throws Exception {
//		INode node = new ASTRoot();
//		ISqlParser parser = new SqlParser("SELECT * FROM sample_table a WHERE a.id = ?", getSqlFormatRule());
//		parser.parse(node);
//		
//		ASTVisitorToString visitor = new ASTVisitorToString();
//		node.accept(visitor, null);
//		visitor.print();
//		
//		System.out.println("[name]" +  node.getName() );
//		
//		System.out.println( node.getChildrenSize() );
//		System.out.println( node.getASTSelectStatement().getName() );
//		
//		
////		System.out.println("[Aliase ]" + node.getASTAlias() );
////		System.out.println( node.getName() );
//		
//		
//		
//	}
}
