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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.restful.TadpoleException;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

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
	public void tableFilterTest(final UserDBDAO userDB, final String strSQL) throws TadpoleException {
		
	}
	
	/**
	 * table filter
	 * @param showTables
	 * @param userDB
	 * @return
	 */
	public List<TableDAO> getTableFilter(List<TableDAO> showTables, UserDBDAO userDB) {
		if(userDB.getDbAccessCtl().getMapSelectTableAccessCtl().isEmpty()) return showTables;
		
		// return data
		List<TableDAO> returnObjects = new ArrayList<TableDAO>();
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectTableAccessCtl();
		
		AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapSelectAccessCtl.get(""+mapSelectAccessCtl.keySet().toArray()[0]);
		String strFilterType = accCtlObj.getFilter_type();
		if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
			for (TableDAO tableDAO : showTables) {
				if(!mapSelectAccessCtl.containsKey(tableDAO.getSchema_name() + "." + tableDAO.getName())) {
					returnObjects.add(tableDAO);
//					if(logger.isDebugEnabled()) logger.debug(tableDAO.getName() + " add object.");
				}
			}
			
		} else {
			for (TableDAO tableDAO : showTables) {
				if(logger.isDebugEnabled()) logger.debug("===> " + tableDAO.getSchema_name() + "." + tableDAO.getName());
				if(mapSelectAccessCtl.containsKey(tableDAO.getSchema_name() + "." + tableDAO.getName())) {
					returnObjects.add(tableDAO);
//					if(logger.isDebugEnabled()) logger.debug(tableDAO.getName() + " add object.");
				}
			}
		}
		
		return returnObjects;
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
		
		List<ProcedureFunctionDAO> returnObjects = new ArrayList<ProcedureFunctionDAO>();
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectFunctionAccessCtl();
		AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapSelectAccessCtl.get(""+mapSelectAccessCtl.keySet().toArray()[0]);
		String strFilterType = accCtlObj.getFilter_type();
		if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
//			returnTables.addAll(listFunction);
			for (ProcedureFunctionDAO funcDAO : listFunction) {
				if(!mapSelectAccessCtl.containsKey(funcDAO.getSchema_name() + "." + funcDAO.getName())) {
					returnObjects.remove(funcDAO);
//					if(logger.isDebugEnabled()) logger.debug(funcDAO.getName() + " object removed.");
				}
			}
			
		} else {
			for (ProcedureFunctionDAO funcDAO : listFunction) {
				if(mapSelectAccessCtl.containsKey(funcDAO.getSchema_name() + "." + funcDAO.getName())) {
					returnObjects.add(funcDAO);
//					if(logger.isDebugEnabled()) logger.debug(funcDAO.getName() + " add object.");
				}
			}
		}
		
		return returnObjects;
	}

	/**
	 * procedure filter
	 * 
	 * @param listProcedure
	 * @param userDB
	 * @return
	 */
	public List<ProcedureFunctionDAO> getProcedureFilter(List<ProcedureFunctionDAO> listProcedure, UserDBDAO userDB) {
		if(userDB.getDbAccessCtl().getMapSelectProcedureAccessCtl().isEmpty()) return listProcedure;
		
		List<ProcedureFunctionDAO> returnObjects = new ArrayList<ProcedureFunctionDAO>();
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectProcedureAccessCtl();
		AccessCtlObjectDAO accCtlObj = (AccessCtlObjectDAO)mapSelectAccessCtl.get(""+mapSelectAccessCtl.keySet().toArray()[0]);
		String strFilterType = accCtlObj.getFilter_type();
		if(PublicTadpoleDefine.FILTER_TYPE.EXCLUDE.name().equals(strFilterType)) {
//			returnObjects.addAll(listProcedure);
			for (ProcedureFunctionDAO procDAO : listProcedure) {
				if(!mapSelectAccessCtl.containsKey(procDAO.getSchema_name() + "." + procDAO.getName())) {
					returnObjects.remove(procDAO);
//					if(logger.isDebugEnabled()) logger.debug(procDAO.getName() + " object removed.");
				}
			}
			
		} else {
			for (ProcedureFunctionDAO procDAO : listProcedure) {
				if(mapSelectAccessCtl.containsKey(procDAO.getSchema_name() + "." + procDAO.getName())) {
					returnObjects.add(procDAO);
//					if(logger.isDebugEnabled()) logger.debug(procDAO.getName() + " add object.");
				}
			}
		}
		
		return returnObjects;
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
