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
 * SMTP DTO
 * 
 * @author hangum
 *
 */
public class SMTPDTO {
	String sendgrid_api = "";
	String host = "";
	String port = "";
	String email = "";
	String passwd = "";
	
	public SMTPDTO() {
	}
	
	public boolean isValid() {
		if("".equals(getSendgrid_api())) {
			if("".equals(getEmail()) || "".equals(getPasswd())) {
				return false;
			}
		}
		
		return true;
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
	
	
}
