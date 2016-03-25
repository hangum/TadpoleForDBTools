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
 * MSSQL V8.0 이하 버전의 DDL Script를 관리합니다.
 * 
 * @author hangum
 *
 */
public class MSSQL_8_LE_DDLScript extends AbstractRDBDDLScript {
	private static final Logger logger = Logger.getLogger(MSSQL_8_LE_DDLScript.class);
	
	/**
	 * @param userDB
	 * @param actionType
	 */
	public MSSQL_8_LE_DDLScript(UserDBDAO userDB, OBJECT_TYPE actionType) {
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
								
			if (source.get("DATA_PRECISION") != null && ((Integer)source.get("DATA_PRECISION")).intValue() > 0 ){
				result.append("("+source.get("DATA_PRECISION"));
				if (source.get("DATA_SCALE") != null && ((Integer)source.get("DATA_SCALE")).intValue() > 0 ){
					result.append(","+source.get("DATA_SCALE"));
				}
						
				result.append(")");
			}else if (!StringUtils.contains((String)source.get("DATA_TYPE"), "DATE") && !StringUtils.contains((String)source.get("DATA_TYPE"), "NUMBER")  && ((Integer)source.get("DATA_LENGTH")).intValue() > 0  ){
				result.append("("+source.get("DATA_LENGTH")+")");
			}else{
				result.append(" ");
			}
			
			if (source.get("DATA_DEFAULT") != null ){
			
				if (StringUtils.contains((String)source.get("DATA_TYPE"), "CHAR")  ){
					result.append(" DEFAULT '"+(String)source.get("DATA_DEFAULT")+"'");
				}else{
					result.append(" DEFAULT "+(String)source.get("DATA_DEFAULT") );
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
				result.append("\t,CONSTRAINT ").append(source.get("CONSTRAINT_NAME")).append(" PRIMARY KEY ");
				if ("CLUSTERED".equals(source.get("INDEX_TYPE")) ) {
					result.append(" CLUSTERED ");
				}
				result.append(" ( ").append(source.get("COLUMN_NAME"));
				
				if ((Boolean)source.get("DESCENDING")){
					result.append(" DESC ");
				}
			}else{
				result.append(", "+source.get("COLUMN_NAME"));
				if ((Boolean)source.get("DESCENDING")){
					result.append(" DESC ");
				}
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

		List<String> srcProcList = client.queryForList("getViewScript", strName);				
		for (int i=0; i<srcProcList.size(); i++){
			result.append(srcProcList.get(i));
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
//		result.append("/* DROP FUNCTION " + functionDAO.getName() + "; */ \n\n");

		List<String> srcProcList = client.queryForList("getFunctionScript", functionDAO.getName());				
		for (int i=0; i<srcProcList.size(); i++){
			result.append(srcProcList.get(i));
		}
		
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO)	throws Exception {
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
			StringBuilder result = new StringBuilder("");
//			result.append("/* DROP PROCEDURE '" + procedureDAO.getName() + "'; */ \n\n");
	
			List<String> srcProcList = client.queryForList("getProcedureScript", procedureDAO.getName());				
			for (int i=0; i<srcProcList.size(); i++){
				result.append(srcProcList.get(i));
			}
			
			return result.toString();
		} catch(Exception e) {
			logger.error("get procedure script [" + procedureDAO.getName() + "]", e);
			
			throw e;
		}
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

	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getTriggerScript(com.hangum.tadpole.dao.mysql.TriggerDAO)
	 */
	@Override
	public String getTriggerScript(TriggerDAO triggerDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		StringBuilder result = new StringBuilder("");
//		result.append("/* DROP PROCEDURE " + triggerDAO.getName() + "; */ \n\n");

		List<String> srcProcList = client.queryForList("getTriggerScript", triggerDAO.getName());				
		for (int i=0; i<srcProcList.size(); i++){
			result.append(srcProcList.get(i));
		}
		
		return result.toString();
	}

}
