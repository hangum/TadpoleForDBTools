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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * System DB 인 UserDB 테이블 데이터를 export 하려는 클래스.
 * 
 *  SystemVersion
 *  OPERATION_TYPE, DBMS_TYPES, URL, DB, GROUP_NAME, DISPLAY_NAME, HOST, PORT, LOCALE, PASSWD, USERS, IS_PROFILE, PROFILE_SELECT_MILL, QUESTION_DML, 
 *  			IS_READONLYCONNECT, IS_AUTOCOMMIT, IS_SHOWTABLES, DELYN
 * 
 * @author hangum
 * @deprecated
 */
public class SystemDBDataManager {
	public static String EXPORT_VER = "00001"; //$NON-NLS-1$
	
	/**
	 * head
	 * 
	 * @return
	 */
	public static String makeHead() {
		String retStr = SystemDefine.NAME +  SystemDefine.MAJOR_VERSION + "." + SystemDefine.SUB_VERSION + " " + new Date() + PublicTadpoleDefine.LINE_SEPARATOR; //$NON-NLS-1$ //$NON-NLS-2$
		retStr += "EXPORT_VER:" + EXPORT_VER + PublicTadpoleDefine.LINE_SEPARATOR; //$NON-NLS-1$
		
		retStr += "OPERATION_TYPE	, DBMS_TYPES	, URL					, DB			, GROUP_NAME	, 		" +
				  "DISPLAY_NAME		, HOST			, PORT					, LOCALE		, PASSWD		, 		" +
				  "USERS			, IS_PROFILE	, PROFILE_SELECT_MILL	, QUESTION_DML	, IS_READONLYCONNECT, 	" +
				  "IS_AUTOCOMMIT	, IS_SHOWTABLES	, DELYN ";
		retStr += PublicTadpoleDefine.LINE_SEPARATOR;
		
		return retStr;
	}
	
	/**
	 * 사용자 디비를 export합니다.
	 * 
	 * @param listUserDB
	 * @return
	 */
	public static String exportUserDB(List<UserDBDAO> listUserDB) {
		String retStr = makeHead();
		
		Gson gson = new Gson();
		
		for (UserDBDAO userDBDAO : listUserDB) {
//			if(userDBDAO.getGroup_seq() == SessionManager.getGroupSeq()) {
//				retStr += gson.toJson(userDBDAO);
//				retStr += PublicTadpoleDefine.LINE_SEPARATOR;
//			}
		}
		// google analytic
		AnalyticCaller.track("export user DB");
						
		return retStr;
	}
	
	/**
	 * 사용자 디비를 import 합니다.
	 * 
	 */
	public static void importUserDB(String strSource) throws Exception {
		Gson gson = new Gson();
		
		String[] strUserdb =  StringUtils.split(strSource, PublicTadpoleDefine.LINE_SEPARATOR);
		if(!StringUtils.startsWith(strUserdb[0], SystemDefine.NAME)) {
			throw new RuntimeException(Messages.get().SystemDBDataManager_8);
		}
		
		// int 가 1인 이유는 시스템 버전 정보를 빼서입니다.
		for (int i=3; i<strUserdb.length; i++) {
			UserDBDAO userDBDAO = gson.fromJson(strUserdb[i], UserDBDAO.class);
			TadpoleSystem_UserDBQuery.newUserDB(userDBDAO, SessionManager.getUserSeq());
		}
		
		// google analytic
		AnalyticCaller.track("import user DB");
		
	}

}
