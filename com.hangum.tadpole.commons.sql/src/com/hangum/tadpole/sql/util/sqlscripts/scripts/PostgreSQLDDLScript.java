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
package com.hangum.tadpole.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.mysql.TriggerDAO;
import com.hangum.tadpole.sql.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Oracle DDL Script
 * 
 * 
 * @author hangum
 * 
 */
public class PostgreSQLDDLScript extends AbstractRDBDDLScript {
	private static final Logger logger = Logger.getLogger(PostgreSQLDDLScript.class);

	public PostgreSQLDDLScript(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType) {
		super(userDB, actionType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript
	 * #getTableScript(com.hangum.tadpole.dao.mysql.TableDAO)
	 */
	@Override
	public String getTableScript(TableDAO tableDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		List<HashMap> srcList = client.queryForList("getTableScript", tableDAO.getName());

		StringBuilder result = new StringBuilder("");
		result.append("/* DROP TABLE " + tableDAO.getName() + " ; */ \n\n");
		result.append("CREATE TABLE " + tableDAO.getName() + "( \n");
		for (int i = 0; i < srcList.size(); i++) {
			HashMap<String, Object> source = srcList.get(i);

			result.append("\t");
			if (i > 0)
				result.append(",");
			result.append(source.get("column_name")).append(" ");
			result.append(source.get("data_type"));

			if (source.get("data_precision") != null && ((Integer) source.get("data_precision") > 0)) {
				result.append("(" + source.get("data_precision"));
				if (source.get("data_scale") != null && ((Integer) source.get("data_scale") > 0)) {
					result.append("," + source.get("data_scale"));
				}

				result.append(")");
			} else if ((Integer) source.get("data_length") > 0) {
				result.append("(" + source.get("data_length") + ")");
			} else {
				result.append(" ");
			}

			if (source.get("data_default") != null && !"".equals(source.get("data_default"))) {
				if (StringUtils.contains((String) source.get("data_type"), "text")) {
					result.append(" DEFAULT '" + source.get("data_default") + "'");
				} else {
					result.append(" DEFAULT " + source.get("data_default"));
				}
			}

			if (!"NULL".equals(source.get("nullable"))) {
				result.append(" NOT NULL ");
			}
			result.append("\n");
		}

		// primary key 
		List<HashMap> srcPkList = client.queryForList("getTableScript.pk", tableDAO.getName());				
		for (int i=0; i<srcPkList.size(); i++){
			HashMap<String, Object> source =  srcPkList.get(i);
			if(i==0){
				result.append("\t,CONSTRAINT ").append(source.get("constraint_name")).append(" PRIMARY KEY ( ").append(source.get("column_name"));
			}else{
				result.append(", "+source.get("column_name"));
			}
			
			if(i == srcPkList.size()-1){
				result.append(") \n");
			}
		}

		result.append(");\n");

		// table, column comments
		result.append("\n\n");
		List<String> srcCommentList = client.queryForList("getTableScript.comments", tableDAO.getName());
		String commentStr = "";
		if (srcCommentList.size() == 0){
			result.append("COMMENT ON TABLE " + tableDAO.getName() + " is ''; /* table comment is empty.*/\n");
			result.append("COMMENT ON COLUMN " + tableDAO.getName() + ".[column name] is ''; /* column comment is empty.*/\n");
		}
		for (int i = 0; i < srcCommentList.size(); i++) {
			commentStr = srcCommentList.get(i);
			if (!"".equals(commentStr)){
				result.append(srcCommentList.get(i) + ";\n");
			}
		}

		// foreign key

		// column constraint (사용자 정의 컬럼 제약조건)

		// partition table define

		// storage option

		// iot_type table define

		// table grant

		// table trigger

		// table synonyms

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript
	 * #getViewScript(java.lang.String)
	 */
	@Override
	public String getViewScript(String strName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		StringBuilder result = new StringBuilder("");
		result.append("/* DROP VIEW " + strName + "; */ \n\n");

		List<String> srcViewHeadList = client.queryForList("getViewScript.head", strName);
		for (int i = 0; i < srcViewHeadList.size(); i++) {
			result.append(srcViewHeadList.get(i) + "\n");
		}
		List<String> srcViewBodyList = client.queryForList("getViewScript.body", strName);
		for (int i = 0; i < srcViewBodyList.size(); i++) {
			result.append(srcViewBodyList.get(i) + "\n");
		}

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript
	 * #getIndexScript(com.hangum.tadpole.dao.mysql.InformationSchemaDAO)
	 */
	@Override
	public String getIndexScript(InformationSchemaDAO indexDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

		StringBuilder result = new StringBuilder("");
		List srcScriptList = client.queryForList("getIndexScript", indexDAO.getINDEX_NAME());

		result.append("/* DROP INDEX " + indexDAO.getINDEX_NAME() + "; */ \n\n");

		for (int i = 0; i < srcScriptList.size(); i++) {
			result.append((String) srcScriptList.get(i));
		}

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript
	 * #getFunctionScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getFunctionScript(ProcedureFunctionDAO functionDAO) throws Exception {
		logger.debug("\n Function DDL Generation...");

		return (getFunctionScript(functionDAO.getName()));

	}

	public String getFunctionScript(String funcName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		StringBuilder result = new StringBuilder("");

		HashMap<String, String> srcProc = null;
		result.append("/* DROP FUNCTION " + funcName + "; */ \n\n");
		result.append("CREATE OR REPLACE FUNCTION " + funcName);
		srcProc = (HashMap<String, String>) client.queryForObject("getFunctionScript", funcName);
		String parameters[] = String.valueOf(srcProc.get("parameter_types")).split(" ");

		result.append("(");
		for (String param : parameters) {
			if (!"".equals(param)) {
				result.append((String) client.queryForObject("getProcedureScript.type", Long.valueOf(param)));
			}
		}
		result.append(")");

		String return_type = (String) client.queryForObject("getProcedureScript.type", srcProc.get("return_types"));

		if (!"".equals(return_type)) {
			result.append(" RETURNS " + return_type);
		}

		result.append(" AS $$");
		result.append(srcProc.get("source_text"));
		result.append("$$ LANGUAGE 'plpgsql';\n");

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript
	 * #getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		logger.debug("\n Procedure DDL Generation...");

		StringBuilder result = new StringBuilder("");

		HashMap<String, String> srcProc = null;
		result.append("/* DROP FUNCTION " + procedureDAO.getName() + "; */ \n\n");
		result.append("CREATE OR REPLACE FUNCTION " + procedureDAO.getName());
		srcProc = (HashMap<String, String>) client.queryForObject("getProcedureScript", procedureDAO.getName());
		String parameters[] = String.valueOf(srcProc.get("parameter_types")).split(" ");

		result.append("(");
		for (String param : parameters) {
			if (!"".equals(param)) {
				result.append((String) client.queryForObject("getProcedureScript.type", Long.valueOf(param)));
			}
		}
		result.append(")");

		String return_type = (String) client.queryForObject("getProcedureScript.type", srcProc.get("return_types"));

		if (!"".equals(return_type)) {
			result.append(" RETURNS " + return_type);
		}

		result.append(" AS $$");
		result.append(srcProc.get("source_text"));
		result.append("$$ LANGUAGE 'plpgsql';\n");

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript
	 * #getTriggerScript(com.hangum.tadpole.dao.mysql.TriggerDAO)
	 */
	@Override
	public String getTriggerScript(TriggerDAO triggerDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		String objectName = triggerDAO.getTrigger();

		logger.debug("\n Trigger DDL Generation...");

		StringBuilder result = new StringBuilder("");

		HashMap<String, String> srcScriptList = (HashMap<String, String>) client.queryForObject("getTriggerScript", objectName);

		result.append("DROP TRIGGER IF EXISTS " + objectName + " ON " + srcScriptList.get("event_table") + ";\n\n");

		result.append("CREATE TRIGGER " + objectName + "\n");
		result.append(srcScriptList.get("action_timing") + " ");
		result.append(srcScriptList.get("event_name") + " ON ");
		result.append(srcScriptList.get("event_table") + " \n ");
		result.append("FOR EACH " + srcScriptList.get("action_orientation") + " ");

		String action_statement = srcScriptList.get("action_statement");

		result.append(action_statement + " \n ");

		if (action_statement.trim().toUpperCase().startsWith("EXECUTE PROCEDURE")) {
			// trigger function script

			String funcName = action_statement.replace("EXECUTE PROCEDURE", "").trim();
			funcName = funcName.substring(0, funcName.lastIndexOf('('));

			result.insert(0, getFunctionScript(funcName.trim().toLowerCase()) + "\n\n");

		}

		return result.toString();
	}

	@Override
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("package_name", procedureDAO.getPackagename());
		map.put("object_name", procedureDAO.getName());

		logger.debug("\n getProcedureInParamter=" + map.get("package_name"));
		logger.debug("\n getProcedureInParamter=" + map.get("object_name"));
		logger.debug("\n procedureDAO=" + procedureDAO);

		return client.queryForList("getProcedureInParamter", map);
	}

	@Override
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("package_name", procedureDAO.getPackagename());
		map.put("object_name", procedureDAO.getName());

		return client.queryForList("getProcedureOutParamter", map);
	}

}
