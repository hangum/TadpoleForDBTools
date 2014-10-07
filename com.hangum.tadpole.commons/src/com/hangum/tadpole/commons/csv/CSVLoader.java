package com.hangum.tadpole.commons.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

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
	
	private static final String SQL_INSERT = "INSERT INTO ${table} (${keys}) VALUES (${values}) ";
	private static final String SQL_UPDATE = "UPDATE ${table} SET ${values} WHERE 1=1 ${keys} ";
	private static final String SQL_DELETE = "DELETE FROM ${table} WHERE 1=1 ${keys} ";
	private static final String TABLE_REGEX = "${table}";
	private static final String KEYS_REGEX = "${keys}";
	private static final String VALUES_REGEX = "${values}";

	private char seprator = ',';
	private int batchSize = 1000;
	private boolean isExceptionStop = false;

	public CSVLoader(String separator, String batchSize, boolean isExceptionStop) {
		this.seprator = separator.charAt(0);//String.',';
		this.isExceptionStop = isExceptionStop;
		try{
			this.batchSize = Integer.parseInt(batchSize);
		}catch(Exception e){
			this.batchSize = 1000;
		}
	}
	
	/**
	 * Parse CSV file using OpenCSV library and load in given database table.
	 * 
	 * @param con
	 * @param csvFile Input CSV file
	 * @param tableName Database table name to import data
	 * @param isTruncate trancate before load
	 * @return insert count
	 * @throws Exception
	 */
	public int loadCSV(final Connection con, final File csvFile, final String tableName, final String workType, final String stmtType, final HashMap<String,Object> keyColumns) throws Exception {
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

		//String questionmarks = StringUtils.repeat("?,", headerRow.length);
		//questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);

		//String query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
		//query = StringUtils.replaceOnce(query, KEYS_REGEX, StringUtils.join(headerRow, ","));
		//query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);

		
		String query = "";
		String questionmarks = "";
		String updateValues = "";
		if ("i".equals(stmtType)) {
			questionmarks = StringUtils.repeat("?,", headerRow.length);
			questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
			query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
			query = StringUtils.replaceOnce(query, KEYS_REGEX, StringUtils.join(headerRow, ","));
			query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);
		}else if ("u".equals(stmtType)) {
			updateValues = StringUtils.join(headerRow, " = ?,") + " = ? ";			
			questionmarks = StringUtils.join((String[]) keyColumns.get("all_key_columns"), " = ? AND ") + " = ? ";
			query = StringUtils.replaceOnce(SQL_UPDATE, TABLE_REGEX, tableName);
			query = StringUtils.replaceOnce(query, VALUES_REGEX, updateValues);			
			if (!"".equals(questionmarks)){
				query = StringUtils.replaceOnce(query, KEYS_REGEX, " AND " + questionmarks);				
			}else{
				query = StringUtils.replaceOnce(query, KEYS_REGEX, "");
			}			
		}else if ("d".equals(stmtType)) {			
			questionmarks = StringUtils.join((String[]) keyColumns.get("all_key_columns"), " = ? AND ") + " = ? ";
			query = StringUtils.replaceOnce(SQL_DELETE, TABLE_REGEX, tableName);			
			if (!"".equals(questionmarks)){
				query = StringUtils.replaceOnce(query, KEYS_REGEX, " AND " + questionmarks);				
			}else{
				query = StringUtils.replaceOnce(query, KEYS_REGEX, "");
			}			
		}
		
		
		//if(logger.isDebugEnabled()) logger.info("CSV to DB Query: " + query);
		logger.debug("CSV to DB Query: " + query);
		
		int count = 0;
		String[] nextLine;
		PreparedStatement ps = null;
		ResultSetMetaData rsmd = null;
		try {
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);

			// insert작업일 경우만
			if ("i".equals(stmtType)){
				// 테이블 복사를 선택하면 
				if ("c".equals(workType)){
					try{
						con.createStatement().execute("CREATE TABLE " + tableName + "_COPY AS SELECT * FROM " + tableName + " WHERE 1 = 0 ");
					}catch(Exception e){
						logger.debug("Copy Table Exception : " + e.getMessage());
					}
				}else if ("t".equals(workType)) {
					try{
						con.createStatement().execute("TRUNCATE TABLE " + tableName);
					}catch(Exception e){
						logger.debug("Truncate Table Exception : " + e.getMessage());
					}
				}else if("d".equals(workType)){
					try{
						con.createStatement().execute("DELETE FROM " + tableName);
					}catch(Exception e){
						logger.debug("Delete Table Exception : " + e.getMessage());
					}
				}
			}
			
			rsmd = con.createStatement().executeQuery("select * from " + tableName + " where 1 = 0 ").getMetaData();
			HashMap rsmdMap = new HashMap();
			for (int i=1; i <= rsmd.getColumnCount(); i++){
				rsmdMap.put(rsmd.getColumnName(i), i);				
			}
			

			Date date = null;
			while ((nextLine = csvReader.readNext()) != null) {
				
			//java.util.Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse("2014-09-27 01:19:57.292888");
			//logger.debug(temp);
				
				//TODO:메타데이터를 읽어서 컬럼의 데이터 타입을 확인후 setString(), setInteger(), setObject()를 사용해야함.
				if (null != nextLine) {
					int index = 1;
					if ("i".equals(stmtType) || "u".equals(stmtType)  ) {
						for (String string : nextLine) {
							int column = (Integer)rsmdMap.get(headerRow[index - 1]);
							setParameterValue(ps, rsmd, column, index++, string);
						}
					}
					
					//update or delete이면 where 추가 작업.
					if ("u".equals(stmtType) || "d".equals(stmtType)  ) {
                        int headIndex = index;
                        if ( "d".equals(stmtType) ) {
                        	// delete인 경우는 where절만 있으므로 parameter 인덱스를 1부터 시작한다. 
                        	headIndex = 1;
                        }
                        int keyIndex = 0;
						for (String string : headerRow) {
							if (keyColumns.containsKey(string)) {
								// pk컬럼인 경우. 
								keyIndex = (Integer)keyColumns.get(string);

								String paramValue = nextLine[keyIndex - 1];
								logger.debug("Update where is " + string + "=" + paramValue);
								setParameterValue(ps, rsmd, keyIndex, headIndex++, paramValue);
							}
						}
					}
					
					ps.addBatch();
				}
				if (++count % batchSize == 0) {
					try{
						ps.executeBatch();				
					}catch(SQLException e){
						logger.error("CSV file import.", e);
						MessageDialog.openError(null, "Tadpole CSV Import", e.getMessage());
						SQLException ne = e.getNextException();
						
						while (ne != null){
							logger.error("NEXT SQLException is ", ne);
							MessageDialog.openError(null, "Tadpole CSV Import", ne.getMessage());
							ne = ne.getNextException();
						}

						if (this.isExceptionStop) {
							con.rollback();
							return 0;
						}else{
							con.commit();
							continue;
						}
					}
				}
			}//while;
			
			try{
				ps.executeBatch(); // insert remaining records
				con.commit();
			}catch(SQLException e){
				logger.error("CSV file import.", e);
				MessageDialog.openError(null, "Tadpole CSV Import", e.getMessage());
				SQLException ne = e.getNextException();
				while (ne != null){
					logger.error("NEXT SQLException is ", ne);
					MessageDialog.openError(null, "Tadpole CSV Import", ne.getMessage());
					ne = ne.getNextException();
				}
				
				if (this.isExceptionStop) {
					con.rollback();
					return 0;
				}else{
					con.commit();
				}
			}
			
			con.setAutoCommit(true);

		} catch (SQLException e) {
			count=0;
			con.rollback();
			logger.error("CSV file import.", e);
			
			SQLException ne = e.getNextException();
			while (ne != null){
				logger.error("NEXT SQLException is ", ne);
				ne = ne.getNextException();
			}
			//throw new SQLException("Error occured while loading data from file to database." + e.getMessage());
		} catch (Exception e) {
			count = 0;
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
	
	private void setParameterValue(PreparedStatement ps, ResultSetMetaData rsmd, int columnIndex, int paramIndex, String paramValue){
		Date date;
		try {
			if(paramValue == null || "".equals(paramValue)) {
				ps.setObject(paramIndex, null );
			}else{
				switch  (rsmd.getColumnType(columnIndex)  ) {
					case java.sql.Types.CHAR:
					case java.sql.Types.VARCHAR:
						ps.setString(paramIndex, paramValue);
						break;
					case java.sql.Types.DATE:
						date = DateUtil.convertToDate(paramValue);
						ps.setDate(paramIndex, new java.sql.Date(date.getTime()) );
						break;
					case java.sql.Types.TIME:
						date = DateUtil.convertToDate(paramValue);
						ps.setTime(paramIndex, new java.sql.Time(date.getTime()) );									
						break;
					case java.sql.Types.TIMESTAMP:
						date = DateUtil.convertToDate(paramValue);
						ps.setTimestamp(paramIndex, new java.sql.Timestamp(date.getTime()) );									
						break;
					case java.sql.Types.SMALLINT:
					case java.sql.Types.INTEGER:
						ps.setInt(paramIndex, Integer.parseInt(paramValue));
						break;
					case java.sql.Types.BOOLEAN:
						ps.setBoolean(paramIndex, Boolean.parseBoolean(paramValue));
						break;
					case java.sql.Types.BIT:
						ps.setBoolean(paramIndex, Boolean.parseBoolean(paramValue));
						break;
					default :
						logger.debug("Data type is " + rsmd.getColumnType(columnIndex) + ":" + rsmd.getColumnTypeName(columnIndex) + "," + paramValue);
						ps.setObject(paramIndex, paramValue);
						break;									
				}//switch;;
			}
		} catch (NumberFormatException e) {
			logger.error("PreparedStatement setValue NumberFormatException. ", e);
		} catch (SQLException e) {
			logger.error("PreparedStatement setValue SQLException. ", e);
			
			SQLException ne = e.getNextException();
			while (ne != null){
				logger.error("NEXT SQLException is ", ne);
				ne = ne.getNextException();
			}
		} catch (Exception e) {
			logger.debug("Exception value is " + paramValue);
			logger.error("PreparedStatement setValue Exception. ", e);
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
	public String generateSQL(File csvFile, String tableName, String stmtType, HashMap<String,Object> keyColumns) throws Exception {
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

		
		String query = "";
		String questionmarks = "";
		String updateValues = "";
		if ("i".equals(stmtType)) {
			questionmarks = StringUtils.repeat("?,", headerRow.length);
			questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
			query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
			query = StringUtils.replaceOnce(query, KEYS_REGEX, StringUtils.join(headerRow, ","));
			query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);
		}else if ("u".equals(stmtType)) {
			updateValues = StringUtils.join(headerRow, " = ?,") + " = ? ";			
			questionmarks = StringUtils.join((String[]) keyColumns.get("all_key_columns"), " = ? AND ") + " = ? ";
			query = StringUtils.replaceOnce(SQL_UPDATE, TABLE_REGEX, tableName);
			query = StringUtils.replaceOnce(query, VALUES_REGEX, updateValues);			
			if (!"".equals(questionmarks)){
				query = StringUtils.replaceOnce(query, KEYS_REGEX, " AND " + questionmarks);				
			}else{
				query = StringUtils.replaceOnce(query, KEYS_REGEX, "");
			}			
		}else if ("d".equals(stmtType)) {			
			questionmarks = StringUtils.join((String[]) keyColumns.get("all_key_columns"), " = ? AND ") + " = ? ";
			query = StringUtils.replaceOnce(SQL_DELETE, TABLE_REGEX, tableName);			
			if (!"".equals(questionmarks)){
				query = StringUtils.replaceOnce(query, KEYS_REGEX, " AND " + questionmarks);				
			}else{
				query = StringUtils.replaceOnce(query, KEYS_REGEX, "");
			}			
		}
		return query + ";";
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}
}
