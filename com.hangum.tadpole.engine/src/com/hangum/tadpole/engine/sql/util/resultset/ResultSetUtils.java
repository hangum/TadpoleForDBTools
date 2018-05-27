/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.resultset;

import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;

/**
 * ResultSet utils
 * 
 * @author hangum
 * 
 */
public class ResultSetUtils {
	private static final Logger logger = Logger.getLogger(ResultSetUtils.class);
	
	/**
	 * ResultSet to List
	 * 
	 * @param userDB
	 * @param rs
	 * @param limitCount
	 * @return
	 * @throws SQLException
	 */
	public static TadpoleResultSet getResultToList(final UserDBDAO userDB, final ResultSet rs, final int limitCount) throws SQLException {
		return getResultToList(userDB, false, rs, limitCount, 0);
	}
	
	/**
	 * ResultSet to List
	 * 
	 * @param userDB
	 * @param isShowRowNum 첫번째 컬럼의 로우 넘버를 추가할 것인지.
	 * @param rs ResultSet
	 * @param limitCount 
	 * @param intLastIndex
	 * @return
	 * @throws SQLException
	 */
	public static TadpoleResultSet getResultToList(final UserDBDAO userDB, final boolean isShowRowNum, final ResultSet rs, final int limitCount, int intLastIndex) throws SQLException {
		TadpoleResultSet returnRS = new TadpoleResultSet();
		Map<Integer, Object> tmpRow = null;
		
		// 결과를 프리퍼런스에서 처리한 맥스 결과 만큼만 거져옵니다.
		int rowCnt = intLastIndex;
		while(rs.next()) {
			tmpRow = new HashMap<Integer, Object>();
			
			int intStartIndex = 0;
			if(isShowRowNum) {
				intStartIndex++;
				tmpRow.put(0, rowCnt+1);
			}
			
			for(int i=0; i<rs.getMetaData().getColumnCount(); i++) {
				final int intColIndex = i+1;
				final int intShowColIndex = i + intStartIndex;
				final int colType = rs.getMetaData().getColumnType(intColIndex); 
//				if(logger.isDebugEnabled()) logger.debug("[column type] --> " + colType);
				
				try {
					if (java.sql.Types.LONGVARCHAR == colType || 
							java.sql.Types.LONGNVARCHAR == colType || 
							java.sql.Types.CLOB == colType || 
							java.sql.Types.NCLOB == colType){
						StringBuffer sb = new StringBuffer();						  
						Reader is =  rs.getCharacterStream(intColIndex);						
						if (is != null) {
							int cnum = 0;
							char[] cbuf = new char[10];							 
							while ((cnum = is.read(cbuf)) != -1) sb.append(cbuf, 0 ,cnum);
						} // if

						tmpRow.put(intShowColIndex, SQLConvertCharUtil.toClient(userDB, sb.toString()));
					} else if(java.sql.Types.BLOB == colType || java.sql.Types.STRUCT == colType) {
						tmpRow.put(intShowColIndex, rs.getObject(intColIndex));
						
					} else if(java.sql.Types.DOUBLE == colType) {
						// 숫자가 클 경우 오류가 납니다. 
						// 하여서 소숫점이 없다면 long으로 받아 처리해야하고, 소숫점이 있다면 
//						double dblValue = rs.getDouble(intColIndex);
						tmpRow.put(intShowColIndex, rs.getDouble(intColIndex));
						
					} else if(java.sql.Types.BIGINT == colType) {
						tmpRow.put(intShowColIndex, rs.getLong(intColIndex));

					} else if(java.sql.Types.DECIMAL == colType || java.sql.Types.NUMERIC == colType) {
						tmpRow.put(intShowColIndex, rs.getBigDecimal(intColIndex));
					
					} else if(java.sql.Types.INTEGER == colType) {
						tmpRow.put(intShowColIndex, rs.getInt(intColIndex));
						
					} else if(java.sql.Types.FLOAT == colType || java.sql.Types.REAL == colType) {
						tmpRow.put(intShowColIndex, rs.getFloat(intColIndex));
						
					} else {
						tmpRow.put(intShowColIndex, SQLConvertCharUtil.toClient(userDB, rs.getString(intColIndex)));
					}
				} catch(Exception e) {
					logger.error(String.format("ResutSet fetch error :%s", e)); //$NON-NLS-1$
					tmpRow.put(i+intStartIndex, ""); //$NON-NLS-1$
				}
			}
			
			returnRS.getData().add(tmpRow);
			
			// 쿼리 검색 결과 만큼만 결과셋을 받습니다. (hive driver는 getRow를 지원하지 않습니다) --;; 2013.08.19, hangum
			if(limitCount == (rowCnt+1)) {
				returnRS.setEndOfRead(false);
				break;
			}
			
			rowCnt++;
		}
		
		return returnRS;
	}
	
