/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.security;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.core.manager.TDBObjectParser;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.sql.parser.define.ParserDefine;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

import net.sf.jsqlparser.parser.CCJSqlParserManager;

/**
 * DBAccess Control manager
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 2.
 *
 */
public class DBAccessCtlManager {
	private static final Logger logger = Logger.getLogger(DBAccessCtlManager.class);
	private static DBAccessCtlManager instance = null;

	/**
	 * 
	 */
	private DBAccessCtlManager() {}
	
	public static DBAccessCtlManager getInstance() {
		if(instance == null) {
			instance = new DBAccessCtlManager();
		}
		
		return instance;
	}
	
	/**
	 * table filter test
	 * 
	 * @param userDB
	 * @param strSQL
	 * @throws Exception
	 */
	public void tableFilterTest(final UserDBDAO userDB, final String strSQL) throws Exception {
		if(userDB.getDbAccessCtl().getAllAccessCtl().isEmpty()) return;
		if(logger.isDebugEnabled()) logger.debug("####################### SQL: " + strSQL);
		
		CCJSqlParserManager pm = new CCJSqlParserManager();
		net.sf.jsqlparser.statement.Statement statement = null;
		try {
			statement = pm.parse(new StringReader(strSQL));
		} catch(Exception e) {
			logger.error("SQL Parsing exception", e);
			
//			if(logger.isDebugEnabled()) {
				logger.debug("#########################################################");
				logger.error(String.format("==[sql parsing exception]===DB : %s, SQL: %s", userDB.getDbms_type(), strSQL));
				logger.debug("#########################################################");
//			}
		}

		// 
		// statement 가 에러이면 어떻게 하지?
		//		쿼리 파싱이 잘 못 되었으면...
		//
		//
		boolean isClearTable 		= true;
		String strMsgTable 			= "";
//		boolean isClearProcedure 	= true;
//		String strMsgProcedure 		= "";
		boolean isClearFunction	 	= true;
		String strMsgFunction 		= "";
		
		if(statement == null) {
			// MySQL procedure 호출 인지 검사합니다. (CALL 명령)
			String strOriSQL = SQLUtil.removeComment(strSQL);
			Matcher matcher = Pattern.compile("CALL\\s+([A-Z0-9_\\.\"'`]+)", ParserDefine.PATTERN_FLAG).matcher(strOriSQL);
			if(matcher.find()) {
				if(logger.isDebugEnabled()) {
					logger.debug("#########################################################");
					logger.debug("=-------=>>> " + matcher.group(1));
					logger.debug("#########################################################");
				}
				String procedureName = matcher.group(1);
				
//				// =========[procedure]=====================================================================================================================================================
				final Map<String, AccessCtlObjectDAO> mapProcedureAccessCtl = userDB.getDbAccessCtl().getMapSelectProcedureAccessCtl();
				if(!mapProcedureAccessCtl.isEmpty()) {
					final AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapProcedureAccessCtl.get(""+mapProcedureAccessCtl.keySet().toArray()[0]);
					final String strFilterType = accCtlObj.getFilter_type();
	
					if(logger.isDebugEnabled()) logger.debug("### Table filter test ####### SQL: " + strSQL);
					if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
		                String fullName = StringUtils.contains(procedureName, ".")?procedureName:userDB.getSchema() + "." + procedureName;
		                
		                if(logger.isDebugEnabled()) logger.debug("\t#### parse procedure " + fullName);
		                if(mapProcedureAccessCtl.containsKey(fullName)) {
		                	if(logger.isDebugEnabled()) logger.debug(String.format("\t type: %s, table: %s 있습니다.", PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name(), fullName));
	    					throw new Exception(String.format("Procedure: %s은 접근 할 수 없습니다.", fullName));
		                }
					} else {
						String fullName = StringUtils.contains(procedureName, ".")?procedureName:userDB.getSchema() + "." + procedureName;
		                
		                if(logger.isDebugEnabled()) logger.debug("\t#### parse procedure " + fullName);
		                if(!mapProcedureAccessCtl.containsKey(fullName)) {
		                	throw new Exception(String.format("Procedure: %s은 접근 할 수 없습니다.", fullName));
		                }
					}
				}

			}
		} else {

			final TDBObjectParser tableFunctionFinder = new TDBObjectParser();
			final List<String> _listTables = tableFunctionFinder.getTableList(statement);
			final List<String> _listFunctionProcedure = tableFunctionFinder.getFunctionProcedureList();

			final Map<String, AccessCtlObjectDAO> mapTableAccessCtl = userDB.getDbAccessCtl().getMapSelectTableAccessCtl();
			if(!mapTableAccessCtl.isEmpty()) {
				final AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapTableAccessCtl.get(""+mapTableAccessCtl.keySet().toArray()[0]);
				final String strFilterType = accCtlObj.getFilter_type();
				
				List<String> _listObject = new ArrayList<String>();
				boolean isFindTable = true;
				if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
					for(String tableName : _listTables) {
		                String fullName = StringUtils.contains(tableName, ".")?tableName:userDB.getSchema() + "." + tableName;
		                
		                if(logger.isDebugEnabled()) logger.debug("\t#### parse tableName " + fullName);
		                if(mapTableAccessCtl.containsKey(fullName)) {
		                	_listObject.add(fullName);
		                	if(logger.isDebugEnabled()) logger.debug(String.format("\t type: %s, table: %s 있습니다.", PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name(), fullName));
		                	isFindTable = false;
		                }
		            }	
				} else {
					for(String tableName : _listTables) {
		                String fullName = StringUtils.contains(tableName, ".")?tableName:userDB.getSchema() + "." + tableName;
		                
		                if(logger.isDebugEnabled()) logger.debug("\t#### parse tableName " + fullName);
		                if(!mapTableAccessCtl.containsKey(fullName)) {
		                	_listObject.add(fullName);
		                	if(logger.isDebugEnabled()) logger.debug(String.format("\t type: %s, table: %s 있습니다.", PublicTadpoleDefine.FILTER_TYPE.INCLUDE.name(), fullName));
		                	isFindTable = false;
		                }
		            }
				}
				
				if(!isFindTable) {
					isClearTable = false;
		            strMsgTable = String.format("Table: %s은 접근 할 수 없습니다.\n", StringUtils.join(_listObject.toArray(), ", " ));
				}
			}
			
