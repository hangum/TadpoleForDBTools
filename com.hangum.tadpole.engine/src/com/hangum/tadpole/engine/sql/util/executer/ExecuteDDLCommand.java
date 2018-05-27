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
package com.hangum.tadpole.engine.sql.util.executer;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.engine.sql.parser.ddl.ParserDDL;
import com.hangum.tadpole.engine.sql.parser.dto.QueryInfoDTO;
import com.hangum.tadpole.engine.sql.util.OracleDbmsOutputUtil;
import com.hangum.tadpole.engine.sql.util.SQLConvertCharUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.session.manager.SessionManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.QUERY_DDL_STATUS;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;

/**
 * tadpole system에서 공통으로 사용하는 DDL 모듈
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
	 * @param reqQuery
	 * @throws Exception
	 */
	public static RequestQuery executSQL(RequestQuery reqQuery) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("\t ### "+ reqQuery.getSql());
		
		RequestResultDAO reqResultDAO = new RequestResultDAO(); 
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(reqQuery.getUserIp());
		reqResultDAO.setSql_text(reqQuery.getSql());
	
		try {
			QueryInfoDTO queryInfoDTO = new QueryInfoDTO();
			ParserDDL parser = new ParserDDL();
			parser.parseQuery(reqQuery.getSql(), queryInfoDTO);
			Map<String, Object> resultMap = _executSQL(queryInfoDTO.getQueryStatus(), queryInfoDTO.getQueryDDLType(), queryInfoDTO.getObjectName(), reqQuery);
			
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
			reqQuery.setRequestResultDao(reqResultDAO);
			
			// 히스토리 정보 메타디비에 저장
			TadpoleSystem_ExecutedSQL.insertExecuteHistory(
					SessionManager.getUserSeq(), reqQuery.getUserDB(), 
						reqResultDAO,
						null
						);
		}
		
		return reqQuery;
	}
	
	/**
	 * 쿼리중에 quote sql을 반영해서 작업합니다.
	 * 
	 * @param userDB
	 * @param queryDDLStatus
	 * @param query_DDL_TYPE
	 * @param reqQuery
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	private static Map<String, Object> _executSQL(QUERY_DDL_STATUS queryDDLStatus, QUERY_DDL_TYPE query_DDL_TYPE, String objName, RequestQuery reqQuery) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.CREATE ||
			queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.DROP ||
			queryDDLStatus == PublicTadpoleDefine.QUERY_DDL_STATUS.ALTER
		) {
			
			java.sql.Connection javaConn = null;
			OracleDbmsOutputUtil dbmsOutput = null;
			Statement stmt = null;
			try {
				javaConn = TadpoleSQLManager.getConnection(reqQuery.getUserDB());
				
				stmt = javaConn.createStatement();
				
				if (DBGroupDefine.ORACLE_GROUP == reqQuery.getUserDB().getDBGroup()){
					dbmsOutput = new OracleDbmsOutputUtil( javaConn );
					dbmsOutput.enable( 1000000 ); 
					resultMap.put("result", stmt.execute(SQLConvertCharUtil.toServer(reqQuery.getUserDB(), reqQuery.getSql())));
					dbmsOutput.show();
					resultMap.put("dbms_output", dbmsOutput.getOutput());
				}else{
					resultMap.put("result", stmt.execute(SQLConvertCharUtil.toServer(reqQuery.getUserDB(), reqQuery.getSql())));
					resultMap.put("dbms_output", "");
				}
				
				// 성공시 변경 히스토리를 입력한다.
				TadpoleSystem_SchemaHistory.save(SessionManager.getUserSeq(), reqQuery.getUserDB(),
						queryDDLStatus.name(), //$NON-NLS-1$
						query_DDL_TYPE.name(),
						objName,
						reqQuery.getSql());
				
			} finally {
				
				try { if(stmt != null) stmt.close(); } catch(Exception e) {}
				try { if(dbmsOutput != null) dbmsOutput.close(); } catch(Exception e) {}
				try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
			}
		}
		return resultMap;
	}
}
