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
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_STATUS;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.engine.sql.parser.ddl.ParserDDL;
import com.hangum.tadpole.engine.sql.parser.dto.QueryInfoDTO;
import com.hangum.tadpole.session.manager.SessionManager;

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
	 * @param reqResultDAO
	 * @param sql
	 * @throws Exception
	 */
	public static RequestResultDAO executSQL(UserDBDAO userDB, RequestResultDAO reqResultDAO, String sql) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("\t ### "+ sql);
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(RWT.getRequest().getRemoteAddr());
		reqResultDAO.setStrSQLText(sql);
	
		try {
			QueryInfoDTO queryInfoDTO = new QueryInfoDTO();
			ParserDDL parser = new ParserDDL();
			parser.parseQuery(sql, queryInfoDTO);
			Map<String, Object> resultMap = _executSQL(userDB, queryInfoDTO.getQueryStatus(), queryInfoDTO.getQueryDDLType(), queryInfoDTO.getObjectName(), sql);
			
			reqResultDAO.setDataChanged((Boolean)resultMap.get("result"));
			reqResultDAO.setMesssage((String)resultMap.get("dbms_output"));
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
	private static Map<String, Object> _executSQL(UserDBDAO userDB, QUERY_DDL_STATUS queryDDLStatus, QUERY_DDL_TYPE query_DDL_TYPE, String objName, String strSQL) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.CREATE ||
			queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.DROP ||
			queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.ALTER
		) {
			java.sql.Connection javaConn = null;
			OracleDbmsOutputUtil dbmsOutput = null;
			Statement stmt = null;
			try {
				javaConn = TadpoleSQLManager.getConnection(userDB);
				
				stmt = javaConn.createStatement();
				
				if (DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()){
					dbmsOutput = new OracleDbmsOutputUtil( javaConn );
					dbmsOutput.enable( 1000000 ); 
					resultMap.put("result", stmt.execute(strSQL));
					dbmsOutput.show();
					resultMap.put("dbms_output", dbmsOutput.getOutput());
				}else{
					resultMap.put("result", stmt.execute(strSQL));
					resultMap.put("dbms_output", "");
				}
				
				
			} finally {
				TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), userDB,
						queryDDLStatus.name(), //$NON-NLS-1$
						query_DDL_TYPE.name(),
						objName,
						strSQL);
				
				try { if(stmt != null) stmt.close(); } catch(Exception e) {}
				try { if(dbmsOutput != null) dbmsOutput.close(); } catch(Exception e) {}
				try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
			}
		}
		return resultMap;
	}
}
