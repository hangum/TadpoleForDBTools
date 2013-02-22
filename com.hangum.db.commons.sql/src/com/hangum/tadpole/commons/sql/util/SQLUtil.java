/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <pre>
 *  java.sql.ResultSet과 ResultSetMeta를 TableViewer로 바꾸기 위해 가공하는 Util
 *  
 *  resource데이터를 저장하기 위해 data를 배열화시킨다.
 * </pre>
 * 
 * @author hangum
 *
 */
public class SQLUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLUtil.class);
	
	/**
	 * 쿼리 실행시 prestatment로 실행 할 것인지, 아니면 execute나 executebatch로 실행할 것인지 검사합니다.
	 * 
	 * @param strSQL
	 * @return
	 */
	public static boolean isStatment(String strSQL) {
		if(strSQL.toUpperCase().startsWith("SHOW") ||  //$NON-NLS-1$
				strSQL.toUpperCase().startsWith("SELECT") ||  //$NON-NLS-1$
				strSQL.toUpperCase().startsWith("DESC") ||  //$NON-NLS-1$
				strSQL.toUpperCase().startsWith("DESCRIBE") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * metadata를 바탕으로 결과를 컬럼 정보를 수집힌다.
	 * 
	 * @param rs
	 * @return index순번에 컬럼명
	 */
	public static HashMap<Integer, String> mataDataToMap(ResultSet rs) throws Exception {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		ResultSetMetaData  rsm = rs.getMetaData();
		int columnCount = rsm.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			map.put(i, rsm.getColumnLabel(i+1));
		}
		
		return map;
	}
	
	/**
	 * INSERT 문을 생성합니다.
	 * 
	 * @param tableName
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static String makeInsertStatment(String tableName, ResultSet rs) throws Exception {
		StringBuffer result = new StringBuffer("INSERT INTO " + tableName + "(");
		
		HashMap<Integer, String> mapTable = mataDataToMap(rs);
		for( int i=0 ;i<mapTable.size(); i++ ) {
			if( i != (mapTable.size()-1) ) result.append( mapTable.get(i) + ",");
			else result.append( mapTable.get(i));
		}
		
		result.append(") VALUES(");
		
		for( int i=0 ;i<mapTable.size(); i++ ) {
			if( i != (mapTable.size()-1) ) result.append("?,");
			else result.append('?');
		}
		
		result.append(')');			
		
		if(logger.isDebugEnabled()) logger.debug("[make insert statment is " + result.toString());
		
		return result.toString();
	}
	
	/**
	 * 쿼리 텍스트에 쿼리 이외의 특수 문자를 제거해 줍니다.
	 * 
	 * @param exeSQL
	 * @return
	 */
	public static String executeQuery(String exeSQL) {
		try {
//			
//			https://github.com/hangum/TadpoleForDBTools/issues/140 오류로 불럭지정하였습니다.
//			TO DO 특정 쿼리에서는 주석이 있으면 오류인데..DB에서 쿼리를 실행받는 다양한 조건을 고려할 필요가 있습니다. 
			
//			// 문장의 -- 뒤에를 주석으로 인식 쿼리열에서 제외합니다.
//			exeSQL = delComment(exeSQL, "--");
			
			// 문장 의 // 뒤에를 주석으로 인식 쿼리열에서 제외합니다.
			exeSQL = delComment(exeSQL, "//");
			
			// 마지막 쿼리를 재 사용하기위해
//			exeSQL = StringUtils.replace(exeSQL, "\r", " ");
//			exeSQL = StringUtils.replace(exeSQL, "\n", " ");
//			exeSQL = StringUtils.replace(exeSQL, Define.LINE_SEPARATOR, " ");
//			exeSQL = exeSQL.replaceAll("(\r\n|\n|\r)", " ");
			
			// 모든 쿼리에 공백 주석 제거
			exeSQL = StringUtils.trimToEmpty(exeSQL);
			
		} catch(Exception e) {
			logger.error("query execute", e);
		}
		
		return exeSQL;
	}
	
	/**
	 * 쿼리중에 주석을 제거합니다.
	 * 
	 * @param sql
	 * @param comment
	 * @return
	 */
	private static String delComment(String sql, String comment) {
		try {
			String[] linesSQL = sql.split("\n");
			if(linesSQL.length > 0) {
				StringBuffer tmpSQL = new StringBuffer();
				for (String string : linesSQL) {
					int idx = string.indexOf(comment);//"--");
					if( idx == 0) {
					} else if( idx > 0) {
						tmpSQL.append(string.substring(0, idx-1)).append("\n");
					} else {
						tmpSQL.append(string).append("\n");
					}
				}
				
				return tmpSQL.toString();
			}
			
		} catch(Exception e) {
			logger.error("execute sql", e);
		}
	
		return sql;
	}
	
	/**
	 * db resource data를 저장할때 2000byte 단위로 저장하도록 합니다.
	 * 
	 * @param resource data
	 * @return
	 */
	public static String[] makeResourceDataArays(String resourceContent)  {
		int cutsize = 1998;
		String[] tmpRetArryStr = new String[2000];
		byte[] byteSqlText = resourceContent.getBytes();
		
		int isEndTextHangul = 0;
		int workCnt = 0;

		while (byteSqlText.length > cutsize) {
			isEndTextHangul = 0;
			for (int i=0; i<cutsize; i++) {
				if (byteSqlText[i] < 0) isEndTextHangul++;
			}

			if (isEndTextHangul%2 != 0) {
				tmpRetArryStr[workCnt] = new String(byteSqlText, 0, cutsize + 1);
				byteSqlText = new String(byteSqlText, cutsize + 1, byteSqlText.length - (cutsize + 1)).getBytes();
			} else {
				tmpRetArryStr[workCnt] = new String(byteSqlText, 0, cutsize);
				byteSqlText = new String(byteSqlText, cutsize, byteSqlText.length - cutsize).getBytes();
			}

			workCnt++;
		}
		tmpRetArryStr[workCnt] = new String(byteSqlText);
		
		// 결과가 있는 만큼 담기위해
		String[] returnDataArry = new String[workCnt+1];
		for (int i=0; i<=workCnt; i++) {
			returnDataArry[i] = tmpRetArryStr[i];
		}
		
		return returnDataArry;
	}
}
