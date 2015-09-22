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
package com.hangum.tadpole.engine.sql.util.sqlscripts;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.AbstractRDBDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.MSSQL_8_LE_DDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.MySqlDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.OracleDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.PostgreSQLDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.SQLiteDDLScript;

/**
 * DDLScript Mananager
 * 
 * @author hangum
 *
 */
public class DDLScriptManager {
	protected UserDBDAO userDB;
	protected DB_ACTION actionType;
	
	protected AbstractRDBDDLScript rdbScript = null;
	
	/**
	 * get ddl script
	 * 
	 * @param userDB
	 * @param actionType
	 * @throws Exception
	 */
	public DDLScriptManager(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType) throws Exception {
		this.userDB = userDB;
		this.actionType = actionType;
		
		initRDBScript();
	}
	
	/**
	 * get ddl script
	 * 
	 * @param userDB
	 * @param actionType
	 * @throws Exception
	 */
	public DDLScriptManager(UserDBDAO userDB) throws Exception {
		this.userDB = userDB;
		
		initRDBScript();
	}
	
	/**
	 * select object db types
	 * 
	 * @throws Exception
	 */
	private void initRDBScript() throws Exception {
		if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
			rdbScript = new SQLiteDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT ) {
			rdbScript = new OracleDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT ) {
			rdbScript = new PostgreSQLDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT ||
				DBDefine.getDBDefine(userDB) == DBDefine.MSSQL_DEFAULT ) {
			rdbScript = new MSSQL_8_LE_DDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT ||
				DBDefine.getDBDefine(userDB) == DBDefine.MARIADB_DEFAULT) {
			rdbScript = new MySqlDDLScript(userDB, actionType);
		} else {
			throw new Exception("Not support Database");
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public String getScript(Object obj) throws Exception {
		String retStr = "";
		
		// find DDL Object
		if(PublicTadpoleDefine.DB_ACTION.TABLES == actionType) {
			retStr = rdbScript.getTableScript((TableDAO)obj);
		} else if(PublicTadpoleDefine.DB_ACTION.VIEWS == actionType) {
			retStr = rdbScript.getViewScript(obj.toString());
		} else if(PublicTadpoleDefine.DB_ACTION.INDEXES == actionType) {
			retStr = rdbScript.getIndexScript((InformationSchemaDAO)obj);
		} else if(PublicTadpoleDefine.DB_ACTION.FUNCTIONS == actionType) {
			retStr = rdbScript.getFunctionScript((ProcedureFunctionDAO)obj);
		} else if(PublicTadpoleDefine.DB_ACTION.PROCEDURES == actionType) {
			retStr = rdbScript.getProcedureScript((ProcedureFunctionDAO)obj);
		} else if(PublicTadpoleDefine.DB_ACTION.PACKAGES == actionType) {
			retStr = rdbScript.getProcedureScript((ProcedureFunctionDAO)obj);
		} else if(PublicTadpoleDefine.DB_ACTION.TRIGGERS == actionType) {
			retStr = rdbScript.getTriggerScript((TriggerDAO)obj);
		} else {
			throw new Exception("Not support Database");
		}
		
		// 마지막 ; 문자가 포함되어있을 경우.
		if(StringUtils.endsWith(StringUtils.trim(retStr), PublicTadpoleDefine.SQL_DELIMITER)) {
			return retStr;
		} else {
			return retStr + PublicTadpoleDefine.SQL_DELIMITER;
		}
	}
	
	/**
	 * Procedure In Parameters
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		return rdbScript.getProcedureInParamter(procedureDAO);
	}
	
	/**
	 * Procedure OUT Parameters
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		return rdbScript.getProcedureOutParamter(procedureDAO);
	}
}
