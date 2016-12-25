/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.mails.dto;

/**
 * 메일 로그인 {@code PublicTadpoleDefine#MAIL_TYPE} (NONE, SENDGRID, SMTP)과 정보를 정의합니다. 
 * 
 * @author hangum
 *
 */
public class SMTPDTO {
	/** 로그인 타입 비정 {@code PublicTadpoleDefine#MAIL_TYPE} */
	String loginMethodType = "";
	
	String sendgrid_api = "";
	
	String starttls_enable;
	String isAuth;
	String host = "";
	String port = "";
	String email = "";
	String passwd = "";
	
	/** ldap domain에서 설정한 이름 */
	String domain = "";
	
	public SMTPDTO() {
	}

	/**
	 * @return the loginMethodType
	 */
	public String getLoginMethodType() {
		return loginMethodType;
	}

	/**
	 * @param loginMethodType the loginMethodType to set
	 */
	public void setLoginMethodType(String loginMethodType) {
		this.loginMethodType = loginMethodType;
	}

	/**
	 * is smtp valid
	 * @return
	 */
	public boolean isSMTPValid() {
		if("".equals(getEmail())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * is send grid valid
	 * @return
	 */
	public boolean isSendGridValid() {
		if("".equals(getSendgrid_api())) return true;
		else return false;
	}
	
	/**
	 * @return the sendgrid_api
	 */
	public String getSendgrid_api() {
		return sendgrid_api;
	}

	/**
	 * @param sendgrid_api the sendgrid_api to set
	 */
	public void setSendgrid_api(String sendgrid_api) {
		this.sendgrid_api = sendgrid_api;
	}
	
	/**
	 * @return the starttls_enable
	 */
	public String getStarttls_enable() {
		return starttls_enable;
	}

	/**
	 * @param starttls_enable the starttls_enable to set
	 */
	public void setStarttls_enable(String starttls_enable) {
		this.starttls_enable = starttls_enable;
	}

	/**
	 * @return the isAuth
	 */
	public String getIsAuth() {
		return isAuth;
	}

	/**
	 * @param isAuth the isAuth to set
	 */
	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	/**
	 * @return the host
	 */
	public final String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public final void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public final String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public final void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the email
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public final void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the passwd
	 */
	public final String getPasswd() {
		return passwd;
	}

	/**
	 * @param passwd the passwd to set
	 */
	public final void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SMTPDTO [loginMethodType=" + loginMethodType + ", sendgrid_api=" + sendgrid_api + ", starttls_enable="
				+ starttls_enable + ", isAuth=" + isAuth + ", host=" + host + ", port=" + port + ", email=" + email
				+ "]";
	}

	
}
