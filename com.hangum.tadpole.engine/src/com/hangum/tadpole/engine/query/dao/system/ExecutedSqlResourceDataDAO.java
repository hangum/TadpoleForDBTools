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
package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;

/**
 * 실행쿼리의 쿼리를 담는다.
 * 
 * @author hangum
 *
 */
public class ExecutedSqlResourceDataDAO {
	long seq;
	Timestamp startDateExecute;
	long executed_sql_resource_seq;
	/** 테드폴허브에서 추가하는 헤더 */
	String tdb_sql_head = "";
    String sql_data = "";
    Timestamp ctreate_time;
    
	public ExecutedSqlResourceDataDAO() {
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

	public Timestamp getStartDateExecute() {
		return startDateExecute;
	}

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
	 * @return the sql_data
	 */
	public String getSql_data() {
		return sql_data;
	}

	/**
	 * @param sql_data the sql_data to set
	 */
	public void setSql_data(String sql_data) {
		this.sql_data = sql_data;
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
