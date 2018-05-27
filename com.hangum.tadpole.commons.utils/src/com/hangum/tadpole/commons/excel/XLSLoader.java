/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     jeongjaehong - POI라이브러리를 이용하여 엑셀파일 가져오기 기능 구현.
 ******************************************************************************/
package com.hangum.tadpole.commons.excel;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.util.DateUtil;

/**
 * Excel file to SQL Script generator
 * 
 * @author jeongjaehong
 *
 */
public class XLSLoader {

	private static final Logger logger = Logger.getLogger(XLSLoader.class);

	private static final String SQL_INSERT = "INSERT INTO ${table} (${keys}) VALUES (${values}) ";
	private static final String SQL_UPDATE = "UPDATE ${table} SET ${values} WHERE 1=1 ${keys} ";
	private static final String SQL_DELETE = "DELETE FROM ${table} WHERE 1=1 ${keys} ";
	private static final String TABLE_REGEX = "${table}";
	private static final String KEYS_REGEX = "${keys}";
	private static final String VALUES_REGEX = "${values}";

	private int batchSize = 1000;
	private String targetSheet = "";
	private boolean isExceptionStop = false;
	private StringBuffer resultLogBuffer = new StringBuffer();
	private String[] headerRow = null;
	private ResultSetMetaData rsmd = null;
	private HashMap<String, Object> rsmdMap = new HashMap<String, Object>();

	private XLSReader xlsReader;

	public XLSLoader(String targetSheet, String batchSize, boolean isExceptionStop) {
		this.isExceptionStop = isExceptionStop;
		try {
			this.batchSize = Integer.parseInt(batchSize);
			this.targetSheet = targetSheet;
		} catch (Exception e) {
			this.batchSize = 1000;
			this.targetSheet = "";
		}
	}

	public StringBuffer getImportResultLog() {
		return this.resultLogBuffer;
	}

