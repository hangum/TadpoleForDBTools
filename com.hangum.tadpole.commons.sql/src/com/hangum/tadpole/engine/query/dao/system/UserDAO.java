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

import com.hangum.tadpole.commons.csv.DateUtil;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * user 정보 정의
 * 
 * @author hangum
 * 
 */
public class UserDAO {
	int seq;
	/** 
	 * {@code PublicTadpoleDefine#INPUT_TYPE }
	 */
	String input_type;
	
	String email;
	String email_key;
	String is_email_certification;

	String passwd;
	String role_type;
	String name;
	String language;
	String timezone;
	String delYn;
	String create_time;
	String approval_yn;
	
	String use_otp;
	String otp_secret;
	
	String allow_ip = "*";

	/** 디비등록 가능 여부 */
	String is_regist_db = PublicTadpoleDefine.YES_NO.YES.name();
	/** 공유 할수 있는지 여부 */
	String is_shared_db = PublicTadpoleDefine.YES_NO.YES.name();
	/** 디비 추가 할 수있는 한계 */
	int limit_add_db_cnt = 5;
	
	Timestamp service_start = new Timestamp(System.currentTimeMillis());
	Timestamp service_end = new Timestamp(DateUtil.afterMonthToMillis(30));
	
	//------------------------------------------------------------------------
	/** tadpole_user_db_role column */
	String role_id;

	public UserDAO() {
	}

	/**
	 * @return the input_type
	 */
	public String getInput_type() {
		return input_type;
	}

	/**
	 * @param input_type the input_type to set
	 */
	public void setInput_type(String input_type) {
		this.input_type = input_type;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the approval_yn
	 */
	public String getApproval_yn() {
		return approval_yn;
	}

	/**
	 * @param approval_yn
	 *            the approval_yn to set
	 */
	public void setApproval_yn(String approval_yn) {
		this.approval_yn = approval_yn;
	}

	public String getUse_otp() {
		return use_otp;
	}

	public void setUse_otp(String use_otp) {
		this.use_otp = use_otp;
	}

	public String getOtp_secret() {
		return otp_secret;
	}

	public void setOtp_secret(String otp_secret) {
		this.otp_secret = otp_secret;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getEmail_key() {
		return email_key;
	}

	public void setEmail_key(String email_key) {
		this.email_key = email_key;
	}

	public String getRole_type() {
		return role_type;
	}

	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}
	
	public String getAllow_ip() {
		return allow_ip;
	}

	public void setAllow_ip(String allow_ip) {
		this.allow_ip = allow_ip;
	}

	/**
	 * @return the is_email_certification
	 */
	public String getIs_email_certification() {
		return is_email_certification;
	}

	/**
	 * @param is_email_certification the is_email_certification to set
	 */
	public void setIs_email_certification(String is_email_certification) {
		this.is_email_certification = is_email_certification;
	}

	/**
	 * @return the is_regist_db
	 */
	public String getIs_regist_db() {
		return is_regist_db;
	}

	/**
	 * @param is_regist_db the is_regist_db to set
	 */
	public void setIs_regist_db(String is_regist_db) {
		this.is_regist_db = is_regist_db;
	}

	/**
	 * @return the is_shared_db
	 */
	public String getIs_shared_db() {
		return is_shared_db;
	}

	/**
	 * @param is_shared_db the is_shared_db to set
	 */
	public void setIs_shared_db(String is_shared_db) {
		this.is_shared_db = is_shared_db;
	}

	/**
	 * @return the limit_add_db_cnt
	 */
	public int getLimit_add_db_cnt() {
		return limit_add_db_cnt;
	}

	/**
	 * @param limit_add_db_cnt the limit_add_db_cnt to set
	 */
	public void setLimit_add_db_cnt(int limit_add_db_cnt) {
		this.limit_add_db_cnt = limit_add_db_cnt;
	}

	/**
	 * @return the service_start
	 */
	public Timestamp getService_start() {
		return service_start;
	}

	/**
	 * @param service_start the service_start to set
	 */
	public void setService_start(Timestamp service_start) {
		this.service_start = service_start;
	}

	/**
	 * @return the service_end
	 */
	public Timestamp getService_end() {
		return service_end;
	}

	/**
	 * @param service_end the service_end to set
	 */
	public void setService_end(Timestamp service_end) {
		this.service_end = service_end;
	}
	
}
