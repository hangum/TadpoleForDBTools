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
		
		Map srcList = (HashMap)client.queryForObject("getTableScript", tableDAO.getFullName());
		if(StringUtils.isBlank(tableDAO.getSchema_name())){
			return srcList.get("Create Table")+"";
		}else{
			return StringUtils.replaceOnce(srcList.get("Create Table")+"", "CREATE TABLE ", "CREATE TABLE " + tableDAO.getSchema_name() + ".");
		}
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getViewScript(java.lang.String)
	 */
	@Override
	public String getViewScript(TableDAO tableDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map srcList = (HashMap)client.queryForObject("getViewScript", tableDao.getFullName());
		String strSource = ""+srcList.get("Create View");
		strSource = StringUtils.substringAfterLast(strSource, "VIEW");
		
		return "CREATE VIEW " + strSource.trim();
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

		Map srcList = (HashMap)client.queryForObject("getFunctionScript", functionDAO.getFullName());
		String strSource = ""+srcList.get("Create Function");
		strSource = StringUtils.substringAfterLast(strSource, "FUNCTION");
		
		if(StringUtils.isBlank(functionDAO.getSchema_name())){
			return "CREATE FUNCTION " + strSource.trim();
		}else{
			return "CREATE FUNCTION " + functionDAO.getSchema_name() + "." + strSource.trim();
		}
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO)	throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		Map srcList = (HashMap)client.queryForObject("getProcedureScript", procedureDAO.getFullName());
		String strSource = ""+srcList.get("Create Procedure");
		strSource = StringUtils.substringAfterLast(strSource, "PROCEDURE");
		
		if(StringUtils.isBlank(procedureDAO.getSchema_name())){
			return "CREATE PROCEDURE " + strSource.trim();
		}else{
			return "CREATE PROCEDURE " + procedureDAO.getSchema_name() + "." + strSource.trim();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getTriggerScript(com.hangum.tadpole.dao.mysql.TriggerDAO)
	 */
	@Override
	public String getTriggerScript(TriggerDAO triggerDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map srcList = (HashMap)client.queryForObject("getTriggerScript", triggerDAO.getFullName());	
		String strSource = ""+srcList.get("SQL Original Statement");
		strSource = StringUtils.substringAfterLast(strSource, "TRIGGER");
		
		if(StringUtils.isBlank(triggerDAO.getSchema_name())){
			return "CREATE TRIGGER " + strSource.trim();
		}else{
			return "CREATE TRIGGER " + triggerDAO.getSchema_name() + "." + strSource.trim();
		}
	}

	@Override
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("schema_name", procedureDAO.getSchema_name() == null ? userDB.getSchema() : procedureDAO.getSchema_name()); //$NON-NLS-1$
		map.put("object_name", procedureDAO.getName());	

		return client.queryForList("getProcedureInParamter", map);
	}
	
	@Override
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("schema_name", procedureDAO.getSchema_name() == null ? userDB.getSchema() : procedureDAO.getSchema_name()); //$NON-NLS-1$
		map.put("object_name", procedureDAO.getName());	

		return client.queryForList("getProcedureOutParamter", map);
	}
}

	