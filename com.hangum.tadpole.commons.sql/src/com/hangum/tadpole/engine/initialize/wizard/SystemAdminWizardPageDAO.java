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
package com.hangum.tadpole.engine.initialize.wizard;


/**
 * System admin wizard page dao
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 3. 19.
 *
 */
public class SystemAdminWizardPageDAO {
	
	// user info
	private String email;
	private String passwd;
	private String rePasswd;
	
	// smtp info
	private String sMTPServer;
	private String port;
	private String sMTPEmail;
	private String sMTPPasswd;
	
	/**
	 * 
	 */
	public SystemAdminWizardPageDAO() {
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return the rePasswd
	 */
	public String getRePasswd() {
		return rePasswd;
	}

	/**
	 * @param rePasswd the rePasswd to set
	 */
	public void setRePasswd(String rePasswd) {
		this.rePasswd = rePasswd;
	}

	/**
	 * @return the sMTPServer
	 */
	public String getsMTPServer() {
		return sMTPServer;
	}

	/**
	 * @param sMTPServer the sMTPServer to set
	 */
	public void setsMTPServer(String sMTPServer) {
		this.sMTPServer = sMTPServer;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the sMTPEmail
	 */
	public String getsMTPEmail() {
		return sMTPEmail;
	}

	/**
	 * @param sMTPEmail the sMTPEmail to set
	 */
	public void setsMTPEmail(String sMTPEmail) {
		this.sMTPEmail = sMTPEmail;
	}

	/**
	 * @return the sMTPPasswd
	 */
	public String getsMTPPasswd() {
		return sMTPPasswd;
	}

	/**
	 * @param sMTPPasswd the sMTPPasswd to set
	 */
	public void setsMTPPasswd(String sMTPPasswd) {
		this.sMTPPasswd = sMTPPasswd;
	}

	
}
