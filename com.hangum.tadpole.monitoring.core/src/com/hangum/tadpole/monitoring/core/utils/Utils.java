package com.hangum.tadpole.monitoring.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.libs.core.mails.dto.EmailDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringIndexDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * monitoring utils
 * 
 * @author hangum
 *
 */
public class Utils {
	
	private static final Logger logger = Logger.getLogger(Utils.class);
	
	/**
	 * resultset to json
	 * 
	 * @param userDB
	 * @param indexDao
	 * @return
	 * @throws Exception
	 */
	public static JsonArray selectToJson(final UserDBDAO userDB, final MonitoringIndexDAO indexDao) throws Exception {
		List<Object> listParam = new ArrayList<>();
		if(StringUtils.isNotEmpty(indexDao.getParam_1_init_value())) listParam.add(indexDao.getParam_1_init_value());
		if(StringUtils.isNotEmpty(indexDao.getParam_2_init_value())) listParam.add(indexDao.getParam_2_init_value());
		
		return QueryUtils.selectToJson(userDB, indexDao.getQuery(), listParam);
	}
	
	/**
	 * get shapshot
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getSnapshot(final UserDBDAO userDB) throws Exception {
		return sqlToJson(userDB, "");
	}

	/**
	 * get db variable
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getDBVariable(final UserDBDAO userDB) {
		return sqlToJson(userDB, userDB.getDBDefine().getSystemVariableQuery());
	}
	
	/**
	 * SQL to json
	 * 
	 * @param strSQL
	 * @return
	 */
	public static String sqlToJson(final UserDBDAO userDB, String strSQL) {
		JsonObject jsonEntry = new JsonObject();
		
		try {
			JsonArray jsonArray = QueryUtils.selectToJson(userDB, strSQL);
			jsonEntry.add(strSQL, jsonArray);
		} catch (Exception e) {
			logger.error("sql to json error", e);
		}
		return jsonEntry.toString();
	}
	
	/**
	 * SQL to json
	 * 
	 * @param strSQL
	 * @return
	 */
	public static String sqlToJson(final UserDBDAO userDB, String[] strSQLs) {
		JsonObject jsonEntry = new JsonObject();
		
		for (String strSQL : strSQLs) {
			try {
				JsonArray jsonArray = QueryUtils.selectToJson(userDB, strSQL);
				jsonEntry.add(strSQL, jsonArray);
			} catch (Exception e) {
				logger.error("sql to json error", e);
			}
		}
		return jsonEntry.toString();
	}
	
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
			
			SendEmails sendEmail = new SendEmails(GetAdminPreference.getSessionSMTPINFO());
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
//			MessageDialog.openError(null, Messages.get().AddScheduleDialog_20, Messages.get().AddScheduleDialog_12);
//			textCronExp.setFocus();
//		}
	}
	
	private static String convPretty(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		return sdf.format(date);
	}

}
