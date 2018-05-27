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
package com.hangum.tadpole.engine.sql.util.export.all;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLQueryUtil;
import com.hangum.tadpole.engine.sql.util.export.AbstractTDBExporter;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.export.SQLExporter;
import com.hangum.tadpole.engine.sql.util.export.XMLExporter;
import com.hangum.tadpole.engine.sql.util.export.dto.ExportResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.EXPORT_METHOD;

/**
 * Make all export data
 * 
 * 에디터에서 데이터 내보내기는 전체를 받아 내보내는 식으로 수정합니다. (cpu, 메모리의 염려가 있습니다) 
 * 
 * @author hangum
 *
 */
public class AllDataExporter {
	private static final Logger logger = Logger.getLogger(AllDataExporter.class);

	/**
	 * json add haded data
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param fileName
	 * @param schemeKey
	 * @param recordKey
	 * @param isFormat
	 * @param encoding
	 * @param strDefaultNullValue
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeJSONHeadAllResult(UserDBDAO userDB, String strSQL, String fileName, String schemeKey,
			String recordKey, boolean isFormat, String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.JSON);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL, intMaxCount);
		exportDto.setFileName(fileName);
		exportDto.setFileFullName(JsonExpoter.makeContentFile(fileName, allResusltDto, schemeKey, recordKey, isFormat, encoding));
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}

	/**
	 * json data
	 * @param userDB
	 * @param strSQL
	 * @param targetName
	 * @param isFormat
	 * @param encoding
	 * @param strDefaultNullValue
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeJSONAllResult(UserDBDAO userDB, String strSQL, String targetName, boolean isFormat,
			String encoding, String strDefaultNullValue, int intMaxCount)  throws Exception {
		
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.JSON);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL, intMaxCount);
		exportDto.setFileName(targetName);
		exportDto.setFileFullName(JsonExpoter.makeContentFile(targetName, allResusltDto, isFormat, encoding));
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}
	
	/**
	 * sql의 모든 결과를 download 하도록 한다.
	 *  
	 * @return
	 */
	private static QueryExecuteResultDTO makeAllResult(UserDBDAO userDB, String strSQL, int intMaxCount) throws Exception {
		QueryExecuteResultDTO allResultDto = null; 
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO partResultDto = sqlUtil.nextQuery();
				if(allResultDto == null) {
					allResultDto = partResultDto;
				} else {
					allResultDto.getDataList().getData().addAll(partResultDto.getDataList().getData());
				}
			}
			
			return allResultDto;
		// page 쿼리를 지원하지 않는 디비는 원래 쿼리 했던 것 만큼만 넘긴다.
		} catch(Exception e) {
			throw e;
		}
	}

	/**
	 * xml result
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param targetName
	 * @param encoding
	 * @param strDefaultNullValue
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeXMLResult(UserDBDAO userDB, String strSQL, String targetName, String encoding,
			String strDefaultNullValue, int intMaxCount) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.XML);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL, intMaxCount);
		exportDto.setFileName(targetName);
		exportDto.setFileFullName(XMLExporter.makeContentFile(targetName, allResusltDto, encoding));
		
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}

	/**
	 * sql download(batch insert)
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param targetName
	 * @param commit
	 * @param encoding
	 * @param strDefaultNullValue
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeFileBatchInsertStatment(UserDBDAO userDB, String strSQL, String targetName, int commit,
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.SQL);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		exportDto.setFileName(targetName);
		exportDto.setFileFullName(AbstractTDBExporter.makeFileName(targetName, "sql"));
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		int intRowCnt = 0;
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileBatchInsertStatment(exportDto.getFileFullName(), targetName, rsDAO, commit, encoding);
				intRowCnt++;
			}
		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
		
		exportDto.setRowCount(intRowCnt);
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}
	
	/**
	 * SQL download(insert)
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param targetName
	 * @param commit
	 * @param encoding
	 * @param strDefaultNullValue
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeFileInsertStatment(UserDBDAO userDB, String strSQL, String targetName, int commit,
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.SQL);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		
		exportDto.setFileName(targetName);
		exportDto.setFileFullName(AbstractTDBExporter.makeFileName(targetName, "sql"));
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		int intRowCnt = 0;
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileInsertStatment(exportDto.getFileFullName(), targetName, rsDAO, commit, encoding);
				intRowCnt++;
			}

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
		
		exportDto.setRowCount(intRowCnt);
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}
	
	/**
	 * SQL download(update)
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param targetName
	 * @param listWhere
	 * @param commit
	 * @param encoding
	 * @param strDefaultNullValue
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeFileUpdateStatment(UserDBDAO userDB, String strSQL, String targetName, List<String> listWhere, int commit,
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.SQL);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		
		exportDto.setFileName(targetName);
		exportDto.setFileFullName(AbstractTDBExporter.makeFileName(targetName, "sql"));
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		int intRowCnt = 0;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileUpdateStatment(exportDto.getFileFullName(), targetName, rsDAO, listWhere, commit, encoding);
				intRowCnt++;
			}

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
		
		exportDto.setRowCount(intRowCnt);
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}
	
	/**
	 * SQL download(merge)
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param targetName
	 * @param listWhere
	 * @param commit
	 * @param encoding
	 * @param strDefaultNullValue
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static ExportResultDTO makeFileMergeStatment(UserDBDAO userDB, String strSQL, String targetName, List<String> listWhere, int commit,
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		ExportResultDTO exportDto = new ExportResultDTO();
		exportDto.setExportMethod(EXPORT_METHOD.SQL);
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		
		exportDto.setFileName(targetName);
		exportDto.setFileFullName(AbstractTDBExporter.makeFileName(targetName, "sql"));
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		int intRowCnt = 0;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileMergeStatment(exportDto.getFileFullName(), targetName, rsDAO, listWhere, commit, encoding);
				intRowCnt++;
			}

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
		
		exportDto.setRowCount(intRowCnt);
		exportDto.setEndCurrentTime(System.currentTimeMillis());
		return exportDto;
	}

}