	/**
	 * get column type
	 * 
	 * @param rsm
	 * @return
	 * @throws SQLException
	 */
	public static Map<Integer, Integer> getColumnType(ResultSetMetaData rsm) throws SQLException {
		return getColumnType(false, rsm);
	}
	
	/**
	 * column of type
	 * 
	 * @param isShowRowNum 로우넘 보여주기위해 첫번째 컬럼을 추가하는 데이터를 만듭니다.
	 * @param rsm
	 * @return
	 * @throws SQLException
	 */
	public static Map<Integer, Integer> getColumnType(boolean isShowRowNum, ResultSetMetaData rsm) throws SQLException {
		Map<Integer, Integer> mapColumnType = new HashMap<Integer, Integer>();
		int intStartIndex = 0;
		
		if(isShowRowNum) {
			intStartIndex++;
			mapColumnType.put(0, java.sql.Types.INTEGER);
		}
		
		for(int i=0;i<rsm.getColumnCount(); i++) {
//			logger.debug("\t ==[column start]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
//			logger.debug("\tColumnLabel  		:  " 	+ rsm.getColumnLabel(i+1));
			
//			logger.debug("\t AutoIncrement  	:  " 	+ rsm.isAutoIncrement(i+1));
//			logger.debug("\t Nullable		  	:  " 	+ rsm.isNullable(i+1));
//			logger.debug("\t CaseSensitive  	:  " 	+ rsm.isCaseSensitive(i+1));
//			logger.debug("\t Currency		  	:  " 	+ rsm.isCurrency(i+1));
//			
//			logger.debug("\t DefinitelyWritable :  " 	+ rsm.isDefinitelyWritable(i+1));
//			logger.debug("\t ReadOnly		  	:  " 	+ rsm.isReadOnly(i+1));
//			logger.debug("\t Searchable		  	:  " 	+ rsm.isSearchable(i+1));
//			logger.debug("\t Signed			  	:  " 	+ rsm.isSigned(i+1));
////			logger.debug("\t Currency		  	:  " 	+ rsm.isWrapperFor(i+1));
//			logger.debug("\t Writable		  	:  " 	+ rsm.isWritable(i+1));
//			
//			logger.debug("\t ColumnClassName  	:  " 	+ rsm.getColumnClassName(i+1));
//			logger.debug("\t CatalogName  		:  " 	+ rsm.getCatalogName(i+1));
//			logger.debug("\t ColumnDisplaySize  :  " 	+ rsm.getColumnDisplaySize(i+1));
//			logger.debug("\t ColumnType  		:  " 	+ rsm.getColumnType(i+1));
//			logger.debug("\t ColumnTypeName 	:  " 	+ rsm.getColumnTypeName(i+1));
			//
			// mysql json 타입의 자바 변수가 1로 매칭 되어 있어서, 이것을 pgsql의 json 타입의 값인 1111로 매칭합니다.
			//							- 2015.10.21 mysql 5.7
			if(StringUtils.equalsIgnoreCase("json", rsm.getColumnTypeName(i+1))) {
				mapColumnType.put(i+intStartIndex, 1111);
			} else {
				mapColumnType.put(i+intStartIndex, rsm.getColumnType(i+1));
			}
//			logger.debug("\t Column Label " + rsm.getColumnLabel(i+1) );
			
//			logger.debug("\t Precision 			:  " 	+ rsm.getPrecision(i+1));
//			logger.debug("\t Scale			 	:  " 	+ rsm.getScale(i+1));
//			logger.debug("\t SchemaName		 	:  " 	+ rsm.getSchemaName(i+1));
//			logger.debug("\t TableName		 	:  " 	+ rsm.getTableName(i+1));
//			logger.debug("\t ==[column end]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
		}
		
		return mapColumnType; 
	}
	
