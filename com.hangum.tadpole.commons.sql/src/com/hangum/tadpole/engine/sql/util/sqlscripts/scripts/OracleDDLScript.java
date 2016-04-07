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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Oracle DDL Script
 * 
 * 
 * @author hangum
 *
 */
public class OracleDDLScript extends AbstractRDBDDLScript {
	private static final Logger logger = Logger.getLogger(OracleDDLScript.class);
	
	public OracleDDLScript(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) {
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
//		result.append("/* DROP TABLE " + tableDAO.getName() + " CASCADE CONSTRAINT; */ \n\n");
		result.append("CREATE TABLE " + tableDAO.getName() + "( \n");
		for (int i=0; i<srcList.size(); i++){
			HashMap<String, Object> source =  srcList.get(i);
			
			
			result.append("\t");
			if(i>0)result.append(",");
			result.append(source.get("COLUMN_NAME")).append(" ");
			result.append(source.get("DATA_TYPE"));
								
			if (source.get("DATA_PRECISION") != null && ((BigDecimal)source.get("DATA_PRECISION")).intValue() > 0 ){
				result.append("("+source.get("DATA_PRECISION"));
				if (source.get("DATA_SCALE") != null && ((BigDecimal)source.get("DATA_SCALE")).intValue() > 0 ){
					result.append(","+source.get("DATA_SCALE"));
				}
						
				result.append(")");
			}else if (!StringUtils.contains((String)source.get("DATA_TYPE"), "DATE") && !StringUtils.contains((String)source.get("DATA_TYPE"), "NUMBER")  && ((BigDecimal)source.get("DATA_LENGTH")).intValue() > 0  ){
				result.append("("+source.get("DATA_LENGTH")+")");
			}else{
				result.append(" ");
			}
			
			if (source.get("DATA_DEFAULT") != null ){
			
				if (StringUtils.contains((String)source.get("DATA_TYPE"), "CHAR")  ){
					result.append(" DEFAULT '"+source.get("DATA_DEFAULT")+"'");
				}else{
					result.append(" DEFAULT "+source.get("DATA_DEFAULT") );
				}
			}
				
			if("NO".equals(source.get("NULLABLE"))){
				result.append(" NOT NULL ");
			}
			
			result.append("\n");
			
		}

		// primary key 
		List<HashMap> srcPkList = client.queryForList("getTableScript.pk", tableDAO.getName());				
		for (int i=0; i<srcPkList.size(); i++){
			HashMap<String, Object> source =  srcPkList.get(i);
			if(i==0){
				result.append("\t,CONSTRAINT ").append(source.get("CONSTRAINT_NAME")).append(" PRIMARY KEY ( ").append(source.get("COLUMN_NAME"));
			}else{
				result.append(", "+source.get("COLUMN_NAME"));
			}
			
			if(i == srcPkList.size()-1){
				result.append(") \n");
			}
		}
				
		result.append("); \n\n");
		
		// table, column comments
		List<String> srcCommentList = client.queryForList("getTableScript.comments", tableDAO.getName());				
		for (int i=0; i<srcCommentList.size(); i++){
			result.append( srcCommentList.get(i)+"\n");
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

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getViewScript(java.lang.String)
	 */
	@Override
	public String getViewScript(String strName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		StringBuilder result = new StringBuilder("");
//		result.append("/* DROP VIEW " + strName + "; */ \n\n");

		List<String> srcViewHeadList = client.queryForList("getViewScript.head", strName);				
		for (int i=0; i<srcViewHeadList.size(); i++){
			result.append( srcViewHeadList.get(i)+"\n");
		}
		List<String> srcViewBodyList = client.queryForList("getViewScript.body", strName);				
		for (int i=0; i<srcViewBodyList.size(); i++){
			result.append( srcViewBodyList.get(i)+"\n");
		}
		
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getIndexScript(com.hangum.tadpole.dao.mysql.InformationSchemaDAO)
	 */
	@Override
	public String getIndexScript(InformationSchemaDAO indexDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		StringBuilder result = new StringBuilder("");	
		List<Map<String, String>> srcScriptList = (List<Map<String, String>>) client.queryForList("getIndexScript", indexDAO.getINDEX_NAME());
		
//		result.append("/* DROP INDEX " + indexDAO.getINDEX_NAME() + "; */ \n\n");
		result.append("CREATE ");
		
		Map<String, String> indexMap = new HashMap<String, String>();
		if (srcScriptList.size() > 0){
			indexMap = srcScriptList.get(0);
			if ("UNIQUE".equals(indexMap.get("UNIQUENESS"))) {
				result.append(" UNIQUE INDEX ");
			}else{
				result.append(" INDEX ");
			}
			
			result.append(indexMap.get("TABLE_OWNER")+".");
			result.append(indexMap.get("INDEX_NAME")+" ON ");
			result.append(indexMap.get("TABLE_OWNER")+".");
			result.append(indexMap.get("TABLE_NAME")+"\n ( ");
		
			for (int i=0; i<srcScriptList.size(); i++){
				indexMap = srcScriptList.get(i);
				
				if ("NORMAL".equals(indexMap.get("INDEX_TYPE")) && indexMap.get("COLUMN_EXPRESSION") == null) {
					if (i>0) result.append(",");
					result.append(indexMap.get("COLUMN_NAME"));
				}else{
					if (i>0) result.append(",");
					result.append(indexMap.get("COLUMN_EXPRESSION"));
				}					
			}
			result.append(" ) \n");
			
			if ("YES".equals(indexMap.get("LOGGING"))) {
				result.append(" LOGGING \n");
			}else{	
				result.append(" NO LOGGING \n");
			}
			result.append(" TABLESPACE "+indexMap.get("TABLESPACE_NAME") + "\n");

			result.append(" PCTFREE "+String.valueOf(indexMap.get("PCT_FREE")) + "\n");
			result.append(" INITRANS "+String.valueOf(indexMap.get("INI_TRANS")) + "\n");
			result.append(" MAXTRANS "+String.valueOf(indexMap.get("MAX_TRANS")) + "\n");
			result.append(" STORAGE ( \n ");
			result.append(" \t INITIAL "+String.valueOf(indexMap.get("INITIAL_EXTENT")) + "\n");
			result.append(" \t MINEXTENTS "+String.valueOf(indexMap.get("MIN_EXTENTS")) + "\n");
			result.append(" \t MAX_EXTENTS "+String.valueOf(indexMap.get("MAX_EXTENTS")) + "\n");
			result.append(" \t PCTINCREASE "+String.valueOf(indexMap.get("PCT_INCREASE")) + "\n");
			result.append(" \t BUFFER_POOL "+String.valueOf(indexMap.get("BUFFER_POOL")) + "\n");
			result.append("\t ) \n ");
			result.append(" COMPUTE STATISTICS \n ");
			result.append(" ONLINE ");
		}
		
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getFunctionScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getFunctionScript(ProcedureFunctionDAO functionDAO)
			throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		if(logger.isDebugEnabled()) logger.debug("\n Function DDL Generation...");
		
		StringBuilder result = new StringBuilder("");
//		result.append("/* DROP FUNCTION " + functionDAO.getName() + "; */ \n\n");
		result.append("CREATE OR REPLACE ");

		List<String> srcScriptList = client.queryForList("getFunctionScript", functionDAO.getName());				
		for (int i=0; i<srcScriptList.size(); i++){
			result.append( srcScriptList.get(i));
		}
		
		return result.toString();				
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		if(logger.isDebugEnabled()) logger.debug("\n Procedure DDL Generation...");
		
		StringBuilder result = new StringBuilder("");
		String objType = (String)client.queryForObject("getSourceObjectType", procedureDAO.getName());				
					
		List<String> srcScriptList = null;
		if (StringUtils.contains(objType, "PROCEDURE")){
//			result.append("/* DROP PROCEDURE " + procedureDAO.getName() + "; */ \n\n");
			result.append("CREATE OR REPLACE ");
			srcScriptList = client.queryForList("getProcedureScript", procedureDAO.getName());				
			for (int i=0; i<srcScriptList.size(); i++){
				result.append( srcScriptList.get(i));
			}
		}else if (StringUtils.contains(objType, "PACKAGE")){
			result.append("/* STATEMENT PACKAGE BODY " + procedureDAO.getName() + "; */ \n\n");
			result.append("/* STATEMENT PACKAGE " + procedureDAO.getName() + "; */ \n\n");
			
			result.append("CREATE OR REPLACE ");
			srcScriptList = client.queryForList("getPackageScript.head", procedureDAO.getName());				
			for (int i=0; i<srcScriptList.size(); i++){
				result.append( srcScriptList.get(i));
			}
			result.append("/ \n\n ");
			result.append("CREATE OR REPLACE ");
			srcScriptList = client.queryForList("getPackageScript.body", procedureDAO.getName());				
			for (int i=0; i<srcScriptList.size(); i++){
				result.append( srcScriptList.get(i));
			}
			
			result.append("/ \n\n ");
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
		
		StringBuilder result = new StringBuilder("");
//		result.append("/* DROP TRIGGER " + objectName + "; */ \n\n");
		result.append("CREATE OR REPLACE ");

		List<String> srcScriptList = client.queryForList("getTriggerScript", objectName);				
		for (int i=0; i<srcScriptList.size(); i++){
			result.append( srcScriptList.get(i));
		}
		
		return result.toString();				
	}

	@Override
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("package_name", procedureDAO.getPackagename());
		map.put("object_name", procedureDAO.getName());	
		map.put("overload", procedureDAO.getOverload()+"");	

		if(logger.isDebugEnabled()) {
			logger.debug("\n getProcedureInParamter.package=" + map.get("package_name"));
			logger.debug("\n getProcedureInParamter.object=" + map.get("object_name"));
			logger.debug("\n getProcedureInParamter.overload=" + map.get("overload"));
			logger.debug("\n procedureDAO=" + procedureDAO);
		}
		
		return client.queryForList("getProcedureInParamter", map);
	}

	@Override
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("package_name", procedureDAO.getPackagename());
		map.put("object_name", procedureDAO.getName());	
		map.put("overload", procedureDAO.getOverload()+"");	
		
		return client.queryForList("getProcedureOutParamter", map );
	}

}
