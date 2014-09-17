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
package com.hangum.tadpole.sql.query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.SchemaHistoryDAO;
import com.hangum.tadpole.sql.dao.system.SchemaHistoryDetailDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.SQLUtil;
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
	public static List<SchemaHistoryDetailDAO> getExecuteQueryHistoryDetail(int seq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
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
	 * @throws Exception
	 */
	public static List<SchemaHistoryDAO> getExecuteQueryHistory(int dbSeq, String workType, String objectType, String objectId, long startTime, long endTime) throws Exception {
		List<SchemaHistoryDAO> returnSchemaHistory = new ArrayList<SchemaHistoryDAO>();
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("db_seq",		dbSeq);
		
		queryMap.put("workType", 	"%" + workType 		+ "%");
		queryMap.put("objectType", 	"%" + objectType	+ "%");
		queryMap.put("objectId", 	"%" + objectId 		+ "%");
		
		queryMap.put("startTime",  	startTime);
		queryMap.put("endTime", 	endTime);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		returnSchemaHistory =  sqlClient.queryForList("getSchemaHistory", queryMap);
		
		return returnSchemaHistory;
	}

	/**
	 * save schema_history 
	 * 
	 * @param user_seq
	 * @param userDB
	 * @param strSQL
	 */
	public static SchemaHistoryDAO save(int user_seq, UserDBDAO userDB, String strSQL) {
		SchemaHistoryDAO schemaDao = new SchemaHistoryDAO(); 
		
		try {
			//
			//
			//
			String strWorkSQL = strSQL.replaceAll("(\r\n|\n|\r)", ""); // 개행문자 제거.
			strWorkSQL = strWorkSQL.replaceAll("\\p{Space}", " ");	// 중간에 공백 제거.
			if(logger.isDebugEnabled()) logger.debug("[start sql]\t" + strWorkSQL);
			
			String[] arrSQL = StringUtils.split(strWorkSQL);
			String strWorkType = arrSQL[0];
			
			// object type
			String strObjecType = arrSQL[1];
			
			// objectId
			String strObjectId = StringUtils.remove(arrSQL[2], "(");
						
			if(StringUtils.equalsIgnoreCase("or", strObjecType)) {
				strObjecType = arrSQL[3];
				strObjectId = StringUtils.remove(arrSQL[4], "(");
			} 
			
			schemaDao = new SchemaHistoryDAO();
			schemaDao.setDb_seq(userDB.getSeq());
			schemaDao.setUser_seq(user_seq);
			
			schemaDao.setWork_type(strWorkType);
			schemaDao.setObject_type(strObjecType);
			schemaDao.setObject_id(strObjectId);
			
			schemaDao.setCreate_date(new Timestamp(System.currentTimeMillis()));
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
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
	 * @throws Exception
	 */
	private static void insertResourceData(SchemaHistoryDAO schemaHistoryDao, String strSQL) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		// content data를 저장합니다.
		SchemaHistoryDetailDAO dataDao = new SchemaHistoryDetailDAO();
		dataDao.setSchema_seq(schemaHistoryDao.getSeq());
		String[] arrayContent = SQLUtil.makeResourceDataArays(strSQL);
		for (String content : arrayContent) {
			dataDao.setSource(strSQL);		
			sqlClient.insert("sqlHistoryDataInsert", dataDao); //$NON-NLS-1$				
		}
	}
}
