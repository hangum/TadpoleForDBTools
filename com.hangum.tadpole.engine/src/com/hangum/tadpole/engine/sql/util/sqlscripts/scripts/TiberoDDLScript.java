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
package com.hangum.tadpole.engine.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Oracle DDL Script
 * 
 * 
 * @author hangum
 *
 */
public class TiberoDDLScript extends OracleDDLScript {
	private static final Logger logger = Logger.getLogger(TiberoDDLScript.class);
	
	public TiberoDDLScript(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) {
		super(userDB, actionType);
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getFunctionScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getFunctionScript(ProcedureFunctionDAO functionDAO)
			throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		if(logger.isDebugEnabled()) logger.debug("\n Function DDL Generation...");
		
		HashMap<String, String>paramMap = new HashMap<String, String>();
		paramMap.put("schema_name", functionDAO.getSchema_name() == null ? userDB.getSchema() : functionDAO.getSchema_name()); //$NON-NLS-1$
		paramMap.put("object_name", StringUtils.replace(functionDAO.getSysName(), "\"", "")); //$NON-NLS-1$

		StringBuilder result = new StringBuilder("");

		List<String> srcScriptList = client.queryForList("getFunctionScript", paramMap);
		if(srcScriptList.isEmpty()) {
			return strMSG_BlankScript;
		} else {
			for (int i=0; i<srcScriptList.size(); i++){
				result.append( srcScriptList.get(i));
			}
			
			return result.toString();
		}
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		if(logger.isDebugEnabled()) logger.debug("\n Procedure DDL Generation...");
		
		HashMap<String, String>paramMap = new HashMap<String, String>();
		paramMap.put("schema_name", procedureDAO.getSchema_name() == null ? userDB.getSchema() : procedureDAO.getSchema_name()); //$NON-NLS-1$
		paramMap.put("object_name", procedureDAO.getName()); //$NON-NLS-1$

		StringBuilder result = new StringBuilder("");
		String objType = (String)client.queryForObject("getSourceObjectType", paramMap);
							
		List<String> srcScriptList = null;
		if (StringUtils.contains(objType, "PROCEDURE")){
			srcScriptList = client.queryForList("getProcedureScript", paramMap);
			if(srcScriptList.isEmpty()) {
				return strMSG_BlankScript;
			} else {
				for (int i=0; i<srcScriptList.size(); i++){
					result.append( srcScriptList.get(i));
				}
			}
		}else if (StringUtils.contains(objType, "PACKAGE")){
			result.append("/* STATEMENT PACKAGE BODY " + procedureDAO.getName() + "; */ \n\n");
			result.append("/* STATEMENT PACKAGE " + procedureDAO.getName() + "; */ \n\n");
			
			srcScriptList = client.queryForList("getPackageScript.head", paramMap);
			if(srcScriptList.isEmpty()) {
				return strMSG_BlankScript;
			} else {
				for (int i=0; i<srcScriptList.size(); i++){
					result.append( srcScriptList.get(i));
				}
				result.append("/ \n\n ");
				srcScriptList = client.queryForList("getPackageScript.body", paramMap);				
				for (int i=0; i<srcScriptList.size(); i++){
					result.append( srcScriptList.get(i));
				}
				
				result.append("/ \n\n ");
			}
		}
		
		return result.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getTriggerScript(com.hangum.tadpole.dao.mysql.TriggerDAO)
	 */
	@Override
	public String getTriggerScript(TriggerDAO triggerDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		String objectName = triggerDAO.getTrigger();

		if(logger.isDebugEnabled()) logger.debug("\n Trigger DDL Generation...");
		
		HashMap<String, String>paramMap = new HashMap<String, String>();
		paramMap.put("schema_name", triggerDAO.getSchema_name() == null ? userDB.getSchema() : triggerDAO.getSchema_name()); //$NON-NLS-1$
		paramMap.put("object_name", triggerDAO.getTrigger()); //$NON-NLS-1$
		
		StringBuilder result = new StringBuilder("");

		List<String> srcScriptList = client.queryForList("getTriggerScript", paramMap);
		if(srcScriptList.isEmpty()) {
			return strMSG_BlankScript;
		} else {
			for (int i=0; i<srcScriptList.size(); i++){
				result.append( srcScriptList.get(i));
			}
			
			return result.toString();
		}
	}

}
