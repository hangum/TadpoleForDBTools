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
package com.hangum.tadpole.engine.sql.util;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.template.AltibaseDMLTemplate;
import com.hangum.tadpole.engine.sql.template.CubridDMLTemplate;
import com.hangum.tadpole.engine.sql.template.HIVEDMLTemplate;
import com.hangum.tadpole.engine.sql.template.MSSQLDMLTemplate;
import com.hangum.tadpole.engine.sql.template.MySQLDMLTemplate;
import com.hangum.tadpole.engine.sql.template.OracleDMLTemplate;
import com.hangum.tadpole.engine.sql.template.PostgreDMLTemplate;
import com.hangum.tadpole.engine.sql.template.SQLiteDMLTemplate;
import com.hangum.tadpole.engine.sql.template.TAJODMLTemplate;

/**
 * 각 DBMS에 맞는 쿼리문을 생성합니다.
 * 
 * @author hangum
 *
 */
public class PartQueryUtil {
	private static final Logger logger = Logger.getLogger(PartQueryUtil.class);
	
	/**
	 *  각 DBMS에 맞는 SELECT 문을 만들어줍니다.
	 *  
	 * @return
	 */
	public static String makeSelect(UserDBDAO userDB, String strQuery, int intStartPos, int intRowCnt) throws Exception {
		String requestQuery = "";
		
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || DBDefine.MARIADB_DEFAULT == userDB.getDBDefine() ) {
			requestQuery = String.format(MySQLDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBDefine.ORACLE_DEFAULT == userDB.getDBDefine() || DBDefine.TIBERO_DEFAULT == userDB.getDBDefine()) {
			requestQuery = String.format(OracleDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intStartPos+intRowCnt);
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			requestQuery = String.format(SQLiteDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBDefine.CUBRID_DEFAULT == userDB.getDBDefine()) {
			requestQuery = String.format(CubridDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			requestQuery = String.format(PostgreDMLTemplate.TMP_GET_PARTDATA, strQuery,  intStartPos, intRowCnt);
		} else if(DBDefine.ALTIBASE_DEFAULT == userDB.getDBDefine()) {
			requestQuery = String.format(AltibaseDMLTemplate.TMP_GET_PARTDATA, strQuery,  intStartPos, intRowCnt);
//		} else if(DBDefine.MSSQL_DEFAULT == userDB.getDBDefine() | DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine()) {
//			requestQuery = String.format(MSSQLDMLTemplate.TMP_GET_PARTDATA, strQuery, intRowCnt, intStartPos+intRowCnt);
//			
//		// 정의 되지 않는 dbms는 전체로 동작하게 합니다.
//		} else {
//			requestQuery = originalQuery;
		} else {
			throw new Exception("Not support Database.");
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
		
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine()
				|| DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()
		) {
			resultQuery = MySQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.ORACLE_DEFAULT == userDB.getDBDefine() || DBDefine.TIBERO_DEFAULT == userDB.getDBDefine()) {
			resultQuery =  OracleDMLTemplate.TMP_EXPLAIN_EXTENDED + "( " + query + ")";
		} else if(DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine() || DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
	      resultQuery =  MSSQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			resultQuery = SQLiteDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBDefine.CUBRID_DEFAULT == userDB.getDBDefine()) {
			resultQuery = query;
		} else if(DBDefine.HIVE_DEFAULT == userDB.getDBDefine()) {
			resultQuery = HIVEDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			resultQuery = TAJODMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			resultQuery = PostgreDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else {
			throw new Exception("Not Support DBMS Query Plan.");
		}

		if(logger.isDebugEnabled()) logger.debug("[plan query]" + resultQuery);
		
		return resultQuery;
	}
}
