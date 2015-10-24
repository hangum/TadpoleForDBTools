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
package com.hangum.tadpole.engine.sql.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.CSVFileUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

public class CSVUtil {
	/**
	 * INSERT 문을 생성합니다.
	 * 
	 * @param tableName
	 * @param rs
	 * @return 파일 위치
	 * 
	 * @throws Exception
	 */
	public static String makeCSVFile(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".csv";
		String strFullPath = strTmpDir + strFile;
		
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		
		List<String[]> listCsvData = new ArrayList<String[]>();
		// column .
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		String[] strArrys = new String[mapLabelName.size()-1];
		for(int i=1; i<mapLabelName.size(); i++) {
			strArrys[i-1] = mapLabelName.get(i);
		}
		listCsvData.add(strArrys);
		String strTitle = CSVFileUtils.makeData(listCsvData);
		FileUtils.writeStringToFile(new File(strFullPath), strTitle, true);
		
		listCsvData.clear();
		// data
		int DATA_COUNT = 1000;
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			strArrys = new String[mapColumns.size()-1];
			for(int j=1; j<mapColumns.size(); j++) {
				strArrys[j-1] = ""+mapColumns.get(j); //$NON-NLS-1$
			}
			listCsvData.add(strArrys);
			
			if((i%DATA_COUNT) == 0) {
				FileUtils.writeStringToFile(new File(strFullPath), CSVFileUtils.makeData(listCsvData), true);
				listCsvData.clear();
			}
		}
		
		// 컬럼 이름.
		
		
		if(!listCsvData.isEmpty()) {
			FileUtils.writeStringToFile(new File(strFullPath), CSVFileUtils.makeData(listCsvData), true);
		}
		
		return strFullPath;
	}
}
