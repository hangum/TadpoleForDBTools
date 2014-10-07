package com.hangum.tadpole.commons.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	private StringBuffer resultLogBuffer = new StringBuffer();
	private CSVReader csvReader = null;
	private String[] headerRow = null;
	private ResultSetMetaData rsmd = null;
	private HashMap<String,Object> rsmdMap = new HashMap<String,Object>();



	public CSVLoader(String separator, String batchSize, boolean isExceptionStop) {
		this.seprator = separator.charAt(0);//String.',';
		this.isExceptionStop = isExceptionStop;
		try{
			this.batchSize = Integer.parseInt(batchSize);
		}catch(Exception e){
			this.batchSize = 1000;
		}
	}
	
	public StringBuffer getImportResultLog(){
		return this.resultLogBuffer;
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
	public int loadCSV(final Connection con, final File csvFile, final String tableName, final String workType, final String stmtType, final HashMap<String,Object> keyColumns, final List<HashMap<String, String>> disableObjects) throws Exception {
		int count = 0;
		int countSum = 0;
		String[] nextLine;
		String query="";
		String preProcessQuery = "";
		PreparedStatement ps = null;
		ResultSetMetaData rsmd = null;
		try {
			con.setAutoCommit(false);
			
			readSourceFile(con, csvFile, tableName, stmtType, keyColumns);

			resultLogBuffer.append("--------------------------- Make execute query ---------------------------\n");
			query = makePreparedStatement(tableName, stmtType, keyColumns);	
			resultLogBuffer.append("Execute Query is " + query + "\n");
			ps = con.prepareStatement(query);
			
			resultLogBuffer.append("--------------------------- Delete exists data ---------------------------\n");
			// 테이블 복사를 선택하면 
			if ("c".equals(workType)){
				preProcessQuery = "CREATE TABLE " + tableName + "_COPY AS SELECT * FROM " + tableName + " WHERE 1 = 0 ";
			}else {
				// insert작업일 경우만
				if ("i".equals(stmtType)){
					if ("t".equals(workType)) {
						preProcessQuery = "TRUNCATE TABLE " + tableName;
					}else if("d".equals(workType)){
						preProcessQuery = "DELETE FROM " + tableName;
					}
				}
			}
			
			if(!"".equals(preProcessQuery)){
				con.createStatement().execute(preProcessQuery);
				resultLogBuffer.append(" - Execute : " + preProcessQuery + "\n");
			}
			
			resultLogBuffer.append("------------------------------ Object Disable ------------------------------\n");
			
			// 새로운 테이블로 복사해서 import를 진행하는 경우가 아니고 disable처리할 객체가 있으면...
			if (!"c".equals(workType)){
				if (disableObjects !=null && disableObjects.size() > 0){
					for (HashMap<String, String> map : disableObjects){
						con.createStatement().execute(map.get("disable_statement").toString());
						resultLogBuffer.append(" - Diable Object : " + map.get("disable_statement").toString() + "\n");
					}
				}
			}

			resultLogBuffer.append("---------------------- Start Import Batch ----------------------\n");
			while ((nextLine = csvReader.readNext()) != null) {
				
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
						resultLogBuffer.append("\t Execute Batch...\n");
					}catch(SQLException e){
						logger.error("CSV file import.", e);
						
						//MessageDialog.openError(null, "Tadpole CSV Import", e.getMessage());
						resultLogBuffer.append(e.getMessage()+"\n");
						SQLException ne = e.getNextException();
						
						while (ne != null){
							logger.error("NEXT SQLException is ", ne);
							//MessageDialog.openError(null, "Tadpole CSV Import", ne.getMessage());
							resultLogBuffer.append(ne.getMessage()+"\n");
							ne = ne.getNextException();
						}

						if (this.isExceptionStop) {
							con.rollback();
							count = 0;
						}else{
							con.commit();
							countSum += count;
							count = 0;
							continue;
						}
					}
				}
			}//while;
			
			ps.executeBatch(); // insert remaining records
			resultLogBuffer.append("\t Execute Batch...\n");
			con.commit();
			countSum += count;
			
			resultLogBuffer.append("---------------------- Data Import Complete!!! ----------------------\n");
			
			// 새로운 테이블로 복사해서 import를 진행하는 경우가 아니고 disable처리할 객체가 있으면...
			if (!"c".equals(workType)){
				if (disableObjects !=null && disableObjects.size() > 0){
					for (HashMap<String, String> map : disableObjects){
						con.createStatement().execute(map.get("enable_statement").toString());
						resultLogBuffer.append(" - Enable Object : " + map.get("enable_statement").toString() + "\n");
						con.commit();
					}
				}
			}
			
			resultLogBuffer.append("================================= End Log =============================\n");

			con.setAutoCommit(true);

		} catch (SQLException e) {
			if (this.isExceptionStop) {
				con.rollback();
				countSum = 0;
			}else{
				con.commit();
			}
			logger.error("CSV file import.", e);
			resultLogBuffer.append(e.getMessage()+"\n");
			
			SQLException ne = e.getNextException();
			while (ne != null){
				logger.error("NEXT SQLException is ", ne);
				ne = ne.getNextException();
			}
			//throw new SQLException("Error occured while loading data from file to database." + e.getMessage());
		} catch (Exception e) {
			countSum = 0;
			con.rollback();
			logger.error("CSV file import.", e);
			resultLogBuffer.append(e.getMessage()+"\n");
			throw new Exception("Error occured while loading data from file to database.\n" + e.getMessage());
		} finally {
			if (null != ps) ps.close();
			if (null != con) con.close();
			if (csvReader != null) csvReader.close();
		}
		
		return countSum;
	}
	
	public boolean readSourceFile(final Connection con, final File csvFile, final String tableName, final String stmtType, final HashMap<String,Object> keyColumns) throws Exception {
		BOMInputStream bos = null; 
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

			if (null == headerRow) {
				throw new Exception("No columns defined in given CSV file.\n" + "Please check the CSV file format.");
			}
			// 컬럼헤더가 없는 컬럼이 있으면.  ex) id,name,address,,,,   이런형태의 csv파일의 경우 오류.
			for (String colHead : headerRow) {
				if (colHead == null || "".equals(colHead)){
					throw new Exception( "There is no column names in the first line of the file.\n" + "Please check the CSV file format.");
				}
			}

			if ("u".equals(stmtType)||"d".equals(stmtType)) {
				if (keyColumns.get("all_key_columns") == null || ((String[]) keyColumns.get("all_key_columns")).length <= 0){
					throw new Exception( "Primary key not define for Update or Delete.\n" + "Please check the Primarykey information of the target table.");
				}			
			}

			// import할 테이블의 데이터 타입별로 파라미터를 설정하기 위해 메타정보를 조회한다.
			if (con != null){
				rsmd = con.createStatement().executeQuery("select * from " + tableName + " where 1 = 0 ").getMetaData();
				if (headerRow.length != rsmd.getColumnCount()) {
					throw new Exception( "Mismatch of the number of columns and the target table .\n" + "Please check the CSV file format.");
				}
	
				for (int i=1; i <= rsmd.getColumnCount(); i++){
					rsmdMap.put(rsmd.getColumnName(i), i);				
				}
			}
			return true;
		} finally {
			if(bos != null) bos.close();
		}
	}
	
	public String makePreparedStatement(final String tableName, final String stmtType, final HashMap<String,Object> keyColumns) {
		
		String query = "";
		String questionmarks = "";
		String updateValues = "";
		
		try{
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
			
			logger.debug("CSV to DB Query: " + query);
			
			return query;
		} catch (Exception e) {
			logger.error(e);
			return "";
		}
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
		try{
			if(readSourceFile(null, csvFile, tableName, stmtType, keyColumns)) {
				return makePreparedStatement(tableName, stmtType, keyColumns).concat(";");
			}
			
			return "";
		} catch (Exception e) {
			logger.error(e);
			MessageDialog.openError(null, "Tadpole CSV Import", e.getMessage());
			return "";
		} finally {
			if (csvReader != null) csvReader.close();
		}
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}
}
