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
package com.hangum.tadpole.engine.utils;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleAuthorityException;

/**
 * LDAP utils
 * 
 * @author hangum
 *
 */
public class LDAPUtil {
	private static final Logger logger = Logger.getLogger(LDAPUtil.class);
	
	public static LDAPUtil instance = null;
	private LDAPUtil() {}
	
	public static LDAPUtil getInstance() {
		if(instance == null) {
			instance = new LDAPUtil();
		}
		
		return instance;
	}
	
	public static void isValidation() throws TadpoleAuthorityException {
		
	}
	
	/**
	 * ldap login 합니다. 
	 * 
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	public static void isValidation(String userId, String userPwd) throws TadpoleAuthorityException {
	}

}
