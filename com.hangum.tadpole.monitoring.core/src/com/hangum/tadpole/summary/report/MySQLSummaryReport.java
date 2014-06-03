package com.hangum.tadpole.summary.report;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author hangum
 *
 */
public class MySQLSummaryReport {
	private static final Logger logger = Logger.getLogger(MySQLSummaryReport.class);

	/**
	 * 
	 * 
	 * @param strSchema
	 * @return
	 */
	public static String getSQL(String strSchema) {
		String retSQL = "";
		
		try {
			InputStream is = MySQLSummaryReport.class.getResourceAsStream("MySQL_Summary.sql");
			
			int size = is.available();
			byte[] dataByte = new byte[size];
			is.read(dataByte, 0, size);
			is.close();
			
			retSQL = new String(dataByte);
			logger.debug("summary sql" + retSQL);
			
			retSQL = StringUtils.replace(retSQL, "#schemaName#", "'" + strSchema + "'" ); 
		} catch (IOException e) {
			logger.error("find summary sql", e);
		}
		
		return retSQL;
	}
}
