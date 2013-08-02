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
package com.hangum.tadpole.commons.sql.util.executer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.commons.sql.util.executer.procedure.MSSQLProcedureExecuter;
import com.hangum.tadpole.commons.sql.util.executer.procedure.MySqlProcedureExecuter;
import com.hangum.tadpole.commons.sql.util.executer.procedure.OracleProcedureExecuter;
import com.hangum.tadpole.commons.sql.util.executer.procedure.ProcedureExecutor;
import com.hangum.tadpole.dao.DBInfoDAO;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB procedure executer manager
 * 
 * @author hangum
 *
 */
public class ProcedureExecuterManager {
	protected UserDBDAO userDB;
	protected ProcedureFunctionDAO procedureDAO;
	
	public ProcedureExecuterManager(UserDBDAO userDB, ProcedureFunctionDAO procedureDAO) {
		this.userDB = userDB;
		this.procedureDAO = procedureDAO;
	}

	/**
	 * return procedure executer
	 * 
	 * @return
	 * @throws Exception
	 */
	public ProcedureExecutor getExecuter() throws Exception {
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT ) {
			return new OracleProcedureExecuter(procedureDAO, userDB);
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MSSQL_8_LE_DEFAULT ||
				DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MSSQL_DEFAULT ) {
			return new MSSQLProcedureExecuter(procedureDAO, userDB);
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT ||
				DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MARIADB_DEFAULT) {
			return new MySqlProcedureExecuter(procedureDAO, userDB);
		} else {
			throw new Exception("Not Support database");
		}
	}
	
	/**
	 * DB is that it supports?
	 * 
	 * @return
	 */
	public boolean isSupport() {
		try {
			getExecuter();
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * Is executed procedure?
	 * 
	 * @param procedureDAO
	 * @param useDB
	 * @return
	 */
	public boolean isExecuted(ProcedureFunctionDAO procedureDAO, UserDBDAO selectUseDB) {
		if(!isSupport()) {
			MessageDialog.openError(null, "Error", "Not Support database");
			return false;
		}
		if(!procedureDAO.isValid()) {
			MessageDialog.openError(null, "Error", "Not valid this procedure.");
			return false;
		}
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT) {
			double dbVersion = 0.0;
			try {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);				
				DBInfoDAO dbInfo = (DBInfoDAO)sqlClient.queryForObject("findDBInfo"); //$NON-NLS-1$
				dbVersion = Double.parseDouble( StringUtils.substring(dbInfo.getProductversion(), 0, 3) );
				
			} catch (Exception e) {

			}
			
			if (dbVersion < 5.5){
				MessageDialog.openError(null, "Error", "The current version does not support.\n\n5.5 or later is supported.");
			}
		}
		
		try {
			ProcedureExecutor procedureExecutor = getExecuter();
			procedureExecutor.getInParameters();
		} catch(Exception e) {
			MessageDialog.openError(null, "Error", e.getMessage());
			return false;
		}
		
		return true;
	}
	
}
