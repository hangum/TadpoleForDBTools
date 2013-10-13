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
package com.hangum.tadpole.sql.dao.system.ext.aws.rds;

import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * AmazonRDS 를 사용하기위한 DAO
 * 
 * @author hangum
 *
 */
public class AWSRDSUserDBDAO extends UserDBDAO {
	
	String accessKey = "";
	String secretKey = "";
	String endPoint = "";
	
	String strCheck = "";
	String strOperationType = "";
	String strGroup = "";
	String strName = "";
	
	public AWSRDSUserDBDAO() {
		super();
	}
	
	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey the accessKey to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}



	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}



	/**
	 * @return the endPoint
	 */
	public String getEndPoint() {
		return endPoint;
	}



	/**
	 * @param endPoint the endPoint to set
	 */
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}



	/**
	 * @return the strCheck
	 */
	public String getStrCheck() {
		return strCheck;
	}

	/**
	 * @param strCheck the strCheck to set
	 */
	public void setStrCheck(String strCheck) {
		this.strCheck = strCheck;
	}

	/**
	 * @return the strGroup
	 */
	public String getStrGroup() {
		return strGroup;
	}

	/**
	 * @param strGroup the strGroup to set
	 */
	public void setStrGroup(String strGroup) {
		this.strGroup = strGroup;
	}

	/**
	 * @return the strOperationType
	 */
	public String getStrOperationType() {
		return strOperationType;
	}

	/**
	 * @param strOperationType the strOperationType to set
	 */
	public void setStrOperationType(String strOperationType) {
		this.strOperationType = strOperationType;
	}

	/**
	 * @return the strName
	 */
	public String getStrName() {
		return strName;
	}

	/**
	 * @param strName the strName to set
	 */
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	
}
