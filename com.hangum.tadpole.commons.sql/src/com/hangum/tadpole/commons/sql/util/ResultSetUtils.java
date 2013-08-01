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
package com.hangum.tadpole.commons.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.sql.utils.RDBTypeToJavaTypeUtils;

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
	 * @param rs ResultSet
	 * @param limitCount 
	 * @param isPretty 
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<Integer, Object>> getResultToList(final ResultSet rs, final int limitCount, final boolean isPretty) throws SQLException {
		List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer, Object>>();
		Map<Integer, Object> tmpRow = null;
		
		// 결과를 프리퍼런스에서 처리한 맥스 결과 만큼만 거져옵니다.
		while(rs.next()) {
			tmpRow = new HashMap<Integer, Object>();
			
			for(int i=0; i<rs.getMetaData().getColumnCount(); i++) {
				final int intColIndex = i+1;
				try {
					String colValue = rs.getString(intColIndex) == null ?"":rs.getString(intColIndex); //$NON-NLS-1$
					if(isPretty) colValue = prettyData(rs.getMetaData().getColumnType(intColIndex), rs.getObject(intColIndex));
					
					tmpRow.put(i, colValue);
				} catch(Exception e) {
					logger.error("ResutSet fetch error", e); //$NON-NLS-1$
					tmpRow.put(i, ""); //$NON-NLS-1$
				}
			}
			
			sourceDataList.add(tmpRow);
			
			// 쿼리 검색 결과 만큼만 결과셋을 받습니다. 
			if(limitCount == rs.getRow()) break;
		}
		
		return sourceDataList;
	}
	
	/**
	 * 숫자일 경우 ,를 찍어보여줍니다.
	 * 
	 * @param columnType java.sql.Types
	 * @param value
	 * @return
	 */
	public static String prettyData(int columnType, Object value) {
		if(RDBTypeToJavaTypeUtils.isNumberType(columnType)) {
			try{
				NumberFormat pf = NumberFormat.getNumberInstance();
				String val = pf.format(value);
				
				return val;
			} catch(Exception e){
//				logger.error("pretty data", e); //$NON-NLS-1$
//				igonr exception
			}			
		} 

		return value==null?"":value.toString(); //$NON-NLS-1$
	}
	
	/**
	 * get column types
	 * 
	 * @param rsm
	 * @return
	 * @throws SQLException
	 */
	public static Map<Integer, Integer> getColumnType(ResultSetMetaData rsm) throws SQLException {
		Map<Integer, Integer> mapColumnType = new HashMap<Integer, Integer>();
		
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
			mapColumnType.put(i, rsm.getColumnType(i+1));
			
//			logger.debug("\t Precision 			:  " 	+ rsm.getPrecision(i+1));
//			logger.debug("\t Scale			 	:  " 	+ rsm.getScale(i+1));
//			logger.debug("\t SchemaName		 	:  " 	+ rsm.getSchemaName(i+1));
//			logger.debug("\t TableName		 	:  " 	+ rsm.getTableName(i+1));
//			logger.debug("\t ==[column end]================================ ColumnName  :  " 	+ rsm.getColumnName(i+1));
		}
		
		return mapColumnType; 
	}

	/**
	 * metadata를 바탕으로 결과를 컬럼 정보를 수집힌다.
	 * 
	 * @param rs
	 * @return index순번에 컬럼명
	 */
	public static Map<Integer, String> getColumnName(ResultSet rs) throws Exception {
		Map<Integer, String> mapColumnName = new HashMap<Integer, String>();
		
		ResultSetMetaData  rsm = rs.getMetaData();
		int columnCount = rsm.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			mapColumnName.put(i, rsm.getColumnLabel(i+1));
		}
		
		return mapColumnName;
	}

	
}
