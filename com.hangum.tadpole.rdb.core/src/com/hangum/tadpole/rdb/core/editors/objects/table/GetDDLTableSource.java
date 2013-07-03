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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TriggerDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * DDL Source
 * 
 * @author hangum
 *
 */
public class GetDDLTableSource {

	private static final Logger logger = Logger.getLogger(GetDDLTableSource.class);
	/**
	 * DDL Source view
	 * 
	 * 	Table, View 소스.
	 * 
	 * @param userDB
	 * @param actionType
	 * @param objectName
	 */
	public static String getSource(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType, String objectName) throws Exception {
		
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
			if(PublicTadpoleDefine.DB_ACTION.TABLES == actionType) {
				return ""+client.queryForObject("getTableScript", objectName);
			} else if(PublicTadpoleDefine.DB_ACTION.VIEWS == actionType) {
				return ""+client.queryForObject("getViewScript", objectName);
			}
			
			throw new Exception("Not support Database");
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
			if(PublicTadpoleDefine.DB_ACTION.TABLES == actionType) {
				List<HashMap> srcList = client.queryForList("getTableScript", objectName);
				
				
				StringBuilder result = new StringBuilder("");
				result.append("/* DROP TABLE " + objectName + " CASCADE CONSTRAINT; */ \n\n");
				result.append("CREATE TABLE " + objectName + "( \n");
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
						
					if("N".equals(source.get("NULLABLE"))){
						result.append(" NOT NULL ");
					}
					
					result.append("\n");
					
				}

				// primary key 
				List<HashMap> srcPkList = client.queryForList("getTableScript.pk", objectName);				
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
				List<String> srcCommentList = client.queryForList("getTableScript.comments", objectName);				
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
				
			} else if(PublicTadpoleDefine.DB_ACTION.VIEWS == actionType) {
				//return ""+client.queryForObject("getViewScript", objectName);
				
				
				StringBuilder result = new StringBuilder("");
				result.append("/* DROP VIEW " + objectName + "; */ \n\n");

				List<String> srcViewHeadList = client.queryForList("getViewScript.head", objectName);				
				for (int i=0; i<srcViewHeadList.size(); i++){
					result.append( srcViewHeadList.get(i)+"\n");
				}
				List<String> srcViewBodyList = client.queryForList("getViewScript.body", objectName);				
				for (int i=0; i<srcViewBodyList.size(); i++){
					result.append( srcViewBodyList.get(i)+"\n");
				}
				
				return result.toString();				
			} 
			
			throw new Exception("Not support Database");
		} else {
			throw new Exception("Not Support Database");
		}
	}

	/**
	 * DDL Source view
	 * 
	 * 	Trigger 소스.
	 * 
	 * @param userDB
	 * @param actionType
	 * @param objectName
	 */
	public static String getTriggerSource(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType, TriggerDAO triggerDAO) throws Exception {
		
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			
			return triggerDAO.getStatement();
			
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			String objectName = triggerDAO.getTrigger();
			
	        if(PublicTadpoleDefine.DB_ACTION.TRIGGERS == actionType) {

				logger.debug("\n Trigger DDL Generation...");
				
				StringBuilder result = new StringBuilder("");
				result.append("/* DROP TRIGGER " + objectName + "; */ \n\n");
				result.append("CREATE OR REPLACE ");

				List<String> srcScriptList = client.queryForList("getTriggerScript", objectName);				
				for (int i=0; i<srcScriptList.size(); i++){
					result.append( srcScriptList.get(i));
				}
				
				return result.toString();				
			}
			
			throw new Exception("Not support Database");
		} else {
			throw new Exception("Not Support Database");
		}
	}
	
	
	/**
	 * DDL Source view
	 * 
	 * 	Function 소스.
	 * 
	 * @param userDB
	 * @param actionType
	 * @param objectName
	 */
	public static String getFunctionSource(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType, String  objectName) throws Exception {
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			
			throw new Exception("Not support Database");
			
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
	        if(PublicTadpoleDefine.DB_ACTION.FUNCTIONS == actionType) {

				logger.debug("\n Function DDL Generation...");
				
				StringBuilder result = new StringBuilder("");
				result.append("/* DROP FUNCTION " + objectName + "; */ \n\n");
				result.append("CREATE OR REPLACE ");

				List<String> srcScriptList = client.queryForList("getFunctionScript", objectName);				
				for (int i=0; i<srcScriptList.size(); i++){
					result.append( srcScriptList.get(i));
				}
				
				return result.toString();				
			}
			
			throw new Exception("Not support Database");
		} else {
			throw new Exception("Not Support Database");
		}
	}
	
		
	/**
	 * DDL Source view
	 * 
	 * 	Procedure 소스.
	 * 
	 * @param userDB
	 * @param actionType
	 * @param objectName
	 */
	public static String getProcedureSource(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType, String objectName) throws Exception {
		
		
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			
			
			throw new Exception("Not support Database");
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
	        if(PublicTadpoleDefine.DB_ACTION.PROCEDURES == actionType) {

				logger.debug("\n Procedure DDL Generation...");
				
				StringBuilder result = new StringBuilder("");
				
				
				String objType = (String)client.queryForObject("getSourceObjectType", objectName);				
							
				List<String> srcScriptList = null;
				if (StringUtils.contains(objType, "PROCEDURE")){
					result.append("/* DROP PROCEDURE " + objectName + "; */ \n\n");
					result.append("CREATE OR REPLACE ");
					srcScriptList = client.queryForList("getProcedureScript", objectName);				
					for (int i=0; i<srcScriptList.size(); i++){
						result.append( srcScriptList.get(i));
					}
				}else if (StringUtils.contains(objType, "PACKAGE")){
					result.append("/* DROP PACKAGE BODY " + objectName + "; */ \n\n");
					result.append("/* DROP PACKAGE " + objectName + "; */ \n\n");
					
					result.append("CREATE OR REPLACE ");
					srcScriptList = client.queryForList("getPackageScript.head", objectName);				
					for (int i=0; i<srcScriptList.size(); i++){
						result.append( srcScriptList.get(i));
					}
					result.append(PublicTadpoleDefine.SQL_DILIMITER + "\n");
					result.append("/ \n\n ");
					result.append("CREATE OR REPLACE ");
					srcScriptList = client.queryForList("getPackageScript.body", objectName);				
					for (int i=0; i<srcScriptList.size(); i++){
						result.append( srcScriptList.get(i));
					}
					
					result.append(PublicTadpoleDefine.SQL_DILIMITER);
					result.append("/ \n\n ");
				}
				
				
				return result.toString();				
			} 
			
			throw new Exception("Not support Database");
		} else {
			throw new Exception("Not Support Database");
		}
	}
	
	/**
	 * index source
	 * 
	 * @param userDB
	 * @param actionType
	 * @param index_name
	 * @param table_name
	 * @return
	 */
	public static String getIndexSource(UserDBDAO userDB, DB_ACTION actionType, String index_name, String table_name) throws Exception {
		if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.SQLite_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("index_name", index_name);
			paramMap.put("table_name", table_name);
			
			return ""+client.queryForObject("getIndexScript", paramMap);
				
		} else if(DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			
			StringBuilder result = new StringBuilder("");	
			
							
			List<Map<String, String>> srcScriptList = (List<Map<String, String>>) client.queryForList("getIndexScript", index_name);
			
			result.append("/* DROP INDEX " + index_name + "; */ \n\n");
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
				
				return result.toString();
			}
			
			throw new Exception("Not support Database");
		}else{
			throw new Exception("Not support Database");
		}
	}

}
