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
 * user 정보 정의
 * 
 * @author hangum
 *
 */
public class UserDAO {
	int seq;
	String email;
	String passwd;
	String name;
	String language;
	String delYn;
	String create_time;
	String approval_yn;
	
	public UserDAO() {
	}
	
	public UserDAO(String email, String passwd, String name, String language, String approval_yn) {
		this.email = email;
		this.passwd = passwd;
		this.name = name;
		this.language = language;
		this.approval_yn = approval_yn;
	}

	public UserDAO(String email, String passwd) {
		this.email = email;
		this.passwd = passwd;
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
	 * @param language the language to set
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
	 * @param approval_yn the approval_yn to set
	 */
	public void setApproval_yn(String approval_yn) {
		this.approval_yn = approval_yn;
	}
	
}