	/**
	 * column name of table.
	 * but this method is db access control.
	 * 
	 * @param userDB
	 * @param columnTableName
	 * @param isShowRownum
	 * @param rs
	 * @return
	 */
	public static Map<Integer, String> getColumnName(UserDBDAO userDB, Map<Integer, String> columnTableName,
			boolean isShowRowNum, ResultSet rs) throws SQLException {
		return getColumnName(isShowRowNum, rs);
//		Map<Integer, String> mapColumnName = getColumnName(isShowRowNum, rs);
//		DBAccessControlDAO dbAccessCtlDao = userDB.getDbAccessCtl();
//		Map<String, AccessCtlObjectDAO> mapDetailCtl = dbAccessCtlDao.getMapSelectAccessCtl();
//		
//		if(!mapDetailCtl.isEmpty()) {
//			Map<Integer, String> mapReturnColumnName = new HashMap<Integer, String>();
//			int intColumnCnt = 0;
//
//			// 컬럼 중에 db access 관련 있는 테이블이 있는 지 검증합니다.
//			for(int i=0; i<mapColumnName.size(); i++) {
//				String strTableName = columnTableName.get(i);
//				
//				// Is filter column?
//				if(mapDetailCtl.containsKey(strTableName)) {
//					// is filter table?
//					AccessCtlObjectDAO acDao = mapDetailCtl.get(strTableName);
//					logger.debug("----- table filter-----");
////					String strTableOfAccessColumns = acDao.getDetail_obj();
////					String strResultColumn = mapColumnName.get(i);
////					if(StringUtils.containsIgnoreCase(strTableOfAccessColumns, strResultColumn)
////							| acDao.getDontuse_object().equals("YES")
////					) {
//////						if(logger.isDebugEnabled()) logger.debug("This colum is remove stauts " + strResultColumn);
////					} else {
//////						if(logger.isDebugEnabled()) logger.debug("This colum is normal stauts " + strResultColumn);
////						mapReturnColumnName.put(intColumnCnt, mapColumnName.get(i));
////						intColumnCnt++;
////					}
//				} else {
//					mapReturnColumnName.put(intColumnCnt, mapColumnName.get(i));
//					intColumnCnt++;
//				}
//			}
//			
//			return mapReturnColumnName;
//		} else {
//			return mapColumnName;			
//		}
	}
	
