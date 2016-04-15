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
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * html exporter
 * 
 * @see original html source is http://www.tablesgenerator.com/
 * @author hangum
 *
 */
public class HTMLExporter extends AbstractTDBExporter {
//	public static final String sbHtml = 
//				"<meta charset='UTF-8'>" +
//				"<style type='text/css'>" +
//				".tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}" +
//				".tg td{font-family:Arial, sans-serif;font-size:12px;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}" +
//				".tg th{font-family:Arial, sans-serif;font-size:12px;font-weight:normal;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}" +
//				".tg .tg-yw4l{vertical-align:top}" +
//				"</style>";
	private static String strContetntGroup = "<table class='tg'>%s%s</table>";
	
	private static String strGroup = "<tr>%s</tr>";
	private static String strHead = "<th class='tg-yw4l'>%s</th>";
	private static String strContent= "<td class='tg-yw4l'>%s</td>";
	
	public static String makeContent(String targetName, QueryExecuteResultDTO queryExecuteResultDTO) {
		return makeContent(targetName, queryExecuteResultDTO, -1);
	}

	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @param intLimitCnt
	 * @return
	 */
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO, int intLimitCnt) {
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		// column .
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		StringBuffer sbHead = new StringBuffer();
		sbHead.append( String.format(strHead, "#") );
		for(int i=1; i<mapLabelName.size(); i++) {
			sbHead.append( String.format(strHead, mapLabelName.get(i)) );
		}
		String strLastColumnName = String.format(strGroup, sbHead.toString());

		StringBuffer sbBody = new StringBuffer("");
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			StringBuffer sbTmp = new StringBuffer();
			sbTmp.append( String.format(strHead, ""+(i+1)) ); //$NON-NLS-1$
			for(int j=1; j<mapColumns.size(); j++) {
				String strValue = mapColumns.get(j)==null?"":""+mapColumns.get(j);
				sbTmp.append( String.format(strContent, StringEscapeUtils.unescapeHtml(strValue)) ); //$NON-NLS-1$
			}
			sbBody.append(String.format(strGroup, sbTmp.toString()));
			
			if(i == intLimitCnt) break;
		}
		String strLastBody = String.format(strContetntGroup, strLastColumnName, sbBody);
		
		return HTMLDefine.sbHtml + strLastBody;
	}
	
	/**
	 * make content file
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 * @throws Exception
	 */
	public static String makeContentFile(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		// full text
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".html";
		String strFullPath = strTmpDir + strFile;
		
		FileUtils.writeStringToFile(new File(strFullPath), HTMLDefine.sbHtml, true);
		FileUtils.writeStringToFile(new File(strFullPath), "<table class='tg'>", true);
		
		// make content
		List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		// column .
		Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
		StringBuffer sbHead = new StringBuffer();
		sbHead.append( String.format(strHead, "#") );
		for(int i=1; i<mapLabelName.size(); i++) {
			sbHead.append( String.format(strHead, mapLabelName.get(i)) );
		}
		String strLastColumnName = String.format(strGroup, sbHead.toString());
		
		// header
		FileUtils.writeStringToFile(new File(strFullPath), strLastColumnName, true);
		
		// body start
		StringBuffer sbBody = new StringBuffer("");
		for(int i=0; i<dataList.size(); i++) {
			Map<Integer, Object> mapColumns = dataList.get(i);
			
			StringBuffer sbTmp = new StringBuffer();
			sbTmp.append( String.format(strHead, ""+(i+1)) ); //$NON-NLS-1$
			for(int j=1; j<mapColumns.size(); j++) {
				String strValue = mapColumns.get(j)==null?"":""+mapColumns.get(j);
				sbTmp.append( String.format(strContent, StringEscapeUtils.unescapeHtml(strValue)) ); //$NON-NLS-1$
			}
			sbBody.append(String.format(strGroup, sbTmp.toString()));
			
			if((i%DATA_COUNT) == 0) {
				FileUtils.writeStringToFile(new File(strFullPath), sbBody.toString(), true);
				sbBody.delete(0, sbBody.length());
			}
		}
		
		if(sbBody.length() > 0) {
			FileUtils.writeStringToFile(new File(strFullPath), sbBody.toString(), true);
		}
		
		FileUtils.writeStringToFile(new File(strFullPath), "</table>", true);
		
		return strFullPath;
	}

}
