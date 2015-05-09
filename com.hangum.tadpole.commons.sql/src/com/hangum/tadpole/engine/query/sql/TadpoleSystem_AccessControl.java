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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	public static DBAccessControlDAO saveDBAccessControl(TadpoleUserDbRoleDAO roleDao) throws Exception {
		DBAccessControlDAO dao = new DBAccessControlDAO();
		dao.setDb_role_seq(roleDao.getSeq());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		dao = (DBAccessControlDAO)sqlClient.insert("saveAccessControl", dao);
		
		return dao;
	}
	
	/**
	 * Get user db access contorl data.
	 * 
	 * @param roleDao
	 * @throws Exception
	 */
	public static DBAccessControlDAO getDBAccessControl(TadpoleUserDbRoleDAO roleDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		DBAccessControlDAO dbaccessCtl = (DBAccessControlDAO)sqlClient.queryForObject("getAccessControl", roleDao);
		
		// select 항목이 있다면..
		List<AccessCtlObjectDAO> listAccessDetail = sqlClient.queryForList("getAccessCtlDetail", dbaccessCtl.getSeq());
		Map<String, AccessCtlObjectDAO> mapAccessDetail = new HashMap<String, AccessCtlObjectDAO>();
		for (AccessCtlObjectDAO accessCtlObjectDAO : listAccessDetail) {
			mapAccessDetail.put(accessCtlObjectDAO.getObj_name(), accessCtlObjectDAO);
		}
		dbaccessCtl.setMapSelectAccessCtl(mapAccessDetail);
		
		return dbaccessCtl;
	}
	
	/**
	 * Get user db access contorl data.
	 * 
	 * @param intRoleSeq
	 * @throws Exception
	 */
	public static DBAccessControlDAO getDBAccessControl(int intRoleSeq) throws Exception {
		TadpoleUserDbRoleDAO roleDao = new TadpoleUserDbRoleDAO();
		roleDao.setSeq(intRoleSeq);
		
		return getDBAccessControl(roleDao);
	}
	
	/**
	 * update db access control
	 * 
	 * @param dao
	 * @throws Exception
	 */
	public static void updateDBAccessControl(DBAccessControlDAO dao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateAccessControl", dao);
		
		// already data remove and add data
		sqlClient.update("removeAccessControlDetail", dao);
		
		for(AccessCtlObjectDAO objecDao : dao.getMapSelectAccessCtl().values()) {
			sqlClient.insert("insertAccessControlDetail", objecDao);
		}
	}
	
}
