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
package com.hangum.tadpole.monitoring.core.manager.schedule;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.ScheduleMainDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_Schedule;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.monitoring.core.jobs.UserJOB;
import com.hangum.tadpole.monitoring.core.jobs.monitoring.MonitoringJob;

/**
 * Schedule Manager
 * 
 * @author hangum
 *
 */
public class ScheduleManager {
	private static final Logger logger = Logger.getLogger(ScheduleManager.class);
	private static ScheduleManager manager = null;
	private static Scheduler scheduler = null;

	private ScheduleManager() {}
	
	public static ScheduleManager getInstance() {
		if(manager == null) {
			manager = new ScheduleManager();
			
			try {
				scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.start();
				
//				// 기 등록된 job들을 등록해줍니다.
//				List<ScheduleMainDAO> listSchedule = TadpoleSystem_Schedule.findAllScheduleMain();
//				for (ScheduleMainDAO scheduleMainDao : listSchedule) {
//					UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(scheduleMainDao.getDb_seq());
//					manager.newJob(userDB, scheduleMainDao);
//				}
				
				// Add monitoring job
				manager.newMonitoringJob();
				
			} catch (SchedulerException e) {
				logger.error("Schedule initialize exception", e);
			} catch(Exception e) {
				logger.error("find schedule", e);
			}
		}
		
		return manager;
	}
	
	/**
	 * add monitoring job
	 */
	public Date newMonitoringJob() throws Exception {
		Date firstJob = new Date(); 
				
		String strJobKey = "MainMonitoringJob";//makeJobkey(userDB, scheduleMainDao);
		
		JobDetail job = JobBuilder.newJob(MonitoringJob.class).withIdentity(strJobKey).build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))//*/10 * * * * ?"))
				.build();
		
		try {
			firstJob = scheduler.scheduleJob(job, trigger);
		} catch(Exception e) {
			logger.error("execute summary reporter", e);
			
			throw e;
		}
		
		return firstJob;
	}
	
	/**
	 * remove job
	 * 
	 * @param userDB
	 * @param scheduleMainDao
	 * @return
	 */
	public boolean deleteJob(final UserDBDAO userDB, final ScheduleMainDAO scheduleMainDao) {
		boolean isRemove = true;
		String strKey = makeJobkey(userDB, scheduleMainDao);
		
		try {
			manager.scheduler.deleteJob(new JobKey(strKey));
		} catch (SchedulerException e) {
			logger.error("delete job", e);
			isRemove = false;
		}
		return isRemove;
	}

	/**
	 * find job
	 * 
	 * @param strJobName
	 * @return
	 */
	public boolean findJob(final UserDBDAO userDB, final ScheduleMainDAO scheduleMainDao) {
		String strJobKey = makeJobkey(userDB, scheduleMainDao);
		
		try {
			for (String groupName : scheduler.getJobGroupNames()) {
				for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					
					if(jobName.equals(strJobKey)) return true;
				}
			}
		} catch(Exception e) {
			logger.error("find job", e);
		}
		
		return false;
	}
	
	/**
	 * add job
	 *  
	 * @param userDB
	 * @param scheduleMainDao
	 */
	public Date newJob(final UserDBDAO userDB, final ScheduleMainDAO scheduleMainDao) throws Exception {
		Date firstJob = new Date(); 
				
		logger.info("## New Job: db info " + userDB.getSeq()+"("+userDB.getDisplay_name() + "), job name is " + scheduleMainDao.getTitle() + "{" + scheduleMainDao.getCron_exp() + "}");
		String strJobKey = makeJobkey(userDB, scheduleMainDao);
		
		JobDetail job = JobBuilder.newJob(UserJOB.class).withIdentity(strJobKey).build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(scheduleMainDao.getCron_exp()))
				.build();
		
		try {
			firstJob = scheduler.scheduleJob(job, trigger);
		} catch(Exception e) {
			logger.error("execute summary reporter", e);
			
			throw e;
		}
		
		return firstJob;
	}
	
	/**
	 * make job key
	 * @param userDB
	 * @param scheduleMainDao
	 * @return
	 */
	private String makeJobkey(final UserDBDAO userDB, final ScheduleMainDAO scheduleMainDao) {
		return userDB.getSeq() + PublicTadpoleDefine.DELIMITER + scheduleMainDao.getSeq() + PublicTadpoleDefine.DELIMITER + userDB.getDisplay_name() + scheduleMainDao.getTitle();
	}

	public void stopSchedule() {
		if(scheduler != null) {
			try {
				scheduler.shutdown(true);
				
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.error("Shutdown Schedule", e);
			}
		}
	}
}
