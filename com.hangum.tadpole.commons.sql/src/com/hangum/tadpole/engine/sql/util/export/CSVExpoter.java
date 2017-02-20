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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.hangum.tadpole.commons.util.CSVFileUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * CSV 유틸
 * 
 * @author hangum
 *
 */
public class CSVExpoter extends AbstractTDBExporter {
	
	public static String makeContent(boolean isAddHead, String targetName, QueryExecuteResultDTO queryExecuteResultDTO, char seprator, String strDefaultNullValue) throws Exception {
		return makeContent(isAddHead, targetName, queryExecuteResultDTO, seprator, -1, strDefaultNullValue);
	}

	/**
	 * make file header 
	 * 
	 * @param strFullPath
	 * @param isAddHead
	 * @param rsDAO
	 * @param seprator
	 * @param encoding
	 * @throws Exception
	 */
	public static void makeHeaderFile(String strFullPath, boolean isAddHead, QueryExecuteResultDTO rsDAO, char seprator, String encoding) throws Exception {
		// make language tag
		FileUtils.writeByteArrayToFile(new File(strFullPath), 
						(new byte[] {(byte) 0xEF,
									  (byte) 0xBB, (byte) 0xBF}), true);
		
		// make header
		FileUtils.writeStringToFile(new File(strFullPath), makeHeader(isAddHead, rsDAO, seprator), encoding, true);
	}
	
	/**
	 * make csv header
	 * 
	 * @param isAddHead
	 * @param rsDAO
	 * @param seprator
	 * @return
	 * @throws Exception
	 */
	public static String makeHeader(boolean isAddHead, QueryExecuteResultDTO rsDAO, char seprator) throws Exception {
		List<String[]> listCsvData = new ArrayList<String[]>();
		
		if(isAddHead) {
			// column .
			Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
			List<String> listLabel = new ArrayList<>();
			
			for(int i=0; i<mapLabelName.size(); i++) {
				String strLabelName = mapLabelName.get(i);
				if(!SQLUtil.isTDBSpecialColumn(strLabelName)) {
					listLabel.add(strLabelName);
				}
			}
			listCsvData.add(listLabel.toArray(new String[listLabel.size()]));
		}
		
		return CSVFileUtils.makeData(listCsvData, seprator);
	}

	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @param intLimitCnt
	 * @return
	 */
	public static String makeContent(boolean isAddHead, String tableName, QueryExecuteResultDTO rsDAO, char seprator, int intLimitCnt, String strDefaultNullValue) throws Exception {

		// make header
		StringBuffer sbReturn = new StringBuffer();
		sbReturn.append(makeHeader(isAddHead, rsDAO, seprator));

		// data
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		List<String[]> listCsvData = new ArrayList<String[]>();
		List<String> listLabel = new ArrayList<>();
		
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			listLabel.clear();
			for(int j=1; j<mapColumns.size(); j++) {
				listLabel.add(mapColumns.get(j) == null?strDefaultNullValue:""+mapColumns.get(j));
			}
			listCsvData.add(listLabel.toArray(new String[listLabel.size()]));

			sbReturn.append(CSVFileUtils.makeData(listCsvData, seprator));
			listCsvData.clear();
			if (intLimitCnt == i) break;
		}
		
		return sbReturn.toString();
	}

	/**
	 * csv 파일을 생성하여 파일 위치를 넘겨줍니다.
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @param seprator
	 * @param encoding
	 * @return 파일 위치
	 * @return strDefaultNullValue
	 * 
	 * @throws Exception
	 */
	public static void makeContentFile(String strFullPath, boolean isAddHead, QueryExecuteResultDTO rsDAO, char seprator, String encoding, String strDefaultNullValue) throws Exception {
		// data
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		List<String[]> listCsvData = new ArrayList<String[]>();
		List<String> listValues = new ArrayList<>();

		// column name
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			listValues.clear();
			for(int j=0; j<mapColumns.size(); j++) {
				
				// tdb 내부적으로 사용하는 컬럼을 보이지 않도록 합니다.
				if(!SQLUtil.isTDBSpecialColumn(mapLabelName.get(j))) {
					listValues.add(mapColumns.get(j) == null?strDefaultNullValue:""+mapColumns.get(j));
				}
			}
			listCsvData.add(listValues.toArray(new String[listValues.size()]));
		}
		
		FileUtils.writeStringToFile(new File(strFullPath), CSVFileUtils.makeData(listCsvData, seprator), encoding, true);
	}
	
}
