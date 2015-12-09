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
package com.hangum.tadpole.rdb.core.viewers.object.sub.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;

/**
 * Execute table column object 
 * 
 * @author hangum
 *
 */
public class TableColumnObjectQuery {
	private static Logger logger = Logger.getLogger(TableColumnObjectQuery.class);
	
	/**
	 * Delete table column
	 * 
	 * @param userDB
	 * @param tableColumnDao
	 * @return
	 */
	public static RequestResultDAO deleteColumn(final UserDBDAO userDB, final List<TableColumnDAO> listTableColumnDao) throws Exception {
		RequestResultDAO resultDao = null;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT |
				userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT |
				userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT |
				userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT |
				userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT
		) {
			for(TableColumnDAO tableColumnDao: listTableColumnDao) {
				String strQuery = String.format("ALTER TABLE %s DROP COLUMN %s", tableColumnDao.getTableDao().getSysName(), tableColumnDao.getField());
				resultDao = ExecuteDDLCommand.executSQL(userDB, strQuery); //$NON-NLS-1$
			}
			
		} else {
			throw new Exception("Not support delete column.");
		}
		
		return resultDao;
	}


}
