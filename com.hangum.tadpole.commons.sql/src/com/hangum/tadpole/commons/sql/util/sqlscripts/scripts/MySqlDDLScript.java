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
package com.hangum.tadpole.commons.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.List;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.mysql.TriggerDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * MySQL DDL Script
 * 
 * @author nilriri
 *
 */
public class MySqlDDLScript extends AbstractRDBDDLScript {

	/**
	 * @param userDB
	 * @param actionType
	 */
	public MySqlDDLScript(UserDBDAO userDB, DB_ACTION actionType) {
		super(userDB, actionType);
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getTableScript(com.hangum.tadpole.dao.mysql.TableDAO)
	 */
	@Override
	public String getTableScript(TableDAO tableDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		List<HashMap> srcList = client.queryForList("getTableScript", tableDAO.getName());
		
		StringBuilder result = new StringBuilder("");
		for (int i=0; i<srcList.size(); i++){
			HashMap<String, Object> source =  srcList.get(i);
			
			for (String key : source.keySet()){
				result.append(key + "\t:\t");
				result.append(source.get(key)+"\n\n");
			}
		}
			
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getViewScript(java.lang.String)
	 */
	@Override
	public String getViewScript(String strName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		StringBuilder result = new StringBuilder("");

		List<HashMap> srcList = client.queryForList("getViewScript", strName);				
		for (int i=0; i<srcList.size(); i++){
			HashMap<String, Object> source =  srcList.get(i);
			
			for (String key : source.keySet()){
				result.append(key + "\t:\t");
				result.append(source.get(key)+"\n\n");
			}
		}
		
		return result.toString();
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

		List<HashMap> srcList = client.queryForList("getFunctionScript", functionDAO.getName());				
		for (int i=0; i<srcList.size(); i++){
			HashMap<String, Object> source =  srcList.get(i);
			
			for (String key : source.keySet()){
				result.append(key + "\t:\t");
				result.append(source.get(key)+"\n\n");
			}
		}
		
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO)	throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		StringBuilder result = new StringBuilder("");

		List<HashMap> srcList = client.queryForList("getProcedureScript", procedureDAO.getName());				
		for (int i=0; i<srcList.size(); i++){
			HashMap<String, Object> source =  srcList.get(i);
			for (String key : source.keySet()){
				result.append(key + "\t:\t");
				result.append(source.get(key)+"\n\n");
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
		
		StringBuilder result = new StringBuilder("");

		List<HashMap> srcList = client.queryForList("getTriggerScript", triggerDAO.getTrigger());				
		for (int i=0; i<srcList.size(); i++){
			HashMap<String, Object> source =  srcList.get(i);
			for (String key : source.keySet()){
				result.append(key + "\t:\t");
				result.append(source.get(key)+"\n\n");
			}
		}
		
		return result.toString();
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

	