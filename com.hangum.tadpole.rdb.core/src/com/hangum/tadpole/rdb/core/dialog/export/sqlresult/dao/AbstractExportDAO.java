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
 * result set to export dao
 * 
 * @author hangum
 *
 */
public abstract class AbstractExportDAO {
	/** file name or table name*/
	protected String targetName;
	/** set codeing type */
	protected String comboEncoding;

	public AbstractExportDAO() {
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
