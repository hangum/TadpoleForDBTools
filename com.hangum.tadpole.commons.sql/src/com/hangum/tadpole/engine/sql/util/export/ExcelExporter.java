/*******************************************************************************
 * Copyright (c) 2017 hangum.
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * excel exporter
 * 
 * @author hangum
 *
 */
public class ExcelExporter extends AbstractTDBExporter {
	
	/**
	 * 엑셀파일을 만든다.
	 * 
	 * @param strFullFileName
	 * @param strSheetName
	 * @param listData
	 * @return
	 * @throws Exception
	 */
	public static String makeFile(String strFullFileName, String strSheetName, List<String[]> listData) throws Exception {
		XSSFWorkbook workbookExcel = null;
		XSSFSheet sheetExcel = null;
		int rowNum = 0;
		
		File fileXlsx = new File(strFullFileName);
		if(fileXlsx.exists()) {
			workbookExcel = new XSSFWorkbook(new FileInputStream(fileXlsx));
			sheetExcel = workbookExcel.getSheet(strSheetName);
			
			rowNum = sheetExcel.getLastRowNum()+1;
		} else {
			workbookExcel = new XSSFWorkbook();
			sheetExcel = workbookExcel.createSheet(strSheetName);
		}
		
		for(int i=0; i<listData.size(); i++) {
			Row row = sheetExcel.createRow(rowNum+i);
			
			String[] arryData = listData.get(i);
			for(int j=0; j<arryData.length; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(arryData[j]);
			}
		}
		
		workbookExcel.write(new FileOutputStream(new File(strFullFileName), true));
		workbookExcel.close();
		
		return strFullFileName;
	}
	
	/**
	 * make content file
	 * 
	 * @param strTmpFile
	 * @param strSheetName
	 * @param listData
	 * @return
	 * @throws Exception
	 */
	public static String makeContentFile(String strTmpFile, String strSheetName, List<String[]> listData) throws Exception {
		String strFullFileName = makeDirName(strTmpFile) + strTmpFile + "." + "xlsx";;
		return makeFile(strFullFileName, strSheetName, listData);
	}

	/**
	 * make content file
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 * @throws Exception
	 */
	public static String makeContentFile(String strFullPath, String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		XSSFWorkbook workbookExcel = null;
		XSSFSheet sheetExcel = null;
		int rowNum = 0;
		int colNum = 0;
		
		File fileXlsx = new File(strFullPath);
		if(fileXlsx.exists()) {
			workbookExcel = new XSSFWorkbook(new FileInputStream(fileXlsx));
			sheetExcel = workbookExcel.getSheet(tableName);
			
			rowNum = sheetExcel.getLastRowNum()+1;
		} else {
			workbookExcel = new XSSFWorkbook();
			sheetExcel = workbookExcel.createSheet(tableName);
			
			// header
			Row row = sheetExcel.createRow(rowNum++);
			Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
			for(int i=0; i<mapLabelName.size(); i++) {
				String strLabelName = mapLabelName.get(i);
				if(!SQLUtil.isTDBSpecialColumn(strLabelName)) {
					Cell cell = row.createCell(colNum++);
					cell.setCellValue(strLabelName);
				}
			}
		}

		final Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		final List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		final Map<Integer, Integer> mapColumnType = rsDAO.getColumnType();
		
		for(int i=0; i<dataList.size(); i++) {
			Row row = sheetExcel.createRow(rowNum++);
			colNum = 0;
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			for(int j=0; j<mapColumns.size(); j++) {
				// tdb 내부적으로 사용하는 컬럼을 보이지 않도록 합니다.
				if(!SQLUtil.isTDBSpecialColumn(mapLabelName.get(j))) {
					Cell cell = row.createCell(colNum++);
					int _intColumnType = mapColumnType.get(j);
					if(RDBTypeToJavaTypeUtils.isNumberType(_intColumnType)) {
						if(mapColumns.get(j) != null) cell.setCellValue(Double.parseDouble(""+mapColumns.get(j)));
						else cell.setCellValue("");
					} else {
						if(mapColumns.get(j) != null) cell.setCellValue(""+mapColumns.get(j)); 
						else cell.setCellValue("");	
					}
					
				}	// is showed
			}	// column size
		}	// data size
		
		workbookExcel.write(new FileOutputStream(new File(strFullPath)));
		workbookExcel.close();
		
		return strFullPath;
	}
}
