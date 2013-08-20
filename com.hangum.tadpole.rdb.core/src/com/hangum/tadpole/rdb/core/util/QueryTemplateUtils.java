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
package com.hangum.tadpole.rdb.core.util;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.CubridDMLTemplate;
import com.hangum.tadpole.define.MySQLDMLTemplate;
import com.hangum.tadpole.define.OracleDMLTemplate;
import com.hangum.tadpole.define.PostgreDMLTemplate;
import com.hangum.tadpole.define.SQLiteDMLTemplate;

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
	public static String getQuery(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION initAction) {
		String defaultStr = "";
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types()) || 
				DBDefine.MARIADB_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types()) ||
				DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types()) 				
		) {
			
			if(initAction == PublicTadpoleDefine.DB_ACTION.TABLES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.INDEXES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.ORACLE_DEFAULT ==  DBDefine.getDBDefine(userDB.getDbms_types())) {
			if(initAction == PublicTadpoleDefine.DB_ACTION.TABLES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.INDEXES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.SQLite_DEFAULT ==  DBDefine.getDBDefine(userDB.getDbms_types())) {
			
			if(initAction == PublicTadpoleDefine.DB_ACTION.TABLES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.INDEXES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types())) {
			if(initAction == PublicTadpoleDefine.DB_ACTION.TABLES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.INDEXES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.POSTGRE_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types())) {
			if(initAction == PublicTadpoleDefine.DB_ACTION.TABLES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.INDEXES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.HIVE_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types())) {
			
		}
		
//		else if(DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB.getDbms_types())) {
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
