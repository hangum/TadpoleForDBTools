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

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * SQLConstant factory
 * 
 * @author hangum
 *
 */
public class SQLConstantFactory {
	/**
	 * 
	 */
	public SQLConstants getDB(UserDBDAO userDB) {
		
		if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			return new PostgreSQLConstant(userDB);
		} else if(userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT) {
			return new MSSQLConstants(userDB);
		} else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT |
				userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			return new MySQLConstants(userDB);
		} else if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
			return new SQLiteConstants(userDB);
		}
		
		return new DefaultConstants(userDB);
	}
}
