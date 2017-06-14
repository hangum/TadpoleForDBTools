/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.export;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * json expoter
 * 
 * @author hangum
 *
 */
public class JsonExpoter extends AbstractTDBExporter {
	
	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 */
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO) {
			return JSONUtil.getPretty( makeContentArray(tableName, rsDAO, -1).toString() );
	}
	
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO, boolean isFormat, int intLimitCnt) {
		if (isFormat){
			return JSONUtil.getPretty( makeContentArray(tableName, rsDAO, intLimitCnt).toString());
		}else{
			return makeContentArray(tableName, rsDAO, intLimitCnt).toString();
		}
	}
	
	public static JsonArray makeContentArray(String tableName, QueryExecuteResultDTO rsDAO, int intLimitCnt) {
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		JsonArray jsonArry = new JsonArray();
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			JsonObject jsonObj = new JsonObject();
			for (int j = 0; j < mapLabelName.size(); j++) {
				String columnName = mapLabelName.get(j);
				if(!SQLUtil.isTDBSpecialColumn(columnName)) {
					String strValue = ""+mapColumns.get(j);
					if(mapColumns.get(j) == null) strValue = "";
				
					jsonObj.addProperty(StringUtils.trimToEmpty(columnName), strValue);
				}
			}
			jsonArry.add(jsonObj);
			if(i == intLimitCnt) break;
		}
		return jsonArry;
	}
	
	public static String makeHeadContent(String tableName, QueryExecuteResultDTO rsDAO, String schemeKey, String recordKey) throws SQLException {
		return makeHeadContent( tableName,  rsDAO,  schemeKey,  recordKey, true, -1);
	}
	
	public static String makeHeadContent(String tableName, QueryExecuteResultDTO rsDAO, String schemeKey, String recordKey, boolean isFormat, int intLimitCnt) throws SQLException {
		
		JsonObject jsonObj = new JsonObject();
		
		jsonObj.add(schemeKey, makeHead(rsDAO));
		jsonObj.add(recordKey, makeContentArray(tableName, rsDAO, intLimitCnt));		
		
		if(isFormat){
			return JSONUtil.getPretty(jsonObj.toString());
		}else{
			return jsonObj.toString();
		}
	}

	public static JsonArray makeHead(QueryExecuteResultDTO rsDAO) throws SQLException {
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
			
		JsonArray jsonMetaArry = new JsonArray();
			for (int j = 0; j < mapLabelName.size(); j++) {
				String strLabelName = mapLabelName.get(j);
				if(!SQLUtil.isTDBSpecialColumn(strLabelName)) {
					JsonObject jsonMetaObj = new JsonObject();
					jsonMetaObj.addProperty("position", j);
					jsonMetaObj.addProperty("column_name", mapLabelName.get(j));
	//				jsonMetaObj.addProperty("data_type", rsm.getColumnTypeName(j));
					jsonMetaObj.addProperty("data_type", rsDAO.getColumnType().get(j));
	//				jsonMetaObj.addProperty("column_size", rsm.getColumnDisplaySize(j));
					
					if(!RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(j))) {
						jsonMetaObj.addProperty("column_size", 90);
					}else{
						jsonMetaObj.addProperty("column_size", 150);
					}
		
					jsonMetaArry.add(jsonMetaObj);
				}
			}
	
		return	jsonMetaArry;
		
	}
	
	public static String makeContentFile(String tableName, QueryExecuteResultDTO rsDAO, boolean isFormat, String encoding) throws Exception {
		String strFullPath = makeFileName(tableName, "json");
		String strContent = makeContent(tableName, rsDAO, isFormat, -1);
		
		FileUtils.writeStringToFile(new File(strFullPath), strContent, encoding, true);
		
		return strFullPath;
	}
	
	public static String makeContentFile(String tableName, QueryExecuteResultDTO rsDAO, String schemeKey, String recordKey, boolean isFormat, String encoding) throws Exception {
		String strFullPath = makeFileName(tableName, "json");
		String strContent = makeHeadContent(tableName, rsDAO, schemeKey, recordKey, isFormat, -1);
		
		FileUtils.writeStringToFile(new File(strFullPath), strContent, encoding, true);
		
		return strFullPath;
	}
	
}
