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
package com.hangum.tadpole.manager.core.export;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * System DB 인 UserDB 테이블 데이터를 export 하려는 클래스.
 * 
 *  SystemVersion
 *  OPERATION_TYPE, DBMS_TYPES, URL, DB, GROUP_NAME, DISPLAY_NAME, HOST, PORT, LOCALE, PASSWD, USERS, IS_PROFILE, PROFILE_SELECT_MILL, QUESTION_DML, 
 *  			IS_READONLYCONNECT, IS_AUTOCOMMIT, IS_SHOWTABLES, DELYN
 * 
 * @author hangum
 *
 */
public class SystemDBDataManager {
	public String EXPORT_VER = "01";
	
	/**
	 * 사용자 디비를 export합니다.
	 * 
	 * @param listUserDB
	 * @return
	 */
	public String exportUserDB(List<UserDBDAO> listUserDB) {
		String retStr = SystemDefine.MAJOR_VERSION + SystemDefine.SUB_VERSION + "." + EXPORT_VER + PublicTadpoleDefine.LINE_SEPARATOR;
		retStr += "OPERATION_TYPE, DBMS_TYPES, URL, DB, GROUP_NAME, DISPLAY_NAME, HOST, PORT, LOCALE, PASSWD, USERS, IS_PROFILE, PROFILE_SELECT_MILL, QUESTION_DML,IS_READONLYCONNECT, IS_AUTOCOMMIT, IS_SHOWTABLES, DELYN ";
		
		for (UserDBDAO userDBDAO : listUserDB) {
			retStr  += userDBDAO.getOperation_type() 	+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getDbms_types() 		+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getUrl() 				+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getDb() 				+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getGroup_name() 		+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getDisplay_name() 		+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getHost() 				+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getPort() 				+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getLocale()			+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getPasswd() 			+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getUsers() 			+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getIs_profile() 		+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getProfile_select_mill() 	+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getQuestion_dml() 			+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getIs_readOnlyConnect() 	+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getIs_autocommit() 		+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getIs_showtables() 		+ PublicTadpoleDefine.DELIMITER;
			retStr  += userDBDAO.getDelYn() 				+ PublicTadpoleDefine.DELIMITER;
			
			retStr += PublicTadpoleDefine.LINE_SEPARATOR;
		}
		
		//
		CipherManager cm = CipherManager.getInstance();
		return cm.encryption(retStr);
	}
	
	/**
	 * 사용자 디비를 import 합니다.
	 * 
	 */
	public List<UserDBDAO> importUserDB(String strSource) {
		List<UserDBDAO> retListUserDB = new ArrayList<UserDBDAO>();
		
		CipherManager cm = CipherManager.getInstance();
		String retStr = cm.decryption(strSource);
		String[] strUserdb =  StringUtils.split(PublicTadpoleDefine.LINE_SEPARATOR);
		
		// int 가 1인 이유는 시스템 버전 정보를 빼서입니다.
		for (int i=1; i<strUserdb.length; i++) {
			UserDBDAO userDBDAO = new UserDBDAO();
			
//			userDBDAO.setOperation_type(); 	
//			userDBDAO.setDbms_types() 		
//			userDBDAO.setUrl() 				
//			userDBDAO.setDb() 				
//			userDBDAO.setGroup_name() 		
//			userDBDAO.setDisplay_name() 		
//			userDBDAO.setHost() 				
//			userDBDAO.setPort() 				
//			userDBDAO.setLocale()			
//			userDBDAO.setPasswd() 			
//			userDBDAO.setUsers() 			
//			userDBDAO.setIs_profile() 		
//			userDBDAO.setProfile_select_mill() 	
//			userDBDAO.setQuestion_dml() 			
//			userDBDAO.setIs_readOnlyConnect() 	
//			userDBDAO.setIs_autocommit() 		
//			userDBDAO.setIs_showtables() 		
//			userDBDAO.setDelYn()
			
			retListUserDB.add(userDBDAO);
		}
		
		return retListUserDB;
	}

}
