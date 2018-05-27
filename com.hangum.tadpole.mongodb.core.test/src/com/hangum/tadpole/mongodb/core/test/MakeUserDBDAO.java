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
package com.hangum.tadpole.mongodb.core.test;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

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
		
		
		userDB.setDbms_type(DBDefine.MONGODB_DEFAULT.getDBToString());
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
