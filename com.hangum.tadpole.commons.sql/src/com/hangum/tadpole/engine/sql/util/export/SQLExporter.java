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
import org.apache.commons.lang.StringEscapeUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.StringHelper;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * SQL exporter
 * 
 * @author hangum
 *
 */
public class SQLExporter extends AbstractTDBExporter {
//	/**
//	 * UPDATE 문을 생성합니다.
//	 * 
//	 * @param tableName
//	 * @param rs
//	 * @return 파일 위치
//	 * 
//	 * @throws Exception
//	 */
//	public static String makeFileUpdateStatment(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
//		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
//		String strFile = tableName + ".sql";
//		String strFullPath = strTmpDir + strFile;
//		
//		final String INSERT_INTO_STMT = "UPDATE " + tableName + " SET %s WHERE %s;" + PublicTadpoleDefine.LINE_SEPARATOR; 		
//		Map<Integer, String> mapTable = rsDAO.getColumnLabelName();
//		
//		// 데이터를 담는다.
//		StringBuffer sbInsertInto = new StringBuffer();
//		int DATA_COUNT = 1000;
//		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
//		Map<Integer, Integer> mapColumnType = rsDAO.getColumnType();
//		String strSetStatement = new String();		
//		for(int i=0; i<dataList.size(); i++) {
//			Map<Integer, Object> mapColumns = dataList.get(i);
//			
//			strSetStatement = "";
//			for(int j=1; j<mapColumns.size(); j++) {
//				Object strValue = mapColumns.get(j);
//				strValue = strValue == null?"":strValue;
//				if(!RDBTypeToJavaTypeUtils.isNumberType(mapColumnType.get(j))) {
//					strValue = StringEscapeUtils.escapeSql(strValue.toString());
//					strValue = StringHelper.escapeSQL(strValue.toString());
//					strValue = SQLUtil.makeQuote(strValue.toString());
//				}
//				
//				if(j != (mapTable.size()-1)) strSetStatement += mapTable.get(j) + "=" + strValue + ",";
//				else strSetStatement +=  mapTable.get(j) + "=" + strValue;
//			}
//			
//			String strHwere = "";
//			sbInsertInto.append(String.format(INSERT_INTO_STMT, strSetStatement, strHwere));
//			
//			if((i%DATA_COUNT) == 0) {
//				FileUtils.writeStringToFile(new File(strFullPath), sbInsertInto.toString(), true);
//				sbInsertInto.setLength(0);
//			}
//		}
//		if(sbInsertInto.length() > 0) {
//			FileUtils.writeStringToFile(new File(strFullPath), sbInsertInto.toString(), true);
//		}
//		
//		return strFullPath;
//	}
	
	/**
	 * INSERT 문을 생성합니다.
	 * 
	 * @param tableName
	 * @param rs
	 * @return 파일 위치
	 * 
	 * @throws Exception
	 */
	public static String makeFileInsertStatment(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + PublicTadpoleDefine.DIR_SEPARATOR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".sql";
		String strFullPath = strTmpDir + strFile;
		
		final String INSERT_INTO_STMT = "INSERT INTO " + tableName + " (%s) VALUES (%S);" + PublicTadpoleDefine.LINE_SEPARATOR; 
		
		// 컬럼 이름.
		String strColumns = "";
		Map<Integer, String> mapTable = rsDAO.getColumnLabelName();
		for( int i=1 ;i<mapTable.size(); i++ ) {
			if(i != (mapTable.size()-1)) strColumns += mapTable.get(i) + ",";
			else strColumns += mapTable.get(i);
		}
		
		// 데이터를 담는다.
		StringBuffer sbInsertInto = new StringBuffer();
		int DATA_COUNT = 1000;
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		Map<Integer, Integer> mapColumnType = rsDAO.getColumnType();
		String strResult = new String();		
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			strResult = "";
			for(int j=1; j<mapColumns.size(); j++) {
				Object strValue = mapColumns.get(j);
				strValue = strValue == null?"":strValue;
				if(!RDBTypeToJavaTypeUtils.isNumberType(mapColumnType.get(j))) {
					strValue = StringEscapeUtils.escapeSql(strValue.toString());
					strValue = StringHelper.escapeSQL(strValue.toString());
					
					strValue = SQLUtil.makeQuote(strValue.toString());
				}
				
				if(j != (mapTable.size()-1)) strResult += strValue + ",";
				else strResult += strValue;
			}
			sbInsertInto.append(String.format(INSERT_INTO_STMT, strColumns, strResult));
			
			if((i%DATA_COUNT) == 0) {
				FileUtils.writeStringToFile(new File(strFullPath), sbInsertInto.toString(), true);
				sbInsertInto.setLength(0);
			}
		}
		if(sbInsertInto.length() > 0) {
			FileUtils.writeStringToFile(new File(strFullPath), sbInsertInto.toString(), true);
		}
		
		return strFullPath;
	}
}
