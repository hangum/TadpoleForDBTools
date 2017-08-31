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
package com.hangum.tadpole.engine.permission;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

/**
 * 사용자 혹은 사용자 쿼리의 권한을 검사합니다.
 * 
 * @author hangum
 *
 */
public class PermissionChecker {
	private static final Logger logger = Logger.getLogger(PermissionChecker.class);
	/**
	 * db를 추가/삭제/수정 할 수 있는 권한이 있는지.
	 * 
	 * @param userDB
	 * @return
	 */
	public static boolean isDBAdminRole(UserDBDAO userDB) {
		String strDBRole = userDB.getRole_id();
		
		if(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.name().equals(strDBRole) || 
				PublicTadpoleDefine.DB_USER_ROLE_TYPE.MANAGER.name().equals(strDBRole) ||
				PublicTadpoleDefine.DB_USER_ROLE_TYPE.DBA.name().equals(strDBRole)) return true;
		
		return false;
	}
	
	/**
	 * db 가 프러덕이거나 백업 디비이면. 
	 * @param userDB
	 * @return
	 */
	public static boolean isProductBackup(UserDBDAO userDB) {
		String strDBType = userDB.getOperation_type();
		
		if(PublicTadpoleDefine.DBOperationType.PRODUCTION.name().equals(strDBType) ||
				PublicTadpoleDefine.DBOperationType.BACKUP.name().equals(strDBType)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 쿼리중에 하나라도 권한이 허락하지 않으면 false를 리턴합니다.
	 * 
	 * @param strUserType
	 * @param userDB
	 * @param listStrExecuteQuery
	 * @return
	 */
	public static boolean isExecute(String strUserType, UserDBDAO userDB, List<String> listStrExecuteQuery) throws Exception {
		boolean isReturn = true;
		
		for (String strQuery : listStrExecuteQuery) {
			isExecute(strUserType, userDB, strQuery);	
		}
		
		return isReturn;
	}

	/**
	 * 쿼리가 실행 가능한지 검사합니다.
	 * {@code UserDBDAO}의 operation_type {@code PublicTadpoleDefine#DBOperationType}인 경우 {@code PublicTadpoleDefine#USER_ROLE_TYPE}에서 MANAGER, ADMIN 권한 만 실행 가능합니다.
	 * 
	 * @param strUserType
	 * @param userDB
	 * @param strSQL
	 * @return
	 */
	public static void isExecute(String strUserType, UserDBDAO userDB, String strSQL) throws Exception {
//		boolean boolReturn = false;
		
//		if(SQLUtil.isNotAllowed(strSQL)) {
//			return false;
//		}
		
		// 디비권한이 read only connection 옵션이 선택되었으면 statement문만 권한을 허락합니다.
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_readOnlyConnect())) {
			if(!SQLUtil.isStatement(strSQL)) throw new Exception("읽기전용 데이터베이스 입니다. ");
		}
		
//		//
//		// 유저가 일반 권한일 경우 프러덕션 디비의 수정을 할 수 없습니다.
//		//
//		PublicTadpoleDefine.DBOperationType opType = PublicTadpoleDefine.DBOperationType.valueOf(userDB.getOperation_type());
//		if(opType != PublicTadpoleDefine.DBOperationType.PRODUCTION) {
//			boolReturn = true;
//		// real db라면 
//		} else {
//			if(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.name().equals(strUserType) || 
//					PublicTadpoleDefine.DB_USER_ROLE_TYPE.MANAGER.name().equals(strUserType) ||
//					PublicTadpoleDefine.DB_USER_ROLE_TYPE.DBA.name().equals(strUserType)) boolReturn = true;
//			
//			// GUEST USER인 경우 SELECT 만 허락합니다.
//			if(SQLUtil.isStatement(strSQL)) boolReturn = true;
//		}
		
		try {
			DBAccessCtlManager.getInstance().tableFilterTest(userDB, strSQL);
		} catch (Exception e) {
			logger.error("table filter exception", e);
			throw e;
		}
		
//		return boolReturn;
	}
	
	/**
	 * isadmin
	 * 
	 * @param strUserType
	 * @return
	 */
	public static boolean isUserAdmin(String strUserType) {
		boolean boolReturn = false;
		
		if(PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN.name().equals(strUserType)) { 
			boolReturn = true;
		}
		
		return boolReturn;
	}
	
	/**
	 * is observer
	 * 
	 * @param strUserType
	 * @return
	 */
	public static boolean isUserObserver(String strUserType) {
		boolean boolReturn = false;
		
		if(PublicTadpoleDefine.USER_ROLE_TYPE.OBSERVER.name().equals(strUserType)) { 
			boolReturn = true;
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
		
		if(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.toString().equals(strUserType) || 
				PublicTadpoleDefine.DB_USER_ROLE_TYPE.MANAGER.toString().equals(strUserType) ||
				PublicTadpoleDefine.DB_USER_ROLE_TYPE.DBA.toString().equals(strUserType)
				) {
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
		
		if(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.toString().equals(strUserType) || 
				PublicTadpoleDefine.DB_USER_ROLE_TYPE.MANAGER.toString().equals(strUserType) ||
				PublicTadpoleDefine.DB_USER_ROLE_TYPE.DBA.toString().equals(strUserType)) {
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
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_readOnlyConnect())) return false;
		
		// 유저의 권한를 검사합니다.
		PublicTadpoleDefine.DBOperationType opType = PublicTadpoleDefine.DBOperationType.valueOf(userDB.getOperation_type());
		// real db가 아니면 모든 사용 권한을 얻습니다.
		if(opType != PublicTadpoleDefine.DBOperationType.PRODUCTION) {
			return true;
		
		// real db라면 
		} else {
			if(PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.name().equals(strUserType) || 
					PublicTadpoleDefine.DB_USER_ROLE_TYPE.MANAGER.name().equals(strUserType) ||
					PublicTadpoleDefine.DB_USER_ROLE_TYPE.DBA.name().equals(strUserType)) return true;
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
 
