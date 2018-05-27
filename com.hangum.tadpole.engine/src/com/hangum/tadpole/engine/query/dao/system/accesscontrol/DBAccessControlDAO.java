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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * 
 * db_access_control dao
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 26.
 *
 */
/**
 * @author hangum
 *
 */
public class DBAccessControlDAO {
	int seq;
	int db_role_seq;
	String schema_lock = PublicTadpoleDefine.YES_NO.YES.name();
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
	List<AccessCtlObjectDAO> allAccessCtl = new ArrayList<AccessCtlObjectDAO>();
		/** 
		 * table or view access ctl
		 * 
		 *  key pattern is schema.object name
		 *  if doesnot schema than just object name.
		 */
		Map<String, AccessCtlObjectDAO> mapSelectTableAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
		/** function access control */
		Map<String, AccessCtlObjectDAO> mapSelectFunctionAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
		/** procedure access control */
		Map<String, AccessCtlObjectDAO> mapSelectProcedureAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
	
	/**
	 * 
	 */
	public DBAccessControlDAO() {
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
	 * @return the schema_lock
	 */
	public String getSchema_lock() {
		return schema_lock;
	}

	/**
	 * @param schema_lock the schema_lock to set
	 */
	public void setSchema_lock(String schema_lock) {
		this.schema_lock = schema_lock;
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

	/**
	 * @return the intDetailCnt
	 */
	public int getIntDetailCnt() {
		return intDetailCnt;
	}

	/**
	 * @param intDetailCnt the intDetailCnt to set
	 */
	public void setIntDetailCnt(int intDetailCnt) {
		this.intDetailCnt = intDetailCnt;
	}

	/**
	 * @return the allAccessCtl
	 */
	public List<AccessCtlObjectDAO> getAllAccessCtl() {
		return allAccessCtl;
	}

	/**
	 * @param allAccessCtl the allAccessCtl to set
	 */
	public void setAllAccessCtl(List<AccessCtlObjectDAO> allAccessCtl) {
		this.allAccessCtl = allAccessCtl;
	}

	/**
	 * @return the mapSelectTableAccessCtl
	 */
	public Map<String, AccessCtlObjectDAO> getMapSelectTableAccessCtl() {
		return mapSelectTableAccessCtl;
	}

	/**
	 * @param mapSelectTableAccessCtl the mapSelectTableAccessCtl to set
	 */
	public void setMapSelectTableAccessCtl(Map<String, AccessCtlObjectDAO> mapSelectTableAccessCtl) {
		this.mapSelectTableAccessCtl = mapSelectTableAccessCtl;
	}

	/**
	 * @return the mapSelectFunctionAccessCtl
	 */
	public Map<String, AccessCtlObjectDAO> getMapSelectFunctionAccessCtl() {
		return mapSelectFunctionAccessCtl;
	}

	/**
	 * @param mapSelectFunctionAccessCtl the mapSelectFunctionAccessCtl to set
	 */
	public void setMapSelectFunctionAccessCtl(Map<String, AccessCtlObjectDAO> mapSelectFunctionAccessCtl) {
		this.mapSelectFunctionAccessCtl = mapSelectFunctionAccessCtl;
	}

	/**
	 * @return the mapSelectProcedureAccessCtl
	 */
	public Map<String, AccessCtlObjectDAO> getMapSelectProcedureAccessCtl() {
		return mapSelectProcedureAccessCtl;
	}

	/**
	 * @param mapSelectProcedureAccessCtl the mapSelectProcedureAccessCtl to set
	 */
	public void setMapSelectProcedureAccessCtl(Map<String, AccessCtlObjectDAO> mapSelectProcedureAccessCtl) {
		this.mapSelectProcedureAccessCtl = mapSelectProcedureAccessCtl;
	}
	
	
}
