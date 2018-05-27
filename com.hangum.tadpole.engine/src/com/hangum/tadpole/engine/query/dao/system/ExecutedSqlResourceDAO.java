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
import java.util.Date;

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * 실행쿼리 상태 정보를 가지고 있는 DAO
 * 
 * @author hangum
 *
 */
public class ExecutedSqlResourceDAO {
	 long seq;
     int user_seq;
     int db_seq;

	 String types;
     Timestamp startDateExecute;
     Timestamp endDateExecute;
     int duration;
     int row;
     String result;

     /** 테드폴허브의 에러코드 정의 
      * {@code com.hangum.tadpole.engine.define#TadpoleCodeDefine} 
      */
     int tdb_result_code;
     String message;
     
     int accesscontrol_seq;

     String delYn;
     String ipAddress;
     
     // sql 결과 데이터 관리 항목
     String result_save_yn = PublicTadpoleDefine.YES_NO.NO.name();
     int result_del_user_seq;
     Timestamp result_del_time;
     String result_del_msg;
     
     Timestamp create_time;
     
	public ExecutedSqlResourceDAO() {
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the db_seq
	 */
	public int getDb_seq() {
		return db_seq;
	}

	/**
	 * @param db_seq the db_seq to set
	 */
	public void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
	}

	/**
	 * @return the types
	 */
	public String getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
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
	public void setStartDateExecute(Timestamp startDateExecute) {
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
	public void setEndDateExecute(Timestamp endDateExecute) {
		this.endDateExecute = endDateExecute;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the delYn
	 */
	public String getDelYn() {
		return delYn;
	}

	/**
	 * @param delYn the delYn to set
	 */
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
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
	 * @return the accesscontrol_seq
	 */
	public int getAccesscontrol_seq() {
		return accesscontrol_seq;
	}

	/**
	 * @param accesscontrol_seq the accesscontrol_seq to set
	 */
	public void setAccesscontrol_seq(int accesscontrol_seq) {
		this.accesscontrol_seq = accesscontrol_seq;
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
	 * @return the result_del_user_seq
	 */
	public int getResult_del_user_seq() {
		return result_del_user_seq;
	}

	/**
	 * @param result_del_user_seq the result_del_user_seq to set
	 */
	public void setResult_del_user_seq(int result_del_user_seq) {
		this.result_del_user_seq = result_del_user_seq;
	}

	/**
	 * @return the result_del_time
	 */
	public Timestamp getResult_del_time() {
		return result_del_time;
	}

	/**
	 * @param result_del_time the result_del_time to set
	 */
	public void setResult_del_time(Timestamp result_del_time) {
		this.result_del_time = result_del_time;
	}

	/**
	 * @return the result_del_msg
	 */
	public String getResult_del_msg() {
		return result_del_msg;
	}

	/**
	 * @param result_del_msg the result_del_msg to set
	 */
	public void setResult_del_msg(String result_del_msg) {
		this.result_del_msg = result_del_msg;
	}
	
}
