/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     nilriri - MySQL DDL Script add.
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * MySQL DDL Script
 * 
 * @author nilriri
 *
 */
public class MySqlDDLScript extends AbstractRDBDDLScript {
	private static final Logger logger = Logger.getLogger(MySqlDDLScript.class);
	/**
	 * @param userDB
	 * @param actionType
	 */
	public MySqlDDLScript(UserDBDAO userDB, OBJECT_TYPE actionType) {
		super(userDB, actionType);
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getTableScript(com.hangum.tadpole.dao.mysql.TableDAO)
	 */
	@Override
	public String getTableScript(TableDAO tableDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map srcList = (HashMap)client.queryForObject("getTableScript", tableDAO.getName());
		return ""+srcList.get("Create Table");
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getViewScript(java.lang.String)
	 */
	@Override
	public String getViewScript(String strName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map srcList = (HashMap)client.queryForObject("getViewScript", strName);
		String strSource = ""+srcList.get("Create View");
		strSource = StringUtils.substringAfterLast(strSource, "VIEW");
		
		return "CREATE VIEW " + strSource;
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getIndexScript(com.hangum.tadpole.dao.mysql.InformationSchemaDAO)
	 */
	@Override
	public String getIndexScript(InformationSchemaDAO indexDAO)
			throws Exception {
		throw new Exception("Not Support Database");
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getFunctionScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getFunctionScript(ProcedureFunctionDAO functionDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		StringBuilder result = new StringBuilder("");

		Map srcList = (HashMap)client.queryForObject("getFunctionScript", functionDAO.getName());
		String strSource = ""+srcList.get("Create Function");
		strSource = StringUtils.substringAfterLast(strSource, "FUNCTION");
		
		return "CREATE FUNCTION " + strSource;
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO)	throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		Map srcList = (HashMap)client.queryForObject("getProcedureScript", procedureDAO.getName());
		String strSource = ""+srcList.get("Create Procedure");
		strSource = StringUtils.substringAfterLast(strSource, "PROCEDURE");
		
		return "CREATE PROCEDURE " + strSource;
	}
	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getTriggerScript(com.hangum.tadpole.dao.mysql.TriggerDAO)
	 */
	@Override
	public String getTriggerScript(TriggerDAO triggerDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		StringBuilder result = new StringBuilder("");

		Map srcList = (HashMap)client.queryForObject("getTriggerScript", triggerDAO.getTrigger());	
		String strSource = ""+srcList.get("SQL Original Statement");
		strSource = StringUtils.substringAfterLast(strSource, "TRIGGER");
		
		return "CREATE TRIGGER " + strSource;
	}

	@Override
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		return client.queryForList("getProcedureInParamter", procedureDAO.getName());
	}
	
	@Override
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		return client.queryForList("getProcedureOutParamter", procedureDAO.getName());
	}
}

	