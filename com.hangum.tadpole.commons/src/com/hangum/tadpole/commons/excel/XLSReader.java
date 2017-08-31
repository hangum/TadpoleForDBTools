/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     jeongjaehong - POI라이브러리를 이용하여 엑셀파일 가져오기 기능 구현.
 ******************************************************************************/
package com.hangum.tadpole.commons.excel;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Excel file Reader
 * 
 * @author jeongjaehong
 *
 */
public class XLSReader {

	private FileInputStream excelStream = null; 
	private Workbook workbook = null; // 엑셀파일
	private Sheet sheet = null; // 기본 작업대상 sheet
	private Iterator<Row> iterator = null; // 전체 행

	public XLSReader(final File xlsFile, final String targetSheet) {
		try {
			excelStream = new FileInputStream(xlsFile);
			// 엑셀파일의 버젼에 따라 처리 방법이 조금씩 다른듯, xls, xlsx 등 테스트 필요함.
			// workbook = new XSSFWorkbook(excelStream);
			workbook = WorkbookFactory.create(excelStream);
			
			if ("".equals(targetSheet)) {
				// 첫번째 sheet를 기본 작업대상으로 설정한다.
				sheet = workbook.getSheetAt(0);
			} else {
				sheet = workbook.getSheet(targetSheet);
			}
			iterator = sheet.iterator();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 현재 선택된 sheet명을 리턴하여 dialog에 기본값 선택에 사용한다.
	 * @return
	 */
	public String getActiveSheetName() {
		if (sheet == null) {
			return null;
		} else {
			return sheet.getSheetName();
		}
	}

	/**
	 * 업로드된 엑셀 파일 내부의 전체 sheet목록을 린턴한다.
	 * @return
	 */
	public List<String> getSheetNames() {
		List<String> sheetNames = null;

		if (workbook != null) {
			sheetNames = new ArrayList<String>();
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				sheetNames.add(workbook.getSheetName(i));
			}
		}
		return sheetNames;
	}

	/**
	 * 현재 선택된 sheet를 대상으로 첫번째 행부터 순차적으로 읽어서 리턴한다.
	 * @return
	 */
	public String[] readNext() {
		ArrayList<String> list = new ArrayList<String>();
		String[] result = null;
		try {
			if (iterator.hasNext()) {

				Row row = iterator.next();
				Iterator<Cell> cell = row.iterator();

				while (cell.hasNext()) {
					try {
						// 각셀의 형식에 따라 값을읽어 문자열로 변환 후 list에 담는다.
						Cell currentCell = cell.next();
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							list.add(currentCell.getStringCellValue());
						} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							list.add(BigDecimal.valueOf(currentCell.getNumericCellValue()).toPlainString());
						} else if (currentCell.getCellTypeEnum() == CellType._NONE
								|| currentCell.getCellTypeEnum() == CellType.BLANK) {
							list.add("");
						} else if (currentCell.getCellTypeEnum() == CellType.FORMULA) {
							list.add(currentCell.getCellFormula());
						} else if (currentCell.getCellTypeEnum() == CellType.BOOLEAN) {
							list.add(Boolean.valueOf(currentCell.getBooleanCellValue()).toString());
						} else if (currentCell.getCellTypeEnum() == CellType.ERROR) {
							list.add(currentCell.getErrorCellValue() + "");
						}
					} catch (Exception e) {
						list.add("");
					}
				}
			}

			// 읽어진 하나의 행을 각 셀단위로 String배열에 담는다.
			if (list.size() > 0) {
				result = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					result[i] = list.get(i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
}
