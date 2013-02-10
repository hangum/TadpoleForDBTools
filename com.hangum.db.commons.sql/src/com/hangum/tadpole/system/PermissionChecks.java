/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.system;

import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * 사용자가 쿼리를 실행해도 되는지 권한을 검사합니다.
 * 1. 검사 후에 쿼리를 실행 유무를 리턴하고, 실행 쿼리를 저장하도록 합니다.
 * 
 * @author hangum
 *
 */
public class PermissionChecks {

	public boolean isExecute(UserDBDAO userDB) {
		boolean boolReturn = false;
		
		
		return boolReturn;
	}
}
 