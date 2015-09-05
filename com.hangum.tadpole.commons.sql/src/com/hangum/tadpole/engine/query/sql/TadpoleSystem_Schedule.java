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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.ScheduleDAO;
import com.hangum.tadpole.engine.query.dao.system.ScheduleDetailDAO;
import com.hangum.tadpole.engine.query.dao.system.ScheduleMainDAO;
import com.hangum.tadpole.engine.query.dao.system.ScheduleResultDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * schedule
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_Schedule {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_Schedule.class);
	
	/**
	 * delete schedule
	 * 
	 * @param seq
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void deleteSchedule(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("deleteScheduleMain", seq);
	}
	
	/**
	 * get result
	 * @param seq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ScheduleResultDAO> getScheduleResult(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getScheduleResult", seq);
	}
	
	/**
	 * schedule result
	 * 
	 * @param seq
	 * @param isResult
	 * @param msg
	 */
	public static void saveScheduleResult(int seq, boolean isResult, String msg) throws TadpoleSQLManagerException, SQLException {
		ScheduleResultDAO dao = new ScheduleResultDAO();
		dao.setSchedule_main_seq(seq);
		dao.setResult(isResult?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		dao.setDescription(msg);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("scheduleResultInsert", dao);
	}
	
	/**
	 * get all schedule 
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ScheduleMainDAO> findAllScheduleMain() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getAllScheduleMain");
	}
	
	/**
	 * find user schedule
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ScheduleMainDAO> findUserScheduleMain() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getUserScheduleMain", SessionManager.getUserSeq());
	}
	
	/**
	 * find schedule main
	 * 
	 * @param seq {@link ScheduleMainDAO#getSeq()}
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static ScheduleMainDAO findScheduleMain(final int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (ScheduleMainDAO)sqlClient.queryForObject("getScheduleMain", seq);
	}
	
	/**
	 * seq
	 * 
	 * @param seq {@link ScheduleDAO#getSeq()}
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ScheduleDAO> findSchedule(final int seq) throws TadpoleSQLManagerException, SQLException {
		List<ScheduleDAO> listSchedule = new ArrayList<ScheduleDAO>();
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		listSchedule = sqlClient.queryForList("getSchedule", seq);
		for (ScheduleDAO scheduleDAO : listSchedule) {
			
			List<ScheduleDetailDAO> listDetail = sqlClient.queryForList("getScheduleDetail", scheduleDAO.getSeq());
			StringBuffer sbSQL = new StringBuffer();
			
			for (ScheduleDetailDAO scheduleDetailDAO : listDetail) {
				sbSQL.append(scheduleDetailDAO.getDatas());
			}
			scheduleDAO.setSql(sbSQL.toString());
		}
		
		return listSchedule;
	}

	/**
	 * 
	 * @param userDB
	 * @param title
	 * @param desc
	 * @param cronExp
	 * @param listSchedule
	 */
	public static ScheduleMainDAO addSchedule(final UserDBDAO userDB, String title, String desc, String cronExp, List<ScheduleDAO> listSchedule) throws TadpoleSQLManagerException, SQLException {
		int userSeq = SessionManager.getUserSeq();
		
		ScheduleMainDAO dao = new ScheduleMainDAO();
		dao.setSeq(userSeq);
		dao.setUser_seq(SessionManager.getUserSeq());
		dao.setDb_seq(userDB.getSeq());
		dao.setTitle(title);
		dao.setDescription(desc);
		dao.setCron_exp(cronExp);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		dao = (ScheduleMainDAO)sqlClient.insert("scheduleMainInsert", dao);
		
		// save schedule
		for (ScheduleDAO scheduleDAO : listSchedule) {
			scheduleDAO.setSchedule_main_seq(dao.getSeq());
			ScheduleDAO retScheduleDAO = (ScheduleDAO)sqlClient.insert("scheduleInsert", scheduleDAO);
			
			// sql 
			String[] sqls = SQLUtil.makeResourceDataArays(scheduleDAO.getSql());
			ScheduleDetailDAO detailDao = null;
			for (String sql : sqls) {
				detailDao = new ScheduleDetailDAO();
				detailDao.setSchedule_seq(retScheduleDAO.getSeq());
				detailDao.setDatas(sql);
				
				sqlClient.insert("scheduleDetailInsert", detailDao);	
			}
		}
		
		return dao;
	}
	
	/**
	 * 데이터 수정.
	 * @param userDB
	 * @param title
	 * @param desc
	 * @param cronExp
	 * @param listSchedule
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static ScheduleMainDAO modifySchedule(final UserDBDAO userDB, ScheduleMainDAO scheduleDao, List<ScheduleDAO> listSchedule) throws TadpoleSQLManagerException, SQLException {
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("scheduleMainUpdate", scheduleDao);
		
		// 기존 데이터를 삭제합니다.
		sqlClient.update("deleteSchedule", scheduleDao.getSeq());
		
		// save schedule
		for (ScheduleDAO scheduleDAO : listSchedule) {
			scheduleDAO.setSchedule_main_seq(scheduleDao.getSeq());
			ScheduleDAO retScheduleDAO = (ScheduleDAO)sqlClient.insert("scheduleInsert", scheduleDAO);
			
			// sql 
			String[] sqls = SQLUtil.makeResourceDataArays(scheduleDAO.getSql());
			ScheduleDetailDAO detailDao = null;
			for (String sql : sqls) {
				detailDao = new ScheduleDetailDAO();
				detailDao.setSchedule_seq(retScheduleDAO.getSeq());
				detailDao.setDatas(sql);
				
				sqlClient.insert("scheduleDetailInsert", detailDao);	
			}
		}
		
		return scheduleDao;
	}
	
}
