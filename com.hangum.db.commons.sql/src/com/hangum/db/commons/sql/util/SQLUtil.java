package com.hangum.db.commons.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *  java.sql.ResultSet과 ResultSetMeta를 TableViewer로 바꾸기 위해 가공하는 Util
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
		
		logger.info("[make insert statment is " + result.toString());
		
		return result.toString();
	}
	
	/**
	 * tadpole에서 실행할 쿼리로 구문을 만듭니다.
	 * 
	 * @param exeSQL
	 * @return
	 */
	public static String executeQuery(String exeSQL) {
		try {
			// 문장의 -- 뒤에를 주석으로 인식 쿼리열에서 제외합니다.
			exeSQL = delComment(exeSQL, "--");
			// 문장 의 // 뒤에를 주석으로 인식 쿼리열에서 제외합니다.
			exeSQL = delComment(exeSQL, "//");
			
			// 마지막 쿼리를 재 사용하기위해
//			exeSQL = StringUtils.replace(exeSQL, "\r", " ");
//			exeSQL = StringUtils.replace(exeSQL, "\n", " ");
//			exeSQL = StringUtils.replace(exeSQL, Define.LINE_SEPARATOR, " ");
			exeSQL = exeSQL.replaceAll("(\r\n|\n|\r)", " ");
			
			// 모든 쿼리에 공백 주석 제거
			exeSQL = StringUtils.trimToEmpty(exeSQL);
			
		} catch(Exception e) {
			logger.error("쿼리 가공중에", e);
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
}
