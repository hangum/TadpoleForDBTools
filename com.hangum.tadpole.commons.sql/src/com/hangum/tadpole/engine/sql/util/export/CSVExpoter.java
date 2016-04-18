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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.CSVFileUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * CSV 유틸
 * 
 * @author hangum
 *
 */
public class CSVExpoter extends AbstractTDBExporter {
	
	public static String makeContent(boolean isAddHead, String targetName, QueryExecuteResultDTO queryExecuteResultDTO, char seprator) throws Exception {
		return makeContent(isAddHead, targetName, queryExecuteResultDTO, seprator, -1);
	}

	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @param intLimitCnt
	 * @return
	 */
	public static String makeContent(boolean isAddHead, String tableName, QueryExecuteResultDTO rsDAO, char seprator, int intLimitCnt) throws Exception {
		StringBuffer sbReturn = new StringBuffer();
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		List<String[]> listCsvData = new ArrayList<String[]>();
		String[] strArrys = null;
		
		if(isAddHead) {
			// column .
			Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
			strArrys = new String[mapLabelName.size()-1];
			for(int i=1; i<mapLabelName.size(); i++) {
				strArrys[i-1] = mapLabelName.get(i);
			}
			listCsvData.add(strArrys);
			String strTitle = CSVFileUtils.makeData(listCsvData, seprator);
			sbReturn.append(strTitle);
		}
		listCsvData.clear();
		
		// data
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			strArrys = new String[mapColumns.size()-1];
			for(int j=1; j<mapColumns.size(); j++) {
				strArrys[j-1] = mapColumns.get(j) == null?"":""+mapColumns.get(j); //$NON-NLS-1$
			}
			listCsvData.add(strArrys);

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
	 * @return 파일 위치
	 * 
	 * @throws Exception
	 */
	public static String makeCSVFile(boolean isAddHead, String tableName, QueryExecuteResultDTO rsDAO, char seprator) throws Exception {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".csv";
		String strFullPath = strTmpDir + strFile;
		
		// add bom character
//	    ByteArrayOutputStream out = new ByteArrayOutputStream();
//	    //Add BOM characters
//	    out.write(0xEF);
//	    out.write(0xBB);
//	    out.write(0xBF);
//	    out.write(csvData.getBytes("UTF-8"));
		
		FileUtils.writeByteArrayToFile(new File(strFullPath), 
						(new byte[] {(byte) 0xEF,
									  (byte) 0xBB, (byte) 0xBF}), true);
		
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		List<String[]> listCsvData = new ArrayList<String[]>();
		String[] strArrys = null;
		
		if(isAddHead) {
			// column .
			Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
			strArrys = new String[mapLabelName.size()-1];
			for(int i=1; i<mapLabelName.size(); i++) {
				strArrys[i-1] = mapLabelName.get(i);
			}
			listCsvData.add(strArrys);
			String strTitle = CSVFileUtils.makeData(listCsvData, seprator);
			FileUtils.writeStringToFile(new File(strFullPath), strTitle, true);
			
			listCsvData.clear();
		}
		
		// data
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			strArrys = new String[mapColumns.size()-1];
			for(int j=1; j<mapColumns.size(); j++) {
				strArrys[j-1] = mapColumns.get(j) == null?"":""+mapColumns.get(j); //$NON-NLS-1$
			}
			listCsvData.add(strArrys);
			
			if((i%DATA_COUNT) == 0) {
				FileUtils.writeStringToFile(new File(strFullPath), CSVFileUtils.makeData(listCsvData, seprator), true);
				listCsvData.clear();
			}
		}
		
		// 컬럼 이름.
		if(!listCsvData.isEmpty()) {
			FileUtils.writeStringToFile(new File(strFullPath), CSVFileUtils.makeData(listCsvData, seprator), true);
		}
		
		return strFullPath;
	}
}
