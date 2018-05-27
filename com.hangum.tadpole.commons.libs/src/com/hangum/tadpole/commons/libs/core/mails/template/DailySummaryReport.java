/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.mails.template;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 
 * @author hangum
 *
 */
public class DailySummaryReport extends MailBodyTemplate {
	
	/**
	 * 
	 * @param displayName
	 * @param fullText
	 * @return
	 */
	public String makeFullSummaryReport(String displayName, String fullText) {
		StringBuffer sb = new StringBuffer("<html>");
		
		sb.append( makeHead(displayName + "  Report.") ); 
		sb.append( fullText );
		sb.append( makeTail() );
		
		sb.append("</html>");
		
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
	public static String makeResultSetTOHTML(String strTitle, ResultSet rs, int limit) throws Exception {
		StringBuffer sb = new StringBuffer();
		int rowCount = 0;

		sb.append("<P ALIGN='center'>");
		sb.append("<TR><TD>" + strTitle + "</TD></TR>");
		sb.append("<TABLE BORDER=1>");
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		// table header
		sb.append("<TR>");
		for (int i = 0; i < columnCount; i++) {
			sb.append("<TH>" + rsmd.getColumnLabel(i + 1) + "</TH>");
		}
		sb.append("</TR>");
		// the data
		while (rs.next()) {
			rowCount++;
			sb.append("<TR>");
			for (int i = 0; i < columnCount; i++) {
				sb.append("<TD>" + rs.getString(i + 1) + "</TD>");
			}
			sb.append("</TR>");
			
			if(limit <= rowCount) break;
		}
		sb.append("</TABLE></P>");
		
		return sb.toString();
	}
}
