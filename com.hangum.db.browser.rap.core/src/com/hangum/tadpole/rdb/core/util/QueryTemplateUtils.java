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
package com.hangum.tadpole.rdb.core.util;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.CubridDMLTemplate;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.define.MySQLDMLTemplate;
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
	public static String getQuery(UserDBDAO userDB, Define.DB_ACTION initAction) {
		String defaultStr = "";
		if(DBDefine.MYSQL_DEFAULT == DBDefine.getDBDefine(userDB.getTypes()) ||
				DBDefine.ORACLE_DEFAULT == DBDefine.getDBDefine(userDB.getTypes()) ||
				DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB.getTypes()) 				
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
			
		} else if(DBDefine.SQLite_DEFAULT ==  DBDefine.getDBDefine(userDB.getTypes())) {
			
			if(initAction == Define.DB_ACTION.TABLES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == Define.DB_ACTION.VIEWS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == Define.DB_ACTION.INDEXES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == Define.DB_ACTION.TRIGGERS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.CUBRID_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
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
		} else if(DBDefine.POSTGRE_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
			if(initAction == Define.DB_ACTION.TABLES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == Define.DB_ACTION.VIEWS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == Define.DB_ACTION.INDEXES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == Define.DB_ACTION.PROCEDURES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == Define.DB_ACTION.FUNCTIONS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == Define.DB_ACTION.TRIGGERS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_TRIGGER_STMT;
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
