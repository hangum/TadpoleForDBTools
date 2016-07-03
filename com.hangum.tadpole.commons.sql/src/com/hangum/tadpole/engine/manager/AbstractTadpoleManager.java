/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.manager;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

public class AbstractTadpoleManager {
	private static final Logger logger = Logger.getLogger(AbstractTadpoleManager.class);
	/**
	 * 
	 * @param userDB
	 * @param conn
	 */
	protected static void setConnectionInitialize(final UserDBDAO userDB, final Connection conn) {
		String applicationName = SystemDefine.NAME;

//		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
//			try {
//				conn.setClientInfo("ApplicationName", applicationName);
//				conn.setClientInfo("ClientUser", userDB.getTdbUserID());
//				conn.setClientInfo("ClientHostname", userDB.getTdbLogingIP());
//		
//			} catch (SQLClientInfoException e) {
//				logger.error("MySQL, Maria connection initialize", e);
//			}
//		}
	}
}
