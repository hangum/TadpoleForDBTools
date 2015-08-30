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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;

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
	
	public List<TableDAO> getTableFilter(List<TableDAO> showTables, UserDBDAO userDB) {
		if(userDB.getDbAccessCtl().getMapSelectAccessCtl().isEmpty()) return showTables;
		
		List<TableDAO> returnTables = new ArrayList<TableDAO>();
		returnTables.addAll(showTables);
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectAccessCtl();
		for (TableDAO tableDAO : showTables) {
			if(mapSelectAccessCtl.containsKey(tableDAO.getName())) {
				if(PublicTadpoleDefine.YES_NO.YES.name().equals(mapSelectAccessCtl.get(tableDAO.getName()).getDontuse_object())) {
					boolean bool = returnTables.remove(tableDAO);
					if(logger.isDebugEnabled()) logger.debug(tableDAO.getName() + " object removed.");
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
		if(userDB.getDbAccessCtl().getMapSelectAccessCtl().isEmpty()) return listTableColumns;
		
		List<TableColumnDAO> returnColumns = new ArrayList<TableColumnDAO>();
		returnColumns.addAll(listTableColumns);
		
		String strTableName = "";
		if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) strTableName = tableDao.getSysName();
		else 												strTableName = tableDao.getName();
		
		// db access control 
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = userDB.getDbAccessCtl().getMapSelectAccessCtl();
		if(mapSelectAccessCtl.containsKey(strTableName)) {
			AccessCtlObjectDAO accessCtlObjectDao = mapSelectAccessCtl.get(strTableName);
		
			for (TableColumnDAO tableColumnDAO : listTableColumns) {
				if(StringUtils.containsIgnoreCase(accessCtlObjectDao.getDetail_obj(), tableColumnDAO.getField())) {
					returnColumns.remove(tableColumnDAO);
				}
			}
		}
		
		return returnColumns;
	}
	

}