	/**
	 * column name of table
	 * 
	 * @param isShowRowNum
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getColumnName(boolean isShowRowNum, ResultSet rs) throws SQLException {
		Map<Integer, String> mapColumnName = new HashMap<Integer, String>();
		int intStartIndex = 0;
		
		if(isShowRowNum) {
			intStartIndex++;
			mapColumnName.put(0, "#");
		}
		
		ResultSetMetaData rsm = rs.getMetaData();
		for(int i=0; i<rsm.getColumnCount(); i++) {
			mapColumnName.put(i+intStartIndex, rsm.getColumnName(i+1));
		}
		
		return mapColumnName;
	}
	
	/**
	 * get column label name
	 * 
	 * @param userDB
	 * @param columnTableName
	 * @param isShowRowNum
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getColumnLabelName(UserDBDAO userDB, Map<Integer, String> columnTableName, boolean isShowRowNum, ResultSet rs) throws SQLException {
		return getColumnLabelName(isShowRowNum, rs);
//		Map<Integer, String> mapColumnName = getColumnLabelName(isShowRowNum, rs);
//		DBAccessControlDAO dbAccessCtlDao = userDB.getDbAccessCtl();
//		Map<String, AccessCtlObjectDAO> mapDetailCtl = dbAccessCtlDao.getMapSelectAccessCtl();
//		
//		if(!mapDetailCtl.isEmpty()) {
//			Map<Integer, String> mapReturnColumnName = new HashMap<Integer, String>();
//			int intColumnCnt = 0;
//		
//			// 컬럼 중에 db access 관련 있는 테이블이 있는 지 검증합니다.
//			for(int i=0; i<mapColumnName.size(); i++) {
////				String strTableName = columnTableName.get(i);
////				
////				// Is filter column?
////				if(mapDetailCtl.containsKey(strTableName)) {
////					// is filter table?
////					AccessCtlObjectDAO acDao = mapDetailCtl.get(strTableName);
////					String strTableOfAccessColumns = acDao.getDetail_obj();
////					String strResultColumn = mapColumnName.get(i);
////					if(StringUtils.containsIgnoreCase(strTableOfAccessColumns, strResultColumn)
////							| acDao.getDontuse_object().equals("YES")
////					) {
////		//				if(logger.isDebugEnabled()) logger.debug("This colum is remove stauts " + strResultColumn);
////					} else {
////		//				if(logger.isDebugEnabled()) logger.debug("This colum is normal stauts " + strResultColumn);
////						mapReturnColumnName.put(intColumnCnt, mapColumnName.get(i));
////						intColumnCnt++;
////					}
////				} else {
//					mapReturnColumnName.put(intColumnCnt, mapColumnName.get(i));
//					intColumnCnt++;
////				}
//			}
//			
//			return mapReturnColumnName;
//		} else {
//			return mapColumnName;			
//		}
	}
	
	/**
	 * column label name of table
	 * 
	 * @param isShowRowNum
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getColumnLabelName(boolean isShowRowNum, ResultSet rs) throws SQLException {
		Map<Integer, String> mapColumnName = new HashMap<Integer, String>();
		int intStartIndex = 0;
		
		if(isShowRowNum) {
			intStartIndex++;
			mapColumnName.put(0, "#");
		}
		
		ResultSetMetaData rsm = rs.getMetaData();
		for(int i=0; i<rsm.getColumnCount(); i++) {
			mapColumnName.put(i+intStartIndex, rsm.getColumnLabel(i+1));
		}
		
		return mapColumnName;
	}
	
	public static Map<Integer, String> getColumnTableName(final UserDBDAO userDB, ResultSet rs) throws Exception {
		return getColumnTableName(userDB, false, rs);
	}
	
	/**
	 * 컬럼에 table name
	 * 
	 * @param isShowRowNum
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getColumnTableName(final UserDBDAO userDB, boolean isShowRowNum, ResultSet rs) throws SQLException {
		Map<Integer, String> mapColumnName = new HashMap<Integer, String>();
		int intStartIndex = 0;
		
		if(isShowRowNum) {
			intStartIndex++;
			mapColumnName.put(0, "#");
		}
		
		ResultSetMetaData rsm = rs.getMetaData();
		for(int i=0; i<rsm.getColumnCount(); i++) {
//			if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
//				PGResultSetMetaData pgsqlMeta = (PGResultSetMetaData)rsm;
//				mapColumnName.put(i+intStartIndex, pgsqlMeta.getBaseTableName(i+1));
//				
////				if(logger.isDebugEnabled()) logger.debug("Table name is " + pgsqlMeta.getBaseTableName(i+1));
//			} else
			if(DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) {
				mapColumnName.put(i+intStartIndex, "Apache Hive is not support this method.");
			} else {
				if(rsm.getSchemaName(i+1) == null || "".equals(rsm.getSchemaName(i+1))) {
//					if(logger.isDebugEnabled()) logger.debug("Table name is " + rsm.getTableName(i+1) + ", schema name is " + rsm.getSchemaName(i+1));
					
					mapColumnName.put(i+intStartIndex, rsm.getTableName(i+1));
				} else {
					mapColumnName.put(i+intStartIndex, rsm.getSchemaName(i+1) + "." + rsm.getTableName(i+1));
				}
			}
		}
		
		return mapColumnName;
	}

	/**
	 * metadata를 바탕으로 결과를 컬럼 정보를 수집힌다.
	 * 
	 * @param rs
	 * @return index순번에 컬럼명
	 */
	public static Map<Integer, String> getColumnName(ResultSet rs) throws Exception {
		return getColumnName(false, rs);
	}

//	/**
//	 * 쿼리결과의 실제 테이블 컬럼 정보를 넘겨 받습니다.
//	 * 현재는 pgsql 만 지원합니다.
//	 * 
//	 * mysql, maria, oracle의 경우는 테이블 alias가 붙은 경우 이름을 처리하지 못합니다.
//	 * 다른 디비는 테스트 해봐야합니다.
//	 * 2014-11-13 
//	 * 
//	 * @param rsm
//	 * @return
//	 * @throws SQLException
//	 */
//	public static Map<Integer, Map> getColumnTableColumnName(UserDBDAO userDB, ResultSetMetaData rsm) {
//		Map<Integer, Map> mapTableColumn = new HashMap<Integer, Map>();
//		
//		// 첫번째 컬럼 순번을 위해 삽입.
//		mapTableColumn.put(0, new HashMap());
//			
//		try {
//			if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
//				PGResultSetMetaData pgsqlMeta = (PGResultSetMetaData)rsm;
//				for(int i=0;i<rsm.getColumnCount(); i++) {
//					int columnSeq = i+1;
//					Map<String, String> metaData = new HashMap<String, String>();
//					metaData.put("schema", pgsqlMeta.getBaseSchemaName(columnSeq));
//					metaData.put("table", pgsqlMeta.getBaseTableName(columnSeq));
//					metaData.put("column", pgsqlMeta.getBaseColumnName(columnSeq));
//					metaData.put("type", 	""+rsm.getColumnType(columnSeq));
//					metaData.put("typeName", 	""+rsm.getColumnTypeName(columnSeq));
//					
////					if(logger.isDebugEnabled()) {
////						logger.debug("\tschema :" + pgsqlMeta.getBaseSchemaName(columnSeq) + "\ttable:" + pgsqlMeta.getBaseTableName(columnSeq) + "\tcolumn:" + pgsqlMeta.getBaseColumnName(columnSeq));
////					}
//					
//					mapTableColumn.put(i+1, metaData);
//				}
//				
////			/**
////			 * table name alia
////			 * 
////			 */
////			} else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT ||
////							userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT
////			) {
//////				com.mysql.jdbc.ResultSetMetaData mysqlMeta = (com.mysql.jdbc.ResultSetMetaData)rsm;
////				org.mariadb.jdbc.MySQLResultSetMetaData mysqlMeta = (org.mariadb.jdbc.MySQLResultSetMetaData)rsm;
////				for(int i=0;i<rsm.getColumnCount(); i++) {
////					int columnSeq = i+1;
////					Map<String, String> metaData = new HashMap<String, String>();
////					if(logger.isDebugEnabled()) {
////						logger.debug("\tschema :" + mysqlMeta.getCatalogName(columnSeq) + "\ttable:" + mysqlMeta.getTableName(columnSeq) + "\tcolumn:" + mysqlMeta.getColumnName(columnSeq));
////					}
////					
////					metaData.put("schema", mysqlMeta.getCatalogName(columnSeq));
////					metaData.put("table", mysqlMeta.getTableName(columnSeq));
////					metaData.put("column", mysqlMeta.getColumnName(columnSeq));
////					
////					mapTableColumn.put(i+1, metaData);
////				}
//				
//			} else if(userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT 
//						|| userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT
//						|| userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT							
//				) {
//				for(int i=0;i<rsm.getColumnCount(); i++) {
//					int columnSeq = i+1;
//					Map<String, String> metaData = new HashMap<String, String>();
//					metaData.put("schema", 	rsm.getSchemaName(columnSeq));
//					metaData.put("table", 	rsm.getTableName(columnSeq));
//					metaData.put("column", 	rsm.getColumnName(columnSeq));
//					metaData.put("type", 	""+rsm.getColumnType(columnSeq));
//					metaData.put("typeName", 	""+rsm.getColumnTypeName(columnSeq));
//					
////					if(logger.isDebugEnabled()) {
////						logger.debug("\tschema :" + rsm.getSchemaName(columnSeq) + "\ttable:" + rsm.getTableName(columnSeq) + "\tcolumn:" + rsm.getColumnName(columnSeq)
////						 + "\ttype : " + rsm.getColumnType(columnSeq) + "\ttypename : " + rsm.getColumnTypeName(columnSeq))
////						;
////					}
//					
//					mapTableColumn.put(i+1, metaData);
//				}
//			}
//		} catch(Exception e) {
//			logger.error("resultset metadata exception", e);
//		}
//		
//		return mapTableColumn;
//	}
}
