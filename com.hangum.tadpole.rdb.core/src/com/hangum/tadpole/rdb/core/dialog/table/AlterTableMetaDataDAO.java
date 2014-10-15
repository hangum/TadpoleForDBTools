package com.hangum.tadpole.rdb.core.dialog.table;

import com.hangum.tadpole.engine.define.DBDefine;

public class AlterTableMetaDataDAO {

	private int seqNo;
	private String columnName;
	private int columnId;
	private boolean primaryKey;
	private int dataType;
	private String dataTypeName;
	private int dataSize;
	private int dataPrecision;
	private int dataScale;
	private String defaultValue;
	private boolean nullable;
	
	/* RDBMS종류 */
	private DBDefine dbdef;
	/* 데이터 사이즈 사용여부 - CREATE, ALTER할때 컬럼의 데이터 타입에 자료사이즈를 명시적으로 지정해야 하는지 여부. */
	private boolean useSize;
	private boolean usePrecision;
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
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

	public int getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision(int dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public int getDataScale() {
		return dataScale;
	}

	public void setDataScale(int dataScale) {
		this.dataScale = dataScale;
	}

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

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

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

}
