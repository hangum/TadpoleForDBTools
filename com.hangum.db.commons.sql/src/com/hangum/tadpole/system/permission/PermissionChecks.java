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
package com.hangum.tadpole.system.permission;

import java.util.List;

import com.hangum.tadpole.commons.sql.util.SQLUtil;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.DBOperationType;
import com.hangum.tadpole.define.Define;

/**
 * 사용자가 쿼리를 실행해도 되는지 권한을 검사합니다.
 * 1. 검사 후에 쿼리를 실행 유무를 리턴하고, 실행 쿼리를 저장하도록 합니다.
 * 
 * @author hangum
 *
 */
public class PermissionChecks {
	
	/**
	 * 쿼리중에 하나라도 권한이 허락하지 않으면 false를 리턴합니다.
	 * 
	 * @param strUserType
	 * @param userDB
	 * @param listStrExecuteQuery
	 * @return
	 */
	public static boolean isExecute(String strUserType, UserDBDAO userDB, List<String> listStrExecuteQuery) {
		boolean isReturn = true;
		
		for (String strQuery : listStrExecuteQuery) {
			if(!isExecute(strUserType, userDB, strQuery)) return false;	
		}
		
		return isReturn;
	}

	/**
	 * 쿼리가 실행 가능한지 검사합니다.
	 * {@code UserDBDAO}의 operation_type {@code DBOperationType#REAL}인 경우 {@code Define#USER_TYPE}에서 MANAGER, ADMIN 권한 만 실행 가능합니다.
	 * 
	 * @param strUserType
	 * @param userDB
	 * @param strSQL
	 * @return
	 */
	public static boolean isExecute(String strUserType, UserDBDAO userDB, String strSQL) {
		boolean boolReturn = false;
		
		DBOperationType opType = DBOperationType.valueOf(userDB.getOperation_type());
		// real db가 아니면 모든 사용 권한을 얻습니다.
		if(opType != DBOperationType.REAL) {
			return true;
		
		// real db라면 
		} else {
			
			if(Define.USER_TYPE.ADMIN.toString().equals(strUserType) || 
					Define.USER_TYPE.MANAGER.toString().equals(strUserType) ||
					Define.USER_TYPE.DBA.toString().equals(strUserType)) return true;
			
			// GUEST USER인 경우 SELECT 만 허락합니다.
			if(SQLUtil.isStatment(strSQL)) return true;
			
		}
		
		return boolReturn;
	}
}
 