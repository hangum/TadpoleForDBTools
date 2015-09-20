/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system.accesscontrol;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * 
 * db_access_control dao
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 26.
 *
 */
public class DBAccessControlDAO {
	int seq;
	int db_role_seq;
	String select_lock = PublicTadpoleDefine.YES_NO.YES.name();
	String insert_lock = PublicTadpoleDefine.YES_NO.NO.name();
	String update_lock = PublicTadpoleDefine.YES_NO.NO.name();
	String delete_locl = PublicTadpoleDefine.YES_NO.NO.name();
	String ddl_lock	 	= PublicTadpoleDefine.YES_NO.NO.name();
	Timestamp create_time;
	String delyn;
	
	/** access control detail count */
	int intDetailCnt = -1;
	
	/** select access ctl */
	Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
	
	/**
	 * 
	 */
	public DBAccessControlDAO() {
	}
	
	/**
	 * @return the mapSelectAccessCtl
	 */
	public Map<String, AccessCtlObjectDAO> getMapSelectAccessCtl() {
		return mapSelectAccessCtl;
	}

	/**
	 * @param mapSelectAccessCtl the mapSelectAccessCtl to set
	 */
	public void setMapSelectAccessCtl(Map<String, AccessCtlObjectDAO> mapSelectAccessCtl) {
		this.mapSelectAccessCtl = mapSelectAccessCtl;
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
	 * @return the db_role_seq
	 */
	public int getDb_role_seq() {
		return db_role_seq;
	}

	/**
	 * @param db_role_seq the db_role_seq to set
	 */
	public void setDb_role_seq(int db_role_seq) {
		this.db_role_seq = db_role_seq;
	}

	/**
	 * @return the select_lock
	 */
	public String getSelect_lock() {
		return select_lock;
	}

	/**
	 * @param select_lock the select_lock to set
	 */
	public void setSelect_lock(String select_lock) {
		this.select_lock = select_lock;
	}

	/**
	 * @return the insert_lock
	 */
	public String getInsert_lock() {
		return insert_lock;
	}

	/**
	 * @param insert_lock the insert_lock to set
	 */
	public void setInsert_lock(String insert_lock) {
		this.insert_lock = insert_lock;
	}

	/**
	 * @return the update_lock
	 */
	public String getUpdate_lock() {
		return update_lock;
	}

	/**
	 * @param update_lock the update_lock to set
	 */
	public void setUpdate_lock(String update_lock) {
		this.update_lock = update_lock;
	}

	/**
	 * @return the delete_locl
	 */
	public String getDelete_locl() {
		return delete_locl;
	}

	/**
	 * @param delete_locl the delete_locl to set
	 */
	public void setDelete_locl(String delete_locl) {
		this.delete_locl = delete_locl;
	}

	/**
	 * @return the ddl_lock
	 */
	public String getDdl_lock() {
		return ddl_lock;
	}

	/**
	 * @param ddl_lock the ddl_lock to set
	 */
	public void setDdl_lock(String ddl_lock) {
		this.ddl_lock = ddl_lock;
	}

	/**
	 * @return the create_time
	 */
	public Timestamp getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the delyn
	 */
	public String getDelyn() {
		return delyn;
	}

	/**
	 * @param delyn the delyn to set
	 */
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}

	public int getIntDetailCnt() {
		return intDetailCnt;
	}

	public void setIntDetailCnt(int intDetailCnt) {
		this.intDetailCnt = intDetailCnt;
	}
	
}
