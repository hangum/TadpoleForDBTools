package com.hangum.db.browser.rap.core.editors.main;

import org.apache.commons.lang.StringUtils;

import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.CubridDMLTemplate;
import com.hangum.db.define.MySQLDMLTemplate;
import com.hangum.db.define.OracleDMLTemplate;
import com.hangum.db.define.SQLiteDMLTemplate;

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
		
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB.getType()) ) {
			requestQuery = String.format(MySQLDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
		
		} else if(DBDefine.ORACLE_DEFAULT == DBDefine.getDBDefine(userDB.getType())) {
			
			if( StringUtils.indexOf(originalQuery, "where") == -1 ) {
				requestQuery = String.format(OracleDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
			} else {
				requestQuery = originalQuery;				
			}
		
		} else if(DBDefine.SQLite_DEFAULT == DBDefine.getDBDefine(userDB.getType())) {
			requestQuery = String.format(SQLiteDMLTemplate.TMP_GET_PARTDATA, originalQuery, startResultPos, endResultPos);
		
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB.getType())) {
			
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
		
		if(DBDefine.MYSQL_DEFAULT ==   DBDefine.getDBDefine(userDB.getType())) {
			resultQuery = MySQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBDefine.ORACLE_DEFAULT ==   DBDefine.getDBDefine(userDB.getType())) {
			throw new Exception("Not Support DBMS Query Plan.");
		} else if(DBDefine.SQLite_DEFAULT ==  DBDefine.getDBDefine(userDB.getType())) {
			resultQuery = SQLiteDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else {
			throw new Exception("Not Support DBMS Query Plan.");
		}
		
		return resultQuery;
	}
}
