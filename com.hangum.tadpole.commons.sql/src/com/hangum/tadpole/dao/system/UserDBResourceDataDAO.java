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
package com.hangum.tadpole.dao.system;

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
