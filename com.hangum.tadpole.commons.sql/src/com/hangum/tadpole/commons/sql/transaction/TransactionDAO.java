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
package com.hangum.tadpole.commons.sql.transaction;

import java.sql.Connection;

/**
 * transaction dao
 * 
 * @author hangum
 *
 */
public class TransactionDAO {

	private Connection conn;
	private boolean isTransaction;
	
	public TransactionDAO() {
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
