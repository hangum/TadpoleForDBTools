/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.query.dao.gateway.ExtensionDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExtensionDB;
import com.hangum.tadpole.engine.utils.MakeJDBCConnectionStringUtil;

/**
 * gateway 
 * 
 * @author hangum
 *
 */
public class TDBGatewayManager {
	private static final Logger logger = Logger.getLogger(TadpoleSQLManager.class);
	
	
	/**
	 * make gateway server
	 * 
	 * @param strUserID
	 * @param userDB
	 * @param isGateWayIDCheck
	 * @return
	 * @throws TadpoleSQLManagerException
	 */
	public static UserDBDAO makeGatewayServer(final String strUserID, final UserDBDAO userDB, boolean isGateWayIDCheck) throws TadpoleSQLManagerException {
		List<ExtensionDBDAO> listExtensionInfo = new ArrayList<ExtensionDBDAO>();
		try {
			listExtensionInfo = TadpoleSystem_ExtensionDB.getExtensionInfo(getExtensionKey(userDB));
		} catch (Exception e) {
			logger.error("get gateway serverlist", e);
		}
		
		if(listExtensionInfo.isEmpty()) return userDB;

		// 사용자 id까지 검사하면.
		if(isGateWayIDCheck) {
//			final String strUserID = SessionManager.getEMAIL();
			boolean isFindUser = false;
			for (ExtensionDBDAO extensionDAO : listExtensionInfo) {
				if(strUserID.equals(extensionDAO.getId())) {
					userDB.setHost(extensionDAO.getGate_host());
					userDB.setPort(extensionDAO.getGate_port());
					userDB.setUrl(MakeJDBCConnectionStringUtil.makeConnectionUrl(userDB));
					if(logger.isDebugEnabled()) {
						logger.debug("**** renew url is " + userDB.getUrl());
					}
					
					isFindUser = true;
					break;
				}
			}
			
			//
			//
			//
			if(!isFindUser) {
				String strErrMsg = String.format("%s user is do not accesss db %s", strUserID, userDB.getHost());
				logger.info(strErrMsg);
				throw new TadpoleSQLManagerException(strErrMsg);
			}
		} else {
			ExtensionDBDAO extensionDAO = listExtensionInfo.get(0);
			
			userDB.setHost(extensionDAO.getGate_host());
			userDB.setPort(extensionDAO.getGate_port());
			userDB.setUrl(MakeJDBCConnectionStringUtil.makeConnectionUrl(userDB));
			if(logger.isDebugEnabled()) {
				logger.debug("**** renew url is " + userDB.getUrl());
			}
		}
		
		return userDB;
	}
	
	/**
	 * 확장 디비를 사용 할 경우 사용자 키를 정의합니다.
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getExtensionKey(final UserDBDAO userDB) {
		return 	userDB.getHost() + userDB.getPort() + userDB.getDb() + userDB.getUsers();
	}
}
