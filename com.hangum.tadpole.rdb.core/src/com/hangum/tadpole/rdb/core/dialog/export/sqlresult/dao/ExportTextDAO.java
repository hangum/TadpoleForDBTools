package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao;

/**
 * text export dao
 * 
 * @author hangum
 *
 */
public class ExportTextDAO extends AExportDAO {
	protected boolean isncludeHeader;
	String separatorType;
	
	
	public ExportTextDAO() {
		super();
	}

	/**
	 * @return the separatorType
	 */
	public String getSeparatorType() {
		return separatorType;
	}

	/**
	 * @param separatorType the separatorType to set
	 */
	public void setSeparatorType(String separatorType) {
		this.separatorType = separatorType;
	}
	

	/**
	 * @return the isncludeHeader
	 */
	public boolean isIsncludeHeader() {
		return isncludeHeader;
	}

	/**
	 * @param isncludeHeader the isncludeHeader to set
	 */
	public void setIsncludeHeader(boolean isncludeHeader) {
		this.isncludeHeader = isncludeHeader;
	}
	
}
