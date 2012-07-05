package com.hangum.db.browser.rap.core.util;

import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.CubridDMLTemplate;
import com.hangum.db.define.Define;
import com.hangum.db.define.MySQLDMLTemplate;
import com.hangum.db.define.SQLiteDMLTemplate;

/**
 * db에 다른 템플릿 쿼리를 생성합니다.
 * 
 * 
 * @author hangum
 *
 */
public class QueryTemplateUtils {
	/**
	 * 쿼리
	 * 
	 * @param userDB
	 * @param initAction
	 * @return
	 */
	public static String getQuery(UserDBDAO userDB, Define.DB_ACTION initAction) {
		String defaultStr = "";
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB.getType()) ||
				DBDefine.ORACLE_DEFAULT == DBDefine.getDBDefine(userDB.getType()) ||
				DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB.getType()) 
			) {
			
			if(initAction == Define.DB_ACTION.TABLES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == Define.DB_ACTION.VIEWS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == Define.DB_ACTION.INDEXES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == Define.DB_ACTION.PROCEDURES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == Define.DB_ACTION.FUNCTIONS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == Define.DB_ACTION.TRIGGERS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.SQLite_DEFAULT ==  DBDefine.getDBDefine(userDB.getType())) {
			
			if(initAction == Define.DB_ACTION.TABLES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == Define.DB_ACTION.VIEWS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == Define.DB_ACTION.INDEXES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == Define.DB_ACTION.TRIGGERS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB.getType())) {
			if(initAction == Define.DB_ACTION.TABLES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == Define.DB_ACTION.VIEWS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == Define.DB_ACTION.INDEXES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == Define.DB_ACTION.PROCEDURES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == Define.DB_ACTION.FUNCTIONS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == Define.DB_ACTION.TRIGGERS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		}
		
		
//		else if(DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB.getType())) {
//		
//			if(initAction == Define.DB_ACTION.TABLES) {
//				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_TABLE_STMT;
//			} else if(initAction == Define.DB_ACTION.VIEWS) {
//				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_VIEW_STMT;
//			} else if(initAction == Define.DB_ACTION.INDEXES) {
//				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_INDEX_STMT;
//			} else if(initAction == Define.DB_ACTION.PROCEDURES) {
//				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
//			} else if(initAction == Define.DB_ACTION.FUNCTIONS) {
//				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_FUNCTION_STMT;
//			} else if(initAction == Define.DB_ACTION.TRIGGERS) {
//				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_TRIGGER_STMT;
//			}
//		}
		
		return defaultStr;
	}
}
