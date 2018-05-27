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

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLExtManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;
import com.hangum.tadpole.engine.sql.util.export.AbstractTDBExporter;
import com.hangum.tadpole.engine.sql.util.export.dto.ExportResultDTO;

/**
 * query all data export 
 * 
 * @author hangum
 *
 */
public abstract class AQueryDataExporter {
	private static final Logger logger = Logger.getLogger(AQueryDataExporter.class);
	protected static final int PER_DATA_SAVE_COUNT = 5000;
	protected static final int THREAD_SLEEP_MILLIS = 20;
	
	protected ExportResultDTO exportDto = new ExportResultDTO();
	
	/**
	 * initialize 
	 * 
	 * @param strFileName
	 * @param strExtension
	 */
	public void init(String fileName, String fileExtension) throws Exception {
		exportDto.setStartCurrentTime(System.currentTimeMillis());
		exportDto.setFileName(fileName);
		exportDto.setFileFullName(AbstractTDBExporter.makeDirName(fileName) + fileName + "." + fileExtension);
	}

	public abstract void makeHead(int intColumnCnt, ResultSetMetaData rsm) throws Exception;
	
	public abstract void makeBody(int intRowCnt, String[] strArryData) throws Exception;
	
	public abstract void makeTail() throws Exception;
	
	public void makeAllData(String fileName, String fileExtension, UserDBDAO userDB, String strSQL, int intMaxCount) throws Exception {
		init(fileName, fileExtension);
		
		/** 한번에 다운로드 받을 것인지 여부 */
		final boolean isOnetimeDownload = intMaxCount == -1?true:false;
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		java.sql.Connection javaConn = null;
		
		int intRowCnt = 0;
		try {
			if(userDB.getDBGroup() == DBGroupDefine.DYNAMODB_GROUP) {
				javaConn = TadpoleSQLExtManager.getInstance().getConnection(userDB);
			} else {
				javaConn = TadpoleSQLManager.getConnection(userDB);
			}
			
			stmt = javaConn.prepareStatement(strSQL); 
			rs = stmt.executeQuery();
			final ResultSetMetaData rsm = rs.getMetaData();
			final int intColumnCnt = rsm.getColumnCount(); 
					
			String[] strArryData = new String[intColumnCnt];

			makeHead(intColumnCnt, rsm);
			
			// 데이터를 만든다.
			while(rs.next()) {
				strArryData = new String[intColumnCnt];
				for(int i=0; i<intColumnCnt; i++) {
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
//							strArryData[i] = "";
						} else {
							strArryData[i] = SQLConvertCharUtil.toClient(userDB, rs.getString(intColIndex));
						}
					} catch(Exception e) {
						logger.error("ResutSet fetch error", e); //$NON-NLS-1$
					}
				}	// end for

				makeBody(intRowCnt, strArryData);
				
				intRowCnt++;
				
				// max row가 넘었으면 중지한다.
				if(!isOnetimeDownload) {
					if(intRowCnt > intMaxCount) {
						break;
					}
				}
			}	// end while
			
			makeTail();
			if(logger.isDebugEnabled()) logger.debug("========== total count is " + intRowCnt);
			
		} finally {
			exportDto.setRowCount(intRowCnt);
			exportDto.setEndCurrentTime(System.currentTimeMillis());
			
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
	}
	
	public abstract void close() throws Exception;

	public ExportResultDTO getExportDto() {
		return exportDto;
	}
}
