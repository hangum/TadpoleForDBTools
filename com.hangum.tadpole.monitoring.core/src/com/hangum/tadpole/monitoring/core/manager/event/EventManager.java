package com.hangum.tadpole.monitoring.core.manager.event;

import java.io.FileWriter;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;
import com.hangum.tadpole.monitoring.core.utils.MonitoringDefine.AFTER_PROCESS_TYPE;
import com.hangum.tadpole.monitoring.core.utils.Utils;
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
		
			if("EMAIL".equals(strAfterType)) {
				sendEmail(resultDAO);
			} else if(AFTER_PROCESS_TYPE.PUSH_EMAIL.equals(strAfterType)) {
				sendEmail(resultDAO);
			} else if("KILL_AFTER_EMAIL".equals(strAfterType)) {
				sendEmail(resultDAO);
				
				JsonElement jsonElement = parser.parse(resultDAO.getQuery_result());
				if(jsonElement == null) {
					logger.info("Not found to SQL Result. Result index is " + resultDAO.getSeq());
					continue;
				} else {
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					String id = "";
					if(DBDefine.MYSQL_DEFAULT == resultDAO.getUserDB().getDBDefine() || DBDefine.MARIADB_DEFAULT == resultDAO.getUserDB().getDBDefine()) {
						id = jsonObject.getAsJsonPrimitive("id").getAsString();
					} else if(DBDefine.POSTGRE_DEFAULT == resultDAO.getUserDB().getDBDefine()) {
						id = jsonObject.getAsJsonPrimitive("pid").getAsString();
					} else {
						logger.error("Do not support Session kill.");
						continue;
					}
					
					try {
						SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
						if (userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
							client.queryForObject("killProcess", Integer.parseInt(id));
						} else {
							client.queryForObject("killProcess", id);
						}
					} catch(Exception e) {
						logger.error("=[monitoring kill session][start]==============================================================================");
						logger.error(resultDAO.getCreate_time() + "\t[db seq]" + resultDAO.getUserDB().getSeq() + "\t [Json msg]" + jsonElement.toString());
						logger.error("kill session", e);
						logger.error("=[monitoring kill session][end]==============================================================================");
					}
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
		if(logger.isDebugEnabled()) logger.debug(resultDao.getQuery_result());
		try {
			String strMailTitle = resultDao.getUserDB().getDisplay_name() + " - " + resultDao.getMonitoringIndexDAO().getTitle();
			String strMailContent = strMailTitle + "\n" + resultDao.getSystem_description() + "\n" + resultDao.getQuery_result();

			// TODO 이메일 보내기를 막아놓고 임시조취 
			//			Utils.sendEmail(resultDao.getMonitoringIndexDAO().getReceiver(), strMailTitle, strMailContent);
			FileWriter fw = new FileWriter("/Users/hangum/Downloads/mail.txt", true);
			fw.write(strMailTitle); 
			fw.flush();
			fw.close();
		} catch (Exception e) {
			logger.error("Mail send", e);
		}
	}

}
