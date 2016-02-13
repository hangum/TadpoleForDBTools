/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.db.metadata.constants;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * SQLite Constants
 * 
 * @author hangum
 *
 */
public class SQLiteConstants extends SQLConstants {

	public SQLiteConstants(UserDBDAO userDB) {
		super(userDB);
	}

	@Override
	public String keyword() {
		return "abort|abs|action|add|after|all|alter|analyze|and|as|asc|attach|autoincrement|avg|before|begin|between|by|cascade|case|cast|check|collate|column|count|commit|conflict|constraint|create|cross|current_date|current_time|current_timestamp|database|default|deferrable|deferred|delete|desc|detach|distinct|drop|each|else|end|escape|except|exclusive|exists|explain|fail|for|foreign|from|full|glob|group|having|if|ignore|immediate|in|index|indexed|initially|inner|insert|instead|intersect|into|is|isnull|join|key|left|like|limit|lower|match|max|min|natural|no|not|notnull|null|of|offset|on|or|order|outer|plan|pragma|primary|query|raise|random|recursive|references|regexp|reindex|release|rename|replace|restrict|right|rollback|row|savepoint|select|set|sqlite_version|sum|table|temp|temporary|then|to|transaction|trigger|upper|union|unique|update|using|vacuum|values|view|virtual|when|where|with|without";
	}

	@Override
	public String function() {
		return "typeof|int|integer|tinyint|smallint|mediumint|bigint|unsigned|big|int|int2|int8|character|varchar|varying|character|nchar|native||nvarchar|text|clob|blob|real|double|precision|float|numberic|decimal|boolean|date|datetime";
	}

	@Override
	public String constant() {
		return "false|true|null";
	}

	@Override
	public String variable() {
		return "charset|clear|connect|edit|ego|exit|go|help|nopager|notee|nowarning|pager|print|prompt|quit|rehash|source|status|system|tee";
	}

}
