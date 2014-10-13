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
package com.hangum.tadpole.sql.util.resultset;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
	 * @param rs
	 * @param limitCount
	 * @return
	 * @throws SQLException
	 */
	public static TadpoleResultSet getResultToList(final ResultSet rs, final int limitCount) throws SQLException {
		return getResultToList(false, rs, limitCount);
	}
	
	/**
	 * ResultSet to List
	 * 
	 * 
	 * 
	 * @param isShowRowNum 첫번째 컬럼의 로우 넘버를 추가할 것인지.
	 * @param rs ResultSet
	 * @param limitCount 
	 * @return
	 * @throws SQLException
	 */
	public static TadpoleResultSet getResultToList(boolean isShowRowNum, final ResultSet rs, final int limitCount) throws SQLException {
		TadpoleResultSet returnRS = new TadpoleResultSet();
		Map<Integer, Object> tmpRow = null;
		
		// 결과를 프리퍼런스에서 처리한 맥스 결과 만큼만 거져옵니다.
		int rowCnt = 0;
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
				try {
//					Object obj = rs.getObject(intColIndex);
					tmpRow.put(intShowColIndex, rs.getObject(intColIndex));
//					int type = rs.getMetaData().getColumnType(intColIndex);
//
//					if (RDBTypeToJavaTypeUtils.isNumberType(type)){
////						if(isPretty) { 
////							tmpRow.put(intShowColIndex, obj == null?PublicTadpoleDefine.DEFINE_NULL_VALUE:addComma(type, obj));
////						}else{
//							tmpRow.put(intShowColIndex, obj == null?PublicTadpoleDefine.DEFINE_NULL_VALUE:obj);
////						}
//					}else if (RDBTypeToJavaTypeUtils.isCharType(type)){
//						tmpRow.put(intShowColIndex, obj == null?PublicTadpoleDefine.DEFINE_NULL_VALUE:obj);
//					} else if (type == java.sql.Types.ROWID) {
//						tmpRow.put(intShowColIndex, obj == null?PublicTadpoleDefine.DEFINE_NULL_VALUE:rs.getString(intColIndex));
//					}else {
//						tmpRow.put(intShowColIndex, obj == null?PublicTadpoleDefine.DEFINE_NULL_VALUE:obj);
////						logger.debug("\nColumn type is " + rs.getObject(intColIndex).getClass().toString());
//					}
				} catch(Exception e) {
					logger.error("ResutSet fetch error", e); //$NON-NLS-1$
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
	 * get column types
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
			mapColumnType.put(i+intStartIndex, rsm.getColumnType(i+1));
			
//			logger.debug("\t Precision 			:  " 	+ rsm.getPrecision(i+1));
//			logger.debug("\t Scale			 	:  " 	+ rsm.getScale(i+1));
//			logger.debug("\t SchemaName		 	:  " 	+ rsm.getSchemaName(i+1));
//			logger.debug("\t TableName		 	:  " 	+ rsm.getTableName(i+1));
//			logger.debug("\t ==[column end]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
		}
		
		return mapColumnType; 
	}
	
	/**
	 * 컬럼에 rownumber를 추가할 것인지.
	 * 
	 * @param isShowRowNum
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getColumnName(boolean isShowRowNum, ResultSet rs) throws Exception {
		Map<Integer, String> mapColumnName = new HashMap<Integer, String>();
		int intStartIndex = 0;
		
		if(isShowRowNum) {
			intStartIndex++;
			mapColumnName.put(0, "#");
		}
		
		ResultSetMetaData  rsm = rs.getMetaData();
		int columnCount = rsm.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			mapColumnName.put(i+intStartIndex, rsm.getColumnLabel(i+1));
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
}
