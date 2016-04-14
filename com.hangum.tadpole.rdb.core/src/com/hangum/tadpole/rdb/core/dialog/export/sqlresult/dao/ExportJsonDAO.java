package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao;

/**
 * text export dao
 * 
 * @author hangum
 *
 */
public class ExportJsonDAO extends AExportDAO {
	protected boolean isncludeHeader;
	protected String schemeKey;
	protected String recordKey;
	protected boolean isFormat;

	public ExportJsonDAO() {
		super();
	}

	/**
	 * @return the separatorType
	 */
	public String getSchemeKey() {
		return schemeKey;
	}

	/**
	 * @param separatorType
	 *            the separatorType to set
	 */
	public void setSchemeKey(String schemeKey) {
		this.schemeKey = schemeKey;
	}

	/**
	 * 
	 * @return
	 */
	public String getRecordKey() {
		return recordKey;
	}

	/**
	 * 
	 * @param recordKey
	 */
	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}

	/**
	 * @return the isncludeHeader
	 */
	public boolean isIsncludeHeader() {
		return isncludeHeader;
	}

	/**
	 * @param isncludeHeader
	 *            the isncludeHeader to set
	 */
	public void setIsncludeHeader(boolean isncludeHeader) {
		this.isncludeHeader = isncludeHeader;
	}

	public boolean isFormat() {
		return isFormat;
	}

	public void setFormat(boolean isFormat) {
		this.isFormat = isFormat;
	}

}
