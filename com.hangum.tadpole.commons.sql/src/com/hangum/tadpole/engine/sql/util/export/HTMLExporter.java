/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.export;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.hangum.tadpole.commons.libs.core.define.HTMLDefine;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * html exporter
 * 
 * @see original html source is http://www.tablesgenerator.com/
 * @author hangum
 *
 */
public class HTMLExporter extends AbstractTDBExporter {
	
	public static String makeContent(String targetName, QueryExecuteResultDTO queryExecuteResultDTO, String strDefaultNullValue) {
		return makeContent(targetName, queryExecuteResultDTO, -1, strDefaultNullValue);
	}
	
	/**
	 * 
	 * @param strFullPath
	 * @param rsDAO
	 * @param encoding
	 * @throws Exception
	 */
	public static void makeHeaderFile(String strFullPath, QueryExecuteResultDTO rsDAO, String encoding) throws Exception {
		FileUtils.writeStringToFile(new File(strFullPath), HTMLDefine.HTML_STYLE, encoding, true);
		FileUtils.writeStringToFile(new File(strFullPath), "<table class='tg'>", encoding, true);
		
		// make column header
		String strHeader = makeHeader(rsDAO);
		FileUtils.writeStringToFile(new File(strFullPath), strHeader, encoding, true);
	}

	/**
	 * make head column header
	 * @param rsDAO
	 * @return
	 */
	public static String makeHeader(QueryExecuteResultDTO rsDAO) {
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		StringBuffer sbHead = new StringBuffer();

		for(int i=0; i<mapLabelName.size(); i++) {
			String strLabelName = mapLabelName.get(i);
			if(!SQLUtil.isTDBSpecialColumn(strLabelName)) {
				sbHead.append( HTMLDefine.makeTH(strLabelName) );
			}
		}
		String strLastColumnName = HTMLDefine.makeTR(sbHead.toString());
		
		return strLastColumnName;
	}

	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @param intLimitCnt
	 * @param strDefaultNullValue
	 * @return
	 */
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO, int intLimitCnt, String strDefaultNullValue) {
		// make column header
		String strHeader = makeHeader(rsDAO);
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		// body
		StringBuffer sbBody = new StringBuffer("");
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			StringBuffer sbTmp = new StringBuffer();
			for(int j=0; j<mapColumns.size(); j++) {
				
				// tdb 내부적으로 사용하는 컬럼을 보이지 않도록 합니다.
				if(!SQLUtil.isTDBSpecialColumn(mapLabelName.get(j))) {
					String strValue = mapColumns.get(j)==null?strDefaultNullValue:""+mapColumns.get(j);
					sbTmp.append( HTMLDefine.makeTD(StringEscapeUtils.unescapeHtml(strValue)) ); //$NON-NLS-1$
				}
			}
			sbBody.append(HTMLDefine.makeTR(sbTmp.toString()));
			
			if(i == intLimitCnt) break;
		}
		
		return HTMLDefine.HTML_STYLE + HTMLDefine.makeTABLE(strHeader, sbBody.toString());
	}
	
	/**
	 * make content file
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @param encoding 
	 * @param strDefaultNullValue
	 * @return
	 * @throws Exception
	 */
	public static void makeContentFile(String strFullPath, QueryExecuteResultDTO rsDAO, String encoding, String strDefaultNullValue) throws Exception {

		// make content
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		
		// column name
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		
		// body start
		StringBuffer sbBody = new StringBuffer("");
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			StringBuffer sbTmp = new StringBuffer();
			for(int j=0; j<mapColumns.size(); j++) {
				
				// tdb 내부적으로 사용하는 컬럼을 보이지 않도록 합니다.
				if(!SQLUtil.isTDBSpecialColumn(mapLabelName.get(j))) {
					String strValue = mapColumns.get(j)==null?strDefaultNullValue:""+mapColumns.get(j);
					sbTmp.append( HTMLDefine.makeTD(StringEscapeUtils.unescapeHtml(strValue)) ); //$NON-NLS-1$
				}
				
			}
			sbBody.append(HTMLDefine.makeTR(sbTmp.toString()));
		}
		FileUtils.writeStringToFile(new File(strFullPath), sbBody.toString(), encoding, true);
	}
	
	/**
	 * make tail content
	 * @param strFullPath
	 * @param encoding
	 * @throws Exception
	 */
	public static void makeTailFile(String strFullPath, String encoding) throws Exception {
		FileUtils.writeStringToFile(new File(strFullPath), "</table>", encoding, true);
	}

}
