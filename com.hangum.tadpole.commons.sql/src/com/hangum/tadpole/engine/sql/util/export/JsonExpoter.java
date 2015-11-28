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
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.JSONUtil;
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
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		JsonArray jsonArry = new JsonArray();
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			JsonObject jsonObj = new JsonObject();
			for (int j = 1; j < mapLabelName.size(); j++) {
				String columnName = mapLabelName.get(j);
				
				jsonObj.addProperty(StringUtils.trimToEmpty(columnName), ""+mapColumns.get(j));
			}
			jsonArry.add(jsonObj);
		}
		return JSONUtil.getPretty(jsonArry.toString());
	}
	
	/**
	 * make content file
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 * @throws Exception
	 */
	public static String makeContentFile(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		String strContent = makeContent(tableName, rsDAO);
		
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".json";
		String strFullPath = strTmpDir + strFile;
		
		FileUtils.writeStringToFile(new File(strFullPath), strContent, true);
		
		return strFullPath;
	}
}
