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

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * Make all export data
 * 
 * @author hangum
 *
 */
public class AllDataExporter {
	private static final Logger logger = Logger.getLogger(AllDataExporter.class);
	
	/**
	 * sql의 모든 결과를 csv로 download 하도록 한다.
	 * @param userDB
	 * @param strSQL
	 * @param isAddHead
	 * @param fileName
	 * @param seprator
	 * @param encoding
	 * @param strDefaultNullValue
	 * @return
	 * @throws Exception
	 */
	public static String makeCSVAllResult(UserDBDAO userDB, String strSQL, boolean isAddHead, String fileName, char seprator, String encoding, String strDefaultNullValue) throws Exception {
		boolean isFirst = true;
		String strFullPath = AbstractTDBExporter.makeFileName(fileName, "csv");
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
				if(isFirst) {
					CSVExpoter.makeHeaderFile(strFullPath, isAddHead, rsDAO, seprator, encoding);
					isFirst = false;
				}
					
				CSVExpoter.makeContentFile(strFullPath, isAddHead, rsDAO, seprator, encoding, strDefaultNullValue);
			}
			
			return strFullPath;

		} catch(Exception e) {
			logger.error("make all CSV export data", e);
			throw e;
		}
	}

	/**
	 * sql의 모든 결과를 html로 download 하도록 한다.
	 * 
	 * @param userDB
	 * @param sql
	 * @param targetName
	 * @param encoding
	 * @param encoding2
	 * @return
	 */
	public static String makeHTMLAllResult(UserDBDAO userDB, String strSQL, String fileName, String encoding, String strDefaultNullValue) throws Exception {
		boolean isFirst = true;
		String strFullPath = AbstractTDBExporter.makeFileName(fileName, "html");
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
				if(isFirst) {
					HTMLExporter.makeHeaderFile(strFullPath, rsDAO, encoding);
					isFirst = false;
				}
					
				HTMLExporter.makeContentFile(strFullPath, rsDAO, encoding, strDefaultNullValue);
			}
			
			// 
			HTMLExporter.makeTailFile(strFullPath, encoding);
			
			return strFullPath;

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
	}

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
	public static String makeJSONHeadAllResult(UserDBDAO userDB, String strSQL, String fileName, String schemeKey,
			String recordKey, boolean isFormat, String encoding, String strDefaultNullValue) throws Exception {
		
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL);
		return JsonExpoter.makeContentFile(fileName, allResusltDto, schemeKey, recordKey, isFormat, encoding);
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
	public static String makeJSONAllResult(UserDBDAO userDB, String strSQL, String targetName, boolean isFormat,
			String encoding, String strDefaultNullValue)  throws Exception {
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL);
		return JsonExpoter.makeContentFile(targetName, allResusltDto, isFormat, encoding);
	}
	
	/**
	 * sql의 모든 결과를 download 하도록 한다.
	 *  
	 * @return
	 */
	private static QueryExecuteResultDTO makeAllResult(UserDBDAO userDB, String strSQL) throws Exception {
		QueryExecuteResultDTO allResultDto = null; 
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
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

	public static String makeXMLResult(UserDBDAO userDB, String strSQL, String targetName, String encoding,
			String strDefaultNullValue) throws Exception {
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL);
		return XMLExporter.makeContentFile(targetName, allResusltDto, encoding);
	}

	public static String makeFileBatchInsertStatment(UserDBDAO userDB, String strSQL, String targetName, int commit,
			String encoding, String strDefaultNullValue) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileBatchInsertStatment(strFullPath, targetName, rsDAO, commit, encoding);
			}
			
			return strFullPath;

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
	}
	
	public static String makeFileInsertStatment(UserDBDAO userDB, String strSQL, String targetName, int commit,
			String encoding, String strDefaultNullValue) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileInsertStatment(strFullPath, targetName, rsDAO, commit, encoding);
			}
			
			return strFullPath;

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
	}
	
	public static String makeFileUpdateStatment(UserDBDAO userDB, String strSQL, String targetName, List<String> listWhere, int commit,
			String encoding, String strDefaultNullValue) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileUpdateStatment(strFullPath, targetName, rsDAO, listWhere, commit, encoding);
			}
			
			return strFullPath;

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
	}
	
	
	public static String makeFileMergeStatment(UserDBDAO userDB, String strSQL, String targetName, List<String> listWhere, int commit,
			String encoding, String strDefaultNullValue) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, true);
			while(sqlUtil.hasNext()) {
				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
					
				SQLExporter.makeFileMergeStatment(strFullPath, targetName, rsDAO, listWhere, commit, encoding);
			}
			
			return strFullPath;

		} catch(Exception e) {
			logger.error("make all HTML export data", e);
			throw e;
		}
	}

}
