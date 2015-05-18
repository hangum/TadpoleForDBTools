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
package com.hangum.tadpole.engine.manager;

/**
 * dbcp info
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 18.
 *
 */
public class DBCPInfoDAO {
	int dbSeq;
	String displayName;
	String dbType;
	
	int numberActive;
	int maxActive;
	int numberIdle;
	long maxWait;

	/**
	 * 
	 */
	public DBCPInfoDAO() {
	}
	
	/**
	 * @return the dbSeq
	 */
	public int getDbSeq() {
		return dbSeq;
	}


	/**
	 * @param dbSeq the dbSeq to set
	 */
	public void setDbSeq(int dbSeq) {
		this.dbSeq = dbSeq;
	}


	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}


	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}


	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}


	/**
	 * @return the numberActive
	 */
	public int getNumberActive() {
		return numberActive;
	}

	/**
	 * @param numberActive the numberActive to set
	 */
	public void setNumberActive(int numberActive) {
		this.numberActive = numberActive;
	}

	/**
	 * @return the maxActive
	 */
	public int getMaxActive() {
		return maxActive;
	}

	/**
	 * @param maxActive the maxActive to set
	 */
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * @return the numberIdle
	 */
	public int getNumberIdle() {
		return numberIdle;
	}

	/**
	 * @param numberIdle the numberIdle to set
	 */
	public void setNumberIdle(int numberIdle) {
		this.numberIdle = numberIdle;
	}

	/**
	 * @return the maxWait
	 */
	public long getMaxWait() {
		return maxWait;
	}

	/**
	 * @param maxWait the maxWait to set
	 */
	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

}
