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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.EXPORT_METHOD;

/**
 * CSV query data exporter
 * 
 * @author hangum
 *
 */
public class CSVQueryDataExporter extends AQueryDataExporter {
	private static final Logger logger = Logger.getLogger(CSVQueryDataExporter.class);
	
	private FileOutputStream fos = null;
	private CSVWriter writer = null;
	
	/**
	 *  data separator 
	 */
	private char separator;
	
	/** 
	 * Is head?
	 */
	private boolean isAddHead;
	
	/**
	 * listData array 
	 */
	private List<String[]> listResultData = new ArrayList<String[]>();

	public CSVQueryDataExporter(char separator, boolean isAddHead) {
		this.separator = separator;
		this.isAddHead = isAddHead;
	}
	
	@Override
	public void init(String fileName, String fileExtension) throws Exception {
		super.init(fileName, fileExtension);
		
		exportDto.setExportMethod(EXPORT_METHOD.TEXT);
		
		// EXCEL UTF-8 엔코딩을 설정한다.
		fos = new FileOutputStream(exportDto.getFileFullName());
		fos.write(0xef);
		fos.write(0xbb);
		fos.write(0xbf);
		
		// CSV 파일 스트림을 생성합니다.
		writer = new CSVWriter(new OutputStreamWriter(fos), separator);
	}

	@Override
	public void makeHead(int intColumnCnt, ResultSetMetaData rsm) throws Exception {
		
		// 초기 헤더 레이블 만든다.
		if(isAddHead) {
			String[] strArryData = new String[intColumnCnt];
			for(int i=0; i<intColumnCnt; i++) {
				strArryData[i] = rsm.getColumnLabel(i+1);
			}
			listResultData.add(strArryData);
		}
	}

	@Override
	public void makeBody(int intRowCnt, String[] strArryData) throws Exception {
		listResultData.add(strArryData);
		
		if(((intRowCnt+1) % PER_DATA_SAVE_COUNT) == 0) {
			writer.writeAll(listResultData);
			listResultData.clear();
			
			if(logger.isDebugEnabled()) logger.debug("===processes =============>" + intRowCnt);
			try { Thread.sleep(THREAD_SLEEP_MILLIS); } catch(Exception e) {}
		}
	}

	@Override
	public void makeTail() {
		if(!listResultData.isEmpty()) {
			writer.writeAll(listResultData);
			listResultData.clear();
		}
	}

	@Override
	public void close() throws Exception {
		try { if(writer != null) writer.close(); } catch(Exception e) {}
		try { if(fos != null) fos.close(); } catch(Exception e) {}
	}
}
