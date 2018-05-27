/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.hangum.tadpole.commons.util.Utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;


/**
 * CSV file util
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 31.
 *
 */
public class CSVUtils {
	private static final Logger logger = Logger.getLogger(CSVUtils.class);
	
	/**
	 * SWT table to csv data
	 * 
	 * @param tbl
	 * @return
	 * @throws Exception
	 */
	public static byte[] tableToCSV(Table tbl) throws Exception {
		List<String[]> listCsvData = new ArrayList<String[]>();
		
		TableColumn[] tcs = tbl.getColumns();
		String[] strArryHeader = new String[tcs.length];
		for (int i=0; i<strArryHeader.length; i++) {
			strArryHeader[i] = tcs[i].getText();
		}
		listCsvData.add(strArryHeader);
	
		String[] strArryData = new String[tcs.length];
		for (int i=0; i<tbl.getItemCount(); i++ ) {
			strArryData = new String[tbl.getColumnCount()];
			
			TableItem gi = tbl.getItem(i);
			for(int intCnt = 0; intCnt<tcs.length; intCnt++) {
				strArryData[intCnt] = Utils.convHtmlToLine(gi.getText(intCnt));
			}
			listCsvData.add(strArryData);
		}
		
		return CSVUtils.makeData(listCsvData);
	}
	
	/**
	 * make csv data
	 * 
	 * @param listContent
	 * @return
	 * @throws Exception
	 */
	public static byte[] makeData(List<String[]> listContent) throws Exception {
		return makeData(listContent, CSVWriter.DEFAULT_SEPARATOR);
	}
			
	/**
	 * csv data
	 * @param seprator 
	 * 
	 * @param 
	 */
	public static byte[] makeData(List<String[]> listContent, char seprator) throws Exception {
		CSVWriter writer = null;
		
		File strTemp = new File(PublicTadpoleDefine.TEMP_DIR + System.currentTimeMillis() + "_TDBTemp.csv");
		FileOutputStream fos = new FileOutputStream(strTemp);
		fos.write(0xef);
		fos.write(0xbb);
		fos.write(0xbf);
		
		byte[] bytesDatas = null;
		try {
			writer = new CSVWriter(new OutputStreamWriter(fos), seprator);
			writer.writeAll(listContent);
			
		} finally {
			if(writer != null) try { writer.close(); } catch(Exception e) {}
			if(fos != null) try {fos.close(); } catch(Exception e) {}

			bytesDatas = FileUtils.readFileToByteArray(strTemp);
			FileUtils.forceDelete(strTemp);
		}
		
		return bytesDatas;
	}
	
	/**
	 * csv data
	 * @param seprator 
	 * 
	 * @param 
	 */
	public static String makeDataString(List<String[]> listContent, char seprator) throws Exception {
		String strReust = "";
		
		StringWriter sw = new StringWriter();
		CSVWriter writer = null;
		
		try {
			writer = new CSVWriter(sw, seprator);
			writer.writeAll(listContent);
			
			strReust = sw.getBuffer().toString();
		} finally {
			if(writer != null) try { writer.close(); } catch(Exception e) {}
			sw.close();
		}
		
		return strReust;
	}
	
	/**
	 * csv file reader
	 * 
	 * @param filename
	 * @param seprator
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> readFile(String filename, char seprator) {
		List<String[]> returnMap = new ArrayList<String[]>();
		String[] nextLine = null;
		CSVReader csvReader = null;
		try {
			 csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8")), seprator);
			 while((nextLine = csvReader.readNext()) != null) {
				returnMap.add(nextLine);
			 }
		} catch(Exception e) {
			logger.error("CSV read error", e);
		} finally {
			if(csvReader != null) try { csvReader.close(); } catch(Exception e) {}
		}
		
		return returnMap;
	}
	
	/**
	 * csv reader
	 * 
	 * @param filename
	 * @param seprator
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> readData(String strData, char seprator) {
		List<String[]> returnMap = new ArrayList<String[]>();
		String[] nextLine = null;
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new BufferedReader(new StringReader(strData)), seprator);
			while((nextLine = csvReader.readNext()) != null) {
				returnMap.add(nextLine);
			}
		} catch(Exception e) {
			logger.error("CSV read error", e);
		} finally {
			if(csvReader != null) try { csvReader.close(); } catch(Exception e) {}
		}
		
		return returnMap;
	}
	
}
