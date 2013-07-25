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

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.commons.sql.util.executer.procedure.MySqlProcedureExecuter;
import com.hangum.tadpole.commons.sql.util.executer.procedure.OracleProcedureExecuter;
import com.hangum.tadpole.commons.sql.util.executer.procedure.ProcedureExecutor;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * RDB procedure executer manager
 * 
 * @author hangum
 *
 */
public class ProcedureExecuterManager {
	protected UserDBDAO userDB;
	protected ProcedureFunctionDAO procedureDAO;
	
	public ProcedureExecuterManager(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
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
		}else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.MYSQL_DEFAULT ) {
			return new MySqlProcedureExecuter(procedureDAO, userDB);
		} else {
			throw new Exception("Not Support database");
		}
	}
	
	
	
}
