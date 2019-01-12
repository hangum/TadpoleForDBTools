/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.parser;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.sql.parser.ddl.ParserDDL;
import com.hangum.tadpole.engine.sql.parser.define.ParserDefine;
import com.hangum.tadpole.engine.sql.parser.dto.QueryInfoDTO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.SQL_TYPE;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 
 * @author hangum
 *
 */
public class BasicTDBSQLParser implements TDBSQLParser {
	private static final Logger logger = Logger.getLogger(BasicTDBSQLParser.class);
	
	/** base statement pattern */
	protected static final String REGEXP_STATEMENT = "^SELECT.*|^EXPLAIN.*|^SHOW.*|^DESCRIBE.*|^DESC.*|^CHECK.*|^PRAGMA.*|^WITH.*|^OPTIMIZE.*|^DECLARE.*|^FETCH.*";
	
	private static final String MSSQL_PATTERN_STATEMENT = "|^SP_HELP.*|^EXEC.*";
	private static final String ORACLE_PATTERN_STATEMENT = "";
	private static final String MYSQL_PATTERN_STATEMENT = "|^CALL.*|^SET.*";
	private static final String PGSQL_PATTERN_STATEMENT = "";
	private static final String SQLITE_PATTERN_STATEMENT = "";
	private static final String CUBRID_PATTERN_STATEMENT = "";

	private static final Pattern PATTERN_DML_BASIC = Pattern.compile(REGEXP_STATEMENT
			+ MSSQL_PATTERN_STATEMENT
			+ ORACLE_PATTERN_STATEMENT
			+ MYSQL_PATTERN_STATEMENT
			+ PGSQL_PATTERN_STATEMENT
			+ SQLITE_PATTERN_STATEMENT
			+ CUBRID_PATTERN_STATEMENT
			, ParserDefine.PATTERN_FLAG
	);
	
	@Override
	public QueryInfoDTO parser(String sql) {
		String strCheckSQL = SQLUtil.makeSQLTestString(sql);
		Statement jsqlStmt = parseSQL(strCheckSQL);
		
		QueryInfoDTO queryInfoDTO = new QueryInfoDTO();
		if(PATTERN_DML_BASIC.matcher(strCheckSQL).matches()) {
			queryInfoDTO.setStatement(true);
			queryInfoDTO.setSqlType(SQL_TYPE.DML);
			
			queryInfoDTO.setQueryType(parseDML(jsqlStmt));
		} else {
			queryInfoDTO.setStatement(false);
			QUERY_DML_TYPE dmlType = parseDML(jsqlStmt);
			if(dmlType == QUERY_DML_TYPE.INSERT | dmlType == QUERY_DML_TYPE.UPDATE | dmlType == QUERY_DML_TYPE.DELETE ) {
				queryInfoDTO.setSqlType(SQL_TYPE.DML);	
			} else {
				queryInfoDTO.setSqlType(SQL_TYPE.DDL);
			}
			queryInfoDTO.setQueryType(dmlType);
			
			parseDDL(sql, queryInfoDTO);
		}

		return queryInfoDTO;
	}
	
	/**
	 * paser dml
	 * 
	 * @param sql
	 */
	protected Statement parseSQL(String sql) {
		Statement statement = null;
		try {
			statement = CCJSqlParserUtil.parse(sql);
			
		} catch (Throwable e) {
			logger.error(e);
			logger.error(String.format("sql parse exception. [ %s ]", sql));
		}
		
		return statement;
	}

	/**
	 * parse DDL
	 * 
	 * @param sql
	 * @param queryInfoDTO
	 */
	protected void parseDDL(String sql, QueryInfoDTO queryInfoDTO) {
		ParserDDL parseDDL = new ParserDDL();
		parseDDL.parseQuery(sql, queryInfoDTO);
	}
	
	/**
	 * paser dml
	 * 
	 * @param jsqlStmt
	 */
	protected QUERY_DML_TYPE parseDML(Statement jsqlStmt) {
		PublicTadpoleDefine.QUERY_DML_TYPE queryType = PublicTadpoleDefine.QUERY_DML_TYPE.UNKNOWN;
		
		if(jsqlStmt != null) {
			if(jsqlStmt instanceof Select) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.SELECT;
			} else if(jsqlStmt instanceof Insert) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.INSERT;
			} else if(jsqlStmt instanceof Update) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.UPDATE;
			} else if(jsqlStmt instanceof Delete) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.DELETE;
			}
		// jsql statement 문이 null이 아닐경우 
		} else {
			queryType = PublicTadpoleDefine.QUERY_DML_TYPE.SELECT;
		}

		return queryType;
	}
}
