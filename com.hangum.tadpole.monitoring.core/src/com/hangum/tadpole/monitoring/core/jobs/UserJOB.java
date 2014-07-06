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
package com.hangum.tadpole.monitoring.core.jobs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.mails.template.DailySummaryReport;
import com.hangum.tadpole.sql.dao.system.ScheduleDAO;
import com.hangum.tadpole.sql.dao.system.ScheduleMainDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_Schedule;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.summary.report.DailySummaryReportJOB;

/**
 * User job
 * 
 * @author hangum
 *
 */
public class UserJOB implements Job {
	private static final Logger logger = Logger.getLogger(UserJOB.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		StringBuffer sbMailContent = new StringBuffer();
		
		String strKey = context.getJobDetail().getKey().toString();
		logger.debug("start job is " + strKey);
		String[] keys = StringUtils.split( StringUtils.removeStart(strKey, "DEFAULT."), PublicTadpoleDefine.DELIMITER);
		
		int dbSeq = NumberUtils.createInteger(keys[0]);
		int scheduleSeq = NumberUtils.createInteger(keys[1]);
		
		try {
			UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(dbSeq);
			ScheduleMainDAO scheduleMainDao = TadpoleSystem_Schedule.findScheduleMain(scheduleSeq);
			List<ScheduleDAO> listSchedule = TadpoleSystem_Schedule.findSchedule(scheduleMainDao.getSeq());
			
			for (ScheduleDAO scheduleDAO : listSchedule) {
				try {
					sbMailContent.append( DailySummaryReportJOB.executSQL(userDB, scheduleDAO.getName(), scheduleDAO.getSql()) );
				} catch(Exception e) {
					sbMailContent.append( "Rise Exception :\n" + e.getMessage() );
				}
			}
//			
			DailySummaryReport report = new DailySummaryReport();
			String mailContent = report.makeFullSummaryReport(scheduleMainDao.getTitle(), sbMailContent.toString());
			
			DailySummaryReportJOB.sendEmail(scheduleMainDao.getTitle(), scheduleMainDao.getUser_seq(), mailContent);;
			
			TadpoleSystem_Schedule.saveScheduleResult(scheduleSeq, true, "");
		} catch (Exception e) {
			logger.error("execute User Job", e);
			
			try {
				TadpoleSystem_Schedule.saveScheduleResult(scheduleSeq, false, e.getMessage());
			} catch (Exception e1) {
				logger.error("save schedule result", e1);
			}
		}
		
	}
}
