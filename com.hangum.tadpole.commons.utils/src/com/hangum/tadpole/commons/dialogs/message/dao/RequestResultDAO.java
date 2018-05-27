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

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * 실행 쿼리와 쿼리 결과를 포함하는 dao
 * 
 * @author hangum
 * 
 */
public class RequestResultDAO {
	
	long seq;
	
	/** Execute start time */
	Timestamp startDateExecute;
	
	/** 테드폴 코멘트 */
	String tdb_sql_head;

	/** execute sql text */
	String sql_text;

	Timestamp endDateExecute;
	int rows;
	boolean dataChanged = false;
	String result = PublicTadpoleDefine.SUCCESS_FAIL.S.toString();
	
	/** {@code com.hangum.tadpole.engine.define.TDBResultCodeDefine#NORMAL_SUCC} */
	int tdb_result_code = 200;
	String messsage = "";
	int duration;

	String userName;
	String dbName;
	int dbSeq;
	String ipAddress;
	
	String description;
	
	Exception exception;
	
	/** execute_sql_type */
	PublicTadpoleDefine.EXECUTE_SQL_TYPE EXECUSTE_SQL_TYPE = PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDITOR;
	
	String result_save_yn = PublicTadpoleDefine.YES_NO.NO.name();
	
	/** 쿼리 결과 데이터 저장 */
	String resultData = "";

	public RequestResultDAO(int duration, Timestamp dateExecute, String tdb_sql_head, String sql_text, Timestamp endDateExecute, int rows, String result, int tdb_result_code, String message) {
		this.duration = duration;
		this.userName = "";
		this.dbName = "";
		this.startDateExecute = dateExecute;
		this.tdb_sql_head = tdb_sql_head;
		this.sql_text = sql_text;
		this.endDateExecute = endDateExecute;
		this.rows = rows;
		this.result = result;
		this.tdb_result_code = tdb_result_code;
		this.messsage = message;
	}

	// Sql history for executedSqlEditor
	public RequestResultDAO(int duration, String userName, String dbName, Timestamp dateExecute, String tdb_sql_head, String sql_text, Timestamp endDateExecute, int rows, String result, 
			int tdb_result_code, String message,
			String ipAddress, int dbSeq, String strDescription) {
		this.duration = duration;
		this.userName = userName;
		this.dbName = dbName;
		this.ipAddress = ipAddress;
		this.dbSeq = dbSeq;
		this.startDateExecute = dateExecute;
		this.tdb_sql_head = tdb_sql_head;
		this.sql_text = sql_text;
		this.endDateExecute = endDateExecute;
		this.rows = rows;
		this.result = result;
		this.tdb_result_code = tdb_result_code;
		this.messsage = message;
		this.description = strDescription;
	}

	public RequestResultDAO() {
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

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	/**
	 * @return the tdb_sql_head
	 */
	public String getTdb_sql_head() {
		return tdb_sql_head;
	}

	/**
	 * @param tdb_sql_head the tdb_sql_head to set
	 */
	public void setTdb_sql_head(String tdb_sql_head) {
		this.tdb_sql_head = tdb_sql_head;
	}

	/**
	 * @return the sql_text
	 */
	public String getSql_text() {
		return sql_text;
	}

	/**
	 * @param sql_text the sql_text to set
	 */
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
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
	 * @return the tdb_result_code
	 */
	public int getTdb_result_code() {
		return tdb_result_code;
	}

	/**
	 * @param tdb_result_code the tdb_result_code to set
	 */
	public void setTdb_result_code(int tdb_result_code) {
		this.tdb_result_code = tdb_result_code;
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

	public PublicTadpoleDefine.EXECUTE_SQL_TYPE getEXECUSTE_SQL_TYPE() {
		return EXECUSTE_SQL_TYPE;
	}

	public void setEXECUSTE_SQL_TYPE(PublicTadpoleDefine.EXECUTE_SQL_TYPE eXECUSTE_SQL_TYPE) {
		EXECUSTE_SQL_TYPE = eXECUSTE_SQL_TYPE;
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * @return the dataChanged
	 */
	public boolean isDataChanged() {
		return dataChanged;
	}

	/**
	 * @param dataChanged the dataChanged to set
	 */
	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the result_save_yn
	 */
	public String getResult_save_yn() {
		return result_save_yn;
	}

	/**
	 * @param result_save_yn the result_save_yn to set
	 */
	public void setResult_save_yn(String result_save_yn) {
		this.result_save_yn = result_save_yn;
	}

	/**
	 * @return the resultData
	 */
	public String getResultData() {
		return resultData;
	}

	/**
	 * @param resultData the resultData to set
	 */
	public void setResultData(String resultData) {
		this.resultData = resultData;
	}
	
}