			// =========[function]=====================================================================================================================================================
			final Map<String, AccessCtlObjectDAO> mapFunctionAccessCtl = userDB.getDbAccessCtl().getMapSelectFunctionAccessCtl();
			if(!mapFunctionAccessCtl.isEmpty()) {
				final AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapFunctionAccessCtl.get(""+mapFunctionAccessCtl.keySet().toArray()[0]);
				final String strFilterType = accCtlObj.getFilter_type();

				List<String> _listObject = new ArrayList<String>();
				boolean isFindTable = true;
				if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
					for(String procedureName : _listFunctionProcedure) {
		                String fullName = StringUtils.contains(procedureName, ".")?procedureName:userDB.getSchema() + "." + procedureName;
		                
		                if(logger.isDebugEnabled()) logger.debug("\t#### parse function " + fullName);
		                if(mapFunctionAccessCtl.containsKey(fullName)) {
		                	_listObject.add(fullName);
		                	if(logger.isDebugEnabled()) logger.debug(String.format("\t type: %s, table: %s 있습니다.", PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name(), fullName));
		                	isFindTable = false;
		                }
		            }	
				} else {
					for(String procedureName : _listFunctionProcedure) {
		                String fullName = StringUtils.contains(procedureName, ".")?procedureName:userDB.getSchema() + "." + procedureName;
		                
		                if(logger.isDebugEnabled()) logger.debug("\t#### parse function " + fullName);
		                if(!mapFunctionAccessCtl.containsKey(fullName)) {
		                	_listObject.add(fullName);
		                	if(logger.isDebugEnabled()) logger.debug(String.format("\t type: %s, table: %s 있습니다.", PublicTadpoleDefine.FILTER_TYPE.INCLUDE.name(), fullName));
		                	isFindTable = false;
		                }
		            }
				}
				
