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
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * db access control
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 27.
 *
 */
public class TadpoleSystem_AccessControl {

	/**
	 * To save default access control data.
	 * 
	 */
	public static DBAccessControlDAO saveDBAccessControl(TadpoleUserDbRoleDAO roleDao) throws TadpoleSQLManagerException, SQLException {
		DBAccessControlDAO dao = new DBAccessControlDAO();
		dao.setDb_role_seq(roleDao.getSeq());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		dao = (DBAccessControlDAO)sqlClient.insert("saveAccessControl", dao);
		
		return dao;
	}
	
	/**
	 * Get user db access contorl data.
	 * 
	 * @param roleDao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static DBAccessControlDAO getDBAccessControl(TadpoleUserDbRoleDAO roleDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		DBAccessControlDAO _accessCtlDao = (DBAccessControlDAO)sqlClient.queryForObject("getAccessControl", roleDao);
		
		if(_accessCtlDao.getIntDetailCnt() == 0) {
			_accessCtlDao.setAllAccessCtl(new ArrayList<AccessCtlObjectDAO>());
		} else {
			// select 항목이 있다면..
			List<AccessCtlObjectDAO> listAccessDetail = sqlClient.queryForList("getAccessCtlDetail", roleDao.getSeq());
			_accessCtlDao.setAllAccessCtl(listAccessDetail);
			
			Map<String, AccessCtlObjectDAO> mapSelectTableAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
			Map<String, AccessCtlObjectDAO> mapSelectFunctionAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
			Map<String, AccessCtlObjectDAO> mapSelectProcedureAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
			for (AccessCtlObjectDAO accCtlObj : listAccessDetail) {
				if(PublicTadpoleDefine.ACCEAS_CTL_DDL_TYPE.TABLEoVIEW.name().equals(accCtlObj.getType())) {
					mapSelectTableAccessCtl.put(String.format("%s.%s", accCtlObj.getObj_schema(), accCtlObj.getObj_name()), accCtlObj);
				} else if(PublicTadpoleDefine.ACCEAS_CTL_DDL_TYPE.FUNCTION.name().equals(accCtlObj.getType())) {
					mapSelectFunctionAccessCtl.put(String.format("%s.%s", accCtlObj.getObj_schema(), accCtlObj.getObj_name()), accCtlObj);
				} else if(PublicTadpoleDefine.ACCEAS_CTL_DDL_TYPE.PROCEDURE.name().equals(accCtlObj.getType())) {
					mapSelectProcedureAccessCtl.put(String.format("%s.%s", accCtlObj.getObj_schema(), accCtlObj.getObj_name()), accCtlObj);
				}
			}
			_accessCtlDao.setMapSelectTableAccessCtl(mapSelectTableAccessCtl);
			_accessCtlDao.setMapSelectFunctionAccessCtl(mapSelectFunctionAccessCtl);
			_accessCtlDao.setMapSelectProcedureAccessCtl(mapSelectProcedureAccessCtl);
		} 
		
		return _accessCtlDao;
	}
	
	/**
	 * Get user db access contorl data.
	 * 
	 * @param intRoleSeq
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static DBAccessControlDAO getDBAccessControl(int intRoleSeq) throws TadpoleSQLManagerException, SQLException {
		TadpoleUserDbRoleDAO roleDao = new TadpoleUserDbRoleDAO();
		roleDao.setSeq(intRoleSeq);
		
		return getDBAccessControl(roleDao);
	}
	
	/**
	 * update db access control
	 * 
	 * @param dao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateDBAccessControl(DBAccessControlDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateAccessControl", dao);
		
		// already data remove and add data
		sqlClient.update("removeAccessControlDetail", dao);
		
		for(AccessCtlObjectDAO objecDao : dao.getAllAccessCtl()) {
			sqlClient.insert("insertAccessControlDetail", objecDao);
		}
	}
	
}
