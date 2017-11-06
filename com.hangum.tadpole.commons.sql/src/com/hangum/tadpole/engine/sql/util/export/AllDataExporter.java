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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLExtManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;
import com.hangum.tadpole.engine.sql.util.SQLQueryUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.opencsv.CSVWriter;

/**
 * Make all export data
 * 
 * 에디터에서 데이터 내보내기는 전체를 받아 내보내는 식으로 수정합니다. (cpu, 메모리 릭의 염려가 있습니다) 
 * 
 * @author hangum
 *
 */
public class AllDataExporter {
	private static final Logger logger = Logger.getLogger(AllDataExporter.class);
	private static final int PER_DATA_SAVE_COUNT = 5000;
	private static final int THREAD_SLEEP_MILLIS = 20;
	
//	/**
//	 * excel export data
//	 * 
//	 * @param userDB
//	 * @param strSQL
//	 * @param fileName
//	 * @param intMaxCount
//	 * @return
//	 * @throws Exception
//	 */
//	public static String makeExcelAllResult(UserDBDAO userDB, String strSQL, String fileName, int intMaxCount) throws Exception {
//		String strFullPath = AbstractTDBExporter.makeDirName(fileName) + fileName + "." + "xlsx";
//		/** 한번에 다운로드 받을 것인지 여부 */
//		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
//		
//		try {
//			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
//			while(sqlUtil.hasNext()) {
//				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
//
//				ExcelExporter.makeContentFile(strFullPath, fileName, rsDAO);
//			}
//			
//			return strFullPath;
//		} catch(Exception e) {
//			logger.error("make all CSV export data", e);
//			throw e;
//		}
//	}
	
