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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * html exporter
 * 
 * @author hangum
 *
 */
public class HTMLExporter {
	private static final String sbHtml = 
				"<meta charset='UTF-8'>" +
				"<style type='text/css'>" +
				".tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}" +
				".tg td{font-family:Arial, sans-serif;font-size:12px;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}" +
				".tg th{font-family:Arial, sans-serif;font-size:12px;font-weight:normal;padding:5px 5px;border-style:dotted;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}" +
				".tg .tg-yw4l{vertical-align:top}" +
				"</style>";
	private static String strContetntGroup = "<table class='tg'>%s%s</table>";
	
	private static String strGroup = "<tr>%s</tr>";
	private static String strHead = "<th class='tg-yw4l'>%s</th>";
	private static String strContent= "<td class='tg-yw4l'>%s</td>";

	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 */
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO) {
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
				sbTmp.append( String.format(strContent, strValue) ); //$NON-NLS-1$
			}
			sbBody.append(String.format(strGroup, sbTmp.toString()));
		}
		String strLastBody = String.format(strContetntGroup, strLastColumnName, sbBody);
		
		return sbHtml + strLastBody;
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
		String strContent = makeContent(tableName, rsDAO);
		
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".html";
		String strFullPath = strTmpDir + strFile;
		
		FileUtils.writeStringToFile(new File(strFullPath), strContent, true);
		
		return strFullPath;
	}
}