				if(!isFindTable) {
					isClearFunction = false;
	                strMsgFunction = String.format("Function: %s은 접근 할 수 없습니다.\n", StringUtils.join(_listObject.toArray(), ", " ));
				}
			}
		}
				
		if(!isClearTable || !isClearFunction) {
			throw new Exception(String.format("%s%s", strMsgTable, strMsgFunction));
		}
	}
	
	/**
	 * table filter
	 * @param showTables
	 * @param userDB
	 * @return
	 */
	public List<TableDAO> getTableFilter(List<TableDAO> showTables, UserDBDAO userDB) {
		if(userDB.getDbAccessCtl().getMapSelectTableAccessCtl().isEmpty()) return showTables;
		
		List<TableDAO> returnTables = new ArrayList<TableDAO>();
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectTableAccessCtl();
		
		AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapSelectAccessCtl.get(""+mapSelectAccessCtl.keySet().toArray()[0]);
		String strFilterType = accCtlObj.getFilter_type();
		if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
			returnTables.addAll(showTables);
			for (TableDAO tableDAO : showTables) {
				if(mapSelectAccessCtl.containsKey(tableDAO.getSchema_name() + "." + tableDAO.getName())) {
					boolean bool = returnTables.remove(tableDAO);
					if(logger.isDebugEnabled()) logger.debug(tableDAO.getName() + " object removed.");
				}
			}
			
		} else {
			for (TableDAO tableDAO : showTables) {
				if(logger.isDebugEnabled()) logger.debug("===> " + tableDAO.getSchema_name() + "." + tableDAO.getName());
				if(mapSelectAccessCtl.containsKey(tableDAO.getSchema_name() + "." + tableDAO.getName())) {
					boolean bool = returnTables.add(tableDAO);
					if(logger.isDebugEnabled()) logger.debug(tableDAO.getName() + " add object.");
				}
			}
		}
		
		return returnTables;
	}
	
	/**
	 * function filter
	 * 
	 * @param listFunction
	 * @param userDB
	 * @return
	 */
	public List<ProcedureFunctionDAO> getFunctionFilter(List<ProcedureFunctionDAO> listFunction, UserDBDAO userDB) {
		if(userDB.getDbAccessCtl().getMapSelectFunctionAccessCtl().isEmpty()) return listFunction;
		
		List<ProcedureFunctionDAO> returnTables = new ArrayList<ProcedureFunctionDAO>();
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectFunctionAccessCtl();
		AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapSelectAccessCtl.get(""+mapSelectAccessCtl.keySet().toArray()[0]);
		String strFilterType = accCtlObj.getFilter_type();
		if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
			returnTables.addAll(listFunction);
			for (ProcedureFunctionDAO funcDAO : listFunction) {
				if(mapSelectAccessCtl.containsKey(funcDAO.getSchema_name() + "." + funcDAO.getName())) {
					boolean bool = returnTables.remove(funcDAO);
					if(logger.isDebugEnabled()) logger.debug(funcDAO.getName() + " object removed.");
				}
			}
			
		} else {
			for (ProcedureFunctionDAO funcDAO : listFunction) {
				if(mapSelectAccessCtl.containsKey(funcDAO.getSchema_name() + "." + funcDAO.getName())) {
					boolean bool = returnTables.add(funcDAO);
					if(logger.isDebugEnabled()) logger.debug(funcDAO.getName() + " add object.");
				}
			}
		}
		
		return returnTables;
	}

	/**
	 * procedure filter
	 * 
	 * @param listProcedure
	 * @param userDB
	 * @return
	 */
	public List<ProcedureFunctionDAO> getProcedureFilter(List<ProcedureFunctionDAO> listProcedure, UserDBDAO userDB) {
		if(userDB.getDbAccessCtl().getMapSelectFunctionAccessCtl().isEmpty()) return listProcedure;
		
		List<ProcedureFunctionDAO> returnTables = new ArrayList<ProcedureFunctionDAO>();
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectProcedureAccessCtl();
		AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapSelectAccessCtl.get(""+mapSelectAccessCtl.keySet().toArray()[0]);
		String strFilterType = accCtlObj.getFilter_type();
		if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
			returnTables.addAll(listProcedure);
			for (ProcedureFunctionDAO procDAO : listProcedure) {
				if(mapSelectAccessCtl.containsKey(procDAO.getSchema_name() + "." + procDAO.getName())) {
					boolean bool = returnTables.remove(procDAO);
					if(logger.isDebugEnabled()) logger.debug(procDAO.getName() + " object removed.");
				}
			}
			
		} else {
			for (ProcedureFunctionDAO procDAO : listProcedure) {
				if(mapSelectAccessCtl.containsKey(procDAO.getSchema_name() + "." + procDAO.getName())) {
					boolean bool = returnTables.add(procDAO);
					if(logger.isDebugEnabled()) logger.debug(procDAO.getName() + " add object.");
				}
			}
		}
		
		return returnTables;
	}
	
	/**
	 * get column filter
	 * 
	 * @param strTableName
	 * @param listTableColumns
	 * @param userDB
	 * @return
	 */
	public List<TableColumnDAO> getColumnFilter(TableDAO tableDao, List<TableColumnDAO> listTableColumns, UserDBDAO userDB) {
		return listTableColumns;
//		if(userDB.getDbAccessCtl().getMapSelectAccessCtl().isEmpty()) return listTableColumns;
//		
//		List<TableColumnDAO> returnColumns = new ArrayList<TableColumnDAO>();
//		returnColumns.addAll(listTableColumns);
//		String strTableName = "";
//		if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) strTableName = tableDao.getSysName();
//		else 												strTableName = tableDao.getName();
//		
//		// db access control 
//		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectAccessCtl();
//		if(mapSelectAccessCtl.containsKey(strTableName)) {
//			AccessCtlObjectDAO accessCtlObjectDao = mapSelectAccessCtl.get(strTableName);
//		
//			for (TableColumnDAO tableColumnDAO : listTableColumns) {
//				if(StringUtils.containsIgnoreCase(accessCtlObjectDao.getDetail_obj(), tableColumnDAO.getField())) {
//					returnColumns.remove(tableColumnDAO);
//				}
//			}
//		}
		
//		return returnColumns;
	}

}
