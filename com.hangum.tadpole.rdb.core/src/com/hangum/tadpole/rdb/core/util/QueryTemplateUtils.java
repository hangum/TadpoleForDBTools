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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
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
	public static String getQuery(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE initAction) {
		String defaultStr = "";
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || 
				DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()
		) {
			
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_CONSTRAINTS_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  MySQLDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.ORACLE_DEFAULT == userDB.getDBDefine() ||
				DBDefine.TIBERO_DEFAULT ==  userDB.getDBDefine()
		) {
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_CONSTRAINTS_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PACKAGES) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_PACKAGE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  OracleDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.SQLite_DEFAULT ==  userDB.getDBDefine()) {
			
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  SQLiteDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
			
		} else if(DBDefine.CUBRID_DEFAULT == userDB.getDBDefine()) {
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  CubridDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  PostgreDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.HIVE_DEFAULT == userDB.getDBDefine() || 
				DBDefine.HIVE2_DEFAULT == userDB.getDBDefine()
				) {
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  HIVEDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  HIVEDMLTemplate.TMP_CREATE_VIEW_STMT;
			}
		} else if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			defaultStr =  TAJODMLTemplate.TMP_CREATE_TABLE_STMT;
		} else if(DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
		
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_CONSTRAINTS_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  MSSQLDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		} else if(DBDefine.ALTIBASE_DEFAULT == userDB.getDBDefine()) {
			if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
				defaultStr =  AltibaseDMLTemplate.TMP_CREATE_TABLE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				defaultStr =  AltibaseDMLTemplate.TMP_CREATE_VIEW_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
				defaultStr =  AltibaseDMLTemplate.TMP_CREATE_INDEX_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
				defaultStr =  AltibaseDMLTemplate.TMP_CREATE_PROCEDURE_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
				defaultStr =  AltibaseDMLTemplate.TMP_CREATE_FUNCTION_STMT;
			} else if(initAction == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
				defaultStr =  AltibaseDMLTemplate.TMP_CREATE_TRIGGER_STMT;
			}
		}
		
		return defaultStr;
	}
}
