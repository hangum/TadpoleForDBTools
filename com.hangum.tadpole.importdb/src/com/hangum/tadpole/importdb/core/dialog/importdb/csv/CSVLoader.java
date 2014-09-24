package com.hangum.tadpole.importdb.core.dialog.importdb.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.hangum.tadpole.commons.util.UnicodeBOMInputStream;

/**
 * csv loader 
 * 
 * @see original file location is (http://viralpatel.net/blogs/java-load-csv-file-to-database/)
 * 
 */
public class CSVLoader {
	private static final Logger logger = Logger.getLogger(CSVLoader.class);
	
	private static final String SQL_INSERT = "INSERT INTO ${table} (${keys}) VALUES (${values})";
	private static final String TABLE_REGEX = "${table}";
	private static final String KEYS_REGEX = "${keys}";
	private static final String VALUES_REGEX = "${values}";

	private char seprator;
	private int batchSize = 1000;

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
	 * @return insert count
	 * @throws Exception
	 */
	public int loadCSV(final Connection con, final File csvFile, final String tableName, final boolean truncateBeforeLoad) throws Exception {
		BOMInputStream bos = null; 
		CSVReader csvReader = null;
		String[] headerRow = null;
		try {
			// find bom
			bos = new BOMInputStream(new FileInputStream(csvFile), false, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32BE);
			String charset = "utf-8";
			if(bos.hasBOM()) {
				charset = bos.getBOMCharsetName();
			}
			InputStream cleanStream = new UnicodeBOMInputStream(new FileInputStream(csvFile)).skipBOM();
			
			// read bom 
			csvReader = new CSVReader(new InputStreamReader(cleanStream, charset), this.seprator);
			headerRow = csvReader.readNext();
		} catch (Exception e) {
			logger.error(e);
			throw new Exception("Error occured while executing file. " + e.getMessage());
		} finally {
			if(bos != null) bos.close();
		}

		if (null == headerRow) {
			throw new FileNotFoundException("No columns defined in given CSV file." + "Please check the CSV file format.");
		}

		String questionmarks = StringUtils.repeat("?,", headerRow.length);
		questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);

		String query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
		query = StringUtils.replaceOnce(query, KEYS_REGEX, StringUtils.join(headerRow, ","));
		query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);
		
		if(logger.isDebugEnabled()) logger.info("CSV to DB Query: " + query);

		int count = 0;
		String[] nextLine;
		PreparedStatement ps = null;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);

			if (truncateBeforeLoad) {
				con.createStatement().execute("DELETE FROM " + tableName);
			}

			Date date = null;
			while ((nextLine = csvReader.readNext()) != null) {

				if (null != nextLine) {
					int index = 1;
					for (String string : nextLine) {
						date = DateUtil.convertToDate(string);
						if (null != date) {
							ps.setDate(index++, new java.sql.Date(date.getTime()));
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
			if (csvReader != null) csvReader.close();
		}
		
		return count;
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
		BOMInputStream bos = null; 
				
		String[] headerRow = null;
		CSVReader csvReader = null;
		try {
			// find bom
			bos = new BOMInputStream(new FileInputStream(csvFile), false, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32BE);
			String charset = "utf-8";
			if(bos.hasBOM()) {
				charset = bos.getBOMCharsetName();
			}
			InputStream cleanStream = new UnicodeBOMInputStream(new FileInputStream(csvFile)).skipBOM();
			
			// read bom 
			csvReader = new CSVReader(new InputStreamReader(cleanStream, charset), this.seprator);
			headerRow = csvReader.readNext();
		} catch (Exception e) {
			logger.error(e);
			throw new Exception("Error occured while executing file. " + e.getMessage());
		} finally {
			if(bos != null) bos.close();
			if (csvReader != null) csvReader.close();
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

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}
}
