package com.hangum.tadpole.monitoring.core.manager.event;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * monitoring event manager
 * 
 * @author hangum
 *
 */
public class EventManager {
	private static final Logger logger = Logger.getLogger(EventManager.class);
	private static EventManager instance;

	private EventManager() {}
	
	public static EventManager getInstance() {
		if(instance == null) {
			instance = new EventManager();
		}
		
		return instance;
	}
	
	/**
	 * event proceed
	 * 
	 * @param listEvent
	 */
	public void proceedEvent(List<MonitoringResultDAO> listMonitoringResult) {
		final JsonParser parser = new JsonParser();
		for (MonitoringResultDAO resultDAO : listMonitoringResult) {
			final UserDBDAO userDB = resultDAO.getUserDB();
			String strAfterType = resultDAO.getMonitoringIndexDAO().getAfter_type();
		
			if(strAfterType.equals("EMAIL")) {
				sendEmail(resultDAO);
			} else if(strAfterType.equals("KILL_AFTER_EMAIL")) {
				sendEmail(resultDAO);
				
				JsonElement jsonElement = parser.parse(resultDAO.getQuery_result() + resultDAO.getQuery_result2());
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				String id = jsonObject.getAsJsonPrimitive("id").getAsString();
				
				try {
					SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
					if (userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
						client.queryForObject("killProcess", Integer.parseInt(id));
					} else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
						client.queryForObject("killProcess", id);
					}
				} catch(Exception e) {
					logger.error("kill session", e);
				}
			}	//  end if kill_after email
				
		}
	}
	
	/**
	 * send mail
	 * 
	 * @param resultDao
	 */
	private void sendEmail(MonitoringResultDAO resultDao) {
		if(logger.isDebugEnabled()) logger.debug(resultDao.getQuery_result() + resultDao.getQuery_result2());
		try {
			FileWriter fw  = new FileWriter("/Users/hangum/Downloads/mail.send.txt", true);
			fw.write(resultDao.getQuery_result() + resultDao.getQuery_result2() + PublicTadpoleDefine.LINE_SEPARATOR);
			fw.flush();
			
			fw.close();
		} catch (IOException e) {
			logger.error("Mail send", e);
		}
	}

}
