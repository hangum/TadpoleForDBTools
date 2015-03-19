/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.adapter.rdb;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.ui.IActionFilter;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * rdb db popup
 * 
 * @author hangum
 *
 */
public class DBTypeActionFilterAdapter implements IActionFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DBTypeActionFilterAdapter.class);
	
	private static final String DB_TYPE = "db_type";
	private static final String USER_TYPE = "user_type";
	
	@Override
	public boolean testAttribute(Object target, String name, String value) {
		UserDBDAO userDB = null;
		
		if (target instanceof UserDBDAO) {
			userDB = (UserDBDAO)target;
			
			// db 종류에 따라.
			if(DB_TYPE.equals(name)) {
				String[] dbTypes = StringUtils.split(value, ",");
				for (String dbType : dbTypes) {
					if(userDB.getDbms_type().equalsIgnoreCase(dbType)) {
						return true;
					}
				}
				
			// 사용자 권한에 따라.
			} else if(USER_TYPE.equals(name)) {
				final String[] userTypes = StringUtils.split(value, ",");
				final String strRoleType = userDB.getRole_id();
				for (String userType : userTypes) {
					if(strRoleType.equalsIgnoreCase(userType)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

}
