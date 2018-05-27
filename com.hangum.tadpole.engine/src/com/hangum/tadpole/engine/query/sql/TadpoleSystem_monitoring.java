package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleRuntimeException;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.commons.TadpoleSequenceDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringDashboardDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringMainDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.session.manager.SessionManager;
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
	 * 모니터링 현재 상태를 리턴합니다.
	 * 
	 * @param dbSeq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<MonitoringResultDAO> getMonitoringStatus(int dbSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getMonitoringStatus", dbSeq);
	}
	
	/**
	 * get monitoring error status
	 * 
	 * @param dbseqs
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<MonitoringDashboardDAO> getMonitoringErrorStatus(String dbseqs) throws TadpoleSQLManagerException, SQLException {
		if("".equals(dbseqs)) return new ArrayList<MonitoringDashboardDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getMonitoringErrorStatus", dbseqs);
	}
	
	/**
	 * get user monitoring db list
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserDBDAO> getUserMonitoringDBList() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getUserMonitoringDBList", SessionManager.getUserSeq());
	}
	
	/**
	 * update parameter
	 * 
	 * @param dao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateParameter(MonitoringIndexDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateParameter", dao);
	}
	
	/**
	 * monitoring list
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<MonitoringIndexDAO> getAllMonitoringList() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getAllMonitoringList");
	}
	
	/**
	 * Get DB monitoring data.
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<MonitoringIndexDAO> getUserMonitoringIndex(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getUserDBMonitoringIndex", userDB);
	}

	/**
	 * 모니터링 데이터를 저장합니다. 
	 * 
	 * @param userDB
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void saveMonitoring(MonitoringMainDAO mainDao, MonitoringIndexDAO indexDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());		
		// 기존에 동일한 type, name으로 항목이 등록 되어 있는지 검사합니다.
		List<MonitoringMainDAO> listMainList = sqlClient.queryForList("getMonitoringDuplicatCheck", mainDao);
		if(listMainList.isEmpty()) {
			mainDao = (MonitoringMainDAO)sqlClient.insert("insertMonitoringMain", mainDao);
			indexDao.setMonitoring_seq(mainDao.getSeq());
			sqlClient.insert("insertMonitoringIndex", indexDao);
		} else {
			throw new TadpoleRuntimeException("이미 동일한 항목이 존재합니다. 이름을 수정하여 주십시오. ");
		}
	}
	
	/**
	 * delete monitoring index
	 * 
	 * @param indexDao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void deleteMonitoringIndex(MonitoringIndexDAO indexDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
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
		
		TadpoleSequenceDAO dao = new TadpoleSequenceDAO();
		dao.setName(TadpoleSystem_Sequence.KEY_MONITORING);
		
		try {
			// unique id를 생성합니다. 
			dao = TadpoleSystem_Sequence.getSequence(dao);
			
			
			sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
//			sqlClient.startTransaction();
//			sqlClient.startBatch();
		
			for (MonitoringResultDAO resultDAO : listMonitoringIndex) {
				resultDAO.setRelation_id(dao.getNo());
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
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<MonitoringResultDAO> getMonitoringResultHistory(MonitoringIndexDAO monitoringIndexDao, 
											String strResultType, String strTerm, long startTime, long endTime) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("monitoring_seq",	monitoringIndexDao.getMonitoring_seq());
		queryMap.put("resultType", strResultType);	
		
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
		return sqlClient.queryForList("getMonitoringResultHistory", queryMap);
	}

	/**
	 * getMonitoring result status
	 * 
	 * @param intMonSeq
	 * @param intMonIndexSeq
	 * @param strUserConfirm
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<MonitoringResultDAO> getMonitoringResultStatus(int intMonSeq, int intMonIndexSeq, String is_user_confirm, String resultType) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("monitoring_seq",			intMonSeq);
		queryMap.put("monitoring_index_seq",	intMonIndexSeq);
		queryMap.put("is_user_confirm", 		is_user_confirm);
		queryMap.put("result", 					resultType);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getMonitoringResultStatus", queryMap);
	}

	/**
	 * monitoring result
	 * 
	 * @param dao
	 * @return
	 */
	public static MonitoringResultDAO getMonitoringResult(MonitoringResultDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return (MonitoringResultDAO)sqlClient.queryForObject("getMonitoringResult", dao);
	}

	
	/**
	 * 사용자 확인 처리를 합니다. 
	 * 
	 * @param monitoring_seq
	 * @param monitoring_index_seq
	 * @param strUserMsg
	 * 
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateUserConfirmMsg(int seq, String strUserMsg) throws TadpoleSQLManagerException, SQLException {
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("seq",			seq);
		queryMap.put("user_description",		strUserMsg);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateUserConfirmMsg", queryMap);
	}
	
	/**
	 * 모든 인덱스 에러에 대해 사용자 확인 처리 합니다.
	 * 
	 * @param monitoring_seq
	 * @param monitoring_index_seq
	 * @param strUserMsg
	 */
	public static void updateUserConfirmMsg(int monitoring_seq, int monitoring_index_seq, String strUserMsg) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("monitoring_seq",			monitoring_seq);
		queryMap.put("monitoring_index_seq",	monitoring_index_seq);
		queryMap.put("user_description",		strUserMsg);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateAllUserConfirmMsg", queryMap);
		
	}	

	/**
	 * update monitoring data
	 * 
	 * @param mainDao
	 * @param indexDao
	 */
	public static void updateMonitoring(MonitoringMainDAO mainDao, MonitoringIndexDAO indexDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateMonitoringMain", mainDao);
		sqlClient.update("updateMonitoringIndex", indexDao);
	}
}
