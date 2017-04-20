/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.utils;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Table;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * Table to data utils
 * 
 * @author hangum
 *
 */
public class TableToDataUtils {
	private static final Logger logger = Logger.getLogger(TableToDataUtils.class);
	
	/**
	 * table multi row data
	 * 
	 * @param tableResult
	 * @param dataList
	 * @param mapColumnType
	 * @return
	 */
	public static TableColumnDAO getTableRowDatas(Table tableResult, TadpoleResultSet dataList, Map<Integer, Integer> mapColumnType) {
		TableColumnDAO columnDao = new TableColumnDAO();
		
		int[] intSelection = tableResult.getSelectionIndices();
		
		// 순서가 위에서 아래로 되도록 합니다.
		if(intSelection[0] > intSelection[intSelection.length-1]) {
			
			for(int i=0; i<tableResult.getSelectionCount(); i++) {
				Map<Integer, Object> mapColumns = dataList.getData().get(intSelection[intSelection.length-(i+1)]);
				TableColumnDAO _columnDao = TableToDataUtils.getTableRowData(tableResult, mapColumns, mapColumnType);
				if("".equals(columnDao.getCol_value())) {
					columnDao.setCol_value(_columnDao.getCol_value());
				} else {
					columnDao.setCol_value(columnDao.getCol_value() + PublicTadpoleDefine.LINE_SEPARATOR + _columnDao.getCol_value());
				}
			}
			
		} else {
			
			for(int i=0; i<tableResult.getSelectionCount(); i++) {
				Map<Integer, Object> mapColumns = dataList.getData().get(intSelection[i]);
				TableColumnDAO _columnDao = TableToDataUtils.getTableRowData(tableResult, mapColumns, mapColumnType);
				if("".equals(columnDao.getCol_value())) {
					columnDao.setCol_value(_columnDao.getCol_value());
				} else {
					columnDao.setCol_value(columnDao.getCol_value() + PublicTadpoleDefine.LINE_SEPARATOR + _columnDao.getCol_value());
				}
			}
		}
		
		return columnDao;
	}
	
	/**
	 * table row data
	 * 
	 * @param tableResult
	 * @param mapColumns
	 * @param mapColumnType
	 * @return
	 */
	public static TableColumnDAO getTableRowData(Table tableResult, Map<Integer, Object> mapColumns, Map<Integer, Integer> mapColumnType) {
		TableColumnDAO columnDao = new TableColumnDAO();
		String strNullValue = GetPreferenceGeneral.getResultNull();
		columnDao.setName(PublicTadpoleDefine.DEFINE_TABLE_COLUMN_BASE_ZERO);
		columnDao.setType(PublicTadpoleDefine.DEFINE_TABLE_COLUMN_BASE_ZERO_TYPE);
		
		for (int j=1; j<tableResult.getColumnCount(); j++) {
			Object columnObject = mapColumns.get(j);
			boolean isNumberType = RDBTypeToJavaTypeUtils.isNumberType(mapColumnType.get(j));
			if(isNumberType) {
				String strText = ""; //$NON-NLS-1$
				
				// if select value is null can 
				if(columnObject == null) strText = strNullValue;
				else strText = columnObject.toString();
				columnDao.setCol_value(columnDao.getCol_value() + strText + PublicTadpoleDefine.DELIMITER_DBL);
			} else if("BLOB".equalsIgnoreCase(columnDao.getData_type())) { //$NON-NLS-1$
				// ignore blob type
			} else {
				String strText = ""; //$NON-NLS-1$
				
				// if select value is null can 
				if(columnObject == null) {
					strText = strNullValue;
					columnDao.setCol_value(columnDao.getCol_value() + strText + PublicTadpoleDefine.DELIMITER_DBL);
				} else {
					strText = columnObject.toString();
					columnDao.setCol_value(columnDao.getCol_value() + SQLUtil.makeQuote(strText) + PublicTadpoleDefine.DELIMITER_DBL);
				}
				
			}
		}
		columnDao.setCol_value(StringUtils.removeEnd(""+columnDao.getCol_value(), PublicTadpoleDefine.DELIMITER_DBL));
		
		return columnDao;
	}
	
	/**
	 * 특정 컬럼의 데이터를 넘겨 준다.
	 * 
	 * @param columnObject
	 * @param intType
	 * @param name
	 * @return
	 */
	public static TableColumnDAO getTableData(Object columnObject, Integer intType, String name) {
		TableColumnDAO columnDao = new TableColumnDAO();
		
		if(intType == null) intType = java.sql.Types.VARCHAR;
		String strType = RDBTypeToJavaTypeUtils.getRDBType(intType);
		
		columnDao.setName(name);
		columnDao.setType(strType);
		
		if(columnObject != null) {
			// 해당컬럼 값이 널이 아니고 clob데이터 인지 확인한다.
			if (columnObject instanceof java.sql.Clob ){
				Clob cl = (Clob) columnObject;

				StringBuffer clobContent = new StringBuffer();
				String readBuffer = new String();

				// 버퍼를 이용하여 clob컬럼 자료를 읽어서 팝업 화면에 표시한다.
				BufferedReader bufferedReader;
				try {
					bufferedReader = new java.io.BufferedReader(cl.getCharacterStream());
					while ((readBuffer = bufferedReader.readLine())!= null) {
						clobContent.append(readBuffer);
					}

					columnDao.setCol_value(clobContent.toString());				
				} catch (Exception e) {
					logger.error("Clob column echeck", e); //$NON-NLS-1$
				}
			}else if (columnObject instanceof java.sql.Blob ){
				try {
					Blob blob = (Blob) columnObject;
					columnDao.setCol_value(blob.getBinaryStream());

				} catch (Exception e) {
					logger.error("Blob column echeck", e); //$NON-NLS-1$
				}

			}else if (columnObject instanceof byte[] ){
				byte[] b = (byte[])columnObject;
				StringBuffer str = new StringBuffer();
				try {
					for (byte buf : b){
						str.append(buf);
					}
					str.append("\n\nHex : " + new BigInteger(str.toString(), 2).toString(16)); //$NON-NLS-1$
					
					columnDao.setCol_value(str.toString());
				} catch (Exception e) {
					logger.error("Clob column echeck", e); //$NON-NLS-1$
				}
			}else{
				columnDao.setCol_value(columnObject.toString());
			}
		} else {
			columnDao.setCol_value(GetPreferenceGeneral.getResultNull());
		}
		
		return columnDao;
	}
}
