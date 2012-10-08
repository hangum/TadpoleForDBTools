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
package com.hangum.tadpole.rdb.core.editors.main;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.MySQLDMLTemplate;
import com.hangum.tadpole.define.OracleDMLTemplate;
import com.hangum.tadpole.define.SQLiteDMLTemplate;

/**
 * 각 DBMS에 맞는 쿼리문을 생성합니다.
 * 
 * @author hangum
 *
 */
public class PartQueryUtil {
	
	/**
	 *  각 DBMS에 맞는 SELECT 문을 만들어줍니다.
	 *  
	 * @return
	 */
	public static String makeSelect(UserDBDAO userDB, String originalQuery, int startResultPos, int endResultPos) {
		String requestQuery = "";
		
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB.getTypes()) ) {
			requestQuery = String.format(MySQLDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
		
		} else if(DBDefine.ORACLE_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
			
			if( StringUtils.indexOf(originalQuery.toLowerCase(), "where") == -1 ) {
				requestQuery = String.format(OracleDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
			} else {
				requestQuery = originalQuery;				
			}
		
		} else if(DBDefine.SQLite_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
			requestQuery = String.format(SQLiteDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
		
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
			
//			
//			https://github.com/hangum/TadpoleForDBTools/issues/12 와 같은 이유로 더 좋은 방법이 나타나기 전까지는 주석 처리 합니다.
//			
//			if( StringUtils.indexOf(originalQuery, "where") == -1 ) {
//				requestQuery = String.format(CubridDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
//			} else {
				requestQuery = originalQuery;				
//			}
			
		// 정의 되지 않는 dbms는 전체로 동작하게 합니다.
		} else {
			requestQuery = originalQuery;
		}
		
		return requestQuery;
	}
	
	/**
	 * 각 dbms에 맞는 explain query를 만들어 줍니다.
	 * 
	 * @param userDB
	 * @param query
	 * @return
	 */
	public static String makeExplainQuery(UserDBDAO userDB, String query) throws Exception {
		String resultQuery = "";
		
		if(DBDefine.MYSQL_DEFAULT ==   DBDefine.getDBDefine(userDB.getTypes())) {
			
			resultQuery = MySQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.ORACLE_DEFAULT ==   DBDefine.getDBDefine(userDB.getTypes())) {

			resultQuery =  OracleDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.SQLite_DEFAULT ==  DBDefine.getDBDefine(userDB.getTypes())) {
			
			resultQuery = SQLiteDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.CUBRID_DEFAULT ==  DBDefine.getDBDefine(userDB.getTypes())) {
			
			resultQuery = query;
			
		} else {
			throw new Exception("Not Support DBMS Query Plan.");
		}
		
		return resultQuery;
	}
}
