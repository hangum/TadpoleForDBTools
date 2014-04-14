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
package com.hangum.tadpole.engine.manager.transaction;

import java.sql.Connection;
import java.sql.Date;

import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * transaction dao
 * 
 * @author hangum
 *
 */
public class TransactionDAO {
	
	private String userId;
	private UserDBDAO userDB;
	private Date startTransaction;
	private String key;

	private Connection conn;
	private boolean isTransaction;
	
	public TransactionDAO() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}
	
	public Date getStartTransaction() {
		return startTransaction;
	}

	public void setStartTransaction(Date startTransaction) {
		this.startTransaction = startTransaction;
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * @param conn the conn to set
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @return the isTransaction
	 */
	public boolean isTransaction() {
		return isTransaction;
	}

	/**
	 * @param isTransaction the isTransaction to set
	 */
	public void setTransaction(boolean isTransaction) {
		this.isTransaction = isTransaction;
	}

	
	
}
