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
package com.hangum.tadpole.rdb.core.dialog.table;

import com.hangum.tadpole.engine.define.DBDefine;

/**
 * alter table dao
 * 
 * @author hangum
 *
 */
public class AlterTableMetaDataDAO implements Cloneable {

	private String original_columnName;
	private String columnName;
	private boolean primaryKey;
	private int dataType;
	private String dataTypeName;
	
	private int dataSize;
//	private int dataPrecision;
//	private int dataScale;
	
	private String defaultValue;
	private boolean nullable;
	
//	/** 데이터 수정시 수정된 sql 문을 기록하고 있습니다. */
//	private DataTypeDef.DATA_TYPE dataStatus 	= DATA_TYPE.NONE;
	
	/* RDBMS종류 */
	private DBDefine dbdef;
	/* 데이터 사이즈 사용여부 - CREATE, ALTER할때 컬럼의 데이터 타입에 자료사이즈를 명시적으로 지정해야 하는지 여부. */
	private boolean useSize;
	private boolean usePrecision;
	private String comment;
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the original_columnName
	 */
	public String getOriginal_columnName() {
		return original_columnName;
	}

	/**
	 * @param original_columnName the original_columnName to set
	 */
	public void setOriginal_columnName(String original_columnName) {
		this.original_columnName = original_columnName;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

//	public int getDataPrecision() {
//		return dataPrecision;
//	}
//
//	public void setDataPrecision(int dataPrecision) {
//		this.dataPrecision = dataPrecision;
//	}
//
//	public int getDataScale() {
//		return dataScale;
//	}
//
//	public void setDataScale(int dataScale) {
//		this.dataScale = dataScale;
//	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

//	public int getSeqNo() {
//		return seqNo;
//	}
//
//	public void setSeqNo(int seqNo) {
//		this.seqNo = seqNo;
//	}

	public String getDataTypeName() {
		return dataTypeName;
	}

	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

	public DBDefine getDbdef() {
		return dbdef;
	}

	public void setDbdef(DBDefine dbdef) {
		this.dbdef = dbdef;
	}

	public boolean isUseSize() {
		return useSize;
	}

	public void setUseSize(boolean useSize) {
		this.useSize = useSize;
	}

	public boolean isUsePrecision() {
		return usePrecision;
	}

	public void setUsePrecision(boolean usePrecision) {
		this.usePrecision = usePrecision;
	}
	
//	/**
//	 * @return the dataStatus
//	 */
//	public DataTypeDef.DATA_TYPE getDataStatus() {
//		return dataStatus;
//	}
//
//	/**
//	 * @param dataStatus the dataStatus to set
//	 */
//	public void setDataStatus(DataTypeDef.DATA_TYPE dataStatus) {
//		this.dataStatus = dataStatus;
//	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
