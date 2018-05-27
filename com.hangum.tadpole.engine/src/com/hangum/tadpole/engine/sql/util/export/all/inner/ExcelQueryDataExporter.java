/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.export.all.inner;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tadpole.common.define.core.define.PublicTadpoleDefine.EXPORT_METHOD;

/**
 * excel query data exporter
 * 
 * @author hangum
 *
 */
public class ExcelQueryDataExporter extends AQueryDataExporter {
	private static final Logger logger = Logger.getLogger(ExcelQueryDataExporter.class);
	private XSSFWorkbook workbookExcel = null;
	private XSSFSheet sheetExcel = null;
	
	private List<String[]> listResultData = new ArrayList<String[]>();
	
	public ExcelQueryDataExporter() {
	}
	
	@Override
	public void init(String fileName, String fileExtension) throws Exception {
		super.init(fileName, fileExtension);
		exportDto.setExportMethod(EXPORT_METHOD.EXCEL);		
		workbookExcel = new XSSFWorkbook();
		sheetExcel = workbookExcel.createSheet(fileName);
	}

	@Override
	public void makeHead(int intColumnCnt, ResultSetMetaData rsm) throws Exception {
		String[] strArryData = new String[intColumnCnt];
		for(int i=0; i<intColumnCnt; i++) {
			strArryData[i] = rsm.getColumnLabel(i+1);
		}
		listResultData.add(strArryData);
	}

	@Override
	public void makeBody(int intRowCnt, String[] strArryData) throws Exception {
		listResultData.add(strArryData);
		
		if(((intRowCnt+1) % PER_DATA_SAVE_COUNT) == 0) {
			_makeExcelFile();
			listResultData.clear();
			
			if(logger.isDebugEnabled()) logger.debug("===processes =============>" + intRowCnt);
			try { Thread.sleep(THREAD_SLEEP_MILLIS); } catch(Exception e) {}
		}
	}

	@Override
	public void makeTail() throws Exception {
		if(!listResultData.isEmpty()) {
			_makeExcelFile();
			listResultData.clear();
		}
	}
	
	/**
	 * excel file
	 */
	private void _makeExcelFile() {
		int rowNum = sheetExcel.getLastRowNum()+1;
		for(int i=0; i<listResultData.size(); i++) {
			Row row = sheetExcel.createRow(rowNum+i);
			
			String[] arryData = listResultData.get(i);
			for(int j=0; j<arryData.length; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(arryData[j]);
			}
		}
	}

	@Override
	public void close() throws Exception {
		workbookExcel.write(new FileOutputStream(new File(exportDto.getFileFullName()), true));
		workbookExcel.close();
	}

}
