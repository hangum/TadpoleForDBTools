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
package com.hangum.tadpole.rdb.core.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.DateUtil;
import com.hangum.tadpole.engine.query.dao.system.ExecutedSqlResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.sql.util.export.CSVExpoter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * 쿼리 결과 저장
 * 
 * @author hangum
 *
 */
public class QueryResultSaved {
	private static final Logger logger = Logger.getLogger(QueryResultSaved.class);
	
	private static String getDownloadPath(String strUserID) {
		return GetAdminPreference.getQueryResultSaved() + PublicTadpoleDefine.DIR_SEPARATOR + strUserID +  PublicTadpoleDefine.DIR_SEPARATOR + DateUtil.getYearMonth() + PublicTadpoleDefine.DIR_SEPARATOR;
	}
	
	/**
	 * query 결과 저장
	 * 
	 * @param strDir
	 * @param fileName
	 * @param rsDAO
	 */
	public static void saveQueryResult(String fileName, QueryExecuteResultDTO rsDAO) {
		// 리소스 세이브 디렉토리 + / + 년월 +
		String strFullPath = getDownloadPath(SessionManager.getEMAIL()) + fileName + ".csv";
		
		try {
			if(!new File(strFullPath).exists()) CSVExpoter.makeHeaderFile(strFullPath, true, rsDAO, ',', "UTF-8");
			CSVExpoter.makeContentFile(strFullPath, true, rsDAO, ',', "UTF-8", "");
		} catch(Exception e) {
			logger.error("queryResultSave", e);
		}
	}
	
	/**
	 * 쿼리 결과를 가져온다.
	 * @param dao 
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getQueryResultPath(ExecutedSqlResourceDAO dao, String fileName) {
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.getUser(dao.getDb_seq());
			
			String filefullPath = getDownloadPath(userDao.getEmail()) + fileName + ".csv";
			return filefullPath;
		} catch(Exception e) {
			logger.error("user path", e);
		}
		return "";
	}
}
