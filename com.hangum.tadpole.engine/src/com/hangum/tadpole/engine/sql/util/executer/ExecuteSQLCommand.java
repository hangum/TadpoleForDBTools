/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.executer;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.engine.sql.paremeter.NamedParameterDAO;
import com.hangum.tadpole.engine.sql.paremeter.NamedParameterUtil;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.session.manager.SessionManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * SQL을 실행하기 위해 유틸 클래스
 * 
 * @author hangum
 *
 */
public class ExecuteSQLCommand {
	private static final Logger logger = Logger.getLogger(ExecuteSQLCommand.class);
	
	/**
	 * 검색
	 * 
	 * @param strSQL
	 * @param strArgument
	 */
	public static String executeQuerForAPI(RequestQuery reqQuery, String strArgument, ExecuteDMLCommand.RESULT_TYPE user_RESULTTYPE, boolean isAddHead, String strDelimiter) throws Exception {
		UserDBDAO userDB = reqQuery.getUserDB();
		String strExecuteResultData = ""; //$NON-NLS-1$
		String strSQLs = "";
		
		RequestResultDAO reqResultDAO = new RequestResultDAO();
		reqResultDAO.setEXECUSTE_SQL_TYPE(PublicTadpoleDefine.EXECUTE_SQL_TYPE.API_USER);
		reqResultDAO.setDbSeq(userDB.getSeq());
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(SessionManager.getLoginIp());
		
		try {
			int intSQLCnt = 0;
			// velocity 로 if else 가 있는지 검사합니다. 
			strSQLs = RESTfulAPIUtils.makeTemplateTOSQL("APIServiceDialog", reqQuery.getSql(), strArgument); //$NON-NLS-1$
			// 분리자 만큼 실행한다.
			String[] strSArrayQLs = reqQuery.getSql().split(PublicTadpoleDefine.SQL_DELIMITER);
			for(int i=0; i<strSArrayQLs.length; i++) {
				String strRealSQL = SQLUtil.removeCommentAndOthers(userDB, strSArrayQLs[i]);
				if(StringUtils.trim(strRealSQL).equals("")) continue;
				intSQLCnt++;
				
				NamedParameterDAO dao = NamedParameterUtil.parseParameterUtils(userDB, strSArrayQLs[i], strArgument);
				if(ExecuteDMLCommand.RESULT_TYPE.JSON == user_RESULTTYPE) {
					if(i != 0) strExecuteResultData += ","; //$NON-NLS-1$
					strExecuteResultData += _getSelect(userDB, dao.getStrSQL(), user_RESULTTYPE, dao.getListParam(), isAddHead, strDelimiter);
				} else {
					strExecuteResultData += _getSelect(userDB, dao.getStrSQL(), user_RESULTTYPE, dao.getListParam(), isAddHead, strDelimiter);
				}
			}
			
			if(intSQLCnt > 1) {	
				if(ExecuteDMLCommand.RESULT_TYPE.JSON == user_RESULTTYPE) {
					strExecuteResultData = "[" + StringUtils.removeEnd(strExecuteResultData, ",") + "]";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
			
			reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.S.toString());
			return strExecuteResultData;
		} catch (Exception e) {
			logger.error("api exception", e); //$NON-NLS-1$
			throw e;
		} finally {
			reqResultDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
			reqResultDAO.setTdb_sql_head("/** api key : API Hub Manager */\r\n/**" + strArgument + "*/");
			reqResultDAO.setSql_text(strSQLs);
			reqResultDAO.setResultData(strExecuteResultData);
			
			try {
				TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(SessionManager.getUserSeq(), 
						userDB, 
						reqResultDAO
					);
			} catch(Exception e) {
				logger.error("save history", e);
			}
		}
	}

	/**
	 * called sql
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param listParam
	 * @param isAddHead
	 * @param strDelimiter
	 * @return
	 * @throws Exception
	 */
	private static String _getSelect(final UserDBDAO userDB, String strSQL, ExecuteDMLCommand.RESULT_TYPE user_RESULTTYPE, List<Object> listParam, boolean isAddHead, String strDelimiter) throws Exception {
		String strResult = ""; //$NON-NLS-1$
		
		if(SQLUtil.isStatement(strSQL)) {
			if(ExecuteDMLCommand.RESULT_TYPE.JSON == user_RESULTTYPE) {
				JsonArray jsonArry = ExecuteDMLCommand.selectToJson(userDB, strSQL, listParam);
				strResult = JSONUtil.getPretty(jsonArry.toString());
			} else if(ExecuteDMLCommand.RESULT_TYPE.CSV == user_RESULTTYPE) {
				strResult = ExecuteDMLCommand.selectToCSV(userDB, strSQL, listParam, isAddHead, strDelimiter);
			} else if(ExecuteDMLCommand.RESULT_TYPE.XML == user_RESULTTYPE) {
				strResult = ExecuteDMLCommand.selectToXML(userDB, strSQL, listParam);
			} else {
				strResult = ExecuteDMLCommand.selectToHTML_TABLE(userDB, strSQL, listParam);
			}
		} else {
			strResult = ExecuteDMLCommand.executeDML(userDB, strSQL, listParam, user_RESULTTYPE);
		}
		
		return strResult;
	}

}
