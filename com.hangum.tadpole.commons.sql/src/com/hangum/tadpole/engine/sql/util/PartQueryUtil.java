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
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.template.AltibaseDMLTemplate;
import com.hangum.tadpole.engine.sql.template.CubridDMLTemplate;
import com.hangum.tadpole.engine.sql.template.HIVEDMLTemplate;
import com.hangum.tadpole.engine.sql.template.MSSQLDMLTemplate;
import com.hangum.tadpole.engine.sql.template.MySQLDMLTemplate;
import com.hangum.tadpole.engine.sql.template.OracleDMLTemplate;
import com.hangum.tadpole.engine.sql.template.PostgreDMLTemplate;
import com.hangum.tadpole.engine.sql.template.RedShiftDMLTemplate;
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
	 *  @param userDB 사용디비
	 *  @param strQuery 사용 쿼리
	 *  @param intStartPosition 시작 포인트
	 *  @param intRowCnt 몇 건 데이터
	 *  
	 * @return
	 */
	public static String makeSelect(UserDBDAO userDB, String strQuery, int intStartPos, int intRowCnt) throws Exception {
		String requestQuery = "";
		
//		if(logger.isDebugEnabled()) logger.debug("make select : " + intStartPos + ", " + intRowCnt);
		
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(MySQLDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(OracleDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intStartPos+intRowCnt);
		} else if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(SQLiteDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBGroupDefine.CUBRID_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(CubridDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(PostgreDMLTemplate.TMP_GET_PARTDATA, strQuery,  intStartPos, intRowCnt);
		} else if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
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
		
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			resultQuery = MySQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			resultQuery =  OracleDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.MSSQL_GROUP == userDB.getDBGroup()) {
	      resultQuery =  MSSQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
			resultQuery = SQLiteDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.CUBRID_GROUP == userDB.getDBGroup()) {
			resultQuery = query;
		} else if(DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) {
			resultQuery = HIVEDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup()) {
			resultQuery = TAJODMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			if(userDB.getDBDefine() == DBDefine.AMAZON_REDSHIFT_DEFAULT) {
				resultQuery = RedShiftDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			} else {
				resultQuery = PostgreDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			}
			
		} else {
			throw new Exception("Not Support DBMS Query Plan.");
		}

		if(logger.isDebugEnabled()) logger.debug("[plan query]" + resultQuery);
		
		return resultQuery;
	}
}
