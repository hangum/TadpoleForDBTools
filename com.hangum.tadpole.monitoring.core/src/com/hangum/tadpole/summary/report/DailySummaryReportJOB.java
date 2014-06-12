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

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.mails.SendEmails;
import com.hangum.tadpold.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpold.commons.libs.core.mails.template.DailySummaryReport;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * daily summary report
 * 
 * @author hangum
 *
 */
public class DailySummaryReportJOB implements Job {
	private static final Logger logger = Logger.getLogger(DailySummaryReportJOB.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("daily summary report");
		
		try {
			// db 종류를 얻습니다.
			List<UserDBDAO> listUserDB = TadpoleSystem_UserDBQuery.getDailySummaryReportDB();
			
			// 리포팅 결과를 만듭니다.
			for (UserDBDAO userDBDAO : listUserDB) {
				StringBuffer strMailContent = new StringBuffer();
				
				
				// 현재(14.06.02)는 mysql 일 경우만 섬머리 레포트를 보여줍니다.
				if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDBDAO.getIs_summary_report())) {
					if(userDBDAO.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDBDAO.getDBDefine() == DBDefine.MARIADB_DEFAULT) {

						String strSummarySQl = MySQLSummaryReport.getSQL(userDBDAO.getDb());
						String[] arrySQLS = StringUtils.split(strSummarySQl, ";");
						for (String strSQL : arrySQLS) {
							String strTitle = StringUtils.split(StringUtils.trim(strSQL), PublicTadpoleDefine.LINE_SEPARATOR)[0];
						
							String strResult = executSQL(userDBDAO, strTitle, strSQL);
							strMailContent.append( strResult  );
						}
						
						DailySummaryReport report = new DailySummaryReport();
						String mailContent = report.makeFullSummaryReport(userDBDAO.getDisplay_name(), strMailContent.toString());

						// 보고서를 보냅니다.						
						sendEmail(userDBDAO, mailContent);
						
						if(logger.isDebugEnabled()) logger.debug(mailContent);
					} // end mysql db
				}	// if yes
			}	// for (UserDBDAO userDBDAO
			
		} catch (Exception e) {
			logger.error("daily summary report", e);
		}
	}
	
	/**
	 * 
	 * @param userType
	 * @param groupSeq
	 * @param groupName
	 * @param name
	 * @param email
	 */
	private void sendEmail(UserDBDAO userDB, String strContent) {
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.getUserInfo(userDB.getUser_seq());
			
			// manager 에게 메일을 보낸다.
			EmailDTO emailDao = new EmailDTO();
			emailDao.setSubject(userDB.getDisplay_name() + " Summary Report.");
			emailDao.setContent(strContent);
			emailDao.setTo(userDao.getEmail());
			
			SendEmails sendEmail = new SendEmails(GetPreferenceGeneral.getSMTPINFO());
			sendEmail.sendMail(emailDao);
		} catch(Exception e) {
			logger.error("Error send email", e);
		}
	}
	
	
	/**
	 * 쿼리중에 quote sql을 반영해서 작업합니다.
	 * 
	 * @param userDB
	 * @param strDML
	 * @param args
	 */
	public static String executSQL(UserDBDAO userDB, String strTitle, String strDML) throws Exception {
		java.sql.Connection javaConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			stmt = javaConn.createStatement();
			rs = stmt.executeQuery(strDML);
			
			return DailySummaryReport.makeResultSetTOHTML(strTitle, rs, 100);
		} finally {
			try { if(rs != null) rs.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close(); } catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
	}

}
