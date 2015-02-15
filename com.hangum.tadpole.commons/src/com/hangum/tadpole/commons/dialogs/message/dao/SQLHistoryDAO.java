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
package com.hangum.tadpole.commons.dialogs.message.dao;

import java.sql.Timestamp;
import java.util.Date;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * 실행 쿼리와 쿼리 결과를 포함하는 dao
 * 
 * @author hangum
 * 
 */
public class SQLHistoryDAO {
	int seq;

	/** Execute start time */
	Timestamp startDateExecute;

	/** execute sql text */
	String strSQLText;

	Timestamp endDateExecute;
	int rows;
	String result = PublicTadpoleDefine.SUCCESS_FAIL.S.toString();
	String messsage;

	String userName;
	String dbName;
	int dbSeq;
	String ipAddress;
	

	public SQLHistoryDAO(Timestamp dateExecute, String strSQLText, Timestamp endDateExecute, int rows, String result, String message) {
		this.userName = "";
		this.dbName = "";
		this.startDateExecute = dateExecute;
		this.strSQLText = strSQLText;
		this.endDateExecute = endDateExecute;
		this.rows = rows;
		this.result = result;
		this.messsage = message;
	}

	// Sql history for executedSqlEditor
	public SQLHistoryDAO(String userName, String dbName, Timestamp dateExecute, String strSQLText, Timestamp endDateExecute, int rows, String result, String message,
			String ipAddress, int dbSeq) {
		this.userName = userName;
		this.dbName = dbName;
		this.ipAddress = ipAddress;
		this.dbSeq = dbSeq;
		this.startDateExecute = dateExecute;
		this.strSQLText = strSQLText;
		this.endDateExecute = endDateExecute;
		this.rows = rows;
		this.result = result;
		this.messsage = message;
	}

	public SQLHistoryDAO() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public int getDbSeq() {
		return dbSeq;
	}

	public void setDbSeq(int dbSeq) {
		this.dbSeq = dbSeq;
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
	public Timestamp getStartDateExecute() {
		return startDateExecute;
	}

	/**
	 * @param startDateExecute
	 *            the startDateExecute to set
	 */
	public void setStartDateExecute(Timestamp startDateExecute) {
		this.startDateExecute = startDateExecute;
	}

	/**
	 * @return the endDateExecute
	 */
	public Timestamp getEndDateExecute() {
		return endDateExecute;
	}

	/**
	 * @param endDateExecute
	 *            the endDateExecute to set
	 */
	public void setEndDateExecute(Timestamp endDateExecute) {
		this.endDateExecute = endDateExecute;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
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
	 * @param result
	 *            the result to set
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
	 * @param messsage
	 *            the messsage to set
	 */
	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}

}
