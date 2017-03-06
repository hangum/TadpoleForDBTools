package com.hangum.tadpole.engine.sql.parser.dto;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;

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
