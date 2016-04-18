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
public class ExportTextDAO extends AbstractExportDAO {
	protected boolean isncludeHeader;
	char separatorType;
	
	
	public ExportTextDAO() {
		super();
	}

	/**
	 * @return the separatorType
	 */
	public char getSeparatorType() {
		return separatorType;
	}

	/**
	 * @param separatorType the separatorType to set
	 */
	public void setSeparatorType(char separatorType) {
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
