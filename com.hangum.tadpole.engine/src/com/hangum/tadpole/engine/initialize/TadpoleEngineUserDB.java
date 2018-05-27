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
package com.hangum.tadpole.engine.initialize;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Define tadpole engine db
 * 
 * @author hangum
 *
 */
public class TadpoleEngineUserDB {
	private static UserDBDAO tadpoleEngineDB;
	
	/**
	 * 
	 * @param userDB
	 */
	public static void setUserDB(UserDBDAO userDB) {
		tadpoleEngineDB = userDB;
	}

	/**
	 * tadpole system의 default userDB
	 * 
	 * @return
	 */
	public static UserDBDAO getUserDB() {
		return tadpoleEngineDB;
	}

}
