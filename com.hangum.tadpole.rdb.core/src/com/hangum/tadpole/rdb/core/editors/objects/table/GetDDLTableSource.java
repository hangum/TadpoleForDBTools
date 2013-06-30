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
import com.hangum.tadpole.dao.system.UserDBDAO;
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
				result.append("/* DROP TABLE " + objectName + " CASCADE CONSTRAINT */ \n\n");
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
				return ""+client.queryForObject("getViewScript", objectName);
			}
			
			throw new Exception("Not support Database");
		} else {
			throw new Exception("Not Support Database");
		}
	}

	/**
	 * procedure source
	 * 
	 * @param userDB
	 * @param actionType
	 * @param index_name
	 * @param table_name
	 * @return
	 */
	public static String getIndexSource(UserDBDAO userDB, DB_ACTION actionType, String index_name, String table_name) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("index_name", index_name);
		paramMap.put("table_name", table_name);
		
		return ""+client.queryForObject("getIndexScript", paramMap);
	}

}
