/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.dao.system;

/** 
 * user db resource data
 * 
 * @author hangum
 *
 */
public class UserDBResourceDataDAO {
	int seq;
	int user_db_resource_seq;
	String datas;
	
	public UserDBResourceDataDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getUser_db_resource_seq() {
		return user_db_resource_seq;
	}

	public void setUser_db_resource_seq(int user_db_resource_seq) {
		this.user_db_resource_seq = user_db_resource_seq;
	}

	public String getDatas() {
		return datas;
	}

	public void setDatas(String datas) {
		this.datas = datas;
	}

	
}
