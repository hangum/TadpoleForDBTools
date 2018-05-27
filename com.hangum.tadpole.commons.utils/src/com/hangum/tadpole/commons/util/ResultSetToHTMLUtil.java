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
package com.hangum.tadpole.commons.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.libs.core.define.HTMLDefine;

/**
 * ResultSet Util
 * 
 * @author hyunjongcho
 *
 */
public class ResultSetToHTMLUtil {
	
	/**
	 * make html head
	 * 
	 * @param strHead
	 * @return
	 */
	public static String makeHTMLHead(String strHead) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table class='tg'><tr>");
		sb.append(String.format(HTMLDefine.SINGLE_TH, strHead));
		sb.append("</tr></table>");
		
		return sb.toString();
	}

	/**
	 * make ResultSet to html
	 * 
	 * @param strTitle
	 * @param rs
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public static String makeResultSetTOHTML(ResultSet rs, int limit) throws Exception {
		StringBuffer sb = new StringBuffer();
		int rowCount = 0;

		sb.append("<table class='tg'>");
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		// table header
		sb.append("<tr>");
		for (int i = 0; i < columnCount; i++) {
			sb.append(String.format(HTMLDefine.TH, rsmd.getColumnLabel(i + 1)));
		}
		sb.append("</tr>");
		// the data
		while (rs.next()) {
			rowCount++;
			sb.append("<tr>");
			for (int i = 0; i < columnCount; i++) {
				sb.append(String.format(HTMLDefine.TD, rs.getString(i + 1)));
			}
			sb.append("</tr>");
			
			if(limit <= rowCount) break;
		}
		sb.append("</table>");
		
		return sb.toString();
	}

	/**
	 * QueryExecuteResultDTO 의 결과를 출력한다. 
	 * ps) QueryExecuteResultDTO를 바로 넘기지 못하는 이유는 클래스 패스 설정에 문제가 있어서 이다.- hangum
	 * 
	 * @param columnLabelName
	 * @param data
	 * @param limit
	 */
	public static String makeResultSetToHTML(Map<Integer, String> columnLabelName, List<Map<Integer, Object>> data, int limit) {
		StringBuffer sb = new StringBuffer();
		int rowCount = 0;

		sb.append("<table class='tg'>");
		
		// table header
		sb.append("<tr>");
		for (int i = 0; i < columnLabelName.size(); i++) {
			sb.append(String.format(HTMLDefine.TH, columnLabelName.get(i)));
		}
		sb.append("</tr>");
		
		// the data
		for(int i=0; i<data.size(); i++) {
			sb.append("<tr>");
			
			Map<Integer, Object> mapData = data.get(i);
			for (int j=0; j<mapData.size(); j++) {
				sb.append(String.format(HTMLDefine.TD, mapData.get(j)));
			}	sb.append("</tr>");
					
			if(limit <= rowCount) break;
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	
}
