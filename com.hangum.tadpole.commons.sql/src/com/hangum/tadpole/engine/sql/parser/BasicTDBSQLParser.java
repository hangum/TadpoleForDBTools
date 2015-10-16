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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 
 * @author hangum
 *
 */
public class BasicTDBSQLParser implements TDBSQLParser {
	/** REGEXP pattern flag */
	protected final static int PATTERN_FLAG = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;
	
	/** base statement pattern */
	protected static final String REGEXP_STATEMENT = "^SELECT.*|^EXPLAIN.*|^SHOW.*|^DESCRIBE.*|^DESC.*|^CHECK.*|^PRAGMA.*|^WITH.*|^OPTIMIZE.*";
	
	/** table statement pattern */
	protected static final String REGEXP_CREATE_TABLE = "CREATE\\s+TABLE\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_ALTER_TABLE = "ALTER\\s+TABLE\\s+([A-Z0-9_\\.\"]+)";
	// -- oracle protected static final String REGEXP_INSERT_INTO = "SELECT\\s+INTO\\s+([A-Z0-9_\\.\"]+)";
	
	/** view statement pattern */
	protected static final String REGEXP_CREATE_VIEW = "CREATE\\s+VIEW\\s+([A-Z0-9_\\.\"]+)";
	// -- oracle protected static final String REGEXP_CREATE_MATERIALIZED_VIEW = "CREATE\\s+MATERIALIZED\\s+VIEW\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_CREATE_OR_REPLACE_VIEW = "CREATE\\s+OR\\s+REPLACE\\s+VIEW\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_ALTER_VIEW = "ALTER\\s+VIEW\\s+([A-Z0-9_\\.\"]+)";
	
	/** procedure statement pattern */	
	protected static final String REGEXP_CREATE_PROCEDURE = "CREATE\\s+PROCEDURE\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_CREATE_OR_REPLACE_PROCEDURE = "CREATE\\s+OR\\s+REPLACE\\s+PROCEDURE\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_ALTER_PROCEDURE = "ALTER\\s+PROCEDURE\\s+([A-Z0-9_\\.\"]+)";
	
	/** function statement pattern */
	protected static final String REGEXP_CREATE_FUNCTION = "CREATE\\s+FUNCTION\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_CREATE_OR_REPLACE_FUNCTION = "CREATE\\s+OR\\s+REPLACE\\s+FUNCTION\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_ALTER_FUNCTION = "ALTER\\s+FUNCTION\\s+([A-Z0-9_\\.\"]+)";
	
	/** drop statement pattern */
	protected static final String REGEXP_DROP_TABLE = "DROP\\s+TABLE\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_DROP_VIEW = "DROP\\s+VIEW\\s+([A-Z0-9_\\.\"]+)";
	// -- oracle protected static final String REGEXP_DROP_MATERIALIZED_VIEW = "DROP\\s+MATERIALIZED\\s+VIEW\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_DROP_PROCEDURE = "DROP\\s+PROCEDURE\\s+([A-Z0-9_\\.\"]+)";
	protected static final String REGEXP_DROP_FUNCTION = "DROP\\s+FUNCTION\\s+([A-Z0-9_\\.\"]+)";


	@Override
	public QueryInfoDTO parser(UserDBDAO userDB, String sql) {
		QueryInfoDTO queryInfoDTO = new QueryInfoDTO();
		
		// if isStatement
		Pattern PATTERN_DML_BASIC = Pattern.compile(REGEXP_STATEMENT, PATTERN_FLAG);
		if(PATTERN_DML_BASIC.matcher(sql).matches()) {
			queryInfoDTO.setStatement(true);
			queryInfoDTO.setSqlType(SQL_TYPE.DML);
		} else {
			queryInfoDTO.setStatement(false);
			queryInfoDTO.setSqlType(SQL_TYPE.DDL);
		}
		
		/** query type */
		
		/** query ddl type */

		return queryInfoDTO;
	}

}
