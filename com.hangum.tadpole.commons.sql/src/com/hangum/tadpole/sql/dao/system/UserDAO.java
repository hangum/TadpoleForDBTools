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
package com.hangum.tadpole.sql.dao.system;

/**
 * user 정보 정의
 * 
 * @author hangum
 * 
 */
public class UserDAO {
	int seq;
	String email;
	String email_key;
	String passwd;
	String role_type;
	String name;
	String language;
	String delYn;
	String create_time;
	String approval_yn;
	
	String use_otp;
	String otp_secret;
	
	/** tadpole_user_db_role column */
	String role_id;

	public UserDAO() {
	}

	public UserDAO(String email, String name, String language, String approval_yn, String use_opt, String otp_secret) {
		this.email = email;
		this.name = name;
		this.language = language;
		this.approval_yn = approval_yn;
		this.use_otp = use_opt;
		this.otp_secret = otp_secret;
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

}
