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
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.AbstractRDBDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.AltibaseDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.MSSQL_8_LE_DDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.MySqlDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.OracleDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.PostgreSQLDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.SQLiteDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.TiberoDDLScript;

/**
 * DDLScript Mananager
 * 
 * @author hangum
 *
 */
public class DDLScriptManager {
	protected String objectName = "";
	protected UserDBDAO userDB;
	protected OBJECT_TYPE actionType;
	
	protected AbstractRDBDDLScript rdbScript = null;
	
	/**
	 * get ddl script
	 * 
	 * @param userDB
	 * @param actionType
	 * @throws Exception
	 */
	public DDLScriptManager(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) throws Exception {
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
		} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
			rdbScript = new OracleDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT) {
			rdbScript = new TiberoDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT ) {
			rdbScript = new PostgreSQLDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT ) {
			rdbScript = new MSSQL_8_LE_DDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT ||
				userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT
		) {
			rdbScript = new MySqlDDLScript(userDB, actionType);
		} else if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
			rdbScript = new AltibaseDDLScript(userDB, actionType);
		} else {
			throw new Exception(Messages.get().ProcedureExecuterManager_0);
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
		if(PublicTadpoleDefine.OBJECT_TYPE.TABLES == actionType) {
			TableDAO tbl = (TableDAO)obj;
			setObjectName(tbl.getName());
			
			retStr = rdbScript.getTableScript(tbl);
		} else if(PublicTadpoleDefine.OBJECT_TYPE.VIEWS == actionType) {
			TableDAO tbl = (TableDAO)obj;
			
			setObjectName(tbl.getName());
			retStr = rdbScript.getViewScript(tbl.getName());
		} else if(PublicTadpoleDefine.OBJECT_TYPE.INDEXES == actionType) {
			InformationSchemaDAO index = (InformationSchemaDAO)obj;
			setObjectName(index.getINDEX_NAME());
			
			retStr = rdbScript.getIndexScript(index);
		} else if(PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS == actionType) {
			ProcedureFunctionDAO procedure = (ProcedureFunctionDAO)obj;
			setObjectName(procedure.getName());
			
			retStr = rdbScript.getFunctionScript(procedure);
		} else if(PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES == actionType) {
			ProcedureFunctionDAO procedure = (ProcedureFunctionDAO)obj;
			setObjectName(procedure.getName());
			
			retStr = rdbScript.getProcedureScript(procedure);
		} else if(PublicTadpoleDefine.OBJECT_TYPE.PACKAGES == actionType) {
			ProcedureFunctionDAO procedure = (ProcedureFunctionDAO)obj;
			setObjectName(procedure.getName());
			
			retStr = rdbScript.getProcedureScript(procedure);
		} else if(PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS == actionType) {
			TriggerDAO trigger = (TriggerDAO)obj;
			setObjectName(trigger.getName());
			
			retStr = rdbScript.getTriggerScript(trigger);
		} else {
			throw new Exception(Messages.get().ProcedureExecuterManager_0);
		}
		
		// 마지막 ; 문자가 포함되어있을 경우.
		if(StringUtils.endsWith(StringUtils.trimToEmpty(retStr), PublicTadpoleDefine.SQL_DELIMITER)) {
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

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
}
