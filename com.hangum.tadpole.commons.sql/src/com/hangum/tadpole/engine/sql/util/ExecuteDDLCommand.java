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
package com.hangum.tadpole.engine.sql.util;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_STATUS;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.engine.sql.parser.ddl.ParserDDL;
import com.hangum.tadpole.engine.sql.parser.dto.QueryInfoDTO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * tadpole system에서 공통으로 사용하는 모듈
 * 
 * @author hangum
 *
 */
public class ExecuteDDLCommand {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExecuteDDLCommand.class);

	/**
	 * 쿼리중에 quote sql을 반영해서 작업합니다.
	 * 
	 * @param userDB
	 * @param sql
	 * @throws Exception
	 */
	public static RequestResultDAO executSQL(UserDBDAO userDB, String sql) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("\t ### "+ sql);
		RequestResultDAO reqResultDAO = new RequestResultDAO();
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(RWT.getRequest().getRemoteAddr());
		reqResultDAO.setStrSQLText(sql);
	
		try {
			QueryInfoDTO queryInfoDTO = new QueryInfoDTO();
			ParserDDL parser = new ParserDDL();
			parser.parseQuery(sql, queryInfoDTO);
			boolean bool = _executSQL(userDB, queryInfoDTO.getQueryStatus(), queryInfoDTO.getQueryDDLType(), queryInfoDTO.getObjectName(), sql);
			
			reqResultDAO.setDataChanged(bool);
		} catch(Exception e) {
			logger.error("execute sql", e);
			reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
			reqResultDAO.setMesssage(e.getMessage());
			reqResultDAO.setException(e);
			
			throw e;
		} finally {
			reqResultDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
		}
		
		return reqResultDAO;
	}
	
	/**
	 * 쿼리중에 quote sql을 반영해서 작업합니다.
	 * 
	 * @param userDB
	 * @param queryDDLStatus
	 * @param query_DDL_TYPE
	 * @param strSQL
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	private static boolean _executSQL(UserDBDAO userDB, QUERY_DDL_STATUS queryDDLStatus, QUERY_DDL_TYPE query_DDL_TYPE, String objName, String strSQL) throws TadpoleSQLManagerException, SQLException {
		if(queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.CREATE |
			queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.DROP |
			queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.ALTER
		) {
			java.sql.Connection javaConn = null;
			Statement stmt = null;
			try {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
				
				stmt = javaConn.createStatement();
				return stmt.execute(strSQL);
				
			} finally {
				TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), userDB,
						queryDDLStatus.name(), //$NON-NLS-1$
						query_DDL_TYPE.name(),
						objName,
						strSQL);
				
				try { if(stmt != null) stmt.close(); } catch(Exception e) {}
				try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
				
			}
		}
		
		return false;
	}
}
