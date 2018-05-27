/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;

/**
 * 쿼리 실행결과 저장 데이터
 * 
 * @author hangum
 *
 */
public class ExecutedSQLResultDataDAO {
	long seq;
	long user_seq;
	Timestamp startDateExecute;
	long executed_sql_resource_seq;
    String result_data = "";
    Timestamp ctreate_time;
    
	public ExecutedSQLResultDataDAO() {
	}

	/**
	 * @return the seq
	 */
	public long getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(long seq) {
		this.seq = seq;
	}
	
	/**
	 * @return the user_seq
	 */
	public long getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(long user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the startDateExecute
	 */
	public Timestamp getStartDateExecute() {
		return startDateExecute;
	}

	/**
	 * @param startDateExecute the startDateExecute to set
	 */
	public void setStartDateExecute(Timestamp startDateExecute) {
		this.startDateExecute = startDateExecute;
	}

	/**
	 * @return the executed_sql_resource_seq
	 */
	public long getExecuted_sql_resource_seq() {
		return executed_sql_resource_seq;
	}

	/**
	 * @param executed_sql_resource_seq the executed_sql_resource_seq to set
	 */
	public void setExecuted_sql_resource_seq(long executed_sql_resource_seq) {
		this.executed_sql_resource_seq = executed_sql_resource_seq;
	}

	/**
	 * @return the result_data
	 */
	public String getResult_data() {
		return result_data;
	}

	/**
	 * @param result_data the result_data to set
	 */
	public void setResult_data(String result_data) {
		this.result_data = result_data;
	}

	/**
	 * @return the ctreate_time
	 */
	public Timestamp getCtreate_time() {
		return ctreate_time;
	}

	/**
	 * @param ctreate_time the ctreate_time to set
	 */
	public void setCtreate_time(Timestamp ctreate_time) {
		this.ctreate_time = ctreate_time;
	}

	

}
