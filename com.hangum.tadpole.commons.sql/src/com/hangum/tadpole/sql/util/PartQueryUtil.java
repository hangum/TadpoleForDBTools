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
package com.hangum.tadpole.sql.util;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.template.CubridDMLTemplate;
import com.hangum.tadpole.sql.template.HIVEDMLTemplate;
import com.hangum.tadpole.sql.template.MSSQLDMLTemplate;
import com.hangum.tadpole.sql.template.MySQLDMLTemplate;
import com.hangum.tadpole.sql.template.OracleDMLTemplate;
import com.hangum.tadpole.sql.template.PostgreDMLTemplate;
import com.hangum.tadpole.sql.template.SQLiteDMLTemplate;

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
		
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB) ) {
			if(!StringUtils.contains(originalQuery.toLowerCase(), "limit ")) {
				requestQuery = String.format(MySQLDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
			} else {
				requestQuery = originalQuery;
			}
		
		} else if(DBDefine.ORACLE_DEFAULT == DBDefine.getDBDefine(userDB)) {
			
			if(!StringUtils.contains(originalQuery.toLowerCase(), "where")) {
				requestQuery = String.format(OracleDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
			} else {
				requestQuery = originalQuery;				
			}
		
		} else if(DBDefine.SQLite_DEFAULT == DBDefine.getDBDefine(userDB)) {
			if(!StringUtils.contains(originalQuery.toLowerCase(), "limit ")) {
				requestQuery = String.format(SQLiteDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
			} else {
				requestQuery = originalQuery;
			}
		
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB)) {
			
//			//https://github.com/hangum/TadpoleForDBTools/issues/12 와 같은 이유로 더 좋은 방법이 나타나기 전까지는 주석 처리 합니다.
			if(!StringUtils.contains(originalQuery.toLowerCase(), "limit ")) {
				requestQuery = String.format(CubridDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
			} else {
				requestQuery = originalQuery;	
			}
			
		} else if(DBDefine.POSTGRE_DEFAULT == DBDefine.getDBDefine(userDB)) {
			// 기존 쿼리에 limit 가 있으면 실행하지 않는다.
			if(!StringUtils.contains(originalQuery.toLowerCase(), "limit ")) {
				requestQuery = String.format(PostgreDMLTemplate.TMP_GET_PARTDATA, originalQuery, endResultPos, startResultPos);
			} else {
				requestQuery = originalQuery;	
			}
			
//		} else if(DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
//			if(!StringUtils.contains(originalQuery.toLowerCase(), "where")) {
//				requestQuery = String.format(MSSQLDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
//			} else {
//				requestQuery = originalQuery;				
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
		
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB)
				|| DBDefine.MARIADB_DEFAULT == DBDefine.getDBDefine(userDB)
		) {
			resultQuery = MySQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.ORACLE_DEFAULT == DBDefine.getDBDefine(userDB)) {
			resultQuery =  OracleDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBDefine.MSSQL_8_LE_DEFAULT == DBDefine.getDBDefine(userDB) || DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB)) {
	      resultQuery =  MSSQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			 
		} else if(DBDefine.SQLite_DEFAULT == DBDefine.getDBDefine(userDB)) {
			resultQuery = SQLiteDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB)) {
			resultQuery = query;
		} else if(DBDefine.HIVE_DEFAULT == DBDefine.getDBDefine(userDB)) {
			resultQuery = HIVEDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else {
			throw new Exception("Not Support DBMS Query Plan.");
		}
		
		return resultQuery;
	}
}
