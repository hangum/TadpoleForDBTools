package com.hangum.tadpole.importdb.core.dialog.importdb.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * csv loader 
 * 
 * @see original file location is (http://viralpatel.net/blogs/java-load-csv-file-to-database/)
 * 
 */
public class CSVLoader {
	private static final Logger logger = Logger.getLogger(CSVLoader.class);
	
	private static final String SQL_INSERT = "INSERT INTO ${table}(${keys}) VALUES(${values})";
	private static final String TABLE_REGEX = "${table}";
	private static final String KEYS_REGEX = "${keys}";
	private static final String VALUES_REGEX = "${values}";

	private char seprator;

	/**
	 * Public constructor to build CSVLoader object with Connection details. The
	 * connection is closed on success or failure.
	 * 
	 * @param connection
	 */
	public CSVLoader(String separator) {
		this.seprator = separator.charAt(0);//String.',';
	}
	
	/**
	 * Parse CSV file using OpenCSV library and load in given database table.
	 * 
	 * @param con
	 * @param csvFile Input CSV file
	 * @param tableName Database table name to import data
	 * @param truncateBeforeLoad trancate before load
	 * @throws Exception
	 */
	public void loadCSV(final Connection con, final File csvFile, final String tableName, final boolean truncateBeforeLoad) throws Exception {

		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(csvFile), this.seprator);

		} catch (Exception e) {
			logger.error(e);
			throw new Exception("Error occured while executing file. " + e.getMessage());
		}

		String[] headerRow = csvReader.readNext();

		if (null == headerRow) {
			throw new FileNotFoundException("No columns defined in given CSV file." + "Please check the CSV file format.");
		}

		String questionmarks = StringUtils.repeat("?,", headerRow.length);
		questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);

		String query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
		
		String strColumns = removeUTF8BOM(StringUtils.join(headerRow, ","));
		query = StringUtils.replaceOnce(query, KEYS_REGEX, strColumns);
		query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);
		
		logger.info("CSV to DB Query: " + query);

		String[] nextLine;
		PreparedStatement ps = null;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);

			if (truncateBeforeLoad) {
				con.createStatement().execute("DELETE FROM " + tableName);
			}

			final int batchSize = 1000;
			int count = 0;
			Date date = null;
			while ((nextLine = csvReader.readNext()) != null) {

				if (null != nextLine) {
					int index = 1;
					for (String string : nextLine) {
						date = DateUtil.convertToDate(string);
						if (null != date) {
							ps.setDate(index++,
									new java.sql.Date(date.getTime()));
						} else {
							ps.setString(index++, string);
						}
					}
					ps.addBatch();
				}
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch(); // insert remaining records
			con.commit();
		} catch (Exception e) {
			con.rollback();
			logger.error("CSV file import.", e);
			throw new Exception("Error occured while loading data from file to database." + e.getMessage());
		} finally {
			if (null != ps) ps.close();
			if (null != con) con.close();

			csvReader.close();
		}
	}

	/**
	 * Parse CSV file using OpenCSV library and generate Insert sql.
	 * 
	 * @param csvFile Input CSV file
	 * @param tableName Database table name to import data
	 * @throws Exception
	 */
	int cvsTotData = 0;
	public String generateSQL(File csvFile, String tableName) throws Exception {
		String[] headerRow = null;
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
			headerRow = csvReader.readNext();
		} catch (Exception e) {
			logger.error(e);
			throw new Exception("Error occured while executing file. " + e.getMessage());
		} finally {
			csvReader.close();
		}

		if (null == headerRow) {
			throw new FileNotFoundException("No columns defined in given CSV file." + "Please check the CSV file format.");
		}

		String questionmarks = StringUtils.repeat("?,", headerRow.length);
		questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);

		String query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
		query = StringUtils.replaceOnce(query, KEYS_REGEX, StringUtils.join(headerRow, ","));
		query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);
		
		return query;
	}
	
	public static final String UTF8_BOM = "\uFEFF";
	private static String removeUTF8BOM(String s) {
	    if (s.startsWith(UTF8_BOM)) {
	        s = s.substring(1);
	    }
	    return s;
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}
}
