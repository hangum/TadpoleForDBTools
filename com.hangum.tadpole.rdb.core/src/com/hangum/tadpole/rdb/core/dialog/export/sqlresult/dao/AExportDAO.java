package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao;

/**
 * result set to export dao
 * 
 * @author hangum
 *
 */
public abstract class AExportDAO {
	protected String targetName;
	protected String comboEncoding;

	public AExportDAO() {
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	/**
	 * @return the comboEncoding
	 */
	public String getComboEncoding() {
		return comboEncoding;
	}

	/**
	 * @param comboEncoding the comboEncoding to set
	 */
	public void setComboEncoding(String comboEncoding) {
		this.comboEncoding = comboEncoding;
	}
	
	
}
