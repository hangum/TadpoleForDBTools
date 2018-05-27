/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao;

/**
 * text export dao
 * 
 * @author hangum
 *
 */
public class ExportJsonDAO extends AbstractExportDAO {
	/** Is include header */
	protected boolean isncludeHeader;
	/** schema key */
	protected String schemeKey;
	/** record key */
	protected String recordKey;
	/**  */
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
