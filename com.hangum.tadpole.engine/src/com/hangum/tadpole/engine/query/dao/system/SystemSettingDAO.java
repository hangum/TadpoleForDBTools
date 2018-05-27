/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system;

import com.hangum.tadpole.preference.define.AdminPreferenceDefine;

/**
 * System setting
 * 
 * @author hangum
 *
 */
public class SystemSettingDAO {
	String newUserPermit = AdminPreferenceDefine.NEW_USER_PERMIT_VALUE;
	
	public SystemSettingDAO() {
	}

	/**
	 * @return the newUserPermit
	 */
	public String getNewUserPermit() {
		return newUserPermit;
	}

	/**
	 * @param newUserPermit the newUserPermit to set
	 */
	public void setNewUserPermit(String newUserPermit) {
		this.newUserPermit = newUserPermit;
	}
	
}