	public int loadExcel(final Connection con, final File xlsFile, final String tableName, final String workType,
			final String stmtType, final HashMap<String, Object> keyColumns,
			final List<HashMap<String, String>> disableObjects) throws Exception {

		int count = 0;
		int countSum = 0;
		String[] nextLine;
		String query = "";
		String preProcessQuery = "";
		PreparedStatement ps = null;
		try {
			con.setAutoCommit(false);

			readSourceFile(con, xlsFile, tableName, stmtType, keyColumns);

			// 테이블 복사를 선택하면
			if ("c".equals(workType)) {
				query = makePreparedStatement(tableName + "_COPY", stmtType, keyColumns); //$NON-NLS-1$
				preProcessQuery = "CREATE TABLE " + tableName + "_COPY AS SELECT * FROM " + tableName + " WHERE 1 = 0 ";//$NON-NLS-1$
			} else {
				// insert작업일 경우만
				query = makePreparedStatement(tableName, stmtType, keyColumns);
				resultLogBuffer.append("--------------------------- Delete exists data ---------------------------\n");//$NON-NLS-1$
				if ("i".equals(stmtType)) {
					if ("t".equals(workType)) {
						preProcessQuery = "TRUNCATE TABLE " + tableName;//$NON-NLS-1$
					} else if ("d".equals(workType)) {
						preProcessQuery = "DELETE FROM " + tableName;//$NON-NLS-1$
					}
				}
			}
			resultLogBuffer.append("Execute Query is " + query + "\n");//$NON-NLS-1$
			ps = con.prepareStatement(query);

			if (!"".equals(preProcessQuery)) {
				con.createStatement().execute(preProcessQuery);
				resultLogBuffer.append(" - Execute : " + preProcessQuery + "\n");//$NON-NLS-1$
			}

			resultLogBuffer.append("------------------------------ Object Disable ------------------------------\n");//$NON-NLS-1$

			// 새로운 테이블로 복사해서 import를 진행하는 경우가 아니고 disable처리할 객체가 있으면...
			if (!"c".equals(workType)) {
				if (disableObjects != null && disableObjects.size() > 0) {
					for (HashMap<String, String> map : disableObjects) {
						con.createStatement().execute(map.get("disable_statement").toString());
						resultLogBuffer.append(" - Diable Object : " + map.get("disable_statement").toString() + "\n");//$NON-NLS-1$
					}
				}
			}

			resultLogBuffer.append("---------------------- Start Import Batch ----------------------\n");//$NON-NLS-1$
			while ((nextLine = xlsReader.readNext()) != null) {

				if (null != nextLine) {
					int index = 1;
					if ("i".equals(stmtType) || "u".equals(stmtType)) {
						for (String string : nextLine) {
							int column = (Integer) rsmdMap.get(headerRow[index - 1]);
							// 메타데이터를 읽어서 컬럼의 데이터 타입을 확인후 setString(),
							// setInteger(), setObject()를 사용함.
							setParameterValue(ps, rsmd, column, index++, string);
						}
					}

					// update or delete이면 where 추가 작업.
					if ("u".equals(stmtType) || "d".equals(stmtType)) {
						int headIndex = index;
						if ("d".equals(stmtType)) {
							// delete인 경우는 where절만 있으므로 parameter 인덱스를 1부터 시작한다.
							headIndex = 1;
						}
						int keyIndex = 0;
						for (String string : headerRow) {
							if (keyColumns.containsKey(string)) {
								// pk컬럼인 경우.
								keyIndex = (Integer) keyColumns.get(string);

								String paramValue = nextLine[keyIndex - 1];
								logger.debug("Update where is " + string + "=" + paramValue);//$NON-NLS-1$
								setParameterValue(ps, rsmd, keyIndex, headIndex++, paramValue);
							}
						}
					}

					ps.addBatch();
				}
				if (++count % batchSize == 0) {
					try {
						ps.executeBatch();
						resultLogBuffer.append("\t Execute Batch...\n");//$NON-NLS-1$
						countSum += count;
						count = 0;
					} catch (SQLException e) {
						logger.error("Excel file import.", e);//$NON-NLS-1$

						resultLogBuffer.append(e.getMessage() + "\n");
						SQLException ne = e.getNextException();

						while (ne != null) {
							logger.error("NEXT SQLException is ", ne);//$NON-NLS-1$
							resultLogBuffer.append(ne.getMessage() + "\n");
							ne = ne.getNextException();
						}

						if (this.isExceptionStop) {
							con.rollback();
							resultLogBuffer.append("\t Rollback() - " + count + "Entry.\n");//$NON-NLS-1$
							count = 0;
							break;
						} else {
							con.commit();
							resultLogBuffer.append("\t Commit() - " + count + "Entry.\n");//$NON-NLS-1$
							count = 0;
							continue;
						}
					}
				}
			} // while;

			ps.executeBatch(); // insert remaining records
			resultLogBuffer.append("\t Execute Batch...\n");//$NON-NLS-1$
			con.commit();
			countSum += count;
			resultLogBuffer.append("\t Commit() - Total " + countSum + "Entry.\n");//$NON-NLS-1$

			resultLogBuffer.append("---------------------- Data Import Complete!!! ----------------------\n");//$NON-NLS-1$

			// 새로운 테이블로 복사해서 import를 진행하는 경우가 아니고 disable처리할 객체가 있으면...
			if (!"c".equals(workType)) {
				if (disableObjects != null && disableObjects.size() > 0) {
					for (HashMap<String, String> map : disableObjects) {
						con.createStatement().execute(map.get("enable_statement").toString());
						resultLogBuffer.append(" - Enable Object : " + map.get("enable_statement").toString() + "\n");//$NON-NLS-1$
						con.commit();
					}
				}
			}

			resultLogBuffer.append("================================= End Log =============================\n");//$NON-NLS-1$

			con.setAutoCommit(true);

		} catch (SQLException e) {
			if (this.isExceptionStop) {
				con.rollback();
				resultLogBuffer.append("\t Rollback() - " + count + "Entry.\n");//$NON-NLS-1$
				countSum = 0;
			} else {
				con.commit();
				resultLogBuffer.append("\t Commit() - " + count + "Entry.\n");//$NON-NLS-1$
			}
			logger.error("Excel file import.", e);//$NON-NLS-1$
			resultLogBuffer.append(e.getMessage() + "\n");

			SQLException ne = e.getNextException();
			while (ne != null) {
				logger.error("NEXT SQLException is ", ne);//$NON-NLS-1$
				ne = ne.getNextException();
			}
			// throw new SQLException("Error occured while loading data from
			// file to database." + e.getMessage());
		} catch (Exception e) {
			countSum = 0;
			con.rollback();
			logger.error("Excel file import.", e);//$NON-NLS-1$
			resultLogBuffer.append(e.getMessage() + "\n");//$NON-NLS-1$
			throw new Exception("Error occured while loading data from file to database.\n" + e.getMessage());//$NON-NLS-1$
		} finally {
			if (null != ps)
				ps.close();
			if (null != con)
				con.close();
			// if (xlsReader != null)xlsReader.close();
		}

		return countSum;
	}

