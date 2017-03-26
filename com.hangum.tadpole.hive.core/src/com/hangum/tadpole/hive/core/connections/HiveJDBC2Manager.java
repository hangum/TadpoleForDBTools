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
package com.hangum.tadpole.hive.core.connections;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.surface.BasicDBInfo;

public class HiveJDBC2Manager extends BasicDBInfo {
	private static final Logger logger = Logger.getLogger(HiveJDBC2Manager.class);
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	private static HiveJDBC2Manager instance = null;
	
	/**
	 * java.sql.connection을 생성하고 관리합니다.
	 * 
	 * @param userDB
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getInstance(final UserDBDAO userDB) throws Exception {
		if(instance == null) {
		  try {
		      Class.forName(driverName);
		    } catch (ClassNotFoundException e) {
		      e.printStackTrace();
		    }
		  
		  instance = new HiveJDBC2Manager();
		}
		java.sql.Connection javaConn = DriverManager.getConnection(userDB.getUrl(), userDB.getUsers(), userDB.getPasswd());
			
		return javaConn;
	}


}