	/**
	 * excel export data
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param fileName
	 * @param intMaxCount
	 * @return
	 * @throws Exception
	 */
	public static String makeExcelAllResult(UserDBDAO userDB, String strSQL, String fileName, int intMaxCount) throws Exception {
		long longSt = System.currentTimeMillis();
		String strFullFileName = AbstractTDBExporter.makeDirName(fileName) + fileName + "." + "xlsx";

		/** 한번에 다운로드 받을 것인지 여부 */
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		java.sql.Connection javaConn = null;
		
		try {
			if(userDB.getDBGroup() == DBGroupDefine.DYNAMODB_GROUP) {
				javaConn = TadpoleSQLExtManager.getInstance().getConnection(userDB);
			} else {
				javaConn = TadpoleSQLManager.getConnection(userDB);
			}
			
			stmt = javaConn.prepareStatement(strSQL); 
			rs = stmt.executeQuery();//Query( selText );
			
			List<String[]> listsvData = new ArrayList<String[]>();
			String[] strArryData = new String[rs.getMetaData().getColumnCount()];
			int intRowCnt = 0;
			while(rs.next()) {
				
				strArryData = new String[rs.getMetaData().getColumnCount()];
				for(int i=0; i<rs.getMetaData().getColumnCount(); i++) {
					
					final int intColIndex = i+1;
					try {
						int colType = rs.getMetaData().getColumnType(intColIndex); 
						if (java.sql.Types.LONGVARCHAR == colType || 
								java.sql.Types.LONGNVARCHAR == colType || 
								java.sql.Types.CLOB == colType || 
								java.sql.Types.NCLOB == colType){
							StringBuffer sb = new StringBuffer();						  
							Reader is =  rs.getCharacterStream(intColIndex);						
							if (is != null) {
								int cnum = 0;
								char[] cbuf = new char[10];							 
								while ((cnum = is.read(cbuf)) != -1) sb.append(cbuf, 0 ,cnum);
							} // if

							strArryData[i] = SQLConvertCharUtil.toClient(userDB, sb.toString());
						} else if(java.sql.Types.BLOB == colType || java.sql.Types.STRUCT == colType) {
//									tmpRow.put(intShowColIndex, rs.getObject(intColIndex));
							strArryData[i] = "";
						} else {
							strArryData[i] = SQLConvertCharUtil.toClient(userDB, rs.getString(intColIndex));
						}
					} catch(Exception e) {
						logger.error("ResutSet fetch error", e); //$NON-NLS-1$
					}
				}
				listsvData.add(strArryData);
				
				if(((intRowCnt+1) % 5000) == 0) {
					Thread.sleep(20);
					if(logger.isDebugEnabled()) logger.debug("===processes =============>" + intRowCnt);
					ExcelExporter.makeFile(strFullFileName, fileName, listsvData);
					listsvData.clear();
				}
				intRowCnt++;
				
				// max row가 넘었으면 중지한다.
				if(!isOnetimeDownload) {
					if(intRowCnt > intMaxCount) {
						break;
					}
				}
			}
			if(logger.isInfoEnabled()) {
				logger.info("========== total count is " + intRowCnt);
			}
			if(!listsvData.isEmpty()) {
				ExcelExporter.makeFile(strFullFileName, fileName, listsvData);
				listsvData.clear();
			}
			
		} finally {
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
		long longEd = System.currentTimeMillis();
		if(logger.isDebugEnabled()) logger.debug("== total resume is " + (longEd - longSt));
		
		return strFullFileName;
	}
	
//	/**
//	 * sql의 모든 결과를 csv로 download 하도록 한다.
//	 * @param userDB
//	 * @param strSQL
//	 * @param isAddHead
//	 * @param fileName
//	 * @param seprator
//	 * @param encoding
//	 * @param strDefaultNullValue
//	 * @param intMaxCount
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public static String makeCSVAllResult(UserDBDAO userDB, String strSQL, boolean isAddHead, String fileName, char seprator, String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
//		long longSt = System.currentTimeMillis();
//		
//		boolean isFirst = true;
//		String strFullPath = AbstractTDBExporter.makeFileName(fileName, "csv");
//		/** 한번에 다운로드 받을 것인지 여부 */
//		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
//		
//		try {
//			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
//			while(sqlUtil.hasNext()) {
//				QueryExecuteResultDTO rsDAO = sqlUtil.nextQuery();
//				if(isFirst) {
//					CSVExpoter.makeHeaderFile(strFullPath, isAddHead, rsDAO, seprator, encoding);
//					isFirst = false;
//				}
//					
//				CSVExpoter.makeContentFile(strFullPath, isAddHead, rsDAO, seprator, encoding, strDefaultNullValue);
//			}
//			
//			long longEd = System.currentTimeMillis();
//			if(logger.isDebugEnabled()) logger.debug("== total resume is " + (longEd - longSt));
//			
//			return strFullPath;
//
//		} catch(Exception e) {
//			logger.error("make all CSV export data", e);
//			throw e;
//		}
//	}
	
	/**
	 * sql의 모든 결과를 csv로 download 하도록 한다.
	 * @param userDB
	 * @param strSQL
	 * @param isAddHead
	 * @param fileName
	 * @param seprator
	 * @param encoding
	 * @param strDefaultNullValue
	 * @param intMaxCount
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String makeCSVAllResult(UserDBDAO userDB, String strSQL, boolean isAddHead, String fileName, char seprator, String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		long longSt = System.currentTimeMillis();
		
		boolean isFirst = true;
		String strFullFileName = AbstractTDBExporter.makeDirName(fileName) + fileName + ".csv";
		/** 한번에 다운로드 받을 것인지 여부 */
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
//		if(isOnetimeDownload) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		java.sql.Connection javaConn = null;
		
		FileOutputStream fos = null;
		CSVWriter writer = null;
		
		try {
			if(userDB.getDBGroup() == DBGroupDefine.DYNAMODB_GROUP) {
				javaConn = TadpoleSQLExtManager.getInstance().getConnection(userDB);
			} else {
				javaConn = TadpoleSQLManager.getConnection(userDB);
			}
			
			stmt = javaConn.prepareStatement(strSQL); 
			rs = stmt.executeQuery();//Query( selText );
			
			List<String[]> listsvData = new ArrayList<String[]>();
			String[] strArryData = new String[rs.getMetaData().getColumnCount()];
			int intRowCnt = 0;
			while(rs.next()) {
				// 초기 헤더 레이블 만든다.
				if(isFirst) {
					ResultSetMetaData rsm = rs.getMetaData();
					for(int i=0; i<rsm.getColumnCount(); i++) {
						strArryData[i] = rsm.getColumnLabel(i+1);
					}
					listsvData.add(strArryData);
					
					fos = new FileOutputStream(strFullFileName);
					fos.write(0xef);
					fos.write(0xbb);
					fos.write(0xbf);
					
					writer = new CSVWriter(new OutputStreamWriter(fos), seprator);
					if(isAddHead) {
						writer.writeAll(listsvData);
					}
					listsvData.clear();
					isFirst = false;
				}
				
				strArryData = new String[rs.getMetaData().getColumnCount()];
				for(int i=0; i<rs.getMetaData().getColumnCount(); i++) {
					
					final int intColIndex = i+1;
					try {
						int colType = rs.getMetaData().getColumnType(intColIndex); 
						if (java.sql.Types.LONGVARCHAR == colType || 
								java.sql.Types.LONGNVARCHAR == colType || 
								java.sql.Types.CLOB == colType || 
								java.sql.Types.NCLOB == colType){
							StringBuffer sb = new StringBuffer();						  
							Reader is =  rs.getCharacterStream(intColIndex);						
							if (is != null) {
								int cnum = 0;
								char[] cbuf = new char[10];							 
								while ((cnum = is.read(cbuf)) != -1) sb.append(cbuf, 0 ,cnum);
							} // if

							strArryData[i] = SQLConvertCharUtil.toClient(userDB, sb.toString());
						} else if(java.sql.Types.BLOB == colType || java.sql.Types.STRUCT == colType) {
//									tmpRow.put(intShowColIndex, rs.getObject(intColIndex));
							strArryData[i] = "";
						} else {
							strArryData[i] = SQLConvertCharUtil.toClient(userDB, rs.getString(intColIndex));
						}
					} catch(Exception e) {
						logger.error("ResutSet fetch error", e); //$NON-NLS-1$
					}
				}
				listsvData.add(strArryData);
				
				if(((intRowCnt+1) % PER_DATA_SAVE_COUNT) == 0) {
					Thread.sleep(THREAD_SLEEP_MILLIS);
					if(logger.isDebugEnabled()) logger.debug("===processes =============>" + intRowCnt);
					writer.writeAll(listsvData);
					listsvData.clear();
				}
				intRowCnt++;
				
				// max row가 넘었으면 중지한다.
				if(!isOnetimeDownload) {
					if(intRowCnt > intMaxCount) {
						break;
					}
				}
			}
			if(logger.isInfoEnabled()) {
				logger.info("========== total count is " + intRowCnt);
			}
			if(!listsvData.isEmpty()) {
				writer.writeAll(listsvData);
				listsvData.clear();
			}
			
		} finally {
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
			try { if(writer != null) writer.close(); } catch(Exception e) {}
			try { if(fos != null) fos.close(); } catch(Exception e) {}
		}
		long longEd = System.currentTimeMillis();
		if(logger.isDebugEnabled()) logger.debug("== total resume is " + (longEd - longSt));
		
		return strFullFileName;
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
	public static String makeHTMLAllResult(UserDBDAO userDB, String strSQL, String fileName, String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		boolean isFirst = true;
		String strFullPath = AbstractTDBExporter.makeFileName(fileName, "html");
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
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
			String recordKey, boolean isFormat, String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL, intMaxCount);
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
			String encoding, String strDefaultNullValue, int intMaxCount)  throws Exception {
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL, intMaxCount);
		return JsonExpoter.makeContentFile(targetName, allResusltDto, isFormat, encoding);
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

	public static String makeXMLResult(UserDBDAO userDB, String strSQL, String targetName, String encoding,
			String strDefaultNullValue, int intMaxCount) throws Exception {
		QueryExecuteResultDTO allResusltDto = makeAllResult(userDB, strSQL, intMaxCount);
		return XMLExporter.makeContentFile(targetName, allResusltDto, encoding);
	}

	public static String makeFileBatchInsertStatment(UserDBDAO userDB, String strSQL, String targetName, int commit,
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
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
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
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
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
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
			String encoding, String strDefaultNullValue, int intMaxCount) throws Exception {
		String strFullPath = AbstractTDBExporter.makeFileName(targetName, "sql");
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		try {
			SQLQueryUtil sqlUtil = new SQLQueryUtil(userDB, strSQL, isOnetimeDownload, intMaxCount);
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
