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
package com.hangum.tadpole.summary.report;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * DB Summary reporter
 * 
 * @author hangum
 *
 */
public class DBSummaryReporter {
	private static final Logger logger = Logger.getLogger(DBSummaryReporter.class);
	
	public static String DB_SUMMARY_REPORT = "DB_SUMMARY_REPORT";
	public static String DAILY_JOB = "DALY_JOB";
	
	public static String TRIGGER_DB_SUMMARY_REPORT = "TRIGGER_DB_SUMMARY_REPORT";
	public static String TRIGGER_DALY_JOB = "TRIGGER_DALY_JOB";
	
	/**
	 * Execute job
	 * 
	 */
	public static void executer() {
		
		// find job
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			for (String groupName : scheduler.getJobGroupNames()) {
				for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					
					if(jobName.equals(DAILY_JOB)) return;
				}
			}
		} catch(Exception e) {
			logger.error("find summary reporter", e);
		}
		
		// execute job
		logger.info("start summary reporter");
		JobDetail job = JobBuilder.newJob(DailySummaryReportJOB.class).withIdentity(DAILY_JOB, DB_SUMMARY_REPORT).build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(TRIGGER_DALY_JOB, TRIGGER_DB_SUMMARY_REPORT)
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 6 * * ?"))
				.build();
		
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch(Exception e) {
			logger.error("execute summary reporter", e);
		}
	}
}
