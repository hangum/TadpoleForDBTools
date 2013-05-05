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
	
	public static void main(String[] args) {
		INode node = SQLParser.parserSql("SELECT * FROM sample_table a WHERE a.id = ?");
		
		ASTVisitorToString visitor = new ASTVisitorToString();
		node.accept(visitor, null);
		visitor.print();
		
		System.out.println( node.getASTAlias() );
		System.out.println(node.getASTSelectStatement().getASTAlias());
		
	}
}
