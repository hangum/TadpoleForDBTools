package com.hangum.db.dialogs.message.dao;

import java.util.Date;

/**
 * system message dao
 * 
 * @author hangum
 *
 */
public class TadpoleMessageDAO  {
	
	/** 실행시간*/
	Date dateExecute;
	/** sql text */
	String strMessage;

	public TadpoleMessageDAO(Date dateExecute, String strMessage) {
		this.dateExecute = dateExecute;
		this.strMessage = strMessage;
	}

	public Date getDateExecute() {
		return dateExecute;
	}

	public void setDateExecute(Date dateExecute) {
		this.dateExecute = dateExecute;
	}

	public String getStrMessage() {
		return strMessage;
	}

	public void setStrMessage(String strMessage) {
		this.strMessage = strMessage;
	}
	
	
}
