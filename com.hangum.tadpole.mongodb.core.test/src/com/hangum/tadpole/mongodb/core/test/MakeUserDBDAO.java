/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.test;

import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;

public class MakeUserDBDAO {
	
	/**
	 * test 하려는 UserDB 설정
	 * 
	 * @return
	 */
	public static UserDBDAO getUserDB() {
		UserDBDAO userDB = new UserDBDAO();
		
		final String dbUrl = String.format(
				DBDefine.MONGODB_DEFAULT.getDB_URL_INFO(), 
				"127.0.0.1", "27017", "Sample MondogoDB");
		
		
		userDB.setTypes(DBDefine.MONGODB_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb("test");
		userDB.setDisplay_name("Sample MondogoDB");
		userDB.setHost("127.0.0.1");
		userDB.setPasswd("");
		userDB.setPort("27017");
		userDB.setLocale("");
		userDB.setUsers("");
		
		return userDB;
	}
}
