package com.hangum.tadpole.sql.query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringDashboardDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringMainDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * monitoring 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_monitoring {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_monitoring.class);
	
	/**
	 * get monitoring error status
	 * 
	 * @param dbseqs
	 * @return
	 * @throws Exception
	 */
	public static List<MonitoringDashboardDAO> getMonitoringErrorStatus(String dbseqs) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getMonitoringErrorStatus", dbseqs);
	}
	
	/**
	 * get user monitoring db list
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<UserDBDAO> getUserMonitoringDBList() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getUserMonitoringDBList", SessionManager.getUserSeq());
	}
	
	/**
	 * update parameter
	 * 
	 * @param dao
	 * @throws Exception
	 */
	public static void updateParameter(MonitoringIndexDAO dao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateParameter", dao);
	}
	
	/**
	 * monitoring list
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<MonitoringIndexDAO> getMonitoring() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getAllMonitoringList");
	}
	
	/**
	 * Get DB monitoring data.
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<MonitoringIndexDAO> getUserMonitoringIndex(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getUserDBMonitoringIndex", userDB);
	}

	/**
	 * 모니터링 데이터를 저장합니다. 
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public static void saveMonitoring(MonitoringMainDAO mainDao, MonitoringIndexDAO indexDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());		
		// 기존에 동일한 type, name으로 항목이 등록 되어 있는지 검사합니다.
		List<MonitoringMainDAO> listMainList = sqlClient.queryForList("getMonitoringDuplicatCheck", mainDao);
		if(listMainList.isEmpty()) {
			mainDao = (MonitoringMainDAO)sqlClient.insert("insertMonitoringMain", mainDao);
			indexDao.setMonitoring_seq(mainDao.getSeq());
			sqlClient.insert("insertMonitoringIndex", indexDao);
		} else {
			throw new Exception("이미 동일한 항목이 존재합니다. 이름을 수정하여 주십시오. ");
		}
	}
	
	/**
	 * delete monitoring index
	 * 
	 * @param indexDao
	 * @throws Exception
	 */
	public static void deleteMonitoringIndex(MonitoringIndexDAO indexDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("deleteMonitoringMain", indexDao);
		sqlClient.delete("deleteMonitoringIndex", indexDao);
	}

	/**
	 * MonitoringResult save
	 * 
	 * @param listMonitoringIndex
	 */
	public static void saveMonitoringResult(List<MonitoringResultDAO> listMonitoringIndex) {
		SqlMapClient sqlClient = null;
		
		try {
			sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//			sqlClient.startTransaction();
//			sqlClient.startBatch();
		
			for (MonitoringResultDAO resultDAO : listMonitoringIndex) {
				sqlClient.insert("insertMonitoringResult", resultDAO);
			}
//			sqlClient.executeBatch();
//			sqlClient.commitTransaction();
		} catch(Exception e) {
			logger.error("Monitoring result save exception", e);
		} finally {
			try {
				if(sqlClient != null) sqlClient.endTransaction();
			} catch(Exception e) {
				logger.error("saveMonitoring Result ", e);
			}
		}
	}
	
	/**
	 * get monitoring result
	 * 
	 * @param monitoringIndexDao
	 * @param strResultType
	 * @param strTerm
	 * @param startTime
	 * @param endTime
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<MonitoringResultDAO> getMonitoringResult(MonitoringIndexDAO monitoringIndexDao, 
											String strResultType, String strTerm, long startTime, long endTime) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("monitoring_seq",	monitoringIndexDao.getMonitoring_seq());
		
		if("Normal".equals(strResultType)) {
			queryMap.put("resultType", 	PublicTadpoleDefine.YES_NO.NO.toString());	
		} else if("Error".equals(strResultType)) {
			queryMap.put("resultType", 	PublicTadpoleDefine.YES_NO.YES.toString());
		}
		
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
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getMonitoringResult", queryMap);
	}
	
	/**
	 * 사용자 확인 처리를 합니다. 
	 * 
	 * @param monitoring_seq
	 * @param monitoring_index_seq
	 * @param strUserMsg
	 * 
	 * @throws Exception
	 */
	public static void updateUserConfirmMsg(int monitoring_seq, int monitoring_index_seq, String strUserMsg) throws Exception {
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("monitoring_seq",			monitoring_seq);
		queryMap.put("monitoring_index_seq",	monitoring_index_seq);
		queryMap.put("user_description",		strUserMsg);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateUserConfirmMsg", queryMap);
	}

	/**
	 * update monitoring data
	 * 
	 * @param mainDao
	 * @param indexDao
	 */
	public static void updateMonitoring(MonitoringMainDAO mainDao, MonitoringIndexDAO indexDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateMonitoringMain", mainDao);
		sqlClient.update("updateMonitoringIndex", indexDao);
	}
	
}
