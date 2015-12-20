/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Get db system schema
 * 
 * @author hangum
 *
 */
public class DBSystemSchema {
	
	/**
	 * get table row count
	 * 
	 * @param userDB
	 * @param strTableName
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static int getTableRowCount(final UserDBDAO userDB, final String strTableName)  throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		Integer listView = (Integer)sqlClient.queryForObject("tableRowCount", strTableName);
		
		return listView;
	}
	
	/**
	 * get view List
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<TableDAO> getViewList(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ) return new ArrayList<TableDAO>();
		
		List<TableDAO> listTblView = new ArrayList<TableDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<String> listView = sqlClient.queryForList("viewList", userDB.getDb());
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(String strView : listView) {
			TableDAO tblDao = new TableDAO();
			tblDao.setName(strView);
			tblDao.setSysName(SQLUtil.makeIdentifierName(userDB, strView));
			
			listTblView.add(tblDao);
		}
		
		return listTblView; 
	}
	
	/**
	 * getViewColumn
	 * 
	 * @param userDB
	 * @param tableDao
	 * @return 
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<TableColumnDAO> getViewColumnList(final UserDBDAO userDB, final TableDAO tableDao) throws TadpoleSQLManagerException, SQLException {
		List<TableColumnDAO> showViewColumns = new ArrayList<TableColumnDAO>();
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("db", userDB.getDb()); //$NON-NLS-1$
		param.put("table", tableDao.getName()); //$NON-NLS-1$

		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		showViewColumns = sqlClient.queryForList("tableColumnList", param); //$NON-NLS-1$
		
		// if find the keyword is add system quote.
		for(TableColumnDAO td : showViewColumns) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getField()));
		}
		
		return showViewColumns;
	}

	/**
	 * get function list
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<ProcedureFunctionDAO> getFunctionList(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ||
				userDB.getDBDefine() == DBDefine.SQLite_DEFAULT 
		) return new ArrayList<ProcedureFunctionDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<ProcedureFunctionDAO> listFunction = sqlClient.queryForList("functionList", userDB.getDb()); //$NON-NLS-1$
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(ProcedureFunctionDAO pfDao : listFunction) {
			pfDao.setSysName(SQLUtil.makeIdentifierName(userDB, pfDao.getName()));
		}
		
		return listFunction;
	}
	
	/**
	 * return procedure list
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<ProcedureFunctionDAO> getProcedure(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ||
				userDB.getDBDefine() == DBDefine.SQLite_DEFAULT 
		) return new ArrayList<ProcedureFunctionDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<ProcedureFunctionDAO> listProcedure = sqlClient.queryForList("procedureList", userDB.getDb()); //$NON-NLS-1$
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(ProcedureFunctionDAO pfDao : listProcedure) {
			pfDao.setSysName(SQLUtil.makeIdentifierName(userDB, pfDao.getName()));
		}
		
		return listProcedure;
	}
	
	/**
	 * Return trigger information
	 * 
	 * @param strObjectName 
	 */
	public static List<TriggerDAO> getTrigger(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ||
				userDB.getDBDefine() == DBDefine.SQLite_DEFAULT 
		) return new ArrayList<TriggerDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("triggerList", userDB.getDb()); //$NON-NLS-1$
	}
}
