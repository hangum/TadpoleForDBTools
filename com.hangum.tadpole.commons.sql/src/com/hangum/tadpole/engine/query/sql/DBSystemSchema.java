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

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.db.metadata.MakeContentAssistUtil;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
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
	 * return namespace
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List getSchemas(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("getSchemas");
	}
	
	/**
	 * get table row count
	 * 
	 * @param userDB
	 * @param strTableName
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static int getTableRowCount(final UserDBDAO userDB, final String strTableName) throws TadpoleSQLManagerException, SQLException {
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
		if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup() || DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) return new ArrayList<TableDAO>();
		
		List<TableDAO> listTblView = new ArrayList<TableDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			listTblView = sqlClient.queryForList("viewList", userDB.getSchema());
			// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
			StringBuffer strViewList = new StringBuffer();
			for(TableDAO td : listTblView) {
				td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getName()));
				strViewList.append(MakeContentAssistUtil.makeObjectPattern(td.getSchema_name(), td.getSysName(), "View")); //$NON-NLS-1$
			}
			userDB.setViewListSeparator( StringUtils.removeEnd(strViewList.toString(), MakeContentAssistUtil._PRE_GROUP)); //$NON-NLS-1$
			
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			List<HashMap<String,String>> listView = sqlClient.queryForList("viewList", userDB.getSchema());
			// 1. 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
			// 2. keyword 를 만든다.
			StringBuffer strViewList = new StringBuffer();
			for(HashMap<String,String> map : listView) {
				TableDAO tblDao = new TableDAO();
				tblDao.setName(map.get("NAME"));
				tblDao.setSchema_name(map.get("SCHEMA_NAME"));
				tblDao.setSysName(SQLUtil.makeIdentifierName(userDB, map.get("NAME")));
				tblDao.setComment(map.get("COMMENTS"));
				
				listTblView.add(tblDao);
	
				strViewList.append(MakeContentAssistUtil.makeObjectPattern(tblDao.getSchema_name(), tblDao.getSysName(), "View")); //$NON-NLS-1$
			}
			userDB.setViewListSeparator( StringUtils.removeEnd(strViewList.toString(), MakeContentAssistUtil._PRE_GROUP)); //$NON-NLS-1$
			
		} else if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			List<HashMap<String,String>> listView = sqlClient.queryForList("viewList", userDB.getSchema());
			// 1. 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
			// 2. keyword 를 만든다.
			StringBuffer strViewList = new StringBuffer();
			for(HashMap<String,String> map : listView) {
				TableDAO tblDao = new TableDAO();
				tblDao.setName(map.get("VIEW_NAME"));
				tblDao.setSchema_name(map.get("SCHEMA_NAME"));
				tblDao.setSysName(SQLUtil.makeIdentifierName(userDB, map.get("VIEW_NAME")));
				
				listTblView.add(tblDao);
	
				strViewList.append(MakeContentAssistUtil.makeObjectPattern(tblDao.getSchema_name(), tblDao.getSysName(), "View")); //$NON-NLS-1$
			}
			userDB.setViewListSeparator( StringUtils.removeEnd(strViewList.toString(), MakeContentAssistUtil._PRE_GROUP)); //$NON-NLS-1$
			
		} else {
			List<String> listView = sqlClient.queryForList("viewList", userDB.getDb());
			// 1. 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
			// 2. keyword 를 만든다.
			StringBuffer strViewList = new StringBuffer();
			for(String strView : listView) {
				TableDAO tblDao = new TableDAO();
				tblDao.setName(strView);
				tblDao.setSysName(SQLUtil.makeIdentifierName(userDB, strView));
				
				listTblView.add(tblDao);
	
				strViewList.append(MakeContentAssistUtil.makeObjectPattern(tblDao.getSchema_name(), tblDao.getSysName(), "View")); //$NON-NLS-1$
			}
			userDB.setViewListSeparator( StringUtils.removeEnd(strViewList.toString(), MakeContentAssistUtil._PRE_GROUP)); //$NON-NLS-1$
		}
		
		/** filter 정보가 있으면 처리합니다. */
		listTblView = DBAccessCtlManager.getInstance().getTableFilter(listTblView, userDB);
		
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
		if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			param.put("user", StringUtils.substringBefore(tableDao.getName(), "."));
			param.put("table", StringUtils.substringAfter(tableDao.getName(), "."));
		} else {
			param.put("db", userDB.getDb()); //$NON-NLS-1$
			param.put("schema", tableDao.getSchema_name()); //$NON-NLS-1$
			param.put("table", tableDao.getName()); //$NON-NLS-1$
		}

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
		if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup() || DBGroupDefine.HIVE_GROUP == userDB.getDBGroup() || DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup())  {
			return new ArrayList<ProcedureFunctionDAO>();
		}
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<ProcedureFunctionDAO> listFunction;
		
		if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup() || DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()){
			listFunction = sqlClient.queryForList("functionList", userDB.getSchema()); //$NON-NLS-1$
		}else if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()){
			listFunction = sqlClient.queryForList("functionList", userDB.getSchema()); //$NON-NLS-1$
		}else{
			listFunction = sqlClient.queryForList("functionList", userDB.getDb()); //$NON-NLS-1$
		}
		
		// function filter
		listFunction = DBAccessCtlManager.getInstance().getFunctionFilter(listFunction, userDB);
		
		// 1. 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		// 2. create to default keyword 
		StringBuffer strFunctionlist = new StringBuffer();
		for(ProcedureFunctionDAO pfDao : listFunction) {
			pfDao.setSysName(SQLUtil.makeIdentifierName(userDB, pfDao.getName()));
			strFunctionlist.append(MakeContentAssistUtil.makeObjectPattern(pfDao.getSchema_name(), pfDao.getSysName(), "Function")); //$NON-NLS-1$
		}
		userDB.setFunctionLisstSeparator(StringUtils.removeEnd(strFunctionlist.toString(), MakeContentAssistUtil._PRE_GROUP));
		
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
		if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup() || DBGroupDefine.HIVE_GROUP == userDB.getDBGroup() || DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup())  {
			return new ArrayList<ProcedureFunctionDAO>();
		}
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<ProcedureFunctionDAO> listProcedure = new ArrayList<ProcedureFunctionDAO>();
		if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup() || DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup() ){
			listProcedure = sqlClient.queryForList("procedureList", userDB.getSchema()); //$NON-NLS-1$
		}else if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()){
			listProcedure = sqlClient.queryForList("procedureList", userDB.getSchema()); //$NON-NLS-1$
		}else{
			listProcedure = sqlClient.queryForList("procedureList", userDB.getDb()); //$NON-NLS-1$
		}
		
		// procedure filter
		listProcedure = DBAccessCtlManager.getInstance().getProcedureFilter(listProcedure, userDB);
		
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
	public static List<TriggerDAO> getTrigger(final UserDBDAO userDB, String strObjectName) throws TadpoleSQLManagerException, SQLException {
		List<TriggerDAO> triggerList = null;
		
		if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup() || DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) { 
			triggerList = new ArrayList<TriggerDAO>();
		}else {
		
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
	
			HashMap<String, String>paramMap = new HashMap<String, String>();
			if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup() || DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()){
				paramMap.put("table_schema", userDB.getSchema()); //$NON-NLS-1$
				paramMap.put("table_name", strObjectName); //$NON-NLS-1$
			}else if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()){
				paramMap.put("table_schema", userDB.getSchema()); //$NON-NLS-1$
				paramMap.put("table_name", strObjectName); //$NON-NLS-1$
			}else if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()){
				
				String _objName = StringUtils.substringAfterLast(strObjectName, ".");
				
				paramMap.put("table_schema", userDB.getSchema()); //$NON-NLS-1$
				paramMap.put("table_name", _objName); //$NON-NLS-1$
			}else{
				paramMap.put("table_schema", userDB.getDb()); //$NON-NLS-1$
				paramMap.put("table_name", strObjectName); //$NON-NLS-1$
			}
			triggerList = sqlClient.queryForList("triggerList", paramMap); //$NON-NLS-1$
		}
		
		for(TriggerDAO dao : triggerList) {
			dao.setSysName(SQLUtil.makeIdentifierName(userDB, dao.getName() ));
		}
		
		return triggerList;
	}

	/**
	 * Return trigger all list
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<TriggerDAO> getAllTrigger(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		if(DBGroupDefine.TAJO_GROUP == userDB.getDBGroup() || DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) { 
			return new ArrayList<TriggerDAO>();
		}
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<TriggerDAO> triggerList = sqlClient.queryForList("triggerAllList", userDB.getDb()); //$NON-NLS-1$
		
		for(TriggerDAO dao : triggerList) {
			dao.setSysName(SQLUtil.makeIdentifierName(userDB, dao.getName() ));
		}
		
		return triggerList;
	}
}
