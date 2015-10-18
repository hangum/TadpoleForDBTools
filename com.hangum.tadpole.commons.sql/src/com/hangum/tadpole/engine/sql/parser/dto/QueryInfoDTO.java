/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.parser.dto;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_STATUS;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_TYPE;

/**
 * Query information
 * 
 * @author hangum
 *
 */
public class QueryInfoDTO {
	
	/** is statement */
	private boolean isStatement = false;
	
	/** SQL TYPE  */
	private SQL_TYPE sqlType = SQL_TYPE.DML;

	/** query type */
	private QUERY_DML_TYPE queryType = QUERY_DML_TYPE.SELECT;
	
	/** query ddl type */
	private QUERY_DDL_TYPE queryDDLType = QUERY_DDL_TYPE.TABLE;
	/** if ddl type is name of object */
	private String objectName = "";
	/** DDL 일 경우 쿼리의 상태 */
	private QUERY_DDL_STATUS queryStatus = QUERY_DDL_STATUS.CREATE;
	
	public QueryInfoDTO() {}

	/**
	 * @return the isStatement
	 */
	public boolean isStatement() {
		return isStatement;
	}

	/**
	 * @param isStatement the isStatement to set
	 */
	public void setStatement(boolean isStatement) {
		this.isStatement = isStatement;
	}

	/**
	 * @return the sqlType
	 */
	public PublicTadpoleDefine.SQL_TYPE getSqlType() {
		return sqlType;
	}

	/**
	 * @param sqlType the sqlType to set
	 */
	public void setSqlType(PublicTadpoleDefine.SQL_TYPE sqlType) {
		this.sqlType = sqlType;
	}

	/**
	 * @return the queryType
	 */
	public PublicTadpoleDefine.QUERY_DML_TYPE getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(PublicTadpoleDefine.QUERY_DML_TYPE queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the queryDDLType
	 */
	public PublicTadpoleDefine.QUERY_DDL_TYPE getQueryDDLType() {
		return queryDDLType;
	}

	/**
	 * @param queryDDLType the queryDDLType to set
	 */
	public void setQueryDDLType(PublicTadpoleDefine.QUERY_DDL_TYPE queryDDLType) {
		this.queryDDLType = queryDDLType;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the queryStatus
	 */
	public QUERY_DDL_STATUS getQueryStatus() {
		return queryStatus;
	}

	/**
	 * @param queryStatus the queryStatus to set
	 */
	public void setQueryStatus(QUERY_DDL_STATUS queryStatus) {
		this.queryStatus = queryStatus;
	}

}
