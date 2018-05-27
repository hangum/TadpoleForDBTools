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
package com.hangum.tadpole.engine.sql.util.export.all;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.export.all.inner.AQueryDataExporter;
import com.hangum.tadpole.engine.sql.util.export.all.inner.CSVQueryDataExporter;
import com.hangum.tadpole.engine.sql.util.export.all.inner.ExcelQueryDataExporter;
import com.hangum.tadpole.engine.sql.util.export.all.inner.HtmlQueryDataExporter;
import com.hangum.tadpole.engine.sql.util.export.dto.ExportResultDTO;

/**
 * 쿼리의 감사로그가 남는다.
 * 
 * @author hangum
 *
 */
public class QueryDataExportFactory {

	/**
	 * 
	 * 
	 * @param separator
	 * @param isAddHead
	 * @param fileName
	 * @param fileExtension
	 * @param userDB
	 * @param strSQL
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO createCSV(char separator, boolean isAddHead, String fileName, String fileExtension, UserDBDAO userDB, String strSQL, int intMaxCount) 
			throws Exception {
	
		AQueryDataExporter queryDataExporter = new CSVQueryDataExporter(separator, isAddHead);
		queryDataExporter.makeAllData(fileName, fileExtension, userDB, strSQL, intMaxCount);
		queryDataExporter.close();
		
		return queryDataExporter.getExportDto();
	}
	
	public static ExportResultDTO createExcel(String fileName, String fileExtension, UserDBDAO userDB, String strSQL, int intMaxCount) 
			throws Exception {
	
		AQueryDataExporter queryDataExporter = new ExcelQueryDataExporter();
		queryDataExporter.makeAllData(fileName, fileExtension, userDB, strSQL, intMaxCount);
		queryDataExporter.close();
		
		return queryDataExporter.getExportDto();
	}
	
	public static ExportResultDTO createHTML(String encoding, String fileName, String fileExtension, UserDBDAO userDB, String strSQL, int intMaxCount) 
			throws Exception {
	
		AQueryDataExporter queryDataExporter = new HtmlQueryDataExporter(encoding);
		queryDataExporter.makeAllData(fileName, fileExtension, userDB, strSQL, intMaxCount);
		queryDataExporter.close();
		
		return queryDataExporter.getExportDto();
	}

}
