package com.hangum.tadpole.monitoring.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.mails.SendEmails;
import com.hangum.tadpold.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;

/**
 * cron exp utils
 * 
 * @author hangum
 *
 */
public class Utils {
	
	private static final Logger logger = Logger.getLogger(Utils.class);
	
	/**
	 * email send
	 * 
	 * @param userSeq
	 * @param title
	 * @param strContent
	 * @throws Exception
	 */
	public static void sendEmail(int userSeq, String title, String strContent) throws Exception {
		UserDAO userDao = TadpoleSystem_UserQuery.getUserInfo(userSeq);
		sendEmail(userDao.getEmail(), title, strContent);
	}
	
	/**
	 * 
	 * @param receivers 
	 * @param title
	 * @param strContent
	 */
	public static void sendEmail(String receivers, String title, String strContent) throws Exception {
		try {
			EmailDTO emailDao = new EmailDTO();
			emailDao.setSubject(title + " waraing message.");
			emailDao.setContent(strContent);
			emailDao.setTo(receivers);
			
			SendEmails sendEmail = new SendEmails(GetPreferenceGeneral.getSMTPINFO());
			sendEmail.sendMail(emailDao);
		} catch(Exception e) {
			logger.error("Error send email", e);
			throw e;
		}
	}

	/**
	 * show cron expression
	 */
	public static String showExp(String strExp) throws ParseException {
		StringBuffer sbStr = new StringBuffer();
//		try {
			CronExpression exp = new CronExpression(strExp);
			java.util.Date showDate = new java.util.Date();
//			sbStr.append(showDate.toString() + PublicTadpoleDefine.LINE_SEPARATOR);
	        
	        for (int i=0; i<=5; i++) {
	          showDate = exp.getNextValidTimeAfter(showDate);
	          sbStr.append(convPretty(showDate) + PublicTadpoleDefine.LINE_SEPARATOR);
	          showDate = new java.util.Date(showDate.getTime() + 1000);
	        }
	        
	        return sbStr.toString();
//		} catch (ParseException e) {
//			MessageDialog.openError(null, Messages.AddScheduleDialog_20, Messages.AddScheduleDialog_12);
//			textCronExp.setFocus();
//		}
	}
	
	private static String convPretty(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		return sdf.format(date);
	}

}
