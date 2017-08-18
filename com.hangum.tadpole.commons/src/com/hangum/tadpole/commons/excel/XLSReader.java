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

public class XLSReader {

	private FileInputStream excelStream = null;
	private Workbook workbook = null;
	private Sheet sheet = null;
	private Iterator<Row> iterator = null; // 전체 행

	public XLSReader(final File xlsFile, final String targetSheet) {
		try {
			excelStream = new FileInputStream(xlsFile);
			// 엑셀파일의 버젼에 따라 처리 방법이 조금씩 다른듯, xls, xlsx 등 테스트 필요함.
			// workbook = new XSSFWorkbook(excelStream);
			workbook = WorkbookFactory.create(excelStream);
			// 첫번째 sheet만 처리함 ui에서 sheet를 선택하도록 하는게 필요해 보임.
			if ("".equals(targetSheet)) {
				//workbook.setActiveSheet(0);
				sheet = workbook.getSheetAt(0);
			} else {
				//workbook.setActiveSheet(workbook.getSheetIndex(targetSheet));
				sheet = workbook.getSheet(targetSheet);
			}
			iterator = sheet.iterator();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getActiveSheetName() {
		if (sheet == null) {
			return null;
		} else {
			return sheet.getSheetName();
		}
	}

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

	public String[] readNext() {
		ArrayList<String> list = new ArrayList<String>();
		String[] result = null;
		try {
			if (iterator.hasNext()) {

				Row row = iterator.next();
				Iterator<Cell> cell = row.iterator();

				while (cell.hasNext()) {
					try {
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
