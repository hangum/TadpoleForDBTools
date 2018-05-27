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
import java.sql.Timestamp;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * transaction dao
 * 
 * @author hangum
 *
 */
public class TransactionDAO {
	
	private String connectId;
	private String userId;
	private UserDBDAO userDB;
	private Timestamp startTransaction;
	private String key;

	private Connection conn;
	private boolean isTransaction;
	
	public TransactionDAO() {
	}
	
	/**
	 * @return the connectId
	 */
	public String getConnectId() {
		return connectId;
	}

	/**
	 * @param connectId the connectId to set
	 */
	public void setConnectId(String connectId) {
		this.connectId = connectId;
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
	
	/**
	 * @return the startTransaction
	 */
	public Timestamp getStartTransaction() {
		return startTransaction;
	}

	/**
	 * @param startTransaction the startTransaction to set
	 */
	public void setStartTransaction(Timestamp startTransaction) {
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