	public boolean readSourceFile(final Connection con, final File xlsFile, final String tableName,
			final String stmtType, final HashMap<String, Object> keyColumns) throws Exception {
		try {

			// read bom
			xlsReader = new XLSReader(xlsFile, targetSheet);
			headerRow = xlsReader.readNext();
			int i = 0;
			if (headerRow != null) {
				for (String columnHead : headerRow) {
					headerRow[i++] = columnHead.toLowerCase();
				}
			}

			if (null == headerRow) {
				throw new Exception("No columns defined in given Excel file.\n" + "Please check the Excel file format.");
			}
			// 컬럼헤더가 없는 컬럼이 있으면. ex) id,name,address,,,, 이런형태의 excel 파일의 경우 오류.
			for (String colHead : headerRow) {
				if (colHead == null || "".equals(colHead)) {
					throw new Exception("There is no column names in the first line of the file.\n"
							+ "Please check the Excel file format.");
				}
			}

			if ("u".equals(stmtType) || "d".equals(stmtType)) {
				if (keyColumns.get("all_key_columns") == null //$NON-NLS-1$
						|| ((String[]) keyColumns.get("all_key_columns")).length <= 0) {
					throw new Exception("Primary key not define for Update or Delete.\n"
							+ "Please check the Primarykey information of the target table.");
				}
			}

			// import할 테이블의 데이터 타입별로 파라미터를 설정하기 위해 메타정보를 조회한다.
			if (con != null) {
				rsmd = con.createStatement().executeQuery("select * from " + tableName + " where 1 = 0 ").getMetaData();//$NON-NLS-1$

				for (int colIndex = 1; colIndex <= rsmd.getColumnCount(); colIndex++) {
					rsmdMap.put(rsmd.getColumnName(colIndex).toLowerCase(), colIndex);
				}

				for (String headColumn : headerRow) {
					if (!rsmdMap.containsKey(headColumn.toLowerCase())) {
						// 대상테이블에 존재하지 않는 컬럼이 Excel 파일에서 발견되었습니다.
						throw new Exception(
								"Column that does not exist in the target table was discovered in the Excel file.");
					}
				}

				if (headerRow.length > rsmd.getColumnCount()) {
					throw new Exception("Mismatch of the number of columns and the target table .\n"
							+ "Please check the Excel file format.");
				}

			}
			return true;
		} finally {
			//if (bos != null) bos.close();
		}
	}

	public String makePreparedStatement(final String tableName, final String stmtType,
			final HashMap<String, Object> keyColumns) {

		String query = "";
		String questionmarks = "";
		String updateValues = "";

		try {
			if ("i".equals(stmtType)) {
				questionmarks = StringUtils.repeat("?,", headerRow.length);
				questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
				query = StringUtils.replaceOnce(SQL_INSERT, TABLE_REGEX, tableName);
				query = StringUtils.replaceOnce(query, KEYS_REGEX, StringUtils.join(headerRow, ","));
				query = StringUtils.replaceOnce(query, VALUES_REGEX, questionmarks);

			} else if ("u".equals(stmtType)) {
				updateValues = StringUtils.join(headerRow, " = ?,") + " = ? ";
				questionmarks = StringUtils.join((String[]) keyColumns.get("all_key_columns"), " = ? AND ") + " = ? ";//$NON-NLS-1$
				query = StringUtils.replaceOnce(SQL_UPDATE, TABLE_REGEX, tableName);
				query = StringUtils.replaceOnce(query, VALUES_REGEX, updateValues);
				if (!"".equals(questionmarks)) {
					query = StringUtils.replaceOnce(query, KEYS_REGEX, " AND " + questionmarks);
				} else {
					query = StringUtils.replaceOnce(query, KEYS_REGEX, "");
				}
			} else if ("d".equals(stmtType)) {
				questionmarks = StringUtils.join((String[]) keyColumns.get("all_key_columns"), " = ? AND ") + " = ? ";//$NON-NLS-1$
				query = StringUtils.replaceOnce(SQL_DELETE, TABLE_REGEX, tableName);
				if (!"".equals(questionmarks)) {
					query = StringUtils.replaceOnce(query, KEYS_REGEX, " AND " + questionmarks);
				} else {
					query = StringUtils.replaceOnce(query, KEYS_REGEX, "");
				}
			}

			logger.debug("Excel to DB Query: " + query);//$NON-NLS-1$

			return query;
		} catch (Exception e) {
			logger.error(e);
			return "";
		}
	}

