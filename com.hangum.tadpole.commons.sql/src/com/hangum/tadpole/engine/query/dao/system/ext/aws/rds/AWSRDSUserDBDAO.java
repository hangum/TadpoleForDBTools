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
package com.hangum.tadpole.engine.query.dao.system.ext.aws.rds;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * AmazonRDS 를 사용하기위한 DAO
 * 
 * @author hangum
 *
 */
public class AWSRDSUserDBDAO extends UserDBDAO {
	String userTadpoleDisplayName = "";
	
	String endPoint 	= "";
	
	String strCheck 	= "";
	String strOperationType = "";
	String strGroup 	= "";
	String strName 	= "";
	
	public AWSRDSUserDBDAO() {
		super();
	}
	
	/**
	 * @return the userTadpoleDisplayName
	 */
	public String getUserTadpoleDisplayName() {
		return userTadpoleDisplayName;
	}

	/**
	 * @param userTadpoleDisplayName the userTadpoleDisplayName to set
	 */
	public void setUserTadpoleDisplayName(String userTadpoleDisplayName) {
		this.userTadpoleDisplayName = userTadpoleDisplayName;
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
