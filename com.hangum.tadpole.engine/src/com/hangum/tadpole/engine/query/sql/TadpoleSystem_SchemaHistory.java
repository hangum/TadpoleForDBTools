/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.utils.LicenseValidator;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.SchemaHistoryDAO;
import com.hangum.tadpole.engine.query.dao.system.SchemaHistoryDetailDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * schema_history 관련 테이블 쿼리.
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_SchemaHistory {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystem_SchemaHistory.class);
	
	/**
	 * get schema history detail data
	 * @param seq
	 */
	public static List<SchemaHistoryDetailDAO> getExecuteQueryHistoryDetail(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getSchemaHistoryDetail", seq);
	}
	
	/**
	 * Get schema history data.
	 * 
	 * @param dbSeq
	 * @param workType
	 * @param objectType
	 * @param objectId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<SchemaHistoryDAO> getExecuteQueryHistory(int dbSeq, String workType, String objectType, String objectId, long startTime, long endTime) throws TadpoleSQLManagerException, SQLException {
		List<SchemaHistoryDAO> returnSchemaHistory = new ArrayList<SchemaHistoryDAO>();
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("db_seq",		dbSeq);
		
		queryMap.put("workType", 	"%" + workType 		+ "%");
		queryMap.put("objectType", 	"%" + objectType	+ "%");
		queryMap.put("objectId", 	"%" + objectId 		+ "%");
		
		if(ApplicationArgumentUtils.isDBServer()) {
			Date date = new Date(startTime);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			queryMap.put("startTime",  formatter.format(date));
			
			Date dateendTime = new Date(endTime);
			queryMap.put("endTime", formatter.format(dateendTime));			
		} else {
			queryMap.put("startTime",  	startTime);
			queryMap.put("endTime", 	endTime);
		}
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		returnSchemaHistory =  sqlClient.queryForList("getSchemaHistory", queryMap);
		
		return returnSchemaHistory;
	}

	/**
	 * save schema_history 
	 * 
	 * @param user_seq
	 * @param userDB
	 * @param strWorkType TABLE, VIEW, PROCEDURE, FUNCTION, TRIGGER...
	 * @param strObjecType CREATE, ALTER, DROP
	 * @param strObjectId 객체 명
	 * @param strSQL
	 */
	public static SchemaHistoryDAO save(int user_seq, UserDBDAO userDB, 
										String strWorkType, String strObjecType, String strObjectId, String strSQL) {
		SchemaHistoryDAO schemaDao = new SchemaHistoryDAO(); 
		if(!LicenseValidator.getLicense().isValidate()) {
			return schemaDao;
		}
		
		try {
			schemaDao = new SchemaHistoryDAO();
			schemaDao.setDb_seq(userDB.getSeq());
			schemaDao.setUser_seq(user_seq);
			
			schemaDao.setWork_type(strWorkType);
			schemaDao.setObject_type(strObjecType);
			schemaDao.setObject_id(strObjectId);
			
			schemaDao.setCreate_date(new Timestamp(System.currentTimeMillis()));
			
			schemaDao.setIpaddress(RWT.getRequest().getRemoteAddr());
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
			SchemaHistoryDAO schemaHistoryDao =  (SchemaHistoryDAO)sqlClient.insert("sqlHistoryInsert", schemaDao); //$NON-NLS-1$
			
			insertResourceData(schemaHistoryDao, strSQL);
		} catch(Exception e) {
			logger.error("Schema histor save error", e);
		}
		
		return schemaDao;
	}
	
	/**
	 * resource data 
	 * 
	 * @param schemaHistoryDao
	 * @param strSQL
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	private static void insertResourceData(SchemaHistoryDAO schemaHistoryDao, String strSQL) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		
		// content data를 저장합니다.
		SchemaHistoryDetailDAO dataDao = new SchemaHistoryDetailDAO();
		dataDao.setSchema_seq(schemaHistoryDao.getSeq());
		dataDao.setSource(strSQL);		
		sqlClient.insert("sqlHistoryDataInsert", dataDao); //$NON-NLS-1$				
	}
}
