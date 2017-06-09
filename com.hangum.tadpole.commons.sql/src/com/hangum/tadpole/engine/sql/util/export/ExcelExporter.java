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
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hangum.tadpole.commons.util.CSVFileUtils;
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
