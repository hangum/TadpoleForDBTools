/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution", " and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.db.metadata.constants;

import java.util.Arrays;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 *
 * Define SQL constants
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 21.
 *
 */
public abstract class SQLConstants {
	public static final String[] DEFAULT_TABLE_KEYWORDS = {"FROM", "UPDATE", "INTO", "TABLE", "JOIN"};
	public static final String[] DEFAULT_COLUMN_KEYWORDS = {"SELECT", "WHERE", "SET", "ON", "AND", "OR", "BY", "HAVING"};
	public static final List<String> listTableKeywords = Arrays.asList(SQLConstants.DEFAULT_TABLE_KEYWORDS);
	public static final List<String> listColumnKeywords = Arrays.asList(SQLConstants.DEFAULT_COLUMN_KEYWORDS);
	
	UserDBDAO userDB;
	public SQLConstants(UserDBDAO userDB) {
		this.userDB = userDB;
	}
	
	public String ext() {
		return userDB.getDBDefine().getExt();
	}
	public abstract String keyword();
	public abstract String function();
	public abstract String constant();
	public abstract String variable();
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}
}