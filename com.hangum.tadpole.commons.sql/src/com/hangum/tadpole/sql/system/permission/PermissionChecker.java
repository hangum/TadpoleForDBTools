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
package com.hangum.tadpole.sql.system.permission;

import java.util.List;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.template.DBOperationType;
import com.hangum.tadpole.sql.util.SQLUtil;

/**
 * 사용자 혹은 사용자 쿼리의 권한을 검사합니다.
 * 
 * @author hangum
 *
 */
public class PermissionChecker {
	
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
		
		if(SQLUtil.isNotAllowed(strSQL)) {
			return false;
		}
		
		DBOperationType opType = DBOperationType.valueOf(userDB.getOperation_type());
		
		// 디비권한이 read only connection 옵션이 선택되었으면 statement문만 권한을 허락합니다.
		if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDB.getIs_readOnlyConnect())) {
			if(!SQLUtil.isStatement(strSQL)) return false;
		}
		
		//
		// 유저의 권한을 검사합니다.
		//
		if(opType != DBOperationType.PRODUCTION) {
			return true;
		// real db라면 
		} else {
			if(PublicTadpoleDefine.USER_TYPE.ADMIN.toString().equals(strUserType) || 
					PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(strUserType) ||
					PublicTadpoleDefine.USER_TYPE.DBA.toString().equals(strUserType)) return true;
			
			// GUEST USER인 경우 SELECT 만 허락합니다.
			if(SQLUtil.isStatement(strSQL)) return true;
		}
		
		return boolReturn;
	}
	
	/**
	 * Manager이상의 권한이 있는 사용자만 보여준다.
	 * 
	 * @param strUserType
	 * @return
	 */
	public static boolean isDBAShow(String strUserType) {
		boolean boolReturn = false;
		
		if(PublicTadpoleDefine.USER_TYPE.ADMIN.toString().equals(strUserType) || 
				PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(strUserType)) {
			boolReturn = true;
		}
		
		return boolReturn;
	}

	/**
	 * 로그인타입에 따른 보여주어도 되는지
	 * 
	 * @param strUserType
	 * @return
	 */
	public static boolean isShow(String strUserType) {
		boolean boolReturn = false;
		
		if(PublicTadpoleDefine.USER_TYPE.ADMIN.toString().equals(strUserType) || 
				PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(strUserType) ||
				PublicTadpoleDefine.USER_TYPE.DBA.toString().equals(strUserType)) {
			boolReturn = true;
		}
		
		return boolReturn;
	}
	
	/**
	 * 사용자가에 보여 주어도 되는 정보 인지 검사합니다.
	 * 
	 * @param strUserType
	 * @param userDB
	 * @return
	 */
	public static boolean isShow(String strUserType, UserDBDAO userDB) {
		boolean boolReturn = false;
		
		// 디비의 권한이 db type이 read only인 경우 보이지 않도록 합니다.
		if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDB.getIs_readOnlyConnect())) return false;
		
		// 유저의 권한를 검사합니다.
		DBOperationType opType = DBOperationType.valueOf(userDB.getOperation_type());
		// real db가 아니면 모든 사용 권한을 얻습니다.
		if(opType != DBOperationType.PRODUCTION) {
			return true;
		
		// real db라면 
		} else {
			if(PublicTadpoleDefine.USER_TYPE.ADMIN.toString().equals(strUserType) || 
					PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(strUserType) ||
					PublicTadpoleDefine.USER_TYPE.DBA.toString().equals(strUserType)) return true;
		}
		
		return boolReturn;
	}
	
	/**
	 * 사용자가 action을 취할 수 있는지 
	 * 
	 * @param strUserType
	 * @param userDB
	 * @return
	 */
	public static boolean isAction(String strUserType, UserDBDAO userDB) {
		return isShow(strUserType, userDB);
	}

}
 
