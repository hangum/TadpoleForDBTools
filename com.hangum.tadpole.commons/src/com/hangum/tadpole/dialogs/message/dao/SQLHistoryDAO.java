/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.dialogs.message.dao;

import java.util.Date;

/**
 * query history
 * 
 * @author hangum
 *
 */
public class SQLHistoryDAO {
	int seq;
	
	/** Execute start time */
	Date startDateExecute;
	
	/** execute sql text */
	String strSQLText;
	
	Date endDateExecute;
	int rows;
	String result;
	String messsage;
	
	public SQLHistoryDAO(Date dateExecute, String strSQLText, Date endDateExecute, int rows, String result, String message) {
		this.startDateExecute = dateExecute;
		this.strSQLText = strSQLText;		
		this.endDateExecute = endDateExecute;
		this.rows = rows;
		this.result = result;
		this.messsage = message;
	}

	public SQLHistoryDAO() {
	}


	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getStrSQLText() {
		return strSQLText;
	}

	public void setStrSQLText(String strSQLText) {
		this.strSQLText = strSQLText;
	}

	/**
	 * @return the startDateExecute
	 */
	public Date getStartDateExecute() {
		return startDateExecute;
	}

	/**
	 * @param startDateExecute the startDateExecute to set
	 */
	public void setStartDateExecute(Date startDateExecute) {
		this.startDateExecute = startDateExecute;
	}

	/**
	 * @return the endDateExecute
	 */
	public Date getEndDateExecute() {
		return endDateExecute;
	}

	/**
	 * @param endDateExecute the endDateExecute to set
	 */
	public void setEndDateExecute(Date endDateExecute) {
		this.endDateExecute = endDateExecute;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the messsage
	 */
	public String getMesssage() {
		return messsage;
	}

	/**
	 * @param messsage the messsage to set
	 */
	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}

	
}
