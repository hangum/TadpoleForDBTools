package com.hangum.db.dialogs.message.dao;

import java.util.Date;

/**
 * query history
 * 
 * @author hangum
 *
 */
public class SQLHistoryDAO {
	/** 실행시간*/
	Date dateExecute;
	/** sql text */
	String strSQLText;
	
	public SQLHistoryDAO(Date dateExecute, String strSQLText) {
		this.dateExecute = dateExecute;
		this.strSQLText = strSQLText;		
	}

	public Date getDateExecute() {
		return dateExecute;
	}

	public void setDateExecute(Date dateExecute) {
		this.dateExecute = dateExecute;
	}

	public String getStrSQLText() {
		return strSQLText;
	}

	public void setStrSQLText(String strSQLText) {
		this.strSQLText = strSQLText;
	}
	
	
}
