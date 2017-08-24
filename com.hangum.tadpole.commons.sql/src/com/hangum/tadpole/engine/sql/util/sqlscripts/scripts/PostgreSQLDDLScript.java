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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.postgresql.jdbc4.Jdbc4Array;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleDBLinkDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSequenceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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

	public PostgreSQLDDLScript(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) {
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
//		result.append("/* DROP TABLE " + tableDAO.getName() + " ; */ \n\n");
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
		
		
		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("schema", tableDAO.getSchema_name());
		sqlParam.put("object_name", tableDAO.getName());
		
		List<String> srcCommentList = client.queryForList("getTableScript.comments", sqlParam);
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
		result.append("\n\n");
		
		if(userDB.getDBDefine() != DBDefine.AMAZON_REDSHIFT_DEFAULT) {
			List<String> srcTriggerScripts = client.queryForList("getTableScript.trigger", tableDAO.getName());
			String scriptSource = "";
			for (int i = 0; i < srcTriggerScripts.size(); i++) {
				scriptSource = srcTriggerScripts.get(i);
				if (!"".equals(scriptSource)){
					result.append(srcTriggerScripts.get(i) + ";\n");
				}
			}
		}

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
	public String getViewScript(TableDAO tableDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		StringBuilder result = new StringBuilder("");
//		result.append("/* DROP VIEW " + strName + "; */ \n\n");

		List<String> srcViewHeadList = client.queryForList("getViewScript.head", tableDao.getName());
		for (int i = 0; i < srcViewHeadList.size(); i++) {
			result.append(srcViewHeadList.get(i) + "\n");
		}
		List<String> srcViewBodyList = client.queryForList("getViewScript.body", tableDao.getName());
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

//		result.append("/* DROP INDEX " + indexDAO.getINDEX_NAME() + "; */ \n\n");

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
		if(logger.isDebugEnabled()) logger.debug("\n Function DDL Generation...");

		return (getFunctionScript(functionDAO.getSchema_name(), functionDAO.getName()));

	}

	public String getFunctionScript(String schemaName, String funcName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		StringBuilder result = new StringBuilder("");

		ArrayList<HashMap<String, Object>> srcProcList = null;
//		result.append("/* DROP FUNCTION " + funcName + "; */ \n\n");
		
		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("schema", schemaName);
		sqlParam.put("object_name", funcName);

		srcProcList = (ArrayList<HashMap<String, Object>>) client.queryForList("getFunctionScript", sqlParam);

		for (HashMap<String, Object> srcProc :srcProcList) {

			result.append("CREATE OR REPLACE FUNCTION " + funcName);

			String parameters[] = String.valueOf(srcProc.get("parameter_types")).split(" ");
			Jdbc4Array array = (Jdbc4Array) srcProc.get("arg_names");
			
			String paramnames[] = null;
			if (array!=null) paramnames = (String[])array.getArray();
			
			result.append("(");
			for (int a=0;a< parameters.length; a++ ) {
				if (a>0)result.append(", ");
				// 파라미터 이름이 없는것도 있으므로...
				if (paramnames != null){
					if (a < paramnames.length) result.append(paramnames[a] + " ");
				}
				if (!"".equals(parameters[a]))
				result.append((String) client.queryForObject("getProcedureScript.type", Long.valueOf(parameters[a])));
			}
			result.append(")");
	
			String return_type = (String) client.queryForObject("getProcedureScript.type", srcProc.get("return_types"));
	
			if (!"".equals(return_type)) {
				result.append(" RETURNS " + return_type);
			}
	
			result.append(" AS $$");
			result.append(srcProc.get("source_text"));
			result.append("$$ LANGUAGE 'plpgsql';\n\n");
		}

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
		if(logger.isDebugEnabled()) logger.debug("\n Procedure DDL Generation...");

		ArrayList<HashMap<String, Object>> srcProcList = null;
		StringBuilder result = new StringBuilder("");

		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("schema", procedureDAO.getSchema_name());
		sqlParam.put("object_name", procedureDAO.getName());
		//srcProc = (HashMap<String, String>) client.queryForObject("getProcedureScript", sqlParam);
		srcProcList = (ArrayList<HashMap<String, Object>>) client.queryForList("getProcedureScript", sqlParam);
		for (HashMap<String, Object> srcProc :srcProcList) {

			result.append("CREATE OR REPLACE FUNCTION " +  procedureDAO.getName());

			String parameters[] = String.valueOf(srcProc.get("parameter_types")).split(" ");
			Jdbc4Array array = (Jdbc4Array) srcProc.get("arg_names");
			
			String paramnames[] = null;
			if (array!=null) paramnames = (String[])array.getArray();
			
			result.append("(");
			for (int a=0;a< parameters.length; a++ ) {
				if (a>0)result.append(", ");
				// 파라미터 이름이 없는것도 있으므로...
				if (paramnames != null){
					if (a < paramnames.length) result.append(paramnames[a] + " ");
				}
				if (!"".equals(parameters[a]))
				result.append((String) client.queryForObject("getProcedureScript.type", Long.valueOf(parameters[a])));
			}
			result.append(")");
	
			String return_type = (String) client.queryForObject("getProcedureScript.type", srcProc.get("return_types"));
	
			if (!"".equals(return_type)) {
				result.append(" RETURNS " + return_type);
			}
	
			result.append(" AS $$");
			result.append(srcProc.get("source_text"));
			result.append("$$ LANGUAGE 'plpgsql';\n\n");
		}

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

		if(logger.isDebugEnabled()) logger.debug("\n Trigger DDL Generation...");

		StringBuilder result = new StringBuilder("");

		result.append("\n\n");
		List<String> srcTriggerScripts = client.queryForList("getTriggerScript", objectName);
		String scriptSource = "";
		for (int i = 0; i < srcTriggerScripts.size(); i++) {
			scriptSource = srcTriggerScripts.get(i);
			if (!"".equals(scriptSource)){
				result.append(srcTriggerScripts.get(i) + ";\n");
			}
		}

		return result.toString();
	}

	@Override
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("package_name", procedureDAO.getPackagename());
		map.put("object_name", procedureDAO.getName());
		map.put("definer", procedureDAO.getDefiner()); /* definer 컬럼에 담은 아규먼트 타입정보를 이용. */

		if(logger.isDebugEnabled()) {
			logger.debug("\n package_name=" + map.get("package_name"));
			logger.debug("\n object_name=" + map.get("object_name"));
			logger.debug("\n argument_types=" + map.get("definer"));
		}

		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put("schema", procedureDAO.getSchema_name());
		sqlParam.put("object_name", procedureDAO.getName());
		sqlParam.put("definer", procedureDAO.getDefiner());
		
		return client.queryForList("getProcedureInParamter", sqlParam);
	}

	@Override
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("package_name", procedureDAO.getPackagename());
		map.put("object_name", procedureDAO.getName());
		map.put("definer", procedureDAO.getDefiner()); /* definer 컬럼에 담은 아규먼트 타입정보를 이용. */

		return client.queryForList("getProcedureOutParamter", map);
	}

	@Override
	public String getSequenceScript(OracleSequenceDAO sequenceDAO) throws Exception {
		// TODO Auto-generated method stub
		return "undefined";
	}

	@Override
	public String getDBLinkScript(OracleDBLinkDAO dblinkDAO) throws Exception {
		// TODO Auto-generated method stub
		return "undefined";
	}

}
