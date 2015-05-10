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

/**
 * 실행쿼리의 쿼리를 담는다.
 * 
 * @author hangum
 *
 */
public class ExecutedSqlResourceDataDAO {
	int seq;
    int executed_sql_resource_seq;
    String datas;
	
	public ExecutedSqlResourceDataDAO() {
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the executed_sql_resource_seq
	 */
	public int getExecuted_sql_resource_seq() {
		return executed_sql_resource_seq;
	}

	/**
	 * @param executed_sql_resource_seq the executed_sql_resource_seq to set
	 */
	public void setExecuted_sql_resource_seq(int executed_sql_resource_seq) {
		this.executed_sql_resource_seq = executed_sql_resource_seq;
	}

	/**
	 * @return the datas
	 */
	public String getDatas() {
		return datas;
	}

	/**
	 * @param datas the datas to set
	 */
	public void setDatas(String datas) {
		this.datas = datas;
	}
	
}
