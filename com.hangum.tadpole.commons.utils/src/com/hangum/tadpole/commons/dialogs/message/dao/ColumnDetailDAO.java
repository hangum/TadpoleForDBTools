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
package com.hangum.tadpole.commons.dialogs.message.dao;

/**
 * 쿼리의 실제 컬럼 정보를 담아 놓습니다.
 * 
 * @author hangum
 *
 */
public class ColumnDetailDAO {
	String column = "";
	String aliasColumn = "";
	String table = "";
	
	public ColumnDetailDAO() {
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * @return the aliasColumn
	 */
	public String getAliasColumn() {
		return aliasColumn;
	}

	/**
	 * @param aliasColumn the aliasColumn to set
	 */
	public void setAliasColumn(String aliasColumn) {
		this.aliasColumn = aliasColumn;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}
	
}
