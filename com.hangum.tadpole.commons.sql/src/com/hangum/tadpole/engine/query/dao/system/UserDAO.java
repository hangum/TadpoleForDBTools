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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.DateUtil;

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
	Timestamp changed_passwd_time = new Timestamp(System.currentTimeMillis());
	String role_type;
	String name;
	String language = "";
	String timezone = "";
	String delYn = "";
	String create_time;
	String approval_yn;
	
	String use_otp = "";
	String otp_secret;
	
	String allow_ip = "*";

	/** 디비등록 가능 여부 */
	String is_regist_db = PublicTadpoleDefine.YES_NO.YES.name();
	/** 공유 할수 있는지 여부 */
	String is_shared_db = PublicTadpoleDefine.YES_NO.YES.name();
	/** 디비 추가 할 수있는 한계 */
	int limit_add_db_cnt = 5;
	/** 쿼리와 관련된 부분의 프리퍼런스 수정할 수 있는지? */
	String is_modify_perference = PublicTadpoleDefine.YES_NO.YES.name();
	
	Timestamp service_start = new Timestamp(System.currentTimeMillis());
	Timestamp service_end = new Timestamp(DateUtil.afterMonthToMillis(12));
	
	String external_id;
	
	// 
	// table viewer 에서 사용자 검색에서 사용하려고 채크 박스 선택 유무로 사용하는 컬럼.
	// 
	boolean select;

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

	public Timestamp getChanged_passwd_time() {
		return changed_passwd_time;
	}

	public void setChanged_passwd_time(Timestamp changed_passwd_time) {
		this.changed_passwd_time = changed_passwd_time;
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

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}
	
	public  boolean equals(UserDAO obj) {
        return (this.name.equals(obj.name) && this.email.equals(obj.email));
    }

	/**
	 * @return the is_modify_perference
	 */
	public String getIs_modify_perference() {
		return is_modify_perference;
	}

	/**
	 * @param is_modify_perference the is_modify_perference to set
	 */
	public void setIs_modify_perference(String is_modify_perference) {
		this.is_modify_perference = is_modify_perference;
	}

	/**
	 * @return the external_id
	 */
	public String getExternal_id() {
		return external_id;
	}

	/**
	 * @param external_id the external_id to set
	 */
	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}
	
}
