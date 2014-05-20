package com.hangum.tadpold.commons.libs.core.mails.template;

import java.sql.ResultSetMetaData;

public class CronExpreeTemplate implements MailBodyTemplate {

	public int dumpData(java.sql.ResultSet rs, java.io.PrintWriter out) throws Exception {
		int rowCount = 0;

		out.println("<P ALIGN='center'><TABLE BORDER=1>");
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		// table header
		out.println("<TR>");
		for (int i = 0; i < columnCount; i++) {
			out.println("<TH>" + rsmd.getColumnLabel(i + 1) + "</TH>");
		}
		out.println("</TR>");
		// the data
		while (rs.next()) {
			rowCount++;
			out.println("<TR>");
			for (int i = 0; i < columnCount; i++) {
				out.println("<TD>" + rs.getString(i + 1) + "</TD>");
			}
			out.println("</TR>");
		}
		out.println("</TABLE></P>");
		return rowCount;
	}
}