	private void setParameterValue(PreparedStatement ps, ResultSetMetaData rsmd, int columnIndex, int paramIndex,
			String paramValue) {
		Date date;
		try {
			if (paramValue == null || "".equals(paramValue)) {
				ps.setObject(paramIndex, null);
			} else {
				switch (rsmd.getColumnType(columnIndex)) {
				case java.sql.Types.CHAR:
				case java.sql.Types.VARCHAR:
					ps.setString(paramIndex, paramValue);
					break;
				case java.sql.Types.DATE:
					date = DateUtil.convertToDate(paramValue);
					ps.setDate(paramIndex, new java.sql.Date(date.getTime()));
					break;
				case java.sql.Types.TIME:
					date = DateUtil.convertToDate(paramValue);
					ps.setTime(paramIndex, new java.sql.Time(date.getTime()));
					break;
				case java.sql.Types.TIMESTAMP:
					date = DateUtil.convertToDate(paramValue);
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(date.getTime()));
					break;
				case java.sql.Types.SMALLINT:
				case java.sql.Types.INTEGER:
				case java.sql.Types.TINYINT:
					ps.setInt(paramIndex, Integer.parseInt(paramValue.replace(",", "")));
					break;
				case java.sql.Types.BIGINT:
				case java.sql.Types.DECIMAL:
				case java.sql.Types.NUMERIC: /* BIGDECIMAL ?? */
					ps.setLong(paramIndex, Long.parseLong(paramValue.replace(",", "")));
					break;
				case java.sql.Types.FLOAT:
					ps.setFloat(paramIndex, Float.parseFloat(paramValue.replace(",", "")));
					break;
				case java.sql.Types.REAL:
				case java.sql.Types.DOUBLE:
					ps.setDouble(paramIndex, Double.parseDouble(paramValue.replace(",", "")));
					break;
				case java.sql.Types.BOOLEAN:
					ps.setBoolean(paramIndex, Boolean.parseBoolean(paramValue));
					break;
				case java.sql.Types.BIT:
					ps.setBoolean(paramIndex, Boolean.parseBoolean(paramValue));
					break;
				default:
					logger.debug("Data type is " + rsmd.getColumnType(columnIndex) + ":"
							+ rsmd.getColumnTypeName(columnIndex) + "," + paramValue);
					ps.setObject(paramIndex, paramValue);
					break;
				}// switch;;
			}
		} catch (NumberFormatException e) {
			logger.error("PreparedStatement setValue NumberFormatException. ", e);//$NON-NLS-1$
		} catch (SQLException e) {
			logger.error("PreparedStatement setValue SQLException. ", e);//$NON-NLS-1$

			SQLException ne = e.getNextException();
			while (ne != null) {
				logger.error("NEXT SQLException is ", ne);//$NON-NLS-1$
				ne = ne.getNextException();
			}
		} catch (Exception e) {
			logger.debug("Exception value is " + paramValue);//$NON-NLS-1$
			logger.error("PreparedStatement setValue Exception. ", e);//$NON-NLS-1$
		}
	}

	int cvsTotData = 0;

	public String generateSQL(File xlsFile, String tableName, String stmtType, HashMap<String, Object> keyColumns)
			throws Exception {
		try {
			if (readSourceFile(null, xlsFile, tableName, stmtType, keyColumns)) {
				return makePreparedStatement(tableName, stmtType, keyColumns).concat(";");
			}

			return "";
		} catch (Exception e) {
			logger.error(e);
			MessageDialog.openError(null, "Tadpole Excel Import", e.getMessage());//$NON-NLS-1$
			return "";
		} finally {
			// if (xlsFile != null) xlsFile.close();
		}
	}

}