/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.parser.dto;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;

/**
 * Query dml define
 * 
 * @author hangum
 *
 */
public class QueryDMLInfoDTO {
	private QUERY_DML_TYPE dmlType;
	private String objectName;
	public QueryDMLInfoDTO() {
	}
	public QUERY_DML_TYPE getDmlType() {
		return dmlType;
	}
	public void setDmlType(QUERY_DML_TYPE dmlType) {
		this.dmlType = dmlType;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	
}
